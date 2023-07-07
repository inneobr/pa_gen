package br.coop.integrada.api.pa.domain.modelDto.produtor;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;

@Data
public class ProdutorResponseDto extends AbstractResponseDto {
    
    private String codProdutor;
    
    public static ProdutorResponseDto construir(
            ProdutorDto produtorDto,
            Boolean integrated,
            String message,
            ConstraintViolationException exception
        ) {
        ProdutorResponseDto responseDto = new ProdutorResponseDto();
        responseDto.setCodProdutor(produtorDto.getCodProdutor());
        responseDto.setIntegrated(integrated);
        responseDto.setMessage(message);
        
        if(exception != null) {
            responseDto.setException(exception.getMessage());

            for (ConstraintViolation<?> constraint : exception.getConstraintViolations()) {
                String campo = ((PathImpl) constraint.getPropertyPath()).getLeafNode().getName();
                String mensagem = constraint.getMessage();
                FieldErrorItem fieldErrorItem = FieldErrorItem.construir(campo, mensagem);
                responseDto.getFieldErrors().add(fieldErrorItem);
            }
        }
        return responseDto;
    }
    
    public static ProdutorResponseDto construir(ProdutorDto produtorDto, Boolean integrated, String message, String exception) {
        var responseDto = new ProdutorResponseDto();
        responseDto.setCodProdutor(produtorDto.getCodProdutor());
        responseDto.setIntegrated(integrated);
        responseDto.setMessage(message);
        responseDto.setException(exception);

        return responseDto;
    }

    public static ProdutorResponseDto construir(ProdutorDto produtorDto, Boolean integrated, String message) {
        var responseDto = new ProdutorResponseDto();
        responseDto.setCodProdutor(produtorDto.getCodProdutor());
        responseDto.setIntegrated(integrated);
        responseDto.setMessage(message);

        return responseDto;
    }

}
