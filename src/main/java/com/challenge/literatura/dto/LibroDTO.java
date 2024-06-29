package com.challenge.literatura.dto;

import com.challenge.literatura.model.Autor;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;

public record LibroDTO(
        Long id,
         String titulo,
         String autor,
         String idioma,
         Integer numeroDescargas,

         Autor autors
) {
}
