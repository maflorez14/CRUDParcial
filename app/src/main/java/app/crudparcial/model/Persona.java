package app.crudparcial.model;

public class Persona {
    public String id;
    public String codigo;
    public String nombre;
    public String genero;

    public Persona() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id; }

    public String getCodigo() { return codigo; }

    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return nombre + "  |  "+codigo+ "  |  "+ genero;
    }
}