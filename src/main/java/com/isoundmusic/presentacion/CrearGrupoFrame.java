package com.isoundmusic.presentacion;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.isoundmusic.dominio.gestores.GestorGrupo;

public class CrearGrupoFrame extends JFrame {
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
        JButton buscarBtn = new JButton("Buscar amigos");
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
                return column == 2;
            }
        };
        tablaAmigos = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaAmigos);

        JPanel center = new JPanel(new BorderLayout(6, 6));
        center.add(searchPanel, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JButton crearBtn = new JButton("Crear Grupo");
        JButton confirmarBtn = new JButton("Confirmar Lista");
        JButton cerrarBtn = new JButton("Cerrar");
        crearBtn.setEnabled(false);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(cerrarBtn);
        actions.add(confirmarBtn);
        actions.add(crearBtn);
        add(actions, BorderLayout.SOUTH);

        // Listeners
        buscarBtn.addActionListener(e -> seleccionarAmigos());
        confirmarBtn.addActionListener(e -> confirmarCreacion(crearBtn));
        crearBtn.addActionListener(e -> registrarGrupo());
        cerrarBtn.addActionListener(e -> dispose());
    }

    // Búsqueda en gestor (stub por ahora)
    private void seleccionarAmigos() {
        List<String[]> resultados = new GestorGrupo().getAmigos();
        modeloTabla.setRowCount(0);
        for (String[] r : resultados) {
            modeloTabla.addRow(new Object[] { r[0], r[1], Boolean.FALSE });
        }
    }

    // Recoge seleccionados y delega creación al gestor
    private void registrarGrupo() {
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

    // Confirma la lista de miembros antes de permitir la creación
    private void confirmarCreacion(JButton crearBtn) {
        List<String> miembros = new ArrayList<>();
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            Boolean sel = (Boolean) modeloTabla.getValueAt(i, 2);
            if (Boolean.TRUE.equals(sel)) {
                miembros.add((String) modeloTabla.getValueAt(i, 1));
            }
        }
        if (miembros.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona al menos un amigo antes de confirmar", "Sin miembros",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String m : miembros) {
            sb.append("- ").append(m).append("\n");
        }
        int resp = JOptionPane.showConfirmDialog(this, "Confirmar lista de miembros:\n" + sb.toString(),
                "Confirmar lista",
                JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            crearBtn.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Lista confirmada. Ahora puedes crear el grupo.");
        }
    }
}