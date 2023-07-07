package br.coop.integrada.api.pa.domain.modelDto.nfRemessa;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.model.nfRemessa.NfRemessa;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;

@Data
public class NfRemessaResponseDto extends AbstractResponseDto {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long nrDocto;
	private String serieDocto;
	
	
	public static NfRemessaResponseDto construir(NfRemessa nfRemessa, Boolean integrated, String message) {
		var objDto = new NfRemessaResponseDto();
        BeanUtils.copyProperties(nfRemessa, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
	}

	public static NfRemessaResponseDto construir(NfRemessa nfRemessaDto, Boolean integrated, String message, ConstraintViolationException exception) {

		var objDto = new NfRemessaResponseDto();
        BeanUtils.copyProperties(nfRemessaDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        if(exception instanceof ConstraintViolationException) {
            for (ConstraintViolation<?> constraint : exception.getConstraintViolations()) {
                FieldErrorItem fieldErrorItem = FieldErrorItem.construir(
                        ((PathImpl) constraint.getPropertyPath()).getLeafNode().getName(),
                        constraint.getMessage()
                );

                objDto.getFieldErrors().add(fieldErrorItem);
            }
        }

        return objDto;
		
	}

	public static NfRemessaResponseDto construir(NfRemessa nfRemessaDto, boolean integrated, String message, Exception exception) {
		var objDto = new NfRemessaResponseDto();
        BeanUtils.copyProperties(nfRemessaDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
	}

	public static NfRemessaResponseDto construir(NfRemessa nfRemessaDto, boolean integrated, String message, DataIntegrityViolationException exception) {
		var objDto = new NfRemessaResponseDto();
        BeanUtils.copyProperties(nfRemessaDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
	}
	
}
