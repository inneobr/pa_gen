package br.coop.integrada.api.pa.domain.validation.re.pendente;

import static br.coop.integrada.api.pa.domain.enums.ParametroEstabelecimentoSituacaoEnum.BLOQUEADO;
import static java.math.BigDecimal.ZERO;
import static java.time.Month.JANUARY;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.ParametroEstabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.ParametrosUsuarioEstabelecimento;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProdutoGmo;
import br.coop.integrada.api.pa.domain.model.rependente.RePendente;
import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampo;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampoProdutor;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeLaudoInspecao;
import br.coop.integrada.api.pa.domain.model.usuario.Usuario;
import br.coop.integrada.api.pa.domain.modelDto.externo.NotaFiscalEntradaResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.safra.composta.SafraCompostaSimplesDto;
import br.coop.integrada.api.pa.domain.repository.parametros.ParametroEstabelecimentoRep;
import br.coop.integrada.api.pa.domain.repository.parametros.ParametroUsuarioEstabelecimentoRep;
import br.coop.integrada.api.pa.domain.repository.produto.GrupoProdutoGmoRep;
import br.coop.integrada.api.pa.domain.repository.semente.SementeClasseRep;
import br.coop.integrada.api.pa.domain.repository.semente.produtor.SementeCampoProdutorRep;
import br.coop.integrada.api.pa.domain.repository.semente.produtor.SementeCampoRep;
import br.coop.integrada.api.pa.domain.repository.semente.produtor.SementeLaudoInspecaoRep;
import br.coop.integrada.api.pa.domain.service.UsuarioService;
import br.coop.integrada.api.pa.domain.service.externo.ErpExtService;
import br.coop.integrada.api.pa.domain.service.imovel.ImovelService;
import br.coop.integrada.api.pa.domain.service.parametros.SafraCompostaService;

@Service
public class RePendenteValidation {

    @Autowired
    private ParametroEstabelecimentoRep parametroEstabelecimentoRep;

    @Autowired
    private GrupoProdutoGmoRep grupoProdutoGmoRep;

    @Autowired
    private SementeClasseRep sementeClasseRep;

    @Autowired
    private SementeCampoRep sementeCampoRep;

    @Autowired
    private SementeCampoProdutorRep sementeCampoProdutorRep;

    @Autowired
    private SementeLaudoInspecaoRep sementeLaudoInspecaoRep;

    @Autowired
    private ParametroUsuarioEstabelecimentoRep parametroUsuarioEstabelecimentoRep;

    @Autowired
    private ImovelService imovelService;

    @Autowired
    private SafraCompostaService safraCompostaService;

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ErpExtService erpExtService;

    public List<String> validar(RePendente obj, boolean validarImovel) {
    	List<String> mensagens = new ArrayList<>();
    	
        validarCampos(obj, mensagens);
        validarPeso(obj, mensagens);

        Estabelecimento estabelecimento = obj.getEstabelecimento();
        if(estabelecimento == null) {
            mensagens.add("O estabelecimento deve ser informado");
        }

        ParametroEstabelecimento parametroEstabelecimento = parametroEstabelecimentoRep.findByEstabelecimento(estabelecimento);
        validarParametroEstabelecimento(obj, parametroEstabelecimento, mensagens);
        validarProdutor(obj, parametroEstabelecimento, mensagens);
        validarGrupoProduto(obj, parametroEstabelecimento, mensagens);
        
        if(validarImovel) {
        	validarImovel(obj, mensagens);
        }
        validarUsuario(obj, mensagens);


        Produtor produtor = obj.getProdutor();
        if(produtor.getCodRepres() == null || Strings.isEmpty(produtor.getCodRepres().trim())) {
            mensagens.add("Produtor está sem representante, favor entrat em contato com o encarregado de crédito da sua regional para regularizar");
        }

        GrupoProduto grupoProduto = obj.getGrupoProduto();
        try {
        	SafraCompostaSimplesDto safraCompostaDto = safraCompostaService.buscarPorDataGrupoProdutoEstabelecimento(obj.getDtEntrada(), grupoProduto.getFmCodigo(), estabelecimento.getCodigo());
        	obj.setSafraCompos(safraCompostaDto.getSafraComposta());
        	obj.setNomeSafraCompos(safraCompostaDto.getNomeSafra());
        }
        catch(Exception e) {
            mensagens.add("Safra composta não encontrada. Favor entrar em contato com o Departamento de Operações para regularização");
        }

        return mensagens;
    }

