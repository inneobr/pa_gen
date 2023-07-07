package br.coop.integrada.api.pa.domain.service.nfRemessa;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.NaturezaEnum;
import br.coop.integrada.api.pa.domain.enums.Operacao;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.enums.StatusNotaFiscalEnum;
import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.model.UnidadeFederacao;
import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import br.coop.integrada.api.pa.domain.model.nfRemessa.NfRemessa;
import br.coop.integrada.api.pa.domain.model.nfRemessa.NfRemessaFiltro;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.model.usuario.Usuario;
import br.coop.integrada.api.pa.domain.modelDto.natureza.NaturezaResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.nfRemessa.IndicadorNfRemessaDto;
import br.coop.integrada.api.pa.domain.modelDto.nfRemessa.NfRemessaChaveAcessoDto;
import br.coop.integrada.api.pa.domain.modelDto.nfRemessa.NfRemessaDadosFiscaisDto;
import br.coop.integrada.api.pa.domain.repository.nfRemessa.NfRemessaRep;
import br.coop.integrada.api.pa.domain.service.HistoricoGenericoService;
import br.coop.integrada.api.pa.domain.service.ProdutorService;
import br.coop.integrada.api.pa.domain.service.UnidadeFederacaoService;
import br.coop.integrada.api.pa.domain.service.UsuarioService;
import br.coop.integrada.api.pa.domain.service.imovel.ImovelService;
import br.coop.integrada.api.pa.domain.service.natureza.NaturezaService;
import br.coop.integrada.api.pa.domain.service.recEntrega.RecEntregaService;
import br.coop.integrada.api.pa.domain.spec.NfRemessaSpecs;

@Service
public class NfRemessaService {
	
	@Autowired
	NfRemessaRep nfRemessaRep;
	
	@Lazy
	@Autowired
	RecEntregaService entregaService;
	
	@Autowired
	private HistoricoGenericoService historicoGenericoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ProdutorService produtorService;
	
	@Autowired
	private ImovelService imovelService;
	
	@Autowired
	private UnidadeFederacaoService unidadeFederacaoService;
	
	@Autowired
	private NaturezaService naturezaService;
	
	public NfRemessa findById(Long id) {
		return nfRemessaRep.findById(id).orElse(null);
	}
	
	public NfRemessa salvar(NfRemessa nfRemessa) {
		
		NfRemessa nfRemessaRetorno = nfRemessaRep.save(nfRemessa);
		
		return nfRemessaRetorno;
	}
	
	public List<NfRemessa> saveAll(List<NfRemessa> lista) {
		return nfRemessaRep.saveAll(lista);
	}

	public List<NfRemessa> findByStatus(StatusNotaFiscalEnum status) {
		return nfRemessaRep.findByStatusOrderById(status);
	}
	
	public List<NfRemessa> findByStatusAndPendenciasFiscais(StatusNotaFiscalEnum status, Boolean pendenciasFiscais) {
		return nfRemessaRep.findByStatusAndPendenciasFiscaisOrderById(status, pendenciasFiscais);
	}
	
