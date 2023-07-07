package br.coop.integrada.api.pa.domain.service.rependente;

import static br.coop.integrada.api.pa.domain.enums.re.pendente.TipoDesmembramentoEnum.PESO_LIQUIDO;
import static br.coop.integrada.api.pa.domain.enums.re.pendente.TipoDesmembramentoEnum.RENDA_LIQUIDA;
import static br.coop.integrada.api.pa.domain.enums.re.pendente.UnidadeDesmembramento.PERCENTUAL;
import static br.coop.integrada.api.pa.domain.enums.re.pendente.UnidadeDesmembramento.QUILOS;
import static br.coop.integrada.api.pa.domain.enums.re.pendente.UnidadeDesmembramento.SACAS;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.domain.enums.re.pendente.TipoDesmembramentoEnum;
import br.coop.integrada.api.pa.domain.enums.re.pendente.UnidadeDesmembramento;
import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.TipoGmo;
import br.coop.integrada.api.pa.domain.model.rependente.RePendente;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteDesmembramento;
import br.coop.integrada.api.pa.domain.modelDto.parametros.ValidaEntradaCooperativaDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.ValidaEntradaCooperativaResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.desmembramento.RePendenteDesmembramentoDto;
import br.coop.integrada.api.pa.domain.modelDto.rependente.item.RePendenteItemDto;
import br.coop.integrada.api.pa.domain.repository.rependente.RePendenteDesmembramentoRep;
import br.coop.integrada.api.pa.domain.service.ProdutorService;
import br.coop.integrada.api.pa.domain.service.imovel.ImovelService;
import br.coop.integrada.api.pa.domain.service.parametroEstabelecimento.ParametroEstabelecimentoService;
import br.coop.integrada.api.pa.domain.service.produto.TipoGmoService;

@Service
public class RePendenteDesmembramentoService {
	
	@Autowired
	private RePendenteDesmembramentoRep desmembramentoRep;
	
	@Autowired
	private ProdutorService produtorService;
	
	@Autowired
	private TipoGmoService tipoGmoService;
	
	@Autowired
	private ParametroEstabelecimentoService parametroEstabelecimentoService;
	
	@Autowired
	private ImovelService imovelService;
	
	public List<RePendenteDesmembramento> converterDto(RePendente rePendente, List<RePendenteDesmembramentoDto> rePendenteDesmembramentoDtos) {
		if(CollectionUtils.isEmpty(rePendenteDesmembramentoDtos)) return Collections.emptyList();
		return rePendenteDesmembramentoDtos.stream().map(rePendenteDesmembramentoDto -> {
			return converterDto(rePendente, rePendenteDesmembramentoDto);
		}).toList();
	}
	
	public RePendenteDesmembramento converterDto(RePendente rePendente, RePendenteDesmembramentoDto rePendenteDesmembramentoDto) {
		var obj = new RePendenteDesmembramento();
		
		BeanUtils.copyProperties(rePendenteDesmembramentoDto, obj);
		
		Produtor produtor = produtorService.buscarPorCodigoProdutor(rePendenteDesmembramentoDto.getProdutorFavorecidoCodigo());
		obj.setProdutorFavorecido(produtor);
		
		Imovel imovel = imovelService.buscarImovelAtivoPorMatriculaImovelECodigoProdutor(
				rePendenteDesmembramentoDto.getImovelMatricula(),rePendenteDesmembramentoDto.getProdutorFavorecidoCodigo());
		obj.setImovel(imovel);
		obj.setRePendente(rePendente);
		
		return obj;
	}

