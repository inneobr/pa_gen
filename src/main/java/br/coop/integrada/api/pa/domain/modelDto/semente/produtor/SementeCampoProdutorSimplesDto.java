package br.coop.integrada.api.pa.domain.modelDto.semente.produtor;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampoProdutor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SementeCampoProdutorSimplesDto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String idUnico;
	private Integer safra;
    private Integer ordemCampo;
    private String codigoFamilia;
    private Long classeCodigo;
    private String codigoProdutor;
    private String codigoEstabelecimento;
	private IntegrationOperacaoEnum operacao;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataUltimaIntegracao;

    public static SementeCampoProdutorSimplesDto construir(SementeCampoProdutor produtor) {
        var produtorDto = new SementeCampoProdutorSimplesDto();
        BeanUtils.copyProperties(produtor, produtorDto);

        if(produtor.getDataAtualizacao() == null) {
            produtorDto.setDataUltimaIntegracao(produtor.getDataCadastro());
        }
        else {
            produtorDto.setDataUltimaIntegracao(produtor.getDataAtualizacao());
        }

        return produtorDto;
    }

    public static List<SementeCampoProdutorSimplesDto> construir(List<SementeCampoProdutor> produtores) {
        if(produtores == null) return new ArrayList<>();
        return produtores.stream().map(sementeCampoProdutor -> {
            return SementeCampoProdutorSimplesDto.construir(sementeCampoProdutor);
        }).toList();
    }
}
