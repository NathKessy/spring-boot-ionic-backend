package com.nelioalves.cursomc.resources;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nelioalves.cursomc.domain.Produto;
import com.nelioalves.cursomc.dto.ProdutoDTO;
import com.nelioalves.cursomc.resources.utils.URL;
import com.nelioalves.cursomc.services.ProdutoService;

// @RestController: Define que esta classe é um controlador REST responsável por receber requisições HTTP e retornar respostas no formato JSON.
@RestController
// @RequestMapping: Define o caminho base de URL para todos os endpoints deste controlador (ex: http://localhost:8080/produtos)
@RequestMapping(value = "/produtos")
public class ProdutoResource {

	// Declaração da dependência para a camada de regras de negócio (Service)
	private final ProdutoService service;

	// Injeção de dependência via construtor (prática recomendada e segura no Spring)
	ProdutoResource(ProdutoService service) {
		this.service = service;
	}
	
	/*
	 * @PathVariable: Captura o valor informado na variável de caminho {id} da URL e repassa para o parâmetro id.
	 * ResponseEntity: Encapsula toda a resposta HTTP (código de status 200 OK, cabeçalhos e o corpo da resposta em JSON).
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Produto> find(@PathVariable Integer id) {
		Produto obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	/*
	 * Endpoint GET com busca paginada e filtros (ex: GET /produtos?nome=tv&categorias=1,2,3&page=0&linesPerPage=24)
	 * @RequestParam: Captura os parâmetros enviados via Query String na URL. 
	 * Se não forem fornecidos pelo cliente, assumem os valores definidos em 'defaultValue'.
	 */
	@GetMapping()
	public ResponseEntity<Page<ProdutoDTO>> findPage(
			@RequestParam(value = "nome", defaultValue = "") String nome, 
			@RequestParam(value = "categorias", defaultValue = "") String categorias, 
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy, 
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		
		// 1. Decodifica o texto do nome (trata espaços, acentos e caracteres especiais codificados na URL)
		String nomeDecoded = URL.decodeParam(nome);
		
		// 2. Converte a String de categorias separadas por vírgula (ex: "1,2,3") em uma List<Integer>
		List<Integer> IDs = URL.decodeIntList(categorias);
		
		// 3. Executa a busca no Service passando os filtros tratando a paginação
		Page<Produto> list = service.search(nomeDecoded, IDs, page, linesPerPage, orderBy, direction); 
		
		// 4. Converte a página de entidades 'Produto' para 'ProdutoDTO' usando a função map da Page
		Page<ProdutoDTO> listDto = list.map(obj -> new ProdutoDTO(obj));
		
		// 5. Retorna o status 200 OK com o objeto de página formatado em DTO no corpo da resposta
		return ResponseEntity.ok().body(listDto);
	}
}