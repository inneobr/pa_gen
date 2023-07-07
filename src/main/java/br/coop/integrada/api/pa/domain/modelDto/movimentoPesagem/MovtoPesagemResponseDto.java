package br.coop.integrada.api.pa.domain.modelDto.movimentoPesagem;

import org.springframework.beans.BeanUtils;

import br.coop.integrada.api.pa.domain.model.movtoPesagem.MovtoPesagem;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;

@Data
public class MovtoPesagemResponseDto extends AbstractResponseDto {
	private static final long serialVersionUID = 1L;

	private Integer nroMovto;
	private String codEstabel;
	private Integer safra;
	private Integer nroDocPesagem;
	private String idMovtoPesagem;
	
	public static MovtoPesagemResponseDto construir(MovtoPesagem movtoPesagem, Boolean integrated, String message) {
        var objDto = new MovtoPesagemResponseDto();
        BeanUtils.copyProperties(movtoPesagem, objDto);
        
        if(movtoPesagem.getEstabelecimento() != null) {
        	String codigoEstabelecimento = movtoPesagem.getEstabelecimento().getCodigo();
        	objDto.setCodEstabel(codigoEstabelecimento);
        }

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }

	public static MovtoPesagemResponseDto construir(MovtoPesagemDto movtoPesagemDto, Boolean integrated, String message, Exception exception) {
        var objDto = new MovtoPesagemResponseDto();
        BeanUtils.copyProperties(movtoPesagemDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }
}
