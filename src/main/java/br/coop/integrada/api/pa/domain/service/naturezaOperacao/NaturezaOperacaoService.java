package br.coop.integrada.api.pa.domain.service.naturezaOperacao;

import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacao;
import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacaoEstabelecimento;
import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacaoMovimentacao;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoCodigoDto;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoDto;
import br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao.NaturezaOperacaoDto;
import br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao.NaturezaOperacaoDtoList;
import br.coop.integrada.api.pa.domain.repository.EstabelecimentoRep;
import br.coop.integrada.api.pa.domain.repository.naturezaOperacao.NaturezaOperacaoEstabelecimentoRep;
import br.coop.integrada.api.pa.domain.repository.naturezaOperacao.NaturezaOperacaoMovimentacaoRep;
import br.coop.integrada.api.pa.domain.repository.naturezaOperacao.NaturezaOperacaoRep;
import br.coop.integrada.api.pa.domain.service.HistoricoGenericoService;
import br.coop.integrada.api.pa.domain.service.pesagem.PesagemService;
import br.coop.integrada.api.pa.domain.spec.NaturezaOperacaoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service @RequiredArgsConstructor @Transactional
public class NaturezaOperacaoService {
	private static final Logger logger = LoggerFactory.getLogger(PesagemService.class);
	
	@Autowired
	private NaturezaOperacaoRep naturezaOperacaoRep;
	
	@Autowired
	private EstabelecimentoRep estabelecimentoRep;
	
	@Autowired
	private HistoricoGenericoService historicoGenericoService;
	
	@Autowired
	private NaturezaOperacaoMovimentacaoRep naturezaMovimentacaoRep;
	
	@Autowired
	private NaturezaOperacaoEstabelecimentoRep naturezaOperacaoEstabelecimentoRep;
	
	public void cadastrar(NaturezaOperacao naturezaOperacaoDto, List<EstabelecimentoCodigoDto> listEstabelecimentoDto) throws Exception {
		logger.info("Executando salvar natureza operação.");
		NaturezaOperacao naturezaOperacao = naturezaOperacaoRep.findByCodGrupo(naturezaOperacaoDto.getCodGrupo());
		if(naturezaOperacao == null) naturezaOperacao = new NaturezaOperacao();
		
		BeanUtils.copyProperties(naturezaOperacaoDto, naturezaOperacao);
		naturezaOperacaoRep.save(naturezaOperacao);		
		
		historicoGenericoService.salvar(naturezaOperacao.getId(), PaginaEnum.NATUREZA_OPERACAO, "Criação de nova Natureza Operação Tributária", naturezaOperacao.getDescricao());

		for(EstabelecimentoCodigoDto item: listEstabelecimentoDto) {
			Estabelecimento estabelecimento = estabelecimentoRep.findByCodigo(item.getCodigo());
			NaturezaOperacaoEstabelecimento naturezaOperacaoEstabelecimento = new NaturezaOperacaoEstabelecimento();
			naturezaOperacaoEstabelecimento.setNaturezaOperacao(naturezaOperacao);
			naturezaOperacaoEstabelecimento.setEstabelecimento(estabelecimento);
			try {
				naturezaOperacaoEstabelecimento = naturezaOperacaoEstabelecimentoRep.save(naturezaOperacaoEstabelecimento);
				historicoGenericoService.salvar(naturezaOperacao.getId(), PaginaEnum.NATUREZA_OPERACAO, "Natureza Operação Tributária recebeu um novo estabelecimento", item.getNomeFantasia());
				
			}catch(DataIntegrityViolationException e) {
		        throw new DataIntegrityViolationException("O estabelecimento: "+estabelecimento.getCodNome() +" já encontra-se cadastrado em outra Natureza de Operação");
	        }			
		}
		
	}
	
	public Estabelecimento localizarEstabelecimento(String codigo) {
		return estabelecimentoRep.findByCodigo(codigo);
	}
	
	public void salvar(NaturezaOperacao naturezaOperacaoDto, List<EstabelecimentoCodigoDto> listEstabelecimentoDto) throws Exception {
		logger.info("Executando salvar natureza operação.");
		NaturezaOperacao naturezaOperacao = naturezaOperacaoRep.findByCodGrupo(naturezaOperacaoDto.getCodGrupo());
		if(naturezaOperacao == null) naturezaOperacao = new NaturezaOperacao();	
		BeanUtils.copyProperties(naturezaOperacaoDto, naturezaOperacao);
		naturezaOperacao = naturezaOperacaoRep.save(naturezaOperacao);		
	
		naturezaOperacaoEstabelecimentoRep.deleteByNaturezaOperacao(naturezaOperacao);	
	    naturezaOperacaoEstabelecimentoRep.flush();		
		for(EstabelecimentoCodigoDto item: listEstabelecimentoDto) {
			Estabelecimento estabelecimento = estabelecimentoRep.findByCodigo(item.getCodigo());
			NaturezaOperacaoEstabelecimento naturezaOperacaoEstabelecimento = new NaturezaOperacaoEstabelecimento();
			naturezaOperacaoEstabelecimento.setNaturezaOperacao(naturezaOperacao);
			naturezaOperacaoEstabelecimento.setEstabelecimento(estabelecimento);
			try {
				naturezaOperacaoEstabelecimento = naturezaOperacaoEstabelecimentoRep.save(naturezaOperacaoEstabelecimento);
				
			}
			catch(DataIntegrityViolationException e) {
		        throw new DataIntegrityViolationException("O estabelecimento: "+estabelecimento.getCodNome() +" já encontra-se cadastrado em outra Natureza de Operação");
	        }			
		}		
		
	}
	
