package br.com.casamagalhaes.workshop.desafio;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.casamagalhaes.workshop.desafio.enums.StatusPedido;
import br.com.casamagalhaes.workshop.desafio.model.ItemPedido;
import br.com.casamagalhaes.workshop.desafio.model.Pedido;
import br.com.casamagalhaes.workshop.desafio.repository.PedidoRepository;
import br.com.casamagalhaes.workshop.desafio.service.PedidoService;

@Configuration
public class PopularBanco {
 
    @Bean
    CommandLineRunner initDB(PedidoService repo) {
        return args -> {
            Pedido p;
            for(int i = 0; i < 10; i++) {
                p = new Pedido();
                p.setEndereco("endereco " + i);
                p.setNomeCliente("nomeCliente " + i);
                p.setTelefone("telefone " + i);
                p.setStatus(StatusPedido.PENDENTE);
                p.setTaxa(10);

                p.getItens().add(new ItemPedido("COCA COLA", 10, 10));
                repo.save(p);
            }
        };
    }

}
