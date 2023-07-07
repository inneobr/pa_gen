package br.coop.integrada.api.pa.domain.modelDto.rependente;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class VerificarTaxasReResponseDto {
	
	/*
	Parâmetros de OUTPUT:
	 RetemDPI (yes/no) 
	 Percentual DPI (dpi)
	 ObservacaoRE (dpi)
	 Sub Produto (yes/no) 
	 Grupo Produtor (sub produto)
	 Produto (sub produto)
	 Secagem (yes/no) 
	 Cobranca – RE/Renda (secagem) 
	 Produto (secagem) 
	 Recepção (yes/no) 
	 Valos Recepcao (recepçao)
	 */
	private Boolean retemDPI;
	private BigDecimal percentualDpi;
	private String observacaoRE;
	
	private Boolean subProduto;
	private String grupoProdutoSub;
	private String produtoSub;
	
	private Boolean secagem;
	private String cobranca;
	private String produto;
	
	private Boolean recepcao;
	private BigDecimal valorRecepcao;
		
	private String mensagem;
	
	public VerificarTaxasReResponseDto(String mensagem) {
		this.mensagem = mensagem;
	}
}
