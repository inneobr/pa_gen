package br.coop.integrada.api.pa.domain.service.rependente;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import br.coop.integrada.api.pa.domain.enums.NaturezaEnum;
import br.coop.integrada.api.pa.domain.enums.Operacao;
import br.coop.integrada.api.pa.domain.enums.OperacaoProdutoEnum;
import br.coop.integrada.api.pa.domain.enums.StatusPesagem;
import br.coop.integrada.api.pa.domain.enums.gerarRe.TipoEntradaEnum;
import br.coop.integrada.api.pa.domain.model.Pesagem;
import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.model.baixaCredito.BaixaCredito;
import br.coop.integrada.api.pa.domain.model.baixaCredito.BaixaCreditoMov;
import br.coop.integrada.api.pa.domain.model.imovel.ImovelProdutor;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntregaItem;
import br.coop.integrada.api.pa.domain.modelDto.movimentoRe.MovimentoReDto;
import br.coop.integrada.api.pa.domain.modelDto.natureza.NaturezaResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.produtor.CadProResponse;
import br.coop.integrada.api.pa.domain.modelDto.produtor.VerificaProdutorResponseDto;
import br.coop.integrada.api.pa.domain.repository.ImovelProdutorRep;
import br.coop.integrada.api.pa.domain.repository.recEntrega.RecEntregaRep;
import br.coop.integrada.api.pa.domain.service.ProdutorService;
import br.coop.integrada.api.pa.domain.service.UsuarioService;
import br.coop.integrada.api.pa.domain.service.baixaCredito.BaixaCreditoMovService;
import br.coop.integrada.api.pa.domain.service.baixaCredito.BaixaCreditoService;
import br.coop.integrada.api.pa.domain.service.imovel.ImovelProdutorService;
import br.coop.integrada.api.pa.domain.service.imovel.ImovelService;
import br.coop.integrada.api.pa.domain.service.movimentoRe.MovimentoReService;
import br.coop.integrada.api.pa.domain.service.natureza.NaturezaService;
import br.coop.integrada.api.pa.domain.service.pesagem.PesagemService;
import br.coop.integrada.api.pa.domain.service.produto.ProdutoService;
import br.coop.integrada.api.pa.domain.service.recEntrega.RecEntregaService;

@Service
public class GerarReService {
	
	@Autowired
	private ImovelService imovelService;
	
	@Autowired
	private PesagemService pesagemService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ProdutorService produtorService;
	
	@Autowired
	private NaturezaService naturezaService;
	
	@Autowired
	private ImovelProdutorService imovelProdutorService;
	
	@Autowired
	private MovimentoReService movimentoReService;
	
	@Autowired
	private BaixaCreditoService baixaCreditoService;
	
	@Autowired
	private BaixaCreditoMovService baixaCreditoMovService;
	
	@Lazy
	@Autowired
	private RecEntregaService recEntregaService;
	
	@Autowired
	private RecEntregaRep recEntregaRep;
	
	@Autowired
	private UsuarioService usuarioService;

