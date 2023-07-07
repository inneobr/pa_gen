package br.coop.integrada.api.pa.domain.modelDto.produtorFilter;

import br.coop.integrada.api.pa.domain.model.Produtor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
public class ProdutorFilterResponse {
	private Long id;
	private String codProdutor;
	private String natureza;
	private String cpfCnpj;
	private String nome;
	private String nomeAbreviado;
	private String tipoProdutor;
	private String telefone;
	private String email;
	private Boolean emiteNota;
	private String codRegional;
	private String codRepres;
	private String classificacaoFinanceira;
	private Date dataDemissao;
	private Date dataNascimento;
	private String numPessoaFisica;
	private String sexo;
	private Boolean ativo;
	private Boolean bloqueado;
	private String codTransp;
	private String nomeTransp;
	private Boolean participanteBayer;
	private Boolean produtorDap;
	private Boolean cooperativa;
	private String endereco;
	private String bairro;
	private String cidade;
	private String estado;
	private String estadoCivil;
	private String nacionalidade;
	private String RgIe;
	private Date dataIntegracao;
	
	public String getCodigoAndNome() {
		return codProdutor + " - " + nome;		
	}
	
	public static ProdutorFilterResponse construir(Produtor produtor) {
		var objDto = new ProdutorFilterResponse();
		BeanUtils.copyProperties(produtor, objDto);
		return objDto;
	}

	public static List<ProdutorFilterResponse> construir(List<Produtor> produtores) {
		if(CollectionUtils.isEmpty(produtores)) return Collections.emptyList();
		return produtores.stream().map(produtor -> {
			return construir(produtor);
		}).toList();
	}
}
