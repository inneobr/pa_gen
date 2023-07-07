package br.coop.integrada.api.pa.domain.enums;

import java.util.ArrayList;
import java.util.List;
import br.coop.integrada.api.pa.domain.modelDto.enumDto.PaginaEnumDto;

public enum PaginaEnum {
	
	CAMPO_SEMENTE("Campo de Semente", false),
	CLASSIFICACAO("Classificação", false),
	ENTRADA_PRODUCAO("Entrada de Produção", true),
	ESTABELECIMENTO("Estabelecimento", true),
	GRUPO_CLASSIFICACAO("Grupo Classificação", true),
	GRUPO_PRODUTO("Grupo de Produto", false),
	ITEM_AVARIADO("Item Avariado", false),
	LAUDO_INSP_CAMPO("Laudo Inspeção Campo", false),
	MONITOR_ENTRADA("Monitor de Entrada", true),
	NATUREZA_OPERACAO("Natureza de Operação", true),
	NATUREZA_TRIBURARIA("Natureza Tributária", true),
	PAGINA_INTEGRACAO("Página de integração", true),
	PARAMETRO_ESTABELECIMENTO("Parâmetros de Estabelecimentos", true),
	PARAMETRO_PRODUCAO("Parâmetros de Produção", true),
	PARAMETRO_USUARIO_ESTABELECIMENTO("Parâmetro de usuário x estabelecimento", false),
	PARTICIPANTE_BAYER("Participante Bayer", true),
	PRODUTO("Produtos", false),
	PRODUTOR("Produtor", false),
	REFERENCIA_PRODUTO("Referência de Produto", false),
	SAFRA_COMPOSTA("Safra Composta", false),
	TABELA_PRECO_FISCAL("Tabela de Preço Fiscal", false),
	TAXA("Taxa", false),
	TAXA_PRODUCAO("Taxas de Produção", false),
	TICKET_PESAGEM("Ticket de Pesagem", false),
	TIPO_CLASSIFICACAO("Tipo de Classificação", false),
	TIPO_GMO("Tipo GMO", true),
	TIPO_PRODUTO("Tipo de Produto",false),
	UNIDADE_FEDERACAO("Unidade da Federação", false),
	MOVIMENTO_DIARIO("Movimento Diário", false),
	ROMANEIO_ENTREGA("Romaneio de Entrega", false),
	NF_REMESSA("NF Remessa", false);
	
	private final String descricao;

	//TRUE para quando for uma página que conterá Informações de área (Modal)
	private final Boolean possuiInformativoSistema; 
	
	
	
	PaginaEnum(String descricao, Boolean possuiInformativoSistema){
		this.descricao = descricao;
		this.possuiInformativoSistema = possuiInformativoSistema;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public Boolean getPossuiInformativoSistema() {
		return possuiInformativoSistema;
	} 
	
	public static List<PaginaEnumDto> listarTodas() {
		List<PaginaEnumDto> paginas = new ArrayList<>();
    	for(PaginaEnum item: PaginaEnum.values()) {
    		paginas.add(PaginaEnumDto.construir(item));
    	}
    	return paginas;
    }
	
}