    private void validarCampos(RePendente obj, List<String> mensagens) {
        if(obj.getDtEntrada() == null) {
            mensagens.add("A data do recebimento deve se informada");
        }

        LocalDate dataAtual = LocalDate.now();
        LocalDate dataEntrada = obj.getDtEntrada().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if(dataAtual.isBefore(dataEntrada)) {
            mensagens.add("A data do recebimento não pode ser maior que que hoje");
        }

        if(Strings.isEmpty(obj.getHrEntrada())) {
            mensagens.add("A hora da entrada deve ser informada");
        }

        if(Strings.isEmpty(obj.getPlaca())) {
            mensagens.add("A placa deve ser informada");
        }

        if(obj.getImovel() == null) {
            mensagens.add("O imóvel deve ser informado");
        }
    }

    private void validarPeso(RePendente obj, List<String> mensagens) {
        if(obj.getPesoLiquido() == null || obj.getPesoLiquido().compareTo(ZERO) == 0) {
            mensagens.add("O peso líquido deve ser informado");
        }

        if(obj.getPesoBruto() == null || obj.getPesoBruto().compareTo(ZERO) == 0) {
            mensagens.add("O peso bruto deve ser informado");
        }

        if(obj.getTaraVeiculo().compareTo(obj.getPesoBruto()) > 0) {
            mensagens.add("Tara do veículo deve ser menor que o peso bruto");
        }

        if(obj.getTaraSacaria().compareTo(obj.getPesoBruto()) > 0) {
            mensagens.add("Tara da sacaria deve ser menor que o peso bruto");
        }

        if(obj.getPesoBruto().compareTo(ZERO) > 0 && obj.getPesoLiquido().compareTo(ZERO) > 0) {
            BigDecimal pesoLiquido = obj.getPesoBruto().subtract(obj.getTaraVeiculo()).subtract(obj.getTaraSacaria());
            if(obj.getPesoLiquido().compareTo(pesoLiquido) != 0){
                mensagens.add("Peso líquido deve ser válid! O peso líquido deve ser igual ao resutado calculo (Peso Bruto - Tara Veículo - Tara Sacaria)");
            }
        }
    }

    private void validarParametroEstabelecimento(RePendente obj, ParametroEstabelecimento parametroEstabelecimento, List<String> mensagens) {
        if(BLOQUEADO.equals(parametroEstabelecimento.getSituacao())) {
            mensagens.add("Estabelecimento está bloqueado para movimentação no sistema de produção");
        }

        LocalDate dataAtual = LocalDate.now();
        LocalDate dataEntrada = obj.getDtEntrada().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Boolean isDiaAtualMaiorQue15 = dataAtual.getDayOfMonth() > 15;
        Boolean isMesAtualEhJaneiro = dataAtual.getMonth().equals(JANUARY);
        Boolean isAnoAtualEhDiferenteAnoEntrada = dataAtual.getYear() != dataEntrada.getYear();
        if(isDiaAtualMaiorQue15 && isMesAtualEhJaneiro && isAnoAtualEhDiferenteAnoEntrada && parametroEstabelecimento.getEntSafraAnt() == false) {
            mensagens.add("O ano da data de entrada está diferente do ano atual, não está liberado a entrada de safra anterior para o estabelecimento");
        }

        if(obj.getEntradaManual() && parametroEstabelecimento.getLogEntradaSemTik() == false) {
            mensagens.add("Entrada manual não liberada para o estabelecimento");
        }

        if(parametroEstabelecimento.getLogSilo() == false) {
            mensagens.add("O estabelecimento de recebimento não está parâmetrizado como silo");
        }
    }

