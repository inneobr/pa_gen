package br.coop.integrada.api.pa.domain.modelDto.movimentoDiario;

import java.io.Serializable;
import java.util.Date;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.model.movimentoDiario.MovimentoDiario;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;

@Data
public class MovimentoDiarioResponseDto extends AbstractResponseDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String idUnico;
	private String codEstabel;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dtMovto;
	private Boolean movtoFech;

	public static MovimentoDiarioResponseDto construir(MovimentoDiario movimentoDiario, Boolean integrated, String message) {
        var objDto = new MovimentoDiarioResponseDto();
        BeanUtils.copyProperties(movimentoDiario, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }

    public static MovimentoDiarioResponseDto construir(MovimentoDiarioDto movimentoDiarioDto, Boolean integrated, String message) {
        var objDto = new MovimentoDiarioResponseDto();
        BeanUtils.copyProperties(movimentoDiarioDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }

    public static MovimentoDiarioResponseDto construir(MovimentoDiario movimentoDiario, Boolean integrated, String message, ConstraintViolationException exception) {
        var objDto = new MovimentoDiarioResponseDto();
        BeanUtils.copyProperties(movimentoDiario, objDto);

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

    public static MovimentoDiarioResponseDto construir(MovimentoDiarioDto movimentoDiarioDto, Boolean integrated, String message, Exception exception) {
        var objDto = new MovimentoDiarioResponseDto();
        BeanUtils.copyProperties(movimentoDiarioDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }
    
    public static MovimentoDiarioResponseDto construir(MovimentoDiarioDto movimentoDiarioDto, Boolean integrated, String message, String exception) {
        var objDto = new MovimentoDiarioResponseDto();
        BeanUtils.copyProperties(movimentoDiarioDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception);

        return objDto;
    }
    
    public static MovimentoDiarioResponseDto construir(String message, Exception exception) {
        var objDto = new MovimentoDiarioResponseDto();
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }


}
