package br.coop.integrada.api.pa.domain.model.integration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.enums.integration.OrigemInputEnum;
import br.coop.integrada.api.pa.domain.enums.integration.TipoIntegracaoEnum;
import br.coop.integrada.api.pa.domain.enums.integration.TipoSincronizacaoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "integration_page")
public class IntegracaoPagina implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;	
	@Enumerated(EnumType.STRING)
	@Column(name="pagina", unique = true)
	private PaginaEnum paginaEnum;
	
	@Enumerated(EnumType.STRING)
	@Column(name="origem")
	private OrigemInputEnum origenEnum; 
	
	@ManyToOne
	@JoinColumn(name = "id_auth_prod")
	private IntegrationAuth authProd;
	
	@ManyToOne
	@JoinColumn(name = "id_auth_dev")
	private IntegrationAuth authDev;
	
	@ManyToOne
	@JoinColumn(name = "id_auth_homolog")
	private IntegrationAuth authHomolog;
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo")
	private TipoIntegracaoEnum tipoEnum;
	
	@Enumerated(EnumType.STRING)
	@Column(name="sincronizacao")
	private TipoSincronizacaoEnum sincronizacaoEnum;
	
	@Column(name="url_api_prod")
	private String urlApiProd;
	
	@Column(name="url_api_dev")
	private String urlApiDev;
	
	@Column(name="url_api_homolog")
	private String urlApiHomolog;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "integracaoPagina", fetch = FetchType.LAZY, cascade= CascadeType.ALL, orphanRemoval = true)
	private List<IntegracaoPaginaHeader> headers = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "integracaoPagina", fetch = FetchType.LAZY, cascade= CascadeType.ALL, orphanRemoval = true)
	private List<IntegracaoPaginaFuncionalidade> funcionalidades = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "integracaoPagina", fetch = FetchType.LAZY, cascade= CascadeType.ALL, orphanRemoval = true)
	private List<IntegracaoPaginaParametros> parametros = new ArrayList<>();
		
	public String getUrlPrincipalApi(String profileActive) {
		switch (profileActive) {
			case "dev": {
				return getUrlApiDev();
			}
			case "prod": {
				return getUrlApiProd();
			}
			case "homolog": {
				return getUrlApiHomolog();
			}
			default:
				return null;
		}
	}
	
	public IntegrationAuth getAuth(String profileActive) {
		switch (profileActive) {
			case "dev": {
				return getAuthDev();
			}
			case "prod": {
				return getAuthProd();
			}
			case "homolog": {
				return getAuthHomolog();
			}
			default:
				return null;
		}
	}
	
	public IntegracaoPaginaFuncionalidade getFuncionalidade(FuncionalidadePaginaEnum funcionalidade) {
		if(!CollectionUtils.isEmpty(funcionalidades)) {
			for(IntegracaoPaginaFuncionalidade item: funcionalidades) {
				if(item.getFuncionalidade() != null && item.getFuncionalidade().equals(funcionalidade)) {
					return item;
				}
			}
		}
		
		throw new ObjectDefaultException("Não foi encontrado a parâmetrização da funcinalidade \"" + funcionalidade.getDescricao().toUpperCase() + "\" no cadastro de integração \"" + paginaEnum.getDescricao().toUpperCase() + "\".");
	}
	
	public boolean isHearderProfileActive(IntegracaoPaginaHeader header, String profileActive) {
		switch (profileActive) {
			case "dev": {
				return header.getDesenvolvimento() != null ? header.getDesenvolvimento() : false;
			}
			case "prod": {
				return header.getProducao() != null ? header.getProducao() : false;
			}
			case "homolog": {
				return header.getHomologacao() != null ? header.getHomologacao() : false;
			}
			default:
				return false;
		}
	}
	
	

}
