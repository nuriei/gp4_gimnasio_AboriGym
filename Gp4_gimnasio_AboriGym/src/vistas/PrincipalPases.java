/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package vistas;

import entidades.Pases;
import entidades.Socio;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import persistencia.MiConexion;
import persistencia.PaseData;
import persistencia.SocioData;

/**
 *
 * @author Nuri
 */
public class PrincipalPases extends javax.swing.JInternalFrame {

   
    private PaseData paseData;
    private SocioData socioData;
    private DefaultTableModel modeloTabla;
    private boolean editando = false;
    private int idPaseActual = -1;
 
    public PrincipalPases(MiConexion conexion) {
        initComponents();
     paseData = new PaseData(conexion);
        socioData = new SocioData(conexion);
        modeloTabla = (DefaultTableModel) tablaPases.getModel();
 
        cmbEntradas.setModel(new DefaultComboBoxModel<>(new String[]{"8", "12", "20"}));
        txtSocio.setEditable(false);          // lo completa el botón Buscar socio
        jdcFechaFin.setEnabled(false);        // se calcula sola (inicio + 30 días)
        tablaPases.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
 
        // Conexión de eventos
        btnBuscarSocio.addActionListener(e -> buscarSocio());
        btnNuevo.addActionListener(e -> nuevo());
        btnGuardar.addActionListener(e -> guardar());
        
        btnModificar.addActionListener(e -> modificar());
        btnAlta.addActionListener(e -> alta());
        btnBaja.addActionListener(e -> baja());
        
        btn_volver.addActionListener(e -> dispose());
        jdcFechaInicio.addPropertyChangeListener("date", e -> actualizarFechaFin());
        tablaPases.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarBotones();
            }
        });
 
        limpiarFormulario();
        cargarPases();
        actualizarBotones();
    }
     public PrincipalPases() {                       // constructor nuevo, sin parámetros
        this(new MiConexion("jdbc:mysql://localhost:3306/gp4_gimnasio_aborigym", "root", ""));
    }
 
 
