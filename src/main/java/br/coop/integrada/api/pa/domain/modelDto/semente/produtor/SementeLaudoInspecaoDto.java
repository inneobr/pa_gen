package br.coop.integrada.api.pa.domain.modelDto.semente.produtor;

import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeLaudoInspecao;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SementeLaudoInspecaoDto  implements Serializable {

    private Long id;

    /* CHAVE COMPOSTA */
    private Integer safra;
    private String codigoEstabelecimento;
    private Long numeroLaudo;
    private Integer ordemCampo;
    private String codigoFamilia;

    private String codigoAgronomo;
    private String cooperante;
    private Date inspecao;
    private String matricula;
    private String item;
    private Date dataInicioPlantio;
    private Date dataTerminoPlantio;
    private BigDecimal plantasM2;
    private BigDecimal mistruraCicloM;
    private BigDecimal mistruraCicloD;
    private String outrasEspecies;
    private BigDecimal area;
    private BigDecimal areaCampo;
    private String densidadeCultura;
    private Date dataAprovacaoPlantio;
    private Date dataAprovacaoFlorescimento;
    private Integer fase;
    private String proibida;
    private String tolerada;
    private String isolamento;
    private BigDecimal areaAprovada;
    private String parecer;
    private Date dataPreviaColheita;
    private BigDecimal producaoEsperada;
    private BigDecimal producaoTotalEsperada;
    private Long classeCodigo;
    private String classeDescricao;
    private Date dataUltimaIntegracao;

    public static SementeLaudoInspecaoDto construir(SementeLaudoInspecao laudo) {
        var laudoDto = new SementeLaudoInspecaoDto();
        BeanUtils.copyProperties(laudo, laudoDto);

        if(laudo.getDataAtualizacao() == null) {
            laudoDto.setDataUltimaIntegracao(laudo.getDataCadastro());
        }
        else {
            laudoDto.setDataUltimaIntegracao(laudo.getDataAtualizacao());
        }

        return laudoDto;
    }

    public static List<SementeLaudoInspecaoDto> construir(List<SementeLaudoInspecao> laudos) {
        if(laudos == null) return new ArrayList<>();

        return laudos.stream().map(sementeLaudoInspecao -> {
            return SementeLaudoInspecaoDto.construir(sementeLaudoInspecao);
        }).toList();
    }
}
