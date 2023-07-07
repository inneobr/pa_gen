package br.coop.integrada.api.pa.domain.enums;

import java.util.ArrayList;
import java.util.List;

import br.coop.integrada.api.pa.domain.modelDto.enumDto.FuncionalidadeEnumDto;

public enum FuncionalidadePaginaEnum {

	INT_IND_CLASSIFICACAO("Integração Individual de Classificação", PaginaEnum.CLASSIFICACAO),
	INT_LOTE_CAMPO_SEMENTE("Integração Lote Campos de Semente", PaginaEnum.CAMPO_SEMENTE),
	INT_LOTE_CLASSIFICACAO("Integração Lote Classificação", PaginaEnum.CLASSIFICACAO),
	INT_LOTE_ESTABELECIMENTO("Integração Lote Estabelecimentos", PaginaEnum.ESTABELECIMENTO),
	INT_LOTE_GRUPO_CLASSIFICACAO("Integração Lote Grupo Produto", PaginaEnum.GRUPO_CLASSIFICACAO),
	INT_LOTE_GRUPO_PRODUTO("Integração Lote Grupo Produto", PaginaEnum.GRUPO_PRODUTO),
	INT_LOTE_ITEM_AVARIADO("Integração Lote Item Avariado", PaginaEnum.ITEM_AVARIADO),
	INT_LOTE_LAUDO_INSP_CAMPO("Integração Lote Laudo Inspeção Campo", PaginaEnum.LAUDO_INSP_CAMPO),
	INT_LOTE_MONITOR_ENTRADA("Integração Lote Monitor de Entrada", PaginaEnum.MONITOR_ENTRADA),
	INT_LOTE_NATUREZA_OPERACAO("Integração Lote Natureza de Operação", PaginaEnum.NATUREZA_OPERACAO),
	INT_LOTE_NATUREZA_TRIBURARIA("Integração Lote Natureza Tributária", PaginaEnum.NATUREZA_TRIBURARIA),
	INT_LOTE_PARAMETRO_ESTABELECIMENTO("Integração Lote Parâmetros de Estabelecimentos", PaginaEnum.PARAMETRO_ESTABELECIMENTO),
	INT_LOTE_PARAMETRO_PRODUCAO("Integração Lote Parâmetros de Produção", PaginaEnum.PARAMETRO_PRODUCAO),
	INT_LOTE_PARTICIPANTE_BAYER("Integração Lote Participante Bayer", PaginaEnum.PARTICIPANTE_BAYER),
	INT_LOTE_PRODUTO("Integração Lote Produto", PaginaEnum.PRODUTO),
	INT_LOTE_REFERENCIA_PRODUTO("Integração Lote Referência Produto", PaginaEnum.REFERENCIA_PRODUTO),
	INT_LOTE_SAFRA_COMPOSTA("Integração Lote Safra Composta", PaginaEnum.SAFRA_COMPOSTA),
	INT_LOTE_TABELA_PRECO_FISCAL("Integração Lote Tabela Preço Fiscal", PaginaEnum.TABELA_PRECO_FISCAL),
	INT_LOTE_TAXA("Integração Lote Taxa", PaginaEnum.TAXA),
	INT_LOTE_TICKET_PESAGEM("Integração Lote Tickets de Pesagem", PaginaEnum.TICKET_PESAGEM),
	INT_LOTE_TIPO_CLASSIFICACAO("Integração Lote Tipo de Classificação", PaginaEnum.TIPO_CLASSIFICACAO),
	INT_LOTE_TIPO_GMO("Integração Lote Tipo GMO", PaginaEnum.TIPO_GMO),
	INT_LOTE_TIPO_PRODUTO("Integração Lote Tipo Produto", PaginaEnum.TIPO_PRODUTO),
	INT_LOTE_USUARIO_ESTABELECIMENTO("Integração Lote Usuário Extabelecimento", PaginaEnum.PARAMETRO_USUARIO_ESTABELECIMENTO),
    INT_LOTE_TAXA_PRODUCAO("Integração Lote Taxa de produção", PaginaEnum.TAXA_PRODUCAO),
    INT_LOTE_UNIDADE_FEDERACAO("Integração Lote Unidade da Federação", PaginaEnum.UNIDADE_FEDERACAO),
	INT_LOTE_MOVIMENTO_DIARIO("Integração Lote Movimento diário", PaginaEnum.MOVIMENTO_DIARIO),
	
    INT_IND_VALIDAR_NF_ENTRADA("Validar NF Entrada", PaginaEnum.ENTRADA_PRODUCAO),
    INT_IND_BUSCAR_NUMERO_RE("Buscar Número RE", PaginaEnum.ENTRADA_PRODUCAO),
    INT_IND_ENVIO_RE("Envio das novas RE's", PaginaEnum.ENTRADA_PRODUCAO),
 
    INT_IND_VALIDA_ENTRADA_PRODUCAO("Validar entrada de produção no ERP", PaginaEnum.ENTRADA_PRODUCAO),	
	INT_LOTE_REMESSA_NF_ROMANEIO_ENTREGA("Remessa NF", PaginaEnum.ROMANEIO_ENTREGA);
	
    private final String descricao;
    private final PaginaEnum pagina;
    
    private FuncionalidadePaginaEnum(String descricao, PaginaEnum pagina) {
        this.descricao = descricao;
        this.pagina = pagina;
    }

    public String getDescricao() {
        return descricao;
    }

    public PaginaEnum getPagina() {
        return pagina;
    }
    
    public static List<FuncionalidadeEnumDto> buscarPorPagina(PaginaEnum pagina) {
    	List<FuncionalidadeEnumDto> funcionalidades = new ArrayList<>();
    	for(FuncionalidadePaginaEnum item: FuncionalidadePaginaEnum.values()) {
    		if(item.getPagina().equals(pagina)) {
    			funcionalidades.add(FuncionalidadeEnumDto.construir(item));
    		}
    	}
    	return funcionalidades;
    }

}
