# __PAGINACAO COM PARAMETROS OPCIONAIS NA REQUISICAO__

Ate o momento foram implementados exemplos nos quias poucos dados sao retornados do banco de dados, porem quando grandes quantidades de dados necessitam ser carregadas, isso deve ser feito de forma controlado, pois retornar todos os dados existentes de uma unica vez, pode sobrecarregar o sistema. Para solucionar este problema os dados retornados serao paginados, deste modo uma quantidade determinada dos dados sera retornada por vez do banco de dados.

#
## Refatoracao adjacente 
antes de implementar a operacao de paginacao, vamos adicionar algumas novos objetos da classe `Categoria` no metodo `run()` da classe principal do Spring Boot, para exemplificar de forma mais clara.

refatoracao do metodo `run()`

```java
package br.com.estudos.springboot.projetospringboot;

import br.com.estudos.springboot.projetospringboot.domain.*;
import br.com.estudos.springboot.projetospringboot.domain.enums.EstadoPagameno;
import br.com.estudos.springboot.projetospringboot.domain.enums.TipoCliente;
import br.com.estudos.springboot.projetospringboot.ropository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.Arrays;

@SpringBootApplication
public class ProjetoSpringBootApplication implements CommandLineRunner {

	@Autowired
	CategoriaRepository categoriaRepository;

	@Autowired
	ProdutoRepository produtoRepository;

	@Autowired
	EstadoRepository estadoRepository;

	@Autowired
	CidadeRepository cidadeRepository;

	@Autowired
	EnderecoRepository enderecoRepository;

	@Autowired
	ClienteRepository clienteRepository;

	@Autowired
	PedidoRepository pedidoRepository;

	@Autowired
	PagamentoRepository pagamentoRepository;

	@Autowired
	ItemPedidoRepository itemPedidoRepository;

	public static void main(String[] args) {
		SpringApplication.run(ProjetoSpringBootApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		var cat1 = new Categoria(null, "Informatica");
		var cat2 = new Categoria(null, "Escritorio");

        // novas categorias inseridas
		var cat3 = new Categoria(null, "Cama, Mesa e Banho");
		var cat4 = new Categoria(null, "Jardinagem");
		var cat5 = new Categoria(null, "Alimentos");
		var cat6 = new Categoria(null, "Bebidas");
		var cat7 = new Categoria(null, "Eletronicos");



		var prod1 = new Produto(null, "Computador", 2000.00);
		var prod2 = new Produto(null, "Impressora", 800.00);
		var prod3 = new Produto(null, "Mouse", 80.00);

		cat1.getProdutos().addAll(Arrays.asList(prod1, prod2, prod3));
		cat2.getProdutos().addAll(Arrays.asList(prod3));

		prod1.getCategorias().addAll(Arrays.asList(cat1));
		prod2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		prod3.getCategorias().addAll(Arrays.asList(cat1));

        // persistencia de novas categorias no banco de dados
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5, cat6, cat7));
		produtoRepository.saveAll(Arrays.asList(prod1, prod2, prod3));

		var est1 = new Estado(null, "Minas Gerais");
		var est2 = new Estado(null, "Sao Paulo");

		var c1 = new Cidade(null, "Uberlandia", est1);
		var c2 = new Cidade(null, "Sao Paulo", est2);
		var c3 = new Cidade(null, "Campinas", est2);

		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2, c3));

		estadoRepository.saveAll(Arrays.asList(est1, est2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));

		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "36378912377", TipoCliente.PESSOA_FISICA);

		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 203", "Jardim", "38220834", cli1, c1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "38777012", cli1, c2);

		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));

		String t1 = "27363323";
		String t2 = "93838393";
		cli1.getTelefones().addAll(Arrays.asList(t1, t2));

		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(e1, e2));

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), e1, cli1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), e2, cli1);

		Pagamento pgto1 = new PagamentoComCartao(null, EstadoPagameno.QUITADO, ped1, 6);
		ped1.setPagamento(pgto1);
		Pagamento pgto2 = new PagamentoComBoleto(null, EstadoPagameno.PEDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);
		ped2.setPagamento(pgto2);

		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));

		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pgto1, pgto2));

		ItemPedido ip1 = new ItemPedido(ped1, prod1, 200d, 1, 2000f);
		ItemPedido ip2 = new ItemPedido(ped1, prod3, 0d, 2, 80f);
		ItemPedido ip3 = new ItemPedido(ped2, prod2, 100d, 1, 800f);

		ped1.getItens().addAll(Arrays.asList(ip1, ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));

		prod1.getItens().addAll(Arrays.asList(ip1));
		prod2.getItens().addAll(Arrays.asList(ip3));
		prod3.getItens().addAll(Arrays.asList(ip2));

		itemPedidoRepository.saveAll(Arrays.asList(ip1, ip2, ip3));
	}
}
```

