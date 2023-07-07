package br.coop.integrada.api.pa.domain.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.usuario.Usuario;
import br.coop.integrada.api.pa.domain.modelDto.filtroDocumentos.ReponseFiltroDocumentos;
import br.coop.integrada.api.pa.domain.modelDto.filtroDocumentos.RequestFiltroDocumentos;
import br.coop.integrada.api.pa.domain.repository.EstabelecimentoRep;
import br.coop.integrada.api.pa.domain.repository.UsuarioRep;
import br.coop.integrada.api.pa.domain.service.naturezaTributaria.NaturezaTributariaService;


@Service @RequiredArgsConstructor
public class FiltroValidService {
	private final UsuarioRep usuarioRep;
	private final EstabelecimentoRep estabelecimentoRep;
	private static final Logger logger = LoggerFactory.getLogger(NaturezaTributariaService.class);
	
	public ReponseFiltroDocumentos getDocumentoValido(RequestFiltroDocumentos request) {
		if(request.getCpf() != null) {
			logger.info("Validando CPF: {}", request.getCpf());
			ReponseFiltroDocumentos response = new ReponseFiltroDocumentos();
			String cpf = request.getCpf().replace(".", "").replace("-", "").replace(",", "");
			List<Usuario> usuario = usuarioRep.findByCpf(cpf);
			if(usuario.isEmpty()) {
				response.setMessage("CPF não cadastrado");
				response.setBloqueado(false);
			}	
			
			if(!usuario.isEmpty()) {
				StringBuilder user = new StringBuilder();
				for(Usuario ocorrencia: usuario) {
					if(usuario.size() <= 1) {
						response.setMessage("CPF cadastrado para o Usuário: " + ocorrencia.getNome());
						response.setNomeCadastro(ocorrencia.getNome());
						response.setBloqueado(true);
						return response;
					}else {
						user.append(" ( "+ocorrencia.getCodUsuario() + " : " + ocorrencia.getNome() + " ) ");
					}
					
					
				}
				response.setMessage("CPF encontrado para os usuários: " + user + ", deseja proceguir?");
				response.setNomeCadastro("CPF possui mais de um cadastro ativo.");
				response.setBloqueado(true);
			}
			return response;
		}
	
		if(request.getUsername() != null) {
			logger.info("Validando username: {}", request.getUsername());
			ReponseFiltroDocumentos response = new ReponseFiltroDocumentos();
			Usuario usuario = usuarioRep.findByUsernameIgnoreCase(request.getUsername());
			
			if(usuario == null && request.getUsername() == null) {
				response.setMessage("Login disponivel");
				response.setBloqueado(false);
			}	
			
			if(usuario != null && request.getUsername() != null) {
				response.setMessage("Login cadastrado para o Usuário: " + usuario.getNome());
				response.setNomeCadastro(usuario.getNome());
				response.setBloqueado(true);
			}

			if(request != null && request.getUsername() != null) {
				Boolean isInvalid = false;
				for( int i=0; i<request.getUsername().length(); i++ ){
			        if( request.getUsername().charAt(i) == ' ' ) {
			        	isInvalid = true;
			        }
				}
				
				if(isInvalid) {
			        response.setMessage("Login possui espaços não pemitidos: " + request.getUsername());
					response.setNomeCadastro(request.getUsername());
					response.setBloqueado(true);
				}
			}
			return response;
		}
		
		if(request.getCnpj() != null) {
			logger.info("Validando CNPJ: {}", request.getCnpj());
			ReponseFiltroDocumentos response = new ReponseFiltroDocumentos();			
			Estabelecimento estabelecimento = estabelecimentoRep.findByCnpj(request.getCnpj());
			if(estabelecimento == null) {
				response.setMessage("CNPJ não cadastrado");
				response.setBloqueado(false);
			}	
			
			if(estabelecimento != null) {
				response.setMessage("CNPJ cadastrado para o Estabelecimento: " + estabelecimento.getRazaoSocial());
				response.setNomeCadastro(estabelecimento.getRazaoSocial());
				response.setBloqueado(true);
			}
			return response;
		}
		return null;	
	}

}
