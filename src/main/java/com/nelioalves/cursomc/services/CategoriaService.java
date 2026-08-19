package com.nelioalves.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.dto.CategoriaDTO;
import com.nelioalves.cursomc.repositories.CategoriaRepository;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	private final CategoriaRepository repo;

	CategoriaService(CategoriaRepository repo) {
		this.repo = repo;
	}

	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}

	public Categoria insert(Categoria obj) {
		obj.setId(null); // Objeto novo a ser inserido ele tem que ser nulo, o id não pode existir
		return repo.save(obj);
	}

	public Categoria update(Categoria obj) {
		Categoria newObj = find(obj.getId()); //Agora vou estar instanciando uma categoria a partir do banco de dados
		updateData(newObj, obj); // Aqui é um metodo auxiliar que ele vai atualizar os dados a partir dos criados com base com o obj que veio como argumento
		return repo.save(newObj);
	}

	public void delete(Integer id) {
		find(id); // Caso o id não exista, ele dispara uma mensagem de não encontrado.
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
		}
	}

	public List<Categoria> findAll() {
		return repo.findAll();
	}

	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	// Metodo auxiliar que instancia uma categoria a partir de um DTO
	public Categoria fromDTO(CategoriaDTO objDto) {
		return new Categoria(objDto.getId(), objDto.getNome());
	}
	
	//Ele esta sendo criado como private por ser um metodo auxiliar dentro da classe e não ter motivo para ficar exposto para fora
	private void updateData(Categoria newObj, Categoria obj) { // Agora ele atualiza os objtos do newObj com os novos dados que vieram no obj
		newObj.setNome(obj.getNome());
	}
}


/* Anotações
- Quando declarar uma dependencia dentro de uma classe e coloca o @Autowired, ela passa a ser automaticamente instanciada pelo Spring
- Serice -> É onde fica todas as regras de negocio dessa classe. */
