package com.isoundmusic;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.isoundmusic.presentacion.MainFrame;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Tema nativo del sistema operativo
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                    | UnsupportedLookAndFeelException e) {
                log.error("No se pudo establecer LookAndFeel", e);
            }

            log.info("Iniciando ISoundMusic...");
            new MainFrame().setVisible(true);
            log.info("UI mostrada");
        });
    }
}
