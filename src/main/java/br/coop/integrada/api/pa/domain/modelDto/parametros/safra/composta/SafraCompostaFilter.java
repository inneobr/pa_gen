package br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SafraCompostaFilter implements Serializable {
    private String nomeSafra;
    private Long idEstabelecimento;
}
