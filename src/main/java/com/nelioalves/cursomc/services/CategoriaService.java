package com.nelioalves.cursomc.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {

	// Quando declarar uma dependencia dentro de uma classe e coloca o @Autowired, ela passa a ser automaticamente instanciada pelo Spring
	private final CategoriaRepository repo;


	CategoriaService(CategoriaRepository repo) {
		this.repo = repo;
	}

	
	public Categoria buscar(Integer id) {
		 Optional<Categoria> obj = repo.findById(id);
		return obj.orElse(null);
		} 
}
