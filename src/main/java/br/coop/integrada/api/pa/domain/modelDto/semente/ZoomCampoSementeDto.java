package br.coop.integrada.api.pa.domain.modelDto.semente;

import java.io.Serializable;

import lombok.Data;

@Data
public class ZoomCampoSementeDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer ordemCampo;
    private String matriculaImovel;
    private String codigoProduto;
    private String descricaoProduto;
    private Long codigoClasse;
    private String descricaoClasse;
    private Integer codigoProdutor;
    private String nomeProdutor;

    public ZoomCampoSementeDto(Integer ordemCampo, String matriculaImovel, String codigoProduto,
            String descricaoProduto, Long codigoClasse, String descricaoClasse, Integer codigoProdutor,
            String nomeProdutor
    ) {
        this.ordemCampo = ordemCampo;
        this.matriculaImovel = matriculaImovel;
        this.codigoProduto = codigoProduto;
        this.descricaoProduto = descricaoProduto;
        this.codigoClasse = codigoClasse;
        this.descricaoClasse = descricaoClasse;
        this.codigoProdutor = codigoProdutor;
        this.nomeProdutor = nomeProdutor;
    }
}
