package miguel.uv.es;

import java.io.Serializable;

public class Municipio implements Serializable {
    private String nombre;
    private int codigoPostal;
    private int casos;
    private int fallecimientos;

    public Municipio(String nombre, int codigoPostal, int casos, int fallecimientos) {
        this.nombre = nombre;
        this.codigoPostal = codigoPostal;
        this.casos = casos;
        this.fallecimientos = fallecimientos;
    }

    public Municipio() {

    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCodigoPostal(int codigoPostal) { this.codigoPostal = codigoPostal; }

    public void setCasos(int casos) {
        this.casos = casos;
    }

    public void setFallecimientos(int fallecimientos) { this.fallecimientos = fallecimientos; }

    public String getNombre() {
        return nombre;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public int getCasos() {
        return casos;
    }

    public int getFallecimientos() { return fallecimientos; }
}
