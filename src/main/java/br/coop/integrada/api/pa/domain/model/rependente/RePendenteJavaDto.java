package br.coop.integrada.api.pa.domain.model.rependente;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.modelDto.imovel.ImovelSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.produtor.ProdutorSimplesDto;

@Data
public class RePendenteJavaDto {
	private Long id;
	private Integer idTicket;
	private RePendenteEstabelecimentoDto estabelecimento;
	private String placa;
	private RePendenteGrupoProdutoDto grupoProduto;
	private ProdutorSimplesDto produtor;
	private ImovelSimplesDto imovel;	
	
	private String hrEntrada;
	private Long matricula;
	private Long nrNfProd;
	private Integer nrDocPes;
	private BigDecimal pesoBruto;
	private BigDecimal taraVeiculo;
	private BigDecimal taraSacaria;
	private BigDecimal pesoLiquido;
	private String motorista;
	private String tulha;
	
	private Integer nrOrdCampo;
	private Integer nrLaudo;
	private String observacoes;
	private Integer tpRe;
	private Integer nrReOrig;
	private Integer nrRePai;
	private Integer classe;
	private Integer codSit;
	private BigDecimal nrAutTransf;
	private String tipoRr;
	
	private BigDecimal descCredito;
	private BigDecimal qtDepi;
	private BigDecimal perDepi;
	private Boolean kitCobrado;
	private String entradaRr;
	private Boolean comCobranca;
	private Integer nrReOrigDfx;
	private Boolean reDisponivel;
	private String tipoRendaCfe;	
	
	private Boolean entradaManual;
	private String nrContSem;
	private String safraCompos;
	private String nomeSafraCompos;
	private RePendenteTipoGmo tipoGmo;
	private Integer nrPriEnt;
	private Integer cdnRepres;
	private Boolean logReDpi;
	private Boolean logBloqDpi;
	private Integer nrReDpi;
	private BigDecimal pesoLiqSemDescDpi;
	private String serNfProd;	
	
	private Boolean prodPadr;
	private Boolean descarUnid;
	private Boolean logTxSpRetida;
	private Integer nrReTxSpRetida;
	private String pjNroNota;
	private String pjSerie;	
	
	private BigDecimal pjVlTotNota;
	private BigDecimal pjQtTotNota;
	private String pjChaveAcesso;
	private Boolean pjLogNotaPropria;
	private String pjNatOper;
	private StatusIntegracao statusIntegracao;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	private Date dtEntrada;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	private Date dtEmissaoOri;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	private Date pjDtEmissao;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	private Date dtNfProd;	
	
	public BigDecimal getPesoSaida() {
		if(pesoBruto == null || pesoLiquido == null) return BigDecimal.ZERO;
        return pesoBruto.subtract(pesoLiquido);
    }
}