    private void validarProdutor(RePendente obj, ParametroEstabelecimento parametroEstabelecimento, List<String> mensagens) {
        Produtor produtor = obj.getProdutor();
        Boolean isMatriculaImovelEhIgualAoCodigoImovelDoParametroEstabelecimento = obj.getImovel() != null && obj.getImovel().getMatricula().equals(parametroEstabelecimento.getCodImovel()); 
        
        if(produtor == null) {
            mensagens.add("O produtor deve ser informado");
        }
        else if(produtor.getCooperativa() && parametroEstabelecimento.getPermitirEntradaCooperativa() == false) {
            mensagens.add("Não é permitido entrada em nome da cooperativa");
        }
        else if(produtor.getCooperativa() && Strings.isEmpty(obj.getNomeProd())) {
            mensagens.add("O nome do produtor deve ser informado");
        }
        else if(produtor.getCooperativa() && !parametroEstabelecimento.getCodEmitente().equalsIgnoreCase(produtor.getCodProdutor())) {
            mensagens.add("A entrada em nome da cooperativa deve ser realizada com o código de produtor parametrizado nos parâmetros do estabelecimento.");
        }
        else if(produtor.getCooperativa() && !isMatriculaImovelEhIgualAoCodigoImovelDoParametroEstabelecimento) {
            mensagens.add("A entrada em nome da cooperativa deve ser feita com o imóvel informado nos parâmetros do estabelecimento."); 
        }
        
        if(produtor != null && produtor.getAtivo() == false) {
            mensagens.add("O produtor não pode receber produção, pois está falecido ou demissionário.");
        }
        
        if(produtor != null && produtor.getBloqueado()) {
            mensagens.add("O produtor não pode receber produção, pois está bloqueado.");
        }
        
        if(produtor != null && Strings.isEmpty(produtor.getCodRegional())) {
            mensagens.add("O cooperado não possui regional cadastrada");
        }

        Boolean obrigaNfProdutor = parametroEstabelecimento.getObrigaNfProdutor();
		Boolean produtorPfOuPjNaoEmiteNf = !(produtor.getNatureza() != null && produtor.getNatureza().equalsIgnoreCase("PJ") && produtor.getEmiteNota());

        if(obrigaNfProdutor && produtorPfOuPjNaoEmiteNf) {
    		List<String> mensagensAux = new ArrayList<String>();
        	if(obj.getNrNfProd() == null) mensagensAux.add("Nota fiscal");
        	if(obj.getSerNfProd() == null || Strings.isEmpty(obj.getSerNfProd().trim())) mensagensAux.add("Serie");
        	if(obj.getDtNfProd() == null) mensagensAux.add("Data emissão");
        	
        	if(!CollectionUtils.isEmpty(mensagensAux)) {
        		String mensagem = String.join("\", \"", mensagensAux);
        		
        		if(mensagensAux.size() > 1) {
        			mensagens.add("Os campos \"" + mensagem + "\" são obrigatórios.");
        		}
        		else {
        			mensagens.add("O campo \"" + mensagem + "\" é obrigatório.");
        		}
        	}
        }
        
        if(Strings.isNotBlank(obj.getPjNroNota()) && Strings.isNotBlank(obj.getPjSerie()) && Strings.isNotBlank(obj.getPjNatOper()) && Strings.isNotBlank(produtor.getCodProdutor())) {        	
        	try {
        		NotaFiscalEntradaResponseDto response = erpExtService.validarNotaFiscalEntrada(obj.getPjNroNota(), obj.getPjSerie(), obj.getPjNatOper(), produtor.getCodProdutor());
        		if(response.getExiste()) {        		
        			mensagens.add("Nota fiscal já existe no sistema, não é possível prosseguir.");
        		}
        	}
        	catch (Exception e) {
        		if(e.getMessage() != null && !e.getMessage().contains("temporariamente indisponível")) {
        			mensagens.add("Falha ao validar nota fiscal: " + e.getMessage());
        		}
        	}
        }
    }

    private void validarGrupoProduto(RePendente obj, ParametroEstabelecimento parametroEstabelecimento, List<String> mensagens) {
        GrupoProduto grupoProduto = obj.getGrupoProduto();

        if(grupoProduto == null) {
            mensagens.add("O grupo de produto deve ser informado");
            return;
        }

        if(grupoProduto.isLogTransgenico() == false && obj.getTipoGmo() != null) {
            mensagens.add("O grupo de produto não está parâmetrizado para GMO");
        }

        if(grupoProduto.isLogTransgenico()) {
	        if(parametroEstabelecimento.getLogRecebeTransgenico() == false) {
	            mensagens.add("O estabelecimento não pode receber transgênico");
	        }
	
	        if(obj.getTipoGmo() == null) {
	            mensagens.add("O tipo GMO deve ser informado");
	        }
	
	        Boolean isPatentiada = obj.getTipoGmo() != null && obj.getTipoGmo().getTipoGmo() != null && obj.getTipoGmo().getTipoGmo().equalsIgnoreCase("PATENTIADA");
	        Boolean isDeclaradaOuTestada = obj.getTipoRr() != null && (obj.getTipoRr().equalsIgnoreCase("DECLARADA") || obj.getTipoRr().equalsIgnoreCase("TESTADA"));
	        if(isPatentiada && !isDeclaradaOuTestada) {
	            mensagens.add("Deve indicar se a patentiada é Declarada ou Testada");
	        }
	
	        GrupoProdutoGmo grupoProdutoGmo = grupoProdutoGmoRep.findByGrupoProdutoAndTipoGmo(grupoProduto, obj.getTipoGmo());
	        if(grupoProdutoGmo == null) {
	            mensagens.add("Não existe vinculo entre o Grupo de Produto e Tipo GMO informado");
	        }
        }

        if(grupoProduto.isNrOrdCampo()) {
            validarSementeCampo(obj, mensagens);
        }
    }

