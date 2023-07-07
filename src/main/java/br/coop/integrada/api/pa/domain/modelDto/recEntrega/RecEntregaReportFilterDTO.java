package br.coop.integrada.api.pa.domain.modelDto.recEntrega;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static br.coop.integrada.api.pa.report.ReportImagePath.LOGO_INTEGRADA;

public record RecEntregaReportFilterDTO(String estabelecimento, Long numeroInicialRE, Long numeroFinalRE,
                                        Date dataInicialEmissao, Date dataFinalEmissao, Long produtorInicial,
                                        Long produtorFinal, boolean apenasREPendente) {

    public Map<String, Object> toMap() {
        var parameters = new HashMap<String, Object>();
        parameters.put("imageOfLogo", this.getClass().getResourceAsStream(LOGO_INTEGRADA));
        parameters.put("REPORT_LOCALE", new Locale("pt", "BR"));
        parameters.put("estabelecimento", estabelecimento);
        parameters.put("numeroInicialRE", numeroInicialRE);
        parameters.put("numeroFinalRE", numeroFinalRE);
        parameters.put("dataInicialEmissao", dataInicialEmissao);
        parameters.put("dataFinalEmissao", dataFinalEmissao);
        parameters.put("produtorInicial", produtorInicial);
        parameters.put("produtorFinal", produtorFinal);
        parameters.put("apenasREPendente", apenasREPendente);
        return parameters;
    }
}
