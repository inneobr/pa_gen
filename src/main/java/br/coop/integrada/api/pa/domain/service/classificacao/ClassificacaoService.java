package br.coop.integrada.api.pa.domain.service.classificacao;

import static br.coop.integrada.api.pa.domain.enums.StatusIntegracao.INTEGRADO;
import static br.coop.integrada.api.pa.domain.enums.StatusIntegracao.INTEGRAR;
import static br.coop.integrada.api.pa.domain.enums.StatusIntegracao.PROCESSANDO;
import static br.coop.integrada.api.pa.domain.enums.TipoClassificacaoEnum.PH;
import static br.coop.integrada.api.pa.domain.enums.integration.OrigemInputEnum.ERP;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectFieldErrorsException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.aplication.utils.CompararObjetos;
import br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.TipoClassificacaoEnum;
import br.coop.integrada.api.pa.domain.enums.integration.SituacaoFuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.model.AbstractEntity;
import br.coop.integrada.api.pa.domain.model.cadastro.TipoClassificacao;
import br.coop.integrada.api.pa.domain.model.classificacao.Classificacao;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoDetalhe;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoEstabelecimento;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoGrupoProduto;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoSafra;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaFuncionalidade;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaHeader;
import br.coop.integrada.api.pa.domain.model.integration.IntegrationAuth;
import br.coop.integrada.api.pa.domain.model.produto.ProdutoReferencia;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.BuscaTabelaClassificacaoDto;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.ClassificacaoDetalheDto;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.ClassificacaoDto;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.ClassificacaoEstabelecimentoDto;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.ClassificacaoFilter;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.ClassificacaoGrupoProdutoDto;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.ClassificacaoSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.integration.ClassificacaoDetalheIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.integration.ClassificacaoGrupoProdutoIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.integration.ClassificacaoIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.integration.ClassificacaoIntegrationListDto;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.integration.ClassificacaoSafraIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.classificacao.integration.TipoClassificacaoIntegrationDto;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoDto;
import br.coop.integrada.api.pa.domain.repository.cadastro.TipoClassificacaoRep;
import br.coop.integrada.api.pa.domain.repository.classificacao.ClassificacaoDetalheRep;
import br.coop.integrada.api.pa.domain.repository.classificacao.ClassificacaoEstabelecimentoRep;
import br.coop.integrada.api.pa.domain.repository.classificacao.ClassificacaoGrupoProdutoRep;
import br.coop.integrada.api.pa.domain.repository.classificacao.ClassificacaoRep;
import br.coop.integrada.api.pa.domain.repository.classificacao.ClassificacaoSafraRep;
import br.coop.integrada.api.pa.domain.service.HistoricoGenericoService;
import br.coop.integrada.api.pa.domain.service.cadastro.TipoClassificacaoService;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;
import br.coop.integrada.api.pa.domain.service.produto.ProdutoReferenciaService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassificacaoService {

    @Autowired
    private ClassificacaoRep classificacaoRep;

    @Autowired
    private TipoClassificacaoService tipoClassificacaoService;

    @Autowired
    private HistoricoGenericoService historicoGenericoService;

    @Autowired
    private ProdutoReferenciaService produtoReferenciaService;

    @Autowired
    private IntegrationService integrationService;
    
    @Autowired
    private TipoClassificacaoRep tipoClassificacaoRep;
    
    @Autowired
    private ClassificacaoDetalheRep classificacaoDetalheRep;
    
    @Autowired
    private ClassificacaoSafraRep classificacaoSafraRep;
    
    @Autowired
    private ClassificacaoGrupoProdutoRep classificacaoGrupoProdutoRep;
    
    @Autowired
    private ClassificacaoEstabelecimentoRep classificacaoEstabelecimentoRep;
    
    @Autowired
    private TipoClassificacaoRep tipoClassificacaoRepository;

	@Value("${spring.profiles.active}")
	private String profileActive;
    

    private static BigDecimal diferencaTeorEntreDetalhe = BigDecimal.valueOf(0.1d);

    public Classificacao cadastrar(ClassificacaoDto objDto) {
        objDto.setId(null);
        validarVinculosDuplicados(objDto);

        Classificacao classificacao = ClassificacaoDto.converterDto(objDto);        
        classificacao.setStatusIntegracao(INTEGRAR);
        
        for(ClassificacaoSafra item : classificacao.getSafras()) { 
        	item.setId(null); 
        	item.setStatusIntegracao(INTEGRAR);
        }
        
        for(ClassificacaoDetalhe item : classificacao.getDetalhes()) { 
        	item.setId(null); 
        	item.setStatusIntegracao(INTEGRAR);
        }
        
        for(ClassificacaoGrupoProduto item : classificacao.getGrupoProdutos()) { 
        	item.setId(null); 
        	item.setStatusIntegracao(INTEGRAR);
        }
        
        for(ClassificacaoEstabelecimento item : classificacao.getEstabelecimentos()) { 
        	item.setId(null); 
        	item.setStatusIntegracao(INTEGRAR);
        }
        
        classificacao = salvar(classificacao);
        historicoGenericoService.salvar(
                classificacao.getId(),
                PaginaEnum.GRUPO_CLASSIFICACAO,
                "Inclusão de classificação",
                classificacao.getDescricao()
        );

        return classificacao;
    }
    
    private void validarVinculosDuplicados(ClassificacaoDto objDto) {
        List<String> vinculosDuplicados = getClassificacoesComMesmasInformacoes(objDto);
        if(vinculosDuplicados.size() > 0) {
            StringBuilder resultado = new StringBuilder();

            for (String vinculo : vinculosDuplicados) {
                resultado.append(vinculo).append("; ");
            }

            throw new RuntimeException("Já existe Grupo de Classificações com Safra X Grupo Produto X Estabelecimento cadastradas: " + resultado.toString());
        }
    }

    public Classificacao atualizarOld(ClassificacaoDto objDto) {
        if(objDto.getId() == null) {
            throw new RuntimeException("Para atualizar é necessário informar o ID do Grupo de Classificação!");
        }

        validarVinculosDuplicados(objDto);

        List<ClassificacaoDetalhe> detalhes = classificacaoRep.getReferenceById(objDto.getId()).getDetalhes();
        String diferencas = "Detalhes: \n";
        for(ClassificacaoDetalheDto detalhe : objDto.getDetalhes()) {
        	        	        	
            try {
                ClassificacaoDetalhe detalheCopia = detalhes.stream().filter(classificacaoDetalhe -> { return classificacaoDetalhe.getId() == detalhe.getId(); }).findFirst().orElse(null);
                if(detalheCopia != null) {
                	
                    ClassificacaoDetalheDto detalheCopiaDto = ClassificacaoDetalheDto.construir(detalheCopia);
                    diferencas += CompararObjetos.comparar(detalheCopiaDto, detalhe);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        Classificacao classificacaoNova = ClassificacaoDto.converterDto(objDto);
        Classificacao classificacaoAntiga = classificacaoRep.findById(classificacaoNova.getId()).orElse(null);
        
        //Verificar alterações de Detalhes
        StatusIntegracao statusDetalhe = CompararObjetos.verificarListas(classificacaoAntiga.getDetalhes(), classificacaoNova.getDetalhes());
        
        //Verificar alterações de Safra
        StatusIntegracao statusSafra = CompararObjetos.verificarListas(classificacaoAntiga.getSafras(), classificacaoNova.getSafras());
        
        //Verificar alteracoes de GrupoProduto
        StatusIntegracao statusGrupoP = CompararObjetos.verificarListas(classificacaoAntiga.getGrupoProdutos(), classificacaoNova.getGrupoProdutos());
                
        //Verificar alterações de Estabelecimento
        StatusIntegracao statusEstab = CompararObjetos.verificarListas(classificacaoAntiga.getEstabelecimentos(), classificacaoNova.getEstabelecimentos());
                
        
        if(statusDetalhe.equals(StatusIntegracao.INTEGRAR) ||
        	statusSafra.equals(StatusIntegracao.INTEGRAR) ||
        	statusGrupoP.equals(StatusIntegracao.INTEGRAR) ||
        	statusEstab.equals(StatusIntegracao.INTEGRAR)) {
        	
        	classificacaoNova.setStatusIntegracao(INTEGRAR);
        	classificacaoNova.setDataIntegracao(null);
        	classificacaoNova.setDataAtualizacao(new Date());
        }
        
        classificacaoNova = salvar(classificacaoNova);
        
        
        historicoGenericoService.salvar(
        		classificacaoNova.getId(),
                PaginaEnum.GRUPO_CLASSIFICACAO,
                "Alteração de classificação",
                diferencas
        );

        return classificacaoNova;
    }
    
    public Classificacao atualizar(ClassificacaoDto objDto) {
        if(objDto.getId() == null) {
            throw new RuntimeException("Para atualizar é necessário informar o ID do Grupo de Classificação!");
        }

        validarVinculosDuplicados(objDto);

        //List<ClassificacaoDetalhe> detalhes = classificacaoRep.getReferenceById(objDto.getId()).getDetalhes();
        StringBuilder diferencas = new StringBuilder();
        diferencas.append("Detalhes: ");
                

        Classificacao classificacaoNova = ClassificacaoDto.converterDto(objDto);
        Classificacao classificacaoAntiga = classificacaoRep.findById(classificacaoNova.getId()).orElse(null);
        
        if(!classificacaoAntiga.getDescricao().equals(classificacaoNova.getDescricao())) {
        	diferencas.append("A descrição foi alterada de : " + classificacaoAntiga.getDescricao() + " para: " + classificacaoNova.getDescricao() );
        }
        
        String modDetalhes = verificarModificacoesDetalhes(classificacaoAntiga.getDetalhes(), classificacaoNova.getDetalhes());
        String modSafra = verificarModificacoesSafra(classificacaoAntiga.getSafras(), classificacaoNova.getSafras());
        String modGrupoProduto = verificarModificacoesGrupoProduto(classificacaoAntiga.getGrupoProdutos(), classificacaoNova.getGrupoProdutos());
        String modEstabelecimentos = verificarModificacoesEstabelecimento(classificacaoAntiga.getEstabelecimentos(), classificacaoNova.getEstabelecimentos());
        
        diferencas.append(modDetalhes);
        diferencas.append(modSafra);
        diferencas.append(modGrupoProduto);
        diferencas.append(modEstabelecimentos);
        
        System.out.println(diferencas.toString());
        
        //Verificar alterações de Detalhes
        StatusIntegracao statusDetalhe = CompararObjetos.verificarListas(classificacaoAntiga.getDetalhes(), classificacaoNova.getDetalhes());
        
        //Verificar alterações de Safra
        StatusIntegracao statusSafra = CompararObjetos.verificarListas(classificacaoAntiga.getSafras(), classificacaoNova.getSafras());
        
        //Verificar alteracoes de GrupoProduto
        StatusIntegracao statusGrupoP = CompararObjetos.verificarListas(classificacaoAntiga.getGrupoProdutos(), classificacaoNova.getGrupoProdutos());
                
        //Verificar alterações de Estabelecimento
        StatusIntegracao statusEstab = CompararObjetos.verificarListas(classificacaoAntiga.getEstabelecimentos(), classificacaoNova.getEstabelecimentos());
                
        
        if(statusDetalhe.equals(StatusIntegracao.INTEGRAR) ||
        	statusSafra.equals(StatusIntegracao.INTEGRAR) ||
        	statusGrupoP.equals(StatusIntegracao.INTEGRAR) ||
        	statusEstab.equals(StatusIntegracao.INTEGRAR)) {
        	
        	classificacaoNova.setStatusIntegracao(INTEGRAR);
        	classificacaoNova.setDataIntegracao(null);
        	classificacaoNova.setDataAtualizacao(new Date());
        }
        
        classificacaoNova = salvar(classificacaoNova);
        
        
        historicoGenericoService.salvar(
        		classificacaoNova.getId(),
                PaginaEnum.GRUPO_CLASSIFICACAO,
                "Alteração de classificação",
                diferencas.toString()
        );

        return classificacaoNova;
    }

    public Classificacao salvar(Classificacao classificacao) {        

        // Vincular referências
        for(ClassificacaoDetalhe item : classificacao.getDetalhes()) {
            if(item.getProdutoReferencia() != null) {
                ProdutoReferencia produtoReferencia = item.getProdutoReferencia();
                produtoReferencia = produtoReferenciaService.buscarPorCodigoReferencia(produtoReferencia.getCodRef());
                item.setProdutoReferencia(produtoReferencia);
            }
        }
        
        //Ordena a lista de Detalhes
        if(classificacao.getTipoClassificacao().getPh()) {
        	Collections.sort(classificacao.getDetalhes(), Comparator.comparing(ClassificacaoDetalhe::getPhEntrada)
        														.thenComparing(ClassificacaoDetalhe::getTeorClassificacaoInicial));
        }
        else {
        	Collections.sort(classificacao.getDetalhes(), Comparator.comparing(ClassificacaoDetalhe::getTeorClassificacaoInicial));
        }
        
        
        validarDetalhes(classificacao);
        validarSafras(classificacao);

        //Salva ou Atualiza o obj Grupo de Classificação
        return classificacaoRep.save(classificacao);
    }

    @SuppressWarnings("unused")
	public Classificacao buscarPorId(Long id) {
        Classificacao obj = classificacaoRep.findById(id).orElse(null);
        
        if(obj.getTipoClassificacao().getPh()) {
        	Collections.sort(obj.getDetalhes(), Comparator.comparing(ClassificacaoDetalhe::getPhEntrada)
        			.thenComparing(ClassificacaoDetalhe::getTeorClassificacaoInicial));
        }
        else {
        	Collections.sort(obj.getDetalhes(), Comparator.comparing(ClassificacaoDetalhe::getTeorClassificacaoInicial));
        }

        if(obj == null) {
            throw new NullPointerException("A classificação não foi encontrado! ID: " + id + ", Tipo: " + Classificacao.class.getName());
        }

        return obj;
    }

    public Page<ClassificacaoSimplesDto> buscarPorPagina(Pageable pageable, ClassificacaoFilter filter, Situacao situacao) {
        Page<Classificacao> classificacaoPage = classificacaoRep.findAll(pageable, filter, situacao);
        List<ClassificacaoSimplesDto> grupoClassificacao = ClassificacaoSimplesDto.construir(classificacaoPage.getContent());

        return new PageImpl<>(
                grupoClassificacao,
                pageable,
                classificacaoPage.getTotalElements()
        );
    }    

    public void inativar(Long id) {
        Classificacao obj = buscarPorId(id);
        obj.setDataInativacao(new Date());
        obj.setStatusIntegracao(INTEGRAR);
        classificacaoRep.save(obj);
        historicoGenericoService.salvar(
                obj.getId(),
                PaginaEnum.GRUPO_CLASSIFICACAO,
                "Inativar classificação",
                obj.getDescricao()
        );
    }
        
    
    public void excluir(Long id) {
        Classificacao obj = buscarPorId(id);
        
        if(obj == null) {
        	throw new ObjectNotFoundException("Não foi encontrado um registro de Grupo de Classificação com o Id: " + id);
        }
        
        Long idGrupoClassificacao = obj.getId();
        String descricao = obj.getDescricao();
        
        historicoGenericoService.salvar(
                idGrupoClassificacao,
                PaginaEnum.GRUPO_CLASSIFICACAO,
                "Excluir Grupo de Classificação",
                descricao
        );
        
        classificacaoRep.delete(obj);
                
    }

    public void ativar(Long id) {
        Classificacao obj = buscarPorId(id);
        obj.setDataInativacao(null);
        ClassificacaoDto objDto = ClassificacaoDto.construir(obj);
        atualizar(objDto);
        historicoGenericoService.salvar(
                obj.getId(),
                PaginaEnum.GRUPO_CLASSIFICACAO,
                "Ativar classificação",
                obj.getDescricao()
        );
    }

    public List<Integer> buscarSafras() {
    	List<Integer> safras = classificacaoRep.buscarSafras();   	 
    	Integer ano = LocalDate.now().getYear(); 
    	
        if(safras == null || safras.isEmpty()) {             
            safras.add(ano-1);
            safras.add(ano);
            safras.add(ano+1);
        }else {
           safras.add(safras.get(safras.size()-1)-1);
        }
        return safras;
    }
    
    public List<Integer> buscarSafras1996() {
    	List<Integer> safras = classificacaoRep.buscarSafras();   	 
       
        if(safras == null || safras.isEmpty()) { 
            Integer ano = LocalDate.now().getYear(); 
            safras.add(ano-1);
            safras.add(ano);
            safras.add(ano+1);
        }else {
            safras.add(0, safras.get(0)+1);
        }
        
        Integer safraAux = safras.get(safras.size()-1);
        
        while(safraAux > 1996) {
        	safraAux--;
        	safras.add(safraAux);
        }
                
        return safras;
    }

    private void validarDetalhes(Classificacao obj) {
        Long idTipoClassificacao = obj.getTipoClassificacao().getId();
        TipoClassificacao tipoClassificacao = tipoClassificacaoService.unico(idTipoClassificacao).orElse(null);

        if(tipoClassificacao == null) {
            throw new ObjectNotFoundException("Não foi encontrado tipo de classificação com o ID: " + idTipoClassificacao + ", Tipo: " + TipoClassificacao.class.getName());
        }

        Boolean teorPorFaixa = tipoClassificacao.getTeorPorFaixa();
        BigDecimal ultimoTeor = null;
        BigInteger phEntradaAnterior = null;
               

        List<FieldErrorItem> fieldErrorItems = new ArrayList<>();
        
        //Collections.sort(obj.getDetalhes());
                
        for(ClassificacaoDetalhe item : obj.getDetalhes()) {
        	
        	//System.out.println("Teor Inicial: " + item.getTeorClassificacaoInicial());
        	
        	if(item.getDataInativacao() != null){
        		continue;
        	}
        	
            try {
                ultimoTeor = validarRangeDetalhes(item, teorPorFaixa, ultimoTeor, tipoClassificacao.getPh(), phEntradaAnterior);
            }
            catch (RuntimeException e) {
                fieldErrorItems.add(FieldErrorItem.construir("Teor Inicial", e.getMessage()));
                ultimoTeor = getUtimoTeorDetalhe(item, teorPorFaixa);
            }

            fieldErrorItems.addAll(validarCamposDetalhes(item, tipoClassificacao));
            
            //Se for do tipo PH, precisamos validar a coluna phEntrada
            if(tipoClassificacao.getPh()) {
            	//Armazeno o valor do ultimo Ph para verificar com o proximo que virá da lista
        		phEntradaAnterior = item.getPhEntrada();
        	}
            
        }

        if(fieldErrorItems.size() > 0) {
            throw new ObjectFieldErrorsException("Validação dos campos dos detalhes da classificação", fieldErrorItems);
        }
    }

    private List<FieldErrorItem> validarCamposDetalhes(ClassificacaoDetalhe obj, TipoClassificacao tipo) {
        List<FieldErrorItem> fieldErrorItems = new ArrayList<>();

        if(tipo.getResultadoDesconto() && obj.getPercentualDesconto() == null) {
            fieldErrorItems.add(FieldErrorItem.construir(
                    "Desconto",
                    "O campo {percentualDesconto} é obrigatório! | Teor Inicial: " + obj.getTeorClassificacaoInicial())
            );
        }

        if(tipo.getResultadoTaxaSecagemKg() && obj.getTaxaSecagemQuilo() == null) {
            fieldErrorItems.add(FieldErrorItem.construir(
                    "Taxa Secagem Quilo",
                    "O campo {taxaSecagemQuilo} é obrigatório! | Teor Inicial: " + obj.getTeorClassificacaoInicial())
            );
        }

        if(tipo.getResultadoTaxaSecagemValor() && obj.getTaxaSecagemValor() == null) {
            fieldErrorItems.add(FieldErrorItem.construir(
                    "Taxa Secagem Valor",
                    "O campo {taxaSecagemValor} é obrigatório! | Teor Inicial: " + obj.getTeorClassificacaoInicial())
            );
        }

        if(tipo.getPh() && obj.getPhEntrada() == null) {
            fieldErrorItems.add(FieldErrorItem.construir(
                    "PH",
                    "O campo {phEntrada} é obrigatório! | Teor Inicial: " + obj.getTeorClassificacaoInicial())
            );
        }

        if(tipo.getPhCorrigido() && obj.getPhCorrigido() == null) {
            fieldErrorItems.add(FieldErrorItem.construir(
                    "PH Corrigido",
                    "O campo {phCorrigido} é obrigatório! | Teor Inicial: " + obj.getTeorClassificacaoInicial())
            );
        }

        return fieldErrorItems;
    }

    private BigDecimal validarRangeDetalhes(ClassificacaoDetalhe obj, Boolean teorPorFaixa, BigDecimal ultimoTeor, Boolean validaPh, BigInteger phEntrada) {
        BigDecimal teorInicial = obj.getTeorClassificacaoInicial();

        if(ultimoTeor == null) {
            return getUtimoTeorDetalhe(obj, teorPorFaixa);
        }
        else {
        	
        	if(validaPh && phEntrada == null) {
        		return getUtimoTeorDetalhe(obj, teorPorFaixa);
        	}
        	else{
        		//Se o PH anterior não for igual ao Atual então:
        		if(validaPh && !phEntrada.equals(obj.getPhEntrada())){
        			//Mudou o Teor Inicial e não haverá a diferença de 0.1
        			return getUtimoTeorDetalhe(obj, teorPorFaixa);
        		}
        		else {
		            if(teorInicial.subtract(ultimoTeor).equals(diferencaTeorEntreDetalhe)) {
		                return getUtimoTeorDetalhe(obj, teorPorFaixa);
		            }
		            else {
		                throw new RuntimeException("Possui uma diferença entre o Teor Final (" + ultimoTeor + ") do detalhe anterior com o Teor Inicial (" + teorInicial + ")");
		            }
        		}
        	}
        }
    }

    private BigDecimal getUtimoTeorDetalhe(ClassificacaoDetalhe detalhe, Boolean teorPorFaixa) {
        BigDecimal teorInicial = detalhe.getTeorClassificacaoInicial();
        BigDecimal teorFinal = detalhe.getTeorClassificacaoFinal();
        

        if(teorPorFaixa) {
            if(teorInicial.compareTo(teorFinal) == 1) {
                throw new RuntimeException("O teor inicial (" + teorInicial + ") é maior que o teor final (" + teorFinal + ")");
            }

            return teorFinal;
        }

        return teorInicial;
    }

    private List<String> getClassificacoesComMesmasInformacoes(ClassificacaoDto objDto) {
        Long idTipoClassificacao = objDto.getTipoClassificacao().getId();
        TipoClassificacao tipoClassificacao = tipoClassificacaoService.unico(idTipoClassificacao).orElse(null);

        if(tipoClassificacao == null) {
            throw new ObjectNotFoundException("Não foi encontrado tipo de classificação com o ID: " + idTipoClassificacao + ", Tipo: " + TipoClassificacao.class.getName());
        }

        List<Long> idGupoProdutos = new ArrayList<>();
        List<Long> idEstabelecimentos = new ArrayList<>();
        List<Integer> safras = objDto.getSafras().stream().map(classificacaoSafraDto -> {
            return classificacaoSafraDto.getSafra();
        }).collect(Collectors.toList());

        for(ClassificacaoGrupoProdutoDto item : objDto.getGrupoProdutos()) {
            idGupoProdutos.add(item.getIdGrupoProduto());
        }
        
        for(ClassificacaoEstabelecimentoDto item : objDto.getEstabelecimentos() ) {
            idEstabelecimentos.add(item.getIdEstabelecimento());
        }

        if(objDto.getId() == null) {
            objDto.setId(0l);
        }

        if(tipoClassificacao.getControlePorSafra()) {
            return classificacaoRep.buscarPorSafraGrupoProdutoEstabelecimento(
                    objDto.getId(),
                    safras,
                    idGupoProdutos,
                    idEstabelecimentos,
                    tipoClassificacao.getTipoClassificacao().name()
            );
        }

        return classificacaoRep.buscarPorGrupoProdutoEstabelecimento(
                objDto.getId(),
                idGupoProdutos,
                idEstabelecimentos,
                tipoClassificacao.getTipoClassificacao().name()
        );
    }

    private void validarSafras(Classificacao obj) {
        Long idTipoClassificacao = obj.getTipoClassificacao().getId();
        TipoClassificacao tipoClassificacao = tipoClassificacaoService.unico(idTipoClassificacao).orElse(null);

        if(tipoClassificacao == null) {
            throw new ObjectNotFoundException("Não foi encontrado tipo de classificação com o ID: " + idTipoClassificacao + ", Tipo: " + TipoClassificacao.class.getName());
        }

        if(tipoClassificacao.getControlePorSafra() && (obj.getSafras() == null || obj.getSafras().isEmpty())) {
            throw new RuntimeException("O tipo de classificação informado necessita das safras.");
        }
    }
        
    public void alterarStatusIntegracao(StatusIntegracao status, List<Classificacao> objs) {
    	
    	if(CollectionUtils.isEmpty(objs)) return;
    	
    	List<Classificacao> classificacoes = objs.stream().map(classificacao -> {
    		    		
    		if(!CollectionUtils.isEmpty(classificacao.getSafras())) {
    			
    			for(ClassificacaoSafra safra : classificacao.getSafras()) {
    				if(safra.getStatusIntegracao() == null || safra.getStatusIntegracao().equals(StatusIntegracao.INTEGRAR) || safra.getStatusIntegracao().equals(StatusIntegracao.PROCESSANDO)) {
    					safra.setStatusIntegracao(status);
    					safra.setDataIntegracao(null);
    				}
    			}
    		}
    			
			for(ClassificacaoDetalhe detalhe : classificacao.getDetalhes()) {
				if(detalhe.getStatusIntegracao() == null || detalhe.getStatusIntegracao().equals(StatusIntegracao.INTEGRAR) || detalhe.getStatusIntegracao().equals(StatusIntegracao.PROCESSANDO)) {
					detalhe.setStatusIntegracao(status);
					detalhe.setDataIntegracao(null);
				}
			}
			
			for(ClassificacaoGrupoProduto grupo : classificacao.getGrupoProdutos()) {
				if(grupo.getStatusIntegracao() == null || grupo.getStatusIntegracao().equals(StatusIntegracao.INTEGRAR) || grupo.getStatusIntegracao().equals(StatusIntegracao.PROCESSANDO)) {
					grupo.setStatusIntegracao(status);
					grupo.setDataIntegracao(null);
				}
			}
			
			for(ClassificacaoEstabelecimento estabelecimento : classificacao.getEstabelecimentos()) {
				if(estabelecimento.getStatusIntegracao() == null || estabelecimento.getStatusIntegracao().equals(StatusIntegracao.INTEGRAR) || estabelecimento.getStatusIntegracao().equals(StatusIntegracao.PROCESSANDO)) {
					estabelecimento.setStatusIntegracao(status);
					estabelecimento.setDataIntegracao(null);
				}
			}
    			
    		    		
    		classificacao.setDataIntegracao(null);
    		classificacao.setStatusIntegracao(status);
    		return classificacao;
    		
    	}).toList();
    	
    	classificacaoRep.saveAll(classificacoes);
    	classificacaoRep.flush();
    }
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void sincronizarClassificacoes(Long id) {
    	IntegracaoPagina pagina = integrationService.buscarPorPagina(PaginaEnum.GRUPO_CLASSIFICACAO, FuncionalidadePaginaEnum.INT_LOTE_GRUPO_CLASSIFICACAO);
    	if(pagina == null || ERP.equals(pagina.getOrigenEnum())) return;     	

		IntegracaoPaginaFuncionalidade paginaFuncionalidade = pagina.getFuncionalidade(FuncionalidadePaginaEnum.INT_LOTE_GRUPO_CLASSIFICACAO);
		if(paginaFuncionalidade == null || paginaFuncionalidade.getSituacao() == null || paginaFuncionalidade.getSituacao().equals(SituacaoFuncionalidadePaginaEnum.INATIVO)) return;
    	
		List<TipoClassificacao> tipos = null;
    	List<Classificacao> classificacoes = null;
    	
    	
    	try {
    		
    		tipos = tipoClassificacaoService.buscarTiposSemIntegracao(); //Lista todos que nao estão integrados
    		tipoClassificacaoService.alterarStatusIntegracao(PROCESSANDO, tipos);
    		
    		if(id == null) {

    			classificacoes = classificacaoRep.findByStatusIntegracao(INTEGRAR);
    		}else {
    			Classificacao classificacao = classificacaoRep.findById(id).orElse(null);

    			if(classificacao != null) {
    				classificacoes = new ArrayList<>();
    				classificacoes.add(classificacao);
    			}
    		}
    		
        	alterarStatusIntegracao(PROCESSANDO, classificacoes);
        	
        	if(CollectionUtils.isEmpty(tipos) && CollectionUtils.isEmpty(classificacoes)) return;
        	
        	ClassificacaoIntegrationListDto objDto = ClassificacaoIntegrationListDto.construir(classificacoes, tipos);
        	
        	System.out.println(objDto);
        
    		IntegrationAuth auth = integrationService.buscarPelaDescricao(pagina.getAuth(profileActive).getDescricao());
    		
			LinkedMultiValueMap mvmap = new LinkedMultiValueMap<>();    		
    		if(pagina.getHeaders() != null) {
	    		for(IntegracaoPaginaHeader header: pagina.getHeaders()) {
	    			if(pagina.isHearderProfileActive(header, profileActive)) {
		    			if(!Strings.isEmpty(header.getChave()) && !Strings.isEmpty(header.getValor())){
		    				mvmap.add(header.getChave(), header.getValor());
		    			}
	    			}	
	    		}	
    		}
    		
    		String url = pagina.getUrlPrincipalApi(profileActive);
    		if(url == null) return;
    		
    		WebClient client = WebClient.builder().baseUrl(url)
    				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    				.filter(ExchangeFilterFunctions.basicAuthentication(auth.getLogin(), auth.getSenha()))
    				.build();
    		
    		HttpMethod httpMethod = IntegrationService.getMetodo(paginaFuncionalidade);

    		Consumer<HttpHeaders> consumer = it -> it.addAll(mvmap);
    		client.method(httpMethod)
    		.uri(paginaFuncionalidade.getEndPointSend())
			.headers(consumer)
    		.body(BodyInserters.fromValue(objDto))
    		.retrieve()
    		.bodyToFlux(ClassificacaoIntegrationListDto.class)
    		.subscribe(classificacaoList -> {
    			atualizarStatusRetornoIntegracaoComErp(classificacaoList.getClassificacoes(), classificacaoList.getTipoClassificacoes() );
    		});
    	}
    	catch (Exception e) {
    		if(!CollectionUtils.isEmpty(tipos)) 
    			tipoClassificacaoService.alterarStatusIntegracao(INTEGRAR, tipos);
    		
    		if(!CollectionUtils.isEmpty(classificacoes)) 
    			alterarStatusIntegracao(INTEGRAR, classificacoes);
    		
    		e.printStackTrace();
    		return;
    	}
    }
    
    public void atualizarStatusRetornoIntegracaoComErp(
    		List<ClassificacaoIntegrationDto> classificacoesList,
    		List<TipoClassificacaoIntegrationDto> tiposClassificacoes) {
    	
    	if(classificacoesList != null) {
	    	for(ClassificacaoIntegrationDto classificacaoDto : classificacoesList) {
				
				try {
					
					StatusIntegracao statusIntegracaoCabecalho = classificacaoDto.getIntegrated() ? INTEGRADO : INTEGRAR;
					
					if(classificacaoDto.getIdAgrupamento() != null) {
						Date dataIntegracao = statusIntegracaoCabecalho.equals(INTEGRADO) ? new Date() : null;
						//classificacaoRep.updateStatusIntegracao(dataIntegracao, classificacaoDto.getIdAgrupamento());
						
						Classificacao objClassificacao = classificacaoRep.findById(classificacaoDto.getIdAgrupamento()).orElse(null);
						
						if(objClassificacao == null)
							throw new ObjectNotFoundException("");
						
						objClassificacao.setDataIntegracao(dataIntegracao);
						objClassificacao.setStatusIntegracao(statusIntegracaoCabecalho);
						
						classificacaoRep.save(objClassificacao);
						
						classificacaoRep.flush();
					}
					
					
				}
				catch (Exception e) {
					System.out.println("Erro no retorno da sincronização de classificações: "+
							classificacaoDto.getIdAgrupamento() +" -> "+ e.getMessage());
				}
			}
    	}
    	
    	if(tiposClassificacoes != null) {
	    	for(TipoClassificacaoIntegrationDto tipoClassificacaoDto: tiposClassificacoes) {
				try {
					
					StatusIntegracao statusIntegracaoCabecalho = tipoClassificacaoDto.getIntegrated() ? INTEGRADO : INTEGRAR;
					Date dataIntegracao = statusIntegracaoCabecalho.equals(INTEGRADO) ? new Date() : null;
						
					TipoClassificacao tipoClassificacao = tipoClassificacaoRepository.findByTipoClassificacao(tipoClassificacaoDto.getCodigo()).orElse(null);
						
					if(tipoClassificacao == null)
						throw new ObjectNotFoundException("");
					
					tipoClassificacao.setDataIntegracao(dataIntegracao);
					tipoClassificacao.setStatusIntegracao(statusIntegracaoCabecalho);
	
					tipoClassificacaoRepository.save(tipoClassificacao);
					
				}
				catch (Exception e) {
					System.out.println("Erro no retorno da sincronização de classificações: "+
							tipoClassificacaoDto.getDescricao() +" -> "+ e.getMessage());
				}
			}
    	}
    }
	
	public BuscaTabelaClassificacaoDto buscarTabelaClassificacaoDetalhe(TipoClassificacaoEnum tipoClassificacaoEnum,
			String codigoEstabelecimento, Long idGrupoProduto, Integer safra, BigInteger phEntrada,
			BigDecimal teorClassificacao) {
		
		TipoClassificacao tipoClassificacao =  tipoClassificacaoRep.findByTipoClassificacaoAndDataInativacaoIsNull(tipoClassificacaoEnum);		
		List<ClassificacaoDetalhe> detalhes = null;
		
		if(tipoClassificacao.getControlePorSafra()) {
			//Safra
			detalhes = classificacaoDetalheRep.buscarTabelaClassificacao(tipoClassificacaoEnum.name(), codigoEstabelecimento, idGrupoProduto, safra);
		}
		else {
			//Sem Safra
			detalhes = classificacaoDetalheRep.buscarTabelaClassificacao(tipoClassificacaoEnum.name(), codigoEstabelecimento, idGrupoProduto);
		}
		
		if(tipoClassificacaoEnum.equals(PH)) {
			for (ClassificacaoDetalhe detalhe : detalhes) {
				Boolean isTeorClassificacaoMaiorOuIgualTeorInicial = teorClassificacao.compareTo(detalhe.getTeorClassificacaoInicial()) >= 0;
				Boolean isTeorClassificacaoMenorOuIgualTeorFinal = teorClassificacao.compareTo(detalhe.getTeorClassificacaoFinal()) <= 0;
				
				Boolean isPhEntradaIgualPhEntradaParam = detalhe.getPhEntrada().compareTo(phEntrada) == 0;
				
				if(isTeorClassificacaoMaiorOuIgualTeorInicial &&
						isTeorClassificacaoMenorOuIgualTeorFinal &&
						isPhEntradaIgualPhEntradaParam) {
					return BuscaTabelaClassificacaoDto.construir(true, detalhe);
				}
			}
		}
		else {
			for (ClassificacaoDetalhe detalhe : detalhes) {
				Boolean isTeorClassificacaoMaiorOuIgualTeorInicial = teorClassificacao.compareTo(detalhe.getTeorClassificacaoInicial()) >= 0;
				Boolean isTeorClassificacaoMenorOuIgualTeorFinal = teorClassificacao.compareTo(detalhe.getTeorClassificacaoFinal()) <= 0;
				
				if(isTeorClassificacaoMaiorOuIgualTeorInicial &&
						isTeorClassificacaoMenorOuIgualTeorFinal) {
					return BuscaTabelaClassificacaoDto.construir(true, detalhe);
				}
			}
		}
		
		return BuscaTabelaClassificacaoDto.construir(false, null);
	}

	public Page<EstabelecimentoDto> buscarIdGrupoClassificacaoEstabelecimentos(Long id, Pageable pageable) {
			
		Classificacao classificacao = classificacaoRep.findById(id).orElse(null);
		
		Page<ClassificacaoEstabelecimento> classificacaoEstabelecimentos = classificacaoEstabelecimentoRep.findByClassificacaoOrderById(classificacao,  pageable);
		
		List<EstabelecimentoDto> estabelecimentosDtoList = new ArrayList<>();
		
		for(ClassificacaoEstabelecimento cEstabelecimento: classificacaoEstabelecimentos.getContent()){			
			EstabelecimentoDto estabelecimentoDto = new EstabelecimentoDto();
			BeanUtils.copyProperties(cEstabelecimento.getEstabelecimento(), estabelecimentoDto);
			estabelecimentosDtoList.add(estabelecimentoDto);
		}
		
		Collections.sort(estabelecimentosDtoList, Comparator.comparing(EstabelecimentoDto::getCodigo));
		
		Page<EstabelecimentoDto> estabelecimentoDtoPage = new PageImpl<>(
				estabelecimentosDtoList, pageable, classificacaoEstabelecimentos.getTotalElements()
		);

		return estabelecimentoDtoPage;
	}
	
	public static <T> String verificarModificacoesDetalhes(List<ClassificacaoDetalhe> listaAntiga, List<ClassificacaoDetalhe> listaNova){
    	
		StringBuilder stringBuilder = new StringBuilder();
    	
		//Verifica se o item da lista antiga com a da lista nova teve alguma modificação
    	for (ClassificacaoDetalhe objAntigo : listaAntiga) {
    		
    		BigDecimal teorInicialAntigo = objAntigo.getTeorClassificacaoInicial();
    			
    		for (ClassificacaoDetalhe objNovo : listaNova) {
	    			
    			BigDecimal teorInicialNovo = objNovo.getTeorClassificacaoInicial();
	    			
    			//Verifica se os objetos tem o mesmo teorInicial
    			if(teorInicialAntigo.equals(teorInicialNovo)) {
    	    				
	    			try {
	    				String alteracoes = CompararObjetos.comparar(objAntigo, objNovo);
    	    				
	    				if(!alteracoes.isEmpty()) {
	    					stringBuilder.append("O Teor Inicial: " + teorInicialNovo +" "+ alteracoes + System.getProperty("line.separator"));
	    				}
    	    				
					} 
	    			catch (Exception e) {
						e.printStackTrace();
					}
				} 
	    			
    		}
    	}
    	
    	//Verificar Itens Excluídos (Existem na Lista Antiga e Não existem na Lista Nova)
    	for (ClassificacaoDetalhe objAntigo : listaAntiga) {
			
    		BigDecimal teorInicialAntigo = objAntigo.getTeorClassificacaoInicial();
    		
    		boolean encontrou = false;
    		
    		for (ClassificacaoDetalhe objNovo : listaNova) {
    				
    			BigDecimal teorInicialNovo = objNovo.getTeorClassificacaoInicial();
    			
    			if(teorInicialAntigo.equals(teorInicialNovo)) {
    				encontrou = true;
    			}
    			
    		}
    		
    		if(!encontrou) {
    			stringBuilder.append("O Teor Inicial: " + teorInicialAntigo + " foi excluído." + System.getProperty("line.separator")); 
    		}
    	}
    	
    	//Verificar Itens Incluídos (Existem na Lista Nova e Não existem na Lista Antiga)
    	for (ClassificacaoDetalhe objNovo : listaNova) {
			
    		BigDecimal teorInicialNovo = objNovo.getTeorClassificacaoInicial();
    		
    		boolean encontrou = false;
    		
    		for (ClassificacaoDetalhe objAntigo : listaAntiga) {
	    			
    			BigDecimal teorInicialAntigo = objAntigo.getTeorClassificacaoInicial();
    			
    			if(teorInicialNovo.equals(teorInicialAntigo)) {
    				encontrou = true;
    			}
    		}
    		
    		if(!encontrou) {
    			stringBuilder.append("O Teor Inicial: " + teorInicialNovo + " foi Incluído." + System.getProperty("line.separator")); 
    		}
    		
    	}
    	
    	return stringBuilder.toString();
    	
    }
	
	public static <T> String verificarModificacoesSafra(List<ClassificacaoSafra> listaAntiga, List<ClassificacaoSafra> listaNova){
    	
		StringBuilder stringBuilder = new StringBuilder();
    	
    	//Verificar Itens Excluídos (Existem na Lista Antiga e Não existem na Lista Nova)
    	for (ClassificacaoSafra objAntigo : listaAntiga) {
			
    		Integer safraAntiga = objAntigo.getSafra();
    		   		
    		boolean encontrou = false;
    		
    		for (ClassificacaoSafra objNovo : listaNova) {
    			
    			Integer safraNova = objNovo.getSafra();
    			
    			if(safraAntiga.equals(safraNova)) {
    				encontrou = true;
    			}
    		}
    		
    		if(!encontrou) {
    			stringBuilder.append("A safra: " + safraAntiga + " foi removida deste Grupo de Classificação." + System.getProperty("line.separator")); 
    		}
    		
    	}
    	
    	//Verificar Itens Incluídos (Existem na Lista Nova e Não existem na Lista Antiga)
    	for (ClassificacaoSafra objNovo : listaNova) {
			
    		Integer safraNova = objNovo.getSafra();
    		
    		boolean encontrou = false;
    		
    		for (ClassificacaoSafra objAntigo : listaAntiga) {
    			Integer safraAntiga = objAntigo.getSafra();
    			
    			if(safraNova.equals(safraAntiga)) {
    				encontrou = true;
    			}
    		}
    		
    		if(!encontrou) {
    			stringBuilder.append("A safra: " + safraNova + " foi Incluída neste Grupo de Classificação." + System.getProperty("line.separator")); 
    		}
    	}
    	
    	return stringBuilder.toString();
    	
    }
	
	public static <T> String verificarModificacoesGrupoProduto(List<ClassificacaoGrupoProduto> listaAntiga, List<ClassificacaoGrupoProduto> listaNova){
    	
		StringBuilder stringBuilder = new StringBuilder();
    	
    	//Verificar Itens Excluídos (Existem na Lista Antiga e Não existem na Lista Nova)
    	for (ClassificacaoGrupoProduto objAntigo : listaAntiga) {
			
    		String fmCodigoAntigo = objAntigo.getGrupoProduto().getFmCodigo();
    		
    		boolean encontrou = false;
    		
    		for (ClassificacaoGrupoProduto objNovo : listaNova) {
    			
    			String fmCodigoNovo = objNovo.getGrupoProduto().getFmCodigo();
    			
    			if(fmCodigoAntigo.equals(fmCodigoNovo)) {
    				encontrou = true;
    			}
    		}
    		
    		if(!encontrou) {
    			stringBuilder.append("O Grupo de Produto: " + fmCodigoAntigo + " foi removido." + System.getProperty("line.separator")); 
    		}
    	}
    	
    	//Verificar Itens Incluídos (Existem na Lista Nova e Não existem na Lista Antiga)
    	for (ClassificacaoGrupoProduto objNovo : listaNova) {
			
    		String fmCodigoNovo = objNovo.getGrupoProduto().getFmCodigo();
    		
    		boolean encontrou = false;
    		for (ClassificacaoGrupoProduto objAntigo : listaAntiga) {
    			
    			
    			String fmCodigoAntigo = objAntigo.getGrupoProduto().getFmCodigo();
    			
    			if(fmCodigoNovo.equals(fmCodigoAntigo)) {
    				encontrou = true;
    			}
    		}
    		
    		if(!encontrou) {
    			stringBuilder.append("O Grupo de Produto: " + fmCodigoNovo + " foi Incluído." + System.getProperty("line.separator")); 
    		}
    	}
    	
    	return stringBuilder.toString();
    	
    }
	
	public static <T> String verificarModificacoesEstabelecimento(List<ClassificacaoEstabelecimento> listaAntiga, List<ClassificacaoEstabelecimento> listaNova){
    	
		StringBuilder stringBuilder = new StringBuilder();
    	
    	//Verificar Itens Excluídos (Existem na Lista Antiga e Não existem na Lista Nova)
    	for (ClassificacaoEstabelecimento objAntigo : listaAntiga) {
			
    		String codAntigo = objAntigo.getEstabelecimento().getCodigo();
    		
    		boolean encontrou = false;
    		
    		for (ClassificacaoEstabelecimento objNovo : listaNova) {
    			
    			String codNovo = objNovo.getEstabelecimento().getCodigo();
    			
    			if(codAntigo.equals(codNovo)) {
    				encontrou = true;
    			}
    		}
    		
    		if(!encontrou) {
    			stringBuilder.append("O Estabelecimento cod: " + codAntigo + " foi removido." + System.getProperty("line.separator")); 
    		}
    	}
    	
    	//Verificar Itens Incluídos (Existem na Lista Nova e Não existem na Lista Antiga)
    	for (ClassificacaoEstabelecimento objNovo : listaNova) {
			
    		String codigoNovo = objNovo.getEstabelecimento().getCodigo();
    		
    		boolean encontrou = false;
    		
    		for (ClassificacaoEstabelecimento objAntigo : listaAntiga) {
    			
    			
    			String fmCodigoAntigo = objAntigo.getEstabelecimento().getCodigo();
    			
    			if(codigoNovo.equals(fmCodigoAntigo)) {
    				encontrou = true;
    			}
    		}
    		if(!encontrou) {
    			stringBuilder.append("O Estabelecimento cod: " + codigoNovo + " foi Incluído." + System.getProperty("line.separator")); 
    		}
    	}
    	
    	return stringBuilder.toString();
    	
    }
	
}
