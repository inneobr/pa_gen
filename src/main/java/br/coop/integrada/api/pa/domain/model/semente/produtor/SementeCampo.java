package br.coop.integrada.api.pa.domain.model.semente.produtor;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import br.coop.integrada.api.pa.domain.modelDto.semente.ZoomCampoSementeDto;
import lombok.Data;

@Data
@Entity
@Table(
        name = "semente_campo",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "key_semente_campo",
                        columnNames = { "safra", "nr_ord_campo", "id_estabelecimento", "id_grupo_produto", "id_classe" }
                )
        }
)

@NamedNativeQuery(
        name = "SementeCampo.findBySafraAndCodigoEstabelecimentoAndCodigoGrupoProduto",
        query = """
                SELECT
                    SC.NR_ORD_CAMPO AS ordemCampo,
                    SC.MATRICULA AS matriculaImovel,
                    P.COD_ITEM AS codigoProduto,
                    P.DES_ITEM AS descricaoProduto,
                    SCLASSE.CODIGO AS codigoClasse,
                    SCLASSE.DESCRICAO AS descricaoClasse,
                    PR.COD_PRODUTOR AS codigoProdutor,
                    PR.NOME AS nomeProdutor
                FROM SEMENTE_CAMPO SC
                INNER JOIN SEMENTE_CLASSE SCLASSE
                    ON SCLASSE.ID = SC.ID_CLASSE
                INNER JOIN SEMENTE_CAMPO_PRODUTOR SCP
                    ON SCP.SAFRA = SC.SAFRA
                    AND SCP.NR_ORD_CAMPO = SC.NR_ORD_CAMPO
                    AND SCP.ID_ESTABELECIMENTO = SC.ID_ESTABELECIMENTO
                    AND SCP.ID_GRUPO_PRODUTO = SC.ID_GRUPO_PRODUTO
                    AND SCP.ID_CLASSE = SC.ID_CLASSE
                INNER JOIN ESTABELECIMENTO E
                    ON E.ID = SC.ID_ESTABELECIMENTO
                    AND E.CODIGO = :codigoEstabelecimento
                INNER JOIN GRUPO_PRODUTO GP
                    ON GP.ID = SC.ID_GRUPO_PRODUTO
                    AND GP.FM_CODIGO = :codigoGrupoProduto
                INNER JOIN PRODUTO P
                    ON P.ID = SC.ID_PRODUTO
                INNER JOIN PRODUTOR PR
                    ON PR.ID = SCP.ID_PRODUTOR
                WHERE
                    SC.SAFRA = :safra
        """,
        resultSetMapping = "Mapping.findBySafraAndCodigoEstabelecimentoAndCodigoGrupoProduto"
)
@SqlResultSetMapping(
        name = "Mapping.findBySafraAndCodigoEstabelecimentoAndCodigoGrupoProduto",
        classes = {
                @ConstructorResult(
                        targetClass = ZoomCampoSementeDto.class,
                        columns = {
                                @ColumnResult(name = "ordemCampo", type = Integer.class),
                                @ColumnResult(name = "matriculaImovel", type = String.class),
                                @ColumnResult(name = "codigoProduto", type = String.class),
                                @ColumnResult(name = "descricaoProduto", type = String.class),
                                @ColumnResult(name = "codigoClasse", type = Long.class),
                                @ColumnResult(name = "descricaoClasse", type = String.class),
                                @ColumnResult(name = "codigoProdutor", type = Integer.class),
                                @ColumnResult(name = "nomeProdutor", type = String.class)
                        }
                )
        }
)
public class SementeCampo extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Campo {idUnico} obrigatório")
	@Column(name = "id_unico", unique = true)
	private String idUnico;

    @NotNull(message = "O campo {safra} é obrigatório!")
    @Column(name = "safra", nullable = false)
    private Integer safra;

    @NotNull(message = "O campo {ordemCampo} é obrigatório!")
    @Column(name = "nr_ord_campo", nullable = false)
    private Integer ordemCampo;

    @ManyToOne
    @NotNull(message = "O campo {codigoEstabelecimento} é obrigatório!")
    @JoinColumn(name = "id_estabelecimento", nullable = false)
    private Estabelecimento estabelecimento;

    @ManyToOne
    @NotNull(message = "O campo {codigoFamilia} é obrigatório!")
    @JoinColumn(name = "id_grupo_produto", nullable = false)
    private GrupoProduto grupoProduto;

    @ManyToOne
    @NotNull(message = "O campo {classeCodigo} é obrigatório!")
    @JoinColumn(name = "id_classe", nullable = false)
    private SementeClasse classe;

    @Column(name = "cod_cliente")
    private String codigoProdutor;

    @Column(name = "matricula")
    private String matricula;
    
    @Column(name = "imovel")
    private String imovel;

    @ManyToOne
    @JoinColumn(name = "id_produto")
    private Produto produto;

    @Column(name = "area")
    private BigDecimal area;

    @Column(name = "estima_prod")
    private BigDecimal estimativaProducao;

    @Column(name = "meta_prod")
    private BigDecimal metaProducao;

    @Column(name = "campo_aprov")
    private Integer campoAprovado;

    @Column(name = "dt_aprov_plantio")
    private Date dataAprovacaoPlantio;

    @Column(name = "dt_aprov_precolh")
    private Date dataAprovacaoRecolhimento;

    @Column(name = "coord_lat")
    private BigDecimal coordenadaLatitude;

    @Column(name = "coord_long")
    private BigDecimal coordenadaLongitude;

    @Size(message = "O campo {periodoPlantio} possui um limite de 15 caracteres", max = 15)
    @Column(name = "periodo_plantio", length = 15)
    private String periodoPlantio;

    @Size(message = "O campo {variedade} possui um limite de 40 caracteres", max = 40)
    @Column(name = "variedade", length = 40)
    private String variedade;

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

    public String getCodigoProduto() {
        if(produto != null) {
            return produto.getCodItem();
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