package com.nelioalves.cursomc.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.domain.Produto;


// @Repository: Indica ao Spring que esta interface é um componente de acesso a dados (DAO/Repository).
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
	
	/*
	 * @Query: Permite escrever uma consulta customizada em JPQL (Java Persistence Query Language).
	 * 
	 * O que a JPQL faz:
	 * 1. SELECT DISTINCT obj: Busca produtos únicos (evitando duplicados no resultado).
	 * 2. FROM Produto obj INNER JOIN obj.categorias cat: Faz o cruzamento (JOIN) da entidade Produto com a sua lista de categorias.
	 * 3. WHERE obj.name LIKE %:nome%: Filtra produtos cujo nome contenha o trecho passado no parâmetro 'nome' (busca parcial).
	 * 4. AND cat IN :categorias: Restringe a busca apenas aos produtos que pertençam a pelo menos uma das categorias informadas.
	 */
	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	Page<Produto> search(
			@Param("nome") String nome,                        // Vincula o parâmetro da função ao ':nome' dentro da query JPQL
			@Param("categorias") List<Categoria> categorias,   // Vincula a lista de categorias ao ':categorias' da query
			Pageable pageRequest                              // Objeto do Spring que gerencia a paginação (página, limite e ordenação)
	);
	
	/* Dessa forma o spring automaticamente vai gerar a consulta para gente, não precisa estar fazendo todo aquele código acima, pois ele
	 * substitui ele
	 * 
	 * Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);
	 */
	
}

