package br.com.casamagalhaes.workshop.desafio.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import br.com.casamagalhaes.workshop.desafio.enums.StatusPedido;

@Entity
public class Pedido implements Serializable {
    
    private Long id;
    private String nomeCliente;
    private String endereco;
    private String telefone;
    private double valorTotalProdutos;
    private double taxa;
    private double valorTotal;
    private StatusPedido status;

    private List<ItemPedido> itens = new ArrayList<>();

    public Pedido() {
        this.status = StatusPedido.PENDENTE;
    }

    public Pedido(String nomeCliente, String endereco, String telefone, double taxa) {
        this.status = StatusPedido.PENDENTE;
        this.nomeCliente = nomeCliente;
        this.endereco = endereco;
        this.telefone = telefone;
        this.taxa = taxa;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="pedido")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public double getValorTotalProdutos() {
        return valorTotalProdutos;
    }

    public void setValorTotalProdutos(double valorTotalProdutos) {
        this.valorTotalProdutos = valorTotalProdutos;
    }

    public double getTaxa() {
        return taxa;
    }

    public void setTaxa(double taxa) {
        this.taxa = taxa;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    @OneToMany(cascade = CascadeType.ALL)
    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public void calcularValorTotalProdutos() {
        this.valorTotalProdutos = 0;

        for(ItemPedido ip : itens) {
            this.valorTotalProdutos += ip.getPrecoUnitario()*ip.getQuantidade();
        }
    }

    public void calcularValorTotal() {
        this.valorTotal = 0;
        this.valorTotal += valorTotalProdutos + taxa;
    }
}
