package br.com.estudos.springboot.projetospringboot.service.validacao;

import br.com.estudos.springboot.projetospringboot.domain.Cliente;
import br.com.estudos.springboot.projetospringboot.domain.dto.ClienteDTO;
import br.com.estudos.springboot.projetospringboot.resource.exceptions.FieldMensage;
import br.com.estudos.springboot.projetospringboot.ropository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClienteDTOConstraintsUpdateValidation implements ConstraintValidator<ClienteDTOUpdateValidation, ClienteDTO> {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired //permite pagar o id passado na url
    private HttpServletRequest request;

    @Override
    public void initialize(ClienteDTOUpdateValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ClienteDTO clienteNovoDTO, ConstraintValidatorContext constraintValidatorContext) {

        // armazena os atributos da requisicao
        Map<String, String> atributosRequisicao = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        // pega o atributo id armazenado no Map atributosRequisicao
        Integer uriId = Integer.parseInt(atributosRequisicao.get("id"));

        List<FieldMensage> errors = new ArrayList<>();

        //retona a entidade cliente a partir do email.
        Cliente entidade = clienteRepository.findByEmail(clienteNovoDTO.getEmail());
        if(entidade != null && !entidade.getId().equals(uriId))
            errors.add(new FieldMensage("email", "O email fornecido pertence a outro cliente"));

        for(FieldMensage msg : errors){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(msg.getMensage()).
                addPropertyNode(msg.getFieldName()).addConstraintViolation();
        }
        return errors.isEmpty();
    }
}
