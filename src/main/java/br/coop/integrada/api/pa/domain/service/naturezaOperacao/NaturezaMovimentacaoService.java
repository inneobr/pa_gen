package br.coop.integrada.api.pa.domain.service.naturezaOperacao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.coop.integrada.api.pa.aplication.utils.CompararObjetos;
import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacao;
import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacaoMovimentacao;
import br.coop.integrada.api.pa.domain.model.usuario.Usuario;
import br.coop.integrada.api.pa.domain.modelDto.naturezaOperacao.NaturezaOperacaoDto;
import br.coop.integrada.api.pa.domain.repository.naturezaOperacao.NaturezaOperacaoMovimentacaoRep;
import br.coop.integrada.api.pa.domain.repository.naturezaOperacao.NaturezaOperacaoRep;
import br.coop.integrada.api.pa.domain.service.UsuarioService;
import br.coop.integrada.api.pa.domain.service.pesagem.PesagemService;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor @Transactional
public class NaturezaMovimentacaoService {
	private static final Logger logger = LoggerFactory.getLogger(PesagemService.class);	
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private NaturezaOperacaoRep naturezaOperacaoRep;
	
	@Autowired
	private NaturezaOperacaoMovimentacaoRep naturezaOperacaoMovimentacaoRep;
	public void salvarHistoricoNatureza(String operacao, NaturezaOperacaoDto naturezaOperacaoDto) throws Exception{
		NaturezaOperacao naturezaOperacao = new NaturezaOperacao();
		Usuario usuario = usuarioService.getUsuarioLogado();
			
		NaturezaOperacao naturezaOperacaoBanco = naturezaOperacaoRep.findByCodGrupoOrDescricao(naturezaOperacaoDto.getCodGrupo(), naturezaOperacaoDto.getDescricao());
		BeanUtils.copyProperties(naturezaOperacaoDto, naturezaOperacao);
		
		NaturezaOperacaoMovimentacao movimentacao = new NaturezaOperacaoMovimentacao();		
		movimentacao.setUsuario(usuario.getUsername());
		movimentacao.setMovimento(operacao);
		movimentacao.setCodGrupo(naturezaOperacao.getCodGrupo());
		movimentacao.setObservacao("Usuário (" + usuario.getUsername() + ") (" + CompararObjetos.comparar(naturezaOperacaoBanco, naturezaOperacao)+" )");
		logger.info("Salvando histórico natureza operação...");		
		naturezaOperacaoMovimentacaoRep.save(movimentacao);
	}
	
	public Page<NaturezaOperacaoMovimentacao> buscarHistoricoNatureza(Integer codGrupo, Pageable pageable) {
		logger.info("Buscando histórico natureza operação: {}", codGrupo);
		Page<NaturezaOperacaoMovimentacao> naturezaMovimentacaoPages = naturezaOperacaoMovimentacaoRep.findByCodGrupoOrderByDataCadastroDesc(codGrupo, pageable);
		return naturezaMovimentacaoPages;
	}
	
	public NaturezaOperacaoMovimentacao findByIdUnico(String idUnico) {
		return naturezaOperacaoMovimentacaoRep.findByIdUnico(idUnico);
	}
}
