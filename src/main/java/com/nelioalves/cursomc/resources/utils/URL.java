package com.nelioalves.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

// Classe utilitária para tratamento e manipulação de parâmetros recebidos via URL
public class URL {
	
	/*
	 * Decodifica uma String enviada pela URL (ex: transforma "TV%2029" ou "TV+29" de volta para "TV 29"). As URLs convertem espaços 
	 * e caracteres especiais em percent-encoding (ex: %20), e este método reverte isso para o texto original.
	 */
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} 
		// Captura a exceção caso o charset informado não seja suportado pelo Java
		catch (UnsupportedEncodingException e) {
			return ""; // Retorna uma String vazia como prevenção em caso de erro
		}
	}
	
	/* 
	 * Método utilitário estático (pode ser chamado sem instanciar a classe URL). 
	 * Recebe uma String de IDs separados por vírgula (ex: "1,2,3") e a converte em uma lista de números inteiros List<Integer>. 
	 */
	public static List<Integer> decodeIntList(String s) {
		// Divide a String de entrada em um array de partes usando a vírgula como separador
		String[] vet = s.split(",");
		
		// Instancia a lista que armazenará os valores convertidos
		List<Integer> list = new ArrayList<>();
		
		// Percorre cada elemento (texto) do array obtido
		for (int i = 0; i < vet.length; i++) {
			// Converte a fração de texto em Integer e adiciona na lista de retorno
			list.add(Integer.parseInt(vet[i]));
		}
		
		// Retorna a lista pronta com os números inteiros
		return list;
		
		/* 
		 * Alternativa usando Java 8 Streams (Lambda):
		 * Converte o array para Stream, transforma cada String em Integer com o map, e coleta no final em uma List.
		 * Nota: Se for usar a linha abaixo, ela deve ser inserida ANTES do 'return list;' acima.
		 * 
		 * return Arrays.stream(s.split(",")).map(Integer::parseInt).collect(Collectors.toList());
		 */
	}
}