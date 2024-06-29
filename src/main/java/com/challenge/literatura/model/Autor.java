package com.challenge.literatura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name="autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String nombre;
    private Integer fechaDeNacimiento;
    private Integer fechaDeMuerte;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor(DatosAutor datosAutor) {

        this.nombre = datosAutor.nombre();
        this.fechaDeNacimiento = datosAutor.fechaDeNacimiento();
        this.fechaDeMuerte = datosAutor.fechaDeMuerte();


    }

    public Autor() {

    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Integer fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Integer getFechaDeMuerte() {
        return fechaDeMuerte;
    }

    public void setFechaDeMuerte(Integer fechaDeMuerte) {
        this.fechaDeMuerte = fechaDeMuerte;
    }


    public List<Libro> getLibro() {
        return libros;
    }

    public void setLibros(List<Libro> libro) {
        libros.forEach(e -> e.setAutor((this)));
        this.libros = libro;
    }

    @Override
    public String toString() {
        return
                "\nId = " + Id +
                " \nNombre = " + nombre  +
                " \nFecha De Nacimiento = " + fechaDeNacimiento +
                " \nFecha De Muerte = " + fechaDeMuerte +
                " \nLibros = " + libros.stream()
                        .map(Libro::getTitulo)
                        .collect(Collectors.joining(", ")) ;
    }
}
