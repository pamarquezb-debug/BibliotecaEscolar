package util;

/**
 * Utilidad para validar y formatear
 * RUT chilenos.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public final class RutUtil {

    /**
     * Constructor privado para impedir
     * instancias de esta clase utilitaria.
     */
    private RutUtil() {
    }

    /**
     * Valida un RUT chileno utilizando
     * el algoritmo Módulo 11.
     *
     * Acepta formatos como:
     *
     * 12.345.678-5
     * 12345678-5
     * 123456785
     *
     * @param rut RUT a validar
     * @return true si el RUT es válido
     */
    public static boolean validar(String rut) {

        if (rut == null || rut.trim().isEmpty()) {
            return false;
        }

        String limpio = limpiar(rut);

        /*
         * Debe contener cuerpo + dígito verificador.
         */
        if (limpio.length() < 2) {
            return false;
        }

        String cuerpo =
                limpio.substring(
                        0,
                        limpio.length() - 1
                );

        char dvIngresado =
                Character.toUpperCase(
                        limpio.charAt(
                                limpio.length() - 1
                        )
                );

        /*
         * El cuerpo solamente puede
         * contener números.
         */
        if (!cuerpo.matches("\\d+")) {
            return false;
        }

        /*
         * El DV solamente puede ser
         * número o K.
         */
        if (!(Character.isDigit(dvIngresado)
                || dvIngresado == 'K')) {

            return false;
        }

        int suma = 0;
        int multiplicador = 2;

        /*
         * Recorremos el cuerpo desde
         * derecha hacia izquierda.
         */
        for (int i = cuerpo.length() - 1;
             i >= 0;
             i--) {

            int numero =
                    Character.getNumericValue(
                            cuerpo.charAt(i)
                    );

            suma += numero * multiplicador;

            multiplicador++;

            if (multiplicador > 7) {
                multiplicador = 2;
            }
        }

        int resto =
                11 - (suma % 11);

        char dvCalculado;

        if (resto == 11) {

            dvCalculado = '0';

        } else if (resto == 10) {

            dvCalculado = 'K';

        } else {

            dvCalculado =
                    Character.forDigit(
                            resto,
                            10
                    );
        }

        return dvCalculado == dvIngresado;
    }

    /**
     * Elimina puntos, guion y espacios.
     *
     * @param rut RUT original
     * @return RUT limpio
     */
    public static String limpiar(String rut) {

        if (rut == null) {
            return "";
        }

        return rut
                .replace(".", "")
                .replace("-", "")
                .replace(" ", "")
                .toUpperCase();
    }

    /**
     * Formatea el RUT utilizando guion,
     * sin agregar puntos.
     *
     * Ejemplo:
     *
     * 123456785 -> 12345678-5
     *
     * @param rut RUT original
     * @return RUT normalizado
     */
    public static String formatear(String rut) {

        String limpio = limpiar(rut);

        if (limpio.length() < 2) {
            return limpio;
        }

        String cuerpo =
                limpio.substring(
                        0,
                        limpio.length() - 1
                );

        String dv =
                limpio.substring(
                        limpio.length() - 1
                );

        return cuerpo + "-" + dv;
    }
}