	@SuppressWarnings("unlikely-arg-type")
	@Transactional
	public List<RecEntrega> gerarRe(TipoEntradaEnum tipoEntradaEnum, List<RecEntrega> recEntregaLista)
			throws CloneNotSupportedException {

		HashMap<Long, Pesagem> mapPesagens = new HashMap<>();
		for (RecEntrega recEntrega : recEntregaLista) {
			CadProResponse cadpro = imovelService.getCadProProdutorAndImovel(recEntrega.getCodEmitente(), recEntrega.getMatricula());
			
//			recEntrega.setItens(new ArrayList<>());			
			recEntrega.setNrRe(0L);
			recEntrega.setLogTemSaldo(true);
			recEntrega.setImpresso(false);
			recEntrega.setOrigemRe("Genesis");
			recEntrega.setLogNfe(false);
			recEntrega.setNrReOrig(recEntrega.getNrReOrig());
			recEntrega.setNrRe(null);
			recEntrega.setSafra(getSafra(recEntrega.getDtEntrada()));
			recEntrega.setNrNotaFis(recEntrega.getNrNotaFis());
			recEntrega.setNrCadPro(cadpro.getCadpro());
			
			VerificaProdutorResponseDto caracteristicasProdutor = produtorService.verificarProdutorPorCodigo(recEntrega.getCodEmitente());
			recEntrega.setCdnRepres(caracteristicasProdutor.getCodRepres());
			recEntrega.setCodRegional(caracteristicasProdutor.getCodRegional());


			if (caracteristicasProdutor.getNatureza().equalsIgnoreCase("PJ")
					&& caracteristicasProdutor.getEmiteNotaPropria()) {
				recEntrega.setNatureza(NaturezaEnum.PJ_NF);
			}
			else if (caracteristicasProdutor.getNatureza().equalsIgnoreCase("PJ")
						&& !caracteristicasProdutor.getEmiteNotaPropria()) {
				recEntrega.setNatureza(NaturezaEnum.PJ);
			}
			else {
				recEntrega.setNatureza(NaturezaEnum.PF);
			}
			
			if (caracteristicasProdutor.getCooperativa()) {
				recEntrega.setNatureza(NaturezaEnum.COOP);
			}

			if (recEntrega.getNatureza().equals(NaturezaEnum.PJ_NF)) {
				NaturezaResponseDto natureza = naturezaService.buscarNaturezaPor(recEntrega.getCodEstabel(),
						Operacao.ENTRADA, recEntrega.getCodEmitente(), recEntrega.getFmCodigo(), "");
				recEntrega.setNatDocum(natureza.getNaturezaOperacao());
				recEntrega.setNroDocum("");
				
				RecEntregaItem recEntregaItem = recEntrega.getItens().get(0);
				recEntrega.setPjQtEntrada(recEntregaItem.getRendaLiquida().intValue());
			}			

			recEntrega = recEntregaRep.save(recEntrega);
			
			if (recEntrega.getNrReOrig() > 0) {
				if (recEntrega.getNrReOrig() == recEntrega.getSequencia()) {
					recEntrega.setNrReOrig(recEntrega.getId());
				}
				else if (recEntrega.getNrReOrig() < recEntrega.getSequencia()
						|| recEntrega.getNrReOrig() > recEntrega.getSequencia()) {
					for (RecEntrega item : recEntregaLista) {
						if (item.getSequencia() == recEntrega.getNrReOrig()) {
							recEntrega.setNrReOrig(item.getId());
						}
					}
				}
			}

			if (recEntrega.getNrRePai() > 0) {
				if (recEntrega.getNrRePai() == recEntrega.getSequencia()) {
					recEntrega.setNrRePai(recEntrega.getId());
				}
				else if (recEntrega.getNrRePai() < recEntrega.getSequencia()
						|| recEntrega.getNrRePai() > recEntrega.getSequencia()) {
					for (RecEntrega item : recEntregaLista) {
						if (item.getSequencia() == recEntrega.getNrRePai()) {
							recEntrega.setNrRePai(item.getId());
						}
					}
				}
			}

			if (recEntrega.getNrPriEnt() > 0) {
				if (recEntrega.getNrPriEnt() == recEntrega.getSequencia()) {
					recEntrega.setNrPriEnt(recEntrega.getId());
				}
				else if (recEntrega.getNrPriEnt() < recEntrega.getSequencia()
						|| recEntrega.getNrPriEnt() > recEntrega.getSequencia()) {
					for (RecEntrega item : recEntregaLista) {
						if (item.getSequencia() == recEntrega.getNrPriEnt()) {
							recEntrega.setNrPriEnt(item.getId());
						}
					}
				}
			}

			if (recEntrega.getLogReDpi()) {
				recEntrega.setNrReDpi(0L);
				for (RecEntrega item : recEntregaLista) {
					if (item.getNrReDpi() == recEntrega.getSequencia()) {
						item.setNrReDpi(recEntrega.getId());
					}
				}
			}

			if (recEntrega.getNrReTxSpRetida() > 0) {
				if (recEntrega.getNrReTxSpRetida() == recEntrega.getSequencia()) {
					recEntrega.setNrReTxSpRetida(recEntrega.getId());
				}
				else if (recEntrega.getNrReTxSpRetida() < recEntrega.getSequencia()
						|| recEntrega.getNrReTxSpRetida() > recEntrega.getSequencia()) {
					for (RecEntrega item : recEntregaLista) {
						if (item.getSequencia() == recEntrega.getNrReTxSpRetida()) {
							recEntrega.setNrReTxSpRetida(item.getId());
						}
					}
				}
			}
			
			for (RecEntregaItem recEntregaItem : recEntrega.getItens()) {
				String codigoItem = produtoService.buscarItemDataSul(recEntregaItem.getCodProduto(),
						OperacaoProdutoEnum.AFIXAR, recEntrega.getCodEmitente());
				recEntregaItem.setItCodigo(codigoItem);

				if (recEntrega.getLogReDpi()) {
					Produtor produtor = produtorService.buscarPorCodigoProdutor(recEntrega.getCodEmitente());
					BaixaCredito baixaCredito = new BaixaCredito();
					baixaCredito.setCodEstabel(recEntrega.getCodEstabel());
					baixaCredito.setCodEmitente(recEntrega.getCodEmitente());
					baixaCredito.setCpfProdutor(produtor.getCpfCnpj());
					baixaCredito.setDtEmiRe(recEntrega.getDtEntrada());
					baixaCredito.setIdRe(recEntrega.getId());
					baixaCredito.setNrRe(null); // será atualizado com o retorno da numeração
					baixaCredito.setQtParaBxa(recEntregaItem.getRendaSemDescDpi());
					baixaCredito.setQtDpi(recEntregaItem.getRendaLiquida());
					baixaCredito.setSafra(recEntrega.getSafra());
					baixaCredito.setSituacao("Aguardando Baixa");
					baixaCredito.setQtParaBxa(recEntregaItem.getRendaSemDescDpi());

					ImovelProdutor imovelProdutor = imovelProdutorService.buscarPorMatriculaECodProdutor(recEntrega.getMatricula(), produtor.getCodProdutor());
					if (imovelProdutor != null && imovelProdutor.getImovel() != null && imovelProdutor.getImovel().getEstado() != null && imovelProdutor.getImovel().getEstado().equals("SP")) {
						if (imovelProdutor.getCpfCnpj() != null) {
							baixaCredito.setCpfProdutor(imovelProdutor.getCpfCnpj());
						}
					}

					BaixaCreditoMov baixaCreditoMov = new BaixaCreditoMov();
					baixaCreditoMov.setCodEstabel(recEntrega.getCodEstabel());
					baixaCreditoMov.setNrRe(null);
					baixaCreditoMov.setIdRe(recEntrega.getId());
					baixaCreditoMov.setTransacao("Entrada RE");
					baixaCreditoMov.setObservacao("Entrada de RE no Genesis, aguardar baixa de crédito");
					baixaCreditoMov.setQtdade(recEntregaItem.getRendaLiquida());

					baixaCreditoService.salvalOuAtualizar(baixaCredito);					
					baixaCreditoMovService.criarBaixaCreditoMovimento(baixaCreditoMov);
					
					if (TipoEntradaEnum.ENTRADA.equals(tipoEntradaEnum)) {
						Pesagem pesagem = pesagemService.buscarPorCodigoEstabelecimentoSafraDocPesagem(
								recEntrega.getCodEstabel(), recEntrega.getSafra(), recEntrega.getNrDocPes());
						if (pesagem != null && !mapPesagens.containsKey(pesagem.getId())) {
							pesagem.setIntegrado(true);
							pesagem.setStatus(StatusPesagem.CONCLUIDO);
							pesagem.setLogReJava(true);
							pesagemService.salvar(pesagem);
							mapPesagens.put(pesagem.getId(), pesagem);
						}
					}
				}

//				RecEntregaItem recEntregaItemNormal = (RecEntregaItem) recEntregaItem.clone();
//				recEntregaItemNormal.setRecEntrega(recEntrega);
//				recEntrega.getItens().add(recEntregaItemNormal);
				MovimentoReDto movimentoRe = new MovimentoReDto();
				movimentoRe.setIdRe(recEntrega.getId().toString());
				movimentoRe.setCodEstabel(recEntrega.getCodEstabel());
				movimentoRe.setNrRe(null);
				movimentoRe.setQuantidade(recEntregaItem.getRendaLiquida());
				movimentoRe.setTransacao("Entrada RE - Genesis");
				movimentoRe.setObservacao("Entrada de RE no Genesis, via Entradas de Produção");
				movimentoReService.criarMovimentoRe(movimentoRe);
			}
		}
		
		mapPesagens.clear();
		return recEntregaLista;
	}

	private Integer getSafra(Date getDtEntrada) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDtEntrada);
		Integer safra = calendar.get(Calendar.YEAR);
		return safra;
	}
}
