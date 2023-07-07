package br.coop.integrada.api.pa.domain.modelDto.semente.produtor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampo;
import lombok.Data;

@Data
public class GrupoSementeProdutorDto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String idUnico;
	private Integer safra;
    private Integer ordemCampo;
    private String codigoFamilia;
    private Long classeCodigo;
    private String codigoProdutor;
    private String matricula;
    private String codigoProduto;
    private BigDecimal area;    
    private String codigoEstabelecimento;
    private BigDecimal estimativaProducao;
    private BigDecimal metaProducao;
    private Integer campoAprovado;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataAprovacaoPlantio;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataAprovacaoRecolhimento;

    private BigDecimal coordenadaLatitude;
    private BigDecimal coordenadaLongitude;
    private String periodoPlantio;
    private String variedade;
    
    @JsonProperty(access = Access.READ_ONLY)
    private String classeDescricao;
    private String imovel;
	private IntegrationOperacaoEnum operacao;
        
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
    private Date dataUltimaIntegracao;
    
    public static GrupoSementeProdutorDto construir(SementeCampo sementeCampo) {
        var produtorDto = new GrupoSementeProdutorDto();
        BeanUtils.copyProperties(sementeCampo, produtorDto);

        if(sementeCampo.getDataAtualizacao() == null) {
            produtorDto.setDataUltimaIntegracao(sementeCampo.getDataCadastro());
        }
        else {
            produtorDto.setDataUltimaIntegracao(sementeCampo.getDataAtualizacao());
        }

        return produtorDto;
    }

    public static List<GrupoSementeProdutorDto> construir(List<SementeCampo> campos) {
        if(campos == null) return new ArrayList<>();
        return campos.stream().map(sementeCampo -> {
            return GrupoSementeProdutorDto.construir(sementeCampo);
        }).toList();
    }
    
}
