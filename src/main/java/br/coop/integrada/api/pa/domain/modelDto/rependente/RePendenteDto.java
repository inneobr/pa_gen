package br.coop.integrada.api.pa.domain.modelDto.rependente;

import lombok.Data;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import br.coop.integrada.api.pa.domain.modelDto.rependente.desmembramento.RePendenteDesmembramentoDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.item.RePendenteItemDto;

import java.math.BigDecimal;

@Data
public class RePendenteDto {
	
	@NotBlank(message = "Campo {codEstabel} obrigatório")
	private String codigoEstabelecimento;	
	
	@NotBlank(message = "Campo {placa} obrigatório")
	private String placa;
	
	@NotBlank(message = "Campo {fmCodigo} obrigatório")
	private String codigoGrupoProduto;
	
	@NotBlank(message = "Campo {codEmitente} obrigatório")
	private String codigoProdutor;
	
	private Long matriculaImovel;
	
	private String nomeProd;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	private Date dtEntrada;
	private String hrEntrada;
	private Integer nrDocPes;
	
	@Digits(integer = 9, fraction = 3)
	private BigDecimal pesoBruto;
	
	@Digits(integer = 9, fraction = 3)
	private BigDecimal taraVeiculo;
	
	@Digits(integer = 9, fraction = 3)
	private BigDecimal taraSacaria;
	
	@Digits(integer = 9, fraction = 3)
	private BigDecimal pesoLiquido;
	
	private String motorista;
	private String tulha;
	private Integer nrOrdCampo;
	private Long nrLaudo;
	private String observacoes;
	private Integer tpRe;
	private Integer nrReOrig;
	private Integer nrRePai;	
	private Long classe;
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
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	private Date dtEmissaoOri;
	private Boolean entradaManual;
	private String nrContSem;
	private String safraCompos;
	private String nomeSafraCompos;
	private String tipoGmo;
	private Integer nrPriEnt;
	private Integer cdnRepres;
	private Boolean logReDpi;
	private Boolean logBloqDpi;
	private Integer nrReDpi;
	private BigDecimal pesoLiqSemDescDpi;
	private Long nrNfProd;
	private String serNfProd;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	private Date dtNfProd;
	private Boolean prodPadr;
	private Boolean descarUnid;
	private Boolean logTxSpRetida;
	private Integer nrReTxSpRetida;
	private String pjNroNota;
	private String pjSerie;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	private Date pjDtEmissao;
	private BigDecimal pjVlTotNota;
	private BigDecimal pjQtTotNota;
	private String pjChaveAcesso;
	private Boolean pjLogNotaPropria;
	private String pjNatOper;
	private Boolean cadastro;
	
	private List<RePendenteItemDto> itens;
	private List<RePendenteDesmembramentoDto> desmembramentos;
}
