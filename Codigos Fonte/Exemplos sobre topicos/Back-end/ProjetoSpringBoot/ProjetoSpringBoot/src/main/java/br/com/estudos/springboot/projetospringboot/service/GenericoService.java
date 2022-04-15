package br.com.estudos.springboot.projetospringboot.service;

import br.com.estudos.springboot.projetospringboot.domain.comum.EntidadeComum;
import br.com.estudos.springboot.projetospringboot.ropository.GenericoRepository;
import br.com.estudos.springboot.projetospringboot.ropository.configuracao.ConfiguracaoRepository;
import br.com.estudos.springboot.projetospringboot.service.exceptions.DataIntegrityException;
import br.com.estudos.springboot.projetospringboot.service.exceptions.ObjectNotFoundException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GenericoService<E extends EntidadeComum> {

    private GenericoRepository<E, Integer> repository;

    private GenericoService(GenericoServiceBuilder<E> builder) {
        this.repository = builder.repository;
    }

    public E buscar(Integer id) {
        Optional<E> entidade = repository.findById(id);

        return entidade.orElseThrow(() -> new ObjectNotFoundException(
                "objeto com id " + id + " nao encontrado em " + this.getClass().getSimpleName()
            )
        );
    }

    public E inserir(E entidade) {
        return repository.save(entidade);
    }

    public E alterar(E entidade) {
        buscar(entidade.getId());
        return repository.save(alterarDadosRecebidos(entidade));
    }

    public void deletar(Integer id) {
        buscar(id);
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Nao foi possivel excluir entidade");
        }
    }

    public <D> List<D> buscarTodas(Class<D> classeDto) {

        List<E> entidades = repository.findAll();
        List<D> entidadeDtos = entidades.stream().map(entidade -> {
            try {
                return classeDto.getDeclaredConstructor(entidade.getClass()).newInstance(entidade);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());

        return entidadeDtos;
    }

    public <D> Page<D> buscarPaginado(Integer numeroPagina, Integer linhasPorPagina, String ordenarPor, String direcaoOrdencao, Class<D> classeDto) {

        PageRequest requisicaoPagina = PageRequest.of(numeroPagina, linhasPorPagina, Sort.Direction.valueOf(direcaoOrdencao), ordenarPor);
        Page<E> paginas = (Page<E>) repository.findAll(requisicaoPagina);
        Page<D> paginasDto = paginas.map(entidade -> {
            try {
                return classeDto.getDeclaredConstructor(entidade.getClass()).newInstance(entidade);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        });
        return paginasDto;
    }

    public <D> E toEntidade(D entidadeDto) {

        Class<E> clazz = null;
        E entidade = null;

        try {
            entidade = clazz.getDeclaredConstructor().newInstance();

            for (Field x : entidadeDto.getClass().getDeclaredFields()) {
                x.setAccessible(true);
                try {
                    x.set(entidade, x.get(entidadeDto));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return entidade;
    }

    private E alterarDadosRecebidos(E novaEntidade) {

        E categoriaAnterior = buscar(novaEntidade.getId());

        for (Field x : novaEntidade.getClass().getDeclaredFields()) {
            x.setAccessible(true);
            try {
                if (x.get(novaEntidade) == null) {
                    x.set(novaEntidade, x.get(categoriaAnterior));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return novaEntidade;
    }

    public static class GenericoServiceBuilder<E extends EntidadeComum> {

        public GenericoRepository<E, Integer> repository;

        public GenericoServiceBuilder(Class<E> clazz) {
            repository = new AnnotationConfigApplicationContext(ConfiguracaoRepository.class).
                getBean(GenericoRepository.class, clazz, Integer.class);
        }

        public GenericoService<E> build() {
            return new GenericoService<>(this);
        }
    }

}