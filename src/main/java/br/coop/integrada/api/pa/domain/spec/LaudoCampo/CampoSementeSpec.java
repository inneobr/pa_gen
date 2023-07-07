package br.coop.integrada.api.pa.domain.spec.LaudoCampo;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import org.springframework.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import br.coop.integrada.api.pa.domain.spec.enums.Situacao;
import br.coop.integrada.api.pa.domain.model.estabelecimentos.Estabelecimento;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampo;

public class CampoSementeSpec {
	
	
	public static Specification<SementeCampo> doEstabelecimento(String codigo){
        return (root, query, builder) -> {
            if(StringUtils.hasText(codigo)) {
                query.distinct(true);
                Join<SementeCampo, Estabelecimento> estabelecimento = root.join("estabelecimento");
                
                return builder.equal(
                    builder.upper(estabelecimento.get("codigo")),codigo.toUpperCase()
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	
	public static Specification<SementeCampo> daSafra(Integer safra){
		return (root, query, builder) -> {
            if(safra != null) {
                return builder.equal(root.get("safra"), safra);
            }
            return builder.and(new Predicate[0]);
        };
	}
	
	public static Specification<SementeCampo> doGrupo(String fmCodigo){
        return (root, query, builder) -> {
            if(StringUtils.hasText(fmCodigo)) {
                query.distinct(true);
                Join<SementeCampo, GrupoProduto> grupoProduto = root.join("grupoProduto");
                
                return builder.equal(
                    builder.upper(grupoProduto.get("fmCodigo")), fmCodigo.toUpperCase()
                );
            }
            return builder.and(new Predicate[0]);
        };
    }
	
	
	public static Specification<SementeCampo> daSituacao(Situacao situacao){
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
}
