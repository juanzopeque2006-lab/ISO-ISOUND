package com.isoundmusic;

public class App {
    public static void main(String[] args) {
        System.out.println("Hola ISoundMusic!");
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new MainFrame().setVisible(true);
        });
    }

    public static int sumar(int a, int b) {
        return a + b;
    }
}
