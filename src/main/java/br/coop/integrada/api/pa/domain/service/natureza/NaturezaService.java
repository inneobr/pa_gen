package br.coop.integrada.api.pa.domain.service.natureza;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.aplication.exceptions.ObjectNotFoundException;
import br.coop.integrada.api.pa.domain.enums.Operacao;
import br.coop.integrada.api.pa.domain.model.Produtor;
import br.coop.integrada.api.pa.domain.model.naturezaOperacao.NaturezaOperacao;
import br.coop.integrada.api.pa.domain.model.naturezaTributaria.NaturezaTributaria;
import br.coop.integrada.api.pa.domain.modelDto.natureza.NaturezaResponseDto;
import br.coop.integrada.api.pa.domain.service.ProdutorService;
import br.coop.integrada.api.pa.domain.service.naturezaOperacao.NaturezaOperacaoService;
import br.coop.integrada.api.pa.domain.service.naturezaTributaria.NaturezaTributariaService;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.coop.integrada.api.pa.domain.enums.Operacao.*;

@Service
public class NaturezaService {
    
    @Autowired
    private ProdutorService produtorService;

    @Autowired
    private NaturezaTributariaService naturezaTributariaService;

    @Autowired
    private NaturezaOperacaoService naturezaOperacaoService;

    public NaturezaResponseDto buscarNaturezaPor(String codigoEstabelecimento, Operacao operacao, String codigoProdutor, String codigoGrupoProduto, String naturezaOrigem) {
        try {
            Produtor produtor = produtorService.buscarPorCodigoProdutor(codigoProdutor);

            if(operacao.equals(FIXACAO_TRIBUTADA)) {
                if(Strings.isEmpty(codigoGrupoProduto)) {
                    throw new ObjectNotFoundException("Necessário informar o código do grupo de produto para a consulta com a operação " + operacao.getDescricao());
                }

                NaturezaTributaria naturezaTributaria = naturezaTributariaService.findByGrupoProdutoAndEstabelecimento(codigoProdutor, codigoGrupoProduto);

                if(naturezaTributaria == null) {
                    throw new ObjectNotFoundException("Não foi encontrado natureza tributária vinculada com o estabelecimento " + codigoEstabelecimento + " e grupo de produto " + codigoProdutor);
                }

                return getNaturezaFinaxaoTributada(produtor, naturezaTributaria);
            }

            NaturezaOperacao naturezaOperacao = naturezaOperacaoService.buscarPorEstabelecimento(codigoEstabelecimento);
            if(naturezaOperacao == null) {
                throw new ObjectNotFoundException("Não foi encontrado natureza de operação vinculada ao estabelecimento " + codigoEstabelecimento);
            }

            if(operacao.equals(ENTRADA)) {
                return getNaturezaEntrada(produtor, naturezaOperacao);
            }
            else if(operacao.equals(DEV_ENTRADA)) {
                return getNaturezaDevEntrada(produtor, naturezaOperacao, naturezaOrigem);
            }
            else if(operacao.equals(FIXACAO)) {
                return getNaturezaFixacao(produtor, naturezaOperacao);
            }
            else if(operacao.equals(FIXACAO_SEMENTE)) {
                return getNaturezaFixacaoSemente(produtor, naturezaOperacao);
            }
            else if(operacao.equals(DEV_SEMENTE)) {
                return getNaturezaDevSemente(produtor, naturezaOperacao);
            }
        }
        catch (Exception e) {
            return NaturezaResponseDto.construir(false, e.getMessage(), e);
        }

        return NaturezaResponseDto.construir(false, "Não foi encontrado a operação!");
    }

