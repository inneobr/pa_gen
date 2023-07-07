package br.coop.integrada.api.pa.domain.service.semente.produtor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.model.semente.SementeClasse;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampo;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampoProdutor;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeLaudoInspecao;
import br.coop.integrada.api.pa.domain.modelDto.semente.LaudoInspecaoItemDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.ZoomCampoSementeDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.produtor.GrupoSementeProdutorDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.produtor.SementeCampoProdutorSimplesDto;
import br.coop.integrada.api.pa.domain.modelDto.semente.produtor.SementeLaudoInspecaoSimplesDto;
import br.coop.integrada.api.pa.domain.repository.ImovelRep;
import br.coop.integrada.api.pa.domain.repository.semente.produtor.SementeCampoProdutorRep;
import br.coop.integrada.api.pa.domain.repository.semente.produtor.SementeCampoRep;
import br.coop.integrada.api.pa.domain.repository.semente.produtor.SementeLaudoInspecaoRep;
import br.coop.integrada.api.pa.domain.service.ProdutorService;
import br.coop.integrada.api.pa.domain.service.estabelecimento.EstabelecimentoService;
import br.coop.integrada.api.pa.domain.service.produto.GrupoProdutoService;
import br.coop.integrada.api.pa.domain.service.produto.ProdutoService;
import br.coop.integrada.api.pa.domain.service.semente.SementeClasseService;

@Service
@Transactional
public class SementeProdutorService {

    @Autowired
    private SementeCampoRep sementeCampoRep;

    @Autowired
    private SementeLaudoInspecaoRep sementeLaudoInspecaoRep;

    @Autowired
    private SementeCampoProdutorRep sementeCampoProdutorRep;

    @Autowired
    private EstabelecimentoService estabelecimentoService;

    @Autowired
    private GrupoProdutoService grupoProdutoService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private SementeClasseService sementeClasseService;

    @Autowired
    private ProdutorService produtorService;
    
    @Autowired
    private ImovelRep imovelRep;

    public void integrationSave(GrupoSementeProdutorDto objDto) {
    	
    	if(Strings.isEmpty(objDto.getIdUnico())) {
			throw new ObjectNotFoundException("Campo {idUnico} obrigatório");
		}
    	
    	if(objDto.getSafra() == null) {
			throw new ObjectNotFoundException("Campo {safra} obrigatório");
		}
    	
    	if(objDto.getCodigoEstabelecimento() == null) {
			throw new ObjectNotFoundException("Campo {codigoEstabelecimento} obrigatório");
		}
    	
    	if(Strings.isEmpty(objDto.getCodigoFamilia())) {
			throw new ObjectNotFoundException("Campo {codigoFamilia} obrigatório");
		}
    	
    	if(Strings.isEmpty(objDto.getCodigoProduto())) {
			throw new ObjectNotFoundException("Campo {codigoProduto} obrigatório");
		}
    	
    	if(Strings.isEmpty(objDto.getCodigoProdutor())) {
			throw new ObjectNotFoundException("Campo {codigoProdutor} obrigatório");
		}
    	
    	if(objDto.getOrdemCampo() == null) {
			throw new ObjectNotFoundException("Campo {ordemCampo} obrigatório");
		}
    	
    	if(objDto.getClasseCodigo() == null) {
			throw new ObjectNotFoundException("Campo {classeCodigo} obrigatório");
		}
    	
    	if(Strings.isEmpty(objDto.getMatricula())) {
			throw new ObjectNotFoundException("Campo {matricula} obrigatório");
		}
    	
    	SementeCampo sementeCampo = sementeCampoRep.findByIdUnico(objDto.getIdUnico());
    	if(sementeCampo == null) {
    		sementeCampo = new SementeCampo();
    	}
    	
    	BeanUtils.copyProperties(objDto, sementeCampo);

        Produto produto = produtoService.buscarProdutoAtivoPorCodItem(objDto.getCodigoProduto());
        if(produto == null) {
        	throw new ObjectNotFoundException("Não foi encontrado produto com o código: "+ objDto.getCodigoProduto());
        }
                
        GrupoProduto grupoProduto = grupoProdutoService.buscarGrupoFmCodigo(objDto.getCodigoFamilia());
        if(grupoProduto == null) {
        	throw new ObjectNotFoundException("Não foi encontrado grupo de produto com o código: "+ objDto.getCodigoFamilia());
        }
        
        Estabelecimento estabelecimento = estabelecimentoService.buscarPorCodigo(objDto.getCodigoEstabelecimento());  
        if(estabelecimento == null) {
        	throw new ObjectNotFoundException("Não foi encontrado estabelecimento com o código: "+ objDto.getCodigoEstabelecimento());
        }

        SementeClasse classe = sementeClasseService.buscarPorCodigo(objDto.getClasseCodigo());
        if(classe == null) {
            throw new ObjectNotFoundException("Não foi encontrado classe com o código: " + objDto.getClasseCodigo());
        }      
        
        Imovel imovel = imovelRep.findByMatricula(Long.parseLong(objDto.getMatricula()));
        if(imovel == null) {
            throw new ObjectNotFoundException("Não foi encontrado imóvel: " + objDto.getMatricula());
        }  

        sementeCampo.setProduto(produto);
        sementeCampo.setClasse(classe);
        sementeCampo.setGrupoProduto(grupoProduto);
        sementeCampo.setEstabelecimento(estabelecimento);
    	sementeCampo.setImovel(imovel.getNome());
    	sementeCampo.setDataIntegracao(new Date());
    	sementeCampo.setStatusIntegracao(StatusIntegracao.INTEGRADO);
        sementeCampoRep.save(sementeCampo);
    }
    