	public NaturezaOperacaoEstabelecimento buscarNaturezaOperacaoEstabelecimento(String idUnico) {
		return naturezaOperacaoEstabelecimentoRep.findByIdUnico(idUnico);
	}
	
	public NaturezaOperacaoMovimentacao buscarNaturezaOperacaoMovimento(String idUnico) {
		return naturezaMovimentacaoRep.findByIdUnico(idUnico);
	}
	
	public Page<NaturezaOperacaoDtoList> buscarTodos(Pageable pageable) {
		logger.info("Listando todas Natureza Operacao...");	
		Page<NaturezaOperacao> naturezaOperacaoPage = naturezaOperacaoRep.findByDataInativacaoNull(pageable);
		
		List<NaturezaOperacaoDtoList>  naturezaLista = new ArrayList<>();
		for(NaturezaOperacao naturezaOperacao: naturezaOperacaoPage.getContent()) {
			NaturezaOperacaoDtoList naturezaOperacaoDtoList = new NaturezaOperacaoDtoList();
			BeanUtils.copyProperties(naturezaOperacao, naturezaOperacaoDtoList);
			naturezaLista.add(naturezaOperacaoDtoList);
		}
		Page<NaturezaOperacaoDtoList> naturezaOperacaoDtoList = new PageImpl<NaturezaOperacaoDtoList>(naturezaLista, pageable, naturezaOperacaoPage.getTotalElements());
		return naturezaOperacaoDtoList;
	}
	
	public Page<NaturezaOperacaoDtoList> buscarTodos(String filter, Pageable pageable) {
		logger.info("Listando todas Natureza Operacao...");	
		Page<NaturezaOperacao> naturezaOperacaoPage = naturezaOperacaoRep.findAll(
				NaturezaOperacaoSpecs.doFilter(filter)
				.and(NaturezaOperacaoSpecs.doSituacao(Situacao.ATIVO)), 
				pageable);
		
		List<NaturezaOperacaoDtoList>  naturezaLista = new ArrayList<>();
		for(NaturezaOperacao naturezaOperacao: naturezaOperacaoPage.getContent()) {
			NaturezaOperacaoDtoList naturezaOperacaoDtoList = new NaturezaOperacaoDtoList();
			BeanUtils.copyProperties(naturezaOperacao, naturezaOperacaoDtoList);
			naturezaLista.add(naturezaOperacaoDtoList);
		}
		Page<NaturezaOperacaoDtoList> naturezaOperacaoDtoList = new PageImpl<NaturezaOperacaoDtoList>(naturezaLista, pageable, naturezaOperacaoPage.getTotalElements());
		return naturezaOperacaoDtoList;
	}
			
	public NaturezaOperacao buscarPorEstabelecimento(String estabelecimento) {
		return naturezaOperacaoRep.buscarPorEstabelecimento(estabelecimento);
	}

	public NaturezaOperacaoDto buscarIdNaturezaOperacao(Long id) {
		logger.info("Buscando natureza operacao por id...");
		NaturezaOperacao naturezaOperacao = naturezaOperacaoRep.getReferenceById(id);
		if(naturezaOperacao == null) throw new NullPointerException("Não é possivel localizar a natureza");
		
		NaturezaOperacaoDto naturezaOperacaoDto = new NaturezaOperacaoDto();
		BeanUtils.copyProperties(naturezaOperacao, naturezaOperacaoDto);	
		
		List<EstabelecimentoCodigoDto> estabelecimentosDtoList = new ArrayList<>();
		for(NaturezaOperacaoEstabelecimento naturezaOperacaoEstabelecimento: naturezaOperacaoEstabelecimentoRep.findByNaturezaOperacao(naturezaOperacao)){			
		    EstabelecimentoCodigoDto estabelecimentoDto = new EstabelecimentoCodigoDto();
			BeanUtils.copyProperties(naturezaOperacaoEstabelecimento.getEstabelecimento(), estabelecimentoDto);
			estabelecimentosDtoList.add(estabelecimentoDto);
		}
		naturezaOperacaoDto.setListEstabelecimentoDto(estabelecimentosDtoList);
		
		return naturezaOperacaoDto;
	}
	
