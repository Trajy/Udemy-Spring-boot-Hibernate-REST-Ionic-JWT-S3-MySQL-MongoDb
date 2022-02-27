package br.com.estudos.springboot.projetospringboot.service;

import br.com.estudos.springboot.projetospringboot.domain.Categoria;
import br.com.estudos.springboot.projetospringboot.domain.dto.CategoriaDTO;
import br.com.estudos.springboot.projetospringboot.ropository.CategoriaRepository;
import br.com.estudos.springboot.projetospringboot.service.exceptions.DataIntegrityException;
import br.com.estudos.springboot.projetospringboot.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public Categoria buscar(Integer id){
        Optional<Categoria> categoria = repository.findById(id);

        return categoria.orElseThrow(() -> new ObjectNotFoundException(
                "objeto com id " + id + " nao encontrado em " + this.getClass().getSimpleName()
            )
        );
    }

    public Categoria inserir(Categoria categoria) {
        return repository.save(categoria);
    }

    public Categoria alterar(Categoria categoria) {
        buscar(categoria.getId());
        return repository.save(categoria);
    }

    public void deletar(Integer id) {
        buscar(id);
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e){
            throw new DataIntegrityException("Nao foi possivel excluir categoria");
        }
    }

    public List<CategoriaDTO> buscarTodas() {

        List<Categoria> categorias = repository.findAll();
        List<CategoriaDTO> categoriaDTOs = categorias.stream().map(categoria ->
                new CategoriaDTO(categoria)).collect(Collectors.toList());

        return categoriaDTOs;
    }

    public Page<CategoriaDTO> buscarPaginado(Integer numeroPagina, Integer linhasPorPagina, String ordenarPor, String direcaoOrdencao){

        PageRequest requisicaoPagina = PageRequest.of(numeroPagina, linhasPorPagina, Sort.Direction.valueOf(direcaoOrdencao), ordenarPor);
        Page<Categoria> paginaCategorias = repository.findAll(requisicaoPagina);
        Page<CategoriaDTO> paginaCategoriasDto = paginaCategorias.map(categoria -> new CategoriaDTO(categoria));
        return paginaCategoriasDto;
    }
}
