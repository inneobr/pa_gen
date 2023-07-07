package br.coop.integrada.api.pa.domain.modelDto.pesagem;

import br.coop.integrada.api.pa.aplication.exceptions.FieldErrorItem;
import br.coop.integrada.api.pa.domain.model.Pesagem;
import br.coop.integrada.api.pa.domain.modelDto.AbstractResponseDto;
import lombok.Data;
import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@Data
public class PesagemResponseDto extends AbstractResponseDto {
    
    private Integer nroDocPesagem;
    private String codEstabelecimento;
    private Integer safra;
    
    public static PesagemResponseDto construir(
            PesagemPostDto pesagemPostDto,
            Boolean integrated,
            String message,
            ConstraintViolationException exception
    ) {
        PesagemResponseDto responseDto = new PesagemResponseDto();
        responseDto.setCodEstabelecimento(pesagemPostDto.getCodEstabelecimento());
        responseDto.setNroDocPesagem(pesagemPostDto.getNroDocPesagem());
        responseDto.setSafra(pesagemPostDto.getSafra());
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

    public static PesagemResponseDto construir(PesagemPostDto pesagemPostDto, Boolean integrated, String message, String exception) {
        var pesagemResponseDto = new PesagemResponseDto();
        pesagemResponseDto.setCodEstabelecimento(pesagemPostDto.getCodEstabelecimento());
        pesagemResponseDto.setNroDocPesagem(pesagemPostDto.getNroDocPesagem());
        pesagemResponseDto.setSafra(pesagemPostDto.getSafra());
        pesagemResponseDto.setIntegrated(integrated);
        pesagemResponseDto.setMessage(message);
        pesagemResponseDto.setException(exception);

        return pesagemResponseDto;
    }

    public static PesagemResponseDto construir(
            PesagemPutDto pesagemPutDto,
            Boolean integrated,
            String message,
            ConstraintViolationException exception
    ) {
        PesagemResponseDto responseDto = new PesagemResponseDto();
        responseDto.setCodEstabelecimento(pesagemPutDto.getCodEstabelecimento());
        responseDto.setNroDocPesagem(pesagemPutDto.getNroDocPesagem());
        responseDto.setSafra(pesagemPutDto.getSafra());
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

    public static PesagemResponseDto construir(PesagemPutDto pesagemPutDto, Boolean integrated, String message, String exception) {
        var pesagemResponseDto = new PesagemResponseDto();
        pesagemResponseDto.setCodEstabelecimento(pesagemPutDto.getCodEstabelecimento());
        pesagemResponseDto.setNroDocPesagem(pesagemPutDto.getNroDocPesagem());
        pesagemResponseDto.setSafra(pesagemPutDto.getSafra());
        pesagemResponseDto.setIntegrated(integrated);
        pesagemResponseDto.setMessage(message);
        pesagemResponseDto.setException(exception);

        return pesagemResponseDto;
    }

    public static PesagemResponseDto construir(Pesagem pesagem, Boolean integrated, String message) {
        var pesagemResponseDto = new PesagemResponseDto();
        pesagemResponseDto.setCodEstabelecimento(pesagem.getCodEstabelecimento());
        pesagemResponseDto.setNroDocPesagem(pesagem.getNroDocPesagem());
        pesagemResponseDto.setSafra(pesagem.getSafra());
        pesagemResponseDto.setIntegrated(integrated);
        pesagemResponseDto.setMessage(message);

        return pesagemResponseDto;
    }
}
