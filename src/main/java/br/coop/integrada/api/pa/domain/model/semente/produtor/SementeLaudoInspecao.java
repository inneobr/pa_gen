package br.coop.integrada.api.pa.domain.model.semente.produtor;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(
        name = "semente_laudo_inspecao",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "key_semente_laudo_inspecao",
                        columnNames = { "safra", "id_estabelecimento", "nr_laudo", "nr_ord_campo", "id_grupo_produto" }
                )
        }
)
public class SementeLaudoInspecao extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "Campo {idUnico} obrigatório")
	@Column(name = "id_unico", unique = true)
	private String idUnico;

	@Column(name = "safra", nullable = false)
    private Integer safra;

    @ManyToOne
    @JoinColumn(name = "id_estabelecimento", nullable = false)
    private Estabelecimento estabelecimento;

    @Column(name = "nr_laudo", nullable = false)
    private Long numeroLaudo;

    @Column(name = "nr_ord_campo", nullable = false)
    private Integer ordemCampo;

    @ManyToOne
    @JoinColumn(name = "id_grupo_produto", nullable = false)
    private GrupoProduto grupoProduto;

    @Column(name = "cod_agronomo", length = 12)
    private String codigoAgronomo;

    @Column(name = "cooperante", length = 12)
    private String cooperante;

    @Column(name = "dt_inspecao")
    private Date inspecao;

    @Column(name = "matricula")
    private String matricula;
    
    @ManyToOne
    @JoinColumn(name = "id_produto")
	private Produto produto;

    @Column(name = "dt_ini_plantio")
    private Date dataInicioPlantio;

    @Column(name = "dt_term_plantio")
    private Date dataTerminoPlantio;

    @Column(name = "plantas_m2")
    private BigDecimal plantasM2;

    @Column(name = "mistura_ciclo_m")
    private BigDecimal mistruraCicloM;

    @Column(name = "mistura_ciclo_d")
    private BigDecimal mistruraCicloD;

    @Column(name = "outras_esp", length = 30)
    private String outrasEspecies;

    @Column(name = "area")
    private BigDecimal area;

    @Column(name = "area_campo")
    private BigDecimal areaCampo;

    @Column(name = "densid_cultura", length = 20)
    private String densidadeCultura;

    @Column(name = "dt_aprov_plant")
    private Date dataAprovacaoPlantio;

    @Column(name = "dt_aprov_flor")
    private Date dataAprovacaoFlorescimento;

    @Column(name = "fase")
    private Integer fase;

    @Column(name = "proibida", length = 40)
    private String proibida;

    @Column(name = "tolerada", length = 40)
    private String tolerada;

    @Column(name = "isolamento", length = 40)
    private String isolamento;

    @Column(name = "area_aprovada")
    private BigDecimal areaAprovada;

    @Column(name = "parecer", length = 150)
    private String parecer;

    @Column(name = "dt_prev_colheita")
    private Date dataPreviaColheita;

    @Column(name = "qt_prod_esperada")
    private BigDecimal producaoEsperada;

    @Column(name = "qt_total_esperada")
    private BigDecimal producaoTotalEsperada;

    @ManyToOne
    @JoinColumn(name = "id_classe")
    private SementeClasse classe;

    public String getCodigoEstabelecimento() {
        if(estabelecimento != null) {
            return estabelecimento.getCodigo();
        }

        return "";
    }

    public String getCodigoFamilia() {
        if(grupoProduto != null) {
            return grupoProduto.getFmCodigo();
        }

        return "";
    }

    public Long getClasseCodigo() {
        if(classe != null) {
            return classe.getCodigo();
        }

        return null;
    }

    public String getClasseDescricao() {
        if(classe != null) {
            return classe.getDescricao();
        }

        return "";
    }
}
