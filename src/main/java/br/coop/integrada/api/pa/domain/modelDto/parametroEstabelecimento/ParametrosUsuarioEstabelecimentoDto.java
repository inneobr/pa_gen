package br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento;

import lombok.Data;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotBlank;

import br.coop.integrada.api.pa.domain.model.usuario.Usuario;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoResumidoDto;

@Data
public class ParametrosUsuarioEstabelecimentoDto {
	
	private Long id;
	
	@NotBlank(message = "Campo {codUsuario} obrigatório")
	private Usuario usuario;
	private String observacao;	
	private Boolean re;
	private Boolean cancelaRe;
	private Boolean transferencia;
	private Boolean fechamento;
	private Boolean cancelaFechamento;
	private Boolean liberaSenha;
	private Boolean alteraContrato;
	private Boolean agendamento;
	private Boolean retirada;
	private Boolean transfCooperado;	
	private Boolean encerDiario;
	private Boolean baixaMassaContrato;
	private Boolean alteraPadrao;
	private Boolean usuarioRegional;
	private Date dataIntegracao;
	
	@NotBlank(message = "Selecione ao menos um {estabelecimento}.")
	private List<EstabelecimentoResumidoDto> estabelecimentos;
}