    private NaturezaResponseDto getNaturezaFinaxaoTributada(Produtor produtor, NaturezaTributaria naturezaTributaria) {
        String tipoProdutor = produtor.getTipoProdutor().toUpperCase();
        String natureza = produtor.getNatureza().toUpperCase();
        
        if(natureza.equals("PF")) {
            if(tipoProdutor.equals("COOP") || tipoProdutor.equals("PC")) {
                if(Strings.isEmpty(naturezaTributaria.getNatFixCoop()) || Strings.isEmpty(naturezaTributaria.getSerFixCoop()) || Strings.isEmpty(naturezaTributaria.getModNatFixCoop())) {
                    throw new ObjectDefaultException("Natureza, série ou modelo eletrônico para o grupo de natureza de operação tributada estão em branco, favor solicitar correção para o setor fiscal");
                }
                
                return NaturezaResponseDto.construir(naturezaTributaria.getNatFixCoop(), naturezaTributaria.getSerFixCoop(), naturezaTributaria.getModNatFixCoop());
            }
            else if(tipoProdutor.equals("TER")) {
                if(Strings.isEmpty(naturezaTributaria.getNatFixTerc()) || Strings.isEmpty(naturezaTributaria.getSerFixTerc()) || Strings.isEmpty(naturezaTributaria.getModNatFixTerc())) {
                    throw new ObjectDefaultException("Natureza, série ou modelo eletrônico para o grupo de natureza de operação tributada estão em branco, favor solicitar correção para o setor fiscal");
                }
                
                return NaturezaResponseDto.construir(naturezaTributaria.getNatFixTerc(), naturezaTributaria.getSerFixTerc(), naturezaTributaria.getModNatFixTerc());
            }
        }
        else if(natureza.equals("PJ")) {
            if(produtor.getEmiteNota()) {
                if(tipoProdutor.equals("COOP") || tipoProdutor.equals("PC")) {
                    if(Strings.isEmpty(naturezaTributaria.getNatFixPjCnfCoop()) || Strings.isEmpty(naturezaTributaria.getSerFixPjCnfCoop()) || Strings.isEmpty(naturezaTributaria.getModFixPjCnfCoop())) {
                        throw new ObjectDefaultException("Natureza, série ou modelo eletrônico para o grupo de natureza de operação tributada estão em branco, favor solicitar correção para o setor fiscal");
                    }
                    
                    return NaturezaResponseDto.construir(naturezaTributaria.getNatFixPjCnfCoop(), naturezaTributaria.getSerFixPjCnfCoop(), naturezaTributaria.getModFixPjCnfCoop());
                }
                else if(tipoProdutor.equals("TER")) {
                    if(Strings.isEmpty(naturezaTributaria.getNatFixPjCnfTerc()) || Strings.isEmpty(naturezaTributaria.getSerFixPjCnfTerc()) || Strings.isEmpty(naturezaTributaria.getModFixPjCnfTerc())) {
                        throw new ObjectDefaultException("Natureza, série ou modelo eletrônico para o grupo de natureza de operação tributada estão em branco, favor solicitar correção para o setor fiscal");
                    }
                    
                    return NaturezaResponseDto.construir(naturezaTributaria.getNatFixPjCnfTerc(), naturezaTributaria.getSerFixPjCnfTerc(), naturezaTributaria.getModFixPjCnfTerc());
                }
            }
            else {
                if(tipoProdutor.equals("COOP") || tipoProdutor.equals("PC")) {
                    if(Strings.isEmpty(naturezaTributaria.getNatFixPjSnfCoop()) || Strings.isEmpty(naturezaTributaria.getSerFixPjSnfCoop()) || Strings.isEmpty(naturezaTributaria.getModFixPjSnfCoop())) {
                        throw new ObjectDefaultException("Natureza, série ou modelo eletrônico para o grupo de natureza de operação tributada estão em branco, favor solicitar correção para o setor fiscal");
                    }
                    
                    return NaturezaResponseDto.construir(naturezaTributaria.getNatFixPjSnfCoop(), naturezaTributaria.getSerFixPjSnfCoop(), naturezaTributaria.getModFixPjSnfCoop());
                }
                else if(tipoProdutor.equals("TER")) {
                    if(Strings.isEmpty(naturezaTributaria.getNatFixPjSnfTerc()) || Strings.isEmpty(naturezaTributaria.getSerFixPjSnfTerc()) || Strings.isEmpty(naturezaTributaria.getModFixPjSnfTerc())) {
                        throw new ObjectDefaultException("Natureza, série ou modelo eletrônico para o grupo de natureza de operação tributada estão em branco, favor solicitar correção para o setor fiscal");
                    }
                    
                    return NaturezaResponseDto.construir(naturezaTributaria.getNatFixPjSnfTerc(), naturezaTributaria.getSerFixPjSnfTerc(), naturezaTributaria.getModFixPjSnfTerc());
                }
            }
        }
        
        throw new ObjectDefaultException("O produtor informado não possui informação de \"Natureza\" e/ou \"Tipo Produtor\"!");
    }
    
