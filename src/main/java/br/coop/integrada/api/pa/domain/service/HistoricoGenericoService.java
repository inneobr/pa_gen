package br.coop.integrada.api.pa.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.model.HistoricoGenerico;
import br.coop.integrada.api.pa.domain.modelDto.HistoricoGenericoDto;
import br.coop.integrada.api.pa.domain.repository.HistoricoGenericoRep;

@Service
public class HistoricoGenericoService {

    @Autowired  
    private HistoricoGenericoRep repo;

    public void salvar(Long idRegistro, PaginaEnum paginaEnum, String movimento, String observacao) {
        HistoricoGenerico historicoGenerico = new HistoricoGenerico();
        historicoGenerico.setRegistro(idRegistro);
        historicoGenerico.setPaginaEnum(paginaEnum);
        historicoGenerico.setObservacao(observacao);
        historicoGenerico.setMovimento(movimento);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(securityContext != null && securityContext.getAuthentication() != null 
        		&& securityContext.getAuthentication().getName() != null) {
        	String usarname = securityContext.getAuthentication().getName();
            historicoGenerico.setUsername(usarname);
        }
        else {
        	historicoGenerico.setUsername("Automático");
        }

        repo.save(historicoGenerico);
    }

    public Page<HistoricoGenericoDto> buscarPorRegistroEPagina(Long idRegistro, PaginaEnum paginaEnum, Pageable pageable) {
        Page<HistoricoGenerico> historicoGenericoPage = repo.findByRegistroAndPaginaEnumOrderByDataCadastroDesc(idRegistro, paginaEnum, pageable);
        List<HistoricoGenericoDto> historicoGenericoDtos = HistoricoGenericoDto.construir(historicoGenericoPage.getContent());
        return new PageImpl<>(historicoGenericoDtos, pageable, historicoGenericoPage.getTotalElements());
    }
}
