package br.coop.integrada.api.pa.domain.modelDto.semente.produtor;

import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampoProdutor;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SementeCampoProdutorDto implements Serializable {

    private Integer Safra;
    private Integer ordemCampo;
    private String codigoProdutor;
    private String codigoEstabelecimento;
    private String codigoFamilia;
    private Long classeCodigo;
    private String classeDescricao;
    private Date dataUltimaIntegracao;

    public static SementeCampoProdutorDto construir(SementeCampoProdutor produtor) {
        var produtorDto = new SementeCampoProdutorDto();
        BeanUtils.copyProperties(produtor, produtorDto);

        if(produtor.getDataAtualizacao() == null) {
            produtorDto.setDataUltimaIntegracao(produtor.getDataCadastro());
        }
        else {
            produtorDto.setDataUltimaIntegracao(produtor.getDataAtualizacao());
        }

        return produtorDto;
    }

    public static List<SementeCampoProdutorDto> construir(List<SementeCampoProdutor> produtores) {
        if(produtores == null) return new ArrayList<>();
        return produtores.stream().map(sementeCampoProdutor -> {
            return SementeCampoProdutorDto.construir(sementeCampoProdutor);
        }).toList();
    }
}
