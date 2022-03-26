package br.com.estudos.springboot.projetospringboot.service;

import br.com.estudos.springboot.projetospringboot.domain.Cliente;
import br.com.estudos.springboot.projetospringboot.domain.dto.ClienteDTO;
import br.com.estudos.springboot.projetospringboot.ropository.ClienteRepository;
import br.com.estudos.springboot.projetospringboot.service.exceptions.DataIntegrityException;
import br.com.estudos.springboot.projetospringboot.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public Cliente buscar(Integer id){
        Optional<Cliente> cliente = repository.findById(id);

        return cliente.orElseThrow(() -> new ObjectNotFoundException(
                "objeto com id " + id + " nao encontrado em " + this.getClass().getSimpleName()
            )
        );
    }

    public Cliente inserir(Cliente cliente) {
        return repository.save(cliente);
    }

    public Cliente alterar(Cliente cliente) {
        buscar(cliente.getId());
        return alterarDadosRecebidos(cliente);
        // return repository.save(cliente);
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

    public Cliente fromDto(ClienteDTO clienteDto){
        return new Cliente(clienteDto.getId(), clienteDto.getNome(), clienteDto.getEmail(), null, null);
    }

    private Cliente alterarDadosRecebidos(Cliente novoCliente) {

        Class<?> classe = novoCliente.getClass();

        Cliente categoriaAnterior = buscar(novoCliente.getId());

        for (Field x : classe.getDeclaredFields()) {
            x.setAccessible(true);
            try {
                if (x.get(novoCliente) == null) {
                    x.set(novoCliente, x.get(categoriaAnterior));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return repository.save(novoCliente);
    }
}
