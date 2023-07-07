package br.coop.integrada.api.pa.domain.model.produto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoGmoDto;

@Getter @Setter
@Entity @NoArgsConstructor @AllArgsConstructor
@Table(name = "tipo_gmo",
    uniqueConstraints = {
        @UniqueConstraint(name = "KEY_CODIGO_UNICO", columnNames = { "cod_unico" })
    })
public class TipoGmo extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "Campo {idUnico} obrigatório")
	@Column(name = "cod_unico", unique=true)
	private String idUnico;
	
	@NotBlank(message = "Campo {tipoGmo} obrigatório")
	@Column(name = "tipo_gmo")
	private String tipoGmo;
	
	@Size(max = 1000)   
	@Column(name = "obs_romaneio", nullable = true, columnDefinition = "CLOB")
	private String obsRomaneio;
	
	@NotNull(message = "Campo {perDeclarada} obrigatório")
	@Column(name = "per_declarada")
	private BigDecimal perDeclarada;
	
	@NotNull(message = "Campo {perTestada} obrigatório")
	@Column(name = "per_testada")
	private BigDecimal perTestada;
	
	@NotNull(message = "Campo {vlKit} obrigatório")
	@Column(name = "val_kit")
	private BigDecimal vlKit;	
	
	@JsonIgnore
	@OneToMany(mappedBy = "tipoGmo", fetch = FetchType.LAZY)
	private List<GrupoProdutoGmo> grupoProdutoGmoList;
	
	public static TipoGmo contruir(TipoGmoDto tipoGmoDto) {
		TipoGmo tipoGmo = new TipoGmo();
		BeanUtils.copyProperties(tipoGmoDto, tipoGmo);
		
		if(tipoGmoDto.getAtivo() != null && tipoGmoDto.getAtivo().equals(false)) {
			tipoGmo.setDataInativacao(new Date());
		}else {
			tipoGmo.setDataInativacao(null);
		}
		
		return tipoGmo;
	}
}
