package br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao;

import java.time.LocalTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.coop.integrada.api.pa.domain.enums.HistoricoEnum;
import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacao;
import br.coop.integrada.api.pa.domain.model.usuario.Usuario;
import lombok.Data;

@Data
public class NaturezaOperacaoMovimentacaoDto {
	private Long id;
	private String id_unico;	
	private Date data;
	private LocalTime hora;
	private String usuario;
	private String movimento;
	private String observacao;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private NaturezaOperacao naturezaOperacao;
	
	public static NaturezaOperacaoMovimentacaoDto construir(NaturezaOperacao naturezaOperacao, String usuario, String movimento, String observacao) {
		NaturezaOperacaoMovimentacaoDto naturezaOperacaoMovimentacaoDto = new NaturezaOperacaoMovimentacaoDto();
		naturezaOperacaoMovimentacaoDto.setData(new Date());
		naturezaOperacaoMovimentacaoDto.setHora(LocalTime.now());
		naturezaOperacaoMovimentacaoDto.setNaturezaOperacao(naturezaOperacao);
		naturezaOperacaoMovimentacaoDto.setUsuario(usuario);
		naturezaOperacaoMovimentacaoDto.setMovimento(movimento);
		naturezaOperacaoMovimentacaoDto.setObservacao(observacao);				
		return naturezaOperacaoMovimentacaoDto;
	}
}
