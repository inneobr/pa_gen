package br.coop.integrada.api.pa.domain.model.naturezaOperacao;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import org.springframework.transaction.annotation.Transactional;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor @Transactional
@Table(
		name = "natureza_operacao_estabelecimento",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "KEY_NATUREZA_OPERACAO_ESTABELECIMENTO",
						columnNames = { "id_estabelecimento" }
				)
		}
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class NaturezaOperacaoEstabelecimento extends AbstractEntity{
	private static final long serialVersionUID = 1L;	
	
	@ManyToOne
	@JoinColumn(name = "id_natureza_operacao")
	private NaturezaOperacao naturezaOperacao;
	
	@ManyToOne
	@JoinColumn(name = "id_estabelecimento")
	private Estabelecimento estabelecimento;
	
	@Column(name = "id_unico")
	private String idUnico;
	
}
