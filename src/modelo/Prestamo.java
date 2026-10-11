package modelo;

import java.time.LocalDate;

/**
 * Representa un préstamo realizado
 * por un estudiante.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class Prestamo {

    private static final int DIAS_PRESTAMO = 7;

    private int id;
    private int idEstudiante;
    private int idLibro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private boolean devuelto;

    public Prestamo() {
    }

    public Prestamo(int id,
                    int idEstudiante,
                    int idLibro,
                    LocalDate fechaPrestamo,
                    LocalDate fechaDevolucion,
                    boolean devuelto) {

        this.id = id;
        this.idEstudiante = idEstudiante;
        this.idLibro = idLibro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.devuelto = devuelto;
    }

    /**
     * Constructor para registrar un préstamo nuevo.
     */
    public Prestamo(int idEstudiante, int idLibro) {

        this.idEstudiante = idEstudiante;
        this.idLibro = idLibro;

        this.fechaPrestamo = LocalDate.now();

        /*
         * Fecha prevista de devolución.
         */
        this.fechaDevolucion =
                fechaPrestamo.plusDays(DIAS_PRESTAMO);

        this.devuelto = false;
    }

    /**
     * Calcula la fecha de vencimiento.
     */
    public LocalDate calcularFechaVencimiento() {

        return fechaDevolucion;
    }

    /**
     * Determina si un préstamo se encuentra atrasado.
     */
    public boolean estaAtrasado() {

        return !devuelto
                && fechaDevolucion != null
                && LocalDate.now()
                .isAfter(fechaDevolucion);
    }

    /**
     * Registra la devolución.
     */
    public void registrarDevolucion() {

        this.devuelto = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(
            LocalDate fechaPrestamo) {

        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(
            LocalDate fechaDevolucion) {

        this.fechaDevolucion =
                fechaDevolucion;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }

    @Override
    public String toString() {

        return "Préstamo #" + id
                + " - Estudiante: "
                + idEstudiante
                + " - Libro: "
                + idLibro;
    }
}