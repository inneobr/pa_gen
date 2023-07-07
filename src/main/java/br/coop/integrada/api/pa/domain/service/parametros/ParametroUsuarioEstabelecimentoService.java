package br.coop.integrada.api.pa.domain.service.parametros;

import static br.coop.integrada.api.pa.domain.enums.FuncaoEstabelecimento.Agendamento;
import static br.coop.integrada.api.pa.domain.enums.FuncaoEstabelecimento.Alterar_Contrato;
import static br.coop.integrada.api.pa.domain.enums.FuncaoEstabelecimento.Alterar_Padronizado;
import static br.coop.integrada.api.pa.domain.enums.FuncaoEstabelecimento.Baixa_Contrato_massa;
import static br.coop.integrada.api.pa.domain.enums.FuncaoEstabelecimento.Cancelar_Fechamento;
import static br.coop.integrada.api.pa.domain.enums.FuncaoEstabelecimento.Cancelar_RE;
import static br.coop.integrada.api.pa.domain.enums.FuncaoEstabelecimento.Encerramento_Diario;
import static br.coop.integrada.api.pa.domain.enums.FuncaoEstabelecimento.Entrada_RE;
import static br.coop.integrada.api.pa.domain.enums.FuncaoEstabelecimento.Fechamento;
import static br.coop.integrada.api.pa.domain.enums.FuncaoEstabelecimento.Liberar_Senha;
import static br.coop.integrada.api.pa.domain.enums.FuncaoEstabelecimento.Retirada_Produto;
import static br.coop.integrada.api.pa.domain.enums.FuncaoEstabelecimento.Transferencia;
import static br.coop.integrada.api.pa.domain.enums.FuncaoEstabelecimento.Transferencia_RE_Cooperativa;
import static br.coop.integrada.api.pa.domain.enums.integration.OrigemInputEnum.GENESIS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.aplication.utils.CompararObjetos;
import br.coop.integrada.api.pa.domain.enums.FuncaoEstabelecimento;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.integration.IntegracaoPagina;
import br.coop.integrada.api.pa.domain.model.parametros.ParametrosUsuarioEstabelecimento;
import br.coop.integrada.api.pa.domain.model.usuario.Usuario;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.EstabelecimentoResumidoDto;
import br.coop.integrada.api.pa.domain.modelDto.estabelecimento.ParametrosUsuarioEstabelecimentoFilter;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.ParametroUsuarioEstabelecimentoFiltro;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.ParametrosUsuarioEstabelecimentoDto;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.ParametrosUsuarioEstabelecimentoIntegrationSaveDto;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.ParametrosUsuarioEstabelecimentoSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.parametroEstabelecimento.UsuarioEstabelecimentoFuncaoDto;
import br.coop.integrada.api.pa.domain.repository.EstabelecimentoRep;
import br.coop.integrada.api.pa.domain.repository.UsuarioRep;
import br.coop.integrada.api.pa.domain.repository.parametros.ParametroUsuarioEstabelecimentoRep;
import br.coop.integrada.api.pa.domain.service.HistoricoGenericoService;
import br.coop.integrada.api.pa.domain.service.UsuarioService;
import br.coop.integrada.api.pa.domain.spec.ParametroUsuarioEstabelecimentoSpecs;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

@Service
public class ParametroUsuarioEstabelecimentoService {
	private static final Logger logger = LoggerFactory.getLogger(ParametroUsuarioEstabelecimentoService.class);
	
	@Autowired
    private HistoricoGenericoService historicoGenericoService;
	
	@Autowired
	private ParametroUsuarioEstabelecimentoRep parametroRep;
	
	@Autowired
	private EstabelecimentoRep estabelecimentoRep;
	
	@Autowired
	private UsuarioRep usuarioRep;
	
	@Autowired
	private UsuarioService usuarioService;
	
