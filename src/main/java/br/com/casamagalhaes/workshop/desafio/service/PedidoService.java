package br.com.casamagalhaes.workshop.desafio.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import br.com.casamagalhaes.workshop.desafio.repository.PedidoRepository;
import br.com.casamagalhaes.workshop.desafio.enums.StatusPedido;
import br.com.casamagalhaes.workshop.desafio.model.ItemPedido;
import br.com.casamagalhaes.workshop.desafio.model.Pedido;

@Service
public class PedidoService {
    
    private final String MSG_ERRO_STATUS = "status não pode ser alterado....";

    @Autowired
    private PedidoRepository repository;

    public Pedido findById(Long id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Pedido> findByCliente(String nomeCliente) {
        return repository.findByCliente(nomeCliente);
    }    

    public List<Pedido> findByEndereco(String endereco) {
        return repository.findByEndereco(endereco);
    }    

    public List<Pedido> findByTelefone(String telefone) {
        return repository.findByTelefone(telefone);
    }      

    public List<Pedido> listarTodos() {
        return repository.findAll();
    }
    
    public Pedido save(Pedido pedido) {
        //Verifica pedido de venda sem produtos
        if(pedido.getItens().isEmpty()) {
            throw new UnsupportedOperationException("Pedido não possui nenhum produto.");
        } else {
            //Verifica produtos sem quantidade, valor ou nome
            for(ItemPedido ip : pedido.getItens()) {
                if(ip.getQuantidade() == 0 || ip.getPrecoUnitario() == 0 || 
                ip.getDescricao().trim().equals("")) {
                    throw new UnsupportedOperationException("Existem produtos com informações incompletas.");
                }
            }
            pedido.calcularValorTotalProdutos();
            pedido.calcularValorTotal();
            return repository.save(pedido);
        }
    }

    public Pedido update(Long id, Pedido pedido) {
        if (repository.existsById(id)) {
            if (id.equals(pedido.getId())) {
                //Verifica se o status foi alterado por este endpoint
                Optional<Pedido> pedidoEncontrado = repository.findById(id);
                if(pedidoEncontrado.get().getStatus() == pedido.getStatus()) {
                    //Verifica pedido de venda sem produtos
                    if(pedido.getItens().isEmpty()) {
                        throw new UnsupportedOperationException("Pedido não possui nenhum produto.");
                    } else {
                        //Verifica produtos sem quantidade, valor ou nome
                        for(ItemPedido ip : pedido.getItens()) {
                            if(ip.getQuantidade() == 0 || ip.getPrecoUnitario() == 0 || 
                            ip.getDescricao().trim().equals("")) {
                                throw new UnsupportedOperationException("Existem produtos com informações incompletas.");
                            }
                        }
                        pedido.calcularValorTotalProdutos();
                        pedido.calcularValorTotal();
                        return repository.saveAndFlush(pedido);
                    }                    
                } else {
                    throw new UnsupportedOperationException("O status não pode ser alterado por este endpoint.");
                }
            } else
                throw new UnsupportedOperationException("Id informado diferente do Pedido.");
        } else
            throw new EntityNotFoundException("Pedido id: " + pedido.getId());
    } 
    
    public Pedido updateStatus(Long id, StatusPedido novoStatus) {
        if (repository.existsById(id)) {
            Optional<Pedido> pedidoEncontrado = repository.findById(id);
            StatusPedido statusPedido = pedidoEncontrado.get().getStatus();

            if(novoStatus.equals(StatusPedido.CANCELADO)) {
                if(!statusPedido.equals(StatusPedido.EM_ROTA) &&
                !statusPedido.equals(StatusPedido.ENTREGUE) &&
                !statusPedido.equals(StatusPedido.CANCELADO)) {
                    pedidoEncontrado.get().setStatus(novoStatus);
                    return update(id, pedidoEncontrado.get());
                } else {
                    throw new UnsupportedOperationException(MSG_ERRO_STATUS);
                }
            } else if (novoStatus.equals(StatusPedido.EM_ROTA)) {
                if(!statusPedido.equals(StatusPedido.PRONTO)) {
                    pedidoEncontrado.get().setStatus(novoStatus);
                    return update(id, pedidoEncontrado.get());
                } else {
                    throw new UnsupportedOperationException(MSG_ERRO_STATUS);
                }
            } else if (novoStatus.equals(StatusPedido.ENTREGUE)) {
                if(!statusPedido.equals(StatusPedido.EM_ROTA)) {
                    pedidoEncontrado.get().setStatus(novoStatus);
                    return update(id, pedidoEncontrado.get());
                } else {
                    throw new UnsupportedOperationException(MSG_ERRO_STATUS);
                }
            } else {
                pedidoEncontrado.get().setStatus(novoStatus);
                return update(id, pedidoEncontrado.get());
            }
        } else {
            throw new EntityNotFoundException("Pedido não encontrado.");
        }
    }

    public void remove(Long id) {
        repository.deleteById(id);
    }

}
