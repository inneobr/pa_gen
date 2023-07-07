package br.coop.integrada.api.pa.domain.model.produto;

import br.coop.integrada.api.pa.domain.enums.grupo.produto.EntradaReEnum;
import br.coop.integrada.api.pa.domain.enums.grupo.produto.ReferenciaEnum;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(
		name = "grupo_produto",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "KEY_FM_CODIGO",
						columnNames = { "fm_codigo" }
				)
		}
)
public class GrupoProduto extends AbstractEntity{
	private static final long serialVersionUID = 1L;

	@NotNull(message = "Campo {fmCodigo} não pode ser nulo!")
	@NotBlank(message = "Campo {fmCodigo} não pode ser vazio!")
	@Column(name="fm_codigo")
	private String fmCodigo;

	@NotNull(message = "Campo {descricao} não pode ser nulo!")
	@NotBlank(message = "Campo {descricao} não pode ser vazio!")
	@Column(name="descricao")
	private String descricao;
	
	@Enumerated(EnumType.STRING)
	@Column(name="referencia")
	private ReferenciaEnum referencia;
	
	@Enumerated(EnumType.STRING)
	@Column(name="refer_fixado")
	private ReferenciaEnum referFixado;
	
	@Column(name="log_transgenico")
	private boolean logTransgenico;
	
	@Column(name="log_retirada")
	private boolean logRetirada;
	
	@Column(name="transferencia")
	private boolean transferencia;
	
	@Column(name="desmembramento")
	private boolean desmembramento;
	
	@Column(name="kg_sc")
	private Integer kgSc;
	
	@Column(name="ph_minimo")
	private Integer phMinimo;
	
	@Column(name="perc_impureza")
	private BigDecimal percImpureza;
	
	@Column(name="semente")
	private boolean semente;
	
	@Column(name="impureza")
	private boolean impureza;
	
	@Column(name="ph_entrada")
	private boolean phEntrada;
	
	@Column(name="umidade")
	private boolean umidade;
	
	@Column(name="chuv_avar")
	private boolean chuvAvar;
	
	@Column(name="lote")
	private boolean lote;
	
	@Column(name="tbm")
	private boolean tbm;
	
	@Column(name="nr_ord_campo")
	private boolean nrOrdCampo;
	
	@Column(name="fnt", columnDefinition = "NUMBER(1,0) default 0")
	private boolean fnt;
	
	@Column(name="densidade", columnDefinition = "NUMBER(1,0) default 1")
	private boolean densidade;
	
	@Column(name="nr_laudo")
	private boolean nrLaudo;
	
	@Column(name="tipo")
	private boolean tipo;
	
	@Column(name="log_permite_desmembra_terc_coop")
	private boolean permiteDesmembraTercCoop;
	
	@Column(name="log_cafe_coco")
	private boolean cafeCoco;
	
	@Column(name="log_cafe_benef")
	private boolean cafeBeneficiado;
	
	@ManyToOne
	@JoinColumn(name="tipo_produto")
	private TipoProduto tipoProduto;
	
	@Column(name="cod_cult")
	private String codCult;
	
	@Column(name="quali_produto")
	private boolean qualiProduto;
	
	@Column(name="log_bandinha")
	private boolean logBandinha;
	
	@Column(name="bebida")
	private boolean bebida;
	
	@Column(name="cafe_escolha")
	private boolean cafeEscolha;
	
	@Column(name="cata")
	private boolean cata;
	
	@Column(name="fm_codigo_sub")
	private String fmCodigoSub;
	
	@Column(name="it_sub_coop")
	private String itSubCoop;
	
	//@Column(name="it_sub_ter")
	//private String itSubTer;
	
	@Column(name="it_taxa_coop")
	private String itTaxaCoop;
	
	//@Column(name="it_taxa_ter")
	//private String itTaxaTer;
	
	@Column(name="entrada_re")
	@Enumerated(EnumType.STRING)
	private EntradaReEnum entradaRe;
	
	@Column(name="cond_chuv_avar_sinal")
	private String condChuvAvarSinal;
	
	@Column(name="cond_chuv_avar_valor")
	private BigDecimal condChuvAvarValor;
	
	public String getCodDesc(){
		return fmCodigo + " - " + descricao;
	}
	
	public BigDecimal getPercImpureza() {
		if(percImpureza == null) return BigDecimal.ZERO;
		return percImpureza;
	}

	public BigDecimal getCondChuvAvarValor() {
		if(condChuvAvarValor == null) return BigDecimal.ZERO;
		return condChuvAvarValor;
	}

	public Integer getKgSc() {
		if(kgSc == null) return 0;
		return kgSc;
	}
	
	public Integer getPhMinimo() {
		if(phMinimo == null) return phMinimo;
		return phMinimo;
	}
}
