package br.coop.integrada.api.pa.domain.service.pesagem;

import static br.coop.integrada.api.pa.domain.enums.StatusPesagem.CONCLUIDO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.aplication.utils.DataUtil;
import br.coop.integrada.api.pa.domain.enums.pedidoIntegracao.OperacaoEnum;
import br.coop.integrada.api.pa.domain.enums.pedidoIntegracao.TipoIntegracaoEnum;
import br.coop.integrada.api.pa.domain.model.Pesagem;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.movtoPesagem.MovtoPesagem;
import br.coop.integrada.api.pa.domain.model.pedidoIntegracao.Integracao;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntregaItem;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.CriarMovimentoPesagemDto;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.PesagemFilter;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.PesagemPostDto;
import br.coop.integrada.api.pa.domain.modelDto.pesagem.ValidarPesagemResponse;
import br.coop.integrada.api.pa.domain.modelDto.rependente.CalculoPesoLiquidoDto;
import br.coop.integrada.api.pa.domain.repository.movimentoPesagem.MovtoPesagemRep;
import br.coop.integrada.api.pa.domain.repository.parametros.PesagemRep;
import br.coop.integrada.api.pa.domain.service.estabelecimento.EstabelecimentoService;
import br.coop.integrada.api.pa.domain.service.pedidoIntegracao.PedidoIntegracaoService;
import br.coop.integrada.api.pa.domain.service.recEntrega.RecEntregaService;
import br.coop.integrada.api.pa.domain.service.rependente.RePendenteService;
import lombok.RequiredArgsConstructor;


@Service @RequiredArgsConstructor
public class PesagemService {
	private static final Logger logger = LoggerFactory.getLogger(PesagemService.class);
	
	@Autowired
	private PesagemRep pesagemRep;
	
	@Autowired
	private MovtoPesagemRep movtoPesagemRep;

	@Lazy
	@Autowired
	private RecEntregaService entregaService;
	
	
	@Autowired
	private EstabelecimentoService estabelecimentoService;
	
	@Lazy
	@Autowired
	private RePendenteService rePendenteService;
	
	@Autowired
	private PedidoIntegracaoService integracaoService;

	@Valid
	public Pesagem cadastrar(PesagemPostDto objDto) {
		Pesagem pesagem = Pesagem.construir(objDto);
		return salvar(pesagem);
	}

	public Pesagem atualizar(Pesagem obj) {
		Pesagem pesagem = pesagemRep.findById(obj.getId()).orElse(null);

		if(pesagem == null) {
			throw new ObjectNotFoundException("Não foi encontrado pesagem com o ID " + obj.getId());
		}

		obj.setDataAtualizacao(new Date());
		BeanUtils.copyProperties(obj, pesagem);
		return salvar(obj);
	}

	public Pesagem salvar(Pesagem pesagem){
		return pesagemRep.save(pesagem);
	}
			
	public Pesagem desativarPesagem(Long id) {
		Optional<Pesagem> pesagem  = pesagemRep.findById(id);
		
		if(pesagem.isPresent()) {
			pesagem.get().setDataInativacao(new Date());
			logger.info("Desativando pesagem...");
			return pesagemRep.save(pesagem.get());
		}else {
			return null;
		}
	}
	
	public Pesagem buscarIdPesagem(Long id) {
		Optional<Pesagem> pesagem  = pesagemRep.findById(id);
		logger.info("Buscando id pesagem...");
		return pesagem.get();
	}
	
	public Page<Pesagem> buscarPesagensPendentes(Pageable pageable, PesagemFilter filter) {
		logger.info("Listando as pesagens pendentes...");
		return pesagemRep.buscarPesagensPendentes(pageable, filter);
	}
	
	public Page<Pesagem> buscarTodasPesagens(Pageable pageable, PesagemFilter filter) {
		logger.info("Listando as pesagens...");
		return pesagemRep.buscarPesagens(pageable, filter);
	}

	public Pesagem buscarPorCodigoEstabelecimentoSafraDocPesagem(String codigoEstabelecimento, Integer safra, Integer nroDocPesagem) {
		return pesagemRep.buscarPorCodigoEstabelecimentoSafraDocPesagem(codigoEstabelecimento, safra, nroDocPesagem).orElse(null);
	}
	