    public void integrationSave(SementeCampoProdutorSimplesDto objDto) {
    	
    	if(Strings.isEmpty(objDto.getIdUnico())) {
			throw new ObjectNotFoundException("Campo {idUnico} obrigatório");
		}
    	
    	if(objDto.getSafra() == null) {
			throw new ObjectNotFoundException("Campo {safra} obrigatório");
		}
    	
    	if(objDto.getCodigoEstabelecimento() == null) {
			throw new ObjectNotFoundException("Campo {codigoEstabelecimento} obrigatório");
		}
    	
    	if(Strings.isEmpty(objDto.getCodigoFamilia())) {
			throw new ObjectNotFoundException("Campo {codigoFamilia} obrigatório");
		}
    	
    	if(objDto.getOrdemCampo() == null) {
			throw new ObjectNotFoundException("Campo {ordemCampo} obrigatório");
		}
    	
    	if(Strings.isEmpty(objDto.getCodigoProdutor())) {
			throw new ObjectNotFoundException("Campo {codigoProdutor} obrigatório");
		}
    	
    	if(objDto.getClasseCodigo() == null) {
			throw new ObjectNotFoundException("Campo {classeCodigo} obrigatório");
		}
    	
    	SementeCampoProdutor sementeCampoProdutor = sementeCampoProdutorRep.findByIdUnico(objDto.getIdUnico());
    	if(sementeCampoProdutor == null) {
    		sementeCampoProdutor = new SementeCampoProdutor();
    	}
    	
    	BeanUtils.copyProperties(objDto, sementeCampoProdutor);
                
        GrupoProduto grupoProduto = grupoProdutoService.buscarGrupoFmCodigo(objDto.getCodigoFamilia());
        if(grupoProduto == null) {
        	throw new ObjectNotFoundException("Não foi encontrado grupo de produto com o código: "+ objDto.getCodigoFamilia());
        }
        
        Estabelecimento estabelecimento = estabelecimentoService.buscarPorCodigo(objDto.getCodigoEstabelecimento());  
        if(estabelecimento == null) {
        	throw new ObjectNotFoundException("Não foi encontrado estabelecimento com o código: "+ objDto.getCodigoEstabelecimento());
        }

        SementeClasse classe = sementeClasseService.buscarPorCodigo(objDto.getClasseCodigo());
        if(classe == null) {
            throw new ObjectNotFoundException("Não foi encontrado classe com o código: " + objDto.getClasseCodigo());
        }   
        
        Produtor produtor = produtorService.buscarPorCodigoProdutor(objDto.getCodigoProdutor());
        if(produtor == null) {
            throw new ObjectNotFoundException("Não foi encontrado produtor com o código: " + objDto.getCodigoProdutor());
        }  

        sementeCampoProdutor.setProdutor(produtor);
        sementeCampoProdutor.setClasse(classe);
        sementeCampoProdutor.setGrupoProduto(grupoProduto);
        sementeCampoProdutor.setEstabelecimento(estabelecimento);        
        sementeCampoProdutor.setDataIntegracao(new Date());
        sementeCampoProdutor.setStatusIntegracao(StatusIntegracao.INTEGRADO);
        sementeCampoProdutorRep.save(sementeCampoProdutor);
    }
    