	public UsuarioEstabelecimentoFuncaoDto findParametroByUsuarioEstabelecimentoFuncao(String estabelecimentoCod, String usuarioCod, FuncaoEstabelecimento funcao) {
		ParametrosUsuarioEstabelecimento parametro = parametroRep.findByUsuarioCodUsuarioContainingIgnoreCaseAndEstabelecimentoCodigo(usuarioCod, estabelecimentoCod);
		
		if(parametro == null) {
			UsuarioEstabelecimentoFuncaoDto response = new UsuarioEstabelecimentoFuncaoDto();
			response.setPermite(false);
			response.setMessage("Não existe permissão cadastrada para o usuário no estabelecimento.");
			return response;
		}		

		if(Entrada_RE.equals(funcao)) {
			UsuarioEstabelecimentoFuncaoDto response = new UsuarioEstabelecimentoFuncaoDto();
			if(parametro.getRe()) {
				response.setPermite(true);
				return response;
			}else {
				response.setPermite(false);
				response.setMessage("Usuário sem permissão de entradade RE no estabelecimento");
				return response;
			}
		}
		
		if(Alterar_Padronizado.equals(funcao)) {
			UsuarioEstabelecimentoFuncaoDto response = new UsuarioEstabelecimentoFuncaoDto();
			if(parametro.getAlteraPadrao()) {
				response.setPermite(true);
				return response;
			}else {
				response.setPermite(false);
				response.setMessage("Usuário sem permissão de Alterar Produto Padronizado  no estabelecimento");
				return response;
			}
		}
		
		if(Cancelar_RE.equals(funcao)) {
			UsuarioEstabelecimentoFuncaoDto response = new UsuarioEstabelecimentoFuncaoDto();
			if(parametro.getCancelaRe()) {
				response.setPermite(true);
				return response;
			}else {
				response.setPermite(false);
				response.setMessage("Usuário sem permissão de Cancelamento de RE no estabelecimento");
				return response;
			}
		}
		
		if(Fechamento.equals(funcao)) {
			UsuarioEstabelecimentoFuncaoDto response = new UsuarioEstabelecimentoFuncaoDto();
			if(parametro.getFechamento()) {
				response.setPermite(true);
				return response;
			}else {
				response.setPermite(false);
				response.setMessage("Usuário sem permissão de Fixação no estabelecimento");
				return response;
			}
		}
		
		if(Agendamento.equals(funcao)) {
			UsuarioEstabelecimentoFuncaoDto response = new UsuarioEstabelecimentoFuncaoDto();
			if(parametro.getAgendamento()) {
				response.setPermite(true);
				return response;
			}else {
				response.setPermite(false);
				response.setMessage("Usuário sem permissão de Agendamento no estabelecimento");
				return response;
			}
		}
		
		if(Retirada_Produto.equals(funcao)) {
			UsuarioEstabelecimentoFuncaoDto response = new UsuarioEstabelecimentoFuncaoDto();
			if(parametro.getRetirada()) {
				response.setPermite(true);
				return response;
			}else {
				response.setPermite(false);
				response.setMessage("Usuário sem permissão de Retirada de Produto no estabelecimento");
				return response;
			}
		}
		
		if(Cancelar_Fechamento.equals(funcao)) {
			UsuarioEstabelecimentoFuncaoDto response = new UsuarioEstabelecimentoFuncaoDto();
			if(parametro.getCancelaFechamento()) {
				response.setPermite(true);
				return response;
			}else {
				response.setPermite(false);
				response.setMessage("Usuário sem permissão de Cancelamento de Fixação no estabelecimento");
				return response;
			}
		}
		
		if(Liberar_Senha.equals(funcao)) {
			UsuarioEstabelecimentoFuncaoDto response = new UsuarioEstabelecimentoFuncaoDto();
			if(parametro.getLiberaSenha()) {
				response.setPermite(true);
				return response;
			}else {
				response.setPermite(false);
				response.setMessage("Usuário sem permissão de Liberação de Senha no estabelecimento");
				return response;
			}
		}
		
		if(Encerramento_Diario.equals(funcao)) {
			UsuarioEstabelecimentoFuncaoDto response = new UsuarioEstabelecimentoFuncaoDto();
			if(parametro.getEncerDiario()) {
				response.setPermite(true);
				return response;
			}else {
				response.setPermite(false);
				response.setMessage("Usuário sem permissão de Encerramento Diário no estabelecimento");
				return response;
			}
		}
		
		if(Transferencia.equals(funcao)) {
			UsuarioEstabelecimentoFuncaoDto response = new UsuarioEstabelecimentoFuncaoDto();
			if(parametro.getTransferencia()) {
				response.setPermite(true);
				return response;
			}else {
				response.setPermite(false);
				response.setMessage("Usuário sem permissão deTransferência no estabelecimento");
				return response;
			}
		}
		
		if(Transferencia_RE_Cooperativa.equals(funcao)) {
			UsuarioEstabelecimentoFuncaoDto response = new UsuarioEstabelecimentoFuncaoDto();
			if(parametro.getTransfCooperado()) {
				response.setPermite(true);
				return response;
			}else {
				response.setPermite(false);
				response.setMessage("Usuário sem permissão de Transferir RE da Cooperativa no estabelecimento");
				return response;
			}
		}
		
		if(Alterar_Contrato.equals(funcao)) {
			UsuarioEstabelecimentoFuncaoDto response = new UsuarioEstabelecimentoFuncaoDto();
			if(parametro.getAlteraContrato()) {
				response.setPermite(true);
				return response;
			}else {
				response.setPermite(false);
				response.setMessage("Usuário sem permissão de Alterart Contrato no estabelecimento");
				return response;
			}
		}
		
		if(Baixa_Contrato_massa.equals(funcao)) {
			UsuarioEstabelecimentoFuncaoDto response = new UsuarioEstabelecimentoFuncaoDto();
			if(parametro.getBaixaMassaContrato()) {
				response.setPermite(true);
				return response;
			}else {
				response.setPermite(false);
				response.setMessage("Usuário sem permissão de Baixa de Contrato em Massa no estabelecimento");
				return response;
			}
		}
		
		return null;
	}
	
