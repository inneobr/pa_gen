package br.coop.integrada.api.pa.domain.model.parametros.safra.composta;

import br.coop.integrada.api.pa.domain.enums.TipoSafraEnum;
import br.coop.integrada.api.pa.domain.enums.TipoSafraOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.produto.TipoProduto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.SafraCompostaComGrupoProdutoDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import static javax.persistence.FetchType.LAZY;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "safra_composta")

@NamedNativeQuery(
        name = "SafraComposta.safrasCompostasComGrupoProduto",
        query = """
                SELECT DISTINCT
                    GP.FM_CODIGO AS fmCodigo,
                    GP.DESCRICAO AS descricao,
                    SC.ID as id,
                    SC.NOME_SAFRA AS nomeSafra,
                    SC.DIA_INICIAL AS diaInicial,
                    SC.MES_INICIAL AS mesInicial,
                    SC.ANO_INICIAL_OPERACAO AS anoInicialOperacao,
                    SC.ANO_INICIAL_QUANTIDADE AS anoInicialQuantidade,
                    SC.DIA_FINAL AS diaFinal,
                    SC.MES_FINAL AS mesFinal,
                    SC.ANO_FINAL_OPERACAO AS anoFinalOperacao,
                    SC.ANO_FINAL_QUANTIDADE AS anoFinalQuantidade,
                    SC.SAFRA_PLANTIO_OPERACAO AS safraPlantioOperacao,
                    SC.SAFRA_PLANTIO_QUANTIDADE AS safraPlantioQuantidade,
                    SC.SAFRA_COLHEITA_OPERACAO AS safraColheitaOperacao,
                    SC.SAFRA_COLHEITA_QUANTIDADE AS safraColheitaQuantidade
                FROM SAFRA_COMPOSTA SC
                INNER JOIN GRUPO_PRODUTO GP
                    ON GP.TIPO_PRODUTO = SC.ID_TIPO_PRODUTO
                    AND GP.DATA_INATIVACAO IS NULL
                WHERE
                    SC.DATA_INATIVACAO IS NULL
        """,
        resultSetMapping = "Mapping.SafraCompostaComGrupoProdutoDto"
)
@SqlResultSetMapping(
        name = "Mapping.SafraCompostaComGrupoProdutoDto",
        classes = {
                @ConstructorResult(
                        targetClass = SafraCompostaComGrupoProdutoDto.class,
                        columns = {
                                @ColumnResult(name = "fmCodigo", type = String.class),
                                @ColumnResult(name = "descricao", type = String.class),
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "nomeSafra", type = String.class),
                                @ColumnResult(name = "diaInicial", type = Integer.class),
                                @ColumnResult(name = "mesInicial", type = Integer.class),
                                @ColumnResult(name = "anoInicialOperacao", type = String.class),
                                @ColumnResult(name = "anoInicialQuantidade", type = Integer.class),
                                @ColumnResult(name = "diaFinal", type = Integer.class),
                                @ColumnResult(name = "mesFinal", type = Integer.class),
                                @ColumnResult(name = "anoFinalOperacao", type = String.class),
                                @ColumnResult(name = "anoFinalQuantidade", type = Integer.class),
                                @ColumnResult(name = "safraPlantioOperacao", type = String.class),
                                @ColumnResult(name = "safraPlantioQuantidade", type = Integer.class),
                                @ColumnResult(name = "safraColheitaOperacao", type = String.class),
                                @ColumnResult(name = "safraColheitaQuantidade", type = Integer.class)
                        }
                )
        }
)
public class SafraComposta extends AbstractEntity {

    private static final long serialVersionUID = 1L;

	@ManyToOne
    @JoinColumn(name = "id_tipo_produto", nullable = false)
    private TipoProduto tipoProduto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_safra")
    private TipoSafraEnum tipoSafra;

    @Column(name = "nome_safra")
    private String nomeSafra;

    @Column(name = "dia_inicial")
    private Integer diaInicial;

    @Column(name = "mes_inicial")
    private Integer mesInicial;

    @Enumerated(EnumType.STRING)
    @Column(name = "ano_inicial_operacao")
    private TipoSafraOperacaoEnum anoInicialOperacao;

    @Column(name = "ano_inicial_quantidade")
    private Integer anoInicialQuantidade;

    @Column(name = "dia_final")
    private Integer diaFinal;

    @Column(name = "mes_final")
    private Integer mesFinal;

    @Enumerated(EnumType.STRING)
    @Column(name = "ano_final_operacao")
    private TipoSafraOperacaoEnum anoFinalOperacao;

    @Column(name = "ano_final_quantidade")
    private Integer anoFinalQuantidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "safra_plantio_operacao")
    private TipoSafraOperacaoEnum safraPlantioOperacao;

    @Column(name = "safra_plantio_quantidade")
    private Integer safraPlantioQuantidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "safra_colheita_operacao")
    private TipoSafraOperacaoEnum safraColheitaOperacao;

    @Column(name = "safra_colheita_quantidade")
    private Integer safraColheitaQuantidade;

    @OneToMany(mappedBy = "safraComposta", fetch = LAZY)
    @Where(clause = "data_inativacao is null")
    private List<SafraCompostaEstabelecimento> estabelecimentos;
}
