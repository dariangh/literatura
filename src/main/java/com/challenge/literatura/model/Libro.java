package com.challenge.literatura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @Column(unique=true)
    private String titulo;

    private String idioma;
    private Integer numeroDescargas;
    @ManyToOne
    private Autor autor;

    public Libro(){

    }



    public Libro(DatosLibro datosLibro, Autor autors) {
        this.id = datosLibro.id();
        this.titulo = datosLibro.titulo();
        this.idioma = datosLibro.idioma().get(0);
        this.numeroDescargas = datosLibro.numeroDescargas();
        this.autor = autors;
    }




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Integer numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autors) {

        this.autor = autors;
    }

    @Override
    public String toString() {
        return

                "\nTitulo = " + titulo  +
                "\nIdioma = " + idioma.replace("[", "").replace("]", "")  +
                "\nNumeroDescargas = " + numeroDescargas +
                "\nAutor = " + autor.getNombre()+"\n" ;

    }
}
