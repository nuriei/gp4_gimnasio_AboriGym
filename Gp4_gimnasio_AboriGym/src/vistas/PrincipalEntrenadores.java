package vistas;

import entidades.Entrenadores;
import entidades.Especialidad;
import java.awt.Image;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import persistencia.EntrenadorData;
import persistencia.MiConexion;

public class PrincipalEntrenadores extends javax.swing.JInternalFrame {

    private EntrenadorData entrenadorData;
    private DefaultTableModel modeloTabla;
    private boolean editando = false;
    private int idEntrenadorActual = -1;

    public PrincipalEntrenadores() {
        initComponents();
        // --- FILTRO PARA PERMITIR SOLO NÚMEROS EN EL DNI ---
        ((javax.swing.text.AbstractDocument) txtDni.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
            @Override
            public void insertString(javax.swing.text.DocumentFilter.FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                if (string != null && string.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(javax.swing.text.DocumentFilter.FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
                if (text != null && text.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        // --- FILTRO SOLO LETRAS Y ESPACIOS PARA EL NOMBRE ---
        ((javax.swing.text.AbstractDocument) txtNombre.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
            @Override
            public void insertString(javax.swing.text.DocumentFilter.FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                if (string != null && string.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(javax.swing.text.DocumentFilter.FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
                if (text != null && text.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        // --- FILTRO SOLO LETRAS Y ESPACIOS PARA EL APELLIDO ---
        ((javax.swing.text.AbstractDocument) txtApellido.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
            @Override
            public void insertString(javax.swing.text.DocumentFilter.FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                if (string != null && string.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(javax.swing.text.DocumentFilter.FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
                if (text != null && text.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        btnGuardar.setEnabled(false); // Arranca apagado

        javax.swing.event.DocumentListener validadorListener = new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { verificarCamposCompletos(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { verificarCamposCompletos(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { verificarCamposCompletos(); }
        };

        txtDni.getDocument().addDocumentListener(validadorListener);
        txtApellido.getDocument().addDocumentListener(validadorListener);
        txtNombre.getDocument().addDocumentListener(validadorListener);

        panelFormulario.setVisible(false);
        modeloTabla = (DefaultTableModel) tablaEntrenadores.getModel();
        MiConexion conexion = new MiConexion("jdbc:mysql://localhost:3306/gp4_gimnasio_aborigym", "root", "");
        entrenadorData = new EntrenadorData(conexion);

        cargarEntrenadores();
        llenarComboEspecialidad();

        jbModificar.setEnabled(false);
        jBBaja.setEnabled(false);
        jBAlta.setEnabled(false);
        jbEliminar.setEnabled(false);

        tablaEntrenadores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaEntrenadores.getSelectedRow();
                boolean seleccionado = fila != -1;

                jbModificar.setEnabled(seleccionado);
                jbEliminar.setEnabled(seleccionado);

                // Dependiendo del estado activo o inactivo del entrenador se habilita el btn baja y alta
                if (seleccionado) {
                    // Ajustá el índice de columna si "Estado" está en otra posición (ej: si está en la última columna)
                    String estado = tablaEntrenadores.getValueAt(fila, 5).toString();

                    jBBaja.setEnabled(estado.equals("Activo"));
                    jBAlta.setEnabled(estado.equals("Inactivo"));
                } else {
                    jBBaja.setEnabled(false);
                    jBAlta.setEnabled(false);
                }
            }
        });
//        try {
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
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jbEliminar = new javax.swing.JButton();
        jbModificar = new javax.swing.JButton();
        jBBaja = new javax.swing.JButton();
        jBAlta = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaEntrenadores = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        PanelBuscar = new javax.swing.JPanel();
        jbBuscar = new javax.swing.JButton();
        jbNuevo = new javax.swing.JButton();
        txtBuscarPorNombre = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtBuscarPorID = new javax.swing.JTextField();
        jBLimpiar = new javax.swing.JButton();
        btn_volver = new javax.swing.JButton();
        panelFormulario = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        txtDni = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        comboBoxEstado = new javax.swing.JComboBox<>();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        comboBoxEspecialidad = new javax.swing.JComboBox<>();

        setTitle("Gestion Entrenadores");

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jbEliminar.setBackground(new java.awt.Color(153, 153, 153));
        jbEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar_usuario.png"))); // NOI18N
        jbEliminar.setText("Elimnar");
        jbEliminar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jbEliminar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jbEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEliminarActionPerformed(evt);
            }
        });

        jbModificar.setBackground(new java.awt.Color(153, 153, 153));
        jbModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/modificar_usuario.png"))); // NOI18N
        jbModificar.setText("Modificar");
        jbModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModificarActionPerformed(evt);
            }
        });

        jBBaja.setBackground(new java.awt.Color(153, 153, 153));
        jBBaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/baja_usuario.png"))); // NOI18N
        jBBaja.setText("Baja ");
        jBBaja.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBBaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBBajaActionPerformed(evt);
            }
        });

        jBAlta.setBackground(new java.awt.Color(153, 153, 153));
        jBAlta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/alta_usuario.png"))); // NOI18N
        jBAlta.setText("Alta");
        jBAlta.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBAlta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAltaActionPerformed(evt);
            }
        });

        tablaEntrenadores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "id", "DNI", "Nombre", "Apellido", "Especialidad", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaEntrenadores);
        if (tablaEntrenadores.getColumnModel().getColumnCount() > 0) {
            tablaEntrenadores.getColumnModel().getColumn(0).setResizable(false);
            tablaEntrenadores.getColumnModel().getColumn(0).setPreferredWidth(1);
        }

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setText("Listado de Entrenadores");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jbEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBBaja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBAlta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbModificar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jbModificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBBaja)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBAlta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbEliminar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setText("Gestion de Entrenadores");

        PanelBuscar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jbBuscar.setBackground(new java.awt.Color(153, 153, 153));
        jbBuscar.setText("Buscar");
        jbBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbBuscarActionPerformed(evt);
            }
        });

        jbNuevo.setBackground(new java.awt.Color(153, 153, 153));
        jbNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar_usuario.png"))); // NOI18N
        jbNuevo.setText("Nuevo");
        jbNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbNuevoActionPerformed(evt);
            }
        });

        txtBuscarPorNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarPorNombreKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Buscar Entrenadores");

        jLabel7.setText("Por Nombre:");

        jLabel8.setText("Por ID:");

        jBLimpiar.setBackground(new java.awt.Color(153, 153, 153));
        jBLimpiar.setText("Limpiar");
        jBLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBLimpiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelBuscarLayout = new javax.swing.GroupLayout(PanelBuscar);
        PanelBuscar.setLayout(PanelBuscarLayout);
        PanelBuscarLayout.setHorizontalGroup(
            PanelBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBuscarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(txtBuscarPorNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                    .addComponent(txtBuscarPorID)
                    .addGroup(PanelBuscarLayout.createSequentialGroup()
                        .addComponent(jbBuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jBLimpiar))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbNuevo)
                .addGap(50, 50, 50))
        );
        PanelBuscarLayout.setVerticalGroup(
            PanelBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelBuscarLayout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PanelBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelBuscarLayout.createSequentialGroup()
                        .addComponent(txtBuscarPorNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8))
                    .addComponent(jbNuevo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBuscarPorID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbBuscar)
                    .addComponent(jBLimpiar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_volver.setBackground(new java.awt.Color(153, 153, 153));
        btn_volver.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btn_volver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/volver.png"))); // NOI18N
        btn_volver.setText("Volver");
        btn_volver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_volverActionPerformed(evt);
            }
        });

        panelFormulario.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Documento:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Apellido:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Nombre:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Estado:");

        comboBoxEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        btnGuardar.setBackground(new java.awt.Color(153, 153, 153));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setBackground(new java.awt.Color(153, 153, 153));
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setText("Registro de Entrenadores:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Especialidad:");

        comboBoxEspecialidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout panelFormularioLayout = new javax.swing.GroupLayout(panelFormulario);
        panelFormulario.setLayout(panelFormularioLayout);
        panelFormularioLayout.setHorizontalGroup(
            panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormularioLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 22, Short.MAX_VALUE)
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCancelar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelFormularioLayout.createSequentialGroup()
                                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel11))
                                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelFormularioLayout.createSequentialGroup()
                                        .addGap(5, 5, 5)
                                        .addComponent(comboBoxEspecialidad, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelFormularioLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(comboBoxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(panelFormularioLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        panelFormularioLayout.setVerticalGroup(
            panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormularioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboBoxEspecialidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(comboBoxEstado))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCancelar)
                            .addComponent(btnGuardar)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(9, 9, 9))
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtDni))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtApellido))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(179, 179, 179))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PanelBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(panelFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_volver)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_volver, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEliminarActionPerformed
        // TODO add your handling code here:
        int fila = tablaEntrenadores.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un entrenador de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(
                tablaEntrenadores.getValueAt(fila, 0).toString()
        );

        Object[] opciones = {"Sí", "No"};

        int respuesta = JOptionPane.showOptionDialog(
                this,
                "¿Seguro desea eliminar el entrenador permanentemente de la BD?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                opciones,
                opciones[1] // "No" seleccionado por defecto
        );

        if (respuesta == 0) {
            // CORRECCIÓN: pasarle 'id' en lugar de idEntrenadorActual
            entrenadorData.borrarEntrenador(id);

            cargarEntrenadores(); // Recarga la tabla de entrenadores

            JOptionPane.showMessageDialog(this, "Se borró el entrenador correctamente.");
        }
    }//GEN-LAST:event_jbEliminarActionPerformed

    private void jbModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModificarActionPerformed
        int fila = tablaEntrenadores.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un entrenador de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        editando = true; // Activamos el modo edición
        idEntrenadorActual = Integer.parseInt(tablaEntrenadores.getValueAt(fila, 0).toString());

        // 💡 BUENAS PRÁCTICAS: Buscamos el objeto completo desde la base de datos usando el ID
        Entrenadores e = entrenadorData.buscarEntrenadorPorId(idEntrenadorActual);

        if (e != null) {
            txtDni.setText(e.getDni());
            txtApellido.setText(e.getApellido()); // Ojo acá: en tu código anterior tenías invertidos nombre y apellido según el orden de la tabla, asegurate que coincidan con tu objeto
            txtNombre.setText(e.getNombre());

            // SELECCIONAMOS LA ESPECIALIDAD
            comboBoxEspecialidad.setSelectedItem(e.getEspecialidad().toString());

            // Estado
            comboBoxEstado.setSelectedItem(e.isEstado() ? "Activo" : "Inactivo");

        }

        panelFormulario.setVisible(true);
    }//GEN-LAST:event_jbModificarActionPerformed

    private void jBBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBBajaActionPerformed
        // TODO add your handling code here:
        int fila = tablaEntrenadores.getSelectedRow();

        if (fila == -1) {
            return;
        }

        int id = Integer.parseInt(
                tablaEntrenadores.getValueAt(fila, 0).toString()
        );

        Object[] opciones = {"Sí", "No"};

        int respuesta = JOptionPane.showOptionDialog(
                this,
                "¿Desea dar de baja este entrenador?",
                "Confirmar baja",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (respuesta == 0) {
            // Usamos el objeto de entrenador y su método de baja lógica
            entrenadorData.bajaLogicaEntrenador(id);
            cargarEntrenadores(); // Recarga la tabla de entrenadores

            JOptionPane.showMessageDialog(this, "Se dio de baja al entrenador correctamente.");
        }
    }//GEN-LAST:event_jBBajaActionPerformed

    private void jBAltaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAltaActionPerformed
        int fila = tablaEntrenadores.getSelectedRow();

        if (fila == -1) {
            return;
        }

        int id = Integer.parseInt(
                tablaEntrenadores.getValueAt(fila, 0).toString()
        );

        Object[] opciones = {"Sí", "No"};

        int respuesta = JOptionPane.showOptionDialog(
                this,
                "¿Desea dar de alta este entrenador?",
                "Confirma",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (respuesta == 0) {
            // Llamamos al método de alta lógica en EntrenadorData
            entrenadorData.altaLogicaEntrenador(id);
            cargarEntrenadores(); // Recarga la tabla

            JOptionPane.showMessageDialog(this, "Entrenador dado de alta correctamente.");
        }
    }//GEN-LAST:event_jBAltaActionPerformed

    private void jbBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbBuscarActionPerformed
        String nombreBuscado = txtBuscarPorNombre.getText().trim(); // Campo para Nombre
        String idBuscado = txtBuscarPorID.getText().trim();        // Campo para ID

        modeloTabla.setRowCount(0); // Limpio la tabla

        // Si escriben en el campo de nombre (búsqueda parcial)
        if (!nombreBuscado.isEmpty() && idBuscado.isEmpty()) {
            if (nombreBuscado.matches(".*\\d.*")) {
                JOptionPane.showMessageDialog(this, "El campo de búsqueda por nombre solo debe contener letras.", "Error de formato", JOptionPane.WARNING_MESSAGE);
                cargarEntrenadores(); // Recargamos la tabla completa
                return;
            }

            // Usamos listarEntrenadoresPorNombre para traer coincidencias
            List<Entrenadores> lista = entrenadorData.listarEntrenadoresPorNombre(nombreBuscado);

            if (!lista.isEmpty()) {
                for (Entrenadores e : lista) {
                    modeloTabla.addRow(new Object[]{
                        e.getIdEntrenadores(),
                        e.getDni(),
                        e.getNombre(),
                        e.getApellido(),
                        e.getEspecialidad(),
                        e.isEstado() ? "Activo" : "Inactivo"
                    });
                }
            } else {
                // 🛑 ESTE ES EL CARTEL QUE SALTA CUANDO NO ENCUENTRA NADA CON LO QUE BUSCARON
                JOptionPane.showMessageDialog(this, "No se encontró ningún entrenador con el nombre: " + nombreBuscado, "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                cargarEntrenadores(); // Recarga la tabla nuevamente con todos
            }
        } // Si se busca por ID
        else if (!idBuscado.isEmpty() && nombreBuscado.isEmpty()) {
            try {
                int id = Integer.parseInt(idBuscado);
                Entrenadores e = entrenadorData.buscarEntrenadorPorId(id);

                if (e != null) {
                    modeloTabla.addRow(new Object[]{
                        e.getIdEntrenadores(),
                        e.getDni(),
                        e.getNombre(),
                        e.getApellido(),
                        e.getEspecialidad(),
                        e.isEstado() ? "Activo" : "Inactivo"
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "No existe un entrenador con ese ID.");
                    cargarEntrenadores();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El ID debe ser numérico.");
                txtBuscarPorID.setText("");

            }
        } // Si no hay texto en ningún campo o hay en ambos
        else {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un solo valor a buscar por vez.");
            cargarEntrenadores();
        }
    }//GEN-LAST:event_jbBuscarActionPerformed

    private void jbNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbNuevoActionPerformed

        idEntrenadorActual = -1;

        txtDni.setText("");
        txtApellido.setText("");
        txtNombre.setText("");

        comboBoxEstado.setSelectedItem("Activo");
        panelFormulario.setVisible(true);

    }//GEN-LAST:event_jbNuevoActionPerformed

    private void jBLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBLimpiarActionPerformed
        txtBuscarPorNombre.setText("");
        txtBuscarPorID.setText("");
        txtBuscarPorNombre.requestFocus();

        cargarEntrenadores(); // Recarga la lista completa
    }//GEN-LAST:event_jBLimpiarActionPerformed

    private void btn_volverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_volverActionPerformed
        // TODO add your handling code here:
        VistaPrincipal principal = new VistaPrincipal();
        principal.setVisible(true);
    }//GEN-LAST:event_btn_volverActionPerformed

    private void txtBuscarPorNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarPorNombreKeyReleased
        // TODO add your handling code here:
        String textoBuscado = txtBuscarPorNombre.getText().trim();

        // Si el usuario empieza a escribir el nombre, limpiamos el campo de ID para que no se mezcle
        if (!textoBuscado.isEmpty()) {
            txtBuscarPorID.setText("");
        }

        modeloTabla.setRowCount(0); // Limpiamos la tabla antes de cargar resultados

        // Si borra todo el texto, recargamos la lista completa de entrenadores
        if (textoBuscado.isEmpty()) {
            cargarEntrenadores();
        } else {
            // Buscamos los entrenadores que coincidan parcialmente con las letras escritas
            List<Entrenadores> lista = entrenadorData.listarEntrenadoresPorNombre(textoBuscado);

            for (Entrenadores e : lista) {
                modeloTabla.addRow(new Object[]{
                    e.getIdEntrenadores(),
                    e.getDni(),
                    e.getNombre(),
                    e.getApellido(),
                    e.getEspecialidad(),
                    e.isEstado() ? "Activo" : "Inactivo"
                });
            }
        }
    }//GEN-LAST:event_txtBuscarPorNombreKeyReleased

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        panelFormulario.setVisible(false);
        txtDni.setText(null);
        txtApellido.setText(null);
        txtNombre.setText(null);

    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        String documento = txtDni.getText().trim();
        String apellido = txtApellido.getText().trim();
        String nombre = txtNombre.getText().trim();
        
        if (documento.length() < 7 || documento.length() > 8) {
        JOptionPane.showMessageDialog(this, "El DNI ingresado no es válido (debe tener entre 7 y 8 dígitos).", "Error en DNI", JOptionPane.WARNING_MESSAGE);
        return;
}

        // 1. OBTENEMOS LA ESPECIALIDAD SELECCIONADA DEL COMBOBOX Y LA CONVERTIMOS A NUESTRO ENUM
        String seleccionStr = comboBoxEspecialidad.getSelectedItem().toString();
        Especialidad especialidadSeleccionada = Especialidad.valueOf(seleccionStr);

        // Validamos que los campos obligatorios no estén vacíos
        if (documento.isEmpty() || apellido.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completá todos los campos.");
            return;
        }

        try {
            boolean estado = comboBoxEstado.getSelectedItem().toString().equals("Activo");

            if (idEntrenadorActual == -1) {
                // Guardar nuevo entrenador (le pasamos la especialidad seleccionada del combo)
                Entrenadores entrenador = new Entrenadores(documento, nombre, apellido, especialidadSeleccionada, estado);
                entrenadorData.guardarEntrenador(entrenador);

                JOptionPane.showMessageDialog(this, "Entrenador guardado.");
            } else {
                // Modificar entrenador existente (le pasamos el ID y la especialidad seleccionada)
                Entrenadores entrenador = new Entrenadores(idEntrenadorActual, documento, nombre, apellido, especialidadSeleccionada, estado);
                entrenadorData.modificarEntrenador(entrenador);

                JOptionPane.showMessageDialog(this, "Entrenador modificado.");
            }

            // Recargamos la tabla, ocultamos el formulario y reseteamos las variables
            cargarEntrenadores();
            panelFormulario.setVisible(false);
            idEntrenadorActual = -1;

            // Limpiamos los campos
            txtDni.setText("");
            txtApellido.setText("");
            txtNombre.setText("");
            comboBoxEstado.setSelectedItem("Activo");
            // Opcional: el combo se puede dejar en la primera opción por defecto
            comboBoxEspecialidad.setSelectedIndex(0);

        } catch (java.sql.SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la Base de Datos: " + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btn_volver;
    private javax.swing.JComboBox<String> comboBoxEspecialidad;
    private javax.swing.JComboBox<String> comboBoxEstado;
    private javax.swing.JButton jBAlta;
    private javax.swing.JButton jBBaja;
    private javax.swing.JButton jBLimpiar;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbBuscar;
    private javax.swing.JButton jbEliminar;
    private javax.swing.JButton jbModificar;
    private javax.swing.JButton jbNuevo;
    private javax.swing.JPanel panelFormulario;
    private javax.swing.JTable tablaEntrenadores;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtBuscarPorID;
    private javax.swing.JTextField txtBuscarPorNombre;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
    private void verificarCamposCompletos() {
    boolean dniLleno = !txtDni.getText().trim().isEmpty();
    boolean apellidoLleno = !txtApellido.getText().trim().isEmpty();
    boolean nombreLleno = !txtNombre.getText().trim().isEmpty();
    
    // El botón se activa solo si los tres tienen contenido
    btnGuardar.setEnabled(dniLleno && apellidoLleno && nombreLleno);
}
    private void cargarEntrenadores() {
        modeloTabla.setRowCount(0); // Limpia la tabla antes de cargar
        for (Entrenadores e : entrenadorData.listarEntrenadores()) {
            modeloTabla.addRow(new Object[]{
                e.getIdEntrenadores(),
                e.getDni(),
                e.getNombre(),
                e.getApellido(),
                e.getEspecialidad(),
                e.isEstado() ? "Activo" : "Inactivo"
            });
        }
    }

    private void cargarEntrenadoresPorNombre(String texto) {
        modeloTabla.setRowCount(0); // Limpia la tabla antes de cargar
        for (Entrenadores e : entrenadorData.listarEntrenadoresPorNombre(texto)) {
            modeloTabla.addRow(new Object[]{
                e.getIdEntrenadores(),
                e.getDni(),
                e.getNombre(),
                e.getApellido(),
                e.getEspecialidad(),
                e.isEstado() ? "Activo" : "Inactivo"
            });
        }
    }

    private void llenarComboEspecialidad() {
        comboBoxEspecialidad.removeAllItems(); // Limpia por las dudas
        for (entidades.Especialidad esp : entidades.Especialidad.values()) {
            comboBoxEspecialidad.addItem(esp.name()); // Agrega el nombre del enum como String
        }
    }
}
