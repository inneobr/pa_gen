package br.coop.integrada.api.pa.domain.model.produto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(
		name = "produto_referencia",
		uniqueConstraints = {
				@UniqueConstraint(name = "KEY_COD_REFERENCIA", columnNames = { "cod_rereferencia" })
		}
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProdutoReferencia extends AbstractEntity {
	private static final long serialVersionUID = 1L;
		
	@Column(name="cod_rereferencia")
	private String codRef;	
}
