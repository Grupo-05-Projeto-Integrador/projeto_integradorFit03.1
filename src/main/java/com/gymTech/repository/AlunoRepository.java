package com.gymTech.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.gymTech.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
	
	public Optional<Aluno> findById(Long id);
	
	public List <Aluno> findAllByNomeContainingIgnoreCase(@Param("nome") String nome);

	
}
