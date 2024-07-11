package com.desafio_literalura.literalura.repositoy;

import com.desafio_literalura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Libro findByTituloContainsIgnoreCase(String titulo);

    List<Libro> findByIdiomas(String idioma);
}
