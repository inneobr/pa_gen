package br.coop.integrada.api.pa.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.model.usuario.Usuario;
import br.coop.integrada.api.pa.domain.repository.UsuarioRep;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRep usuarioRep;

    public Usuario getUsuarioLogado() {
        logger.info("Consultando usuário logado...");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRep.findByUsernameIgnoreCase(username);

        if(usuario == null) {
            throw new ObjectNotFoundException("O usuário não foi encontrado! Username: " + username + ", Tipo: " + Usuario.class.getName());
        }

        return usuario;
    }
}
