package br.coop.integrada.api.pa.domain.service;

import static br.coop.integrada.api.pa.domain.enums.OrigemEnum.IMPORTACAO;
import static br.coop.integrada.api.pa.domain.enums.OrigemEnum.MANUAL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.OrigemEnum;
import br.coop.integrada.api.pa.domain.model.ParticipanteBayer;
import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.model.usuario.Usuario;
import br.coop.integrada.api.pa.domain.modelDto.participante.bayer.ParticipanteBayerDto;
import br.coop.integrada.api.pa.domain.repository.participante.bayer.ParticipanteBayerRep;
import br.coop.integrada.api.pa.domain.repository.produtor.ProdutorRep;

@Service
@Transactional
public class ParticipanteBayerService {

    @Autowired
    private ParticipanteBayerRep participanteBayerRep;

    @Autowired
    private ProdutorService produtorService;
    
    @Autowired
    private ProdutorRep produtorRep;

    @Autowired
    private UsuarioService usuarioService;
    

    public ParticipanteBayer salvarManual(ParticipanteBayerDto objDto) {
        List<ParticipanteBayer> participantesBayer = buscarPorCnpj(objDto.getCnpj());
        if(participantesBayer != null)
        	if(!participantesBayer.isEmpty()) {
            throw new ObjectNotFoundException("Já existe um Participante Bayer cadastrado com o CNPJ informado!");
        }

        Usuario usuario = usuarioService.getUsuarioLogado();
                
        ParticipanteBayer participanteBayer = converterDto(objDto, MANUAL, usuario.getUsername());
		
        return salvar(participanteBayer);
    }
    
    //Salva 1 a 1
    public ParticipanteBayer salvar(ParticipanteBayer obj) {
        String movimentacao = MANUAL.equals(obj.getOrigem()) ? "Inclusao Manual Parceiro" : "Importação Bayer";
        produtorService.setParticipanteBayer(obj.getCnpj(), true, movimentacao);
        return participanteBayerRep.save(obj);
    }
    
    //Método para salvar uma Lista de participante Bayer    
    public void salvarLista(List<ParticipanteBayer> listParticipantesBayer) {
    	    	
    	if(!listParticipantesBayer.isEmpty()) {
    	
	    	StringBuilder sql = new StringBuilder();
	    	
	    	Integer total = 1;
	    	sql.append("INSERT INTO PARTICIPANTE_BAYER (DATA_ATUALIZACAO,DATA_CADASTRO,CNPJ,NOME,ORIGEM,USUARIO) WITH p AS (");
	    	for(ParticipanteBayer p : listParticipantesBayer) {
	    		
	    		sql.append(" SELECT CURRENT_TIMESTAMP DATA_ATUALIZACAO, CURRENT_TIMESTAMP DATA_CADASTRO, '");
	    		sql.append(p.getCnpj()).append("' CNPJ, '");
	    		sql.append(p.getNome()).append("' NOME, 'IMPORTACAO' ORIGEM, '");
	    		sql.append(p.getUsuario()).append("' USUARIO "); 
	    		sql.append(total.equals(listParticipantesBayer.size()) ? " FROM dual ) SELECT * FROM p" : " FROM dual UNION ALL ");
	    		total++;
	    	}
	    	
	    	participanteBayerRep.insertList(sql);
	    	    	
	    	String userName = listParticipantesBayer.get(0).getUsuario();
        	String movimentacao = "Importação Bayer";
        
        	//Altera o Produtor setando participanteBayer = true para todos que são Importação
        	this.definirProdutorComoParticipanteBayer(OrigemEnum.IMPORTACAO.name());
    	
        	//Insere um histórico para a mudança do produtor
        	this.gerarHistoricoGenericoEmLote(userName, OrigemEnum.IMPORTACAO.name(), movimentacao);
        	
    	}
    	
    }

    /*public void excluirPorOrigemAntigo(OrigemEnum origem) {
        List<ParticipanteBayer> participantesBayer = buscarPorOrigem(origem);

        // Desvincular o Produtor como Participante Bayer
        for(ParticipanteBayer participanteBayer : participantesBayer) {
            produtorService.setParticipanteBayer(participanteBayer.getCnpj(), false, "Excluir importação Bayer");
        }

        participanteBayerRep.deleteAll(participantesBayer);
    }*/
    
    public void excluirPorOrigem(OrigemEnum origem) {
        
    	String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    	
    	//Gera o historico
		participanteBayerRep.gerarHistoricoGenericoExcluirTudoOrigem(userName, origem.name());
		
		//Muda o campo participanteBayer para false
		participanteBayerRep.inativarTodosParticipantesBayerPorOrigem(origem.name());
		
		//Realiza a exclusão do Participante Bayer de acordo com a origem
		participanteBayerRep.excluirTodosParticipantesBayerPorOrigem(origem.name());
    	
    	
    }

    public void excluirOrigemManualQueEstaNaListaDeImportacao(List<ParticipanteBayer> participantesBayerImportacao) {
        List<ParticipanteBayer> participantesBayerManual = buscarPorOrigem(MANUAL);

        for(ParticipanteBayer participanteBayerManual : participantesBayerManual) {
            String cnpj = participanteBayerManual.getCnpj();
            ParticipanteBayer participanteBayer = participantesBayerImportacao.stream().filter(item -> {
                return item.getCnpj().equalsIgnoreCase(cnpj);
            }).findFirst().orElse(null);

            if(participanteBayer != null) {
                excluir(participanteBayerManual, IMPORTACAO);
            }
        }
    }

