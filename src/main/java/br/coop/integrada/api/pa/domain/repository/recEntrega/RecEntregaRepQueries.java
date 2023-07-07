package br.coop.integrada.api.pa.domain.repository.recEntrega;

import br.coop.integrada.api.pa.domain.enums.StatusIntegracao;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.modelDto.recEntrega.RecEntregaFilter;
import br.coop.integrada.api.pa.domain.modelDto.recEntrega.RecEntregaReportFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface RecEntregaRepQueries {

	Page<RecEntrega> buscarRomaneios(Pageable pageable, RecEntregaFilter filter);

	void updateIntegracaoRecEntrega(Date dataIntegracao, Boolean logIntegrado, StatusIntegracao statusIntegracao, Long id);
	void updateIntegracaoRecEntregaItem(Date dataIntegracao, Boolean logIntegrado, StatusIntegracao statusIntegracao, Long id);
	void updateCodSitRecEntrega(Integer codSit, List<Long> ids);
	void updateCodSitRecEntrega(Integer codSit, Long id);
	void updateImpressoRecEntrega(RecEntregaReportFilterDTO filterDTO);
}