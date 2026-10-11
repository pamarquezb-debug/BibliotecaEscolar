import vista.VentanaLogin;

import javax.swing.*;

/**
 * Punto de entrada de Biblioteca Escolar.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {

                UIManager.setLookAndFeel(
                        UIManager
                                .getSystemLookAndFeelClassName()
                );

            } catch (Exception e) {

                System.err.println(
                        "No fue posible cargar "
                                + "el LookAndFeel del sistema."
                );
            }

            new VentanaLogin()
                    .setVisible(true);
        });
    }
}