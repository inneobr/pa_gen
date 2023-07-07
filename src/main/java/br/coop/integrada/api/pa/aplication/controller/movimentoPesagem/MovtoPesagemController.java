package br.coop.integrada.api.pa.aplication.controller.movimentoPesagem;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.coop.integrada.api.pa.domain.model.movtoPesagem.MovtoPesagem;
import br.coop.integrada.api.pa.domain.modelDto.movimentoPesagem.MovtoPesagemDto;
import br.coop.integrada.api.pa.domain.modelDto.movimentoPesagem.MovtoPesagemDtoList;
import br.coop.integrada.api.pa.domain.modelDto.movimentoPesagem.MovtoPesagemResponseDto;
import br.coop.integrada.api.pa.domain.service.movimentoPesagem.MovtoPesagemService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/pa/v1/movimento-pesagem")
@SecurityRequirement(name = "javainuseapi")
@Tag(name = "Movimento Pesagem", description = "API Movimento Pesagem.")
public class MovtoPesagemController {
	
	@Autowired
	private MovtoPesagemService movtoPesagemService;
	
	@PostMapping()
	public ResponseEntity<?> save(@RequestBody MovtoPesagemDto movtoPesagem){
		try{
			movtoPesagemService.save(movtoPesagem);
			return ResponseEntity.ok("Cadastrado com sucesso.");
		}catch (Exception e) {
			return new ResponseEntity<>("Error:"+ e.getMessage(), 
            		BAD_REQUEST);
		}		
	}
	
	@PostMapping("/integration/save-all")
	public ResponseEntity<List<MovtoPesagemResponseDto>> saveAll(@RequestBody MovtoPesagemDtoList movtoPesagemDtoList){
		List<MovtoPesagemResponseDto> response = new ArrayList<MovtoPesagemResponseDto>();
		
		for(MovtoPesagemDto movtoPesagemDto : movtoPesagemDtoList.getMovtoPesagem()) {
			try{
				MovtoPesagem movtoPesagem = movtoPesagemService.save(movtoPesagemDto);
				response.add(MovtoPesagemResponseDto.construir(movtoPesagem, true, "Salvo com sucesso!"));
			}catch (Exception e) {
				response.add(MovtoPesagemResponseDto.construir(movtoPesagemDto, false, "Falha ao salvar!", e));
			}
		}
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/id-movimento={idMovtoPesagem}")
	public ResponseEntity<MovtoPesagem> findByIdMovtoPesagem(@PathVariable(name="idMovtoPesagem") String idMovtoPesagem){
		MovtoPesagem movtoPesagem = movtoPesagemService.findByIdMovtoPesagem(idMovtoPesagem);	
		return ResponseEntity.ok(movtoPesagem);
	}
	
	@GetMapping("/numero-pesagem={nroDocPesagem}")
	public ResponseEntity<Page<MovtoPesagem>> findByNroDocPesagem(
			@PathVariable(name="nroDocPesagem") Integer nroDocPesagem,
			@PageableDefault(page = 0, size = 5, sort = "dtMovto", direction = Sort.Direction.DESC) Pageable pageable){
		Page<MovtoPesagem> movtoPesagem = movtoPesagemService.findByNroDocPesagem(nroDocPesagem, pageable);	
		return ResponseEntity.ok(movtoPesagem);
	}
	
	@GetMapping("/numero-movimento={nroMovto}")
	public ResponseEntity<Page<MovtoPesagem>> findByNroMovto(
			@PathVariable(name="nroMovto") Integer nroMovto,
			@PageableDefault(page = 0, size = 5, sort = "dtMovto", direction = Sort.Direction.DESC) Pageable pageable){
		Page<MovtoPesagem> movtoPesagem = movtoPesagemService.findByNroMovto(nroMovto, pageable);	
		return ResponseEntity.ok(movtoPesagem);
	}
	
	@GetMapping("/numero-documento={nroDocto}")
	public ResponseEntity<Page<MovtoPesagem>> findByNroDocto(
			@PathVariable(name="nroDocto") String nroDocto,
			@PageableDefault(page = 0, size = 5, sort = "dtMovto", direction = Sort.Direction.DESC) Pageable pageable){
		Page<MovtoPesagem> movtoPesagem = movtoPesagemService.findByNroDocto(nroDocto, pageable);	
		return ResponseEntity.ok(movtoPesagem);
	}
	
	@GetMapping("codigo-emitente={codEmitente}")
	public ResponseEntity<Page<MovtoPesagem>> findByCodEmitente(
			@PathVariable(name="codEmitente") Integer codEmitente,
			@PageableDefault(page = 0, size = 5, sort = "dtMovto", direction = Sort.Direction.DESC) Pageable pageable){
		Page<MovtoPesagem> movtoPesagem = movtoPesagemService.findByCodEmitente(codEmitente, pageable);	
		return ResponseEntity.ok(movtoPesagem);
	}
	
	@GetMapping("/numero-re={nrRe}")
	public ResponseEntity<Page<MovtoPesagem>> findByNrRe(
			@PathVariable(name="nrRe") Integer nrRe,
			@PageableDefault(page = 0, size = 5, sort = "dtMovto", direction = Sort.Direction.DESC) Pageable pageable){
		Page<MovtoPesagem> movtoPesagem = movtoPesagemService.findByNrRe(nrRe, pageable);	
		return ResponseEntity.ok(movtoPesagem);
	}
}