	//Objetivo: Criar estrutura para solicitar geração de nota fiscal
	public NfRemessa gerarNfRemessa(String codEstabel, Long idRecEntrega, Long nrRe, BigDecimal quantidade) {
		NfRemessa nfRemessa = new NfRemessa();
		
		nfRemessa.setCodEstabel(codEstabel);
		nfRemessa.setIdRecEntrega(idRecEntrega);	
		nfRemessa.setQuantidade(quantidade);
		nfRemessa.setNrRe(nrRe);
		nfRemessa.setStatus(StatusNotaFiscalEnum.AGUARDANDO_INTEGRACAO);
		nfRemessa.setDtCriacao( new Date() );
		
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");  
	    Date date = new Date();  
	    
		nfRemessa.setHrCriacao( formatter.format(date) );
		
		RecEntrega recEntrega = entregaService.buscarRe(codEstabel, nrRe);
		nfRemessa.setPendenciasFiscais(recEntrega.getPendenciasFiscais());
		nfRemessa.setInfoNfProdutorManual(false);
		
		if(recEntrega.getNatureza().equals(NaturezaEnum.PJ_NF)) {
			nfRemessa.setFuncaoNota("Escritura");
			nfRemessa.setNatOperacao(recEntrega.getPjNatOper());
			nfRemessa.setSerieDocto(recEntrega.getPjSerie());
			nfRemessa.setNrDocto(recEntrega.getPjNroNota());
			nfRemessa.setSeqItem(recEntrega.getItens().get(0).getSeqItemDocum());
			nfRemessa.setCodRefer(recEntrega.getItens().get(0).getCodRefer());
		}
		else {
			nfRemessa.setFuncaoNota("Gera");
		}
		
		nfRemessa = this.salvar(nfRemessa);
		historicoGenericoService.salvar(nfRemessa.getId(), PaginaEnum.NF_REMESSA, "Gerar", "Gerando NF Remessa referente a RE " + recEntrega.getNrRe());
		
		return nfRemessa;
	}
	
	public NfRemessa atualizarDadosFiscais(Long idRemessa, NfRemessaDadosFiscaisDto objDto) {
		NfRemessa nfRemessa = nfRemessaRep.findById(idRemessa)
				.orElseThrow(() -> new ObjectNotFoundException("Não foi encontrado NF Remessa com o ID " + idRemessa + ""));
		
		RecEntrega recEntrega = entregaService.buscarRe(nfRemessa.getCodEstabel(), nfRemessa.getNrRe());
		
		if(!recEntrega.getPendenciasFiscais()) {
			throw new ObjectNotFoundException("A RE " + recEntrega.getNrRe() + " não possui pendências fiscais."); 
		}
		
		recEntrega.setPjLogNotaPropria(true);
		recEntrega.setPendenciasFiscais(false);
		
		BeanUtils.copyProperties(objDto, recEntrega);
		
		recEntrega = entregaService.atualizar(recEntrega);
		
		nfRemessa.setPendenciasFiscais(false);
		nfRemessa.setInfoNfProdutorManual(true);
		nfRemessa.setFuncaoNota("Escritura");
		nfRemessa.setNatOperacao(recEntrega.getPjNatOper());
		nfRemessa.setSerieDocto(recEntrega.getPjSerie());
		nfRemessa.setNrDocto(recEntrega.getPjNroNota());
		nfRemessa.setSeqItem(recEntrega.getItens().get(0).getSeqItemDocum());
		nfRemessa.setCodRefer(recEntrega.getItens().get(0).getCodRefer());
		
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");  
	    Date data = new Date();  
		nfRemessa.setDtUltMov(data);
		nfRemessa.setHrUltMov(formatter.format(data));
		
		Usuario usuario = usuarioService.getUsuarioLogado();
		nfRemessa = nfRemessaRep.save(nfRemessa);
		
		StringBuilder mensagem = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		mensagem.append("O usuário " + usuario.getUsername() + " \"" + usuario.getNome() + "\" alterou as informações fiscais.");
		mensagem.append("\nNatureza Operacação: " + objDto.getPjNatOper());
		mensagem.append("\nNro Nota Fiscal: " + objDto.getPjNroNota());
		mensagem.append("\nSérie: " + objDto.getPjSerie());
		mensagem.append("\nData: " + sdf.format(objDto.getPjDtEmissao()));
		mensagem.append("\nValor total: " + objDto.getPjVlTotNota());
		mensagem.append("\nQtd total: " + objDto.getPjQtTotNota());
		mensagem.append("\nChave de acesso: " + objDto.getPjChaveAcesso());
		
		historicoGenericoService.salvar(nfRemessa.getId(), PaginaEnum.NF_REMESSA, "Atualizar", mensagem.toString());
		
		return nfRemessa;
	}
	