#
## Implementacao de paginacao no endpoint de `Cateogria`

o metodo `findAll()` possui sobrecarga e pode receber como argumento um objeto do tipo `PageRequest`, este por sua vez recebe os argumentos para realizar a paginacao, como retorno do metodo `findAll()` obtemos um objeto do tipo `Page<T>`.

Implementacao do metodo para paginacao na classe `CategoriaService`.

```java
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

    // novo metodo para paginacao
    public Page<CategoriaDTO> buscarPaginado(Integer numeroPagina, Integer linhasPorPagina, String ordenarPor, String direcaoOrdencao){

        PageRequest requisicaoPagina = PageRequest.of(numeroPagina, linhasPorPagina, Sort.Direction.valueOf(direcaoOrdencao), ordenarPor);
        Page<Categoria> paginaCategorias = repository.findAll(requisicaoPagina);
        Page<CategoriaDTO> paginaCategoriasDto = paginaCategorias.map(categoria -> new CategoriaDTO(categoria));
        return paginaCategoriasDto;
    }
}
```
note que apos obter a paginacao dos objetos do tipo `Categoria` e realizada a conversao para uma pagina que possui objetos do tipo `CategoriaDTO`, para apresentar apenas os dados relevantes a solicitacao. a interface `Page` possui o metodo `map()` assinado, logo o metodo pode ser utilizado diretamente no objeto do tipo `Page` e retorna o tipo correto.

Implementacao do endpoint para acessar categorias paginadas.

```java
package br.com.estudos.springboot.projetospringboot.resource;

import br.com.estudos.springboot.projetospringboot.domain.Categoria;
import br.com.estudos.springboot.projetospringboot.domain.dto.CategoriaDTO;
import br.com.estudos.springboot.projetospringboot.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

    @Autowired
    private CategoriaService service;

    @RequestMapping(method = RequestMethod.GET, value = "/buscar/{id}")
    public ResponseEntity<?> buscar(@PathVariable Integer id){

        Categoria categoria = service.buscar(id);

        return ResponseEntity.ok().body(categoria);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/nova")
    public ResponseEntity<Void> inserir(@RequestBody Categoria categoria){
        categoria = service.inserir(categoria);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().replacePath("categorias/buscar/{id}").build(categoria.getId());
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/alterar/{id}")
    public ResponseEntity<Void> alterar(@RequestBody Categoria categoria, @PathVariable Integer id){
        categoria.setId(id);
        categoria = service.alterar(categoria);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deletar/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/buscar/todas")
    public ResponseEntity<List<CategoriaDTO>> buscarTodas(){
        List<CategoriaDTO> categorias = service.buscarTodas();
        return ResponseEntity.ok().body(categorias);
    }

    // novo endpoint para acessar categorias paginadas
    @RequestMapping(method = RequestMethod.GET, value = "/buscar")
    public ResponseEntity<Page<CategoriaDTO>> buscarPaginado(
            @RequestParam(value = "pg", defaultValue = "0") Integer numeroPagina,
            @RequestParam(value = "qtditenspg", defaultValue = "2") Integer linhasPorPagina,
            @RequestParam(value = "ordenar", defaultValue = "nome") String ordenarPor,
            @RequestParam(value = "dir", defaultValue = "ASC") String direcaoOrdencao
    ){
        Page<CategoriaDTO> paginaCategoriaDto = service.buscarPaginado(numeroPagina, linhasPorPagina, ordenarPor, direcaoOrdencao);
        return ResponseEntity.ok().body(paginaCategoriaDto);
    }

}
```

ate o momento os argumentos necessarios para os metodos mapeados foram passados como caminho da url, para este metodo os argumentos necessarios serao passados atraves de parametros na url, para isto e necessario utilizar a _annotation_ `@RequestParam` nos parametros do metodo. caso nenhum valor seja forneceido como parametro na url o valor padrao e definido para o metodo.

#
## Acessando endpoint de categorias paginado

acesso sem passar nenhum parametro na url.

<p align="center">
    <img src="img/postman-endpoint-categorias-paginado-acesso-sem-passar-parametros.png
"><br>
    figura 1 - endpoint paginado categorias acesso sem passar parametros.
</p>

acesso passando apenas o numero da pagina.

<p align="center">
    <img src="img/postman-endpoint-categorias-paginado-passando-parametro-numpagina.png
"><br>
    figura 2 - endpoint paginado categorias acesso passando numero da pagina.
</p>

os demais parametros podem ser informados na url conforme o desejado.