    public void integrationSave(SementeLaudoInspecaoSimplesDto objDto) {
    	
    	if(Strings.isEmpty(objDto.getIdUnico())) {
			throw new ObjectNotFoundException("Campo {idUnico} obrigatório");
		}
    	
    	if(objDto.getSafra() == null) {
			throw new ObjectNotFoundException("Campo {safra} obrigatório");
		}
    	
    	if(objDto.getCodigoEstabelecimento() == null) {
			throw new ObjectNotFoundException("Campo {codigoEstabelecimento} obrigatório");
		}
    	
    	if(Strings.isEmpty(objDto.getCodigoFamilia())) {
			throw new ObjectNotFoundException("Campo {codigoFamilia} obrigatório");
		}
    	
    	if(objDto.getOrdemCampo() == null) {
			throw new ObjectNotFoundException("Campo {ordemCampo} obrigatório");
		}
    	
    	if(Strings.isEmpty(objDto.getMatricula())) {
			throw new ObjectNotFoundException("Campo {matricula} obrigatório");
		}
    	
    	if(objDto.getClasse() == null) {
			throw new ObjectNotFoundException("Campo {classe} obrigatório");
		}
    	
    	if(objDto.getItem() == null) {
			throw new ObjectNotFoundException("Campo {item} obrigatório");
		}
    	
    	SementeLaudoInspecao sementeLaudoInspecao = sementeLaudoInspecaoRep.findByIdUnico(objDto.getIdUnico());
    	if(sementeLaudoInspecao == null) {
    		sementeLaudoInspecao = new SementeLaudoInspecao();
    	}
    	
    	BeanUtils.copyProperties(objDto, sementeLaudoInspecao);
                
        GrupoProduto grupoProduto = grupoProdutoService.buscarGrupoFmCodigo(objDto.getCodigoFamilia());
        if(grupoProduto == null) {
        	throw new ObjectNotFoundException("Não foi encontrado grupo de produto com o código: "+ objDto.getCodigoFamilia());
        }
        
        Estabelecimento estabelecimento = estabelecimentoService.buscarPorCodigo(objDto.getCodigoEstabelecimento());  
        if(estabelecimento == null) {
        	throw new ObjectNotFoundException("Não foi encontrado estabelecimento com o código: "+ objDto.getCodigoEstabelecimento());
        }

        SementeClasse classe = sementeClasseService.buscarPorCodigo(objDto.getClasse());
        if(classe == null) {
            throw new ObjectNotFoundException("Não foi encontrado classe com o código: " + objDto.getClasse());
        }   
        
        Produto produto = produtoService.buscarPorCodigo(objDto.getItem());
        if(produto == null) {
        	throw new ObjectNotFoundException("Não foi encontrado produto com o código: "+ objDto.getItem());
        }
        
        sementeLaudoInspecao.setProduto(produto);
        sementeLaudoInspecao.setClasse(classe);
        sementeLaudoInspecao.setGrupoProduto(grupoProduto);
        sementeLaudoInspecao.setEstabelecimento(estabelecimento);        
        sementeLaudoInspecao.setDataIntegracao(new Date());
        sementeLaudoInspecao.setStatusIntegracao(StatusIntegracao.INTEGRADO);
        sementeLaudoInspecaoRep.save(sementeLaudoInspecao);
    }

    /*
     **** SEMENTE CAMPO ****
     */
    public SementeCampo buscarSementeCampoPor(Integer safra, Integer ordemCampo, String codigoEstabelecimento, String codigoFamilia, Long codigoClasse) {
        GrupoProduto grupoProduto = grupoProdutoService.buscarGrupoFmCodigo(codigoFamilia);
        Estabelecimento estabelecimento = estabelecimentoService.buscarPorCodigo(codigoEstabelecimento);

        SementeClasse classe = sementeClasseService.buscarPorCodigo(codigoClasse);

        if(classe == null) {
            throw new NullPointerException("Não foi encontrado classe com o código " + codigoClasse + "!");
        }

        return buscarSementeCampoPor(safra, ordemCampo, estabelecimento, grupoProduto, classe);
    }

