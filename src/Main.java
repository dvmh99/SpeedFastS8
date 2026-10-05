package cl.duoc.speedfast;

import view.VentanaSpeedFast;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            VentanaSpeedFast ventana = new VentanaSpeedFast();
            ventana.setVisible(true);
        });
    }
}