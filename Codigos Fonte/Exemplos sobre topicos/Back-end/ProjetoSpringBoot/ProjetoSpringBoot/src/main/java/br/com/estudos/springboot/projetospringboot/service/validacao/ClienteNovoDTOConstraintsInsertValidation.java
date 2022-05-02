package br.com.estudos.springboot.projetospringboot.service.validacao;

import br.com.estudos.springboot.projetospringboot.domain.dto.ClienteNovoDTO;
import br.com.estudos.springboot.projetospringboot.domain.enums.TipoCliente;
import br.com.estudos.springboot.projetospringboot.resource.exceptions.FieldMensage;
import br.com.estudos.springboot.projetospringboot.ropository.ClienteRepository;
import br.com.estudos.springboot.projetospringboot.service.validacao.utils.ValidacaoCpfOuCnpj;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class ClienteNovoDTOConstraintsInsertValidation implements ConstraintValidator<ClienteNovoDTOInsertValidation, ClienteNovoDTO> {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void initialize(ClienteNovoDTOInsertValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ClienteNovoDTO clienteNovoDTO, ConstraintValidatorContext constraintValidatorContext) {
        List<FieldMensage> errors = new ArrayList<>();

        if(clienteNovoDTO.getTipo() == null) errors.add(new FieldMensage("tipo", "O tipo nao pode ser nulo"));
        else if(clienteNovoDTO.getTipo().equals(TipoCliente.PESSOA_JURIDICA.getId()) && ValidacaoCpfOuCnpj.isNotValidCnpj(clienteNovoDTO.getCpfOuCnpj()))
            errors.add(new FieldMensage("cpfOuCnpj", "Cnpj Invalido"));
        else if(clienteNovoDTO.getTipo().equals(TipoCliente.PESSOA_FISICA.getId()) && ValidacaoCpfOuCnpj.isNotValidCpf(clienteNovoDTO.getCpfOuCnpj()))
            errors.add(new FieldMensage("cpfOuCnpj", "Cpf Invalido"));

        if(clienteRepository.findByEmail(clienteNovoDTO.getEmail()) != null)
            errors.add(new FieldMensage("email", "O email fornecido ja esta cadastrado"));

        for(FieldMensage msg : errors){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(msg.getMensage()).
                addPropertyNode(msg.getFieldName()).addConstraintViolation();
        }
        return errors.isEmpty();
    }
}