    public SementeCampo buscarSementeCampoPor(Integer safra, Integer ordemCampo, Estabelecimento estabelecimento, GrupoProduto grupoProduto, SementeClasse classe) {
        SementeCampo sementeCampo = sementeCampoRep.findBySafraAndOrdemCampoAndEstabelecimentoAndGrupoProdutoAndClasse(safra, ordemCampo, estabelecimento, grupoProduto, classe).orElse(null);

        if(sementeCampo == null) {
            throw new NullPointerException("A semente campo não foi encontrado! (Safra: " + safra + " | Ordem campo: " + ordemCampo + " | Estabelecimento: " + estabelecimento.getNomeFantasia() + " | Grupo produto: " + grupoProduto.getDescricao() + " | Classe: " + classe + ")");
        }

        return sementeCampo;
    }

    public ZoomCampoSementeDto buscarSementeCampoPor(Integer safra, String codigoEstabelecimento, String codigoGrupoProduto) {
        ZoomCampoSementeDto zoomCampoSementeDto = sementeCampoRep.findBySafraAndCodigoEstabelecimentoAndCodigoGrupoProduto(safra, codigoEstabelecimento, codigoGrupoProduto);

        if(zoomCampoSementeDto == null) {
            throw new ObjectNotFoundException("Não foi encontrado campo semente com a safra: " + safra + ", codigo estabelecimento: " + codigoEstabelecimento + " e codigo grupo produto: " + codigoGrupoProduto);
        }

        return zoomCampoSementeDto;
    }

    /*
     **** SEMENTE CAMPO PRODUTOR ****
     */
    public SementeCampoProdutor buscarSementeCampoProdutorPor(Integer safra, Integer ordemCampo, String codigoProdutor, String codigoEstabelecimento, String codigoFamilia, Long codigoClasse) {
        GrupoProduto grupoProduto = grupoProdutoService.buscarGrupoFmCodigo(codigoFamilia);
        Estabelecimento estabelecimento = estabelecimentoService.buscarPorCodigo(codigoEstabelecimento);
        Produtor produtor = produtorService.buscarPorCodigoProdutor(codigoProdutor);

        SementeClasse classe = sementeClasseService.buscarPorCodigo(codigoClasse);

        if(classe == null) {
            throw new NullPointerException("Não foi encontrado classe com o código " + codigoClasse + "!");
        }

        return buscarSementeCampoProdutorPor(safra, ordemCampo, produtor, estabelecimento, grupoProduto, classe);
    }  

    public SementeCampoProdutor buscarSementeCampoProdutorPor(Integer safra, Integer ordemCampo, Produtor produtor, Estabelecimento estabelecimento, GrupoProduto grupoProduto, SementeClasse classe) {
        SementeCampoProdutor sementeCampoProdutor = sementeCampoProdutorRep.findBySafraAndOrdemCampoAndProdutorAndEstabelecimentoAndGrupoProdutoAndClasse(safra, ordemCampo, produtor, estabelecimento, grupoProduto, classe).orElse(null);

        if(sementeCampoProdutor == null) {
            throw new NullPointerException("A semente campo produtor não foi encontrado! (Safra: " + safra + " | Ordem campo: " + ordemCampo + " | Codigo produtor: " + produtor.getCodProdutor() + " | Estabelecimento: " + estabelecimento.getNomeFantasia() + " | Grupo produto: " + grupoProduto.getDescricao() + " | Classe: " + classe + ")");
        }

        return sementeCampoProdutor;
    }

    public List<SementeCampoProdutor> buscarSementeCampoProdutorPor(Integer safra, Integer ordemCampo, Estabelecimento estabelecimento, GrupoProduto grupoProduto, SementeClasse classe) {
        return sementeCampoProdutorRep.findBySafraAndOrdemCampoAndEstabelecimentoAndGrupoProdutoAndClasse(safra, ordemCampo, estabelecimento, grupoProduto, classe);
    }

    /*
     **** SEMENTE LAUDO INSPECAO ****
     */
    public SementeLaudoInspecao buscarSementeLaudoInspecaoPor(Integer safra, String codigoEstabelecimento, Long numeroLaudo, Integer ordemCampo, String codigoFamilia) {
        return sementeLaudoInspecaoRep.findBySafraAndNumeroLaudoAndOrdemCampoAndGrupoProdutoFmCodigoAndEstabelecimentoCodigo(safra, numeroLaudo, ordemCampo, codigoFamilia, codigoEstabelecimento)
        		.orElseThrow(() -> new ObjectNotFoundException("Não foi encontrado laudo de inspeção com os parâmetros informados. (Safra: " + safra + 
        				" | Código estabelecimento: " + codigoEstabelecimento + 
        				" | Número laudo: " + numeroLaudo + 
        				" | Ordem campo: " + ordemCampo + 
        				" | Código Grupo produto: " + codigoFamilia + ")"));
    }

