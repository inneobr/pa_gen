package br.coop.integrada.api.pa.aplication.controller.recEntrega;

import br.coop.integrada.api.pa.domain.modelDto.recEntrega.RecEntregaReportFilterDTO;
import br.coop.integrada.api.pa.domain.service.recEntrega.RecEntregaReportService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static br.coop.integrada.api.pa.report.ReportMessage.*;

@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class RecEntregaReportController {
    private static final Logger log = LoggerFactory.getLogger(RecEntregaReportController.class);

    private RecEntregaReportService reportService;

    @PutMapping("/api/pa/v1/gerar-relatorio-re")
    public ResponseEntity<?> gerarRelatorioDeRE(@RequestBody RecEntregaReportFilterDTO reportFilter) {
        try {
            log.info(BEGIN_REPORT);
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "recebimento-de-entrega.pdf");
            var relatorio = reportService.gerarRelatorio(reportFilter);
            log.info(FINALIZED_REPORT);
            return new ResponseEntity<byte[]>(relatorio, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERROR_IN_REPORT);
        }
    }
}
