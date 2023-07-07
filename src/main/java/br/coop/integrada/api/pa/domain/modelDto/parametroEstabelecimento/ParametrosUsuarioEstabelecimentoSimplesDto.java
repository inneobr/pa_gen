package br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento;

import lombok.Data;

import java.util.Date;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoResumidoDto;

@Data
public class ParametrosUsuarioEstabelecimentoSimplesDto {
	
	private Long id;
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
	private EstabelecimentoResumidoDto estabelecimento;	
	private Date dataCadastro;
	private Date dataAtualizacao;
	private Date dataIntegracao;
	private StatusIntegracao statusIntegracao;
}
