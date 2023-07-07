package br.coop.integrada.api.pa.aplication.controller.re;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.coop.integrada.api.pa.domain.enums.gerarRe.TipoEntradaEnum;
import br.coop.integrada.api.pa.domain.model.nfRemessa.NfRemessaFiltro;
import br.coop.integrada.api.pa.domain.model.recEntrega.RecEntrega;
import br.coop.integrada.api.pa.domain.modelDto.integration.solicitaNfRemessa.nfErpGen.IntegracaoRequestNfErpGen;
import br.coop.integrada.api.pa.domain.modelDto.integration.solicitaNfRemessa.nfErpGen.IntegracaoResponseNfErpGen;
import br.coop.integrada.api.pa.domain.modelDto.rependente.validation.EntradaReValidation;
import br.coop.integrada.api.pa.domain.service.rependente.EntradaReService;
import br.coop.integrada.api.pa.domain.service.rependente.GerarReService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/gerar-re")
@Tag(name = "Gerar Re")
public class GerarReController {

	@Autowired
	private GerarReService gerarReService;

	@Autowired
	private EntradaReService entradaRe;

	@PostMapping
	public ResponseEntity<List<RecEntrega>> gerarRe(@RequestBody TipoEntradaEnum tipoEntradaEnum,
			List<RecEntrega> recEntregaLista) throws CloneNotSupportedException {
		List<RecEntrega> recEntrega = gerarReService.gerarRe(tipoEntradaEnum, recEntregaLista);
		return ResponseEntity.ok(recEntrega);
	}

	@PostMapping("/entrada-re")
	public ResponseEntity<?> entradaRe(@RequestBody EntradaReValidation entradaReValidation) {
		return ResponseEntity.ok(entradaRe.validarEntradaProducao(entradaReValidation));
	}
	
	@GetMapping("/solicitar-nf-erp")
	public void solicitarNfErp() {
		entradaRe.solicitarNfErp(null);
	}
	
	@GetMapping("/solicitar-nf-erp/{idNfRemessa}")
	public void solicitarNfErp(@PathVariable(name="idNfRemessa") Long idNfRemessa) {
		entradaRe.solicitarNfErp(idNfRemessa);
	}
	
	@GetMapping("/solicitar-nf-erp-por-status")
	public void solicitarNfErpPorStatus(NfRemessaFiltro filter) {
		entradaRe.solicitarNfErpPorStatus(filter);
	}
	
	@PostMapping("/nf-integration-return")
	public ResponseEntity<IntegracaoResponseNfErpGen> nfIntegrationReturn(@RequestBody IntegracaoRequestNfErpGen integracaoRequestNfErpGen) {		
		return ResponseEntity.ok(entradaRe.nfIntegrationReturn(integracaoRequestNfErpGen));
	}
	
	
	
}
