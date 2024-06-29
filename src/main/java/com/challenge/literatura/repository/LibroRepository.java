package com.challenge.literatura.repository;

import com.challenge.literatura.model.Autor;
import com.challenge.literatura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro,Long> {
    Libro findFirstByTitulo(String titulo);
    Optional<Libro> findByTituloContainsIgnoreCase(String titulo);
    List<Libro> findByIdioma(String idioma);
    List<Libro> findTop5ByOrderByNumeroDescargasDesc();
}
