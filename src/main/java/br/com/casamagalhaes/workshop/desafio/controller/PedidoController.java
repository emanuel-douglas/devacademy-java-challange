package br.com.casamagalhaes.workshop.desafio.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.UnsupportedMediaType;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import br.com.casamagalhaes.workshop.desafio.enums.StatusPedido;
import br.com.casamagalhaes.workshop.desafio.model.Pedido;
import br.com.casamagalhaes.workshop.desafio.service.PedidoService;

@RestController
@RequestMapping(path = "/api/v1/pedidos",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoController {

    @Autowired
    private PedidoService service;

    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY) 
    public Map unsupport(Exception ex) {
        return Collections.singletonMap("mensagem", ex.getMessage());
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND) 
    public void notFound() {}    

    @GetMapping({"/", ""})
    public List<Pedido> listarTodos() {
        return service.listarTodos();
    }

    @PutMapping("/{id}/status/{novoStatus}")
    public Pedido updateStatus(@PathVariable Long id, @PathVariable StatusPedido novoStatus) {
        return service.updateStatus(id, novoStatus);
    }

    @GetMapping("/{id}")
    public Pedido getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping({"/", ""})
    @ResponseStatus(code = HttpStatus.CREATED)
    public Pedido salvar(@Valid @RequestBody Pedido pedido) {
        return service.save(pedido);
    }    

    @PutMapping("/{id}")
    public Pedido atualiza(@PathVariable Long id, @RequestBody Pedido pedido) {
        return service.update(id, pedido);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        service.remove(id);
    }
}
