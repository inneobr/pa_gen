package br.coop.integrada.api.pa.domain.service.parametros;

import static br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum.INT_LOTE_TAXA_PRODUCAO;
import static br.coop.integrada.api.pa.domain.enums.PaginaEnum.TAXA;
import static br.coop.integrada.api.pa.domain.enums.PaginaEnum.TAXA_PRODUCAO;
import static br.coop.integrada.api.pa.domain.enums.StatusIntegracao.INTEGRADO;
import static br.coop.integrada.api.pa.domain.enums.StatusIntegracao.INTEGRAR;
import static br.coop.integrada.api.pa.domain.enums.StatusIntegracao.PROCESSANDO;
import static br.coop.integrada.api.pa.domain.enums.integration.OrigemInputEnum.ERP;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.lowagie.text.pdf.AcroFields.Item;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectFieldErrorsException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.aplication.utils.CompararObjetos;
import br.coop.integrada.api.pa.domain.enums.FuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.enums.integration.IntegrationOperacaoEnum;
import br.coop.integrada.api.pa.domain.enums.integration.SituacaoFuncionalidadePaginaEnum;
import br.coop.integrada.api.pa.domain.enums.integration.SituacaoIntegracaoLogEnum;
import br.coop.integrada.api.pa.domain.model.classificacao.ClassificacaoDetalhe;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoLog;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaFuncionalidade;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPaginaHeader;
import br.coop.integrada.api.pa.domain.model.integration.IntegrationAuth;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.Taxa;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaCarenciaArmazenagem;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaEstabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.taxa.TaxaGrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.TaxaCarenciaArmazenagemDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.TaxaDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.TaxaEstabelecimentoDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.TaxaFilter;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.TaxaGrupoProdutoDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.TaxaRetornoCadastroDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.TaxaSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.integration.TaxaIntegrationListDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.integration.TaxaIntegrationListResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.parametros.taxa.integration.TaxaIntegrationResponseDto;
import br.coop.integrada.api.pa.domain.repository.parametros.TaxaCarenciaArmazenagemRep;
import br.coop.integrada.api.pa.domain.repository.parametros.TaxaEstabelecimentoRep;
import br.coop.integrada.api.pa.domain.repository.parametros.TaxaGrupoProdutoRep;
import br.coop.integrada.api.pa.domain.repository.parametros.TaxaRep;
import br.coop.integrada.api.pa.domain.service.HistoricoGenericoService;
import br.coop.integrada.api.pa.domain.service.estabelecimento.EstabelecimentoService;
import br.coop.integrada.api.pa.domain.service.integration.IntegracaoLogService;
import br.coop.integrada.api.pa.domain.service.integration.IntegrationService;
import br.coop.integrada.api.pa.domain.service.produto.GrupoProdutoService;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TaxaService {

    private static final Logger logger = LoggerFactory.getLogger(TaxaService.class);

    @Autowired
    private TaxaRep taxaRep;

    @Autowired
    private HistoricoGenericoService historicoGenericoService;
    
    @Autowired
    private TaxaCarenciaArmazenagemService taxaCarenciaArmazenagemService;
    
    @Autowired
    private TaxaGrupoProdutoService taxaGrupoProdutoService;
    
    @Autowired
    private TaxaEstabelecimentoService taxaEstabelecimentoService;
    
    @Autowired
    private IntegrationService integrationService;
	
	@Autowired
    private IntegracaoLogService integracaoLogService;
	
	@Autowired
	private EstabelecimentoService estabelecimentoService;
	
	@Autowired
	private GrupoProdutoService grupoProdutoService;

	@Value("${spring.profiles.active}")
	private String profileActive;

    public List<TaxaRetornoCadastroDto> savarVarias(List<TaxaDto> objDtos) {
        List<TaxaRetornoCadastroDto> cadastrados = new ArrayList<>();

        for(TaxaDto objDto : objDtos) {
            Long taxaId = null;
            String mensagem = "";
            Boolean integrado = false;

            try {
                Taxa taxa = null;

                if(objDto.getId() == null) {
                    taxa = cadastrar(objDto);
                    mensagem = "Taxa cadastrada com sucesso!";
                }
                else {
                    taxa = atualizar(objDto);
                    mensagem = "Taxa atualizada com sucesso!";
                }

                taxaId = taxa.getId();
                integrado = true;
            }
            catch (Exception e) {
                logger.error("SALVAR VARIOS TAXAS: " + e.getMessage());
                mensagem = e.getMessage();
            }

            TaxaRetornoCadastroDto retornoDto = TaxaRetornoCadastroDto.construir(
                    taxaId,
                    objDto.getCodigoERP(),
                    objDto.getDescricao(),
                    mensagem,
                    integrado
            );
            cadastrados.add(retornoDto);
        }

        return cadastrados;
    }

    public Taxa cadastrar(TaxaDto objDto) {
        objDto.setId(null);

        validarVinculosDuplicados(objDto);

        for(TaxaGrupoProdutoDto item : objDto.getGrupoProdutos()) { item.setId(null); }
        for(TaxaEstabelecimentoDto item : objDto.getEstabelecimentos()) { item.setId(null); }
        for(TaxaCarenciaArmazenagemDto item : objDto.getCarenciaArmazenagens()) { item.setId(null); }

        Taxa taxa = new Taxa();
        BeanUtils.copyProperties(objDto, taxa);
        taxa = salvar(taxa, objDto.getCarenciaArmazenagens(), objDto.getGrupoProdutos(), objDto.getEstabelecimentos());
        historicoGenericoService.salvar(
        		taxa.getId(),
                PaginaEnum.TAXA,
                "Inclusão Grupo",
                taxa.getDescricao()
        );

        return taxa;
    }

    public Taxa atualizar(TaxaDto objDto) {
        if(objDto.getId() == null) {
            throw new RuntimeException("Para atualizar é necessário informar o ID da Taxa!");
        }
        
        Taxa taxa = taxaRep.findById(objDto.getId()).orElse(null);
        if(taxa == null) {
            throw new ObjectDefaultException("Para atualizar é necessário informar o ID da Taxa!");
        }

        validarVinculosDuplicados(objDto);
        String alteracoesTaxa = getAlteracoesTaxa(objDto);
        String alteracoesTaxaCarencia = getAlteracoesTaxaCarencia(objDto.getId(), objDto.getCarenciaArmazenagens());
        String exclusaoTaxaCarencia = getExclusaoTaxaCarencia(objDto.getId(), objDto.getCarenciaArmazenagens());
        String alteracoesTaxaGrupoProduto = getAlteracoesTaxaGrupoProduto(objDto.getId(), objDto.getGrupoProdutos());
        String exclusaoTaxaGrupoProduto = getExclusaoTaxaGrupoProduto(objDto.getId(), objDto.getGrupoProdutos());
        
        String alteracoesTaxaEstabelecimento = getAlteracoesTaxaEstabelecimento(objDto.getId(), objDto.getEstabelecimentos());
        String exclusaoTaxaEstabelecimento = getExclusaoTaxaEstabelecimento(objDto.getId(), objDto.getEstabelecimentos());
        
        BeanUtils.copyProperties(objDto, taxa);

        List<TaxaCarenciaArmazenagem> carenciaArmazenagensParaInativar = taxaCarenciaArmazenagemService.getCarenciaArmazenagemParaInativar(taxa.getId(), objDto.getCarenciaArmazenagens());
        List<TaxaGrupoProduto> grupoProdutoParaInativar = taxaGrupoProdutoService.getGrupoProdutoParaInativar(taxa.getId(), objDto.getGrupoProdutos());
        List<TaxaEstabelecimento> estabelecimentoParaInativar = taxaEstabelecimentoService.getEstabelecimentoParaInativar(taxa.getId(), objDto.getEstabelecimentos());
        
        taxa = salvar(taxa, objDto.getCarenciaArmazenagens(), objDto.getGrupoProdutos(), objDto.getEstabelecimentos());
        taxaCarenciaArmazenagemService.inativar(carenciaArmazenagensParaInativar);
        taxaGrupoProdutoService.inativar(grupoProdutoParaInativar);
        taxaEstabelecimentoService.inativar(estabelecimentoParaInativar);

        if(!alteracoesTaxa.isEmpty()) {
            historicoGenericoService.salvar(
            		taxa.getId(),
                    PaginaEnum.TAXA,
                    "Alteração Grupo",
                    alteracoesTaxa
            );
        }

        if(!alteracoesTaxaCarencia.isEmpty()) {
            historicoGenericoService.salvar(
            		taxa.getId(),
                    PaginaEnum.TAXA,
                    "Alteração Período Carência",
                    alteracoesTaxaCarencia
            );
        }

        if(!exclusaoTaxaCarencia.isEmpty()) {
            historicoGenericoService.salvar(
            		taxa.getId(),
                    PaginaEnum.TAXA,
                    "Exclusão Periodo Carência ",
                    exclusaoTaxaCarencia
            );
        }

        if(!alteracoesTaxaGrupoProduto.isEmpty()) {
            historicoGenericoService.salvar(
            		taxa.getId(),
                    PaginaEnum.TAXA,
                    "Alteração Vínculo Grupos Produtos X Estabelecimentos",
                    alteracoesTaxaGrupoProduto
            );
        }

        if(!exclusaoTaxaGrupoProduto.isEmpty()) {
            historicoGenericoService.salvar(
            		taxa.getId(),
                    PaginaEnum.TAXA,
                    "Exclusão Vínculo Grupos Produtos",
                    exclusaoTaxaGrupoProduto
            );
        }
        
        if(!exclusaoTaxaEstabelecimento.isEmpty()) {
            historicoGenericoService.salvar(
            		taxa.getId(),
                    PaginaEnum.TAXA,
                    "Exclusão Vínculo Estabelecimento",
                    exclusaoTaxaEstabelecimento
            );
        }

        return taxa;
    }

    public Taxa salvar(Taxa taxa, List<TaxaCarenciaArmazenagemDto> carenciaArmazenagemDtos, List<TaxaGrupoProdutoDto> grupoProdutoDtos, List<TaxaEstabelecimentoDto> estabelecimentoDtos) {
        validarGrupoProduto(grupoProdutoDtos);
        validarEstabelecimento(estabelecimentoDtos);
        
        Collections.sort(carenciaArmazenagemDtos, Comparator.comparing(TaxaCarenciaArmazenagemDto::getDataInicial));
        validarCarenciaArmazenagem(carenciaArmazenagemDtos);
        
        StatusIntegracao statusIntegracao = integrationService.buscarStatusIntegracaoPorPagina(TAXA);
        taxa.setStatusIntegracao(statusIntegracao);
        taxa.setDataIntegracao(null);
        
        Taxa obj = taxaRep.save(taxa);
        taxaCarenciaArmazenagemService.salvar(obj, carenciaArmazenagemDtos);
        taxaGrupoProdutoService.salvar(obj, grupoProdutoDtos);
        taxaEstabelecimentoService.salvar(obj, estabelecimentoDtos);
        
        return obj;
    }

    public Page<TaxaSimplesDto> buscarPorPagina(Pageable pageable, TaxaFilter filter, Situacao situacao) {
        Page<Taxa> taxaPage = taxaRep.findAll(pageable, filter, situacao);
        List<TaxaSimplesDto> taxaSimplesDtos = TaxaSimplesDto.construir(taxaPage.getContent());

        return new PageImpl<>(
                taxaSimplesDtos,
                pageable,
                taxaPage.getTotalElements()
        );
    }

    public Taxa buscarPorId(Long id) {
        Taxa taxa = taxaRep.findById(id).orElse(null);

        if(taxa == null) {
            throw new ObjectNotFoundException("A taxa não foi encontrado! ID: " + id + ", Tipo: " + Taxa.class.getName());
        }
        
        if(taxa.getEstabelecimentos() != null && taxa.getEstabelecimentos().size() > 1) {
        	Collections.sort(taxa.getEstabelecimentos(), Comparator.comparing(TaxaEstabelecimento::getCodigo));
        }
        
        return taxa;
    }
 

    public List<TaxaCarenciaArmazenagem> buscarCarenciaArmazenagemPorIdTaxa(Long id) {
        Taxa taxa = buscarPorId(id);
        return taxa.getCarenciaArmazenagens();
    }

    public List<TaxaGrupoProduto> buscarGrupoProdutosPorIdTaxa(Long id) {
        Taxa taxa = buscarPorId(id);
        return taxa.getGrupoProdutos();
    }

    public void inativar(Long id) {
        Taxa obj = buscarPorId(id);
        obj.setDataInativacao(new Date());
        taxaRep.save(obj);
        historicoGenericoService.salvar(
                obj.getId(),
                PaginaEnum.TAXA,
                "Inativar Grupo",
                obj.getDescricao()
        );
    }

    public void ativar(Long id) {
        Taxa obj = buscarPorId(id);
        obj.setDataInativacao(null);
        TaxaDto objDto = TaxaDto.construir(obj);
        atualizar(objDto);
        historicoGenericoService.salvar(
                obj.getId(),
                PaginaEnum.TAXA,
                "Ativar Grupo",
                obj.getDescricao()
        );
    }

    private List<String> getTaxasComMesmasInformacoes(TaxaDto objDto) {
        List<Long> idGupoProdutos = new ArrayList<>();
        List<String> codigoEstabelecimentos = new ArrayList<>();

        for(TaxaGrupoProdutoDto grupoProduto : objDto.getGrupoProdutos()) {
            idGupoProdutos.add(grupoProduto.getIdGrupoProduto());
        }
        
        for(TaxaEstabelecimentoDto estabelecimento : objDto.getEstabelecimentos()) {
        	codigoEstabelecimentos.add(estabelecimento.getCodigo());
        }

        return taxaRep.buscarPorSafraGrupoProdutoEstabelecimento(
                objDto.getId() == null ? 0l : objDto.getId(),
                objDto.getSafra(),
                idGupoProdutos,
                codigoEstabelecimentos
        );
    }

    private void validarVinculosDuplicados(TaxaDto objDto) {
        List<String> vinculosDuplicados = getTaxasComMesmasInformacoes(objDto);
        if(vinculosDuplicados.size() > 0) {
            StringBuilder resultado = new StringBuilder();

            for (String vinculo : vinculosDuplicados) {
                resultado.append(vinculo).append("; ");
            }

            throw new RuntimeException("Já existe taxa com Safra X Grupo Produto X Estabelecimento cadastradas: " + resultado.toString());
        }
    }

    private void validarCarenciaArmazenagem(List<TaxaCarenciaArmazenagemDto> objDtos) {
        Date ultimaData = null;
        List<FieldErrorItem> fieldErrorItems = new ArrayList<>();

        //if(objDtos == null || objDtos.isEmpty()) {
        //    throw new RuntimeException("Necessário informar ao menos uma carência de armazenagem!");
        //}

        for(TaxaCarenciaArmazenagemDto item : objDtos) {
            try {
                ultimaData = validarRangeDataCarenciaArmazenagem(item, ultimaData);
            }
            catch (RuntimeException e) {
                fieldErrorItems.add(FieldErrorItem.construir("Data Inicial", e.getMessage()));
            }
        }

        if(fieldErrorItems.size() > 0) {
            throw new ObjectFieldErrorsException("Validação das carências", fieldErrorItems);
        }
    }

    private Date validarRangeDataCarenciaArmazenagem(TaxaCarenciaArmazenagemDto objDto, Date ultimaData) {
        var sdf = new SimpleDateFormat("dd/MM/yyyy");
        var dataInicial = objDto.getDataInicial();
        var dataFinal = objDto.getDataFinal();

        if(ultimaData == null) {
            validarRangeEntreDatasCarenciaArmazenagem(dataInicial, dataFinal);
            return dataFinal;
        }

        if(ultimaData.compareTo(dataInicial) == 0) {
            throw new RuntimeException("A data final (" + sdf.format(ultimaData) + ") da carência de armazenagem anterior é igual a data inicial (" + sdf.format(dataInicial) + ")");
        }

        Calendar calendarUltima = Calendar.getInstance();
        calendarUltima.setTime(ultimaData);
        calendarUltima.add(Calendar.DATE, 1);
        Integer diferenca = calendarUltima.getTime().compareTo(dataInicial);
        if(diferenca != 0) {
            throw new RuntimeException("A data final (" + sdf.format(ultimaData) + ") da carência de armazenagem anterior possui uma diferença a " + (diferenca < 0 ? "menos" : "mais" ) + " da data inicial (" + sdf.format(dataInicial) + ") da carência atual");
        }

        validarRangeEntreDatasCarenciaArmazenagem(dataInicial, dataFinal);
        return dataFinal;
    }

    private void validarRangeEntreDatasCarenciaArmazenagem(Date dataInicial, Date dataFinal) {
        if(dataFinal.compareTo(dataInicial) > 0) return;
        var sdf = new SimpleDateFormat("dd/MM/yyyy");
        throw new RuntimeException("A data final (" + sdf.format(dataFinal) + ") é menor ou igual a data inicial (" + sdf.format(dataInicial) + ")");
    }

    private void validarGrupoProduto(List<TaxaGrupoProdutoDto> objDtos) {
        if(CollectionUtils.isEmpty(objDtos)) {
        	throw new RuntimeException("Necessário informar ao menos um grupo de produto!");
        }
    }
    
    private void validarEstabelecimento(List<TaxaEstabelecimentoDto> objEstabelecimentosDto) {
    	if(CollectionUtils.isEmpty(objEstabelecimentosDto)) {
            throw new RuntimeException("Necessário informar ao menos um estabelecimento!");
        }
    }

    private String getAlteracoesTaxa(TaxaDto objDto) {
        Taxa taxa = buscarPorId(objDto.getId());
        TaxaDto taxaDto = TaxaDto.construir(taxa);

        try {
            return CompararObjetos.comparar(taxaDto, objDto);
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return "";
    }

    private String getAlteracoesTaxaCarencia(Long idTaxa, List<TaxaCarenciaArmazenagemDto> objDtos) {
        List<TaxaCarenciaArmazenagem> taxaCarenciaArmazenagens = buscarCarenciaArmazenagemPorIdTaxa(idTaxa);
        List<TaxaCarenciaArmazenagemDto> taxaCarenciaArmazenagemDtos = TaxaCarenciaArmazenagemDto.construir(taxaCarenciaArmazenagens);

        String alteracoes = "";
        for(TaxaCarenciaArmazenagemDto item : objDtos) {
            TaxaCarenciaArmazenagemDto itemAtual = taxaCarenciaArmazenagemDtos.stream()
                    .filter(taxaCarenciaArmazenagemDto -> { return taxaCarenciaArmazenagemDto.getId().equals(item.getId()); })
                    .findFirst().orElse(null);

            if(itemAtual != null) {
                try {
                    alteracoes += CompararObjetos.comparar(itemAtual, item);
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
        }

        return alteracoes;
    }

    private String getExclusaoTaxaCarencia(Long idTaxa, List<TaxaCarenciaArmazenagemDto> objDtos) {
        List<TaxaCarenciaArmazenagem> taxaCarenciaArmazenagens = buscarCarenciaArmazenagemPorIdTaxa(idTaxa);
        List<TaxaCarenciaArmazenagemDto> taxaCarenciaArmazenagemDtos = TaxaCarenciaArmazenagemDto.construir(taxaCarenciaArmazenagens);

        StringBuilder excluidos = new StringBuilder();
        for(TaxaCarenciaArmazenagemDto itemAtual : taxaCarenciaArmazenagemDtos) {
            TaxaCarenciaArmazenagemDto item = objDtos.stream()
                    .filter(taxaCarenciaArmazenagemDto -> { return taxaCarenciaArmazenagemDto.getId() == itemAtual.getId(); })
                    .findFirst().orElse(null);

            if(item == null) {
                excluidos.append("\nData Inicial: \"");
                excluidos.append(itemAtual.getDataInicial());
                excluidos.append("\", Data Final: \"");
                excluidos.append(itemAtual.getDataFinal());
                excluidos.append("\", Quantidade de dias carência: \"");
                excluidos.append(itemAtual.getQuantidadeDiasCarencia());
                excluidos.append("\"");
            }
        }

        return excluidos.toString();
    }

    private String getAlteracoesTaxaGrupoProduto(Long idTaxa, List<TaxaGrupoProdutoDto> objDtos) {
        List<TaxaGrupoProduto> taxaGrupoProdutos = buscarGrupoProdutosPorIdTaxa(idTaxa);
        List<TaxaGrupoProdutoDto> taxaGrupoProdutoDtos = TaxaGrupoProdutoDto.construir(taxaGrupoProdutos);

        String alteracoes = "";
        for(TaxaGrupoProdutoDto item : objDtos) {
            if(item.getId() == null) continue;
            TaxaGrupoProdutoDto itemAtual = taxaGrupoProdutoDtos.stream()
                    .filter(taxaGrupoProdutoDto -> { return taxaGrupoProdutoDto.getId().equals(item.getId()); })
                    .findFirst().orElse(null);

            if(itemAtual != null) {
                try {
                    alteracoes += CompararObjetos.comparar(itemAtual, item);
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
        }

        return alteracoes;
    }
    
    private String getAlteracoesTaxaEstabelecimento(Long idTaxa, List<TaxaEstabelecimentoDto> objDtos) {
        List<TaxaEstabelecimento> taxaEstabelecimentos = buscarEstabelecimentosPorIdTaxa(idTaxa);
        List<TaxaEstabelecimentoDto> taxaEstabelecimentoDtos = TaxaEstabelecimentoDto.construir(taxaEstabelecimentos);

        String alteracoes = "";
        for(TaxaEstabelecimentoDto item : objDtos) {
            if(item.getId() == null) continue;
            TaxaEstabelecimentoDto itemAtual = taxaEstabelecimentoDtos.stream()
                    .filter(taxaEstabelecimentoDto -> { return taxaEstabelecimentoDto.getId().equals(item.getId()); })
                    .findFirst().orElse(null);

            if(itemAtual != null) {
                try {
                    alteracoes += CompararObjetos.comparar(itemAtual, item);
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
        }

        return alteracoes;
    }

    private String getExclusaoTaxaGrupoProduto(Long idTaxa, List<TaxaGrupoProdutoDto> objDtos) {
        List<TaxaGrupoProduto> taxaGrupoProdutos = buscarGrupoProdutosPorIdTaxa(idTaxa);
        List<TaxaGrupoProdutoDto> taxaGrupoProdutoDtos = TaxaGrupoProdutoDto.construir(taxaGrupoProdutos);

        StringBuilder excluidos = new StringBuilder();
        for(TaxaGrupoProdutoDto itemAtual : taxaGrupoProdutoDtos) {
            TaxaGrupoProdutoDto item = objDtos.stream()
                    .filter(taxaGrupoProdutoDto -> { return taxaGrupoProdutoDto.getId() == itemAtual.getId(); })
                    .findFirst().orElse(null);

            if(item == null) {
                excluidos.append(itemAtual.toString());
                excluidos.append("\n");
            }
        }

        return excluidos.toString();
    }
    
    private String getExclusaoTaxaEstabelecimento(Long idTaxa, List<TaxaEstabelecimentoDto> objDtos) {
        List<TaxaEstabelecimento> taxaEstabelecimentos = buscarEstabelecimentosPorIdTaxa(idTaxa);
        List<TaxaEstabelecimentoDto> taxaEstabelecimentosDtos = TaxaEstabelecimentoDto.construir(taxaEstabelecimentos);

        StringBuilder excluidos = new StringBuilder();
        for(TaxaEstabelecimentoDto itemAtual : taxaEstabelecimentosDtos) {
            TaxaEstabelecimentoDto item = objDtos.stream()
                    .filter(taxaEstabelecimentoDto -> { return taxaEstabelecimentoDto.getId() == itemAtual.getId(); })
                    .findFirst().orElse(null);

            if(item == null) {
                excluidos.append(itemAtual.toString());
                excluidos.append("\n");
            }
        }

        return excluidos.toString();
    }

    
    private List<TaxaEstabelecimento> buscarEstabelecimentosPorIdTaxa(Long id) {
    	Taxa taxa = buscarPorId(id);
        return taxa.getEstabelecimentos();
	}

	public List<Taxa> buscarComStatus(StatusIntegracao status) {
    	List<Taxa> taxas = taxaRep.findByStatusIntegracao(status);
    	if(CollectionUtils.isEmpty(taxas)) return Collections.emptyList();
    	return taxas.stream().map(taxa -> {
    		var obj = new Taxa();
    		BeanUtils.copyProperties(taxa, obj);
    		List<TaxaCarenciaArmazenagem> carenciaArmazenagens = taxaCarenciaArmazenagemService.buscarComStatus(obj.getId(), status);
    		List<TaxaGrupoProduto> grupoProdutos = taxaGrupoProdutoService.buscarComStatus(obj.getId(), status);
    		obj.setCarenciaArmazenagens(carenciaArmazenagens);
    		obj.setGrupoProdutos(grupoProdutos);
    		return obj;
    	}).toList();
    }

	public void alterarStatusIntegracao(StatusIntegracao status, List<Taxa> objs) {
		if(CollectionUtils.isEmpty(objs)) return;
		List<Taxa> taxas = objs.stream().map(taxa -> {
			taxaCarenciaArmazenagemService.alterarStatusIntegracao(status, taxa.getCarenciaArmazenagens());
			taxaGrupoProdutoService.alterarStatusIntegracao(status, taxa.getGrupoProdutos());
			taxa.setDataIntegracao(null);
			taxa.setStatusIntegracao(status);
			return taxa;
		}).toList();
		taxaRep.saveAllAndFlush(taxas);
	}
    
	public void sincronizarTaxas(Long id) {
		logger.info("Iniciando a sincronização de taxas de produção...");
		
		IntegracaoPagina pagina = integrationService.buscarPorPagina(TAXA_PRODUCAO, INT_LOTE_TAXA_PRODUCAO);
    	if(pagina == null || ERP.equals(pagina.getOrigenEnum())) {
    		logger.info("Não foi encontrado o cadastro de integração para a taxa!");
    		return;
    	}

		IntegracaoPaginaFuncionalidade paginaFuncionalidade = pagina.getFuncionalidade(INT_LOTE_TAXA_PRODUCAO);
		if(paginaFuncionalidade == null || paginaFuncionalidade.getSituacao() == null || paginaFuncionalidade.getSituacao().equals(SituacaoFuncionalidadePaginaEnum.INATIVO)) return;
    	
    	List<Taxa> taxas = null; 
    	IntegracaoLog integracaoLogRequest = IntegracaoLog.construirLogRequest(PaginaEnum.TAXA_PRODUCAO, FuncionalidadePaginaEnum.INT_LOTE_TAXA_PRODUCAO);   	
    	
    	try {
    		
    		if(id == null) {
    			taxas = buscarComStatus(INTEGRAR);
    		}else {
    			Taxa taxa = buscarPorId(id);
    			if(taxa != null) {
    				taxas = new ArrayList<>();
    				taxas.add(taxa);
    			}
    		}
    		
    		integracaoLogRequest.setTotalRegistros(taxas != null ? taxas.size() : 0);
    		alterarStatusIntegracao(PROCESSANDO, taxas);
    		
    		

        	if(!CollectionUtils.isEmpty(taxas)) {    		
    		
	        	TaxaIntegrationListDto objDto = TaxaIntegrationListDto.construir(taxas);
	    		
	    		IntegrationAuth auth = integrationService.buscarPelaDescricao(pagina.getAuth(profileActive).getDescricao());
	    		
	    		LinkedMultiValueMap<String, String> mvmap = new LinkedMultiValueMap<>();
	    		for(IntegracaoPaginaHeader header: pagina.getHeaders()) {
	    			if(pagina.isHearderProfileActive(header, profileActive)) {
		    			if(!Strings.isEmpty(header.getChave()) && !Strings.isEmpty(header.getValor())){
		    				mvmap.add(header.getChave(), header.getValor());
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
	    		.bodyToFlux(TaxaIntegrationListResponseDto.class)
	    		.subscribe(taxaIntegrationListDto -> {
	    			logger.info("Iniciando o processamento do retorna da sincronizações das taxas.");
	    			
	    			Integer totalRegistros = 0;
	    			Integer sucessNotFound = 0;
	    			Integer sucess = 0;
	    			Integer error = 0;
	    			
					for(TaxaIntegrationResponseDto item : taxaIntegrationListDto.getTaxas()) {
						try {
							StatusIntegracao statisIntegracaoCabecalho = item.getIntegrated() ? INTEGRADO : INTEGRAR;	
	    						
	    					Taxa taxa = taxaRep.findById(item.getIdTaxa()).orElse(null);
	    					if(taxa != null) {
	    						taxa.setDataIntegracao(statisIntegracaoCabecalho.equals(INTEGRADO) ? new Date() : null);
	    						taxa.setStatusIntegracao(statisIntegracaoCabecalho);
	    						
	    						if(statisIntegracaoCabecalho.equals(INTEGRADO)) {
    								sucess++;
    							}else {
    								error++;
    							}
	    						
	    						if(item.getOperacao() == null || item.getOperacao().equals(IntegrationOperacaoEnum.WRITE)) {
	    							taxaRep.save(taxa);
	    						}else {
	    							try {
	    								taxaRep.delete(taxa);
	    							}catch (Exception e) {
										if(taxa.getAtivo()) {
											taxa.setDataInativacao(new Date());
											taxaRep.delete(taxa);
											
										}
									}
	    						}
	    						
	    					}else {
	    						sucessNotFound++;
	    					}
	    					totalRegistros++;	    					
	    					
		    			}
						catch (Exception e) {
							logger.error("Retorno da sincronização de TAXA ");
							logger.error("TAXA: " + item.toString());
						}
	    			}
					
					StringBuilder obs = new StringBuilder();
    				obs.append("[Resumo Integração ERP] \n\r");
    				obs.append("Sucesso: ").append(sucess)
    					.append(", Sucesso ERP/NotFound Gênesis: ")
    					.append(sucessNotFound).append(", Falha: ").append(error);
    				
    				SituacaoIntegracaoLogEnum situacao = (error > 0 || sucess == 0)
							? SituacaoIntegracaoLogEnum.FALHA
							: SituacaoIntegracaoLogEnum.SUCESSO;
    				
	    			
					IntegracaoLog integracaoLogResponse = IntegracaoLog.construirLogResponse(
						PaginaEnum.TAXA_PRODUCAO,
						FuncionalidadePaginaEnum.INT_LOTE_TAXA_PRODUCAO,
						situacao,
						totalRegistros, 
						obs.toString() );
	    			
					integracaoLogService.salvar(integracaoLogResponse);
					
					
	    		});
        	}
    	}
    	catch (Exception e) {
    		System.out.println("Erro sincronização de Taxas de Produção: " + e.getMessage());
    		alterarStatusIntegracao(INTEGRAR, taxas);
			integracaoLogRequest.registrarFalha("Error: " + e.getMessage());
    		e.printStackTrace();
    	} finally {
			integracaoLogService.salvar(integracaoLogRequest);
		}
	}

	public Taxa buscarTaxaProducaoEstabelecimento(int safra, Long codEstabelecimento, Long familia) {
		Taxa taxa = taxaRep.TaxaProducaoEstabelecimento(safra, codEstabelecimento, familia).orElse(null);
		
		if(taxa == null) {
			GrupoProduto grupoProduto = grupoProdutoService.buscarIdGrupoProduto(familia);
			Estabelecimento estabelecimento = estabelecimentoService.buscarPorId(codEstabelecimento);
			throw new ObjectNotFoundException("Não foi encontrado taxa com os parâmetros informados. (Grupo Produto: " + grupoProduto.getFmCodigo() + ", Safra: " + safra + ", Estabelecimento: " + estabelecimento.getCodigo() + ")");
		}

		return taxa;
	}

	public Page<EstabelecimentoDto> buscarIdTaxaEstabelecimentos(Long id, Pageable pageable) {
			
		//Taxa taxa = taxaRep.findById(id).orElse(null);
		Page<TaxaEstabelecimento> taxaEstabelecimentos = taxaEstabelecimentoService.findByTaxaIdAndDataInativacaoIsNullOrderByEstabelecimentoCodigo(id,  pageable);
			
		List<EstabelecimentoDto> estabelecimentosDto = new ArrayList<>();
		
		for(TaxaEstabelecimento item: taxaEstabelecimentos.getContent()){			
			
			EstabelecimentoDto estabelecimentoDto = new EstabelecimentoDto();
			BeanUtils.copyProperties(item.getEstabelecimento(), estabelecimentoDto);
			estabelecimentosDto.add(estabelecimentoDto);
			
		}
		
		Page<EstabelecimentoDto> estabelecimentoDtoPage = new PageImpl<>(
				estabelecimentosDto, pageable, taxaEstabelecimentos.getTotalElements()
		);

		return estabelecimentoDtoPage;
	}

	public Page<TaxaCarenciaArmazenagem> buscarPeridoCarencia(Long idTaxa, Pageable pageable) {
		
		return taxaCarenciaArmazenagemService.findByTaxaIdAndDataInativacaoIsNull(idTaxa, pageable);
		
	}

	public void excluir(Long idTaxa) {
		
		Taxa objTaxa = buscarPorId(idTaxa);
        
        if(objTaxa == null) {
        	throw new ObjectNotFoundException("Não foi encontrada a Taxa de Produção com o Id: " + idTaxa);
        }
                
        objTaxa.setDataInativacao(new Date());
        taxaRep.save(objTaxa);
        
        
        sincronizarTaxas(objTaxa.getId());   
        
        
        historicoGenericoService.salvar(
        		objTaxa.getId(),
                PaginaEnum.TAXA_PRODUCAO,
                "Excluir Taxa de Produção",
                objTaxa.getDescricao()
        );
		
	}

	public Page<GrupoProduto> buscarIdTaxaGrupoProdutos(Long id, Pageable pageable) {
		Page<TaxaGrupoProduto> taxaGrupoProdutos = taxaGrupoProdutoService.findByTaxaIdAndDataInativacaoIsNull(id, pageable);
		
		List<GrupoProduto> grupoProdutos = new ArrayList<>();
		
		for(TaxaGrupoProduto item: taxaGrupoProdutos.getContent()){			
			grupoProdutos.add(item.getGrupoProduto());
		}
		
		Page<GrupoProduto> grupoProdutoPage = new PageImpl<>(
				grupoProdutos, pageable, taxaGrupoProdutos.getTotalElements()
		);

		return grupoProdutoPage;
	}
		
	

}
