package br.coop.integrada.api.pa.domain.model.parametros;

import br.coop.integrada.api.pa.aplication.utils.ComparaObjeto;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.usuario.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(
		name = "parametros_usuario_estabelecimento",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "KEY_USUARIO_X_ESTABELECIMENTO",
						columnNames = { "id_usuario", "id_estabelecimento" }
				)
		}
)
public class ParametrosUsuarioEstabelecimento extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
    @JoinColumn(name = "id_usuario")
	private Usuario usuario;
	
	@ComparaObjeto(nome = "re", verifica = true)
	private Boolean re;
	
	@ComparaObjeto(nome = "cancelaRe", verifica = true)
	private Boolean cancelaRe;
	
	@ComparaObjeto(nome = "transferencia", verifica = true)
	private Boolean transferencia;
	
	@ComparaObjeto(nome = "fechamento", verifica = true)
	private Boolean fechamento;
	
	@ComparaObjeto(nome = "cancelaFechamento", verifica = true)
	private Boolean cancelaFechamento;
	
	@ComparaObjeto(nome = "liberaSenha", verifica = true)
	private Boolean liberaSenha;
	
	@ComparaObjeto(nome = "alteraContrato", verifica = true)
	private Boolean alteraContrato;
	
	@ComparaObjeto(nome = "agendamento", verifica = true)
	private Boolean agendamento;
	
	@ComparaObjeto(nome = "retirada", verifica = true)
	private Boolean retirada;
	
	@ComparaObjeto(nome = "transfCooperado", verifica = true)
	private Boolean transfCooperado;	
	
	@ComparaObjeto(nome = "observacao", verifica = true)
	private String observacao;	
	
	@ComparaObjeto(nome = "encerDiario", verifica = true)
	private Boolean encerDiario;
	
	@ComparaObjeto(nome = "baixaMassaContrato", verifica = true)
	private Boolean baixaMassaContrato;
	
	@ComparaObjeto(nome = "alteraPadrao", verifica = true)
	private Boolean alteraPadrao;	

	@ManyToOne
    @JoinColumn(name = "id_estabelecimento")
	private Estabelecimento estabelecimento;

	public String getCodigoUsuario() {
		if(usuario == null) return "";
		return usuario.getCodUsuario();
	}

	public String getCodigoEstabelecimento() {
		if(estabelecimento == null) return "";
		return estabelecimento.getCodigo();
	}
}
