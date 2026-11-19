
package vistas;

import persistencia.MiConexion;
import persistencia.PaseData;
import persistencia.SocioData;
import entidades.Socio; //para poder trabajar con lalista del metood "listarSocios()"
import java.util.List;
import persistencia.ClaseData;
import entidades.Clases;
import entidades.Pases;
import persistencia.AsistenciaData;
import entidades.Asistencias;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;//para poder usar super.replace() 
                                       //q nos permite escribir si se cumple la expresion regular
/**
 *
 * @author Jess Moran
 */
public class PrincipalAsistencias extends javax.swing.JInternalFrame {    
    
    private SocioData socioData ;
    private PaseData paseData;
    
    private ClaseData claseData;
    private AsistenciaData asistenciaData;

          
    public PrincipalAsistencias()
    {
        initComponents();
        
        configurarValidacionBusqueda();
        dcFecha.getDateEditor().addPropertyChangeListener
        ("date", evt ->
        {cbClaseItemStateChanged(new java.awt.event.ItemEvent(cbClase,
         java.awt.event.ItemEvent.ITEM_STATE_CHANGED,cbClase.getSelectedItem(),java.awt.event.ItemEvent.SELECTED));}
        );
      
        this.setSize(800, 900);
        
        // Compartimos una unica conexion para ahorrar recursos 
        //y evitar abrir varias conexiones independientes a la base de datos.        
        MiConexion conexion = new MiConexion("jdbc:mysql://localhost:3306/gp4_gimnasio_aborigym","root","");
    
    
        socioData = new SocioData(conexion);
        paseData = new PaseData(conexion);
        claseData =new ClaseData(conexion);
        asistenciaData = new AsistenciaData(conexion);
        cargarSocios();
        cargarClases();
        cbSocio.setSelectedIndex(-1);
        cbClase.setSelectedIndex(-1);
        btnLimpiarActionPerformed(null);
       
        
       
        
    }
    
    //Esto mantiene limpio el panel de informacion hasta q no elija opciones en eel jpanel de informacion
        private void limpiarPanelInfo()
    {
        lblClaseTitulo.setText("");
        lblHorario.setText("");
        lblCupos.setText("");

        lblTipoPase.setText("");
        lblCantidadClases.setText("");
        lblClaseTitulo.setText("Clase");
    }
    
    //cargamos las clases Activas
    
    private void cargarClases()
    {
        cbClase.removeAllItems();
        List<Clases> clases = claseData.listarClasesActivas();
        
        for(Clases clase : clases )
        {
            String profesor = clase.getEntrenador().getApellido()+ ", " + clase.getEntrenador().getNombre();
            String opcion = clase.getNombre() + " - "+ profesor + " - " + clase.getHorario();
            cbClase.addItem(opcion);
        }
    }
    
    // Carga el nombre y apellido de los socios activos al combobox
    private void cargarSocios()
    {
        cbSocio.removeAllItems();

        List<Socio> socios = socioData.listarSocios();

        for (Socio socio : socios) {

            if (socio.isEstado() && buscarPaseActivo(socio) != null) {

                cbSocio.addItem(
                    socio.getApellido() + ", "
                    + socio.getNombre() + " - ID: "
                    + socio.getIdSocio()
                );
            }
        }

    }
    
