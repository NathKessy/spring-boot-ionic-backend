package com.nelioalves.cursomc.dto;

import java.io.Serializable;

import com.nelioalves.cursomc.domain.Categoria;

public class CategoriaDTO implements Serializable {
	private static final long serialVersionUID = 1L;	
	
	private int id;
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

// --> DTO - Objeto de Transferência de dados, ele serve para exibir os dados que deseja que seja visto