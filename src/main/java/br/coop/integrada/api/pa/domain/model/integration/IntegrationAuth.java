package br.coop.integrada.api.pa.domain.model.integration;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import br.coop.integrada.api.pa.domain.enums.integration.TipoAuthenticationEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "integracao_auth")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class IntegrationAuth implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;	
	
	@Column(name="login")
	private String login;	

	private String senha;
	
	@Column(name="descricao", unique=true)
	private String descricao;
	
	private String urlApi;
		
	@Enumerated(EnumType.STRING)
	@Column(name="tipo")
	private TipoAuthenticationEnum tipoEnum;
	
	private Integer minRefreshToken;
	
	@Lob
	private String payloadRetorno;	
	
	@Lob
	private String token;	
	
	@Lob
	private String refreshToken;
	
	
}
