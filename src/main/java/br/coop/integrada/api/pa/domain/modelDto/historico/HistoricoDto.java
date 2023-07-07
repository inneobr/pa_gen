package br.coop.integrada.api.pa.domain.modelDto.historico;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data @NoArgsConstructor @AllArgsConstructor
public class HistoricoDto{

    private Long id;
    private Integer nroDocPesagem;
    private String codEstabelecimento;
    private String codUsuario;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
    private Date data;
    private String hora;
    private String transacao;
    private String observacao;
    private Integer safra;
	
}
