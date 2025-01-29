package com.gymTech.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "tb_categorias")
public class Categoria {

		@Id //CHAVE PRIVARIA 
		@GeneratedValue (strategy = GenerationType.IDENTITY) //AUTOICRMENT
		private Long id;
		
		@NotNull(message = "O Atributo Categoria é obrigatorio") //NÃO DEIXA O CAMPO SER NULO
		private String tipo;
		
		@OneToMany(fetch = FetchType.LAZY, mappedBy = "categoria", cascade = CascadeType.REMOVE)
		@JsonIgnoreProperties("categoria")
		private List<Exercicio> exercicio;


		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getTipo() {
			return tipo;
		}

		public void setTipo(String tipo) {
			this.tipo = tipo;
		}

		public List<Exercicio> getExercicio() {
			return exercicio;
		}

		public void setExercicio(List<Exercicio> exercicio) {
			this.exercicio = exercicio;
		}
		

}