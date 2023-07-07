package br.coop.integrada.api.pa.domain.modelDto.estabelecimento;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class EstabelecimentoDto {
	private String codigo;
	private String codigoRegional;
	private String razaoSocial;
	private String nomeFantasia;
	private String endereco;
	private String bairro;
    private String cidade;
    private String estado;
    private String cnpj;
    private String inscricaoEstadual;    
    private String telefone;
    private String email;
    private Boolean matriz;
    private Boolean ativo;
    
    public String getCodNome() {
		return codigo + " - " + nomeFantasia;
		
	}

    
	public static EstabelecimentoDto construir(Estabelecimento obj) {
		var objDto = new EstabelecimentoDto();
		BeanUtils.copyProperties(obj, objDto);
		return objDto;
	}

	public static List<EstabelecimentoDto> construir(List<Estabelecimento> objs) {
		if(CollectionUtils.isEmpty(objs)) return Collections.emptyList();
		return objs.stream().map(estabelecimento -> {
			return construir(estabelecimento);
		}).toList();
	}
}
	