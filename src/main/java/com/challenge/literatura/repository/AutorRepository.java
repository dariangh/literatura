package com.challenge.literatura.repository;

import com.challenge.literatura.model.Autor;
import com.challenge.literatura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor,Long> {
    Autor findByNombreContainsIgnoreCase(String nombre);
//    Autor findByNombreContainsIgnoreCaseGroupBy
    @Query("SELECT a FROM Autor a  WHERE a.fechaDeNacimiento<=:year AND a.fechaDeMuerte>:year")
    List<Autor> obtenerAutoresVivosPorYear( int year);



}
