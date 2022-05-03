package br.com.estudos.springboot.projetospringboot;

import br.com.estudos.springboot.projetospringboot.domain.*;
import br.com.estudos.springboot.projetospringboot.domain.enums.EstadoPagamento;
import br.com.estudos.springboot.projetospringboot.domain.enums.TipoCliente;
import br.com.estudos.springboot.projetospringboot.ropository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Arrays;

@SpringBootApplication
public class ProjetoSpringBootApplication implements CommandLineRunner {

	private static ApplicationContext appContext;

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
		var cat3 = new Categoria(null, "Cama, Mesa e Banho");
		var cat4 = new Categoria(null, "Jardinagem");
		var cat5 = new Categoria(null, "Alimentos");
		var cat6 = new Categoria(null, "Bebidas");
		var cat7 = new Categoria(null, "Eletronicos");


		var prod1 = new Produto(null, "Computador", 2000.00);
		var prod2 = new Produto(null, "Impressora", 800.00);
		var prod3 = new Produto(null, "Mouse", 80.00);
		var prod4 = new Produto(null, "Mesa de Escritorio", 300.00);
		var prod5 = new Produto(null, "Toalha", 50.00);
		var prod6 = new Produto(null, "Colcha", 200.00);
		var prod7 = new Produto(null, "TV true color", 1200.00);
		var prod8 = new Produto(null, "Rocadeira", 800.00);
		var prod9 = new Produto(null, "Abajour", 100.00);
		var prod10 = new Produto (null, "Pendente", 180.00);
		var prod11 = new Produto(null, "Shampoo", 90.00);

		cat1.getProdutos().addAll(Arrays.asList(prod1, prod2, prod3));
		cat2.getProdutos().addAll(Arrays.asList(prod3, prod4));
		cat3.getProdutos().addAll(Arrays.asList(prod5, prod6));
		cat4.getProdutos().addAll(Arrays.asList(prod1, prod2, prod3, prod7));
		cat5.getProdutos().addAll(Arrays.asList(prod8));
		cat6.getProdutos().addAll(Arrays.asList(prod9, prod10));
		cat7.getProdutos().addAll(Arrays.asList(prod11));

		prod1.getCategorias().addAll(Arrays.asList(cat1, cat4));
		prod2.getCategorias().addAll(Arrays.asList(cat1, cat2, cat4));
		prod3.getCategorias().addAll(Arrays.asList(cat1, cat4));
		prod4.getCategorias().addAll(Arrays.asList(cat2));
		prod5.getCategorias().addAll(Arrays.asList(cat3));
		prod6.getCategorias().addAll(Arrays.asList(cat3));
		prod7.getCategorias().addAll(Arrays.asList(cat4));
		prod8.getCategorias().addAll(Arrays.asList(cat5));
		prod9.getCategorias().addAll(Arrays.asList(cat6));
		prod10.getCategorias().addAll(Arrays.asList(cat6));
		prod11.getCategorias().addAll(Arrays.asList(cat7));

		categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5, cat6, cat7));
		produtoRepository.saveAll(Arrays.asList(prod1, prod2, prod3, prod4, prod5, prod6, prod7, prod8, prod9, prod10, prod11));

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

		Pagamento pgto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pgto1);
		Pagamento pgto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);
		ped2.setPagamento(pgto2);

		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));

		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pgto1, pgto2));

		ItemPedido ip1 = new ItemPedido(ped1, prod1, 200d, 1, 2000.00);
		ItemPedido ip2 = new ItemPedido(ped1, prod3, 0d, 2, 80.00);
		ItemPedido ip3 = new ItemPedido(ped2, prod2, 100d, 1, 800.00);

		ped1.getItens().addAll(Arrays.asList(ip1, ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));

		prod1.getItens().addAll(Arrays.asList(ip1));
		prod2.getItens().addAll(Arrays.asList(ip3));
		prod3.getItens().addAll(Arrays.asList(ip2));

		itemPedidoRepository.saveAll(Arrays.asList(ip1, ip2, ip3));
	}
}
