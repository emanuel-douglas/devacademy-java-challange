package br.com.casamagalhaes.workshop.desafio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.casamagalhaes.workshop.desafio.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    @Query("SELECT p FROM Pedido p where p.nomeCliente like ?1%")
    List<Pedido> findByCliente(String nomeCliente);

    @Query("SELECT p FROM Pedido p where p.endereco like ?1%")
    List<Pedido> findByEndereco(String endereco);

    @Query("SELECT p FROM Pedido p where p.telefone like ?1%")
    List<Pedido> findByTelefone(String telefone);

}