    private void validarSementeCampo(RePendente obj, List<String> mensagens) {
        Estabelecimento estabelecimento = obj.getEstabelecimento();
        GrupoProduto grupoProduto = obj.getGrupoProduto();
        Produtor produtor = obj.getProdutor();

        if(estabelecimento == null || grupoProduto == null || produtor == null) {
            mensagens.add("Não é possível consultar semente campo sem o Estabelecimento, Grupo de produto ou Produtor");
            return;
        }

        if(obj.getClasse() == null) return;
        
        SementeClasse sementeClasse = sementeClasseRep.findByCodigo(obj.getClasse().getCodigo());
        if(sementeClasse == null) {
            mensagens.add("Não foi encontrado classe com o código informado");
            return;
        }

        Calendar dataEntrada = Calendar.getInstance();
		dataEntrada.setTime(obj.getDtEntrada());
		Integer safra = dataEntrada.get(Calendar.YEAR);
        Integer ordemCampo = obj.getNrOrdCampo();

        SementeCampo sementeCampo = sementeCampoRep.findBySafraAndOrdemCampoAndEstabelecimentoAndGrupoProdutoAndClasse(safra, ordemCampo, estabelecimento, grupoProduto, sementeClasse).orElse(null);

        if(sementeCampo == null) {
            mensagens.add("Campo de Semente não cadastrado");
        }
        else if(!sementeCampo.getMatricula().equalsIgnoreCase(obj.getMatricula().toString())) {
            mensagens.add("Campo não pertence ao imóvel informado");
        }

        SementeCampoProdutor sementeCampoProdutor = sementeCampoProdutorRep.findBySafraAndOrdemCampoAndProdutorAndEstabelecimentoAndGrupoProdutoAndClasse(safra, ordemCampo, produtor, estabelecimento, grupoProduto, sementeClasse).orElse(null);
        if(sementeCampoProdutor == null) {
            mensagens.add("Produtor não cadastrado para o campo semente");
        }

        Long numeroLaudo = obj.getNrLaudo() != null ? Long.parseLong(obj.getNrLaudo().toString()) : null;
        SementeLaudoInspecao sementeLaudoInspecao = sementeLaudoInspecaoRep.findBySafraAndEstabelecimentoAndNumeroLaudoAndOrdemCampoAndGrupoProdutoAndClasse(safra, estabelecimento, numeroLaudo, ordemCampo, grupoProduto, sementeClasse).orElse(null);
        if(sementeLaudoInspecao == null) {
            mensagens.add("Laudo de inspeção de campo não cadastrado");
        }
    }

    private void validarImovel(RePendente obj, List<String> mensagens) {
        Estabelecimento estabelecimento = obj.getEstabelecimento();
        Produtor produtor = obj.getProdutor();

        try {
        	if(obj.getImovel() != null) {
        		imovelService.validarImovelEntrada(obj.getImovel().getMatricula(), produtor.getCodProdutor(), estabelecimento.getCodigo());
        	}
        }
        catch (Exception e) {
            mensagens.add(e.getMessage());
        }
    }

    private void validarUsuario(RePendente obj, List<String> mensagens) {
        Estabelecimento estabelecimento = obj.getEstabelecimento();
        Usuario usuario = null;

        try {
            usuario = usuarioService.getUsuarioLogado();
        }
        catch (Exception e) {
            mensagens.add(e.getMessage());
        }

        if(usuario == null) {
            mensagens.add("Não foi encontrado o usuário logado");
            return;
        }

        ParametrosUsuarioEstabelecimento parametrosUsuarioEstabelecimento = parametroUsuarioEstabelecimentoRep.findByCodigoUsuarioAndCodigoEstabelecimento(usuario.getCodUsuario(), estabelecimento.getCodigo());
        if(parametrosUsuarioEstabelecimento == null) {
            mensagens.add("Usuário não pode fazer movimentações para esse estabelecimento, sem permissão em Usuários do Estabelecimento");
            return;
        }

        if(parametrosUsuarioEstabelecimento.getRe() == false) {
            mensagens.add("Usuário não pode fazer recebimento de produção nesse estabelecimento");
        }
    }
}
