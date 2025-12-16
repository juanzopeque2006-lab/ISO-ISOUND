package com.isoundmusic.presentacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.isoundmusic.dominio.gestores.GestorGrupo;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CrearGrupoFrame extends JFrame {
    private final JTextField campoBusqueda;
    private final JTable tablaAmigos;
    private final DefaultTableModel modeloTabla;
    private final JTextField nombreGrupoField;

    public CrearGrupoFrame(Frame owner) {
        super("Crear Grupo");
        setSize(640, 420);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JPanel header = new JPanel(new BorderLayout(8, 8));
        JLabel titulo = new JLabel("Crear Grupo", SwingConstants.LEFT);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        header.add(titulo, BorderLayout.WEST);

        JPanel nombrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nombrePanel.add(new JLabel("Nombre del grupo:"));
        nombreGrupoField = new JTextField(20);
        nombrePanel.add(nombreGrupoField);
        header.add(nombrePanel, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new BorderLayout(6, 6));
        campoBusqueda = new JTextField();
        JButton buscarBtn = new JButton("Buscar amigos");
        searchPanel.add(campoBusqueda, BorderLayout.CENTER);
        searchPanel.add(buscarBtn, BorderLayout.EAST);

        modeloTabla = new DefaultTableModel(new String[] { "ID", "Nombre", "Seleccionado" }, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2)
                    return Boolean.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Solo la columna de seleccionado es editable
            }
        };
        tablaAmigos = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaAmigos);

        JPanel center = new JPanel(new BorderLayout(6, 6));
        center.add(searchPanel, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JButton crearBtn = new JButton("Crear Grupo");
        JButton cerrarBtn = new JButton("Cerrar");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(cerrarBtn);
        actions.add(crearBtn);
        add(actions, BorderLayout.SOUTH);

        // Listeners
        buscarBtn.addActionListener(e -> realizarBusqueda());
        crearBtn.addActionListener(e -> crearGrupo());
        cerrarBtn.addActionListener(e -> dispose());
    }

    // Búsqueda en gestor (stub por ahora)
    private void realizarBusqueda() {
        String query = campoBusqueda.getText().trim();
        List<String[]> resultados = new GestorGrupo().buscarAmigosPorNombre(query);
        modeloTabla.setRowCount(0);
        for (String[] r : resultados) {
            modeloTabla.addRow(new Object[] { r[0], r[1], Boolean.FALSE });
        }
    }

    // Recoge seleccionados y delega creación al gestor
    private void crearGrupo() {
        String nombre = nombreGrupoField.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Indica un nombre de grupo", "Falta nombre",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<String> miembros = new ArrayList<>();
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            Boolean sel = (Boolean) modeloTabla.getValueAt(i, 2);
            if (Boolean.TRUE.equals(sel)) {
                miembros.add((String) modeloTabla.getValueAt(i, 1));
            }
        }
        if (miembros.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona al menos un amigo", "Sin miembros",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            new GestorGrupo().crearGrupo(nombre, miembros);
            JOptionPane.showMessageDialog(this, "Grupo '" + nombre + "' creado con " + miembros.size() + " miembros.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear grupo: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
/*
 * package com.isoundmusic;
 * 
 * import javax.swing.*;
 * import javax.swing.table.DefaultTableModel;
 * import java.awt.*;
 * import java.util.ArrayList;
 * import java.util.List;
 * import com.isoundmusic.gestores.GestorGrupo;
 * 
 * public class CrearGrupoFrame extends JFrame {
 * private final JTextField campoBusqueda;
 * private final JTable tablaAmigos;
 * private final DefaultTableModel modeloTabla;
 * private final JTextField nombreGrupoField;
 * 
 * public CrearGrupoFrame(Frame owner) {
 * super("Crear Grupo");
 * setSize(640, 420);
 * setLocationRelativeTo(owner);
 * setDefaultCloseOperation(DISPOSE_ON_CLOSE);
 * setLayout(new BorderLayout(8, 8));
 * 
 * JPanel header = new JPanel(new BorderLayout(8, 8));
 * JLabel titulo = new JLabel("Crear Grupo", SwingConstants.LEFT);
 * titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
 * header.add(titulo, BorderLayout.WEST);
 * 
 * JPanel nombrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
 * nombrePanel.add(new JLabel("Nombre del grupo:"));
 * nombreGrupoField = new JTextField(20);
 * nombrePanel.add(nombreGrupoField);
 * header.add(nombrePanel, BorderLayout.SOUTH);
 * add(header, BorderLayout.NORTH);
 * 
 * JPanel searchPanel = new JPanel(new BorderLayout(6, 6));
 * campoBusqueda = new JTextField();
 * JButton buscarBtn = new JButton("Buscar amigos");
 * searchPanel.add(campoBusqueda, BorderLayout.CENTER);
 * searchPanel.add(buscarBtn, BorderLayout.EAST);
 * 
 * modeloTabla = new DefaultTableModel(new String[] { "ID", "Nombre",
 * "Seleccionado" }, 0) {
 * 
 * @Override
 * public Class<?> getColumnClass(int columnIndex) {
 * if (columnIndex == 2)
 * return Boolean.class;
 * return String.class;
 * }
 * 
 * @Override
 * public boolean isCellEditable(int row, int column) {
 * return column == 2; // Solo la columna de seleccionado es editable
 * }
 * };
 * tablaAmigos = new JTable(modeloTabla);
 * JScrollPane scroll = new JScrollPane(tablaAmigos);
 * 
 * JPanel center = new JPanel(new BorderLayout(6, 6));
 * center.add(searchPanel, BorderLayout.NORTH);
 * center.add(scroll, BorderLayout.CENTER);
 * add(center, BorderLayout.CENTER);
 * 
 * JButton crearBtn = new JButton("Crear Grupo");
 * JButton cerrarBtn = new JButton("Cerrar");
 * JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
 * actions.add(cerrarBtn);
 * actions.add(crearBtn);
 * add(actions, BorderLayout.SOUTH);
 * 
 * // Listeners
 * buscarBtn.addActionListener(e -> realizarBusqueda());
 * crearBtn.addActionListener(e -> crearGrupo());
 * cerrarBtn.addActionListener(e -> dispose());
 * }
 * 
 * // Búsqueda en gestor (stub por ahora)
 * private void realizarBusqueda() {
 * String query = campoBusqueda.getText().trim();
 * List<String[]> resultados = new GestorGrupo().buscarAmigosPorNombre(query);
 * modeloTabla.setRowCount(0);
 * for (String[] r : resultados) {
 * modeloTabla.addRow(new Object[] { r[0], r[1], Boolean.FALSE });
 * }
 * }
 * 
 * // Recoge seleccionados y delega creación al gestor
 * private void crearGrupo() {
 * String nombre = nombreGrupoField.getText().trim();
 * if (nombre.isEmpty()) {
 * JOptionPane.showMessageDialog(this, "Indica un nombre de grupo",
 * "Falta nombre",
 * JOptionPane.WARNING_MESSAGE);
 * return;
 * }
 * List<String> miembros = new ArrayList<>();
 * for (int i = 0; i < modeloTabla.getRowCount(); i++) {
 * Boolean sel = (Boolean) modeloTabla.getValueAt(i, 2);
 * if (Boolean.TRUE.equals(sel)) {
 * miembros.add((String) modeloTabla.getValueAt(i, 1));
 * }
 * }
 * if (miembros.isEmpty()) {
 * JOptionPane.showMessageDialog(this, "Selecciona al menos un amigo",
 * "Sin miembros",
 * JOptionPane.WARNING_MESSAGE);
 * return;
 * }
 * try {
 * new GestorGrupo().crearGrupo(nombre, miembros);
 * JOptionPane.showMessageDialog(this, "Grupo '" + nombre + "' creado con " +
 * miembros.size() + " miembros.");
 * dispose();
 * } catch (Exception ex) {
 * JOptionPane.showMessageDialog(this, "Error al crear grupo: " +
 * ex.getMessage(), "Error",
 * JOptionPane.ERROR_MESSAGE);
 * }
 * }
 * }
 * package com.isoundmusic;
 * 
 * import java.awt.Frame;
 * import java.util.ArrayList;
 * import java.util.List;
 * 
 * import javax.swing.JFrame;
 * import javax.swing.JOptionPane;
 * import javax.swing.JTable;
 * import javax.swing.JTextField;
 * import javax.swing.table.DefaultTableModel;
 * 
 * import com.isoundmusic.gestores.GestorGrupo;
 * 
 * public class CrearGrupoFrame extends JFrame {
 * private final JTextField campoBusqueda;
 * private final JTable tablaAmigos;
 * private final DefaultTableModel modeloTabla;
 * private final JTextField nombreGrupoField;
 * 
 * public CrearGrupoFrame(Frame owner) {
 * super("Crear Grupo");
 * setSize(640, 420);
 * setLocationRelativeTo(owner);
 * setDefaultCloseOperation(DISPOSE_ON_CLOSE);
 * setLayout(new BorderLayout(8, 8));
 * 
 * JPanel header = new JPanel(new BorderLayout(8, 8));
 * JLabel titulo = new JLabel("Crear Grupo", SwingConstants.LEFT);
 * titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
 * header.add(titulo, BorderLayout.WEST);
 * 
 * JPanel nombrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
 * nombrePanel.add(new JLabel("Nombre del grupo:"));
 * nombreGrupoField = new JTextField(20);
 * nombrePanel.add(nombreGrupoField);
 * header.add(nombrePanel, BorderLayout.SOUTH);
 * add(header, BorderLayout.NORTH);
 * 
 * JPanel searchPanel = new JPanel(new BorderLayout(6, 6));
 * campoBusqueda = new JTextField();
 * JButton buscarBtn = new JButton("Buscar amigos");
 * searchPanel.add(campoBusqueda, BorderLayout.CENTER);
 * searchPanel.add(buscarBtn, BorderLayout.EAST);
 * 
 * modeloTabla = new DefaultTableModel(new String[] { "ID", "Nombre",
 * "Seleccionado" }, 0) {
 * 
 * @Override
 * public Class<?> getColumnClass(int columnIndex) {
 * if (columnIndex == 2)
 * return Boolean.class;
 * return String.class;
 * }
 * 
 * @Override
 * public boolean isCellEditable(int row, int column) {
 * return column == 2; // Solo la columna de seleccionado es editable
 * }
 * };
 * tablaAmigos = new JTable(modeloTabla);
 * JScrollPane scroll = new JScrollPane(tablaAmigos);
 * 
 * JPanel center = new JPanel(new BorderLayout(6, 6));
 * center.add(searchPanel, BorderLayout.NORTH);
 * center.add(scroll, BorderLayout.CENTER);
 * add(center, BorderLayout.CENTER);
 * 
 * JButton crearBtn = new JButton("Crear Grupo");
 * JButton cerrarBtn = new JButton("Cerrar");
 * JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
 * actions.add(cerrarBtn);
 * actions.add(crearBtn);
 * add(actions, BorderLayout.SOUTH);
 * 
 * // Listeners
 * buscarBtn.addActionListener(e -> realizarBusqueda());
 * crearBtn.addActionListener(e -> crearGrupo());
 * cerrarBtn.addActionListener(e -> dispose());
 * buscarBtn.addActionListener(e -> realizarBusqueda());
 * crearBtn.addActionListener(e -> crearGrupo());
 * cerrarBtn.addActionListener(e -> dispose());
 * // Simula una búsqueda en la BBDD. Sustituir por llamada real a persistencia
 * private void realizarBusqueda() {
 * String query = campoBusqueda.getText().trim().toLowerCase();
 * List<String[]> resultados = datosSimulados();
 * String query = campoBusqueda.getText().trim();
 * List<String[]> resultados = new GestorGrupo().buscarAmigosPorNombre(query);
 * if (query.isEmpty() || r[1].toLowerCase().contains(query)) {
 * modeloTabla.addRow(new Object[] { r[0], r[1], Boolean.FALSE });
 * modeloTabla.addRow(new Object[]{r[0], r[1], Boolean.FALSE});
 * 
 * // Recoge los seleccionados y muestra confirmación. Sustituir por inserción
 * en
 * // BBDD
 * private void crearGrupo() {
 * String nombre = nombreGrupoField.getText().trim();
 * if (nombre.isEmpty()) {
 * JOptionPane.showMessageDialog(this, "Indica un nombre de grupo",
 * "Falta nombre",
 * JOptionPane.WARNING_MESSAGE);
 * return;
 * }
 * List<String> miembros = new ArrayList<>();
 * for (int i = 0; i < modeloTabla.getRowCount(); i++) {
 * Boolean sel = (Boolean) modeloTabla.getValueAt(i, 2);
 * if (Boolean.TRUE.equals(sel)) {
 * miembros.add((String) modeloTabla.getValueAt(i, 1));
 * }
 * }
 * if (miembros.isEmpty()) {
 * JOptionPane.showMessageDialog(this, "Selecciona al menos un amigo",
 * "Sin miembros",
 * JOptionPane.WARNING_MESSAGE);
 * return;
 * }
 * JOptionPane.showMessageDialog(this, "Grupo '" + nombre + "' creado con " +
 * miembros.size() + " miembros.");
 * dispose();
 * try {
 * new GestorGrupo().crearGrupo(nombre, miembros);
 * JOptionPane.showMessageDialog(this, "Grupo '" + nombre + "' creado con " +
 * miembros.size() + " miembros.");
 * dispose();
 * } catch (Exception ex) {
 * JOptionPane.showMessageDialog(this, "Error al crear grupo: " +
 * ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
 * }
 * 
 * // Datos de ejemplo. Reemplazar con consulta real (persistencia)
 * private List<String[]> datosSimulados() {
 * 
 */
