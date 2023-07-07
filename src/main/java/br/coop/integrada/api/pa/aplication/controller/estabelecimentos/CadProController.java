package br.coop.integrada.api.pa.aplication.controller.estabelecimentos;

import java.util.List;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import br.coop.integrada.api.pa.domain.model.cadpro.CadPro;
import br.coop.integrada.api.pa.domain.service.estabelecimento.CadProCliente;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController 
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/cadpro")
@Tag(name = "CADPRO", description = "Valida cadpro do produtor.")
public class CadProController {
    
    @Autowired
    private CadProCliente cadproService;  
    
    @GetMapping("/{cadpro}/{uf}")
    public List<CadPro> consultar(@PathVariable(name="cadpro") String cadpro, @PathVariable(name="uf") String uf){
        return cadproService.consultarCadpro(cadpro, uf);
    }

}
