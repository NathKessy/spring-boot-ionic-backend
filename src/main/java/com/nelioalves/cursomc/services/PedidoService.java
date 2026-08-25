package com.nelioalves.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.ItemPedido;
import com.nelioalves.cursomc.domain.PagamentoComBoleto;
import com.nelioalves.cursomc.domain.Pedido;
import com.nelioalves.cursomc.domain.enums.EstadoPagamento;
import com.nelioalves.cursomc.repositories.ItemPedidoRepository;
import com.nelioalves.cursomc.repositories.PagamentoRepository;
import com.nelioalves.cursomc.repositories.PedidoRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class PedidoService {

	// Injeções de dependência declaradas como final (boas práticas de imutabilidade)
	private final PedidoRepository repo;
	private final BoletoService boletoService;
	private final PagamentoRepository pagamentoRepository;
	private final ItemPedidoRepository itemPedidoRepository;
	private final ProdutoService produtoService;

	// Construtor para injeção de dependências. O Spring injeta automaticamente as instâncias necessárias ao inicializar o PedidoService.
	PedidoService(PedidoRepository repo, BoletoService boletoService, PagamentoRepository pagamentoRepository,
			ItemPedidoRepository itemPedidoRepository, ProdutoService produtoService) {
		this.repo = repo;
		this.boletoService = boletoService;
		this.pagamentoRepository = pagamentoRepository;
		this.itemPedidoRepository = itemPedidoRepository;
		this.produtoService = produtoService;
	}

	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

	/*
	 * Processa a inserção (criação) de um novo Pedido completo no sistema.
	 * 
	 * @param obj Objeto Pedido contendo as informações enviadas na requisição.
	 * @return O pedido salvo com ID gerado e relacionamentos persistidos.
	 */
	@Transactional
	public Pedido insert(Pedido obj) {
		// Garante que é uma inserção (ID nulo faz o banco gerar um novo ID)
		obj.setId(null);

		// Define o instante/data do pedido para a data e hora atual do sistema
		obj.setInstante(new Date());

		// Define o estado inicial do pagamento como PENDENTE
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);

		// Associa o pagamento ao pedido (relacionamento bidirecional)
		obj.getPagamento().setPedido(obj);

		// Verifica se o pagamento é do tipo "PagamentoComBoleto" (polimorfismo)
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagamento = (PagamentoComBoleto) obj.getPagamento();
			// Delega ao BoletoService o preenchimento da data de vencimento (data atual + 7 dias)
			boletoService.preencherPagamentoComBoleto(pagamento, obj.getInstante());
		}

		// Salva o pedido no banco de dados para gerar o seu ID
		obj = repo.save(obj);

		// Persiste os dados do pagamento no banco de dados
		pagamentoRepository.save(obj.getPagamento());

		// Modifica e vincula cada item associado ao pedido
		for (ItemPedido ip : obj.getItens()) {
			// Por regra de negócio inicial, define o desconto como 0.0
			ip.setDesconto(0.0);

			// Busca no banco o preço atualizado do produto associado ao item para evitar adulterações
			ip.setPreco(produtoService.find(ip.getProduto().getId()).getPreco());

			// Associa o item ao pedido recém-salvo
			ip.setPedido(obj);
		}

		// Salva todos os itens do pedido no banco de dados de uma só vez
		itemPedidoRepository.saveAll(obj.getItens());

		return obj;
	}
}