package br.coop.integrada.api.pa.aplication.controller.filtroDocumentos;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.coop.integrada.api.pa.domain.modelDto.filtroDocumentos.RequestFiltroDocumentos;
import br.coop.integrada.api.pa.domain.service.FiltroValidService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/documentos")
@Tag(name = "Validação", description = "Validação CPF, CNPJ, Login, etc.")
public class FiltroDocumentosController {	
	private final FiltroValidService validService;
	
	@PostMapping
	public ResponseEntity<?> getCpf(@RequestBody @Valid RequestFiltroDocumentos request){
		return ResponseEntity.ok(validService.getDocumentoValido(request));		
	} 
	
}
