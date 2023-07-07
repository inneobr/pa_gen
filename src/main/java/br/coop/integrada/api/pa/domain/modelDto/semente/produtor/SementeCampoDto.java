package br.coop.integrada.api.pa.domain.modelDto.semente.produtor;

import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampo;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampoProdutor;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeLaudoInspecao;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class SementeCampoDto implements Serializable {

    private Long id;

    private Integer safra;
    private Integer ordemCampo;
    private String codigoEstabelecimento;
    private String codigoFamilia;
    private Long classeCodigo;
    private String classeDescricao;

    private String codigoProdutor;
    private String matricula;
    private String codigoProduto;
    private BigDecimal area;
    private BigDecimal estimativaProducao;
    private BigDecimal metaProducao;
    private Integer campoAprovado;
    private Date dataAprovacaoPlantio;
    private Date dataAprovacaoRecolhimento;
    private BigDecimal coordenadaLatitude;
    private BigDecimal coordenadaLongitude;
    private String periodoPlantio;
    private String variedade;
    private Date dataUltimaIntegracao;

    public static SementeCampoDto construir(SementeCampo sementeCampo) {
        var grupoSementeDto = new SementeCampoDto();
        BeanUtils.copyProperties(sementeCampo, grupoSementeDto);

        if(sementeCampo.getDataAtualizacao() == null) {
            grupoSementeDto.setDataUltimaIntegracao(sementeCampo.getDataCadastro());
        }
        else {
            grupoSementeDto.setDataUltimaIntegracao(sementeCampo.getDataAtualizacao());
        }

        return grupoSementeDto;
    }
}
