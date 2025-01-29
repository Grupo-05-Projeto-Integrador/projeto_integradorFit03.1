package com.gymTech.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gymTech.model.Aluno;
import com.gymTech.repository.AlunoRepository;
import com.gymTech.service.AlunoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/alunos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AlunoController {

	@Autowired
	private AlunoService alunoService;

	@Autowired
	private AlunoRepository alunoRepository;

	/**
	 * Retorna todos os alunos. Calcula e atualiza o IMC de cada aluno antes de
	 * retornar a lista.
	 */
	@GetMapping("/all")
	public ResponseEntity<List<Aluno>> getAll() {
		List<Aluno> alunos = alunoRepository.findAll();

		if (alunos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		// Atualiza o IMC e a categoria para cada aluno antes de retornar
		alunos.forEach(aluno -> alunoService.calcularImc(aluno.getId()));

		return ResponseEntity.ok(alunos);
	}

	/**
	 * Retorna um aluno pelo ID. Calcula e atualiza o IMC do aluno antes de retornar
	 * os dados.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Aluno> getById(@PathVariable Long id) {
		return alunoRepository.findById(id).map(aluno -> {
			alunoService.calcularImc(id); // Calcula e atualiza o IMC
			return ResponseEntity.ok(aluno);
		}).orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Retorna uma lista de alunos cujo nome contém o termo especificado (ignora
	 * maiúsculas/minúsculas).
	 */
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Aluno>> getByNome(@PathVariable String nome) {
		List<Aluno> alunos = alunoRepository.findAllByNomeContainingIgnoreCase(nome);

		if (alunos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(alunos);
	}
	

	/**
	 * Calcula e retorna o IMC e a categoria de um aluno pelo ID.
	 */
	@GetMapping("/calcular-imc/{id}")
	public ResponseEntity<Aluno> calcularImc(@PathVariable Long id) {
		return alunoService.calcularImc(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	/**
	 * Cria um novo aluno.
	 */
	@PostMapping
	public ResponseEntity<Aluno> post(@Valid @RequestBody Aluno aluno) {
		return ResponseEntity.status(HttpStatus.CREATED).body(alunoRepository.save(aluno));
	}

	/**
	 * Atualiza os dados de um aluno existente.
	 */
	@PutMapping
	public ResponseEntity<Aluno> put(@Valid @RequestBody Aluno aluno) {
		return alunoRepository.findById(aluno.getId())
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(alunoRepository.save(aluno)))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	/**
	 * Deleta um aluno pelo ID.
	 */
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Aluno> aluno = alunoRepository.findById(id);

		if (aluno.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		alunoRepository.deleteById(id);
	}
}
