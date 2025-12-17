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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.isoundmusic.dominio.gestores.GestorMusica;

public class CrearPlaylistFrame extends JFrame {
    private final JTextField nombreField;

    public CrearPlaylistFrame(Frame owner) {
        super("Crear Playlist");
        setSize(420, 200);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JLabel titulo = new JLabel("Crear Playlist", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        add(titulo, BorderLayout.NORTH);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        center.add(new JLabel("Nombre:"));
        nombreField = new JTextField(20);
        center.add(nombreField);
        add(center, BorderLayout.CENTER);

        JButton crearBtn = new JButton("Crear Playlist");
        JButton cerrarBtn = new JButton("Cerrar");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(cerrarBtn);
        actions.add(crearBtn);
        add(actions, BorderLayout.SOUTH);

        crearBtn.addActionListener(e -> crearPlaylist());
        cerrarBtn.addActionListener(e -> dispose());
    }

    private void crearPlaylist() {
        String nombre = nombreField.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Indica un nombre", "Falta nombre", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            new GestorMusica().crearPlaylist(nombre);
            JOptionPane.showMessageDialog(this, "Playlist creada correctamente");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear playlist: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