	public void validarDesmembramentos(RePendente rePendente, List<RePendenteItemDto> itemDtos, List<RePendenteDesmembramentoDto> desmembramentoDtos) {
		if(CollectionUtils.isEmpty(desmembramentoDtos) || CollectionUtils.isEmpty(itemDtos)) return;
		
		String codigoEstabelecimento = rePendente.getEstabelecimento().getCodigo();
		RePendenteItemDto itemDto = itemDtos.get(0);
		
		if(!itemDto.getDesmembramento()) return;
		
		UnidadeDesmembramento unidadeDesmembramento = itemDto.getUnidadeDesmembramento();
		TipoDesmembramentoEnum tipoDesmebramento = itemDto.getTipoDesmembramento();
		
		TipoGmo tipoGmo = null;
		if(rePendente.getTipoGmo() != null) {
			tipoGmo = tipoGmoService.buscarPorTipoGmo(rePendente.getTipoGmo().getTipoGmo());
		}
		
		Boolean isValorKitZerado = tipoGmo != null && tipoGmo.getVlKit() != null && tipoGmo.getVlKit().compareTo(BigDecimal.ZERO) != 0;
		BigDecimal totalizador = BigDecimal.ZERO;
		
		
		Boolean existeResponsavelPagaKit = false;
		
		for(RePendenteDesmembramentoDto desmembramentoDto: desmembramentoDtos) {
			String codigoProdutor = desmembramentoDto.getProdutorFavorecidoCodigo();
			Produtor produtor = produtorService.buscarPorCodigoProdutor(codigoProdutor);
			
			validarProdutorFavorecido(produtor, desmembramentoDto.getProdutorFavorecidoNome());
			
			if(produtor.getCooperativa()) {
				validarEntradaCooperativa(rePendente.getEstabelecimento().getCodigo(), desmembramentoDto.getImovelMatricula(), desmembramentoDto.getProdutorFavorecidoCodigo());
			}
			
			//Validação do Imóvel, utilizar a API de Validação de Imóvel, para passar por  todas aquelas validações
			imovelService.validarImovelEntrada(desmembramentoDto.getImovelMatricula(), desmembramentoDto.getProdutorFavorecidoCodigo(), codigoEstabelecimento);
			
			BigDecimal valorDesmembramento = validarValorDesmembramento(unidadeDesmembramento, desmembramentoDto);
			totalizador = totalizador.add(valorDesmembramento);
			
			if(isValorKitZerado && desmembramentoDto.getCobraKit()) {
				existeResponsavelPagaKit = true;
			}
		}
		
		
		if(isValorKitZerado) {
			//Caso nenhum favorecido está checado que paga o kit mostra a mensagem
			if(!existeResponsavelPagaKit) {
				throw new ObjectDefaultException("Pelo menos uma pessoa do desmembramento deve pagar o KIT GMO");
			}
		}
		
		if(unidadeDesmembramento.equals(PERCENTUAL)) {
			BigDecimal cemPorcento = new BigDecimal(100);
			if(totalizador.compareTo(cemPorcento) != 0) {
				throw new ObjectDefaultException("O total do desmembramento deve fechar em 100%");
			}
		}
		else if(unidadeDesmembramento.equals(QUILOS)){			
			if(tipoDesmebramento.equals(RENDA_LIQUIDA) && totalizador.compareTo(itemDto.getRendaLiquida()) != 0) {
				throw new ObjectDefaultException("O total do desmembramento deve bater com o valor do total da renda liquida");
			}
			else if(tipoDesmebramento.equals(PESO_LIQUIDO) && totalizador.compareTo(rePendente.getPesoLiquido()) != 0) {
				throw new ObjectDefaultException("O total do desmembramento deve bater com o valor do total do peso líquido");
			}
		}
		else if(unidadeDesmembramento.equals(SACAS)) {
			//Recuperar o grupo do produto para ver o peso da saca
			GrupoProduto grupoProduto = rePendente.getGrupoProduto();
			
			BigDecimal quiloSaca = BigDecimal.ZERO;
			if(grupoProduto.getKgSc() != null && grupoProduto.getKgSc() > 0) {
				quiloSaca = new BigDecimal(grupoProduto.getKgSc());
			}
			else {
				throw new ObjectDefaultException("O Parâmetro Kg/Saca do Grupo de Produto não foi parametrizado.");
			}
			
			//Verifica o Totalizador
			BigDecimal totalSacas = BigDecimal.ZERO;
			
			if(tipoDesmebramento.equals(PESO_LIQUIDO)) {
				totalSacas = rePendente.getPesoLiquido().divide(quiloSaca, 4, RoundingMode.HALF_EVEN);
			}
			else if(tipoDesmebramento.equals(RENDA_LIQUIDA)) {
				totalSacas = itemDto.getRendaLiquida().divide(quiloSaca, 4,RoundingMode.HALF_EVEN);
			}
			
			if(totalSacas.compareTo(totalizador) != 0) {
				throw new ObjectDefaultException("A soma do total de sacas desmembrado não é igual com o total de sacas entregue.");
			}
		}
	}
	
