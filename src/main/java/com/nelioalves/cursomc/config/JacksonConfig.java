package com.nelioalves.cursomc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelioalves.cursomc.domain.PagamentoComBoleto;
import com.nelioalves.cursomc.domain.PagamentoComCartao;

// Indica ao Spring que esta é uma classe de configuração. Ela será processada na inicialização da aplicação para registrar Beans.
@Configuration
public class JacksonConfig {

	/**
	 * Define um Bean gerenciado pelo Spring para customizar o mapeador JSON (Jackson).
	 * Essa configuração resolve o problema de desserialização (conversão de JSON para Objeto) quando existe herança/polimorfismo envolvido.
	 */
	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
		// https://stackoverflow.com/questions/41452598/overcome-can-not-construct-instance-ofinterfaceclass-without-hinting-the-pare
		
		// Cria um builder anônimo do Jackson para sobrescrever a configuração padrão
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder() {
			
			@Override
			public void configure(ObjectMapper objectMapper) {
				
				// Registra explicitamente a subclasse PagamentoComCartao no mapeador JSON. Permite que o Jackson identifique a classe concreta durante o parse do JSON.
				objectMapper.registerSubtypes(PagamentoComCartao.class);
				
				// Registra explicitamente a subclasse PagamentoComBoleto no mapeador JSON.
				objectMapper.registerSubtypes(PagamentoComBoleto.class);
				
				// Aplica a configuração padrão do Spring após adicionar os subtipos
				super.configure(objectMapper);
			}
		};
		
		return builder;
	}
}