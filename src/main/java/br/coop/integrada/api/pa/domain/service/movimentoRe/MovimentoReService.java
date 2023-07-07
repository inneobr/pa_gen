package br.coop.integrada.api.pa.domain.service.movimentoRe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.coop.integrada.api.pa.domain.model.movimentoRe.MovimentoRe;
import br.coop.integrada.api.pa.domain.model.movimentoRe.MovimentoReFiltro;
import br.coop.integrada.api.pa.domain.model.usuario.Usuario;
import br.coop.integrada.api.pa.domain.modelDto.movimentoRe.MovimentoReDto;
import br.coop.integrada.api.pa.domain.repository.movimentoRe.MovimentoReRep;
import br.coop.integrada.api.pa.domain.service.UsuarioService;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;

@Service @RequiredArgsConstructor @Transactional
public class MovimentoReService {
	
	private static final Logger logger = LoggerFactory.getLogger(MovimentoReService.class);
	private final MovimentoReRep movimentoReRep;
	
	@Autowired
	UsuarioService usuarioService;
			
	public Page<MovimentoRe> findByEstabelecimentoAndDataMovimento(Pageable pageable, MovimentoReFiltro filter){	
		Page<MovimentoRe> movimentoRe = movimentoReRep.findByCodEstabelAndDataBetween(
				filter.getCodEstabelecimento(), 
				filter.getDataInicio(),
				filter.getDataFinal(), 
				pageable);
		return movimentoRe;
	}	
	
	public Page<MovimentoRe> findByCodEstabel(String codEstabel, Pageable pageble){
		return movimentoReRep.findByCodEstabel(codEstabel, pageble);
	}
	
	public MovimentoRe cadastrarOuAtualizar(MovimentoReDto objDto) {	
		
		if(Strings.isEmpty(objDto.getCodEstabel())) {
			throw new ObjectNotFoundException("Campo {codEstabel} obrigatório");
		}
		
		if(objDto.getIdRe() == null) {
			throw new ObjectNotFoundException("Campo {idRe} obrigatório");
		}
		
		if(objDto.getIdMovRe() == null) {
			throw new ObjectNotFoundException("Campo {idMovRe} obrigatório");
		}
		
		if(objDto.getData() == null) {
			throw new ObjectNotFoundException("Campo {data} obrigatório");
		}
		
		MovimentoRe movimentoReAtual = movimentoReRep.findByCodEstabelAndIdReAndIdMovRe(
				objDto.getCodEstabel(),
				Long.parseLong(objDto.getIdRe()),
				objDto.getIdMovRe());
		
		if(movimentoReAtual != null) {
			logger.info("Atualizando o movimento da RE : "+ objDto.getIdMovRe());
			if(!movimentoReAtual.getCodEstabel().equals(objDto.getCodEstabel()) &&
					!movimentoReAtual.getIdMovRe().equals(objDto.getIdMovRe()) ) {
				throw new ObjectNotFoundException("O idMovRe informado já está registrado no sistema com outros parâmetros.");
			}
		}else {
			logger.info("Cadastrando o movimento RE : "+ objDto.getIdMovRe());
		}
				
		if(movimentoReAtual == null) {
			movimentoReAtual = new MovimentoRe();
		}
		
		BeanUtils.copyProperties(objDto, movimentoReAtual);
		movimentoReAtual.setIdRe(Long.parseLong(objDto.getIdRe()));
		
		return movimentoReRep.save(movimentoReAtual);		
	}
	
	public MovimentoRe findByCodEstabelAndIdRe(String codEstabel, Long idRe) {
		return movimentoReRep.findByCodEstabelAndIdRe(codEstabel, idRe);
	}
	
	public MovimentoRe salvar(MovimentoRe movimentoRe) {
		return movimentoReRep.save(movimentoRe);
	}
	
	//Objetivo: Criar movimento da RE 
	public MovimentoRe criarMovimentoRe(MovimentoReDto objDto) {
		
		MovimentoRe movimentoRe = new MovimentoRe();
		
		movimentoRe.setIdRe(Long.parseLong(objDto.getIdRe()));
		movimentoRe.setCodEstabel(objDto.getCodEstabel());
		movimentoRe.setNrRe(objDto.getNrRe());
		movimentoRe.setData( new Date() );
		
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");  
	    Date date = new Date();  
	    		
		movimentoRe.setHora( formatter.format(date) );
		
		movimentoRe.setTransacao(objDto.getTransacao());
		movimentoRe.setQuantidade(objDto.getQuantidade());
		movimentoRe.setObservacao(objDto.getObservacao());
				
		Usuario usuario = usuarioService.getUsuarioLogado();
		movimentoRe.setUsuario(usuario.getCodUsuario());
		
		MovimentoRe movRe = movimentoReRep.save(movimentoRe);
			
		movRe.setIdMovRe( "GEN" + movRe.getId() );
		
		return movimentoReRep.save(movRe);

	}
	
	public MovimentoRe findById(Long id) {
		return movimentoReRep.findById(id).get();
	}

	public MovimentoRe findByIdMovRe(String idMovRe) {
		return movimentoReRep.findByIdMovRe(idMovRe);
	}

	public Page<MovimentoRe> findByNrRe(Long nrRe, Pageable pageble) {
		return movimentoReRep.findByNrRe(nrRe, pageble);
	}

}