// ===== 3) MÉTODOS: pegalos antes de la línea "// Variables declaration - do not modify" =====
 
    // ---------------- BUSCAR SOCIO ----------------
    private void buscarSocio() {
        String dni = txtDniSocio.getText().trim();
        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el DNI del socio.");
            txtDniSocio.requestFocus();
            return;
        }
        Socio socio = socioData.buscarSocioPorDni(dni);
        if (socio == null) {
            JOptionPane.showMessageDialog(this, "No existe un socio con ese DNI.");
            txtSocio.setText("");
        } else {
            txtSocio.setText(socio.getApellido() + ", " + socio.getNombre());
        }
    }
 
    // ---------------- NUEVO ----------------
    private void nuevo() {
        tablaPases.clearSelection();
        limpiarFormulario();
        jdcFechaInicio.setDate(aDate(LocalDate.now()));   // por defecto, hoy
        txtDniSocio.requestFocus();
    }
 
    // ---------------- GUARDAR (alta o modificación) ----------------
    private void guardar() {
        java.util.Date d = jdcFechaInicio.getDate();
        if (d == null) {
            JOptionPane.showMessageDialog(this, "Seleccione la fecha de inicio.");
            return;
        }
        LocalDate inicio = aLocalDate(d);
 
        double costo;
        try {
            costo = Double.parseDouble(txtCosto.getText().trim());
            if (costo <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un costo numérico mayor a 0.");
            txtCosto.requestFocus();
            return;
        }
 
        if (editando) {
            // Las entradas no se editan a mano: se consumen con las asistencias.
            Pases p = paseData.buscarPasePorId(idPaseActual);
            p.setFechaInicio(inicio);
            p.setFechaFin(inicio.plusDays(Pases.DIAS_VALIDEZ));
            p.setCosto(costo);
            p.setEstado(checkEstado.isSelected());
            paseData.modificarPase(p);
            JOptionPane.showMessageDialog(this, "Pase modificado correctamente.");
        } else {
            String dni = txtDniSocio.getText().trim();
            if (dni.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese el DNI del socio.");
                txtDniSocio.requestFocus();
                return;
            }
            Socio socio = socioData.buscarSocioPorDni(dni);
            if (socio == null) {
                JOptionPane.showMessageDialog(this, "No existe un socio con ese DNI.");
                txtDniSocio.requestFocus();
                return;
            }
            if (!socio.isEstado()) {
                JOptionPane.showMessageDialog(this, "El socio está dado de baja. No se le puede asignar un pase.");
                return;
            }
 
            int entradas = Integer.parseInt(cmbEntradas.getSelectedItem().toString());
 
            // El constructor calcula la fechaFin (inicio + 30 días)
            Pases nuevo = new Pases(socio, entradas, inicio, costo, checkEstado.isSelected());
            paseData.guardarPase(nuevo);
 
            if (nuevo.getIdMembresia() == 0) {
                JOptionPane.showMessageDialog(this, "No se pudo guardar el pase. Revise la consola.");
                return;
            }
            JOptionPane.showMessageDialog(this, "Pase agregado correctamente.");
        }
 
        cargarPases();
        limpiarFormulario();
    }
 
    // ---------------- CANCELAR ----------------
    private void cancelar() {
        if (editando) {
            JOptionPane.showMessageDialog(this, "No se hicieron modificaciones en el pase.");
        } else {
            JOptionPane.showMessageDialog(this, "Se canceló el guardado del pase nuevo.");
        }
        tablaPases.clearSelection();
        limpiarFormulario();
    }
 
    // ---------------- MODIFICAR (carga la fila elegida en el formulario) ----------------
    private void modificar() {
        int fila = tablaPases.getSelectedRow();
        if (fila == -1) {
            return;
        }
        editando = true;
        idPaseActual = Integer.parseInt(tablaPases.getValueAt(fila, 0).toString());
 
        txtDniSocio.setText(tablaPases.getValueAt(fila, 1).toString());
        txtSocio.setText(tablaPases.getValueAt(fila, 2).toString());
        jdcFechaInicio.setDate(aDate(LocalDate.parse(tablaPases.getValueAt(fila, 4).toString())));
        txtCosto.setText(tablaPases.getValueAt(fila, 6).toString());
        checkEstado.setSelected(tablaPases.getValueAt(fila, 7).toString().equals("Activo"));
 
        // El socio y las entradas no se cambian desde acá
        txtDniSocio.setEnabled(false);
        btnBuscarSocio.setEnabled(false);
        cmbEntradas.setEnabled(false);
    }
 
    // ---------------- ALTA / BAJA / ELIMINAR ----------------
    private void alta() {
        int id = idFilaSeleccionada();
        if (id == -1) {
            return;
        }
        if (confirmar("Confirmar alta", "¿Desea dar de alta este pase?", JOptionPane.QUESTION_MESSAGE, 0)) {
            paseData.altaLogicaPase(id);
            cargarPases();
            JOptionPane.showMessageDialog(this, "Se dio de alta el pase.");
        }
    }
 
    private void baja() {
        int id = idFilaSeleccionada();
        if (id == -1) {
            return;
        }
        if (confirmar("Confirmar baja", "¿Desea dar de baja este pase?", JOptionPane.QUESTION_MESSAGE, 0)) {
            paseData.bajaLogicaPase(id);
            cargarPases();
            JOptionPane.showMessageDialog(this, "Se dio de baja el pase.");
        }
    }
 
//    private void eliminar() { //comente esta linea porue no usamos el eliminar
//        int id = idFilaSeleccionada();
//        if (id == -1) {
//            return;
//        }
//        if (confirmar("Confirmación", "¿Seguro que desea eliminar el pase permanentemente de la base de datos?",
//                JOptionPane.WARNING_MESSAGE, 1)) {
//            paseData.borrarPase(id);
//            cargarPases();
//            JOptionPane.showMessageDialog(this, "Se borró el pase.");
//        }
//    }
 
    // ---------------- MÉTODOS AUXILIARES ----------------
    private void actualizarFechaFin() {
        java.util.Date d = jdcFechaInicio.getDate();
        if (d == null) {
            jdcFechaFin.setDate(null);
        } else {
            jdcFechaFin.setDate(aDate(aLocalDate(d).plusDays(Pases.DIAS_VALIDEZ)));
        }
    }
 
    private void actualizarBotones() {
        int fila = tablaPases.getSelectedRow();
        boolean seleccionado = fila != -1;
 
        btnModificar.setEnabled(seleccionado);
        //btnEliminar.setEnabled(seleccionado);
        if (seleccionado) {
            String estado = tablaPases.getValueAt(fila, 7).toString();
            btnBaja.setEnabled(estado.equals("Activo"));
            btnAlta.setEnabled(estado.equals("Inactivo"));
        } else {
            btnBaja.setEnabled(false);
            btnAlta.setEnabled(false);
        }
    }
 
    private void limpiarFormulario() {
        editando = false;
        idPaseActual = -1;
        txtDniSocio.setText("");
        txtSocio.setText("");
        cmbEntradas.setSelectedIndex(0);
        jdcFechaInicio.setDate(null);
        jdcFechaFin.setDate(null);
        txtCosto.setText("");
        checkEstado.setSelected(true);
 
        txtDniSocio.setEnabled(true);
        btnBuscarSocio.setEnabled(true);
        cmbEntradas.setEnabled(true);
    }
 
    private int idFilaSeleccionada() {
        int fila = tablaPases.getSelectedRow();
        if (fila == -1) {
            return -1;
        }
        return Integer.parseInt(tablaPases.getValueAt(fila, 0).toString());
    }
 
    private boolean confirmar(String titulo, String mensaje, int tipoMensaje, int opcionPorDefecto) {
        Object[] opciones = {"Sí", "No"};
        int respuesta = JOptionPane.showOptionDialog(this, mensaje, titulo, JOptionPane.YES_NO_OPTION,
                tipoMensaje, null, opciones, opciones[opcionPorDefecto]);
        return respuesta == 0;
    }
 
    private void cargarPases() {
        modeloTabla.setRowCount(0);
        List<Pases> lista = paseData.listarPases();
        for (Pases p : lista) {
            modeloTabla.addRow(new Object[]{
                p.getIdMembresia(),
                p.getSocio().getDni(),
                p.getSocio().getApellido() + ", " + p.getSocio().getNombre(),
                p.getCantidadPases(),
                p.getFechaInicio(),
                p.getFechaFin(),
                p.getCosto(),
                p.isEstado() ? "Activo" : "Inactivo"
            });
        }
    }
 
    // JDateChooser trabaja con java.util.Date; la entidad usa LocalDate
    private LocalDate aLocalDate(java.util.Date d) {
        return new java.sql.Date(d.getTime()).toLocalDate();
    }
 
    private java.util.Date aDate(LocalDate fecha) {
        return java.util.Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
 
   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_volver = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        PanelBuscar = new javax.swing.JPanel();
        btnBuscarSocio = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jBLimpiar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jRBPasesActivos = new javax.swing.JRadioButton();
        jRBPasesInactivos = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        txtDniSocio = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        btnNuevo = new javax.swing.JButton();
        btnBaja = new javax.swing.JButton();
        btnAlta = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaPases = new javax.swing.JTable();
        panelFormulario = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtNombreSocio = new javax.swing.JTextField();
        txtApellidoSocio = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnCancelar1 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        checkEstado = new javax.swing.JCheckBox();
        jdcFechaInicio = new com.toedter.calendar.JDateChooser();
        txtCosto = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cmbEntradas = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        txtSocio = new javax.swing.JTextField();
        jdcFechaFin = new com.toedter.calendar.JDateChooser();

        setTitle("Gestion de Pases");

        btn_volver.setBackground(new java.awt.Color(153, 153, 153));
        btn_volver.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_volver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/volver.png"))); // NOI18N
        btn_volver.setText("Volver");
        btn_volver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_volverActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel11.setText("Gestion de Pases");

        PanelBuscar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnBuscarSocio.setBackground(new java.awt.Color(153, 153, 153));
        btnBuscarSocio.setText("Buscar");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setText("Buscar Pases:");

        jBLimpiar.setBackground(new java.awt.Color(153, 153, 153));
        jBLimpiar.setText("Limpiar");

        jLabel9.setText("Por DNI:");

        jRBPasesActivos.setText("Pases Activos");

        jRBPasesInactivos.setText("Pases Inactivos");

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/logo (3).png"))); // NOI18N
        jLabel10.setText("jLabel9");

        javax.swing.GroupLayout PanelBuscarLayout = new javax.swing.GroupLayout(PanelBuscar);
        PanelBuscar.setLayout(PanelBuscarLayout);
        PanelBuscarLayout.setHorizontalGroup(
            PanelBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBuscarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addGroup(PanelBuscarLayout.createSequentialGroup()
                        .addComponent(jRBPasesActivos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRBPasesInactivos))
                    .addGroup(PanelBuscarLayout.createSequentialGroup()
                        .addComponent(btnBuscarSocio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jBLimpiar))
                    .addComponent(txtDniSocio, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(80, 80, 80)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelBuscarLayout.setVerticalGroup(
            PanelBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelBuscarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
            .addGroup(PanelBuscarLayout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDniSocio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRBPasesActivos)
                    .addComponent(jRBPasesInactivos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscarSocio)
                    .addComponent(jBLimpiar))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setText("Listado de Pases:");

        btnNuevo.setBackground(new java.awt.Color(153, 153, 153));
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/+.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnBaja.setBackground(new java.awt.Color(153, 153, 153));
        btnBaja.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/baja.png"))); // NOI18N
        btnBaja.setText("Baja");
        btnBaja.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBajaActionPerformed(evt);
            }
        });

        btnAlta.setBackground(new java.awt.Color(153, 153, 153));
        btnAlta.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAlta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/alta.png"))); // NOI18N
        btnAlta.setText("Alta");
        btnAlta.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAlta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAltaActionPerformed(evt);
            }
        });

        btnModificar.setBackground(new java.awt.Color(153, 153, 153));
        btnModificar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        tablaPases.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "id ", "dni socio", "socio", "entradas", "inicio", "fin", "costo", "estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaPases);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAlta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBaja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnNuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBaja)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAlta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModificar)))
                .addContainerGap())
        );

        panelFormulario.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("Nombre Socio:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("Fecha Inicio:");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setText("Apellido Socio:");

        btnGuardar.setBackground(new java.awt.Color(153, 153, 153));
        btnGuardar.setText("Guardar");

        btnCancelar1.setBackground(new java.awt.Color(153, 153, 153));
        btnCancelar1.setText("Cancelar");
        btnCancelar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelar1ActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel17.setText("Registro de Pases:");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setText("Costo:");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setText("Fecha Fin:");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setText("Activo:");

        checkEstado.setSelected(true);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Entradas");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Socio ID:");

        txtSocio.setEditable(false);
        txtSocio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSocioActionPerformed(evt);
            }
        });

        jdcFechaFin.setEnabled(false);

        javax.swing.GroupLayout panelFormularioLayout = new javax.swing.GroupLayout(panelFormulario);
        panelFormulario.setLayout(panelFormularioLayout);
        panelFormularioLayout.setHorizontalGroup(
            panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormularioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel14)
                            .addComponent(jLabel20)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNombreSocio)
                            .addComponent(jdcFechaInicio, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                            .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(checkEstado)))
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel15)
                            .addComponent(jLabel7)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtApellidoSocio, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                            .addComponent(txtSocio)
                            .addComponent(jdcFechaFin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbEntradas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelFormularioLayout.setVerticalGroup(
            panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormularioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel2)
                    .addComponent(txtSocio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtNombreSocio)
                    .addComponent(jLabel15)
                    .addComponent(txtApellidoSocio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel14))
                    .addComponent(jdcFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jdcFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(cmbEntradas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCancelar1)
                        .addComponent(btnGuardar))
                    .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(checkEstado)
                        .addComponent(jLabel20)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(208, 208, 208)
                .addComponent(jLabel11)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelFormulario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_volver)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_volver)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_volverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_volverActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btn_volverActionPerformed

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
        panelFormulario.setVisible(false);
        txtNombreSocio.setText(null);
        txtCosto.setText(null);
        txtApellidoSocio.setText(null);
    }//GEN-LAST:event_btnCancelar1ActionPerformed

    private void txtSocioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSocioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSocioActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBajaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBajaActionPerformed

    private void btnAltaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAltaActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnModificarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelBuscar;
    private javax.swing.JButton btnAlta;
    private javax.swing.JButton btnBaja;
    private javax.swing.JButton btnBuscarSocio;
    private javax.swing.JButton btnCancelar1;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btn_volver;
    private javax.swing.JCheckBox checkEstado;
    private javax.swing.JComboBox<String> cmbEntradas;
    private javax.swing.JButton jBLimpiar;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRBPasesActivos;
    private javax.swing.JRadioButton jRBPasesInactivos;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcFechaFin;
    private com.toedter.calendar.JDateChooser jdcFechaInicio;
    private javax.swing.JPanel panelFormulario;
    private javax.swing.JTable tablaPases;
    private javax.swing.JTextField txtApellidoSocio;
    private javax.swing.JTextField txtCosto;
    private javax.swing.JTextField txtDniSocio;
    private javax.swing.JTextField txtNombreSocio;
    private javax.swing.JTextField txtSocio;
    // End of variables declaration//GEN-END:variables
}
