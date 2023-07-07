package br.coop.integrada.api.pa.domain.modelDto.parametros.taxa;

import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class TaxaRetornoCadastroDto extends AbstractResponseDto implements Serializable {

    private Long id;
    private String codigoERP;
    private String descricao;

    public static TaxaRetornoCadastroDto construir(Long id, String codigoERP, String descricao, String mensagem, Boolean integrado) {
        var objDto = new TaxaRetornoCadastroDto();
        objDto.setId(id);
        objDto.setCodigoERP(codigoERP);
        objDto.setDescricao(descricao);
        objDto.setMessage(mensagem);
        objDto.setIntegrated(integrado);

        return objDto;
    }
}
