package br.coop.integrada.api.pa.domain.enums;

public enum PaginaAreaEnum {
	
	PARAM_PRODUCAO_INFORMACOES(PaginaEnum.PARAMETRO_PRODUCAO,"Informações"),
	PARAM_PRODUCAO_EPC(PaginaEnum.PARAMETRO_PRODUCAO,"Parâmetro para geração de EPC"),
	PARAM_PRODUCAO_TAXAS(PaginaEnum.PARAMETRO_PRODUCAO,"Taxas"),
	PARAM_PRODUCAO_ESPECIE(PaginaEnum.PARAMETRO_PRODUCAO,"Espécie"),
	PARAM_PRODUCAO_INSS(PaginaEnum.PARAMETRO_PRODUCAO,"INSS"),
	PARAM_PRODUCAO_SEMENTE_TRADATA(PaginaEnum.PARAMETRO_PRODUCAO,"Geração TD Semente Trada"),
	PARAM_PRODUCAO_GILRAT(PaginaEnum.PARAMETRO_PRODUCAO,"GILRAT"),
	PARAM_PRODUCAO_SENAR(PaginaEnum.PARAMETRO_PRODUCAO,"SENAR"),
	NATUREZA_OPERACAO_AREA_TITULO(PaginaEnum.NATUREZA_OPERACAO, "Area do Título"),
	NATUREZA_TRIBUTARIA_AREA_TITULO(PaginaEnum.NATUREZA_TRIBURARIA, "Área do Título"),
	
	MONITOR_ENTRADA_AREA_1(PaginaEnum.MONITOR_ENTRADA, "Area 1"),
	MONITOR_ENTRADA_AREA_2(PaginaEnum.MONITOR_ENTRADA, "Area 2"),
	MONITOR_ENTRADA_AREA_3(PaginaEnum.MONITOR_ENTRADA, "Area 3"),
	ESTABELECIMENTO_AREA_1(PaginaEnum.ESTABELECIMENTO, "Area 1"),
	ESTABELECIMENTO_AREA_2(PaginaEnum.ESTABELECIMENTO, "Area 2"),
	TIPO_GMO_AREA_1(PaginaEnum.TIPO_GMO, "Area 1"),
	TIPO_GMO_AREA_2(PaginaEnum.TIPO_GMO, "Area 2"),
	ENTRADA_PRODUCAO_PARTE_1(PaginaEnum.ENTRADA_PRODUCAO, "Parte 1"),
	ENTRADA_PRODUCAO_PARTE_2(PaginaEnum.ENTRADA_PRODUCAO, "Parte 2"),
	ENTRADA_PRODUCAO_PARTE_3(PaginaEnum.ENTRADA_PRODUCAO, "Parte 3"),
	
	PAGINA_INTEGRACAO_MENU(PaginaEnum.PAGINA_INTEGRACAO, "Área de menu de páginas"),
	PAGINA_INTEGRACAO_LOGIN(PaginaEnum.PAGINA_INTEGRACAO, "Área de listagem de Login"),
	PAGINA_INTEGRACAO_LOGIN_CADASTRO(PaginaEnum.PAGINA_INTEGRACAO, "Área de cadastro de Login"),
	PAGINA_INTEGRACAO_CABECALHO(PaginaEnum.PAGINA_INTEGRACAO, "Cabeçalho de integração"),
	PART_BAYER_XLSX(PaginaEnum.PARTICIPANTE_BAYER, "Instruções da Planilha"),
	GRUPO_CLASSIFICACAO_XLSX(PaginaEnum.GRUPO_CLASSIFICACAO, "Instruções da Planilha")
	;
	
	private final PaginaEnum pagina;
	private final String area;
	
	PaginaAreaEnum(PaginaEnum pagina, String area) {
		this.pagina = pagina;
		this.area = area;
	}
	
	public PaginaEnum getPagina(){
		return pagina;
	}
	
	public String getArea() {
		return area;
	}
	
	
}