	private BigDecimal validarValorDesmembramento(UnidadeDesmembramento unidadeDesmembramento, RePendenteDesmembramentoDto desmembramentoDto) {
		if(unidadeDesmembramento.equals(PERCENTUAL)) {
			//Se o desmembramento for em percentual, e o percentual informado for menor ou igual a 1%,
			if(desmembramentoDto.getPercentual().compareTo(BigDecimal.ONE) <= 0) {
				throw new ObjectDefaultException("Percentual inválido para desmembramento, não é possível desmembrar menos que 1,1%");
			}
			
			return desmembramentoDto.getPercentual();
		}
		else if(unidadeDesmembramento.equals(QUILOS)) {
			//Se o desmembramento for em quilos, e a quantidade for menor ou igual a 5
			if(desmembramentoDto.getQuantidadeQuilos().compareTo(new BigDecimal(5)) == -1) {
				throw new ObjectDefaultException("Não é possível desmembrar menos que 5 quilos");
			}
			
			return desmembramentoDto.getQuantidadeQuilos();
		}
		else if(unidadeDesmembramento.equals(SACAS)) {
			//Se o desmembramento é em sacas, não é possível desmembrar menos que 1
			if(desmembramentoDto.getQuantidadeSacas().compareTo(BigDecimal.ONE) == -1) {
				throw new ObjectDefaultException("Não é possível desmembrar menos que 1 saca");
			}
			
			return desmembramentoDto.getQuantidadeSacas();
		}
		
		return BigDecimal.ZERO;
	}
	
	private void validarProdutorFavorecido(Produtor produtor, String produtorFavorecidoNome) {
		if(produtor.getBloqueado() != null && produtor.getBloqueado()) {
			throw new ObjectDefaultException("Produtor bloqueado, não é possível movimentá-lo");
		}
		
		if(!produtor.getAtivo()) {
			throw new ObjectDefaultException("Produtor não está ativo, não é possível movimentá-lo");
		}
		
		if(Strings.isEmpty(produtor.getCodRegional())) {
			throw new ObjectDefaultException("Regional do produtor está em branco, não é possível prosseguir");
		}
		
		if(produtor.getNatureza() == null || (produtor.getNatureza().toUpperCase().equals("PJ") && produtor.getEmiteNota())) {
			throw new ObjectDefaultException("Não é possível desmembrar para Pessoa Jurídica que emite nota fiscal.");
		}
		
		if(produtor.getCooperativa() != null && produtor.getCooperativa() && Strings.isEmpty(produtorFavorecidoNome)) {
			throw new ObjectDefaultException("Nome do produtor favorecido deve ser infomado");
		}
	}
	
	private void validarEntradaCooperativa(String codigoEstabelecimento, Long matriculaImovel, String codigoProdutorFavorecido) {
		//------------------------------------
		//
		//  Validar entrada nome cooperativa
		//
		//-------------------------------------
		
		ValidaEntradaCooperativaDto entradaCooperativaInput = new ValidaEntradaCooperativaDto();
		
		entradaCooperativaInput.setCodEstabelecimento(codigoEstabelecimento); 
		entradaCooperativaInput.setCodImovel(matriculaImovel);
		entradaCooperativaInput.setCodProdutor(codigoProdutorFavorecido);
		ValidaEntradaCooperativaResponseDto valEntCoopResponse = parametroEstabelecimentoService.validarEntradaNomeCooperativa(entradaCooperativaInput);
		
		if(!valEntCoopResponse.getPermitido()) {
			throw new ObjectDefaultException(valEntCoopResponse.getMensagemPermitido());
		}
		
		if(valEntCoopResponse.getCodProd().equals("Diferente")) {
			throw new ObjectDefaultException(valEntCoopResponse.getMensagemCodProd());
		}
		
		if(valEntCoopResponse.getCodImovel().equals("Diferente")) {
			throw new ObjectDefaultException(valEntCoopResponse.getMensagemCodImovel());
		}
	}
}
