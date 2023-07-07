package br.coop.integrada.api.pa.domain.modelDto.semente.campoSemente;

import lombok.Data;
import java.util.List;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import br.coop.integrada.api.pa.domain.model.semente.produtor.SementeCampo;

@Data
public class CampoDto {
	private Long id;
	private String imovelCodNome;
	private String descricaoClasse;
	private String ordemCampo;
	private String codProduto;
	private String imovel;
	private Integer safra;
	private String codEstab;
	private String fmCodigo;
	private Long codClasse;
	
	private SementeCampo sementeCampo;
	
	public static List<CampoDto> construir(List<SementeCampo> sementeCampoList) {
		List<CampoDto> campoSemente = new ArrayList<>();
		for(SementeCampo item: sementeCampoList) {
			CampoDto campoSementeDto = new CampoDto();
			campoSementeDto.setOrdemCampo(item.getOrdemCampo().toString());
			campoSementeDto.setDescricaoClasse(item.getClasseDescricao());
			campoSementeDto.setCodProduto(item.getProduto().getCodItem());	
			campoSementeDto.setImovelCodNome(null);		
			campoSementeDto.setId(item.getId());
			campoSementeDto.setSafra(item.getSafra());
			campoSementeDto.setCodEstab(item.getCodigoEstabelecimento());
			campoSementeDto.setFmCodigo(item.getGrupoProduto().getFmCodigo());
			campoSementeDto.setCodClasse(item.getClasse().getCodigo());
			campoSementeDto.setSementeCampo(item);
			campoSementeDto.setImovel(item.getMatricula());
			campoSemente.add(campoSementeDto);
		}
		return campoSemente;
	}
	
	public static List<CampoDto> builder(Page<SementeCampo> sementeCampoList) {
		List<CampoDto> campoSemente = new ArrayList<>();
		for(SementeCampo item: sementeCampoList.getContent()) {
			CampoDto campoSementeDto = new CampoDto();
			campoSementeDto.setOrdemCampo(item.getOrdemCampo().toString());
			campoSementeDto.setDescricaoClasse(item.getClasseDescricao());
			campoSementeDto.setCodProduto(item.getProduto().getCodItem());	
			campoSementeDto.setImovelCodNome(null);		
			campoSementeDto.setId(item.getId());
			campoSementeDto.setSafra(item.getSafra());
			campoSementeDto.setCodEstab(item.getCodigoEstabelecimento());
			campoSementeDto.setFmCodigo(item.getGrupoProduto().getFmCodigo());
			campoSementeDto.setCodClasse(item.getClasse().getCodigo());
			campoSementeDto.setSementeCampo(item);
			campoSementeDto.setImovel(item.getMatricula());
			campoSemente.add(campoSementeDto);
		}
		return campoSemente;
	}
	
	
}