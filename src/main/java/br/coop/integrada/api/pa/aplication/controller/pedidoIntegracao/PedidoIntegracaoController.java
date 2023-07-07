package br.coop.integrada.api.pa.aplication.controller.pedidoIntegracao;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.coop.integrada.api.pa.domain.model.pedidoIntegracao.Integracao;
import br.coop.integrada.api.pa.domain.service.pedidoIntegracao.PedidoIntegracaoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/pedido-integracao")
@Tag(name = "pedido-integracao")
public class PedidoIntegracaoController {
	
	@Autowired
    private PedidoIntegracaoService integracaoService;
		
	@PostMapping("/gerar-pedido-integracao")
    public ResponseEntity<Integracao> gerarPedidoIntegracao(@RequestBody @Valid Integracao integracao){
        
		Integracao integracaoOutput = integracaoService.gerarPedidoIntegracao(integracao);
        
        return ResponseEntity.ok(integracaoOutput);
    }
	
	
}
