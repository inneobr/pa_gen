package br.coop.integrada.api.pa.domain.service;

import br.coop.integrada.api.pa.domain.model.UnidadeFederacao;
import br.coop.integrada.api.pa.domain.modelDto.unidade.federacao.UnidadeFederacaoDto;
import br.coop.integrada.api.pa.domain.repository.UnidadeFederacaoRep;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UnidadeFederacaoService {

    @Autowired
    private UnidadeFederacaoRep unidadeFederacaoRep;

    public UnidadeFederacao salvar(UnidadeFederacaoDto objDto) {
        //UnidadeFederacao unidadeFederacao = buscarPorCodigoIbge(objDto.getCodigoIbge());
    	UnidadeFederacao unidadeFederacao = null;
    	
    	if(objDto.getId() != null)
    		unidadeFederacao = buscarPorId(objDto.getId());

        if(unidadeFederacao == null) {
            unidadeFederacao = new UnidadeFederacao();
        }

        validarSeExisteUnidadeFederacaoCadastrada(objDto);
        
        objDto.setId(unidadeFederacao.getId());
        BeanUtils.copyProperties(objDto, unidadeFederacao);
        
        return unidadeFederacaoRep.save(unidadeFederacao);
    }

    public UnidadeFederacao buscarPorCodigoIbge(String codigoIbge) {
        return unidadeFederacaoRep.findByCodigoIbgeIgnoreCaseAndDataInativacaoIsNull(codigoIbge);
    }

    public UnidadeFederacao buscarPorEstado(String estado) {
        return unidadeFederacaoRep.findByEstadoIgnoreCaseAndDataInativacaoIsNull(estado);
    }

    public UnidadeFederacao buscarPorId(Long id) {
        return unidadeFederacaoRep.findById(id).orElse(null);
    }

    public Page<UnidadeFederacaoDto> buscarComPaginacao(Pageable pageable, String filter, Situacao situacao) {
        Page<UnidadeFederacao> unidadeFederacaoPage = unidadeFederacaoRep.findAll(pageable, filter, situacao);
        List<UnidadeFederacaoDto> unidadeFederacaoDtos = UnidadeFederacaoDto.construir(unidadeFederacaoPage.getContent());
        return new PageImpl<>(unidadeFederacaoDtos, pageable, unidadeFederacaoPage.getTotalElements());
    }

    public UnidadeFederacao inativar(Long id) {
        UnidadeFederacao unidadeFederacao = unidadeFederacaoRep.findById(id).orElse(null);

        if(unidadeFederacao == null) {
            throw new NullPointerException("Não foi encontrado a unidade de federação com o ID " + id + "!");
        }

        unidadeFederacao.setDataInativacao(new Date());
        return unidadeFederacaoRep.save(unidadeFederacao);
    }

    public UnidadeFederacao ativar(Long id) {
        UnidadeFederacao unidadeFederacaoAtual = unidadeFederacaoRep.findById(id).orElse(null);

        if(unidadeFederacaoAtual == null) {
            throw new NullPointerException("Não foi encontrado a unidade de federação com o ID " + id + "!");
        }

        UnidadeFederacao unidadeFederacao = new UnidadeFederacao();
        BeanUtils.copyProperties(unidadeFederacaoAtual, unidadeFederacao);

        unidadeFederacao.setDataInativacao(null);
        validarSeExisteUnidadeFederacaoCadastrada(unidadeFederacao);
        return unidadeFederacaoRep.save(unidadeFederacao);
    }
       
    
    private void validarSeExisteUnidadeFederacaoCadastrada(UnidadeFederacao unidadeFederacao) {
        List<UnidadeFederacao> unidadeFederacaoList = unidadeFederacaoRep.findByCodigoIbgeIgnoreCaseAndDataInativacaoIsNullOrEstadoIgnoreCaseAndDataInativacaoIsNullOrEstadoNomeIgnoreCaseAndDataInativacaoIsNull(
                unidadeFederacao.getCodigoIbge(),
                unidadeFederacao.getEstado(),
                unidadeFederacao.getEstadoNome()
        );

        if(unidadeFederacaoList != null && !unidadeFederacaoList.isEmpty()) {
            List<String> mensagens = new ArrayList<>();

            for(UnidadeFederacao item : unidadeFederacaoList) {
                Boolean osIdsSaoIguais = item.getId().equals(unidadeFederacao.getId());
                Boolean osCodigosIbgeSaoIguais = item.getCodigoIbge().equals(unidadeFederacao.getCodigoIbge());
                Boolean osEstadosSaoIguais = item.getEstado().trim().toUpperCase().equals(unidadeFederacao.getEstado().trim().toUpperCase());
                Boolean osEstadosNomesSaoIguais = item.getEstadoNome().trim().toUpperCase().equals(unidadeFederacao.getEstadoNome().trim().toUpperCase());
                
                if(osIdsSaoIguais) {
                	//Edição
                	if(osCodigosIbgeSaoIguais && osEstadosSaoIguais && osEstadosNomesSaoIguais) {
	                    mensagens.add("Já existe uma unidade federação cadastrada (CÓDIGO IBGE: " + item.getCodigoIbge() + " | ESTADO: " + item.getEstado() + " | ESTADO NOME: " + item.getEstadoNome() + ")");
	                }
                	else if (!osCodigosIbgeSaoIguais) { //Alteração em um codigo IBGE
                		System.out.print("IBGE");
                	}
                	else if(!osEstadosNomesSaoIguais) { //Alteração Nome do Estado
                		System.out.print("NOME");
                	}
                	else if(!osEstadosSaoIguais) { //Alteração na Sigla do estado
                		System.out.print("Sigla");
                	}
                }
                else 
                {
                	//Cadastro
	                if(osCodigosIbgeSaoIguais || osEstadosSaoIguais || osEstadosNomesSaoIguais) {
	                    mensagens.add("Já existe uma unidade federação cadastrada (CÓDIGO IBGE: " + item.getCodigoIbge() + " | ESTADO: " + item.getEstado() + " | ESTADO NOME: " + item.getEstadoNome() + ")");
	                }
	                
                }
            }

            if(!mensagens.isEmpty()) {
                String mensagem = String.join("\n", mensagens);
                throw new IllegalArgumentException(mensagem);
            }
        }
    }
    
    private void validarSeExisteUnidadeFederacaoCadastrada(UnidadeFederacaoDto unidadeFederacao) {
        List<UnidadeFederacao> unidadeFederacaoList = unidadeFederacaoRep.findByCodigoIbgeIgnoreCaseOrEstadoIgnoreCaseOrEstadoNomeIgnoreCase(
                unidadeFederacao.getCodigoIbge(),
                unidadeFederacao.getEstado(),
                unidadeFederacao.getEstadoNome()
        );

        if(unidadeFederacaoList != null && !unidadeFederacaoList.isEmpty()) {
            List<String> mensagens = new ArrayList<>();

            for(UnidadeFederacao item : unidadeFederacaoList) {
                Boolean osIdsSaoIguais = item.getId().equals(unidadeFederacao.getId());
                Boolean osCodigosIbgeSaoIguais = item.getCodigoIbge().equals(unidadeFederacao.getCodigoIbge());
                Boolean osEstadosSaoIguais = item.getEstado().trim().toUpperCase().equals(unidadeFederacao.getEstado().trim().toUpperCase());
                Boolean osEstadosNomesSaoIguais = item.getEstadoNome().trim().toUpperCase().equals(unidadeFederacao.getEstadoNome().trim().toUpperCase());
                
                if(osIdsSaoIguais) {
                	//Edição
                	if(osCodigosIbgeSaoIguais && osEstadosSaoIguais && osEstadosNomesSaoIguais) {
	                    mensagens.add("Já existe uma unidade federação cadastrada (CÓDIGO IBGE: " + item.getCodigoIbge() + " | ESTADO: " + item.getEstado() + " | ESTADO NOME: " + item.getEstadoNome() + ")");
	                }
                }
                else 
                {
                	//Cadastro
	                if(osCodigosIbgeSaoIguais || osEstadosSaoIguais || osEstadosNomesSaoIguais) {
	                    mensagens.add("Já existe uma unidade federação cadastrada (CÓDIGO IBGE: " + item.getCodigoIbge() + " | ESTADO: " + item.getEstado() + " | ESTADO NOME: " + item.getEstadoNome() + ")");
	                }
	                
                }
            }

            if(!mensagens.isEmpty()) {
                String mensagem = String.join("\n", mensagens);
                throw new IllegalArgumentException(mensagem);
            }
        }
    }
}
