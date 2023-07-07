package br.coop.integrada.api.pa.domain.modelDto.imovel;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.model.imovel.Imovel;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.BeanUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@Data
public class ImovelResponseDto extends AbstractResponseDto {
    private static final long serialVersionUID = 1L;
	private Long matricula;

    public static ImovelResponseDto construir(Imovel imovel, Boolean integrated, String message) {
        var objDto = new ImovelResponseDto();
        BeanUtils.copyProperties(imovel, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }

    public static ImovelResponseDto construir(ImovelDto imovelDto, Boolean integrated, String message, Exception exception) {
        var objDto = new ImovelResponseDto();
        BeanUtils.copyProperties(imovelDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }

    public static ImovelResponseDto construir(ImovelDto imovelDto, Boolean integrated, String message, ConstraintViolationException exception) {
        var objDto = new ImovelResponseDto();
        BeanUtils.copyProperties(imovelDto, objDto);

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
}
