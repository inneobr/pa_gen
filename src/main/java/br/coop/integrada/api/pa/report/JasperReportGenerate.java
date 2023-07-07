package br.coop.integrada.api.pa.report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;

@Component
public class JasperReportGenerate {

    @PersistenceContext
    private EntityManager entityManager;

    public byte[] createPdfReport(Map<String, Object> parameters, InputStream streamOfReport) throws JRException, SQLException {
        var info = (EntityManagerFactoryInfo) entityManager.getEntityManagerFactory();
        var connection = info.getDataSource().getConnection();
        var jasperReport = JasperCompileManager.compileReport(streamOfReport);
        var jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
