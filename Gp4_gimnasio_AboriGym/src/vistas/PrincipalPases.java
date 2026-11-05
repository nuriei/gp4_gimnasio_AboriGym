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
        btnCancelar.addActionListener(e -> cancelar());
        btnModificar.addActionListener(e -> modificar());
        btnAlta.addActionListener(e -> alta());
        btnBaja.addActionListener(e -> baja());
        btnEliminar.addActionListener(e -> eliminar());
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
 
    private void eliminar() {
        int id = idFilaSeleccionada();
        if (id == -1) {
            return;
        }
        if (confirmar("Confirmación", "¿Seguro que desea eliminar el pase permanentemente de la base de datos?",
                JOptionPane.WARNING_MESSAGE, 1)) {
            paseData.borrarPase(id);
            cargarPases();
            JOptionPane.showMessageDialog(this, "Se borró el pase.");
        }
    }
 
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
        btnEliminar.setEnabled(seleccionado);
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
 
//    java.net.URL rutaImagen = getClass().getClassLoader().getResource("imagenes/fondo.jpg");
//    if (rutaImagen != null) {
//        ImageIcon iconoOriginal = new ImageIcon(rutaImagen);
//        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(490, 402, Image.SCALE_SMOOTH);
//        labelFondo.setIcon(new ImageIcon(imagenEscalada));
//        labelFondo.setBounds(0, 0, 490, 402);
//        this.getContentPane().setComponentZOrder(labelFondo, this.getContentPane().getComponentCount() - 1);
//        labelFondo.setEnabled(false);
//        
//    }
//} catch (Exception e) {
//    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_volver = new javax.swing.JButton();
        btnBuscarSocio = new javax.swing.JButton();
        jdcFechaInicio = new com.toedter.calendar.JDateChooser();
        jdcFechaFin = new com.toedter.calendar.JDateChooser();
        cmbEntradas = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtDniSocio = new javax.swing.JTextField();
        txtSocio = new javax.swing.JTextField();
        txtCosto = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaPases = new javax.swing.JTable();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnAlta = new javax.swing.JButton();
        btnBaja = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        checkEstado = new javax.swing.JCheckBox();

        setTitle("Gestion de Pases");

        btn_volver.setText("Volver");
        btn_volver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_volverActionPerformed(evt);
            }
        });

        btnBuscarSocio.setText("Buscar Socio");
        btnBuscarSocio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarSocioActionPerformed(evt);
            }
        });

        jdcFechaFin.setEnabled(false);

        jLabel1.setText("DNI del Socio");

        jLabel2.setText("Socio");

        jLabel3.setText("Fecha Inicio");

        jLabel4.setText("Fecha Fin");

        jLabel5.setText("Costo");

        jLabel6.setText("Activo");

        jLabel7.setText("Entradas");

        txtSocio.setEditable(false);

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

        btnNuevo.setText("Nuevo");

        btnGuardar.setText("Guardar");

        btnCancelar.setText("Cancelar");

        btnModificar.setText("Modificar");

        btnAlta.setText("Alta");

        btnBaja.setText("Baja");

        btnEliminar.setText("Eliminar");

        checkEstado.setSelected(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnModificar)
                        .addGap(18, 18, 18)
                        .addComponent(btnAlta)
                        .addGap(18, 18, 18)
                        .addComponent(btnBaja)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar))
                    .addComponent(btnNuevo)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(txtDniSocio, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnBuscarSocio))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jdcFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jdcFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(checkEstado)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtSocio, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(cmbEntradas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addGap(48, 48, 48)
                        .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(197, 197, 197)
                        .addComponent(btnCancelar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addComponent(btnGuardar)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_volver)
                .addGap(48, 48, 48))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(204, 204, 204)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnBuscarSocio)
                            .addComponent(jLabel1)
                            .addComponent(txtDniSocio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbEntradas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel7)
                            .addComponent(txtSocio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jdcFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jdcFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(checkEstado))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModificar)
                    .addComponent(btnAlta)
                    .addComponent(btnBaja)
                    .addComponent(btnEliminar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(btn_volver)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_volverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_volverActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btn_volverActionPerformed

    private void btnBuscarSocioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarSocioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarSocioActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlta;
    private javax.swing.JButton btnBaja;
    private javax.swing.JButton btnBuscarSocio;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btn_volver;
    private javax.swing.JCheckBox checkEstado;
    private javax.swing.JComboBox<String> cmbEntradas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcFechaFin;
    private com.toedter.calendar.JDateChooser jdcFechaInicio;
    private javax.swing.JTable tablaPases;
    private javax.swing.JTextField txtCosto;
    private javax.swing.JTextField txtDniSocio;
    private javax.swing.JTextField txtSocio;
    // End of variables declaration//GEN-END:variables
}
