package br.coop.integrada.api.pa.domain.validation.rec.entrega;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.domain.enums.NaturezaEnum;
import br.coop.integrada.api.pa.domain.enums.Operacao;
import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.model.parametros.ParametroEstabelecimento;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.model.rependente.RePendente;
import br.coop.integrada.api.pa.domain.modelDto.natureza.NaturezaResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.ValidaEntradaCooperativaDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.ValidaEntradaCooperativaResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.produtor.VerificaProdutorResponseDto;
import br.coop.integrada.api.pa.domain.service.ProdutorService;
import br.coop.integrada.api.pa.domain.service.imovel.ImovelService;
import br.coop.integrada.api.pa.domain.service.natureza.NaturezaService;
import br.coop.integrada.api.pa.domain.service.parametroEstabelecimento.ParametroEstabelecimentoService;

@Service
public class RecEntregaValidation {
	
	@Autowired
	private ProdutorService produtorService;
	
	@Autowired
	private ParametroEstabelecimentoService parametroEstabelecimentoService;
	
	@Autowired
	private ImovelService imovelService;
	
	@Autowired
	private NaturezaService naturezaService;

	public List<String> validar(List<RecEntrega> recEntregas) {
		List<String> mensagens = new ArrayList<>();
		
		verificarSeExisteUmaRecEntregaComPesoLiquidoMenorQue1Kg(recEntregas, mensagens);
		verificarEntradaNomeCooperativa(recEntregas, mensagens);
		verificarParametroEstabelecimento(recEntregas, mensagens);
		verificarImovel(recEntregas, mensagens);
		verificarCaracteristicasProdutor(recEntregas, mensagens);
		verificarSeProdutorEmiteNF(recEntregas, mensagens);
		
		return mensagens;
	}
	
	private void verificarSeExisteUmaRecEntregaComPesoLiquidoMenorQue1Kg(List<RecEntrega> recEntregas, List<String> mensagens) {
		if(CollectionUtils.isEmpty(recEntregas)) return;
		
		List<String> codEmitenteComPesoLiquidoMenorQue1Kg = recEntregas.stream().filter(recEntrega -> {
			return recEntrega.getPesoLiquido().compareTo(BigDecimal.ONE) == -1;
		}).map(recEntrega -> {
			return recEntrega.getCodEmitente();
		}).toList();
		
		if(!CollectionUtils.isEmpty(codEmitenteComPesoLiquidoMenorQue1Kg)) {
			String codEmitentes = String.join("\", \"", codEmitenteComPesoLiquidoMenorQue1Kg);
			mensagens.add("Peso líquido abaixo de 1kg não pode ser atualizado, pois a renda líquida fica zerada. (Código Emitente: \"" + codEmitentes + "\")");
		}
	}
	
	private void verificarEntradaNomeCooperativa(List<RecEntrega> recEntregas, List<String> mensagens) {
		for(RecEntrega recEntrega : recEntregas) {
			Produtor produtor = produtorService.findByCodProdutor(recEntrega.getCodEmitente());
			
			if(produtor.getCooperativa()) {
				ValidaEntradaCooperativaDto entradaCooperativaInput = new ValidaEntradaCooperativaDto();
				
				entradaCooperativaInput.setCodEstabelecimento(recEntrega.getCodEstabel()); 
				entradaCooperativaInput.setCodImovel(recEntrega.getMatricula());
				entradaCooperativaInput.setCodProdutor(recEntrega.getCodEmitente());
				ValidaEntradaCooperativaResponseDto valEntCoopResponse = parametroEstabelecimentoService.validarEntradaNomeCooperativa(entradaCooperativaInput);
				
				if(!valEntCoopResponse.getPermitido()) {
					mensagens.add(valEntCoopResponse.getMensagemPermitido());
				}
				
				if(valEntCoopResponse.getCodProd().equals("Diferente")) {
					mensagens.add(valEntCoopResponse.getMensagemCodProd());
				}
				
				if(valEntCoopResponse.getCodImovel().equals("Diferente")) {
					mensagens.add(valEntCoopResponse.getMensagemCodImovel());
				}
			}
		}
	}
	
