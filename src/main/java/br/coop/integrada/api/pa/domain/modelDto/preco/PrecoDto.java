package br.coop.integrada.api.pa.domain.modelDto.preco;

import br.coop.integrada.api.pa.domain.model.preco.Preco;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class PrecoDto {

	private String idUnico;
	private String codigoEstabelecimento;
	private String codigoProduto;
	private String descricaoProduto;
	private String codigoReferencia;
	private BigDecimal precoFiscal;
	private BigDecimal precoFechamento;
	private BigDecimal precoFechamentoCoco;	

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataValidade;
	
	private String horaValidade;
	private String dataInativacao;
	private Boolean ativo;

	/*
	public void setDescricaoProduto(Produto produto){
		if (!Objects.isNull(produto)) this.descricaoProduto = produto.getDescItem();
	}*/

	public static PrecoDto construir(Preco obj) {
		var objDto = new PrecoDto();
		BeanUtils.copyProperties(obj, objDto);

		return objDto;
	}

	public static List<PrecoDto> construir(List<Preco> objs) {
		if(objs == null) return new ArrayList<>();

		return objs.stream().map(preco -> {
			return construir(preco);
		}).toList();
	}

	public static Preco converterDto(PrecoDto objDto) {
		var obj = new Preco();
		BeanUtils.copyProperties(objDto, obj);
		
		return obj;
	}
}
