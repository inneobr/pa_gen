package br.coop.integrada.api.pa.domain.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.coop.integrada.api.pa.domain.enums.PaginaAreaEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "informacoes_recurso_sistema")
public class InformacoesRecursoSistema extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "Campo {paginaArea} obrigatório")
	@Enumerated(EnumType.STRING)
	@Column(name="pagina_area", nullable = false, unique = true)
	private PaginaAreaEnum paginaArea;
	
	@NotBlank(message = "Campo {tituloModal} obrigatório")
	@Column(name = "titulo_modal")
	private String tituloModal;
	
	@NotBlank(message = "Campo {conteudo} obrigatório")
	@Column(name = "conteudo", nullable = false, columnDefinition = "CLOB")
	private String conteudo;
	
	public String getArea(){
		return this.paginaArea.getArea();
	}
	
	public String getPagina()
	{
		return this.paginaArea.getPagina().getDescricao();
	}
	
	public String getPaginaName()
	{
		return this.paginaArea.getPagina().name();
	}
	
		
}
