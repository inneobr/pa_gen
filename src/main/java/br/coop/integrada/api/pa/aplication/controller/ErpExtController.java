package br.coop.integrada.api.pa.aplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.coop.integrada.api.pa.aplication.exceptions.ObjectDefaultException;
import br.coop.integrada.api.pa.domain.modelDto.externo.NotaFiscalEntradaResponseDto;
import br.coop.integrada.api.pa.domain.modelDto.externo.NumeroReDto;
import br.coop.integrada.api.pa.domain.service.externo.ErpExtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/externo")
@Tag(name = "Externo", description = "Serviços externos")
public class ErpExtController {

    @Autowired
    private ErpExtService erpExtService;

    @PutMapping("/buscar-numero-re")
    public ResponseEntity<List<NumeroReDto>> buscarNumeroRe(@RequestBody List<NumeroReDto> numeroReDtos) {
        List<NumeroReDto> repsonse = erpExtService.buscarNumeroRe(numeroReDtos);

        if(CollectionUtils.isEmpty(repsonse)) {
            throw new ObjectDefaultException("O retorno está vazio!");
        }

        return ResponseEntity.ok(repsonse);
    }

    @Operation(description = "Esse endpoint depende dos cadastros realizados no cadastro de integrações.")
    @GetMapping("/validar-nota-fiscal-entrada/{numeroNotaFiscal}/{serie}/{natureza}/{codigoProdutor}")
    public ResponseEntity<NotaFiscalEntradaResponseDto> buscarNumeroRe(
    		@PathVariable(name="serie") String serie,
    		@PathVariable(name="natureza") String natureza,
    		@PathVariable(name="codigoProdutor") String codigoProdutor,
    		@PathVariable(name="numeroNotaFiscal") String numeroNotaFiscal
    		) {
        NotaFiscalEntradaResponseDto responseDto = erpExtService.validarNotaFiscalEntrada(numeroNotaFiscal, serie, natureza, codigoProdutor);
        return ResponseEntity.ok(responseDto);
    }
}
