package br.coop.integrada.api.pa.domain.modelDto.estabelecimento;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class EstabelecimentoAllDto {	
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
    private Boolean ativo;    
    private String telefone;  
    private String email;
    private boolean matriz;	
    
    private Long id;
    private Date dataCadastro;
    private Date dataAtualizacao;
    private Date dataIntegracao;
	
	private String codigoComZero;
    
    public String getCodNome() {
        return codigo + " - "+ nomeFantasia;
    }
    
    public boolean isRegional() {
        return codigo.equals(codigoRegional);
    }

    
	private static EstabelecimentoAllDto contruir(Estabelecimento obj) {
		var objDto = new EstabelecimentoAllDto();
		BeanUtils.copyProperties(obj, objDto);
		return objDto;
	}

	public static List<EstabelecimentoAllDto> construir(List<Estabelecimento> objs) {
		if(CollectionUtils.isEmpty(objs)) return Collections.emptyList();
		return objs.stream().map(estabelecimento -> {
			return contruir(estabelecimento);
		}).toList();
	}
}
	