package br.coop.integrada.api.pa.domain.model.pedidoIntegracao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonBackReference;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pedido_integracao_campos", uniqueConstraints = {@UniqueConstraint(name = "key_un_Integracao_campos" , columnNames = { "id", "id_integracao" }) })
public class IntegracaoCampos extends AbstractEntity{
	
    private static final long serialVersionUID = 1L;
    
    @JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_integracao", nullable = false)
    private Integracao integracao;
    
    @NotBlank(message = "Campo {nomeCampo} obrigatório")
	@Column(name = "nome_Campo", nullable = false)
	private String nomeCampo;
    
    @NotBlank(message = "Campo {valorCampo} obrigatório")
	@Column(name = "valor_Campo", nullable = false)
	private String valorCampo;
    
}
