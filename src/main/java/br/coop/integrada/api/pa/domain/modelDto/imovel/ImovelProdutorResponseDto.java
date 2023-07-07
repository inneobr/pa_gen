package br.coop.integrada.api.pa.domain.modelDto.imovel;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.BeanUtils;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.model.imovel.ImovelProdutor;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;

@Data
public class ImovelProdutorResponseDto extends AbstractResponseDto {
    private static final long serialVersionUID = 1L;
    private String idUnico;
    private Long matricula;
    private String codProdutor;

    public static ImovelProdutorResponseDto construir(ImovelProdutor imovelProdutor, Boolean integrated, String message) {
        var objDto = new ImovelProdutorResponseDto();
        BeanUtils.copyProperties(imovelProdutor, objDto);
        objDto.setMatricula(imovelProdutor.getImovel().getMatricula());
        objDto.setIntegrated(integrated);
        objDto.setMessage(message);

        return objDto;
    }

    public static ImovelProdutorResponseDto construir(ImovelProdutorDto imovelProdutorDto, Boolean integrated, String message, Exception exception) {
        var objDto = new ImovelProdutorResponseDto();
        BeanUtils.copyProperties(imovelProdutorDto, objDto);

        objDto.setIntegrated(integrated);
        objDto.setMessage(message);
        objDto.setException(exception.getMessage());

        return objDto;
    }

    public static ImovelProdutorResponseDto construir(ImovelProdutorDto imovelProdutorDto, Boolean integrated, String message, ConstraintViolationException exception) {
        var objDto = new ImovelProdutorResponseDto();
        BeanUtils.copyProperties(imovelProdutorDto, objDto);

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
