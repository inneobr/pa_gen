package br.coop.integrada.api.pa.domain.model.pedidoIntegracao;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.pedidoIntegracao.OperacaoEnum;
import br.coop.integrada.api.pa.domain.enums.pedidoIntegracao.TipoIntegracaoEnum;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "pedido_integracao")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Integracao extends AbstractEntity{
	
    private static final long serialVersionUID = 1L;
    
    @NotBlank(message = "Campo {Nome da Tabela} obrigatório")
	@Column(name = "table_Name", nullable = false)
	private String tableName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_Integracao", nullable = false)
	private TipoIntegracaoEnum tipoIntegracao;
        
	@Column(name = "id_Reg1")
	private String idReg1;
	
	@Column(name = "id_Reg2")
	private String idReg2;
	
	@Column(name = "id_Reg3")
	private String idReg3;
	
	@Column(name = "id_Reg4")
	private String idReg4;
	
	@Enumerated(EnumType.STRING)
	//@NotBlank(message = "Campo {operacao} obrigatório")
	@Column(name = "operacao")
	private OperacaoEnum operacao;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private StatusIntegracao status;
	
	//@Column(name = "dt_UltInt")
	//private Date dataUltimaIntegracao;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "integracao", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<IntegracaoCampos> itens = new ArrayList<>();

}
