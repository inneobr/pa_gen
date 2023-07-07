package br.coop.integrada.api.pa.domain.modelDto.pesagem;

import br.coop.integrada.api.pa.domain.modelDto.historico.HistoricoDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PesagemIntegrationSaveDto implements Serializable {

    private List<PesagemPutDto> pesagens;
    private List<HistoricoDto> historicos;
}
