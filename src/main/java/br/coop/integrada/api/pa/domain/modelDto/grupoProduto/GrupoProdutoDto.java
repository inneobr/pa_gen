package br.coop.integrada.api.pa.domain.modelDto.grupoProduto;

import br.coop.integrada.api.pa.domain.enums.grupo.produto.EntradaReEnum;
import br.coop.integrada.api.pa.domain.enums.grupo.produto.ReferenciaEnum;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProduto;
import br.coop.integrada.api.pa.domain.model.produto.GrupoProdutoGmo;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoGmoDto;
import br.coop.integrada.api.pa.domain.modelDto.produto.TipoProdutoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class GrupoProdutoDto{

	private Long id;
	
	@NotBlank(message = "Campo {fmCodigo} obrigatório")
	private String fmCodigo;
	
	@NotBlank(message = "Campo {descricao} obrigatório")
	private String descricao;
	private ReferenciaEnum referencia;
	private ReferenciaEnum referFixado;
	private boolean logTransgenico;
	private boolean logRetirada;
	private boolean transferencia;
	private boolean desmembramento;
	private Integer kgSc;
	private Integer phMinimo;
	private BigDecimal percImpureza;
	private boolean semente;
	private boolean impureza;
	private boolean phEntrada;
	private boolean umidade;
	private boolean chuvAvar;
	private boolean lote;
	private boolean tbm;
	private boolean nrOrdCampo;
	private boolean fnt;
	private boolean densidade;
	private boolean nrLaudo;
	private boolean tipo;
	private TipoProdutoDto tipoProduto;
	private String codCult;
	private boolean qualiProduto;
	private boolean logBandinha;
	private boolean bebida;
	private boolean cafeEscolha;
	
	private boolean permiteDesmembraTercCoop;
	private boolean cafeCoco;
	private boolean cafeBeneficiado;
	
	private boolean cata;
	private String fmCodigoSub;
	private String itSubCoop;
	private String itSubTer;
	private String itTaxaCoop;
	private String itTaxaTer;
	
	private EntradaReEnum entradaRe;
	private String condChuvAvarSinal;
	private BigDecimal condChuvAvarValor;
	
	private List<GrupoProdutoGmoDto> listGrupoProdutoGmo;
	
	public String getCodDesc() {
		return fmCodigo + " - " + descricao;
	}
	
	public static GrupoProdutoDto construir(GrupoProduto grupoProduto, List<GrupoProdutoGmo> grupoProdutoGmoList) {
		GrupoProdutoDto grupoProdutoDto = new GrupoProdutoDto();
		BeanUtils.copyProperties(grupoProduto, grupoProdutoDto);
		
		if(grupoProduto.getTipoProduto() != null) {
			TipoProdutoDto tipoProdutoDto = TipoProdutoDto.construir(grupoProduto.getTipoProduto());
			grupoProdutoDto.setTipoProduto(tipoProdutoDto);
		}
		
		
		List<GrupoProdutoGmoDto> grupoProdutoGmoDtoList = new ArrayList<>();
		if(grupoProdutoGmoList != null) {
			for (GrupoProdutoGmo grupoProdutoGmo : grupoProdutoGmoList) {			
				GrupoProdutoGmoDto grupoProdutoGmoDto = new GrupoProdutoGmoDto();
				BeanUtils.copyProperties(grupoProdutoGmo, grupoProdutoGmoDto);
				
				if(grupoProdutoGmo.getTipoGmo() != null) {
					TipoGmoDto tipoGmoDto = new TipoGmoDto();
					BeanUtils.copyProperties(grupoProdutoGmo.getTipoGmo(), tipoGmoDto);
					grupoProdutoGmoDto.setTipoGmo(tipoGmoDto);
				}
				
				grupoProdutoGmoDtoList.add(grupoProdutoGmoDto);
			}
		}
		grupoProdutoDto.setListGrupoProdutoGmo(grupoProdutoGmoDtoList);
		
		return grupoProdutoDto;
	}
}