    public SementeLaudoInspecao buscarSementeLaudoInspecaoPor(Integer safra, Estabelecimento estabelecimento, Long numeroLaudo, Integer ordemCampo, GrupoProduto grupoProduto) {
        SementeLaudoInspecao sementeLaudoInspecao = sementeLaudoInspecaoRep.findBySafraAndEstabelecimentoAndNumeroLaudoAndOrdemCampoAndGrupoProduto(safra, estabelecimento, numeroLaudo, ordemCampo, grupoProduto).orElse(null);

        if(sementeLaudoInspecao == null) {
            throw new ObjectNotFoundException("O laudo não foi encontrado! (Safra: " + safra + " | Estabelecimento: " + estabelecimento.getNomeFantasia() + " | Numero laudo: " + numeroLaudo + " | Ordem campo: " + ordemCampo + " | Grupo produto: " + grupoProduto.getDescricao() + ")");
        }

        return sementeLaudoInspecao;
    }
    
    public List<LaudoInspecaoItemDto> buscarLaudoCampoSementePorClasse(String codigoEstabelecimento, Integer safra, String codigoFamilia, Long codigoClasse, Long numeroLaudo) {
        Estabelecimento estabelecimento = estabelecimentoService.buscarPorCodigo(codigoEstabelecimento);  
        GrupoProduto grupoProduto = grupoProdutoService.buscarGrupoFmCodigo(codigoFamilia);
        SementeClasse classe = sementeClasseService.buscarPorCodigo(codigoClasse);

        System.out.println("");
        
        List<SementeLaudoInspecao> lista = sementeLaudoInspecaoRep.findByEstabelecimentoAndSafraAndGrupoProdutoAndClasseAndNumeroLaudo(estabelecimento, safra, grupoProduto, classe, numeroLaudo);
        if(lista.isEmpty()) throw new NullPointerException("Laudo não encontrado, produto indisponível"); 
        	
        List<LaudoInspecaoItemDto> response = new ArrayList<>();
        for(SementeLaudoInspecao item: lista) {        	
        	LaudoInspecaoItemDto laudo = new LaudoInspecaoItemDto();
        	BeanUtils.copyProperties(item, laudo);
        	response.add(laudo);
        }
        return response;
    }

    public List<SementeLaudoInspecao> buscarSementeLaudoInspecaoPor(Integer safra, Estabelecimento estabelecimento, Integer ordemCampo, GrupoProduto grupoProduto) {
        return sementeLaudoInspecaoRep.findBySafraAndEstabelecimentoAndOrdemCampoAndGrupoProduto(safra, estabelecimento, ordemCampo, grupoProduto);
    }
    
    public SementeCampo findSementeCampoByIdUnico(String idUnico) {
    	return sementeCampoRep.findByIdUnico(idUnico);
    }
    
    public SementeCampoProdutor findSementeCampoProdutorByIdUnico(String idUnico) {
    	return sementeCampoProdutorRep.findByIdUnico(idUnico);
    }
    
    public SementeLaudoInspecao findSementeLaudoInspecaoByIdUnico(String idUnico) {
    	return sementeLaudoInspecaoRep.findByIdUnico(idUnico);
    }
    
    public void delete(SementeCampo sementeCampo) {		
    	sementeCampoRep.delete(sementeCampo);
	}
	
	public void save(SementeCampo sementeCampo) {
		sementeCampoRep.save(sementeCampo);
	}
    
    public void delete(SementeCampoProdutor sementeCampoProdutor) {		
    	sementeCampoProdutorRep.delete(sementeCampoProdutor);
	}
	
	public void save(SementeCampoProdutor sementeCampoProdutor) {
		sementeCampoProdutorRep.save(sementeCampoProdutor);
	}
    
    public void delete(SementeLaudoInspecao sementeLaudoInspecao) {		
    	sementeLaudoInspecaoRep.delete(sementeLaudoInspecao);
	}
	
	public void save(SementeLaudoInspecao sementeLaudoInspecao) {
		sementeLaudoInspecaoRep.save(sementeLaudoInspecao);
	}
}
