package br.coop.integrada.api.pa.domain.model.estabelecimentos;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.OneToMany;

import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CNPJ;
import org.springframework.context.annotation.Lazy;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.parametros.ParametroEstabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.ParametrosUsuarioEstabelecimento;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table( 
		name = "estabelecimento",
		uniqueConstraints = {
				@UniqueConstraint(name = "KEY_CODIGO", columnNames = { "codigo" })
		}
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Estabelecimento extends AbstractEntity{
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Campo {codigo} obrigatório")
	@Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

	@NotBlank(message = "Campo {codigoRegional} obrigatório")
	@Column(name = "codigo_regional", nullable = false)
    private String codigoRegional;

	@NotBlank(message = "Campo {razaoSocial} obrigatório")
	@Column(name = "razao_social", nullable = false, unique = true)
    private String razaoSocial;

	@NotBlank(message = "Campo {nomeFantasia} obrigatório")
	@Column(name = "nome_fantasia", nullable = false)
    private String nomeFantasia;
	
	@Column(name = "endereco")
    private String endereco;
	
	@Column(name = "bairro")
    private String bairro;
	
	@Column(name = "cidade")
    private String cidade;
	
	@Column(name = "estado")
    private String estado;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "telefone")
	private String telefone;

	@CNPJ
	@Column(name = "cnpj", unique = true)
    private String cnpj;
	
	@Column(name = "inscricao_estadual", unique = true)
    private String inscricaoEstadual;

	@NotNull(message = "Campo {é Matriz} obrigatório")
	@Column(name="matriz")
	private Boolean matriz;	
	
	@JsonIgnore
	@Lazy
	@OneToMany(mappedBy = "estabelecimento")
    private List<ParametroEstabelecimento> parametrosEstabelecimento;	
	
	@JsonIgnore
	@Lazy
	@OneToMany(mappedBy = "estabelecimento")
    private List<ParametrosUsuarioEstabelecimento> parametrosUsuarios;
	
	public String getCodigoComZero() {
		Integer tamanho = codigo.length();
		
		if(tamanho > 3) return codigo;
		
		String retorno = codigo;
		for(Integer indice = tamanho; indice < 3; indice++) {
			retorno = "0" + retorno;
		}
		
        return retorno;
    }
    
    public String getCodNome() {
        return codigo + " - "+ nomeFantasia;
    }
    
    public boolean isRegional() {
        return codigo.equals(codigoRegional);
    } 
    
    @Override
    public String toString() {
    	return codigo;
    }
	 
}