	public void salvar(ParametrosUsuarioEstabelecimentoDto objDto) throws Exception {
		logger.info("Salvando novo parametro usuario estabelecimentos... {}");
		validarDuplicados(objDto);
		Usuario usuario = usuarioRep.findByUsernameIgnoreCase(objDto.getUsuario().getUsername());
		if(usuario == null) {
			throw new ObjectDefaultException("Usúario não encontrado!");
		}	
		
		for(EstabelecimentoResumidoDto item:  objDto.getEstabelecimentos()) {	
			System.out.println(item.getCodNome());
			ParametrosUsuarioEstabelecimento obj = new ParametrosUsuarioEstabelecimento();
			BeanUtils.copyProperties(objDto, obj);
			
			Estabelecimento estabelecimento = estabelecimentoRep.findByIdOrCodigo(item.getId(), item.getCodigo());
			obj.setEstabelecimento(estabelecimento);
			obj.setUsuario(usuario);
			obj.setDataIntegracao(new Date());		
			obj = parametroRep.save(obj);
			String historico = CompararObjetos.cadastrar(obj);
			historicoGenericoService.salvar(obj.getId(), PaginaEnum.PARAMETRO_USUARIO_ESTABELECIMENTO,  obj.getUsuario().getNome() +" Recebeu novos parametros de estabelecimento", historico + "para "+ obj.getEstabelecimento().getNomeFantasia());
		}
	}
	
	public ParametrosUsuarioEstabelecimento buscarPorUsuarioAutenticadoECodigoEstabelecimento(String codigoEstabelecimento) {
		Usuario usuario = usuarioService.getUsuarioLogado();
		Optional<ParametrosUsuarioEstabelecimento> parametros = parametroRep.findOne(
				ParametroUsuarioEstabelecimentoSpecs.usernameEquals(usuario.getUsername())
				.and(ParametroUsuarioEstabelecimentoSpecs.codigoEstabelecimentoEquals(codigoEstabelecimento)));
		return parametros.orElseThrow(() -> new ObjectNotFoundException("Não foi encontrado parâmetro de usuário vs estabelecimento com os parâmetros informados. (Usuario: " + usuario.getUsername() + " e código estabelecimento: " + codigoEstabelecimento + ")"));
	}
	
