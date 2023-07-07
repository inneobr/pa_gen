package br.coop.integrada.api.pa.domain.modelDto.rependente;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.model.rependente.RePendente;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteItem;
import lombok.Data;

@Data
public class RePendenteSimplesDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Integer nrDocPes;
	private String placa;
	private Date dtEntrada;
	private String hrEntrada;
	private String produtorCodigo;
	private String produtorNome;
	private Long imovelMatricula;
	private String imovelDescricao;
	private String produtoCodigo;
	private String produtoDescricao;
	private BigDecimal pesoBruto;
	private BigDecimal pesoLiquido;
	private BigDecimal rendaLiquida;
	private String codigoEstabelecimento;
	private String codigoGrupoProduto;
	private Integer nrOrdCampo;
	private Long nrLaudo;
	
	public static RePendenteSimplesDto construir(RePendente obj) {
		var objDto = new RePendenteSimplesDto();
		BeanUtils.copyProperties(obj, objDto);
		
		if(obj.getProdutor() != null) {
			Produtor produtor = obj.getProdutor();
			String produtorNome = produtor.getCooperativa() ? obj.getNomeProd() : produtor.getNome();

			objDto.setProdutorNome(produtorNome);
			objDto.setProdutorCodigo(produtor.getCodProdutor());
		}
		
		if(obj.getImovel() != null) {
			Imovel imovel = obj.getImovel();
			objDto.setImovelMatricula(imovel.getMatricula());
			objDto.setImovelDescricao(imovel.getDescricao());
		}
		
		if(obj.getEstabelecimento() != null) {
			Estabelecimento estabelecimento = obj.getEstabelecimento();
			objDto.setCodigoEstabelecimento(estabelecimento.getCodigo());
		}
		
		if(obj.getGrupoProduto() != null) {
			GrupoProduto grupoProduto = obj.getGrupoProduto();
			objDto.setCodigoGrupoProduto(grupoProduto.getFmCodigo());
		}
		
		if(!CollectionUtils.isEmpty(obj.getItens())) {
			RePendenteItem item = obj.getItens().get(0);
			
			objDto.setRendaLiquida(item.getRendaLiquida());
			
			if(item.getProduto() != null) {
				Produto produto = item.getProduto();
				objDto.setProdutoCodigo(produto.getCodItem());
				objDto.setProdutoDescricao(produto.getDescItem());
			}
		}
		
		return objDto;
	}
	
	public static List<RePendenteSimplesDto> construir(List<RePendente> objs) {
		if(CollectionUtils.isEmpty(objs)) return Collections.emptyList();
		
		return objs.stream().map(rePendente -> {
			return construir(rePendente);
		}).toList();
	}
}
