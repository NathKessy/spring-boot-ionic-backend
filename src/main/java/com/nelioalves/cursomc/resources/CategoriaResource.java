package com.nelioalves.cursomc.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {
	
	private final CategoriaService service;

	CategoriaResource(CategoriaService service) {
		this.service = service;
	}
	
	@RequestMapping(value = "/{id}",  method=RequestMethod.GET)
	public ResponseEntity<?> find(@PathVariable Integer id) {
		Categoria obj = service.buscar(id);
		return ResponseEntity.ok().body(obj);
		
	}
}


// ResponseEntity --> Esse tipo é um tipo especial do Spring onde ele encapsula varias informações de uma resposta HTTP para um serviço rest