    //Busca el socio cargado en el cbSocio y lo compara con los datos reales en la base 
    private Socio buscarSocioSeleccionado() 
    {
        if (cbSocio.getSelectedItem() == null) 
        {
            return null;
        }

        String seleccion = cbSocio.getSelectedItem().toString();

        String idTexto = seleccion.substring(seleccion.lastIndexOf("ID: ") + 4);

        int idSocio = Integer.parseInt(idTexto);

        List<Socio> socios = socioData.listarSocios();

        for (Socio socio : socios)
        {

            if (socio.getIdSocio() == idSocio) 
            {
                return socio;
            }
        }

    return null;
    }
      private void configurarValidacionBusqueda() {

        AbstractDocument documento =(AbstractDocument) txtBuscarSocio.getDocument();

        documento.setDocumentFilter(new DocumentFilter() {

            @Override
            public void replace(FilterBypass fb, int offset, int length,
                    String texto, AttributeSet attrs)
                    throws BadLocationException {

                String actual = fb.getDocument().getText(0,
                        fb.getDocument().getLength());

                String nuevoTexto = actual.substring(0, offset)
                        + texto
                        + actual.substring(offset + length);

                // Validamos según el RadioButton seleccionado
                 //super.replace() permite que se pueda escribir cuando el texto es valido
                if (rbDni.isSelected())
                {
                    if (nuevoTexto.matches("[0-9]{0,8}"))
                    {
                        super.replace(fb, offset, length, texto, attrs);
                   }

                } else if (rbID.isSelected()) 
                        {
                           if (nuevoTexto.matches("[0-9]{0,4}")) 
                           {
                            super.replace(fb, offset, length, texto, attrs);
                            }

                } else if (rbNombre.isSelected()) 
                        {

                    if (nuevoTexto.matches("[\\p{L} ]*")) 
                        {
                            super.replace(fb, offset, length, texto, attrs);
                        }

                } else 
                {
                    super.replace(fb, offset, length, texto, attrs);
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String texto, AttributeSet attrs)
                    throws BadLocationException {

                replace(fb, offset, 0, texto, attrs);
            }
        });

        // Limpiamos el campo cuando cambia el tipo de búsqueda
        rbDni.addActionListener(e -> txtBuscarSocio.setText(""));
        rbID.addActionListener(e -> txtBuscarSocio.setText(""));
        rbNombre.addActionListener(e -> txtBuscarSocio.setText(""));
}
    private Pases buscarPaseActivo(Socio socio) 
        {
        if (socio == null) {
            return null;
        }

        List<Pases> pases = paseData.listarPasesPorDni(socio.getDni());

        for (Pases pase : pases) 
        {
            if (pase.estaVigente()) 
            {
                return pase;
            }
        }

        return null;
    }
    
    // Verifica si el socio tenia un pase vigente en la fecha indicada
private boolean teniaPaseVigente(Socio socio, LocalDate fecha) {

    if (socio == null || fecha == null) 
    {
        return false;
    }

    // Buscamos todos los pases del socio
    List<Pases> pases = paseData.listarPasesPorDni(socio.getDni());

    for (Pases pase : pases) {

        // vems si la fecha estaba dentro del periodo de vigencia
        if (pase.isEstado()&& !fecha.isBefore(pase.getFechaInicio())&& !fecha.isAfter(pase.getFechaFin())) {

            return true;
        }
    }

    return false;
}
    private void actualizarInfoClase() {

    if (cbClase.getSelectedItem() == null || dcFecha.getDate() == null)
    {
        return;
    }

    String seleccion = cbClase.getSelectedItem().toString();

    List<Clases> clases = claseData.listarClasesActivas();

    for (Clases clase : clases) {

        String profesor = clase.getEntrenador().getApellido()+ ", " + clase.getEntrenador().getNombre();

        String opcion = clase.getNombre() + " - " + profesor + " - " + clase.getHorario();

        if (opcion.equals(seleccion)) 
        {
            lblClaseTitulo.setText(clase.getNombre());
            lblHorario.setText(clase.getHorario().toString());

            LocalDate fecha = dcFecha.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

            List<Asistencias> asistenciasDelDia =asistenciaData.listarAsistenciasPorDia(fecha);

            int ocupados = 0;

            for (Asistencias a : asistenciasDelDia) 
            {
                if (a.getClase().getIdClase() == clase.getIdClase()) {
                    ocupados++;
                }
            }
            int disponibles = clase.getCapacidad() - ocupados;
            lblCupos.setText(String.valueOf(Math.max(0, disponibles)));
            break;
        }
    }
}
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        sPanelAsistencia = new javax.swing.JScrollPane();
        jpAsistencias = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        panelFormulario = new javax.swing.JPanel();
        lblSocio = new javax.swing.JLabel();
        lblClase = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        cbClase = new javax.swing.JComboBox<>();
        cbSocio = new javax.swing.JComboBox<>();
        dcFecha = new com.toedter.calendar.JDateChooser();
        jpInfo = new javax.swing.JPanel();
        lblCupos = new javax.swing.JLabel();
        lblHorario = new javax.swing.JLabel();
        lblCuposTitulo = new javax.swing.JLabel();
        lblClaseTitulo = new javax.swing.JLabel();
        lblCuposTitulo1 = new javax.swing.JLabel();
        btnRegistrarAsistencia = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtBuscarSocio = new javax.swing.JTextField();
        btnBuscarSocio = new javax.swing.JToggleButton();
        rbNombre = new javax.swing.JRadioButton();
        rbDni = new javax.swing.JRadioButton();
        rbID = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        lblSocioTitulo = new javax.swing.JLabel();
        lblPase = new javax.swing.JLabel();
        lblCantidadClases = new javax.swing.JLabel();
        lblClasesRestantes = new javax.swing.JLabel();
        lblTipoPase = new javax.swing.JLabel();
        panelListado = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaAsistencias = new javax.swing.JTable();
        lblClaseTitulo1 = new javax.swing.JLabel();
        btnVolver = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        setTitle("Gestion de Asistencias");
        setPreferredSize(new java.awt.Dimension(632, 736));

        sPanelAsistencia.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Gestion de Asistencias");

        panelFormulario.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblSocio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSocio.setText("Socio");

        lblClase.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblClase.setText("Clase");

        lblFecha.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFecha.setText("Fecha");

        cbClase.addItemListener(this::cbClaseItemStateChanged);

        cbSocio.addItemListener(this::cbSocioItemStateChanged);

        jpInfo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblCupos.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        lblCupos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCupos.setText("-");

        lblHorario.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        lblHorario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHorario.setText("-");

        lblCuposTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCuposTitulo.setText("Horario");

        lblClaseTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblClaseTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblClaseTitulo.setText("Clase ");

        lblCuposTitulo1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCuposTitulo1.setText("Cupos disponibles");

        javax.swing.GroupLayout jpInfoLayout = new javax.swing.GroupLayout(jpInfo);
        jpInfo.setLayout(jpInfoLayout);
        jpInfoLayout.setHorizontalGroup(
            jpInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpInfoLayout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addComponent(lblClaseTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jpInfoLayout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(lblCuposTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                .addComponent(lblCuposTitulo1)
                .addGap(36, 36, 36))
            .addGroup(jpInfoLayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(lblHorario, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblCupos, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
        );
        jpInfoLayout.setVerticalGroup(
            jpInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpInfoLayout.createSequentialGroup()
                .addComponent(lblClaseTitulo)
                .addGap(18, 18, 18)
                .addGroup(jpInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCuposTitulo)
                    .addComponent(lblCuposTitulo1))
                .addGap(21, 21, 21)
                .addGroup(jpInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCupos)
                    .addComponent(lblHorario))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnRegistrarAsistencia.setBackground(new java.awt.Color(153, 153, 153));
        btnRegistrarAsistencia.setText("Registrar Asistencia");
        btnRegistrarAsistencia.addActionListener(this::btnRegistrarAsistenciaActionPerformed);

        btnLimpiar.setBackground(new java.awt.Color(153, 153, 153));
        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Filtro de busqueda");

        btnBuscarSocio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/listar_usuario.png"))); // NOI18N
        btnBuscarSocio.addActionListener(this::btnBuscarSocioActionPerformed);

        buttonGroup1.add(rbNombre);
        rbNombre.setText("Nombre");

        buttonGroup1.add(rbDni);
        rbDni.setText("DNI");

        buttonGroup1.add(rbID);
        rbID.setText("ID");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblSocioTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblSocioTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSocioTitulo.setText("Socio");

        lblPase.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPase.setText("Pase:");

        lblCantidadClases.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        lblCantidadClases.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCantidadClases.setText("-");

        lblClasesRestantes.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblClasesRestantes.setText("Clases restantes:");

        lblTipoPase.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        lblTipoPase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTipoPase.setText("-");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(119, 119, 119)
                .addComponent(lblSocioTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(lblPase))
                    .addComponent(lblTipoPase, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCantidadClases, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblClasesRestantes))
                .addGap(42, 42, 42))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(lblSocioTitulo)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPase, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblClasesRestantes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCantidadClases)
                    .addComponent(lblTipoPase))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelFormularioLayout = new javax.swing.GroupLayout(panelFormulario);
        panelFormulario.setLayout(panelFormularioLayout);
        panelFormularioLayout.setHorizontalGroup(
            panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormularioLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFormularioLayout.createSequentialGroup()
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelFormularioLayout.createSequentialGroup()
                                .addComponent(lblFecha)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(panelFormularioLayout.createSequentialGroup()
                                .addComponent(cbSocio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnBuscarSocio, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelFormularioLayout.createSequentialGroup()
                                .addComponent(dcFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnRegistrarAsistencia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jpInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(58, 58, 58))
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblClase)
                            .addComponent(lblSocio)
                            .addComponent(cbClase, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBuscarSocio, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelFormularioLayout.createSequentialGroup()
                                .addComponent(rbNombre)
                                .addGap(18, 18, 18)
                                .addComponent(rbDni)
                                .addGap(18, 18, 18)
                                .addComponent(rbID))
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18))))
        );
        panelFormularioLayout.setVerticalGroup(
            panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormularioLayout.createSequentialGroup()
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jpInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelFormularioLayout.createSequentialGroup()
                                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(rbNombre)
                                    .addComponent(rbDni)
                                    .addComponent(rbID))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtBuscarSocio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnBuscarSocio, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblSocio)))
                .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFormularioLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbSocio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblClase)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbClase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFecha)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dcFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRegistrarAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(btnLimpiar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelFormularioLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cbClase, cbSocio});

        panelListado.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tablaAsistencias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Socio", "Clase", "Fecha", "Horario"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaAsistencias);

        lblClaseTitulo1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblClaseTitulo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblClaseTitulo1.setText("Registro de asistencias por socio");

        javax.swing.GroupLayout panelListadoLayout = new javax.swing.GroupLayout(panelListado);
        panelListado.setLayout(panelListadoLayout);
        panelListadoLayout.setHorizontalGroup(
            panelListadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListadoLayout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addGap(13, 13, 13))
            .addGroup(panelListadoLayout.createSequentialGroup()
                .addGap(210, 210, 210)
                .addComponent(lblClaseTitulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(217, Short.MAX_VALUE))
        );
        panelListadoLayout.setVerticalGroup(
            panelListadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelListadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblClaseTitulo1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE))
        );

        btnVolver.setBackground(new java.awt.Color(153, 153, 153));
        btnVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/volver.png"))); // NOI18N
        btnVolver.setText("Volver");
        btnVolver.addActionListener(this::btnVolverActionPerformed);

        btnEliminar.setBackground(new java.awt.Color(153, 153, 153));
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);

        javax.swing.GroupLayout jpAsistenciasLayout = new javax.swing.GroupLayout(jpAsistencias);
        jpAsistencias.setLayout(jpAsistenciasLayout);
        jpAsistenciasLayout.setHorizontalGroup(
            jpAsistenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpAsistenciasLayout.createSequentialGroup()
                .addGroup(jpAsistenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpAsistenciasLayout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpAsistenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnVolver)
                        .addGroup(jpAsistenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpAsistenciasLayout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(btnEliminar))
                            .addGroup(jpAsistenciasLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panelListado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(19, 19, Short.MAX_VALUE))
        );
        jpAsistenciasLayout.setVerticalGroup(
            jpAsistenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpAsistenciasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelListado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnVolver)
                .addContainerGap(150, Short.MAX_VALUE))
        );

        sPanelAsistencia.setViewportView(jpAsistencias);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sPanelAsistencia, javax.swing.GroupLayout.DEFAULT_SIZE, 868, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sPanelAsistencia, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
                .addGap(35, 35, 35))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarAsistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarAsistenciaActionPerformed
    
    // Asignamos a una variable el socio seleccionado en el cbSocio
    Socio socio = buscarSocioSeleccionado();

    if (socio == null) 
    {
        JOptionPane.showMessageDialog(this, "Seleccione un socio.");
        return;
    }

    // Verificamos que haya una fecha seleccionada
    if (dcFecha.getDate() == null) 
    {
        JOptionPane.showMessageDialog(this, "Seleccione una fecha.");
        return;
    }

    // Convertimos la fecha seleccionada
    LocalDate fechaSeleccionada = dcFecha.getDate().toInstant()
            .atZone(java.time.ZoneId.systemDefault()).toLocalDate();

    // Verificamos que el socio haya tenido un pase vigente en esa fecha
    if (!teniaPaseVigente(socio, fechaSeleccionada)) {
        JOptionPane.showMessageDialog(this,"El socio no tenia un pase vigente en la fecha seleccionada.");
        return;
    }

    // Buscamos el pase actual para descontar del saldo acumulado
    Pases pase = buscarPaseActivo(socio);

    if (pase == null || pase.getCantidadPases() <= 0) {
        JOptionPane.showMessageDialog(this,"El socio no tiene clases disponibles.");
        return;
    }

    // Verificamos que haya una clase seleccionada en cbClase
    if (cbClase.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(this, "Seleccione una clase.");
        return;
    }

    // Buscamos la clase seleccionada
    Clases claseSeleccionada = null;
    String seleccion = cbClase.getSelectedItem().toString();

    List<Clases> clases = claseData.listarClasesActivas();

    for (Clases clase : clases) {

        String profesor = clase.getEntrenador().getApellido()
                + ", " + clase.getEntrenador().getNombre();

        String opcion = clase.getNombre() + " - "
                + profesor + " - " + clase.getHorario();

        if (opcion.equals(seleccion)) {
            claseSeleccionada = clase;
            break;
        }
    }

    if (claseSeleccionada == null) {
        JOptionPane.showMessageDialog(this,
                "No se pudo encontrar la clase.");
        return;
    }

    // No se pueden registrar asistencias con fechas futuras
    if (fechaSeleccionada.isAfter(LocalDate.now())) {
        JOptionPane.showMessageDialog(this,
                "La fecha de asistencia no puede ser posterior al dia de hoy.");
        return;
    }

    // Verificamos los cupos disponibles y evitamos asistencias duplicadas
    List<Asistencias> asistenciasDelDia =
            asistenciaData.listarAsistenciasPorDia(fechaSeleccionada);

    int asistenciasRegistradas = 0;

    for (Asistencias a : asistenciasDelDia) {

        if (a.getClase().getIdClase() == claseSeleccionada.getIdClase()) {
            asistenciasRegistradas++;
        }

        if (a.getSocio().getIdSocio() == socio.getIdSocio()
                && a.getClase().getIdClase() == claseSeleccionada.getIdClase()) {

            JOptionPane.showMessageDialog(this,
                    "El socio ya tiene una asistencia registrada para esta clase y fecha.");
            return;
        }
    }

    // Verificamos la capacidad de la clase
    if (asistenciasRegistradas >= claseSeleccionada.getCapacidad()) {
        JOptionPane.showMessageDialog(this,"La clase no tiene cupos disponibles para esta fecha.");
        return;
    }

    // Creamos la asistencia
    Asistencias asistencia = new Asistencias();
    asistencia.setSocio(socio);
    asistencia.setClase(claseSeleccionada);
    asistencia.setFechaAsistencia(fechaSeleccionada);

    // se verifica fecha registrada
    boolean registrado = asistenciaData.registrarAsistencia(asistencia);

    if (!registrado) {
        JOptionPane.showMessageDialog(this,"No se pudo registrar la asistencia.");
        return;
    }

    // Descontamos una entrada del saldo actual del socio
    pase.descontarPase();
    paseData.modificarPase(pase);

    // Actualizamos la cantidad de clases disponibles
    lblCantidadClases.setText(String.valueOf(pase.getCantidadPases()));

    // Actualizamos el historial de asistencias ymostramos en la tabla
    List<Asistencias> asistencias = asistenciaData.obtenerAsistenciasPorSocio(socio.getIdSocio());

    DefaultTableModel modelo =(DefaultTableModel) tablaAsistencias.getModel();

    modelo.setRowCount(0);

    for (Asistencias a : asistencias) {
        modelo.addRow(new Object[]{
            a.getIdAsistencia(),
            a.getSocio().getApellido() + ", " + a.getSocio().getNombre(),
            a.getClase().getNombre(),
            a.getFechaAsistencia(),
            a.getClase().getHorario()
        });
    }

    // Actualizamos los cupos disponibles
    actualizarInfoClase();

    JOptionPane.showMessageDialog(this,"Asistencia registrada correctamente.");

    }//GEN-LAST:event_btnRegistrarAsistenciaActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
    
        cbSocio.setSelectedIndex(-1);
        cbClase.setSelectedIndex(-1);
        dcFecha.setDate(new java.util.Date());
        txtBuscarSocio.setText("");
        lblClaseTitulo.setText("Clase");
        lblHorario.setText("Horario");
        lblCupos.setText("-");
        lblTipoPase.setText("-");
        lblCantidadClases.setText("-");

        DefaultTableModel modelo =(DefaultTableModel) tablaAsistencias.getModel();

        modelo.setRowCount(0);

    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnVolverActionPerformed

    private void cbSocioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbSocioItemStateChanged
       

    if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {

        Socio socio = buscarSocioSeleccionado();

        if (socio != null) {

            Pases pase = buscarPaseActivo(socio);

            if (pase != null) {
                lblTipoPase.setText(pase.getCantidadInicial()+" clases");
                lblCantidadClases.setText(pase.getCantidadPases()+"");

            } else {
                lblTipoPase.setText("Sin pase vigente");
                lblCantidadClases.setText("0");
            }

            // Cargar historial de asistencias del socio
            List<Asistencias> asistencias = asistenciaData.obtenerAsistenciasPorSocio(socio.getIdSocio());

            DefaultTableModel modelo =(DefaultTableModel) tablaAsistencias.getModel();

            modelo.setRowCount(0);

            for (Asistencias asistencia : asistencias) 
            {

                modelo.addRow(new Object[]{
                    asistencia.getIdAsistencia(),
                    asistencia.getSocio().getApellido() + ", " + asistencia.getSocio().getNombre(),
                    asistencia.getClase().getNombre(),
                    asistencia.getFechaAsistencia(),
                    asistencia.getClase().getHorario()
                });
            }
        }
        
    }


    }//GEN-LAST:event_cbSocioItemStateChanged
    
      
    private void btnBuscarSocioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarSocioActionPerformed

        // tiene q haber un radiobutton seleccionado si o si 
        if (!rbNombre.isSelected() && !rbDni.isSelected() && !rbID.isSelected()) 
        {
            JOptionPane.showMessageDialog(this, "Seleccione tipo de busqueda.");
            return;
        }

        String texto = txtBuscarSocio.getText().trim().toLowerCase();

        // Se registro un dato?
        if (texto.isEmpty()) //No
        {
            JOptionPane.showMessageDialog(this, "Ingrese un dato para buscar.");
            return;
        }

        // Validamos la longitud del DNI
        if (rbDni.isSelected() && !texto.matches("[0-9]{7,8}")) 
        {
            JOptionPane.showMessageDialog(this, "El DNI debe tener entre 7 y 8 numeros.");
            return;
        }

        // Validamos la longitud del ID
        if (rbID.isSelected() && !texto.matches("[0-9]{1,4}")) 
        {
            JOptionPane.showMessageDialog(this,"El ID debe tener entre 1 y 4 numeros.");
            return;
        }

        // Limpiamos el combo antes de volver a buscar
        cbSocio.removeAllItems();

        List<Socio> socios = socioData.listarSocios();

        // Buscamos por nombre
        if (rbNombre.isSelected()) {

            for (Socio socio : socios) {

                if (socio.isEstado() && buscarPaseActivo(socio) != null) {

                    String nombreCompleto = socio.getApellido() + ", " + socio.getNombre();

                    if (nombreCompleto.toLowerCase().contains(texto)) {
                        cbSocio.addItem(nombreCompleto + " - ID: " + socio.getIdSocio());
                    }
                }
            }
        }

        // Buscamos por DNI
        else if (rbDni.isSelected()) {

            for (Socio socio : socios) {

                if (socio.isEstado() && buscarPaseActivo(socio) != null) {

                    if (socio.getDni().contains(texto)) {
                        cbSocio.addItem(socio.getApellido() + ", "+ socio.getNombre() + " - ID: " + socio.getIdSocio());
                    }
                }
            }
        }

        // Buscamos por ID
        else if (rbID.isSelected()) {

            for (Socio socio : socios) {

                if (socio.isEstado() && buscarPaseActivo(socio) != null) {

                    if (String.valueOf(socio.getIdSocio()).contains(texto)) {
                        cbSocio.addItem(socio.getApellido() + ", "+ socio.getNombre() + " - ID: " + socio.getIdSocio());
                    }
                }
            }
        }

        cbSocio.setSelectedIndex(-1);

        if (cbSocio.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No se encontraron socios.");
        }

    }//GEN-LAST:event_btnBuscarSocioActionPerformed

    //Esto solo actualiza la informacion q se muestra en el panel informativo
    private void cbClaseItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbClaseItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) 
        {
            actualizarInfoClase();
        }
    }//GEN-LAST:event_cbClaseItemStateChanged

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
    // Verificamos que se haya seleccionado una fila
    int fila = tablaAsistencias.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(this,"Seleccione una asistencia para eliminar.");
        return;
    }

    // Buscamos socio seleccionado
    Socio socio = buscarSocioSeleccionado();

    if (socio == null) 
    {
        JOptionPane.showMessageDialog(this, "Seleccione un socio.");
        return;
    }

    // Obtenemos el ID de la asistencia seleccionada
    int idAsistencia = Integer.parseInt(tablaAsistencias.getValueAt(fila, 0).toString());

    // Buscamos la asistencia para obtener su clase y fecha
    List<Asistencias> asistencias = asistenciaData.obtenerAsistenciasPorSocio(socio.getIdSocio());

    Asistencias asistenciaSeleccionada = null;

    for (Asistencias a : asistencias) {
        if (a.getIdAsistencia() == idAsistencia) {
            asistenciaSeleccionada = a;
            break;
        }
    }

    if (asistenciaSeleccionada == null) {
        JOptionPane.showMessageDialog(this, "No se encontro la asistencia seleccionada.");
        return;
    }

    // Buscamos el pase vigente del socio
    Pases pase = buscarPaseActivo(socio);

    if (pase == null) {
        JOptionPane.showMessageDialog(this,"El socio no tiene un pase vigente. Primero debe registrar un pase nuevo.");
        return;
    }

    int respuesta = JOptionPane.showConfirmDialog(this,"¿Seguro de que desea eliminar esta asistencia?",
            "Confirmar eliminacion",JOptionPane.YES_NO_OPTION);

    if (respuesta != JOptionPane.YES_OPTION)
    {
        return;
    }

    // Guardamos los datos de la clase y fecha antes de eliminar
    Clases claseEliminada = asistenciaSeleccionada.getClase();
    LocalDate fechaEliminada = asistenciaSeleccionada.getFechaAsistencia();

    // Eliminamos la asistencia de la base de datos
    boolean eliminado = asistenciaData.eliminarAsistencia(idAsistencia);

    if (!eliminado) 
    {
        JOptionPane.showMessageDialog(this,"No se pudo eliminar la asistencia.");
        return;
    }

    // Devolvemos una entrada al pase vigente
    pase.setCantidadPases(pase.getCantidadPases() + 1);
    paseData.modificarPase(pase);

    // Actualizamos la cantidad de entradas disponibles
    lblCantidadClases.setText(
            String.valueOf(pase.getCantidadPases()));

    // Seleccionamos la clase que acabamos de modificar
    String profesor = claseEliminada.getEntrenador().getApellido()+ ", " + claseEliminada.getEntrenador().getNombre();

    String opcion = claseEliminada.getNombre() + " - "+ profesor + " - " + claseEliminada.getHorario();

    cbClase.setSelectedItem(opcion);

    // Asignamos al calendario la fecha de la asistencia eliminada
    dcFecha.setDate(java.sql.Date.valueOf(fechaEliminada));

    // Actualizamos el panel de información y los cupos disponibles
    actualizarInfoClase();

    // Actualizamos el historial de asistencias
    List<Asistencias> historial =asistenciaData.obtenerAsistenciasPorSocio(socio.getIdSocio());

    DefaultTableModel modelo =(DefaultTableModel) tablaAsistencias.getModel();

    modelo.setRowCount(0);

    for (Asistencias a : historial) {
        modelo.addRow(new Object[]{
            a.getIdAsistencia(),
            a.getSocio().getApellido() + ", " + a.getSocio().getNombre(),
            a.getClase().getNombre(),
            a.getFechaAsistencia(),
            a.getClase().getHorario()
        });
    }

    JOptionPane.showMessageDialog(this,
            "Asistencia eliminada correctamente.");


    }//GEN-LAST:event_btnEliminarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnBuscarSocio;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnRegistrarAsistencia;
    private javax.swing.JButton btnVolver;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbClase;
    private javax.swing.JComboBox<String> cbSocio;
    private com.toedter.calendar.JDateChooser dcFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jpAsistencias;
    private javax.swing.JPanel jpInfo;
    private javax.swing.JLabel lblCantidadClases;
    private javax.swing.JLabel lblClase;
    private javax.swing.JLabel lblClaseTitulo;
    private javax.swing.JLabel lblClaseTitulo1;
    private javax.swing.JLabel lblClasesRestantes;
    private javax.swing.JLabel lblCupos;
    private javax.swing.JLabel lblCuposTitulo;
    private javax.swing.JLabel lblCuposTitulo1;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblHorario;
    private javax.swing.JLabel lblPase;
    private javax.swing.JLabel lblSocio;
    private javax.swing.JLabel lblSocioTitulo;
    private javax.swing.JLabel lblTipoPase;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel panelFormulario;
    private javax.swing.JPanel panelListado;
    private javax.swing.JRadioButton rbDni;
    private javax.swing.JRadioButton rbID;
    private javax.swing.JRadioButton rbNombre;
    private javax.swing.JScrollPane sPanelAsistencia;
    private javax.swing.JTable tablaAsistencias;
    private javax.swing.JTextField txtBuscarSocio;
    // End of variables declaration//GEN-END:variables
}
