package br.coop.integrada.api.pa.domain.service.dashBoard;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.coop.integrada.api.pa.domain.model.dashBoard.DashBoard;
import br.coop.integrada.api.pa.domain.modelDto.dashBoard.ConsultaPrincipalFilter;
import br.coop.integrada.api.pa.domain.repository.dashBoard.DashBoardRep;
import br.coop.integrada.api.pa.domain.service.recEntrega.RecEntregaService;

@Service
public class DashBoardService {
	private static final Logger log = LoggerFactory.getLogger(DashBoardService.class);
	
	@Autowired
	private DashBoardRep dashBoardRep;
	
	public DashBoard consultaPrincipal(ConsultaPrincipalFilter consultaPrincipalFilter) {
		
		
		
		Map<String, BigDecimal> map = dashBoardRep.consultaPrincipal(
				consultaPrincipalFilter.getRegional(),
				consultaPrincipalFilter.getCodEstabel(),
				consultaPrincipalFilter.getSafra(),
				consultaPrincipalFilter.getTipoProduto() );
		
		DashBoard dashBoard = new DashBoard();
		dashBoard.setRomaneio(map.get("ROMANEIOS"));
		dashBoard.setPesoLiquido(map.get("PESO_LIQUIDO"));
		dashBoard.setTonPesoLiquido(map.get("TON_PESO_LIQUIDO"));
		dashBoard.setScPesoLiquido(map.get("SC_PESO_LIQUIDO"));
		dashBoard.setTonRendaLiquida(map.get("TON_RENDA_LIQUIDA"));
		dashBoard.setScRendaLiquida(map.get("SC_RENDA_LIQUIDA"));
		dashBoard.setEntradaSemTicket(map.get("ENTRADA_SEM_TICKET"));
		dashBoard.setEntradaComTicket(map.get("ENTRADA_COM_TICKET"));
		
		return dashBoard;
		
	}

}
