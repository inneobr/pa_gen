package br.coop.integrada.api.pa.domain.modelDto.semente.produtor;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeLaudoInspecao;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SementeLaudoInspecaoSimplesDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String idUnico;
	private Integer safra;
    private Integer ordemCampo;
	private Long numeroLaudo;
    private String codigoFamilia;
    private String codigoAgronomo;
    private String cooperante;
    private String codigoEstabelecimento;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date inspecao;
    private String matricula;
    private String item;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataInicioPlantio;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataTerminoPlantio;
    private BigDecimal mistruraCicloM;
    private BigDecimal mistruraCicloD;
    private BigDecimal plantasM2;
    private String outrasEspecies;
    private BigDecimal area;
    private BigDecimal areaCampo;
    private String densidadeCultura;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataAprovacaoPlantio;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataAprovacaoFlorescimento;
    private Integer fase;
    private String proibida;
    private String tolerada;
    private String isolamento;
    private BigDecimal areaAprovada;
    private String parecer;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataPreviaColheita;
    private BigDecimal producaoEsperada;
    private BigDecimal producaoTotalEsperada;
    private Long classe;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataUltimaIntegracao;

	private IntegrationOperacaoEnum operacao;

    public static SementeLaudoInspecaoSimplesDto construir(SementeLaudoInspecao laudo) {
        var laudoDto = new SementeLaudoInspecaoSimplesDto();
        BeanUtils.copyProperties(laudo, laudoDto);
        
        if(laudo.getProduto() != null) {
        	String codigoProduto = laudo.getProduto().getCodItem();
        	laudoDto.setItem(codigoProduto);
        }

        if(laudo.getDataAtualizacao() == null) {
            laudoDto.setDataUltimaIntegracao(laudo.getDataCadastro());
        }
        else {
            laudoDto.setDataUltimaIntegracao(laudo.getDataAtualizacao());
        }

        return laudoDto;
    }

    public static List<SementeLaudoInspecaoSimplesDto> construir(List<SementeLaudoInspecao> laudos) {
        if(laudos == null) return new ArrayList<>();

        return laudos.stream().map(sementeLaudoInspecao -> {
            return SementeLaudoInspecaoSimplesDto.construir(sementeLaudoInspecao);
        }).toList();
    }
}
