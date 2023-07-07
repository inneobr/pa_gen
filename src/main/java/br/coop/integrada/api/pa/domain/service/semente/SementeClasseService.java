package br.coop.integrada.api.pa.domain.service.semente;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntregaItem;
import br.coop.integrada.api.pa.domain.model.rependente.RePendente;
import br.coop.integrada.api.pa.domain.model.rependente.RePendenteItem;
import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeLaudoInspecao;
import br.coop.integrada.api.pa.domain.modelDto.semente.SementeCampoFilter;
import br.coop.integrada.api.pa.domain.modelDto.semente.SementeClasseDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.VerificaTotleranciaRecebimentoDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.VerificaTotleranciaRecebimentoResponseDto;
import br.coop.integrada.api.pa.domain.repository.EstabelecimentoRep;
import br.coop.integrada.api.pa.domain.repository.recEntrega.RecEntregaRep;
import br.coop.integrada.api.pa.domain.repository.rependente.RePendenteRep;
import br.coop.integrada.api.pa.domain.repository.semente.SementeClasseRep;
import br.coop.integrada.api.pa.domain.repository.semente.produtor.SementeLaudoInspecaoRep;
import br.coop.integrada.api.pa.domain.service.produto.GrupoProdutoService;
import br.coop.integrada.api.pa.domain.spec.SementeClasseSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SementeClasseService {

    @Autowired
    private SementeClasseRep sementeClasseRep;
    
    @Autowired
    private SementeLaudoInspecaoRep sementeLaudoInspecaoRep;
    
    @Autowired
    private RecEntregaRep recEntregaRep;
    
    @Autowired
    private RePendenteRep rePendenteRep;
    
    @Autowired
    private EstabelecimentoRep estabelecimentoRep;
    
    @Autowired
    private GrupoProdutoService grupoProdutoService;

    public SementeClasse salvar(SementeClasseDto sementeClasse) {
        SementeClasse sementeClasseAtual = sementeClasseRep.findByCodigo(sementeClasse.getId());

        if(sementeClasseAtual == null) {
            sementeClasseAtual = new SementeClasse();
        }

        BeanUtils.copyProperties(sementeClasse, sementeClasseAtual);

        validarSeExisteClasseComCodigoOuDescricao(sementeClasseAtual);

        return sementeClasseRep.save(sementeClasseAtual);
    }

    public SementeClasse buscarPorDescricao(String descricao) {
        return sementeClasseRep.findByDescricaoIgnoreCaseAndDataInativacaoIsNull(descricao);
    }

    public SementeClasse buscarPorCodigo(Long codigo) {
        return sementeClasseRep.findByCodigo(codigo);
    }

    public SementeClasse buscarPorId(Long id) {
        return sementeClasseRep.findById(id).orElse(null);
    }

    public List<SementeClasse> listarTodasClasses() {
        return sementeClasseRep.findAll();
    }

    public Page<SementeClasse> buscarPorCodigoOuDescricao(String filtro, Situacao situacao, Pageable pageable) {
    	return sementeClasseRep.findAll(
    			SementeClasseSpecs.codigoOuDescricaoLike(filtro)
    			.and(SementeClasseSpecs.doSituacao(situacao)),
    			pageable);
    }

    private void validarSeExisteClasseComCodigoOuDescricao(SementeClasse sementeClasse) {
        List<SementeClasse> sementeClasseList = sementeClasseRep.findByCodigoOrDescricaoIgnoreCase(sementeClasse.getCodigo(), sementeClasse.getDescricao());

        if(sementeClasseList != null && !sementeClasseList.isEmpty()) {
            List<String> mensagens = new ArrayList<>();

            for(SementeClasse item : sementeClasseList) {
                Boolean osIdsSaoDiferentes = item.getId() != sementeClasse.getId();
                Boolean osCodigosSaoIguais = item.getCodigo().equals(sementeClasse.getCodigo());
                Boolean asDescricoesSaoIguais = item.getDescricao().trim().toUpperCase().equals(sementeClasse.getDescricao().trim().toUpperCase());

                if(osIdsSaoDiferentes && (osCodigosSaoIguais || asDescricoesSaoIguais)) {
                    mensagens.add("Já existe uma classe com o código (" + item.getCodigo() + ") e descrição (" + item.getDescricao() + ")");
                }
            }

            if(!mensagens.isEmpty()) {
                String mensagem = String.join("\n", mensagens);
                throw new IllegalArgumentException(mensagem);
            }
        }
    }

    public SementeClasse inativar(Long id) {
        SementeClasse sementeClasse = sementeClasseRep.findById(id).orElse(null);

        if(sementeClasse == null) {
            throw new NullPointerException("Não foi encontrado classe com o ID " + id + "!");
        }
        
        sementeClasse.setDataInativacao(new Date());
        return sementeClasseRep.save(sementeClasse);
    }

    public SementeClasse ativar(Long id) {
        
    	SementeClasse sementeClasseAtual = sementeClasseRep.findById(id).orElse(null);
        
        if(sementeClasseAtual == null) {
            throw new NullPointerException("Não foi encontrado classe com o ID " + id + "!");
        }
        
        sementeClasseAtual.setDataInativacao(null);
        
        return sementeClasseRep.save(sementeClasseAtual);

    }

    public Page<SementeClasseDto> buscarComPaginacao(Pageable pageable, String filtro, Situacao situacao) {
        Page<SementeClasse> sementeClassePage = sementeClasseRep.findAll(pageable, filtro, situacao);
        List<SementeClasseDto> sementeClasseDtos = SementeClasseDto.construir(sementeClassePage.getContent());

        return new PageImpl(sementeClasseDtos, pageable, sementeClassePage.getTotalElements());
    }
    
    
    //Objetivo: Validar quantidade recebida x estimada de Semente
	public VerificaTotleranciaRecebimentoResponseDto verificarToleranciaRecebimentoSemente(VerificaTotleranciaRecebimentoDto input) {
		VerificaTotleranciaRecebimentoResponseDto output = new VerificaTotleranciaRecebimentoResponseDto(null);
		output.setMensagem("");
		output.setTipoRetorno("");
		
		GrupoProduto grupoProduto = grupoProdutoService.buscarGrupoFmCodigo(input.getGrupoProduto());
		if(grupoProduto.isSemente() == false) return output;
		
		//Ler a tabela tb_laudo_insp
		SementeLaudoInspecao semLaudoInsp = sementeLaudoInspecaoRep.findBySafraAndNumeroLaudoAndOrdemCampoAndGrupoProdutoFmCodigoAndEstabelecimentoCodigo(
				input.getSafra(),
				input.getNumeroLaudo(),
				input.getNumeroOrdemCampo(),
				input.getGrupoProduto(),
				input.getCodEstabelecimento()
				).orElse(null);
		
		if(semLaudoInsp == null) {
			throw new NotFoundException("Não foi possível encontrar um laudo de inspeção com os parâmetros informados.");
		}
		
		//Encontrar a quantidade esperada = tb_laudo_insp.qt_prod_esperada * tb_laudo_ins.area-aprovada
		BigDecimal quantidadeEsperada = semLaudoInsp.getProducaoEsperada().multiply(semLaudoInsp.getAreaAprovada());
		
		//Encontrar o total com tolerância = ((quantidade esperada * 10) / 100) + quantidade esperada
		BigDecimal totalComTolerancia = quantidadeEsperada.multiply(new BigDecimal(10)).divide(new BigDecimal(100), RoundingMode.HALF_EVEN).add(quantidadeEsperada);
		
		//Verificar a quantidade já recebida
		// Ler a rec_entrega,
		
		BigDecimal qtdRecebida = recEntregaRep.quantidadeTotalRendaLiquidaAtu(
				input.getCodEstabelecimento(),
				input.getSafra(),
				input.getGrupoProduto(),
				input.getNumeroOrdemCampo(),
				input.getNumeroLaudo());
		
		if(qtdRecebida == null) {
			qtdRecebida = BigDecimal.ZERO;
		}
		
		//Verificar as re_pendente do mesmo laudo para validar a quantidade
		
		qtdRecebida.add( 
				rePendenteRep.quantidadeTotalRendaLiquida(
				input.getCodEstabelecimento(),
				input.getSafra(),
				input.getGrupoProduto(),
				input.getNumeroOrdemCampo(),
				input.getNumeroLaudo()) 
		);
		
		//Verifica quantidades:

		if(qtdRecebida.compareTo(totalComTolerancia) == 1) {
			//Se a quantidade recebida > total com tolerância, retornar:
			output.setTipoRetorno("Erro");
			output.setMensagem("Já houve entrada por re de " + qtdRecebida + " kg ultrapassando assim a quantidade de tolerância que é de " + totalComTolerancia + " kg");
		}
		
		if(qtdRecebida.compareTo(quantidadeEsperada) == 1 && qtdRecebida.compareTo(totalComTolerancia) == -1) {
			//Se quantidade recebida > quantidade esperada E quantidade recebida < total com tolerância, retornar:
			output.setTipoRetorno("Aviso");
			output.setMensagem("Já houve entrada por RE de " + qtdRecebida + " kg e a quantidade esperada é de " + quantidadeEsperada + " kg. "
					+ "Portanto já foi ultrapassada a quantidade esperada."
					+ " Atente-se, pois a quantidade de tolerância é de " + totalComTolerancia + "kg");
		}
		
		return output;
	}
}
