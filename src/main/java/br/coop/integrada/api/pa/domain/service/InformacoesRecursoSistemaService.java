package br.coop.integrada.api.pa.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.coop.integrada.api.pa.domain.enums.PaginaAreaEnum;
import br.coop.integrada.api.pa.domain.enums.PaginaEnum;
import br.coop.integrada.api.pa.domain.model.InformacoesRecursoSistema;
import br.coop.integrada.api.pa.domain.modelDto.informacaoRecursoSistema.PaginaAreaDto;
import br.coop.integrada.api.pa.domain.repository.InformacoesRecursoSistemaRep;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor @Transactional
public class InformacoesRecursoSistemaService {
	
	private final InformacoesRecursoSistemaRep informacoesRecursoSistemaRep;
	
	public Optional<InformacoesRecursoSistema> unico(Long id) {
		return informacoesRecursoSistemaRep.findById(id);
	}
	
	public void salvar(InformacoesRecursoSistema recursoSistemaDto ) {
		InformacoesRecursoSistema recursoSistema = informacoesRecursoSistemaRep.findByPaginaArea(recursoSistemaDto.getPaginaArea());
		if(recursoSistema == null) recursoSistema = new InformacoesRecursoSistema();
		BeanUtils.copyProperties(recursoSistemaDto, recursoSistema);
		informacoesRecursoSistemaRep.save(recursoSistema);
	}
	
	public void ativar(Long id) {
		Optional<InformacoesRecursoSistema> informacoesRecursoSistema = informacoesRecursoSistemaRep.findById(id);
		if(informacoesRecursoSistema.get() != null) {
			informacoesRecursoSistema.get().setDataInativacao(null);
			informacoesRecursoSistemaRep.save(informacoesRecursoSistema.get());
		}
		
	}
	
	public void inativar(Long id) {
		Optional<InformacoesRecursoSistema> informacoesRecursoSistema = informacoesRecursoSistemaRep.findById(id);
		if(informacoesRecursoSistema.get() != null) {
			informacoesRecursoSistema.get().setDataInativacao(new Date());
			informacoesRecursoSistemaRep.save(informacoesRecursoSistema.get());
		}
		
	}
	
	public List<PaginaAreaDto> buscarPaginas(Boolean possuiInformativoSistema){
		List<PaginaAreaDto> listaPagina = new  ArrayList<>();
		
		for(PaginaEnum op : PaginaEnum.values()){
			PaginaAreaDto paginaAreaDto = new PaginaAreaDto();
			
			if(possuiInformativoSistema == null){
				paginaAreaDto.setChave(op.name());
				paginaAreaDto.setValor(op.getDescricao());				
				listaPagina.add(paginaAreaDto);
			}
			else if(  op.getPossuiInformativoSistema() && possuiInformativoSistema){			
				paginaAreaDto.setChave(op.name());
				paginaAreaDto.setValor(op.getDescricao());				
				listaPagina.add(paginaAreaDto);
				
			}else if(!op.getPossuiInformativoSistema() && !possuiInformativoSistema ){				
				paginaAreaDto.setChave(op.name());
				paginaAreaDto.setValor(op.getDescricao());				
				listaPagina.add(paginaAreaDto);
			}
		}		
		return listaPagina;
	}

	public List<PaginaAreaDto> buscarArea(String pagina){		
		List<PaginaAreaDto> listaArea = new ArrayList<>();		
		for(PaginaAreaEnum op : PaginaAreaEnum.values() ) {
			if(op.getPagina().name().equals( pagina ) ){				
				PaginaAreaDto paginaAreaDto = new PaginaAreaDto();				
				paginaAreaDto.setChave(op.name());
				paginaAreaDto.setValor(op.getArea());				
				listaArea.add(paginaAreaDto);				
			}
		}		
		return listaArea;
	}

	
	public InformacoesRecursoSistema buscarInformacaoModal(PaginaAreaEnum paginaArea) {			
		InformacoesRecursoSistema recursoSistema = informacoesRecursoSistemaRep.findByPaginaArea(paginaArea);
		if(recursoSistema == null) throw new NullPointerException("Pagina ou área não encontrada.");		
		return recursoSistema;
		
	}
	
	@SuppressWarnings("unused")
    public Page<InformacoesRecursoSistema> buscarTodosList(Pageable pageable) {
		Page<InformacoesRecursoSistema> recursoSistema = informacoesRecursoSistemaRep.findAll(pageable);
		if(recursoSistema.isEmpty()) throw new NullPointerException("Pagina ou área não encontrada.");
		for(InformacoesRecursoSistema op : recursoSistema){
			String paginaArea = op.getPaginaArea().name();
		}				
		return recursoSistema;
	}
	
	
	public Page<InformacoesRecursoSistema> buscarTodosAtivoList(Pageable pageable) {
		Page<InformacoesRecursoSistema> recursoSistema = informacoesRecursoSistemaRep.findByDataInativacaoNull(pageable);
		if(recursoSistema.isEmpty()) throw new NullPointerException("Não existem paginas cadastradas.");
		return recursoSistema;
	}

	public PaginaAreaDto buscarPaginaByPaginaArea(String paginaArea) {
		
		for(PaginaAreaEnum op : PaginaAreaEnum.values() ) {
			if(op.name().equals( paginaArea ) ){
				for(PaginaEnum paginaEnum : PaginaEnum.values()){
					if(paginaEnum.name().equals( op.getPagina().name() ) ){
						PaginaAreaDto paginaAreaDto = new PaginaAreaDto();
				
						paginaAreaDto.setChave(paginaEnum.name());
						paginaAreaDto.setValor(paginaEnum.getDescricao());
				
						return paginaAreaDto;
					}
				}
				
			}
		}
		
		return null;
	}
	
}
