package br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento;

import lombok.Data;

import java.io.Serializable;

import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;


@Data
public class ParametrosUsuarioEstabelecimentoIntegrationSaveDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String idUnico;
	private String codigoEstabelecimento;
    private String codigoUsuario;
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
    private String observacao;
    private Boolean encerDiario;
    private Boolean baixaMassaContrato;
    private Boolean alteraPadrao;
    private Boolean usuarioRegional;
    private IntegrationOperacaoEnum operacao;
    
}
