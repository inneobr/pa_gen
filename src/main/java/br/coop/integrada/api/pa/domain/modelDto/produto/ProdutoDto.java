package br.coop.integrada.api.pa.domain.modelDto.produto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.model.produto.VinculoProdutoReferencia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProdutoDto{
	
	private String codItem;	
	private String descItem;
	private String fmCodigo;
	private String codItemAfCoop;
	private String descItemAfCoop;
	private String codItemAfTerc;
	private String descItemAfTerc;
	private String codItemFixaCoop;
	private String descItemFixaCoop;
	private String codItemFicaTerc;
	private String descItemFicaTerc;
	private String descReferencia;
	private Boolean ativo;
	private Integer idErp;
    private List<ProdutoReferenciaIntegrationDto> referencias = new ArrayList<>();
    

	public static Produto convertDto(ProdutoDto objDto) {
		var obj = new Produto();
		BeanUtils.copyProperties(objDto, obj);
		
		if(objDto.getAtivo() != null && !objDto.getAtivo()) {
			obj.setDataInativacao(new Date());
		}

		return obj;
	}
	
	public static ProdutoDto convertProdutoToDto(Produto produto) {
		var objDto = new ProdutoDto();
		BeanUtils.copyProperties(produto, objDto);

		objDto.setFmCodigo(produto.getGrupoProduto() != null? produto.getGrupoProduto().getFmCodigo(): null);
		
		if(produto.getReferencias() != null) {
			
			for(VinculoProdutoReferencia vProdReferencia : produto.getReferencias() ) {
				ProdutoReferenciaIntegrationDto objReferencia = new ProdutoReferenciaIntegrationDto();
				
				//BeanUtils.copyProperties(vProdReferencia, objReferencia);
				objReferencia.setCodRef(vProdReferencia.getProdutoReferencia().getCodRef());
				objReferencia.setId(vProdReferencia.getProdutoReferencia().getId());
								
				objDto.getReferencias().add(objReferencia);
			}
		}
		
		
		return objDto;
	}

	public static ProdutoDto construir(Produto obj) {
		var objDto = new ProdutoDto();
		BeanUtils.copyProperties(obj, objDto);
		return objDto;
	}
}