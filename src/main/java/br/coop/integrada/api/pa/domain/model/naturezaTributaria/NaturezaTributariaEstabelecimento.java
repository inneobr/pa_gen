package br.coop.integrada.api.pa.domain.model.naturezaTributaria;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;

import javax.persistence.JoinColumn;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoNomeCodigoDto;

@Getter
@Setter
@Entity
@Table(name = "natureza_tributaria_estabelecimento")
public class NaturezaTributariaEstabelecimento extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@Column(name="id_registro")
	private String idRegistro;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name = "id_natureza_tributaria", nullable = false)
	private NaturezaTributaria naturezaTributaria;

	@ManyToOne
	@JoinColumn(name = "id_estabelecimento", nullable = false)
	private Estabelecimento estabelecimento;
	
	public static NaturezaTributariaEstabelecimento builder(NaturezaTributaria naturezaTributaria, EstabelecimentoNomeCodigoDto estabelecimentoDto, StatusIntegracao statusIntegracao) {
		NaturezaTributariaEstabelecimento naturezaTributariaEstabelecimento = new NaturezaTributariaEstabelecimento();
		Estabelecimento estabelecimento = new Estabelecimento();
		BeanUtils.copyProperties(estabelecimentoDto, estabelecimento);
		naturezaTributariaEstabelecimento.setNaturezaTributaria(naturezaTributaria);
		naturezaTributariaEstabelecimento.setEstabelecimento(estabelecimento);
		naturezaTributariaEstabelecimento.setStatusIntegracao(statusIntegracao);
		naturezaTributariaEstabelecimento.setDataIntegracao(null);
		return naturezaTributariaEstabelecimento;
	}
}
