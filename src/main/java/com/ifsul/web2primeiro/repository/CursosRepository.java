package com.ifsul.web2primeiro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ifsul.web2primeiro.model.Cursos;

public interface CursosRepository extends JpaRepository <Cursos, Integer> {
    List<Cursos> findByNomeContainingIgnoreCase(String termo);
}
