package br.coop.integrada.api.pa.domain.service.recEntrega;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntregaItem;
import br.coop.integrada.api.pa.domain.modelDto.recEntrega.RecEntregaItemDto;
import br.coop.integrada.api.pa.domain.repository.EstabelecimentoRep;
import br.coop.integrada.api.pa.domain.repository.produto.ProdutoRep;
import br.coop.integrada.api.pa.domain.repository.recEntrega.RecEntregaItemRep;


@Service
public class RecEntregaItemService {
	
	@Autowired
	private RecEntregaItemRep recEntregaItemRep;
	
	@Autowired
	private EstabelecimentoRep estabelecimentoRep;
	
	@Autowired
	private ProdutoRep produtoRep;
	
	public RecEntregaItem converterDto(RecEntrega recEntrega, RecEntregaItemDto recEntregaItemDto) {
		RecEntregaItem obj = null;
		
		if(recEntrega.getId() != null) {
			obj = recEntregaItemRep.findByRecEntregaAndNrReAndItCodigoAndCodRefer(recEntrega, recEntregaItemDto.getNrRe(), recEntregaItemDto.getItCodigo(), recEntregaItemDto.getCodRefer()).orElse(null);
		}
		
		if(obj == null) {
			obj = new RecEntregaItem();
		}
		
		BeanUtils.copyProperties(recEntregaItemDto, obj);
		
		return obj;
	}
	
	public List<RecEntregaItem> converterDto(RecEntrega recEntrega, List<RecEntregaItemDto> recEntregaItensDtos) {
		if(CollectionUtils.isEmpty(recEntregaItensDtos)) return Collections.emptyList();
		
		return recEntregaItensDtos.stream().map(recEntradaItemDto -> { return converterDto(recEntrega, recEntradaItemDto);	}).toList();
	}
}
