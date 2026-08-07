package com.nelioalves.cursomc.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.cursomc.domain.Pedido;
import com.nelioalves.cursomc.services.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

	private final PedidoService service;

	PedidoResource(PedidoService service) {
		this.service = service;
	}
	
	
	// @RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@GetMapping("/{id}") // Maneira atualizada de usar o GET
	public ResponseEntity<Pedido> find(@PathVariable Integer id) {
		Pedido obj = service.find(id);
		return ResponseEntity.ok().body(obj);

	}
}