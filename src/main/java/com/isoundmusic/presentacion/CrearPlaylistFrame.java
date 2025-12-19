package com.isoundmusic.presentacion;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;

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

import com.isoundmusic.dominio.gestores.GestorMusica;

public class CrearPlaylistFrame extends JFrame {
    private final JTextField nombreField;
    private final JTable tablaPlaylists;
    private final DefaultTableModel modelo;

    public CrearPlaylistFrame(Frame owner) {
        super("Crear Playlist");
        setSize(640, 420);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JLabel titulo = new JLabel("Crear Playlist", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        add(titulo, BorderLayout.NORTH);

        JPanel inputs = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        inputs.add(new JLabel("Nombre:"));
        nombreField = new JTextField(20);
        inputs.add(nombreField);
        // Se elimina filtro y botón listar para simplificar la UI

        modelo = new DefaultTableModel(new String[] { "ID", "Nombre" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPlaylists = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tablaPlaylists);

        JPanel center = new JPanel(new BorderLayout(6, 6));
        center.add(inputs, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JButton crearBtn = new JButton("Crear Playlist");
        JButton cerrarBtn = new JButton("Cerrar");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(cerrarBtn);
        actions.add(crearBtn);
        add(actions, BorderLayout.SOUTH);

        crearBtn.addActionListener(e -> crearPlaylist());
        cerrarBtn.addActionListener(e -> dispose());

        cargarPlaylists();
    }

    /**
     * Crea una nueva playlist con el nombre indicado en el campo de texto
     */
    private void crearPlaylist() {
        String nombre = nombreField.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Indica un nombre", "Falta nombre", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            new GestorMusica().crearPlaylist(nombre);
            JOptionPane.showMessageDialog(this, "Playlist creada correctamente");
            nombreField.setText("");
            cargarPlaylists();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear playlist: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga las playlists desde la base de datos y las muestra en la tabla
     */
    private void cargarPlaylists() {
        java.util.List<String[]> data = new GestorMusica().listarPlaylists(null);
        modelo.setRowCount(0);
        for (String[] r : data) {
            modelo.addRow(new Object[] { r[0], r[1] });
        }
    }
}
