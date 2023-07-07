package br.coop.integrada.api.pa.domain.modelDto.movimentoRe;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.model.movimentoRe.MovimentoRe;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;

@Data
public class MovimentoReResponseDto extends AbstractResponseDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String codEstabel;
	private Long nrRe;
	private String idMovRe;

	public static MovimentoReResponseDto construir(MovimentoRe movimentoRe, Boolean integrated, String message) {
        var objDto = new MovimentoReResponseDto();
        BeanUtils.copyProperties(movimentoRe, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }

    public static MovimentoReResponseDto construir(MovimentoReDto movimentoReDto, Boolean integrated, String message) {
        var objDto = new MovimentoReResponseDto();
        BeanUtils.copyProperties(movimentoReDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }

    public static MovimentoReResponseDto construir(MovimentoRe movimentoRe, Boolean integrated, String message, ConstraintViolationException exception) {
        var objDto = new MovimentoReResponseDto();
        BeanUtils.copyProperties(movimentoRe, objDto);

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

    public static MovimentoReResponseDto construir(MovimentoReDto movimentoReDto, Boolean integrated, String message, Exception exception) {
        var objDto = new MovimentoReResponseDto();
        BeanUtils.copyProperties(movimentoReDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }
    
    public static MovimentoReResponseDto construir(MovimentoReDto movimentoReDto, Boolean integrated, String message, String exception) {
        var objDto = new MovimentoReResponseDto();
        BeanUtils.copyProperties(movimentoReDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception);

        return objDto;
    }
    
    public static MovimentoReResponseDto construir(String message, Exception exception) {
        var objDto = new MovimentoReResponseDto();
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }


}