	public void atualizar(ParametrosUsuarioEstabelecimento parametros) throws Exception {		
		logger.info("Atualizando parametro usuario estabelecimentos...");
		if(parametros.getId() == null) throw new NullPointerException("ID obrigatorio para atualizações de parametros");
		ParametrosUsuarioEstabelecimento obj = parametroRep.getReferenceById(parametros.getId());
		ParametrosUsuarioEstabelecimento old = new ParametrosUsuarioEstabelecimento();
		BeanUtils.copyProperties(obj, old);
		
		parametros.setUsuario(obj.getUsuario());
		BeanUtils.copyProperties(parametros, obj);	
		obj.setDataIntegracao(new Date());
		obj = parametroRep.save(obj);
		
		String observacao = CompararObjetos.comparar(obj, old);
		historicoGenericoService.salvar(obj.getId(), PaginaEnum.PARAMETRO_USUARIO_ESTABELECIMENTO, "Atualizou os parametros do usúario "+ obj.getUsuario().getNome(), observacao + " para " + obj.getEstabelecimento().getNomeFantasia());
	}

	public ParametrosUsuarioEstabelecimento cadastrarOuAtualizar(ParametrosUsuarioEstabelecimentoIntegrationSaveDto objDto, IntegracaoPagina pagina) throws Exception {
		//logger.info("Gravando parâmetro de usuário x estabelecimento...");
		ParametrosUsuarioEstabelecimento parametrosUsuarioEstabelecimento = parametroRep.findByCodigoUsuarioAndCodigoEstabelecimento(
				objDto.getCodigoUsuario(),
				objDto.getCodigoEstabelecimento()
				
		);

		if(parametrosUsuarioEstabelecimento == null) {
			parametrosUsuarioEstabelecimento = new ParametrosUsuarioEstabelecimento();
		}


		Boolean isCadastro = parametrosUsuarioEstabelecimento.getId() == null;

		var parametrosUsuarioEstabelecimentoNovo = new ParametrosUsuarioEstabelecimento();
		BeanUtils.copyProperties(parametrosUsuarioEstabelecimento, parametrosUsuarioEstabelecimentoNovo);
		BeanUtils.copyProperties(objDto, parametrosUsuarioEstabelecimentoNovo);

		String observacao = "";
		if(isCadastro) {
			observacao = CompararObjetos.cadastrar(parametrosUsuarioEstabelecimentoNovo);
			
			parametrosUsuarioEstabelecimentoNovo.setUsuario(usuarioRep.findByCodUsuarioIgnoreCase(objDto.getCodigoUsuario()));
			parametrosUsuarioEstabelecimentoNovo.setEstabelecimento(estabelecimentoRep.findByCodigo(objDto.getCodigoEstabelecimento()));
			
			if(parametrosUsuarioEstabelecimentoNovo.getUsuario() == null) {
				throw new ObjectNotFoundException("Usuário não encontrado.");
			}
			
			if(parametrosUsuarioEstabelecimentoNovo.getEstabelecimento() == null) {
				throw new ObjectNotFoundException("Estabelecimento não encontrado.");
			}
			
		}
		else {
			observacao = CompararObjetos.comparar(parametrosUsuarioEstabelecimento, parametrosUsuarioEstabelecimentoNovo);
		}
		
    	if(pagina != null && GENESIS.equals(pagina.getOrigenEnum())) {
    		parametrosUsuarioEstabelecimentoNovo.setStatusIntegracao(StatusIntegracao.INTEGRAR);
    		parametrosUsuarioEstabelecimentoNovo.setDataIntegracao(null);
    	}else{
    		parametrosUsuarioEstabelecimentoNovo.setStatusIntegracao(StatusIntegracao.INTEGRADO);
    		parametrosUsuarioEstabelecimentoNovo.setDataIntegracao(new Date());
    	}

    	parametrosUsuarioEstabelecimentoNovo = parametroRep.save(parametrosUsuarioEstabelecimentoNovo);

		if(isCadastro) {
			historicoGenericoService.salvar(
					parametrosUsuarioEstabelecimentoNovo.getId(),
					PaginaEnum.PARAMETRO_USUARIO_ESTABELECIMENTO,
					parametrosUsuarioEstabelecimentoNovo.getUsuario().getNome() + " Recebeu novos parâmetros de estabelecimento",
					!Strings.isEmpty(observacao) ? observacao : "Operação realizada com  sucesso."
			);
		}
		else {
			historicoGenericoService.salvar(
					parametrosUsuarioEstabelecimentoNovo.getId(),
					PaginaEnum.PARAMETRO_USUARIO_ESTABELECIMENTO,
					"Atualizou os parâmetros do usúario " + parametrosUsuarioEstabelecimentoNovo.getUsuario().getNome(),
					!Strings.isEmpty(observacao) ? observacao : "Operação realizada com  sucesso."
			);
		}

		return parametrosUsuarioEstabelecimentoNovo;
	}

