package br.coop.integrada.api.pa.domain.spec;

import java.util.Date;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.coop.integrada.api.pa.domain.enums.StatusPesagem;
import br.coop.integrada.api.pa.domain.enums.TipoPesagemBalancaEnum;
import br.coop.integrada.api.pa.domain.model.Pesagem;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.Produto;

public class PesagemSpecs {
	
	public static Specification<Pesagem> doEstabelecimento(String codEstabel){
		return (root, query, builder) -> {
			if(StringUtils.hasText(codEstabel)) {
				return builder.like(root.get("codEstabelecimento"), codEstabel);
			}
			return builder.and(new Predicate[0]);
		};
	}
	
	public static Specification<Pesagem> doEstabelecimentoEqual(String codEstabel){
		return (root, query, builder) -> {
			if(StringUtils.hasText(codEstabel)) {
				return builder.equal(root.get("codEstabelecimento"), codEstabel);
			}
			return builder.and(new Predicate[0]);
		};
	}
	
	public static Specification<Pesagem> daSafra(String safra){
		return (root, query, builder) -> {
			if(StringUtils.hasText(safra)) {
				return builder.equal(root.get("safra"), safra);
			}
			return builder.and(new Predicate[0]);			
		};
	}
	
	public static Specification<Pesagem> doProduto(String produto){
		return (root, query, builder) -> {
			if(StringUtils.hasText(produto)) {
				Predicate predicateFieldNome = builder.like(builder.upper(root.get("nomeProduto")), "%"+ produto.toUpperCase() +"%");
				Predicate predicateFieldCodigo = builder.equal(root.get("codProduto"), produto);
				return builder.or(predicateFieldNome, predicateFieldCodigo);
			}
			return builder.and(new Predicate[0]);			
		};
	}
	
	public static Specification<Pesagem> doTipoPesagem(String tipoPesagem){
		return (root, query, builder) -> {
			if(StringUtils.hasText(tipoPesagem)) {
				return builder.like(
						builder.upper(root.get("tipoPesagem")), "%"+ tipoPesagem.toUpperCase() +"%"
				);
			}
			return builder.and(new Predicate[0]);			
		};
	}
	
	public static Specification<Pesagem> situacaoNotEquals(String situacao){
		return (root, query, builder) -> {
			if(StringUtils.hasText(situacao)) {
				return builder.notEqual(builder.upper(root.get("situacao")), situacao.toUpperCase());
			}
			return builder.and(new Predicate[0]);			
		};
	}
	
	public static Specification<Pesagem> doLogReJava(Boolean logReJava){
		return (root, query, builder) -> {
			if(logReJava != null) {
				return builder.equal(root.get("logReJava"), logReJava);
			}
			return builder.and(new Predicate[0]);			
		};
	}
	
	public static Specification<Pesagem> doProdutor(String produtor){
		return (root, query, builder) -> {
			if(StringUtils.hasText(produtor)) {
				if(produtor.matches("^\\d+$")) {
					return builder.equal(root.get("codProdutor"), produtor);
				}else {
					return builder.like(builder.upper(root.get("nomeProdutor")), "%"+ produtor.toUpperCase() +"%");
				}
			}
			return builder.and(new Predicate[0]);			
		};
	}
	
	public static Specification<Pesagem> doMotorista(String motorista){
		return (root, query, builder) -> {
			if(StringUtils.hasText(motorista)) {
				return builder.like(
						builder.upper(root.get("motorista")), "%"+ motorista.toUpperCase() +"%"
				);
			}
			return builder.and(new Predicate[0]);			
		};
	}
	
	public static Specification<Pesagem> doImovel(String imovel){
		return (root, query, builder) -> {
			if(StringUtils.hasText(imovel)) {
				if(imovel.matches("^\\d+$")) {
					return builder.equal(root.get("codImovel"), imovel);
				}else{
					return builder.like(builder.upper(root.get("nomeImovel")), "%"+ imovel.toUpperCase() +"%");
				}
			}
			return builder.and(new Predicate[0]);			
		};
	}
	
	public static Specification<Pesagem> daPlaca(String placa){
		return (root, query, builder) -> {
			if(StringUtils.hasText(placa)) {
				return builder.like(
						builder.upper(root.get("placa")), "%"+ placa.toUpperCase() +"%"
				);
			}
			return builder.and(new Predicate[0]);			
		};
	}
	
	public static Specification<Pesagem> doStatus(StatusPesagem status){
		return (root, query, builder) -> {
			if(status != null) {
				return builder.equal(root.get("status"), status);
			}
			return builder.and(new Predicate[0]);
		};
	}
			
	public static Specification<Pesagem> ativo(){
		return (root, query, builder) -> 
			builder.isNull(root.get("dataInativacao"));
	}
		
	public static Specification<Pesagem> doPeriodo(Date inicio, Date termino){
		return (root, query, builder) -> {
			if(inicio != null && termino != null) {
				return builder.between(root.get("dataEntrada"), inicio, termino);
			}
			return builder.and(new Predicate[0]);			
		};
	}
	
	public static Specification<Pesagem> doNroDocPesagem(Integer inicio, Integer termino){
		return (root, query, builder) -> {
			if(inicio != null && termino != null) {
				return builder.between(root.get("nroDocPesagem"), inicio, termino);
			}
			return builder.and(new Predicate[0]);			
		};
	}
	
	public static Specification<Pesagem> nroDocPesagemEquals(Integer nroDocPesagem) {
		return (root, query, builder) -> {
			if(nroDocPesagem != null) {
				return builder.equal(root.get("nroDocPesagem"), nroDocPesagem);
			}
			return builder.and(new Predicate[0]);			
		};
	}
	
	public static Specification<Pesagem> doGrupoProduto(Long id){
		return (root, query, builder) -> {
			if(id != null) {
				Join<Produto, Pesagem> produto = root.join("produto");
				Join<GrupoProduto, Produto> grupoProduto = produto.join("grupoProduto");
				return builder.equal(grupoProduto.get("id"), id);
			}
			return builder.and(new Predicate[0]);			
		};
	}

	
	public static Specification<Pesagem> comPesoAutomatico(TipoPesagemBalancaEnum tipo){
		return (root, query, builder) -> {
			if(tipo != null) {
				Predicate predicateFieldPesoEntrada = builder.equal(root.get("pesAutomatica"), tipo.isPesoAutomatico());
				Predicate predicateFieldPesoSaida = builder.equal(root.get("pesAutomatica2"), tipo.isPesoAutomatico());
				
				if(tipo.isPesoAutomatico()) {
					return builder.and(predicateFieldPesoEntrada, predicateFieldPesoSaida);
				}else {
					return builder.or(predicateFieldPesoEntrada, predicateFieldPesoSaida);
				}
			}
			return builder.and(new Predicate[0]);			
		};
	}
}