	public Page<EstabelecimentoDto> buscarIdNaturezaOperacaoEstabelecimentos(Long id, Pageable pageable) {
		logger.info("Buscando natureza operacao por id...");	
		Page<NaturezaOperacaoEstabelecimento> naturezaOperacaoEstabelecimentoPage = naturezaOperacaoEstabelecimentoRep.findByNaturezaOperacaoIdOrderByEstabelecimentoCodigo(id,  pageable);
		
		List<EstabelecimentoDto> estabelecimentosDtoList = new ArrayList<>();
		for(NaturezaOperacaoEstabelecimento naturezaOperacaoEstabelecimento: naturezaOperacaoEstabelecimentoPage.getContent()){			
			EstabelecimentoDto estabelecimentoDto = new EstabelecimentoDto();
			BeanUtils.copyProperties(naturezaOperacaoEstabelecimento.getEstabelecimento(), estabelecimentoDto);
			estabelecimentosDtoList.add(estabelecimentoDto);
		}
		
		Page<EstabelecimentoDto> estabelecimentoDtoPage = new PageImpl<>(
				estabelecimentosDtoList, pageable, naturezaOperacaoEstabelecimentoPage.getTotalElements()
		);

		return estabelecimentoDtoPage;
	}
	
	public NaturezaOperacaoEstabelecimento findByNaturezaOperacaoEstabelecimentoId(Long id) {
		return naturezaOperacaoEstabelecimentoRep.getReferenceById(id);
	}
	
	public void deletar(Long id) throws Exception {
		logger.info("Deletando natureza operacao... Aguarde!");
		NaturezaOperacao naturezaOperacao = naturezaOperacaoRep.getReferenceById(id);
		naturezaOperacaoEstabelecimentoRep.deleteByNaturezaOperacao(naturezaOperacao);
		naturezaOperacaoRep.delete(naturezaOperacao);
	}
	
	public void deletarEstabelecimento(Long id) throws Exception {
		logger.info("Deletando estabelecimento natureza operação... Aguarde!");
		NaturezaOperacaoEstabelecimento naturezaOperacaoEstabelecimento = naturezaOperacaoEstabelecimentoRep.getReferenceById(id);
		if(naturezaOperacaoEstabelecimento != null) {	
			naturezaOperacaoEstabelecimentoRep.delete(naturezaOperacaoEstabelecimento);
		}
	}
	
	public void deletarMovimento(Long id) throws Exception {
		logger.info("Deletando movimento natureza operação... Aguarde!");
		NaturezaOperacaoMovimentacao naturezaOperacaoMovimento = naturezaMovimentacaoRep.getReferenceById(id);
		if(naturezaOperacaoMovimento != null) {
			naturezaMovimentacaoRep.delete(naturezaOperacaoMovimento);
		}
	}
	
	public void inativarNaturezaOperacao(Long id) throws Exception {
		logger.info("Inativando natureza operacao por id...");
		NaturezaOperacao naturezaOperacao = naturezaOperacaoRep.getReferenceById(id);
		if(naturezaOperacao == null) throw new NullPointerException("Não é possivel inativar a natureza");
		naturezaOperacao.setDataInativacao(new Date());
		naturezaOperacaoRep.save(naturezaOperacao);
	}
	
	public void ativarNaturezaOperacao(Long id) throws Exception {
		logger.info("Ativando natureza operacao por id...");
		NaturezaOperacao naturezaOperacao = naturezaOperacaoRep.getReferenceById(id);
		if(naturezaOperacao == null) throw new NullPointerException("Não é possivel ativar a natureza"); 
		naturezaOperacao.setDataInativacao(null);
		naturezaOperacaoRep.save(naturezaOperacao);
	}
	
	public NaturezaOperacao findByCodGrupo(Integer codGrupo) {
	    logger.info("Buscando natureza operação por código: " + codGrupo);
	    NaturezaOperacao naturezaOperacao = naturezaOperacaoRep.findByCodGrupo(codGrupo);

        if(naturezaOperacao == null) {
            logger.info("Não foi encontrado natureza com o código " + codGrupo);
        }
        return naturezaOperacao;
	}
	
	public void save (NaturezaOperacao naturezaOperacao) {
		naturezaOperacaoRep.save(naturezaOperacao);	
	}
	
	public void saveEstabelecimento (NaturezaOperacaoEstabelecimento naturezaOperacaoEstabelecimento) {
		naturezaOperacaoEstabelecimentoRep.save(naturezaOperacaoEstabelecimento);	
	}
	
	public void saveMovimentacao (NaturezaOperacaoMovimentacao naturezaOperacaoMovimentacao) {
		naturezaMovimentacaoRep.save(naturezaOperacaoMovimentacao);	
	}
}