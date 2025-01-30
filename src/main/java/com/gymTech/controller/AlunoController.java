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
import com.gymTech.model.AlunoLogin;
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

	@GetMapping("/all")
	public ResponseEntity <List<Aluno>> getAll(){
		
		return ResponseEntity.ok(alunoRepository.findAll());
		
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
	 * Calcula e retorna o IMC e a categoria de um aluno pelo ID.
	 */
	@GetMapping("/calcular-imc/{id}")
	public ResponseEntity<Aluno> calcularImc(@PathVariable Long id) {
		return alunoService.calcularImc(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	
	@PostMapping("/logar")
	public ResponseEntity<AlunoLogin> autenticarUsuario(@RequestBody Optional<AlunoLogin> usuarioLogin){
		
		return alunoService.autenticarUsuario(usuarioLogin)
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
    

	@PostMapping("/cadastrar")
	public ResponseEntity<Aluno> postUsuario(@RequestBody @Valid Aluno aluno) {

		return alunoService.cadastrarUsuario(aluno)
			.map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
			.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

	}

	@PutMapping("/atualizar")
	public ResponseEntity<Aluno> putUsuario(@Valid @RequestBody Aluno aluno) {
		
		return alunoService.atualizarUsuario(aluno)
			.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
			.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		
	}




}
