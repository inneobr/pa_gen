package br.coop.integrada.api.pa.aplication.controller.semente.campoSemente;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import br.coop.integrada.api.pa.domain.modelDto.semente.campoSemente.CampoProdutorFilter;
import br.coop.integrada.api.pa.domain.service.semente.campoSemente.CampoProdutorService;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/semente/campo-produtor")
public class CampoProdutorController {

	@Autowired
	private CampoProdutorService campoProdutorService;
	
	@GetMapping("/filtrar")
	   public ResponseEntity<?> findAllFilter(CampoProdutorFilter filter) {
	       return ResponseEntity.ok(campoProdutorService.getCampoProdutor(filter));
	}
}
