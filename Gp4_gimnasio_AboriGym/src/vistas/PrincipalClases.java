package vistas;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import entidades.Clases;
import entidades.Entrenadores;
import java.time.Instant;
import persistencia.ClaseData;
import persistencia.EntrenadorData;
import persistencia.MiConexion;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author david vera barz
 */
public class PrincipalClases extends javax.swing.JInternalFrame {

    private persistencia.ClaseData claseData;
    private DefaultTableModel modeloTabla;
    private boolean editando = false;
    private int idClaseActual = -1;

    public PrincipalClases() {
        initComponents();
        //agregado de configuracion parajspiner adatespiner
        //javax.swing.SpinnerDateModel modeloHora = new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.MINUTE);
        //JSHorario.setModel(modeloHora);
        //javax.swing.JSpinner.DateEditor editorHora = new javax.swing.JSpinner.DateEditor(JSHorario, "HH:mm");
        //JSHorario.setEditor(editorHora);
        java.util.Calendar horaInicial = java.util.Calendar.getInstance();
        horaInicial.set(java.util.Calendar.HOUR_OF_DAY, 8);
        horaInicial.set(java.util.Calendar.MINUTE, 0);
        horaInicial.set(java.util.Calendar.SECOND, 0);
        
        javax.swing.SpinnerDateModel modeloHora = new javax.swing.SpinnerDateModel(
        horaInicial.getTime(),null,null,java.util.Calendar.HOUR_OF_DAY) {
       @Override
       public Object getNextValue(){
       java.util.Calendar cal = java.util.Calendar.getInstance();
       cal.setTime((java.util.Date)getValue());
       cal.add(java.util.Calendar.HOUR_OF_DAY,1);
       return cal.getTime();
       }
       @Override
       public Object getPreviousValue(){
       java.util.Calendar cal = java.util.Calendar.getInstance();
       cal.setTime((java.util.Date)getValue());
       cal.add(java.util.Calendar.HOUR_OF_DAY,-1);
       return cal.getTime();
       }
        };
        JSHorario.setModel(modeloHora);
        javax.swing.JSpinner.DateEditor editorHora = new javax.swing.JSpinner.DateEditor(JSHorario, "HH:mm");
        JSHorario.setEditor(editorHora);
        editorHora.getTextField().setEditable(false);    
        //conexion
        persistencia.MiConexion conexion = new persistencia.MiConexion("jdbc:mysql://localhost:3306/gp4_gimnasio_aborigym", "root", "");
        claseData = new persistencia.ClaseData(conexion);

        editarEncabezado();
        cargarClases();
        LlenarComboEntrenadores();
        panelFormulario.setVisible(false);
        
         // empezamos aca para generar la activacion de los botones baja alta y borrar segun tipo de clase
         
        jbModificar.setEnabled(false);
        jBBaja.setEnabled(false);
        jBAlta.setEnabled(false);
        jbEliminar.setEnabled(false);
         // aca se inicia para escuchar a los botones
        tablaClases.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int fila = tablaClases.getSelectedRow();

                    if (fila != -1) {
                        jbModificar.setEnabled(true);
                        jbEliminar.setEnabled(true);

                        String estado = tablaClases.getValueAt(fila, 5).toString();
                        if (estado.equals("Activo")) {
                            jBBaja.setEnabled(true);
                            jBAlta.setEnabled(false);
                        } else {
                            jBBaja.setEnabled(false);
                            jBAlta.setEnabled(true);
                        }
                    } else {
                        jbModificar.setEnabled(false);
                        jbEliminar.setEnabled(false);
                        jBBaja.setEnabled(false);
                        jBAlta.setEnabled(false);
                    }
                }
            }
        });
        
    }

    private boolean validarSoloLetras(String texto, String nombreCampo) {
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo '" + nombreCampo + "' no puede estar vacío.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            JOptionPane.showMessageDialog(this, "El campo '" + nombreCampo + "' solo debe contener letras.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarSoloNumeros(String texto, String nombreCampo) {
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo '" + nombreCampo + "' no puede estar vacío.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!texto.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "El campo '" + nombreCampo + "' debe ser un número entero válido.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel5 = new javax.swing.JLabel();
        btn_volver = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        PanelBuscar = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jbEliminar = new javax.swing.JButton();
        jbModificar = new javax.swing.JButton();
        jBBaja = new javax.swing.JButton();
        jBAlta = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaClases = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        panelFormulario = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRBInactivo = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        JCBEntrenador = new javax.swing.JComboBox<>();
        JSHorario = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        JTxtCapacidad = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtBuscarPorNombre = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtBuscarPorNombre1 = new javax.swing.JTextField();
        jbBuscar = new javax.swing.JButton();
        jBLimpiar = new javax.swing.JButton();
        jbNuevo = new javax.swing.JButton();

        setTitle("Gestion de Clases");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setText("Gestion de Clases");

        btn_volver.setBackground(new java.awt.Color(153, 153, 153));
        btn_volver.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btn_volver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/volver.png"))); // NOI18N
        btn_volver.setText("Volver");
        btn_volver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_volverActionPerformed(evt);
            }
        });

        PanelBuscar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jbEliminar.setBackground(new java.awt.Color(153, 153, 153));
        jbEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        jbEliminar.setText("Eliminar");
        jbEliminar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jbEliminar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jbEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEliminarActionPerformed(evt);
            }
        });

        jbModificar.setBackground(new java.awt.Color(153, 153, 153));
        jbModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        jbModificar.setText("Modificar");
        jbModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModificarActionPerformed(evt);
            }
        });

        jBBaja.setBackground(new java.awt.Color(153, 153, 153));
        jBBaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/baja.png"))); // NOI18N
        jBBaja.setText("Baja ");
        jBBaja.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBBaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBBajaActionPerformed(evt);
            }
        });

        jBAlta.setBackground(new java.awt.Color(153, 153, 153));
        jBAlta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/alta.png"))); // NOI18N
        jBAlta.setText("Alta");
        jBAlta.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBAlta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAltaActionPerformed(evt);
            }
        });

        tablaClases.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "id", "nombre", "estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaClases);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setText("Listado de Clases.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jbModificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBBaja, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jBAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jbEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(316, 439, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbModificar)
                    .addComponent(jBBaja)
                    .addComponent(jBAlta)
                    .addComponent(jbEliminar))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        panelFormulario.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Nombre:");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setText("Registro de Clases:");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Activo");
        jRadioButton1.setFocusCycleRoot(true);

        buttonGroup1.add(jRBInactivo);
        jRBInactivo.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jRBInactivo.setText("Inactivo");

        jLabel1.setBackground(new java.awt.Color(153, 153, 153));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Entrenador/ra:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Horario:");

        JCBEntrenador.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Capacidad:");

        btnGuardar.setBackground(new java.awt.Color(153, 153, 153));
        btnGuardar.setText("Guardar");
        btnGuardar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setBackground(new java.awt.Color(153, 153, 153));
        btnCancelar.setText("Cancelar");
        btnCancelar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFormularioLayout = new javax.swing.GroupLayout(panelFormulario);
        panelFormulario.setLayout(panelFormularioLayout);
        panelFormularioLayout.setHorizontalGroup(
            panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormularioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormularioLayout.createSequentialGroup()
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelFormularioLayout.createSequentialGroup()
                                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addGroup(panelFormularioLayout.createSequentialGroup()
                                        .addComponent(jRadioButton1)
                                        .addGap(18, 18, 18)
                                        .addComponent(jRBInactivo))
                                    .addGroup(panelFormularioLayout.createSequentialGroup()
                                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(JCBEntrenador, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(66, 66, 66)
                                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(JSHorario, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormularioLayout.createSequentialGroup()
                                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(JTxtCapacidad, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(40, 40, 40))
                                    .addComponent(btnGuardar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelFormularioLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(47, 47, 47))))
        );
        panelFormularioLayout.setVerticalGroup(
            panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormularioLayout.createSequentialGroup()
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButton1)
                            .addComponent(jRBInactivo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(JCBEntrenador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JSHorario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(btnGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancelar)
                        .addGap(32, 32, 32)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTxtCapacidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Buscar Clases:");

        jLabel7.setText("Por Nombre:");

        txtBuscarPorNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarPorNombreActionPerformed(evt);
            }
        });
        txtBuscarPorNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarPorNombreKeyReleased(evt);
            }
        });

        jLabel8.setText("Por ID:");

        jbBuscar.setBackground(new java.awt.Color(153, 153, 153));
        jbBuscar.setText("Buscar");
        jbBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbBuscarActionPerformed(evt);
            }
        });

        jBLimpiar.setBackground(new java.awt.Color(153, 153, 153));
        jBLimpiar.setText("Limpiar");
        jBLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBLimpiarActionPerformed(evt);
            }
        });

        jbNuevo.setBackground(new java.awt.Color(153, 153, 153));
        jbNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/+.png"))); // NOI18N
        jbNuevo.setText("Nuevo");
        jbNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbNuevoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jbBuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jBLimpiar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel7)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtBuscarPorNombre)
                                .addComponent(txtBuscarPorNombre1, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbNuevo)
                        .addGap(135, 135, 135))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtBuscarPorNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8))
                    .addComponent(jbNuevo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBuscarPorNombre1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbBuscar)
                    .addComponent(jBLimpiar)))
        );

        javax.swing.GroupLayout PanelBuscarLayout = new javax.swing.GroupLayout(PanelBuscar);
        PanelBuscar.setLayout(PanelBuscarLayout);
        PanelBuscarLayout.setHorizontalGroup(
            PanelBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBuscarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelBuscarLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(PanelBuscarLayout.createSequentialGroup()
                        .addComponent(panelFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 112, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        PanelBuscarLayout.setVerticalGroup(
            PanelBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelBuscarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(panelFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane2.setViewportView(PanelBuscar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(244, 244, 244)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 696, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(7, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_volver)
                .addGap(65, 65, 65))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_volver, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        String nombre = txtNombre.getText().trim();
        String capacidadStr = JTxtCapacidad.getText().trim();

        // validacion interna tradicionals: Nombre
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre de la clase.");
            txtNombre.requestFocus();
            return;
        }
        if (nombre.length() > 20) {
            JOptionPane.showMessageDialog(this, "El nombre no puede superar los 20 caracteres.");
            txtNombre.requestFocus();
            return;
        }
        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            JOptionPane.showMessageDialog(this, "El nombre de la clase solo debe contener letras y espacios(Sin numeros ni simbolos)", "Validación", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return;
        }
        
        //validacion capacidad
        if (capacidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese la capacidad maxima de alumnos", "Validacion de Datos", JOptionPane.WARNING_MESSAGE);
            JTxtCapacidad.requestFocus();
            return;
        }
        if (!capacidadStr.matches("\\d{1,2}")) {
            JOptionPane.showMessageDialog(this, "La capacidad debe ser un numero entero entre el 1 y 30.", "Error de tipo de dato", JOptionPane.WARNING_MESSAGE);
            JTxtCapacidad.setText("");
            JTxtCapacidad.requestFocus();
            return;
        }
        //validacion entrenador
        if (JCBEntrenador.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un entrenador para la clase", "Validacion de Datos", JOptionPane.WARNING_MESSAGE);

            return;
        }

        try {
            int capacidadReal = Integer.parseInt(capacidadStr);
            if (capacidadReal <= 0 || capacidadReal > 30) {
                JOptionPane.showMessageDialog(this, "La capacidad debe ser mayor a cero y no puede superar los 30 alumnos", "Validacion de Datos", JOptionPane.WARNING_MESSAGE);
                JTxtCapacidad.requestFocus();
                return;
            }

            entidades.Entrenadores entrenadorSeleccionado = (entidades.Entrenadores) JCBEntrenador.getSelectedItem();
            java.util.Date fechaSpinner = (java.util.Date) JSHorario.getValue();
            java.time.LocalTime horarioClase = fechaSpinner.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalTime();
            boolean activo = jRadioButton1.isSelected();

            //Agregacion de validacion de especialidd
            if (entrenadorSeleccionado.getEspecialidad() == null) {
                javax.swing.JOptionPane.showMessageDialog(this, "El entrenador selecionado no tiene una especialidad asignada", "Validacion", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String especialidadProf = entrenadorSeleccionado.getEspecialidad().toString().trim().toLowerCase();
            String nombreClaseMin = nombre.toLowerCase();

            if (!nombreClaseMin.contains(especialidadProf) && !especialidadProf.contains(nombreClaseMin)) {
                javax.swing.JOptionPane.showMessageDialog(this, "La especialidad del entrenador (" + entrenadorSeleccionado.getEspecialidad().toString() + ") no coincide con la clase ' " + nombre + " '.", "Especialidad incompatible", JOptionPane.WARNING_MESSAGE);
                return;
            }
            //validacion de hora clase, evitar choque de horarios
            horarioClase = horarioClase.withSecond(0).withNano(0);
            for (entidades.Clases c : claseData.listarClasesActivas()) {
                if (editando && c.getIdClase() == idClaseActual) {
                    continue;
                }
                if (c.getEntrenador() != null && c.getEntrenador().getIdEntrenadores() == entrenadorSeleccionado.getIdEntrenadores()) {
                    java.time.LocalTime horaExistente = c.getHorario().withSecond(0).withNano(0);
                    long diferenciaMinutos = Math.abs(java.time.Duration.between(horaExistente, horarioClase).toMinutes());

                    if (diferenciaMinutos < 60) {
                        javax.swing.JOptionPane.showMessageDialog(this,
                                "El entrenador " + entrenadorSeleccionado.getNombre() + " " + entrenadorSeleccionado.getApellido()
                                + "ya dicta " + c.getNombre() + " de: " + horaExistente + " a " + horaExistente.plusHours(1) + " hs.\n"
                                + "Se superponen los horarios", "Horario ocupado", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }

            if (editando) {
                entidades.Clases claseModificar = claseData.buscarClasePorId(idClaseActual);
                if (claseModificar != null) {
                    claseModificar.setNombre(nombre);
                    claseModificar.setEstado(activo);
                    claseModificar.setEntrenador(entrenadorSeleccionado);
                    claseModificar.setHorario(horarioClase);
                    claseModificar.setCapacidad(capacidadReal);

                    claseData.modificarClase(claseModificar);
                    JOptionPane.showMessageDialog(this, "Clase modificada correctamente.", "Exitoso", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // persistencia.MiConexion con = new persistencia.MiConexion("jdbc:mysql://localhost:3306/gp4_gimnasio_aborigym", "root", "");
                // entidades.Entrenadores entren = new persistencia.EntrenadorData(con).buscarEntrenadorPorId(1);

                entidades.Clases nuevaClase = new entidades.Clases(nombre, entrenadorSeleccionado, horarioClase, capacidadReal, activo);

                claseData.guardarClase(nuevaClase);
                JOptionPane.showMessageDialog(this, "Clase registrada con éxito y asociada al entrenador.", "Exitoso", JOptionPane.INFORMATION_MESSAGE);

            }
            cargarClases();
            txtNombre.setText("");
            JTxtCapacidad.setText("");
            JCBEntrenador.setSelectedIndex(0);
            panelFormulario.setVisible(false);
            editando = false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error de sistema", JOptionPane.ERROR_MESSAGE);

        }

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed

        txtNombre.setText("");
        panelFormulario.setVisible(false);
        editando = false;
        idClaseActual = -1;
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void jbBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbBuscarActionPerformed
        String nombreBuscado = txtBuscarPorNombre.getText().trim();
        String idBuscado = txtBuscarPorNombre1.getText().trim();//extraigo id
        //validacion por nombre
        if (!nombreBuscado.isEmpty()) {
            //agrego condicion solo letras
            if(!validarSoloLetras(nombreBuscado,"Buscar por Nombre")){
            txtBuscarPorNombre.setText("");
            txtBuscarPorNombre.requestFocus();
            cargarClases();
            return;
            }
            try {
                modeloTabla.setRowCount(0);
                boolean encontro = false;

                for (Clases c : claseData.listartodaslasclases()) {
                    if (c.getNombre().toLowerCase().contains(nombreBuscado.toLowerCase())) {
                        String nombreEntrenador = (c.getEntrenador() != null)
                                ? c.getEntrenador().getNombre() + " " + c.getEntrenador().getApellido()
                                : "No asignado";

                        modeloTabla.addRow(new Object[]{
                            c.getIdClase(),
                            c.getNombre(),
                            c.getHorario(),
                            c.getCapacidad() + " Alumnos",
                            nombreEntrenador,
                            c.isEstado() ? "Activo" : "Inactivo"
                        });
                        encontro = true;
                    }
                }//fin for
                if (!encontro) {
                    JOptionPane.showMessageDialog(this, "No se encontraron clases con el nombre: " + nombreBuscado, "Busqueda", JOptionPane.INFORMATION_MESSAGE);
                    cargarClases();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al procesar la búsqueda por nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }
        // validacion po ID
        if (!validarSoloNumeros(idBuscado, "ID de Búsqueda")) {
            txtBuscarPorNombre1.setText("");
            txtBuscarPorNombre1.requestFocus();
            cargarClases();
            return;
        }

        try {
            int id = Integer.parseInt(idBuscado);
            Clases c = claseData.buscarClasePorId(id);
            modeloTabla.setRowCount(0);

            if (c != null) {
                String nombreEntrenador = (c.getEntrenador() != null)
                        ? c.getEntrenador().getNombre() + " " + c.getEntrenador().getApellido()
                        : "No asignado";

                modeloTabla.addRow(new Object[]{
                    c.getIdClase(),
                    c.getNombre(),
                    c.getHorario(),
                    c.getCapacidad() + " Alumnos",
                    nombreEntrenador,
                    c.isEstado() ? "Activo" : "Inactivo"
                });
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ninguna clase con el ID: " + id, "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
                cargarClases();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al procesar la búsqueda por ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jbBuscarActionPerformed

    private void jbNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbNuevoActionPerformed
        editando = false;
        LlenarComboEntrenadores();
        panelFormulario.setVisible(true);   // muestra el formulario
        txtNombre.setText("");
        JTxtCapacidad.setText("");
        jRadioButton1.setSelected(true);    // Activo por defecto
        txtNombre.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_jbNuevoActionPerformed

    private void jBLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBLimpiarActionPerformed
        txtBuscarPorNombre.setText("");
        txtBuscarPorNombre1.setText("");
        cargarClases();
        txtBuscarPorNombre.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_jBLimpiarActionPerformed

    private void jbEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEliminarActionPerformed
        int fila = tablaClases.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una clase de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(tablaClases.getValueAt(fila, 0).toString());
        
        // agregamos o anulamos funcion botone y cambiamos por si o no.
        Object[] opciones = {"Si","No"};
            int respuesta = JOptionPane.showOptionDialog(
        this,
        "¿Seguro desea eliminar esta clase de forma permanente ?",
        "Confirmacion",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE,
        null,
        opciones,
        opciones[1]//po precaucion queda marcado en opcion no
        );

        if (respuesta == 0) {

            if(claseData.borrarClase(id)){
                cargarClases();
                JOptionPane.showMessageDialog(this, "Clase eliminada permanentemente.");
            }
        }
    }//GEN-LAST:event_jbEliminarActionPerformed

    private void jbModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModificarActionPerformed
        // TODO add your handling code here:
        int fila = tablaClases.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una clase de la tabla para modificar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        idClaseActual = Integer.parseInt(tablaClases.getValueAt(fila, 0).toString());
        Clases c = claseData.buscarClasePorId(idClaseActual);

        if (c != null) {
            LlenarComboEntrenadores();
            txtNombre.setText(c.getNombre());
            JTxtCapacidad.setText(String.valueOf(c.getCapacidad()));

            java.util.Date horaDate = java.util.Date.from(c.getHorario()
                    .atDate(java.time.LocalDate.now())
                    .atZone(java.time.ZoneId.systemDefault()).toInstant());
            JSHorario.setValue(horaDate);

            if (c.getEntrenador() != null) {
                for (int i = 0; i < JCBEntrenador.getItemCount(); i++) {
                    Entrenadores e = (Entrenadores) JCBEntrenador.getItemAt(i);
                    if (e.getIdEntrenador() == c.getEntrenador().getIdEntrenador()) {
                        JCBEntrenador.setSelectedIndex(i);
                        break;
                    }
                }
            }

            if (c.isEstado()) {
                jRadioButton1.setSelected(true);
            } else {
                jRBInactivo.setSelected(true);
            }

            editando = true;
            panelFormulario.setVisible(true);
            txtNombre.requestFocus();
        }
    }//GEN-LAST:event_jbModificarActionPerformed

    private void jBBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBBajaActionPerformed
        int fila = tablaClases.getSelectedRow();

        if (fila == -1) {
            return;
        }
      //
        int id = Integer.parseInt(tablaClases.getValueAt(fila, 0).toString());
        Object[] opciones ={"Si","No"};
        int confirmacion = JOptionPane.showOptionDialog(
                this,
                "¿Está seguro que desea aplicar la baja lógica a esta clase?",
                "Confirmar Acción",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones
                );
        

        if (confirmacion == JOptionPane.YES_OPTION) {
            claseData.bajaLogicaClase(id);
            cargarClases();
            JOptionPane.showMessageDialog(this, "La clase fue desactivada (Estado = Inactivo) con éxito.", "Baja Lógica", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jBBajaActionPerformed
    }
    private void jBAltaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAltaActionPerformed
        int fila = tablaClases.getSelectedRow();

        if (fila == -1) {
            return;
        }
        int id = Integer.parseInt(tablaClases.getValueAt(fila, 0).toString());
        Object[] opciones = {"Sí", "No"};
        int confirmacion = JOptionPane.showOptionDialog(
                this,
                "¿Está seguro que desea aplicar el alta lógica a esta clase?",
                "Confirmar Alta",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                Clases claseAlta = claseData.buscarClasePorId(id);
                if (claseAlta != null) {
                    //agregamos verificacion de que el entrenador no tenga otra clase activa a esa hora
                    java.time.LocalTime horaReactivar = claseAlta.getHorario().withSecond(0).withNano(0);
                    for(Clases c :claseData.listarClasesActivas()){
                    if(c.getIdClase()!= claseAlta.getIdClase() && c.getEntrenador() != null
                            && claseAlta.getEntrenador() != null
                            && c.getEntrenador().getIdEntrenador() == claseAlta.getEntrenador().getIdEntrenador()){
                    
                        long diff = Math.abs(java.time.Duration.between(c.getHorario().withSecond(0).withNano(0), horaReactivar).toMinutes());
                        if(diff <60){
                            JOptionPane.showMessageDialog(this, 
                                    "No se puede reactivar: el entrenador ya tiene activa la clase: "+c.getNombre()+" a las"+c.getHorario()+".",
                                    "Conflicto de Horario", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                    }
                    
                    claseAlta.setEstado(true);
                    claseData.modificarClase(claseAlta);
                    cargarClases();
                    JOptionPane.showMessageDialog(this, "La clase fue activada (Estado = Activo) con éxito.", "Alta Lógica", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró la clase en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al procesar el alta: " + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jBAltaActionPerformed

    private void btn_volverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_volverActionPerformed
        // TODO add your handling code here:
        //VistaPrincipal principal = new VistaPrincipal();
        //principal.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_volverActionPerformed

    private void txtBuscarPorNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarPorNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarPorNombreActionPerformed

    private void txtBuscarPorNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarPorNombreKeyReleased
        
        String textoOriginal = txtBuscarPorNombre.getText().trim();
        if(!textoOriginal.isEmpty() && !textoOriginal.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")){
            JOptionPane.showMessageDialog(this, "La búsqueda por nombre solo permite letras.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            txtBuscarPorNombre.setText("");
            cargarClases();
            return;
        }
        
        String textoBuscado = textoOriginal.toLowerCase();
        modeloTabla.setRowCount(0);
        
        for(Clases c :claseData.listartodaslasclases()){
        //validamos si el texto esta vacio muestra todas, sino filtra las q contenga la letra q se tipee
        if(textoBuscado.isEmpty()|| c.getNombre().toLowerCase().contains(textoBuscado)){
         String nombreEntrenador;
         
         if(c.getEntrenador() !=null){
                 nombreEntrenador = c.getEntrenador().getNombre()+" "+c.getEntrenador().getApellido();
                 }else{
                 nombreEntrenador = "No Asignado";
         }
         
                modeloTabla.addRow(new Object[]{
                    c.getIdClase(),
                    c.getNombre(),
                    c.getHorario(),
                    c.getCapacidad() + " Alumnos",nombreEntrenador,
                    c.isEstado() ?"Activo":"Inactivo"
                
                });
           }
        }
        
    }//GEN-LAST:event_txtBuscarPorNombreKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<Object> JCBEntrenador;
    private javax.swing.JSpinner JSHorario;
    private javax.swing.JTextField JTxtCapacidad;
    private javax.swing.JPanel PanelBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btn_volver;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jBAlta;
    private javax.swing.JButton jBBaja;
    private javax.swing.JButton jBLimpiar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRBInactivo;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbBuscar;
    private javax.swing.JButton jbEliminar;
    private javax.swing.JButton jbModificar;
    private javax.swing.JButton jbNuevo;
    private javax.swing.JPanel panelFormulario;
    private javax.swing.JTable tablaClases;
    private javax.swing.JTextField txtBuscarPorNombre;
    private javax.swing.JTextField txtBuscarPorNombre1;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables

    private void editarEncabezado() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloTabla.addColumn("ID Clase");
        modeloTabla.addColumn("Nombre Clase");
        modeloTabla.addColumn("Horario");
        modeloTabla.addColumn("Capacidad Max");
        modeloTabla.addColumn("Entrenador");
        modeloTabla.addColumn("Estado");
        tablaClases.setModel(modeloTabla);
    }

    private void cargarClases() {
        modeloTabla.setRowCount(0);
        for (Clases c : claseData.listartodaslasclases()) {
            String nombreEntrenador = (c.getEntrenador() != null)
                    ? c.getEntrenador().getNombre() + " " + c.getEntrenador().getApellido()
                    : "No Asignado ";
            modeloTabla.addRow(new Object[]{
                c.getIdClase(),
                c.getNombre(),
                c.getHorario(),
                c.getCapacidad() + " Alumnos",
                nombreEntrenador,
                c.isEstado() ? "Activo" : "Inactivo"
            });
        }
    }

    private void LlenarComboEntrenadores() {
        JCBEntrenador.removeAllItems();
        try {
            persistencia.MiConexion con = new persistencia.MiConexion("jdbc:mysql://localhost:3306/gp4_gimnasio_aborigym", "root", "");
            persistencia.EntrenadorData entrenadorData = new persistencia.EntrenadorData(con);

            java.util.List<entidades.Entrenadores> lista = entrenadorData.listarEntrenadores();

            for (entidades.Entrenadores e : lista) {
                if (e.isEstado()) {//revisamos el tributo booleano de estado
                    JCBEntrenador.addItem((Object) e);
                }
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al cargar la lista de entrenadores" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
