package com.gymTech.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_exercicios")
public class Exercicio {
			
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "O atributo 'Nome' é obrigatório!")
	@Size(min = 5, max = 100, message = "O nome precisa conter no mínimo 5 e no máximo 100 caracteres")
	private String nome;
	
	@NotNull(message =  "O atributo 'Repetição' não pode ser nulo")
	private Integer repeticao;
	
	@NotNull(message =  "O atributo 'Série' não pode ser nulo")
	private Integer serie;
	
	@NotBlank(message = "O atributo 'Descrição' é obrigatório!")
	@Size(min = 5, max = 100, message = "A Descrição precisa conter no mínimo 5 e no máximo 100 caracteres")
	private String descricao;
	
	
	@ManyToOne
	@JsonIgnoreProperties("exercicio")
	private Categoria categoria;
	
	@ManyToOne
	@JsonIgnoreProperties("exercicio")
	private Aluno aluno;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getRepeticao() {
		return repeticao;
	}

	public void setRepeticao(Integer repeticao) {
		this.repeticao = repeticao;
	}

	public Integer getSerie() {
		return serie;
	}

	public void setSerie(Integer serie) {
		this.serie = serie;
	}
	

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
}