	public Page<ParametrosUsuarioEstabelecimentoSimplesDto> findAll(Long id, Pageable pageable) {	
		logger.info("Buscando todos os parametros usuario estabelecimentos...");
		Usuario usuario = usuarioRep.getReferenceById(id);
		Page<ParametrosUsuarioEstabelecimento> parametro =  parametroRep.findByUsuarioOrderByEstabelecimentoIdAsc(usuario, pageable);
		if(parametro.isEmpty()) throw new NullPointerException("Usúario não possui parametros cadastrados.");
		
		List<ParametrosUsuarioEstabelecimentoSimplesDto> lista = new ArrayList<>();
		for(ParametrosUsuarioEstabelecimento item: parametro.getContent()) {
			ParametrosUsuarioEstabelecimentoSimplesDto obj = new  ParametrosUsuarioEstabelecimentoSimplesDto();
			BeanUtils.copyProperties(item, obj);
			
			EstabelecimentoResumidoDto estabelecimentoResumidoDto = new EstabelecimentoResumidoDto();
			BeanUtils.copyProperties(item.getEstabelecimento(), estabelecimentoResumidoDto);
			obj.setEstabelecimento(estabelecimentoResumidoDto);
			lista.add(obj);
		}
		
		Page<ParametrosUsuarioEstabelecimentoSimplesDto> page = new PageImpl<>(
				lista, pageable, parametro.getTotalElements()
		);
		return page;
	}
	
	public Page<ParametrosUsuarioEstabelecimentoSimplesDto> findParamByFilter(ParametrosUsuarioEstabelecimentoFilter filter, Pageable pageable) {	
		logger.info("Filtrando parâmetros de usuario x estabelecimentos, aguarde! {}", filter.getUsuario() );
		Page<ParametrosUsuarioEstabelecimento> parametroPage = null;
		if(filter.getUsuario() == null) {
			throw new ObjectNotFoundException("Usúario é obrigatório!");
		}

		parametroPage = parametroRep.findAll(
				ParametroUsuarioEstabelecimentoSpecs.doCodUsuarioEquals(filter.getUsuario())
				.and(ParametroUsuarioEstabelecimentoSpecs.codigoOuNomeFantasiaEstabelecimentoLike(filter.getEstabelecimento()))
				.and(ParametroUsuarioEstabelecimentoSpecs.doSituacao(filter.getSituacao())), pageable);
		
		if(parametroPage.isEmpty()) {
			logger.info("Usúario {} não possui parametros cadastrados  {} {}.", filter.getUsuario(), filter.getEstabelecimento(), filter.getSituacao());
			throw new NullPointerException("Usúario ( "+ filter.getUsuario() + " ) não possui parametros cadastrado para: ( "+ filter.getEstabelecimento() +" / "+ filter.getSituacao() + " )");
			
		}
		logger.info("Carregando {} parametros encontrado... agrarde!", parametroPage.getTotalElements());
		List<ParametrosUsuarioEstabelecimentoSimplesDto> parametrosResponse = new ArrayList<>();
		for(ParametrosUsuarioEstabelecimento item: parametroPage.getContent()) {
			ParametrosUsuarioEstabelecimentoSimplesDto parametro = new  ParametrosUsuarioEstabelecimentoSimplesDto();
			EstabelecimentoResumidoDto estabelecimento = new EstabelecimentoResumidoDto();
			BeanUtils.copyProperties(item.getEstabelecimento(), estabelecimento);
			BeanUtils.copyProperties(item, parametro);
			parametro.setEstabelecimento(estabelecimento);
			parametrosResponse.add(parametro);
		}
		
		return new PageImpl<>(
			parametrosResponse, pageable, parametroPage.getTotalElements()
		);
		
	}
	
