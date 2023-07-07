package br.coop.integrada.api.pa.domain.model.parametros.taxa;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.coop.integrada.api.pa.domain.enums.taxa.TipoCobrancaEnum;
import br.coop.integrada.api.pa.domain.enums.taxa.TipoCobrancaSecagemEnum;
import br.coop.integrada.api.pa.domain.enums.taxa.TipoDescontoEnum;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.Data;

@Data
@Entity
@Table(name = "taxa")
public class Taxa extends AbstractEntity {

    private static final long serialVersionUID = 1L;

	@Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "safra", nullable = false)
    private Integer safra;

    @Column(name = "manutencao_carencia")
    private Integer manutencaoCarencia;

    @Digits(integer = 8, fraction = 6)
    @Column(name = "manutencao_valor_por_saca")
    private BigDecimal manutencaoValorPorSaca;

    @Enumerated(EnumType.STRING)
    @Column(name = "quebra_tecnica_Cobranca_por")
    private TipoDescontoEnum quebraTecnicaCobrancaPor;

    @Column(name = "quebra_tecnica_carencia")
    private Integer quebraTecnicaCarencia;

    @Column(name = "quebra_tecnica_dias_maximo")
    private Integer quebraTecnicaDiasMaximo;

    @Digits(integer = 3, fraction = 2)
    @Column(name = "quebra_tecnica_percentual_dia")
    private BigDecimal quebraTecnicaPercentualAoDia;

    @Column(name = "taxa_seguro_carencia")
    private Integer taxaSeguroCarencia;

    @Digits(integer = 3, fraction = 2)
    @Column(name = "taxa_seguro_percentual_por_saca")
    private BigDecimal taxaSeguroPercentualPorSaca;

    @Digits(integer = 9, fraction = 2)
    @Column(name = "taxa_valor_operacional")
    private BigDecimal taxaValorOperacional;

    @Digits(integer = 3, fraction = 2)
    @Column(name = "taxa_receita_regional")
    private BigDecimal taxaReceitaRegional;

    @Digits(integer = 3, fraction = 2)
    @Column(name = "taxa_retencao_especial")
    private BigDecimal taxaRetencaoEspecial;

    @Digits(integer = 3, fraction = 2)
    @Column(name = "taxa_outras")
    private BigDecimal taxaOutras;

    @Column(name = "cobra_recepcao")
    private Boolean cobraRecepcao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cobra_recepcao_por")
    private TipoCobrancaEnum tipoCobraRecepcaoPor;

    @Digits(integer = 3, fraction = 9)
    @Column(name = "valor_taxa_saca_60_recepcao")
    private BigDecimal valorTaxaSaca60Recepcao;
    
    @Digits(integer = 8, fraction = 9)
    @Column(name = "valor_taxa_recepcao")
    private BigDecimal valorTaxaRecepcao;

    @Column(name = "cobra_secagem")
    private Boolean cobraSecagem;

    @Column(name = "tipo_cobra_secagem_na")
    private TipoCobrancaEnum tipoCobraSecagemNa;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cobranca_secagem_por")
    private TipoCobrancaSecagemEnum tipoCobrancaSecagemPor;

    @Column(name = "cobranca_armazenagem")
    private Boolean cobrancaArmazenagem;

    @Digits(integer = 8, fraction = 9)
    @Column(name = "valor_armazenagem")
    private BigDecimal valorArmazenagem;
    
    @Digits(integer = 3, fraction = 9)
    @Column(name = "valor_taxa_saca_60")
    private BigDecimal valorTaxaSaca60;
    
    @Column(name = "nr_dias_carencia")
    private Integer numeroDiasCarencia;

    @Column(name = "data_cobranca_armazenagem")
    private Date dataCobrancaArmazenagem;

    @Column(name = "data_cobranca_armazenagem_2")
    private Date dataCobrancaArmazenagem2;

    @JsonBackReference
    @OneToMany(mappedBy = "taxa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Where(clause = "data_inativacao is null")
    private List<TaxaGrupoProduto> grupoProdutos;
    
    @JsonBackReference
    @OneToMany(mappedBy = "taxa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Where(clause = "data_inativacao is null")
    private List<TaxaEstabelecimento> estabelecimentos;

    @JsonBackReference
    @OneToMany(mappedBy = "taxa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Where(clause = "data_inativacao is null")
    private List<TaxaCarenciaArmazenagem> carenciaArmazenagens;
}