	private void verificarParametroEstabelecimento(List<RecEntrega> recEntregas, List<String> mensagens) {
		ParametroEstabelecimento parametroEstabelecimento = null;
		
		for(RecEntrega recEntrega : recEntregas) {
			if(parametroEstabelecimento == null || !parametroEstabelecimento.getEstabelecimento().getCodigo().equalsIgnoreCase(recEntrega.getCodEstabel())) {
				parametroEstabelecimento = parametroEstabelecimentoService.buscarPorCodigoEstabelecimento(recEntrega.getCodEstabel());
			}
			
			Produtor produtor = produtorService.buscarPorCodigoProdutor(recEntrega.getCodEmitente());
			
			Boolean obrigaNfProdutor = parametroEstabelecimento.getObrigaNfProdutor();
			Boolean numeroNfProdutorNaoPreenchida = recEntrega.getNrNfProd() == null;
			Boolean produtorPfOuPjNaoEmiteNf = !(produtor.getNatureza() != null && produtor.getNatureza().equalsIgnoreCase("PJ") && produtor.getEmiteNota());
			if(obrigaNfProdutor && numeroNfProdutorNaoPreenchida && produtorPfOuPjNaoEmiteNf) {
				mensagens.add("Necessário informar a nota fiscal do produtor");
			}
			
			Calendar hoje = Calendar.getInstance();
			Integer anoCorrente = hoje.get(Calendar.YEAR);
			
			Calendar dataEntrada = Calendar.getInstance();
			dataEntrada.setTime(recEntrega.getDtEntrada());
			Integer safra = dataEntrada.get(Calendar.YEAR);
			
			if(safra < anoCorrente) {
				mensagens.add("Não está liberado entrada de RE de safra anterior para esse estabelecimento, se necessário favor verificar com o SIO");
			}
		}
	}
	
	private void verificarImovel(List<RecEntrega> recEntregas, List<String> mensagens) {
		for(RecEntrega recEntrega : recEntregas) {
			try {
				imovelService.validarImovelEntrada(recEntrega.getMatricula(), recEntrega.getCodEmitente(), recEntrega.getCodEstabel());
			}
			catch (Exception e) {
				mensagens.add(e.getMessage());
			}
		}
	}
	
	private void verificarCaracteristicasProdutor(List<RecEntrega> recEntregas, List<String> mensagens) {
		for(RecEntrega recEntrega : recEntregas) {
			try {
				VerificaProdutorResponseDto verificaProdutorResponseDto = produtorService.verificarProdutor(recEntrega.getCodEmitente());
				
				if(verificaProdutorResponseDto.getBloqueado()) {
					mensagens.add("Produtor \"" + recEntrega.getCodEmitente() + "\" Bloqueado.");
				}
				
				if(verificaProdutorResponseDto.getAtivo() == false) {
					mensagens.add("Produtor \"" + recEntrega.getCodEmitente() + "\" não está Ativo.");
				}
				
				if(Strings.isEmpty(verificaProdutorResponseDto.getCodRegional())) {
					mensagens.add("Produtor \"" + recEntrega.getCodEmitente() + "\" não possui regional informada.");
				}
				
				if(verificaProdutorResponseDto.getEmiteNotaPropria()) {
					recEntrega.setNatureza(NaturezaEnum.PJ_NF);
				}

				if(verificaProdutorResponseDto.getEmiteNotaPropria() == false) {
					recEntrega.setNatureza(NaturezaEnum.PJ);
				}
				
				if(verificaProdutorResponseDto.getCooperativa()) {
					recEntrega.setNatureza(NaturezaEnum.COOP);
				}
				
				if(verificaProdutorResponseDto.getNatureza().equalsIgnoreCase("PF")) {
					recEntrega.setNatureza(NaturezaEnum.PF);
				}
				
				recEntrega.setCodRegional(verificaProdutorResponseDto.getCodRegional());
				recEntrega.setCdnRepres(verificaProdutorResponseDto.getCodRepres());
			}
			catch (Exception e) {
				mensagens.add(e.getMessage());
			}
		}
	}
	
