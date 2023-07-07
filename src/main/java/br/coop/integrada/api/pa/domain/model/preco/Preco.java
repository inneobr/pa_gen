package br.coop.integrada.api.pa.domain.model.preco;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(
		name = "preco",
		uniqueConstraints = {
				@UniqueConstraint(name = "KEY_ID_UNICO", columnNames = { "id_unico" })
		}
)
public class Preco extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "Campo {idUnico} obrigatório")
	@Column(name = "id_unico", unique = true)
	private String idUnico;
	
	
	@NotNull(message = "Campo {codigoEstabelecimento} é obrigatório")
	@Column(name = "codigo_estabelecimento", nullable = false)
	private String codigoEstabelecimento;
	
	@NotNull(message = "Campo {codigoProduto} é obrigatório")
	@Column(name = "codigo_produto", nullable = false)
	private String codigoProduto;
	
	@Column(name = "codigo_referencia")
	private String codigoReferencia;
	
    @Column(name = "preco_fiscal")
    @Digits(integer = 6, fraction = 9)
	@NotNull(message = "Campo {precoFiscal} é obrigatório")
	private BigDecimal precoFiscal;
    
    @Column(name = "preco_fechamento")
    @Digits(integer = 6, fraction = 9)
	private BigDecimal precoFechamento;
    
    @Column(name = "preco_coco")
    @Digits(integer = 6, fraction = 9)
	private BigDecimal precoFechamentoCoco;
    
    @Column(name = "data_validade")
	private LocalDate dataValidade;
    
    @Column(name = "hora_validade")
	private String horaValidade;

}
