package modelo;

/**
 * Representa un libro de la biblioteca.
 *
 * Implementa Prestable y protege mediante
 * synchronized las operaciones que modifican stock.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class Libro implements Prestable {

    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private String editorial;
    private int stock;
    private int idCategoria;

    public Libro() {
    }

    public Libro(int id,
                 String titulo,
                 String autor,
                 String isbn,
                 String editorial,
                 int stock,
                 int idCategoria) {

        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.editorial = editorial;
        this.stock = stock;
        this.idCategoria = idCategoria;
    }

    public Libro(String titulo,
                 String autor,
                 String isbn,
                 String editorial,
                 int stock,
                 int idCategoria) {

        this(
                0,
                titulo,
                autor,
                isbn,
                editorial,
                stock,
                idCategoria
        );
    }

    @Override
    public synchronized boolean estaDisponible() {
        return stock > 0;
    }

    @Override
    public synchronized boolean prestar() {

        if (stock <= 0) {
            return false;
        }

        stock--;
        return true;
    }

    @Override
    public synchronized void devolver() {
        stock++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public synchronized int getStock() {
        return stock;
    }

    public synchronized void setStock(int stock) {
        this.stock = stock;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    @Override
    public String toString() {
        return titulo + " - " + autor +
                " (Stock: " + getStock() + ")";
    }
}