	private void verificarSeProdutorEmiteNF(List<RecEntrega> recEntregas, List<String> mensagens) {
		for(RecEntrega recEntrega : recEntregas) {
			Produtor produtor = produtorService.findByCodProdutor(recEntrega.getCodEmitente());
			
			if(produtor.getEmiteNota() == false) {
				NaturezaResponseDto naturezaResponseDto = naturezaService.buscarNaturezaPor(recEntrega.getCodEstabel(), Operacao.ENTRADA, recEntrega.getCodEmitente(), recEntrega.getFmCodigo(), "");
				if(naturezaResponseDto.getIntegrated()) {
					recEntrega.setSerieDocum(naturezaResponseDto.getSerie());
					recEntrega.setNatDocum(naturezaResponseDto.getNaturezaOperacao());
					recEntrega.setNroDocum("");
				}
				else {
					mensagens.add(naturezaResponseDto.getMessage());
				}
			}
		}
	}
	
	public static void validarNotaPropria(RePendente obj) {
		Produtor produtor = obj.getProdutor();
		if(produtor == null || Strings.isEmpty(produtor.getNatureza()) || !produtor.getNatureza().equalsIgnoreCase("PJ") || !produtor.getEmiteNota()) return;
		
    	List<String> mensagens = new ArrayList<String>();
    	if(Strings.isBlank(obj.getPjNroNota())) mensagens.add("Nota fiscal");
    	if(Strings.isBlank(obj.getPjSerie())) mensagens.add("Serie");
    	if(obj.getPjDtEmissao() == null) mensagens.add("Data emissão");
    	if(Strings.isBlank(obj.getPjNatOper())) mensagens.add("Natureza");
    	
    	String chaveAcesso = obj.getPjChaveAcesso();
    	if(Strings.isEmpty(obj.getPjChaveAcesso())) {
    		mensagens.addAll(Arrays.asList("UF", "AAMM", "CNPJ", "MOD", "Série", "NF-e", "F.NF-e", "COD", "DV"));
    	}
    	else {
            String uf = "";
            String aamm = "";
            String cnpj = "";
            String mod = "";
            String serie = "";
            String nfe = "";
            String fnfe = "";
            String cod = "";
            String dv = "";
            
            try {
            	uf = chaveAcesso.substring(0, 2);
	            aamm = chaveAcesso.substring(2, 6);
	            cnpj = chaveAcesso.substring(6, 20);
	            mod = chaveAcesso.substring(20, 22);
	            serie = chaveAcesso.substring(22, 25);
	            nfe = chaveAcesso.substring(25, 34);
	            fnfe = chaveAcesso.substring(34, 35);
	            cod = chaveAcesso.substring(35, 43);
	            dv = chaveAcesso.substring(43, 44);
            }
            catch (Exception e) {
            	e.printStackTrace();
			}
            
            if(Strings.isBlank(uf)) mensagens.add("UF");
            if(Strings.isBlank(aamm)) mensagens.add("AAMM");
            if(Strings.isBlank(cnpj)) mensagens.add("CNPJ");
            if(Strings.isBlank(mod)) mensagens.add("MOD");
            if(Strings.isBlank(serie)) mensagens.add("Série");
            if(Strings.isBlank(nfe)) mensagens.add("NF-e");
            if(Strings.isBlank(fnfe)) mensagens.add("F.NF-e");
            if(Strings.isBlank(cod)) mensagens.add("COD");
            if(Strings.isBlank(dv)) mensagens.add("DV");
    	}
    	
    	if(!CollectionUtils.isEmpty(mensagens)) {
    		String mensagem = String.join("\", \"", mensagens);
    		
    		if(mensagens.size() > 1) {
    			throw new ObjectDefaultException("Os campos \"" + mensagem + "\" são obrigatórios.");
    		}
    		else {
    			throw new ObjectDefaultException("O campo \"" + mensagem + "\" é obrigatório.");
    		}
    	}
	}
}