	private void validarDuplicados(ParametrosUsuarioEstabelecimentoDto parametros) {
	        List<String> duplicados = getParametroComMesmasInformacoes(parametros);
	        if(duplicados.size() > 0) {
	            StringBuilder resultado = new StringBuilder();

	            for (String vinculo : duplicados) {
	                resultado.append(vinculo).append("; ");
	            }

	            throw new RuntimeException("Parametro já cadastrado para o estabelecimento: " + resultado.toString());
	        }
	}
	
	private List<String> getParametroComMesmasInformacoes(ParametrosUsuarioEstabelecimentoDto objDto) {	        
		List<ParametrosUsuarioEstabelecimento> listParametros = parametroRep.findByUsuarioOrderByEstabelecimentoCodigoAsc(objDto.getUsuario());
		
		List<String> duplicado = new ArrayList<>();
		for(EstabelecimentoResumidoDto estabelecimento: objDto.getEstabelecimentos()) {
			for(ParametrosUsuarioEstabelecimento parametro : listParametros) {
				if(parametro.getEstabelecimento().getId() == estabelecimento.getId()) {
					duplicado.add(parametro.getEstabelecimento().getNomeFantasia());
				}
			}
		}
		return duplicado;	       
	}
	
	public void deletar(Long idParametro) throws Exception {
		if(idParametro == null) {
			throw new ObjectNotFoundException("Id do parametro é obrigatório!");
		}
		logger.info("Deletando parametro usuario estabelecimentos...");
		ParametrosUsuarioEstabelecimento parametro = parametroRep.getReferenceById(idParametro);
		historicoGenericoService.salvar(parametro.getId(), PaginaEnum.PARAMETRO_USUARIO_ESTABELECIMENTO, "Deletou parametro", parametro.getEstabelecimento().getCodNome() + " do " + parametro.getUsuario().getNome());
		parametroRep.delete(parametro);		
		logger.info("Deletando: {}",  parametro.getEstabelecimento().getCodNome());
	}
	
	public void deletAll(String username) {
		Usuario usuario = usuarioRep.findByUsernameIgnoreCase(username);
		if(username == null) {
			throw new ObjectNotFoundException("Usúario é obrigatório!");
		}
		logger.info("Deletando todos os parâmetro usuário: {}", username);
		List<ParametrosUsuarioEstabelecimento> parametro = parametroRep.findByUsuario(usuario);
		for(ParametrosUsuarioEstabelecimento item: parametro) {
			logger.info("Deletendo: {}",  item.getEstabelecimento().getCodNome());
			historicoGenericoService.salvar(item.getId(), PaginaEnum.PARAMETRO_USUARIO_ESTABELECIMENTO, "Deletou parametro", item.getEstabelecimento().getCodNome() + " do " + item.getUsuario().getNome());
		}
		parametroRep.deleteAll(parametro);			
	}
	
