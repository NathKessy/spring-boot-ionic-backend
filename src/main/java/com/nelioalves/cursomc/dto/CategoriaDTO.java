package com.nelioalves.cursomc.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import com.nelioalves.cursomc.domain.Categoria;

import jakarta.validation.constraints.NotEmpty;

public class CategoriaDTO implements Serializable {
	private static final long serialVersionUID = 1L;	
	
	private int id;
	
	@NotEmpty(message = "Preenchimento obrigatório")
	@Length(min = 5, max = 80, message = "O tamanho deve ser entre 5 e 80 caracteres")
	private String nome;
	
	public CategoriaDTO() {
	}

	// Esse construtor vai ser responsável por instanciar o DTO a partir de um obj categoria
	public CategoriaDTO(Categoria obj) {
		id = obj.getId();
		nome = obj.getNome();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	
	

}
// Anotações
// --> DTO - Objeto de Transferência de dados, ele serve para exibir os dados que deseja que seja visto