    private NaturezaResponseDto getNaturezaEntrada(Produtor produtor, NaturezaOperacao naturezaOperacao) {
        String tipoProdutor = produtor.getTipoProdutor().toUpperCase();
        String natureza = produtor.getNatureza().toUpperCase();

        if(natureza.equals("PF")) {
            if(tipoProdutor.equals("COOP") || tipoProdutor.equals("PC")) {
                if(Strings.isEmpty(naturezaOperacao.getNatEntCoop()) || Strings.isEmpty(naturezaOperacao.getSerEntCoop())) {
                    throw new ObjectDefaultException("Natureza ou série eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                }
                
                return NaturezaResponseDto.construir(naturezaOperacao.getNatEntCoop(), naturezaOperacao.getSerEntCoop(), naturezaOperacao.getModNatEntCoop());
            }
            else if(tipoProdutor.equals("TER")) {
                if(Strings.isEmpty(naturezaOperacao.getNatEntTerc()) || Strings.isEmpty(naturezaOperacao.getSerEntTerc())) {
                    throw new ObjectDefaultException("Natureza ou série eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                }
                
                return NaturezaResponseDto.construir(naturezaOperacao.getNatEntTerc(), naturezaOperacao.getSerEntTerc(), naturezaOperacao.getModNatEntTerc());
            }
        }
        else if(natureza.equals("PJ")) {
        	if(produtor.getEmiteNota()) {
                if(tipoProdutor.equals("COOP") || tipoProdutor.equals("PC")) {
                    if(Strings.isEmpty(naturezaOperacao.getNatEntPjCnfCoop()) || Strings.isEmpty(naturezaOperacao.getModNatEntPjCnfCoop())) {
                        throw new ObjectDefaultException("Natureza ou modelo eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }
                    
                    return NaturezaResponseDto.construir(naturezaOperacao.getNatEntPjCnfCoop(), naturezaOperacao.getSerEntPjCnfCoop(), naturezaOperacao.getModNatEntPjCnfCoop());
                }
                else if(tipoProdutor.equals("TER")) {
                    if(Strings.isEmpty(naturezaOperacao.getNatEntPjCnfTerc()) || Strings.isEmpty(naturezaOperacao.getModNatEntPjCnfTerc())) {
                        throw new ObjectDefaultException("Natureza ou modelo eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }
                    
                    return NaturezaResponseDto.construir(naturezaOperacao.getNatEntPjCnfTerc(), naturezaOperacao.getSerEntPjCnfTerc(), naturezaOperacao.getModNatEntPjCnfTerc());
                }
            }
            else {
                if(tipoProdutor.equals("COOP") || tipoProdutor.equals("PC")) {
                    if(Strings.isEmpty(naturezaOperacao.getNatEntPjSnfCoop()) || Strings.isEmpty(naturezaOperacao.getSerEntPjSnfCoop())) {
                        throw new ObjectDefaultException("Natureza ou série eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }
                    
                    return NaturezaResponseDto.construir(naturezaOperacao.getNatEntPjSnfCoop(), naturezaOperacao.getSerEntPjSnfCoop(), naturezaOperacao.getModNatEntPjSnfCoop());
                }
                else if(tipoProdutor.equals("TER")) {
                    if(Strings.isEmpty(naturezaOperacao.getNatEntPjSnfTerc()) || Strings.isEmpty(naturezaOperacao.getSerEntPjSnfTerc())) {
                        throw new ObjectDefaultException("Natureza ou série eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }
                    
                    return NaturezaResponseDto.construir(naturezaOperacao.getNatEntPjSnfTerc(), naturezaOperacao.getSerEntPjSnfTerc(), naturezaOperacao.getModEntPjSnfTerc());
                }
            }
        }
        
        throw new ObjectDefaultException("O produtor informado não possui informação de \"Natureza\" e/ou \"Tipo Produtor\"!");
    }

    private NaturezaResponseDto getNaturezaDevEntrada(Produtor produtor, NaturezaOperacao naturezaOperacao, String naturezaOrigem) {
        String tipoProdutor = produtor.getTipoProdutor().toUpperCase();
        String natureza = produtor.getNatureza().toUpperCase();

        if(natureza.equals("PF")) {
            if(tipoProdutor.equals("COOP") || tipoProdutor.equals("PC")) {
                if((Strings.isEmpty(naturezaOperacao.getNatDevEntCoop()) && !naturezaOrigem.equalsIgnoreCase("199TIC")) || Strings.isEmpty(naturezaOperacao.getSerDevEntCoop())) {
                    throw new ObjectDefaultException("Natureza ou série eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                }
                
                if(naturezaOrigem.equalsIgnoreCase("199TIC")) {
                    return NaturezaResponseDto.construir("599tic", naturezaOperacao.getSerDevEntCoop(), naturezaOperacao.getModDevEntCoop());
                }
                
                return NaturezaResponseDto.construir(naturezaOperacao.getNatDevEntCoop(), naturezaOperacao.getSerDevEntCoop(), naturezaOperacao.getModDevEntCoop());
            }
            else if(tipoProdutor.equals("TER")) {
                if((Strings.isEmpty(naturezaOperacao.getNatDevEntTerc()) && !naturezaOrigem.equalsIgnoreCase("199TIC")) || Strings.isEmpty(naturezaOperacao.getSerDevEntTerc())) {
                    throw new ObjectDefaultException("Natureza ou série eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                }
                
                if(naturezaOrigem.equalsIgnoreCase("199TIC")) {
                    return NaturezaResponseDto.construir("599tic", naturezaOperacao.getSerDevEntTerc(), naturezaOperacao.getModDevEntTerc());
                }
                
                return NaturezaResponseDto.construir(naturezaOperacao.getNatDevEntTerc(), naturezaOperacao.getSerDevEntTerc(), naturezaOperacao.getModDevEntTerc());
            }
        }
        else if(natureza.equals("PJ")) {
            if(produtor.getEmiteNota()) {
                if(tipoProdutor.equals("COOP") || tipoProdutor.equals("PC")) {
                    if(Strings.isEmpty(naturezaOperacao.getNatDevPjCnfCoop()) || Strings.isEmpty(naturezaOperacao.getModDevPjCnfCoop())) {
                        throw new ObjectDefaultException("Natureza ou modelo eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }
                    
                    return NaturezaResponseDto.construir(naturezaOperacao.getNatDevPjCnfCoop(), naturezaOperacao.getSerDevPjCnfCoop(), naturezaOperacao.getModDevPjCnfCoop());
                }
                else if(tipoProdutor.equals("TER")) {
                    if(Strings.isEmpty(naturezaOperacao.getNatDevPjCnfTerc()) || Strings.isEmpty(naturezaOperacao.getModDevPjCnfTerc())) {
                        throw new ObjectDefaultException("Natureza ou modelo eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }
                    
                    return NaturezaResponseDto.construir(naturezaOperacao.getNatDevPjCnfTerc(), naturezaOperacao.getSerDevPjCnfTerc(), naturezaOperacao.getModDevPjCnfTerc());
                }
            }
            else {
                if(tipoProdutor.equals("COOP") || tipoProdutor.equals("PC")) {
                    if(Strings.isEmpty(naturezaOperacao.getNatDevPjSnfCoop()) || Strings.isEmpty(naturezaOperacao.getSerDevPjSnfCoop())) {
                        throw new ObjectDefaultException("Natureza ou série eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }
                    
                    return NaturezaResponseDto.construir(naturezaOperacao.getNatDevPjSnfCoop(), naturezaOperacao.getSerDevPjSnfCoop(), naturezaOperacao.getModDevPjSnfCoop());
                }
                else if(tipoProdutor.equals("TER")) {
                    if(Strings.isEmpty(naturezaOperacao.getNatDevPjSnfTerc()) || Strings.isEmpty(naturezaOperacao.getSerDevPjSnfTerc())) {
                        throw new ObjectDefaultException("Natureza ou modelo eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }
                    
                    return NaturezaResponseDto.construir(naturezaOperacao.getNatDevPjSnfTerc(), naturezaOperacao.getSerDevPjSnfTerc(), naturezaOperacao.getModDevPjSnfTerc());
                }
            }
        }
        
        throw new ObjectDefaultException("O produtor informado não possui informação de \"Natureza\" e/ou \"Tipo Produtor\"!");
    }

    private NaturezaResponseDto getNaturezaFixacao(Produtor produtor, NaturezaOperacao naturezaOperacao) {
        String tipoProdutor = produtor.getTipoProdutor().toUpperCase();
        String natureza = produtor.getNatureza().toUpperCase();

        if(natureza.equals("PF")) {
            if(tipoProdutor.equals("COOP") || tipoProdutor.equals("PC")) {
                if(Strings.isEmpty(naturezaOperacao.getNatFixCoop()) || Strings.isEmpty(naturezaOperacao.getSerFixCoop())) {
                    throw new ObjectDefaultException("Natureza ou série eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                }
                
                return NaturezaResponseDto.construir(naturezaOperacao.getNatFixCoop(), naturezaOperacao.getSerFixCoop(), naturezaOperacao.getModNatFixCoop());
            }
            else if(tipoProdutor.equals("TER")) {
                if(Strings.isEmpty(naturezaOperacao.getNatFixTerc()) || Strings.isEmpty(naturezaOperacao.getSerFixTerc())) {
                    throw new ObjectDefaultException("Natureza ou série eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                }
                
                return NaturezaResponseDto.construir(naturezaOperacao.getNatFixTerc(), naturezaOperacao.getSerFixTerc(), naturezaOperacao.getModNatFixTerc());
            }
        }
        else if(natureza.equals("PJ")) {
            if(produtor.getEmiteNota()) {
                if(tipoProdutor.equals("COOP") || tipoProdutor.equals("PC")) {
                    if(Strings.isEmpty(naturezaOperacao.getNatFixPjCnfCoop()) || Strings.isEmpty(naturezaOperacao.getModFixPjCnfCoop())) {
                        throw new ObjectDefaultException("Natureza ou modelo eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }
                    
                    return NaturezaResponseDto.construir(naturezaOperacao.getNatFixPjCnfCoop(), naturezaOperacao.getSerFixPjCnfCoop(), naturezaOperacao.getModFixPjCnfCoop());
                }
                else if(tipoProdutor.equals("TER")) {
                    if(Strings.isEmpty(naturezaOperacao.getNatFixPjCnfTerc()) || Strings.isEmpty(naturezaOperacao.getModFixPjCnfTerc())) {
                        throw new ObjectDefaultException("Natureza ou modelo eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }
                    
                    return NaturezaResponseDto.construir(naturezaOperacao.getNatFixPjCnfTerc(), naturezaOperacao.getSerFixPjCnfTerc(), naturezaOperacao.getModFixPjCnfTerc());
                }
            }
            else {
                if (tipoProdutor.equals("COOP") || tipoProdutor.equals("PC")) {
                    if (Strings.isEmpty(naturezaOperacao.getNatFixPjSnfCoop()) || Strings.isEmpty(naturezaOperacao.getSerFixPjSnfCoop())) {
                        throw new ObjectDefaultException("Natureza ou série eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }

                    return NaturezaResponseDto.construir(naturezaOperacao.getNatFixPjSnfCoop(), naturezaOperacao.getSerFixPjSnfCoop(), naturezaOperacao.getModFixPjSnfCoop());
                }
                else if (tipoProdutor.equals("TER")) {
                    if (Strings.isEmpty(naturezaOperacao.getNatFixPjSnfTerc()) || Strings.isEmpty(naturezaOperacao.getSerFixPjSnfTerc())) {
                        throw new ObjectDefaultException("Natureza ou série eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }

                    return NaturezaResponseDto.construir(naturezaOperacao.getNatFixPjSnfTerc(), naturezaOperacao.getSerFixPjSnfTerc(), naturezaOperacao.getModFixPjSnfTerc());
                }
            }
        }
        
        throw new ObjectDefaultException("O produtor informado não possui informação de \"Natureza\" e/ou \"Tipo Produtor\"!");
    }

    private NaturezaResponseDto getNaturezaFixacaoSemente(Produtor produtor, NaturezaOperacao naturezaOperacao) {
        String tipoProdutor = produtor.getTipoProdutor().toUpperCase();
        String natureza = produtor.getNatureza().toUpperCase();

        if(natureza.equals("PF")) {
            if(tipoProdutor.equals("COOP") || tipoProdutor.equals("PC")) {
                if(Strings.isEmpty(naturezaOperacao.getNatFixSemCoop()) || Strings.isEmpty(naturezaOperacao.getSerFixSemCoop())) {
                    throw new ObjectDefaultException("Natureza ou série eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                }

                return NaturezaResponseDto.construir(naturezaOperacao.getNatFixSemCoop(), naturezaOperacao.getSerFixSemCoop(), naturezaOperacao.getModNatFixSemCoop());
            }
            else if(tipoProdutor.equals("TER")) {
                if(Strings.isEmpty(naturezaOperacao.getNatFixSemTerc()) || Strings.isEmpty(naturezaOperacao.getSerFixSemTerc())) {
                    throw new ObjectDefaultException("Natureza ou série eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                }

                return NaturezaResponseDto.construir(naturezaOperacao.getNatFixSemTerc(), naturezaOperacao.getSerFixSemTerc(), naturezaOperacao.getModNatFixSemTerc());
            }
        }
        else if(natureza.equals("PJ")) {
            if(produtor.getEmiteNota()) {
                if(tipoProdutor.equals("COOP") || tipoProdutor.equals("PC")) {
                    if(Strings.isEmpty(naturezaOperacao.getNatFixSemPjCnfCoop()) || Strings.isEmpty(naturezaOperacao.getModFixSemPjCnfCoop())) {
                        throw new ObjectDefaultException("Natureza ou modelo eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }

                    return NaturezaResponseDto.construir(naturezaOperacao.getNatFixSemPjCnfCoop(), naturezaOperacao.getSerFixSemPjCnfCoop(), naturezaOperacao.getModFixSemPjCnfCoop());
                }
                else if(tipoProdutor.equals("TER")) {
                    if(Strings.isEmpty(naturezaOperacao.getNatFixSemPjCnfTerc()) || Strings.isEmpty(naturezaOperacao.getModFixSemPjCnfTerc())) {
                        throw new ObjectDefaultException("Natureza ou modelo eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }

                    return NaturezaResponseDto.construir(naturezaOperacao.getNatFixSemPjCnfTerc(), naturezaOperacao.getSerFixSemPjCnfTerc(), naturezaOperacao.getModFixSemPjCnfTerc());
                }
            }
            else {
                if (tipoProdutor.equals("COOP") || tipoProdutor.equals("PC")) {
                    if (Strings.isEmpty(naturezaOperacao.getNatFixSemPjSnfCoop()) || Strings.isEmpty(naturezaOperacao.getSerFixSemPjSnfCoop())) {
                        throw new ObjectDefaultException("Natureza ou série eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }

                    return NaturezaResponseDto.construir(naturezaOperacao.getNatFixSemPjSnfCoop(), naturezaOperacao.getSerFixSemPjSnfCoop(), naturezaOperacao.getModFixSemPjSnfCoop());
                }
                else if (tipoProdutor.equals("TER")) {
                    if (Strings.isEmpty(naturezaOperacao.getNatFixSemPjSnfTec()) || Strings.isEmpty(naturezaOperacao.getSerFixSemPjSnfTerc())) {
                        throw new ObjectDefaultException("Natureza ou série eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }

                    return NaturezaResponseDto.construir(naturezaOperacao.getNatFixSemPjSnfTec(), naturezaOperacao.getSerFixSemPjSnfTerc(), naturezaOperacao.getModFixSemPjSnfTerc());
                }
            }
        }

        throw new ObjectDefaultException("O produtor informado não possui informação de \"Natureza\" e/ou \"Tipo Produtor\"!");
    }

    private NaturezaResponseDto getNaturezaDevSemente(Produtor produtor, NaturezaOperacao naturezaOperacao) {
        String tipoProdutor = produtor.getTipoProdutor().toUpperCase();
        String natureza = produtor.getNatureza().toUpperCase();

        if(natureza.equals("PF")) {
            if(tipoProdutor.equals("COOP") || tipoProdutor.equals("PC")) {
                if(Strings.isEmpty(naturezaOperacao.getNatDevFixCoop()) || Strings.isEmpty(naturezaOperacao.getSerDevFixCoop())) {
                    throw new ObjectDefaultException("Natureza ou série eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                }

                return NaturezaResponseDto.construir(naturezaOperacao.getNatDevFixCoop(), naturezaOperacao.getSerDevFixCoop(), naturezaOperacao.getModDevFixCoop());
            }
            else if(tipoProdutor.equals("TER")) {
                if(Strings.isEmpty(naturezaOperacao.getNatDevFixTerc()) || Strings.isEmpty(naturezaOperacao.getSerDevFixTerc())) {
                    throw new ObjectDefaultException("Natureza ou série eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                }

                return NaturezaResponseDto.construir(naturezaOperacao.getNatDevFixTerc(), naturezaOperacao.getSerDevFixTerc(), naturezaOperacao.getModNatDevFixTerc());
            }
        }
        else if(natureza.equals("PJ")) {
            if(produtor.getEmiteNota()) {
                if(tipoProdutor.equals("COOP") || tipoProdutor.equals("PC")) {
                    if(Strings.isEmpty(naturezaOperacao.getNatDevFixPjCnfCoop()) || Strings.isEmpty(naturezaOperacao.getModDevFixPjCnfCoop())) {
                        throw new ObjectDefaultException("Natureza ou modelo eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }

                    return NaturezaResponseDto.construir(naturezaOperacao.getNatDevFixPjCnfCoop(), naturezaOperacao.getSerDevFixPjCnfCoop(), naturezaOperacao.getModDevFixPjCnfCoop());
                }
                else if(tipoProdutor.equals("TER")) {
                    if(Strings.isEmpty(naturezaOperacao.getNatDevFixPjCnfTerc()) || Strings.isEmpty(naturezaOperacao.getModDevFixPjCnfTerc())) {
                        throw new ObjectDefaultException("Natureza ou modelo eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }

                    return NaturezaResponseDto.construir(naturezaOperacao.getNatDevFixPjCnfTerc(), naturezaOperacao.getSerDevFixPjCnfTerc(), naturezaOperacao.getModDevFixPjCnfTerc());
                }
            }
            else {
                if (tipoProdutor.equals("COOP") || tipoProdutor.equals("PC")) {
                    if (Strings.isEmpty(naturezaOperacao.getNatDevFixPjSnfCoop()) || Strings.isEmpty(naturezaOperacao.getSerDevFixPjSnfCoop())) {
                        throw new ObjectDefaultException("Natureza ou série eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }

                    return NaturezaResponseDto.construir(naturezaOperacao.getNatDevFixPjSnfCoop(), naturezaOperacao.getSerDevFixPjSnfCoop(), naturezaOperacao.getModDevFixPjSnfCoop());
                }
                else if (tipoProdutor.equals("TER")) {
                    if (Strings.isEmpty(naturezaOperacao.getNatDevFixPjSnfTerc()) || Strings.isEmpty(naturezaOperacao.getSerDevFixPjSnfTerc())) {
                        throw new ObjectDefaultException("Natureza ou série eletrônico para o grupo de natureza de operação estão em branco, favor solicitar correção para o setor fiscal");
                    }

                    return NaturezaResponseDto.construir(naturezaOperacao.getNatDevFixPjSnfTerc(), naturezaOperacao.getSerDevFixPjSnfTerc(), naturezaOperacao.getModDevFixPjSnfTerc());
                }
            }
        }

        throw new ObjectDefaultException("O produtor informado não possui informação de \"Natureza\" e/ou \"Tipo Produtor\"!");
    }
}
