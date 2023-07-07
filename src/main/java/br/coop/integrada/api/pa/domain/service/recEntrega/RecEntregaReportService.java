package br.coop.integrada.api.pa.domain.service.recEntrega;

import br.coop.integrada.api.pa.domain.impl.RecEntregaRepImpl;
import br.coop.integrada.api.pa.domain.modelDto.recEntrega.RecEntregaReportFilterDTO;
import br.coop.integrada.api.pa.report.JasperReportGenerate;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

import static br.coop.integrada.api.pa.report.ReportFilePath.RECEBIMENTO_DE_ENTREGA_JRXML;

@Service
@AllArgsConstructor
public class RecEntregaReportService {

    private JasperReportGenerate jasperReportGenerate;
    private RecEntregaRepImpl recEntregaRep;

    public byte[] gerarRelatorio(RecEntregaReportFilterDTO reportFilter) throws JRException, SQLException {
        var streamOfReport = this.getClass().getResourceAsStream(RECEBIMENTO_DE_ENTREGA_JRXML);
        var pdfReport = jasperReportGenerate.createPdfReport(reportFilter.toMap(), streamOfReport);
        recEntregaRep.updateImpressoRecEntrega(reportFilter);
        return pdfReport;
    }
}
