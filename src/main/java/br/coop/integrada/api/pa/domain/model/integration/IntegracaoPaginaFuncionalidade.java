package br.coop.integrada.api.pa.domain.model.integration;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.integration.HttpRequestMethodEnum;
import br.coop.integrada.api.pa.domain.enums.integration.SituacaoFuncionalidadePaginaEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "integration_page_funcionalidade")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class IntegracaoPaginaFuncionalidade implements Serializable{
		private static final long serialVersionUID = 1L;

		@Id
		@GeneratedValue(strategy= GenerationType.IDENTITY)
		private Long id;	
		
		@Enumerated(EnumType.STRING)
		@Column(name="funcionalidade")
		private FuncionalidadePaginaEnum funcionalidade;
		
		@Enumerated(EnumType.STRING)
		@Column(name="situacao")
		private SituacaoFuncionalidadePaginaEnum situacao;
		
		@Enumerated(EnumType.STRING)
		@Column(name="metodo_send")
		private HttpRequestMethodEnum methodSend;
		
		@Enumerated(EnumType.STRING)
		@Column(name="metodo_return")
		private HttpRequestMethodEnum methodReturn;
		
		@Lob
		private String endPointSend;
		
		@Lob
		private String endPointReturn;
			
		private String schedulerSend;
		private String schedulerReturn;
		
		@Lob
		private String payLoadRequestSend;
		
		@Lob
		private String payLoadResponseSend;
		
		@Lob
		private String payLoadRequestReturn;
		
		@Lob
		private String payLoadResponseReturn;
		
		@JsonBackReference
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "id_integracao_page", nullable = false)
		private IntegracaoPagina integracaoPagina;
		
		public String getDescricaoFuncionalidade() {
			if(funcionalidade != null) {
				return funcionalidade.getDescricao();
			}
			return null;
		}
}
