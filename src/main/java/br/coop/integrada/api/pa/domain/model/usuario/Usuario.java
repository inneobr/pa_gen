package br.coop.integrada.api.pa.domain.model.usuario;

import lombok.Setter;
import lombok.Getter;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "usuario")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario extends AbstractEntity {
	private static final long serialVersionUID = 1L;	

	@Column(unique = true)
	private String matricula;
	
	@Column(unique = true)
    private String codUsuario;
	
	@Column(unique = true)
	private String username;

	private String nome;
	
	@Column(name="id_regional")
	private String regional;
	
	private String cpf;
	
	@Column(name = "id_estabelecimento")
	private String estabelecimento;
	 
	 public String getCodUsuario() {
    	     if(this.codUsuario == null) {
                 this.codUsuario = this.getId()+"-"+this.username;
             }
	        return codUsuario;
	  }  
}
