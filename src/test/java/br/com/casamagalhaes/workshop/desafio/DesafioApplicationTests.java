package br.com.casamagalhaes.workshop.desafio;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import br.com.casamagalhaes.workshop.desafio.enums.StatusPedido;
import br.com.casamagalhaes.workshop.desafio.model.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
class DesafioApplicationTests {

	@Value("${server.port}")
	private int porta;

	private RequestSpecification requisicao;

	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	private void preperarRequisicao(){
	   requisicao = new RequestSpecBuilder()
		  .setBasePath("/api/v1/pedidos")
		  .setPort(porta)
		  .setAccept(ContentType.JSON)
		  .setContentType(ContentType.JSON)
		  .log(LogDetail.ALL)
		  .build();
	}


	@Test
	public void deveriaReceberMensagemDeOk(){
		given()
		 .spec(requisicao)
	   .expect()   
		 .statusCode(HttpStatus.SC_OK)  
	   .when()  
		 .get();
	}

	@Test
	public void deveriaCriarUmPedido() throws JsonProcessingException {
	   Pedido pedidoCadastrado =
	   given()
		 .spec(requisicao)
		 .body(objectMapper.writeValueAsString(dadoUmPedido()))
	   .when()  
		 .post()
	   .then()
		 .statusCode(HttpStatus.SC_CREATED)
	   .extract()
		 .as(Pedido.class);    
	   
	   assertNotNull(pedidoCadastrado, "produto não foi cadastrado");    
	   assertNotNull(pedidoCadastrado.getId(), "id do produto não gerado");       
	}

	@Test
	public void naoDeveriaGravarPedidoSemProdutos() throws JsonProcessingException {
	  given()
		.spec(requisicao)
		.body(objectMapper.writeValueAsString(dadoUmPedidoSemProdutos()))
	  .when()  
		.post()
	  .then()
		.statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);    
	}

	@Test
	public void naoDeveriaGravarPedidoComProdutoIncompleto() throws JsonProcessingException {
	  given()
		.spec(requisicao)
		.body(objectMapper.writeValueAsString(dadoUmPedidoProdutoIncompleto()))
	  .when()  
		.post()
	  .then()
		.statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);    
	}	

	@Test
	public void deveriaAlterarUmProduto() throws JsonProcessingException {
	  Pedido pedidoCadastrado =
	   given()
		 .spec(requisicao)
		 .body(objectMapper.writeValueAsString(dadoUmPedido()))
	   .when()  
		 .post()
	   .then()
		 .statusCode(HttpStatus.SC_CREATED)
	   .extract()
		 .as(Pedido.class);    
	   
	   assertNotNull(pedidoCadastrado, "pedido não foi cadastrado");    
	   assertNotNull(pedidoCadastrado.getId(), "id do pedido não gerado");

	   pedidoCadastrado.setNomeCliente("produto alterado");

	   Pedido produtoaAlterado =
	   given()
		 .spec(requisicao)
		 .body(objectMapper.writeValueAsString(pedidoCadastrado))
	   .when()
		 .put("/{id}", pedidoCadastrado.getId())
	   .then() 
		 .statusCode(HttpStatus.SC_OK)
	   .extract()
		 .as(Pedido.class);
		 
	   assertEquals(pedidoCadastrado.getId(), produtoaAlterado.getId(),
		 "cliente não foi alterado");  
	}

	@Test
	public void deveriaExcluirUmPedido() throws JsonProcessingException {

	  Pedido pedidoCadastrado =
	   given()
		 .spec(requisicao)
		 .body(objectMapper.writeValueAsString(dadoUmPedido()))
	   .when()  
		 .post()
	   .then()
		 .statusCode(HttpStatus.SC_CREATED)
	   .extract()
		 .as(Pedido.class);    
	   
	   assertNotNull(pedidoCadastrado, "pedido não foi cadastrado");    
	   assertNotNull(pedidoCadastrado.getId(), "id do pedido não gerado");

	   given()
		 .spec(requisicao)
	   .when()
		 .delete("/{id}", pedidoCadastrado.getId())
	   .then() 
		 .statusCode(HttpStatus.SC_NO_CONTENT);

	   given()
		 .spec(requisicao)
		 .body(objectMapper.writeValueAsString(pedidoCadastrado))
	   .when()
		 .get("/{id}", pedidoCadastrado.getId())
	   .then() 
		 .statusCode(HttpStatus.SC_NOT_FOUND);

	}

	private Pedido dadoUmPedidoSemProdutos(){
		Pedido p = new Pedido();
		p.setEndereco("25 DE JANEIRO");
		p.setNomeCliente("JOAO DA SILVA");
		p.setTelefone("(99) 9 9999-9999");
		p.setStatus(StatusPedido.PENDENTE);
		p.setTaxa(10);
	    return p;
	}

	private Pedido dadoUmPedido(){
		Pedido p = new Pedido();
		p.setEndereco("25 DE JANEIRO");
		p.setNomeCliente("JOAO DA SILVA");
		p.setTelefone("(99) 9 9999-9999");
		p.setStatus(StatusPedido.PENDENTE);
		p.setTaxa(10);

		p.getItens().add(new ItemPedido("COCA COLA", 10, 10));
	    return p;
	}

	private Pedido dadoUmPedidoProdutoIncompleto(){
		Pedido p = new Pedido();
		p.setEndereco("25 DE JANEIRO");
		p.setNomeCliente("JOAO DA SILVA");
		p.setTelefone("(99) 9 9999-9999");
		p.setStatus(StatusPedido.PENDENTE);
		p.setTaxa(10);

		p.getItens().add(new ItemPedido("", 10, 10));
	    return p;
	}	
}
