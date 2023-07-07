package br.coop.integrada.api.pa.aplication.controller.dashBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.coop.integrada.api.pa.domain.model.dashBoard.DashBoard;
import br.coop.integrada.api.pa.domain.modelDto.dashBoard.ConsultaPrincipalFilter;
import br.coop.integrada.api.pa.domain.service.dashBoard.DashBoardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("/api/pa/v1/dash-board")
@Tag(name = "dashboard", description = "Busca informações para o DashBoard do Gênesis.")
public class DashBoardController {
	
	@Autowired
	private DashBoardService dashBoardService1;
	
	@PostMapping("principal")
    public DashBoard dashBoadPrincipal(@RequestBody ConsultaPrincipalFilter consultaPrincipalFilter)
	{
        return dashBoardService1.consultaPrincipal(consultaPrincipalFilter);
    }
	
	
}