	public Page<NfRemessa> buscarPorPaginacao(Pageable pageable, NfRemessaFiltro filter){
		return nfRemessaRep.findAll(
				NfRemessaSpecs.codEstabelecimentoEquals(filter.getCodEstabelecimento())
				.and(NfRemessaSpecs.dtCriacaoBetween(filter.getDataInicio(), filter.getDataFinal()))
				.and(NfRemessaSpecs.pendenciasFiscaisEquals(filter.getPendenciasFiscais()))
				, pageable);
	}
	
	public NfRemessa buscarPorRecEntrega(String codEstabel, Long IdRecEntrega) {
		return nfRemessaRep.findByCodEstabelAndIdRecEntrega(codEstabel, IdRecEntrega);
	}

	public IndicadorNfRemessaDto buscarIndicadores(NfRemessaFiltro filter) {
		Map<String, BigDecimal> map = nfRemessaRep.buscarIndicadores(
				filter.getCodEstabelecimento(),
				filter.getDataInicio(),
				filter.getDataFinal(),
				filter.getPendenciasFiscais());
				
		IndicadorNfRemessaDto indicador = new IndicadorNfRemessaDto();
		
		if(map != null) {			
			indicador.setAguardandoIntegracao(map.get("AGUARDANDO_INTEGRACAO"));
			indicador.setEmProcesso(map.get("EM_PROCESSAMENTO"));
			indicador.setAguardandoTotvs(map.get("AGUARDANDO_TOTVS"));
			indicador.setNfeGerada(map.get("NFE_GERADA"));				
			indicador.setFuncaoGera(map.get("FUNCAO_NOTA_GERA"));
			indicador.setFuncaoEscritura(map.get("FUNCAO_NOTA_ESCRITURA"));
			indicador.setTotal(map.get("TOTAL"));			
		}
		
		return indicador;
	}
	
	public List<NfRemessa> findByCodEstabelAndStatusAndPendenciasFiscaisAndDtCriacaoBetween(NfRemessaFiltro filter){
		return nfRemessaRep.findByCodEstabelAndStatusAndPendenciasFiscaisAndDtCriacaoBetween(
				filter.getCodEstabelecimento() ,
				filter.getStatus(),
				filter.getPendenciasFiscais(),
				filter.getDataInicio(),
				filter.getDataFinal());
	}

	public NfRemessaChaveAcessoDto buscarDadosChaveAcessoPorIdRecEntrega(Long idRecEntrega) {
		RecEntrega recEntrega = entregaService.findById(idRecEntrega);
		if(recEntrega == null) throw new ObjectNotFoundException("Não foi encontrado RE com o id \"" + idRecEntrega + "\" informado");
		
		String codProdutor = recEntrega.getCodEmitente();
		Produtor produtor = produtorService.findByCodProdutor(codProdutor); 
		if(produtor == null) throw new ObjectNotFoundException("Não foi encontrado produtor com o código " + codProdutor);
		
		Long matricula = recEntrega.getMatricula();
		Imovel imovel = imovelService.buscarPorMatricula(matricula);
		if(imovel == null) throw new ObjectNotFoundException("Não foi encontrado imóvel com a matrícula " + matricula);
		
		UnidadeFederacao unidadeFederacao = unidadeFederacaoService.buscarPorEstado(imovel.getEstado());
		if(unidadeFederacao == null) throw new ObjectNotFoundException("Não foi encontrado cadastro da unidade de federação para o estado " + imovel.getEstado());
		
		String codigoEstabelecimento = recEntrega.getCodEstabel();
		String codigoGrupoProduto = recEntrega.getFmCodigo();
		NaturezaResponseDto naturezaOperacao = naturezaService.buscarNaturezaPor(codigoEstabelecimento, Operacao.ENTRADA, codProdutor, codigoGrupoProduto, "");
		
		var objDto = new NfRemessaChaveAcessoDto();
    	objDto.setCnpj(produtor.getCpfCnpj());
    	objDto.setUf(unidadeFederacao.getCodigoIbge());
    	objDto.setMod(naturezaOperacao.getModeloEletronico());
    	objDto.setPjNatOper(naturezaOperacao.getNaturezaOperacao());
    	
    	return objDto;
	}
	
}
