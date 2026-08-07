package com.nelioalves.cursomc.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.repositories.CategoriaRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	private final CategoriaRepository repo;

	CategoriaService(CategoriaRepository repo) {
		this.repo = repo;
	}

	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null); //Objeto novo a ser inserido ele tem que ser nulo, o id não pode existir
		return repo.save(obj);
	}
}

// Quando declarar uma dependencia dentro de uma classe e coloca o @Autowired, ela passa a ser automaticamente instanciada pelo Spring
