package com.isoundmusic.presentacion;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;

import com.isoundmusic.persistencia.DataSeeder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainFrame extends JFrame {
    public MainFrame() {
        super("ISoundMusic");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 320);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("ISoundMusic", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        add(title, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(3, 1, 12, 12));
        JButton crearGrupoBtn = new JButton("Crear Grupo");
        JButton crearPlaylistBtn = new JButton("Crear Playlist");
        JButton seedBtn = new JButton("Generar datos de prueba");

        crearGrupoBtn.addActionListener((ActionEvent e) -> {
            new CrearGrupoFrame(this).setVisible(true);
        });

        crearPlaylistBtn.addActionListener((ActionEvent e) -> {
            new CrearPlaylistFrame(this).setVisible(true);
        });

        seedBtn.addActionListener((ActionEvent e) -> {
            try {
                log.info("Iniciando carga de datos de prueba");
                DataSeeder.seedDemoData();
                JOptionPane.showMessageDialog(this, "Datos de prueba cargados correctamente.");
                log.info("Datos de prueba cargados");
            } catch (Exception ex) {
                log.error("Error al cargar datos de prueba", ex);
                JOptionPane.showMessageDialog(this, "Error al cargar datos de prueba: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        buttons.add(crearGrupoBtn);
        buttons.add(crearPlaylistBtn);
        buttons.add(seedBtn);

        JPanel center = new JPanel(new GridBagLayout());
        center.add(buttons);
        add(center, BorderLayout.CENTER);
    }
}
