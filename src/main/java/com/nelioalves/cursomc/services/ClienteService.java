package com.nelioalves.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	private final ClienteRepository repo;

	ClienteService(ClienteRepository repo) {
		this.repo = repo;
	}

	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId()); //Agora vou estar instanciando um cliente a partir do banco de dados
		updateData(newObj, obj); // Aqui é um metodo auxiliar que ele vai atualizar os dados a partir dos criados com base com o obj que veio como argumento
		return repo.save(newObj);
	}

	public void delete(Integer id) {
		find(id); // Caso o id não exista, ele dispara uma mensagem de não encontrado.
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há entidades relacionadas");
		}
	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
	}	
	
	//Ele esta sendo criado como private por ser um metodo auxiliar dentro da classe e não ter motivo para ficar exposto para fora
	private void updateData(Cliente newObj, Cliente obj) { // Agora ele atualiza os objtos do newObj com os novos dados que vieram no obj
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
