package br.coop.integrada.api.pa.domain.modelDto.rependente;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class VerificarTaxasReDto {
	/* PARÂMETROS INPUT
	    
	    Cod.Grupo de Produto (dpi/sub produto/secagem/recepção) 
	    GMO (dpi)
		Declarada ou Testada (dpi)
		Cód.Produtor (dpi)
		TBM (sub produto)
		Cod.Estabelecimento (secagem/recepção) 
		Safra (Secagem/recepção) 
		Produto Padronizado - sim/não (Secagem) 
		Descarga Unidade – sim/não (recepção)
	 */
	
	private String fmCodigo; //Código Grupo de Produto
	
	//DPI
	private Long gmo;
	private String declaradaTestada;
	private String codProdutor;
	private BigDecimal tbm;
	
	private Long codEstabelecimento;
	private int safra;
	private Boolean produtoPadronizado;
	private Boolean descargaUnidade;
	
	
	
	
	
}