	public ValidarPesagemResponse validaPesagem(String codigoEstabelecimento, Integer safra, Integer nroDocPesagem) {
		ValidarPesagemResponse pesagemResponse = new ValidarPesagemResponse();
		Optional<Pesagem> pesagem = pesagemRep.buscarPorCodigoEstabelecimentoSafraDocPesagem(codigoEstabelecimento, safra, nroDocPesagem);
		if(pesagem == null) {
			pesagemResponse.setStatus(false);
			pesagemResponse.setMensagem("Não foi encontrado ticket de pesagem com os parâmetros informados. (Código Estabelecimento: " + codigoEstabelecimento +", Safra: " + safra + " e Nro Doc Pesagem: " + nroDocPesagem + ")");
			pesagemResponse.setHoraSaída(DataUtil.horaString(new Date()));
			return pesagemResponse;
		}
		
		Pesagem pesagemDto = new Pesagem();
		BeanUtils.copyProperties(pesagem, pesagemDto);
		
		if(pesagemDto.getSituacao() != null && pesagemDto.getSituacao().equalsIgnoreCase("C")) {
			pesagemResponse.setStatus(false);
			pesagemResponse.setMensagem("Ticket de pesagem nro " + nroDocPesagem + " foi cancelado.");
			pesagemResponse.setHoraSaída(pesagemDto.getHoraSaida());
			return pesagemResponse;
		}
		
		if(pesagemDto.getStatus() != null && pesagemDto.getStatus().equals(CONCLUIDO)) {
			pesagemResponse.setStatus(false);
			pesagemResponse.setMensagem("Não é possível prosseguir, o ticket está concluído. Favor verificar se a entrada para esse ticket já foi efetuada.");
			pesagemResponse.setHoraSaída(pesagemDto.getHoraSaida());
			return pesagemResponse;
		}
		pesagemResponse.setStatus(true);
		pesagemResponse.setMensagem("");
		pesagemResponse.setHoraSaída(pesagemDto.getHoraSaida());
		return pesagemResponse;
	}	

	
	public Pesagem buscarPesagemEstabelecimentoSafraNrDocumento(String codigoEstabelecimento, Integer safra, Integer nroDocPesagem) {
		Pesagem pesagemAtual = pesagemRep.buscarPorCodigoEstabelecimentoSafraDocPesagem(codigoEstabelecimento, safra, nroDocPesagem)
				.orElseThrow(() -> new ObjectNotFoundException("Não foi encontrado ticket de pesagem com os parâmetros informados. (Código Estabelecimento: " + codigoEstabelecimento +", Safra: " + safra + " e Nro Doc Pesagem: " + nroDocPesagem + ")"));
		
		Pesagem pesagem = new Pesagem();
		BeanUtils.copyProperties(pesagemAtual, pesagem);
		
		if(pesagem.getSituacao() != null && pesagem.getSituacao().equalsIgnoreCase("C")) {
			throw new ObjectNotFoundException("Ticket de pesagem nro " + nroDocPesagem + " foi cancelado.");
		}
		
		if(pesagem.getStatus() != null && pesagem.getStatus().equals(CONCLUIDO)) {
			throw new ObjectNotFoundException("Não é possível prosseguir, o ticket está concluído. Favor verificar se a entrada para esse ticket já foi efetuada.");
		}
		
		return pesagem;
	}
	
	public MovtoPesagem criarMovimentoPesagem(CriarMovimentoPesagemDto input, RecEntrega recEntrega) {		
		//RecEntrega recEntrega = entregaService.buscarRe(input.getCodEstabel(), input.getNrRe()); 
		
		MovtoPesagem movimentoPesagem = new MovtoPesagem();
		
		Estabelecimento estabelecimento = estabelecimentoService.buscarPorCodigo(input.getCodEstabel());
		
		movimentoPesagem.setEstabelecimento( estabelecimento );
		movimentoPesagem.setSafra( recEntrega.getSafra() );
		movimentoPesagem.setNroDocPesagem( recEntrega.getNrDocPes() );
		movimentoPesagem.setDtMovto( new Date() ); 
		movimentoPesagem.setSerieDocto( input.getSerie() );
		movimentoPesagem.setNroDocto( input.getNroNota() );		
		movimentoPesagem.setCodEmitente(recEntrega.getCodEmitente());
		movimentoPesagem.setNatOperacao( input.getNatOperacao() );		
		movimentoPesagem.setRendaMovto(input.getQuantidade());
		movimentoPesagem.setOperacao( input.getOperacao().getDescricao() );
		if(input.getOperacao().getEntSai().equals("ENT")) {
			//Se Remessa para armazenagem ENT movimento
			movimentoPesagem.setMovto("Movimento");
		}
		
		movimentoPesagem.setEstornado( input.getEstornado() );
		movimentoPesagem.setNrRe( recEntrega.getNrRe() );
				
		RecEntregaItem item = recEntrega.getItens().get(0);
		
		//Calcular Peso Líquido
		CalculoPesoLiquidoDto calculoPesoLiquidoInput = new CalculoPesoLiquidoDto();
		
		calculoPesoLiquidoInput.setTipoTbm(false);
		calculoPesoLiquidoInput.setRendaLiquida( item.getRendaLiquida() );
		calculoPesoLiquidoInput.setPercDescImpureza(item.getPerDescImpur());
		calculoPesoLiquidoInput.setPercDescUmidade(item.getPerDescUmid());
		calculoPesoLiquidoInput.setPercDescChvadoAvariado(item.getPerDescChuv());
		calculoPesoLiquidoInput.setQtdTbm(BigDecimal.ZERO);
		calculoPesoLiquidoInput.setQtdRecepcao(BigDecimal.ZERO);
		calculoPesoLiquidoInput.setQtdSecagem(BigDecimal.ZERO);
		calculoPesoLiquidoInput.setPercTbm(BigDecimal.ZERO);
		
		BigDecimal pesoLiquido = rePendenteService.calcularPesoLiquidoAtual(calculoPesoLiquidoInput);
				
		movimentoPesagem.setPesoMovto(pesoLiquido);
				
		
		movimentoPesagem.setNroMovto( 0 ); //Mirian vai verificar como preencher este campo corretamente.
				
		movimentoPesagem.setIdMovtoPesagem("0");
				
		MovtoPesagem movimentoPesagemAux = movtoPesagemRep.save(movimentoPesagem);

		//gera id para controle no formato GEN+nro-sequencial
		movimentoPesagemAux.setIdMovtoPesagem("GEN"+movimentoPesagemAux.getId());
		
		movimentoPesagemAux = movtoPesagemRep.save(movimentoPesagemAux);
		
		//Gerar Pedido de Integração 
		Integracao integracao = new Integracao();
		integracao.setTipoIntegracao(TipoIntegracaoEnum.TABELACOMPLETA);
		integracao.setTableName("movto_pesagem");
		integracao.setOperacao(OperacaoEnum.CREATE);
		integracao.setIdReg1(movimentoPesagemAux.getIdMovtoPesagem());
		integracaoService.gerarPedidoIntegracao(integracao);
				
		return movimentoPesagemAux;
	}	
	
}
