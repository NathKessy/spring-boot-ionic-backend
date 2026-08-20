package com.nelioalves.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import com.nelioalves.cursomc.domain.enums.TipoCliente;
import com.nelioalves.cursomc.dto.ClienteNewDTO;
import com.nelioalves.cursomc.resources.exception.FieldMessage;
import com.nelioalves.cursomc.services.validation.utils.BR;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// Esta classe implementa a lógica de uma anotação customizada (@ClienteInsert) 
// para validar os dados do DTO de criação de cliente (ClienteNewDTO)
public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {

	// Método chamado na inicialização do validador.
	// Como não há configurações iniciais necessárias, permanece vazio.
	@Override
	public void initialize(ClienteInsert ann) {
	}

	// Método responsável por executar a regra de negócio da validação.
	// Retorna 'true' se o DTO for válido e 'false' caso contrário.
	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {

		// Lista auxiliar para guardar os erros de validação encontrados
		List<FieldMessage> list = new ArrayList<>();

		// REGRA 1: Se o tipo for Pessoa Física, verifica se o CPF é inválido usando a classe utilitária BR
		if (objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}

		// REGRA 2: Se o tipo for Pessoa Jurídica, verifica se o CNPJ é inválido usando a classe utilitária BR
		if (objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
		}

		/* Transforma os erros da nossa lista personalizada (FieldMessage) no formato padrão de erro esperado 
		 pelo framework de validação (Bean Validation) */
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation(); // Desabilita a mensagem de erro padrão
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation(); // Adiciona o campo e a mensagem customizada
		}

		// Se a lista de erros estiver vazia, significa que o DTO é válido
		return list.isEmpty();
	}
}