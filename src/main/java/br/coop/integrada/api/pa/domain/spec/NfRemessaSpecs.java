package br.coop.integrada.api.pa.domain.spec;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.coop.integrada.api.pa.domain.model.nfRemessa.NfRemessa;

public class NfRemessaSpecs {

	public static Specification<NfRemessa> codEstabelecimentoEquals(String codigoEstabeleciemnto){
        return (root, query, builder) -> {
            if(StringUtils.hasText(codigoEstabeleciemnto)) {
                return builder.equal(builder.upper(root.get("codEstabel")), codigoEstabeleciemnto.toUpperCase());
            }
            return builder.and(new Predicate[0]);
        };
    }

	public static Specification<NfRemessa> dtCriacaoBetween(Date dataInicial, Date dataFinal){
        return (root, query, builder) -> {
            if(dataInicial != null && dataFinal != null) {
            	Calendar calendarInicial = Calendar.getInstance();
            	calendarInicial.setTime(dataInicial);
            	calendarInicial.set(Calendar.HOUR, 00);
            	calendarInicial.set(Calendar.MINUTE, 00);
            	calendarInicial.set(Calendar.SECOND, 00);
            	
            	Calendar calendarFinal = Calendar.getInstance();
            	calendarFinal.setTime(dataFinal);
            	calendarFinal.set(Calendar.HOUR, 23);
            	calendarFinal.set(Calendar.MINUTE, 59);
            	calendarFinal.set(Calendar.SECOND, 59);
                return builder.between(root.get("dtCriacao"), calendarInicial.getTime(), calendarFinal.getTime());
            }
            return builder.and(new Predicate[0]);
        };
    }

	public static Specification<NfRemessa> pendenciasFiscaisEquals(Boolean pendenciasFiscais){
        return (root, query, builder) -> {
            if(pendenciasFiscais != null) {
                return builder.equal(root.get("pendenciasFiscais"), pendenciasFiscais);
            }
            return builder.and(new Predicate[0]);
        };
    }
}
