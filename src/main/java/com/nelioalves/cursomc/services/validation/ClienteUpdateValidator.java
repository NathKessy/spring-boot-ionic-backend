package com.nelioalves.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.HandlerMapping;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.resources.exception.FieldMessage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// Esta classe implementa a lógica da anotação customizada (@ClienteUpdate) para validar os dados na atualização de um cliente (ClienteDTO)
public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

	// Injeção de dependências via construtor (HttpServletRequest para capturar dados da requisição HTTP)
	private final HttpServletRequest request;
	private final ClienteRepository repo;

	ClienteUpdateValidator(ClienteRepository repo, HttpServletRequest request) {
		this.repo = repo;
		this.request = request;
	}

	@Override
	public void initialize(ClienteUpdate ann) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {

		// Obtém os parâmetros/variáveis de URL passados na requisição (ex:/clientes/{id})
		Map<String, String> map = (Map<String, String>) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

		// Extrai o ID do cliente que veio na URL e o converte para Integer
		Integer uriId = Integer.parseInt(map.get("id"));

		// Lista auxiliar para guardar os erros de validação
		List<FieldMessage> list = new ArrayList<>();

		// REGRA DE NEGÓCIO: Busca no banco se já existe um cliente cadastrado com este e-mail
		Cliente aux = repo.findByEmail(objDto.getEmail());

		// Se o e-mail já existe no banco, e pertence a OUTRO cliente (ID diferente do que está sendo atualizado), gera erro de e-mail duplicado
		if (aux != null && !aux.getId().equals(uriId)) {
			list.add(new FieldMessage("email", "Email já existente"));
		}

		// Converte os erros da nossa lista personalizada (FieldMessage) para o formato padrão do Bean Validation
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation(); // Desabilita a mensagem de erro padrão
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation(); // Adiciona o campo e a mensagem customizada
		}

		// Se a lista de erros estiver vazia, significa que o DTO é válido
		return list.isEmpty();
	}
}