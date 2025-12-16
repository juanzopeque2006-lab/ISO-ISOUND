package com.isoundmusic.presentacion;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.isoundmusic.dominio.gestores.GestorMusica;

public class ConsultarPlaylistFrame extends JFrame {
    private final JTable tablaPlaylists;
    private final DefaultTableModel modelo;
    private final JTextField filtroField;

    public ConsultarPlaylistFrame(Frame owner) {
        super("Consultar Playlists");
        setSize(640, 420);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JLabel titulo = new JLabel("Playlists en la BBDD", SwingConstants.LEFT);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        add(titulo, BorderLayout.NORTH);

        JPanel filtroPanel = new JPanel(new BorderLayout(6, 6));
        filtroField = new JTextField();
        JButton filtrarBtn = new JButton("Filtrar");
        filtroPanel.add(new JLabel("Filtro por nombre:"), BorderLayout.WEST);
        filtroPanel.add(filtroField, BorderLayout.CENTER);
        filtroPanel.add(filtrarBtn, BorderLayout.EAST);

        modelo = new DefaultTableModel(new String[] { "ID", "Nombre" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPlaylists = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tablaPlaylists);

        JPanel center = new JPanel(new BorderLayout(6, 6));
        center.add(filtroPanel, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JButton cerrarBtn = new JButton("Cerrar");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(cerrarBtn);
        add(actions, BorderLayout.SOUTH);

        filtrarBtn.addActionListener(e -> cargarPlaylists());
        cerrarBtn.addActionListener(e -> dispose());

        cargarPlaylists();
    }

    private void cargarPlaylists() {
        String filtro = filtroField.getText().trim();
        List<String[]> data = new GestorMusica().listarPlaylists(filtro);
        modelo.setRowCount(0);
        for (String[] r : data) {
            modelo.addRow(new Object[] { r[0], r[1] });
        }
    }
}
