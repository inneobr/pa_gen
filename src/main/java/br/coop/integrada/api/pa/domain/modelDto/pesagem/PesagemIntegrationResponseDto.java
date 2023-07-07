package br.coop.integrada.api.pa.domain.modelDto.pesagem;

import br.coop.integrada.api.pa.domain.modelDto.historico.HistoricoResponseDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PesagemIntegrationResponseDto implements Serializable {

    private List<PesagemResponseDto> pesagens;
    private List<HistoricoResponseDto> historicos;

    public static PesagemIntegrationResponseDto construir(List<PesagemResponseDto> pesagens, List<HistoricoResponseDto> historicos) {
        var objDto = new PesagemIntegrationResponseDto();
        objDto.setPesagens(pesagens);
        objDto.setHistoricos(historicos);

        return objDto;
    }
}
