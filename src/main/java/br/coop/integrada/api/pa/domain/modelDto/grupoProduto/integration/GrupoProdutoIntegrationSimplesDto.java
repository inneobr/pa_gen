package br.coop.integrada.api.pa.domain.modelDto.grupoProduto.integration;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import br.coop.integrada.api.pa.domain.enums.grupo.produto.EntradaReEnum;
import br.coop.integrada.api.pa.domain.enums.grupo.produto.ReferenciaEnum;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;

@Data
public class GrupoProdutoIntegrationSimplesDto implements Serializable {
    private static final long serialVersionUID = 1L;
	private String fmCodigo;
    private String descricao;
    private ReferenciaEnum referencia;
    private ReferenciaEnum referFixado;
    private Boolean logTransgenico;
    private Boolean logRetirada;
    private Boolean transferencia;
    private Boolean desmembramento;
    private Integer kgSc;
    private Integer phMinimo;
    private BigDecimal percImpureza;
    private Boolean semente;
    private Boolean impureza;
    private Boolean phEntrada;
    private Boolean umidade;
    private Boolean chuvAvar;
    private Boolean lote;
    private Boolean tbm;
    private Boolean nrOrdCampo;
    private Boolean nrLaudo;
    private Boolean tipo;
    private String tipoProdutoNome;
    private String codCult;
    private Boolean qualiProduto;
    private Boolean logBandinha;
    private Boolean bebida;
    private Boolean cafeEscolha;
    private Boolean cafeCoco;
    private Boolean cafeBenef;
    private Boolean cata;
    private String fmCodigoSub;
    //private String itSub;
    private String itSubCoop;
    //private String itSubTer;
    //private String itTaxa;
    private String itTaxaCoop;
    //private String itTaxaTer;
    private Boolean ativo;
    private EntradaReEnum entradaRe;
    private String condChuvAvarSinal;
    private BigDecimal condChuvAvarValor;
	private IntegrationOperacaoEnum operacao;
    private List<GrupoProdutoGmoIntegrationSimplesDto> gmos;
    
}
