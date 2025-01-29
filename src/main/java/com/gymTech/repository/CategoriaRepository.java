package com.gymTech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gymTech.model.Categoria;

public interface CategoriaRepository  extends JpaRepository<Categoria, Long> {
	
   public List<Categoria> findAllByTipoContainingIgnoreCase(String tipo);

}