	public List<Estabelecimento> estabelecimentosDisponivelParaUsuario(Long idUsuario) {
	    return estabelecimentoRep.buscarEstabelecimentosDisponiveisSilodoUsuario(idUsuario);
	}

	public Page<ParametrosUsuarioEstabelecimento> buscarPorUsuarioAutenticado(ParametroUsuarioEstabelecimentoFiltro filtro, Situacao situacao, Pageable pageable) {
		Usuario usuario = usuarioService.getUsuarioLogado();
		Page<ParametrosUsuarioEstabelecimento> parametros = parametroRep.findAll(
				ParametroUsuarioEstabelecimentoSpecs.usernameEquals(usuario.getUsername())
				.and(ParametroUsuarioEstabelecimentoSpecs.codigoOuNomeFantasiaEstabelecimentoLike(filtro.getEstabelecimentoCodigoOuNomeFantasia()))
				.and(ParametroUsuarioEstabelecimentoSpecs.reEquals(filtro.getRe()))
				.and(ParametroUsuarioEstabelecimentoSpecs.doSituacao(situacao)),
				pageable);
		
		return parametros;
	}

	
	
	public Boolean copiarParametros(Long idOrigem, Long idDestino) throws Exception {
		logger.info("Buscando ID de usuário de origem e destino");	
		
		Usuario origem = usuarioRep.getReferenceById(idOrigem);
		if(origem == null) {
			throw new ObjectDefaultException("Usuário de origem não encontrado");
		}
		logger.info("Origem encontrado: {}", origem.getNome());
		
		Usuario destino = usuarioRep.getReferenceById(idDestino);
		if(destino == null) {
			throw new ObjectDefaultException("Usuário de destino não encontrado");
		}
		logger.info("Destino encontrado: {}", origem.getNome());
		
		logger.info("Carregando parâmetros do usuario de origem: {}", origem.getNome());	
		List<ParametrosUsuarioEstabelecimento> parametrosOrigem = parametroRep.findByUsuario(origem);
		if(parametrosOrigem.isEmpty()) {
			throw new ObjectDefaultException("Usuário não possui parâmetros cadastrados.");
		}
		
		logger.info("Parâmetros encontrados, iniciando a cópia");
		for(ParametrosUsuarioEstabelecimento parametro: parametrosOrigem) {	
			ParametrosUsuarioEstabelecimento parametroDestino = new ParametrosUsuarioEstabelecimento();
			Estabelecimento estabelecimento = estabelecimentoRep.findByCodigo(parametro.getEstabelecimento().getCodigo());
			BeanUtils.copyProperties(parametro, parametroDestino);
			parametroDestino.setId(isCadastrado(destino, estabelecimento));
			parametroDestino.setEstabelecimento(estabelecimento);
			parametroDestino.setUsuario(destino);
			parametroRep.save(parametroDestino);
			logger.info("Copiando parâmetro do estabelecimento: {}",estabelecimento.getCodNome());
		}
		logger.info("Parâmetros copiados com sucesso.");
		return true;
	}
	
	public Long isCadastrado(Usuario destino, Estabelecimento estabelecimento) {
		ParametrosUsuarioEstabelecimento parametroExistente = parametroRep.findByUsuarioAndEstabelecimento(destino, estabelecimento);
		if(parametroExistente != null) {
			logger.info("Parâmetros encontrado, atualizando.");
			return parametroExistente.getId();
		}
		return null;
	}
	
	public ParametrosUsuarioEstabelecimento findByCodigoUsuarioAndCodigoEstabelecimento(String codigoUsuario, String codigoEstabelecimento) {
		return parametroRep.findByCodigoUsuarioAndCodigoEstabelecimento(codigoUsuario, codigoEstabelecimento);
	}
		
	public void remove(ParametrosUsuarioEstabelecimento parametro) {
		parametroRep.delete(parametro);
	}

	public ParametrosUsuarioEstabelecimento atualizarDireto(ParametrosUsuarioEstabelecimento parametro) {
		return parametroRep.save(parametro);
	}
}
