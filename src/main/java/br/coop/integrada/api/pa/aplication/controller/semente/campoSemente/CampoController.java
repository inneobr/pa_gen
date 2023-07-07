package br.coop.integrada.api.pa.aplication.controller.semente.campoSemente;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import br.coop.integrada.api.pa.domain.modelDto.semente.SementeCampoFilter;
import br.coop.integrada.api.pa.domain.service.semente.campoSemente.CampoService;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/semente/campo")
@Tag(name = "Semente Produtor", description = "Campo semente produtor")
public class CampoController {
	
	@Autowired
	private CampoService campoService;
	
	@GetMapping("/filtrar")
	   public ResponseEntity<?> findAllFilter(SementeCampoFilter filter) {
	       return ResponseEntity.ok(campoService.getCampoSemente(filter));
	}
	
	@GetMapping("/pageable/filtrar")
	   public ResponseEntity<?> findPagesFilter(
			   @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
			   SementeCampoFilter filter) {
	       return ResponseEntity.ok(campoService.getCampoSementePagina(filter, pageable));
	}
}
