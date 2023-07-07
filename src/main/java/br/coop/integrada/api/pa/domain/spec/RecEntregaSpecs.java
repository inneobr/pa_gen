package br.coop.integrada.api.pa.domain.spec;

import java.util.Date;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.coop.integrada.api.pa.domain.enums.TipoPesagemBalancaEnum;
import br.coop.integrada.api.pa.domain.enums.tratamentoTransacaoRe.MovimentoReEnum;
import br.coop.integrada.api.pa.domain.model.Pesagem;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.parametros.ParametrosUsuarioEstabelecimento;
import br.coop.integrada.api.pa.domain.model.produto.Produto;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntregaItem;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;

public class RecEntregaSpecs {

    public static Specification<RecEntrega> codigoEstabelecimentoEquals(String codigoEstabelecimento){
        return (root, query, builder) -> {
            if(StringUtils.hasText(codigoEstabelecimento)) {
                return builder.equal(builder.upper(root.get("codEstabel")), codigoEstabelecimento.toUpperCase());
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<RecEntrega> safraEquals(Integer safra){
        return (root, query, builder) -> {
            if(safra != null) {
                return builder.equal(root.get("safra"), safra);
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<RecEntrega> safraEquals(String safra){
        return (root, query, builder) -> {
            if(safra != null) {
                return builder.equal(root.get("safra"), safra);
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<RecEntrega> nrDocumentoPesagemEquals(Long nrDocPesagem){
        return (root, query, builder) -> {
            if(nrDocPesagem != null) {
                return builder.equal(root.get("nrDocPes"), nrDocPesagem);
            }
            return builder.and(new Predicate[0]);
        };
    }

    public static Specification<RecEntrega> rendaLiquidaAtualDiferenteDeZero(){
        return (root, query, builder) -> {
            Join<RecEntregaItem, RecEntrega> itens = root.join("itens");
            return builder.notEqual(itens.get("rendaLiquidaAtu"), 0);
        };
    }

    public static Specification<RecEntrega> doSituacao(Situacao situacao){
        return (root, query, builder) -> {
            if(situacao != null) {
                if(situacao.equals(Situacao.ATIVO)) {
                    return builder.isNull(root.get("dataInativacao"));
                }
                else if(situacao.equals(Situacao.INATIVO)) {
                    return builder.isNotNull(root.get("dataInativacao"));
                }
            }
            return builder.and(new Predicate[0]);
        };
    }
    	
	public static Specification<RecEntrega> codEmitenteEquals(String codEmitente){
		return (root, query, builder) -> {
			if(StringUtils.hasText(codEmitente)) {
				return builder.equal(builder.upper(root.get("codEmitente")), codEmitente.toUpperCase());
			}
			return builder.and(new Predicate[0]);			
		};
	}
	
	public static Specification<RecEntrega> doMotorista(String motorista){
		return (root, query, builder) -> {
			if(StringUtils.hasText(motorista)) {
				return builder.like(
						builder.upper(root.get("motorista")), "%"+ motorista.toUpperCase() +"%"
				);
			}
			return builder.and(new Predicate[0]);			
		};
	}
	
	public static Specification<RecEntrega> matriculaImovelEquals(Long matriculaImovel){
		return (root, query, builder) -> {
			if(matriculaImovel != null) {
				return builder.equal(root.get("matricula"), matriculaImovel);
			}
			return builder.and(new Predicate[0]);			
		};
	}
	
	public static Specification<RecEntrega> daPlaca(String placa){
		return (root, query, builder) -> {
			if(StringUtils.hasText(placa)) {
				return builder.like(
						builder.upper(root.get("placa")), "%"+ placa.toUpperCase() +"%"
				);
			}
			return builder.and(new Predicate[0]);			
		};
	}
	
	public static Specification<RecEntrega> ativo(){
		return (root, query, builder) -> 
			builder.isNull(root.get("dataInativacao"));
	}
	
	public static Specification<RecEntrega> doPeriodo(Date inicio, Date termino){
		return (root, query, builder) -> {
			if(inicio != null && termino != null) {
				return builder.between(root.get("dtEmissao"), inicio, termino);
			}
			return builder.and(new Predicate[0]);			
		};
	}
	
	public static Specification<RecEntrega> doNroDocPesagem(Integer inicio, Integer termino){
		return (root, query, builder) -> {
			if(inicio != null && termino != null) {
				return builder.between(root.get("nrDocPes"), inicio, termino);
			}
			return builder.and(new Predicate[0]);			
		};
	}
	
	public static Specification<RecEntrega> nroDocPesagemEquals(Integer nroDocPesagem) {
		return (root, query, builder) -> {
			if(nroDocPesagem != null) {
				return builder.equal(root.get("nrDocPes"), nroDocPesagem);
			}
			return builder.and(new Predicate[0]);			
		};
	}

	public static Specification<RecEntrega> codigoGrupoProdutoEquals(String codigoGrupoProduto) {
		return (root, query, builder) -> {
			if(codigoGrupoProduto != null && Strings.isNotEmpty(codigoGrupoProduto.trim())) {
				return builder.equal(root.get("fmCodigo"), codigoGrupoProduto);
			}
			return builder.and(new Predicate[0]);			
		};
	}

	public static Specification<RecEntrega> comPesoAutomatico(TipoPesagemBalancaEnum tipo) {
		return (root, query, builder) -> {
			if(tipo != null) {
				Subquery<Pesagem> queryPesagem = query.subquery(Pesagem.class);
            	Root<Pesagem> rootPesagem = queryPesagem.from(Pesagem.class);
            	queryPesagem.select(rootPesagem.get("nroDocPesagem"));
            	
            	Predicate predicateFieldCodEstabelecimento = builder.equal(rootPesagem.get("codEstabelecimento"), root.get("codEstabel"));
            	Predicate predicateFieldNroDocPesagem = builder.equal(rootPesagem.get("nroDocPesagem"), root.get("nrDocPes"));
				Predicate predicateFieldPesoEntrada = builder.equal(rootPesagem.get("pesAutomatica"), tipo.isPesoAutomatico());
				Predicate predicateFieldPesoSaida = builder.equal(rootPesagem.get("pesAutomatica2"), tipo.isPesoAutomatico());
				
				if(tipo.isPesoAutomatico()) {
					queryPesagem.where(predicateFieldCodEstabelecimento, predicateFieldNroDocPesagem, builder.and(predicateFieldPesoEntrada, predicateFieldPesoSaida));
				}else {
					queryPesagem.where(predicateFieldCodEstabelecimento, predicateFieldNroDocPesagem, builder.or(predicateFieldPesoEntrada, predicateFieldPesoSaida));
				}
				
				return builder.equal(root.get("nrDocPes"), queryPesagem);
			}
			return builder.and(new Predicate[0]);			
		};
	}

	public static Specification<RecEntrega> doProduto(String produto) {
        return (root, query, builder) -> {
            if(Strings.isNotEmpty(produto) && Strings.isNotEmpty(produto.trim())) {
            	Join<RecEntregaItem, RecEntrega> recEntregaItem = root.join("itens");
                return builder.like(builder.upper(recEntregaItem.get("codProduto")), "%" + produto.toUpperCase() + "%");
            }
            return null;
        };
	}

	public static Specification<RecEntrega> doStatus(MovimentoReEnum status) {
        return (root, query, builder) -> {
            if(status != null) {
                return builder.equal(root.get("codSit"), status.getCodSit());
            }
            return null;
        };
	}
	
	public static Specification<RecEntrega> nrReBetween(Long nrReInicial, Long nrReFinal){
        return (root, query, builder) -> {
            if(nrReInicial != null && nrReFinal != null) {
                return builder.between(root.get("nrRe"), nrReInicial, nrReFinal);
            }
            return builder.and(new Predicate[0]);
        };
    }
}
