package org.alvarowau.democrud.modelo;

import java.util.Date;

public class Persona {

    private int id;
    private String nombre;
    private String apellidos;
    private Date fechaNac;
    private int experiencia;

    public Persona() {
        this.id = 0;
        this.nombre = "";
        this.apellidos = "";
        this.fechaNac = null;
        this.experiencia = 0;
    }

    public Persona(int id, String nombre, String apellidos, Date fechaNac, int experiencia) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNac = fechaNac;
        this.experiencia = experiencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    @Override
    public String toString() {
        return "Persona{id=" + id + ", nombre='" + nombre + "', apellidos='" + apellidos + "', fechaNac=" + fechaNac + ", experiencia=" + experiencia + '}';
    }

}
