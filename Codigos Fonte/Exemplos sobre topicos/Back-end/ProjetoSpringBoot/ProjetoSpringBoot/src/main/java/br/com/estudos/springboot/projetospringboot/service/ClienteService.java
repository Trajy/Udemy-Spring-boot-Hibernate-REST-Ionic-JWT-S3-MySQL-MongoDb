package br.com.estudos.springboot.projetospringboot.service;

import br.com.estudos.springboot.projetospringboot.domain.Cidade;
import br.com.estudos.springboot.projetospringboot.domain.Cliente;
import br.com.estudos.springboot.projetospringboot.domain.Endereco;
import br.com.estudos.springboot.projetospringboot.domain.dto.ClienteDTO;
import br.com.estudos.springboot.projetospringboot.domain.dto.ClienteNovoDTO;
import br.com.estudos.springboot.projetospringboot.domain.enums.Perfil;
import br.com.estudos.springboot.projetospringboot.domain.enums.TipoCliente;
import br.com.estudos.springboot.projetospringboot.ropository.ClienteRepository;
import br.com.estudos.springboot.projetospringboot.ropository.EnderecoRepository;
import br.com.estudos.springboot.projetospringboot.security.UserSpringSecurity;
import br.com.estudos.springboot.projetospringboot.service.exceptions.AuthorizationException;
import br.com.estudos.springboot.projetospringboot.service.exceptions.DataIntegrityException;
import br.com.estudos.springboot.projetospringboot.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public Cliente buscar(Integer id){
        Optional<Cliente> cliente = repository.findById(id);

        // recupera o usuario atual logano no sistema
        UserSpringSecurity usuario = UserService.authenticated();

        if(usuario == null || !usuario.hasRole(Perfil.ADM) && !id.equals(usuario.getId())){
            throw new AuthorizationException("Acesso negado voce nao e este cliente");
        }

        return cliente.orElseThrow(() -> new ObjectNotFoundException(
                "objeto com id " + id + " nao encontrado em " + this.getClass().getSimpleName()
            )
        );
    }

    @Transactional
    public Cliente inserir(Cliente cliente) {
        cliente.setId(null);
        enderecoRepository.saveAll(cliente.getEnderecos());
        return repository.save(cliente);
    }

    public Cliente alterar(Cliente cliente) {
        return repository.save(alterarDadosRecebidos(cliente));
    }

    public void deletar(Integer id) {
        buscar(id);
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e){
            throw new DataIntegrityException("Nao foi possivel excluir cliente");
        }
    }

    public List<ClienteDTO> buscarTodas() {

        List<Cliente> clientes = repository.findAll();
        List<ClienteDTO> clienteDTOs = clientes.stream().map(cliente ->
                new ClienteDTO(cliente)).collect(Collectors.toList());

        return clienteDTOs;
    }

    public Page<ClienteDTO> buscarPaginado(Integer numeroPagina, Integer linhasPorPagina, String ordenarPor, String direcaoOrdencao){

        PageRequest requisicaoPagina = PageRequest.of(numeroPagina, linhasPorPagina, Sort.Direction.valueOf(direcaoOrdencao), ordenarPor);
        Page<Cliente> paginaClientes = repository.findAll(requisicaoPagina);
        Page<ClienteDTO> paginaClientesDto = paginaClientes.map(cliente -> new ClienteDTO(cliente));
        return paginaClientesDto;
    }

    public Cliente fromDTO(ClienteNovoDTO clienteNovoDTO) {
        Cliente cliente = new Cliente(null,
            clienteNovoDTO.getNome(),
            clienteNovoDTO.getEmail(),
            clienteNovoDTO.getCpfOuCnpj(),
            TipoCliente.toEnum(clienteNovoDTO.getTipo()),
            encoder.encode(clienteNovoDTO.getSenha())
        );

        Cidade cidade = new Cidade(clienteNovoDTO.getCidadeId(), null, null);

        Endereco endereco = new Endereco(null,
            clienteNovoDTO.getLogradouro(),
            clienteNovoDTO.getNumero(),
            clienteNovoDTO.getComplemento(),
            clienteNovoDTO.getBairro(),
            clienteNovoDTO.getCep(),
            cliente,
            cidade
        );

        cliente.getEnderecos().add(endereco);
        cliente.getTelefones().add(clienteNovoDTO.getTelefone());

        return cliente;
    }

    public Cliente fromDTO(ClienteDTO clienteDto){
        return new Cliente(clienteDto.getId(), clienteDto.getNome(), clienteDto.getEmail(), null, null, null);
    }

    private Cliente alterarDadosRecebidos(Cliente novoCliente) {

        Class<?> classe = novoCliente.getClass();

        Cliente clienteAnterior = buscar(novoCliente.getId());

        for (Field x : classe.getDeclaredFields()) {
            x.setAccessible(true);
            try {
                if (x.get(novoCliente) == null) {
                    x.set(novoCliente, x.get(clienteAnterior));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return novoCliente;
    }
}