    public void excluir(ParticipanteBayer obj, OrigemEnum origem) {
        String movimentacao = MANUAL.equals(origem) ? "Exclusão Manual Parceiro" : "Excluir importação Bayer";
        produtorService.setParticipanteBayer(obj.getCnpj(), false, movimentacao);
        participanteBayerRep.delete(obj);
    }

    public void excluirPorCnpj(String cnpj) {
        List<ParticipanteBayer> participantesBayer = buscarPorCnpj(cnpj);

        if(participantesBayer == null || participantesBayer.isEmpty()) {
            throw new ObjectNotFoundException("Não foi encontrado Participanete Bayer com o CNPJ " + cnpj);
        }

        for (ParticipanteBayer participanteBayer : participantesBayer) {
        	excluir(participanteBayer, MANUAL);
		}
        
    }

    public List<ParticipanteBayer> buscarPorCnpj(String cnpj) {
        cnpj = cnpj.replaceAll("[^0-9]", "");
        return participanteBayerRep.findByCnpjIgnoreCase(cnpj);
    }

    public List<ParticipanteBayer> buscarPorOrigem(OrigemEnum origem) {
        return participanteBayerRep.findByOrigem(origem);
    }

    public Page<ParticipanteBayerDto> listar(Pageable pageable, String filtro) {
        Page<ParticipanteBayer> participanteBayerPages = participanteBayerRep.findAll(pageable, filtro);
        List<ParticipanteBayerDto> participanteBayerDtos = ParticipanteBayerDto.construir(participanteBayerPages.getContent());
        return new PageImpl<>(participanteBayerDtos, pageable, participanteBayerPages.getTotalElements());
    }

    public ParticipanteBayer converterDto(ParticipanteBayerDto objDto, OrigemEnum origem, String usuario) {
        var obj = new ParticipanteBayer();
        obj.setNome(objDto.getNome());
        obj.setCnpj(objDto.getCnpj());
        obj.setOrigem(origem);
        obj.setUsuario(usuario);

        return obj;
    }

    public List<ParticipanteBayer> converterDto(List<ParticipanteBayerDto> objDtos, OrigemEnum origem, String usuario) {
        if(CollectionUtils.isEmpty(objDtos)) return new ArrayList<>();
        
        HashMap<String, String> map = new HashMap<>();
        List<ParticipanteBayer> participantesBayer = new ArrayList<>();
        
        for(ParticipanteBayerDto dto : objDtos ) {
       	        	
        	if(dto.getCnpj().length() == 10){
        		dto.setCnpj("0" + dto.getCnpj());
        	}
        	else if(dto.getCnpj().length() == 13) {
        		dto.setCnpj("0" + dto.getCnpj());
        	}
        	
        	if(!map.containsKey(dto.getCnpj())) {
        		participantesBayer.add(converterDto(dto, origem, usuario));
        		map.put(dto.getCnpj(), dto.getNome());
        	}
        }
        
        
        return participantesBayer;
           
    }

	public void excluirTodos() {
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		participanteBayerRep.gerarHistoricoGenericoExcluirTudo(userName);
		
		//Muda o campo participanteBayer para false em toda a tabela de produtor
		participanteBayerRep.inativarTodosParticipantesBayer();
		
		//Realiza a exclusão do Participante Bayer
		participanteBayerRep.excluirTodosParticipantesBayer();
				
	}

	public List<String> validarProdutorBayer() {
		
		List<String> listaMensagens = new ArrayList<>();
		
		List<Produtor> listaProdutorPrecisaSetarFalse =  produtorRep.listarProdutorParticipanteBayerSemRegistroParticipanteBayer();
		
		for(Produtor produtor : listaProdutorPrecisaSetarFalse){
			String msg = "AVISO: O Produtor CNPJ: " + produtor.getCpfCnpj() + " teve o campo Participante Bayer alterado para NÃO.";
			listaMensagens.add(msg);
			
			produtor.setParticipanteBayer(false);
			
			try {
				produtorService.salvar(produtor);
			} catch (ParseException e) {
				msg = "ERRO: CNPJ: " + produtor.getCpfCnpj() + e.getMessage() ;
				listaMensagens.add(msg);
			}
		}
		
		List<Produtor> listaProdutorPrecisaSetarTrue =  produtorRep.listarProdutorParticipanteBayerComRegistroParticipanteBayer();
		
		for(Produtor produtor : listaProdutorPrecisaSetarTrue){
			String msg = "AVISO: O Produtor CNPJ: " + produtor.getCpfCnpj() + " teve o campo Participante Bayer alterado para SIM.";
			listaMensagens.add(msg);
			
			produtor.setParticipanteBayer(true);
			
			try {
				produtorService.salvar(produtor);
			} 
			catch (ParseException e) {
				msg = "ERRO: CNPJ: " + produtor.getCpfCnpj() + e.getMessage() ;
				listaMensagens.add(msg);
			}
		}
				
		if(listaMensagens.size() == 0){
			listaMensagens.add("Nenhuma inconsistência foi encontrada.");
		}
		
		return listaMensagens;
	}
	
	public void definirProdutorComoParticipanteBayer(String origem) {
		participanteBayerRep.definirProdutorComoParticipanteBayer(origem);
	}
	
	public void gerarHistoricoGenericoEmLote(String userName, String origem, String motivo) {
		participanteBayerRep.gerarHistoricoGenericoEmLote(userName, origem, motivo);
	}
	
}
