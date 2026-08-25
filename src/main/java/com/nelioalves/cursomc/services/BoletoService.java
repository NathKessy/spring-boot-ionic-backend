package com.nelioalves.cursomc.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.PagamentoComBoleto;

// Indica que esta classe é um componente de Serviço (Service) gerenciado pelo Spring. Contém regras de negócio relacionadas especificamente a boletos bancários.
@Service
public class BoletoService {
	
	/**
	 * Preenche a data de vencimento do boleto com base na data em que o pedido foi realizado.
	 * 
	 * @param pagamento Objeto da entidade PagamentoComBoleto que receberá a data de vencimento.
	 * @param instanteDoPedido Data e hora exatas em que o pedido foi gerado.
	 */
	public void preencherPagamentoComBoleto(PagamentoComBoleto pagamento, Date instanteDoPedido) {
		
		// Obtém uma instância do Calendar configurada com a data/hora atual
		Calendar calendario = Calendar.getInstance();
		
		// Define a data do Calendar para ser idêntica à data do pedido recebida por parâmetro
		calendario.setTime(instanteDoPedido);
		
		// Adiciona 7 dias à data definida (define a regra de vencimento para 7 dias após o pedido)
		calendario.add(Calendar.DAY_OF_MONTH, 7);
		
		// Atribui a nova data calculada (data do pedido + 7 dias) ao atributo dataVencimento do boleto
		pagamento.setDataVencimento(calendario.getTime());
	}

}