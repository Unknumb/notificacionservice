package com.perfulandia.productservice.controller;

import com.perfulandia.productservice.assembler.ProductoModelAssembler;
import com.perfulandia.productservice.model.Carrito;
import com.perfulandia.productservice.model.Producto;
import com.perfulandia.productservice.model.Usuario;
import com.perfulandia.productservice.service.ProductoService;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService servicio;
    private final RestTemplate restTemplate;
    private final ProductoModelAssembler assembler;

    public ProductoController(ProductoService servicio, RestTemplate restTemplate, ProductoModelAssembler assembler) {
        this.servicio = servicio;
        this.restTemplate = restTemplate;
        this.assembler = assembler;
    }

    // Listar con HATEOAS
    @GetMapping
    public CollectionModel<EntityModel<Producto>> listar() {
        List<EntityModel<Producto>> productos = servicio.listar().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(productos,
            linkTo(methodOn(ProductoController.class).listar()).withSelfRel());
    }

    // Guardar producto
    @PostMapping
    public Producto guardar(@RequestBody Producto producto) {
        return servicio.guardar(producto);
    }

    // Buscar por ID con HATEOAS
    @GetMapping("/{id}")
    public EntityModel<Producto> buscar(@PathVariable long id) {
        Producto producto = servicio.bucarPorId(id);
        if (producto == null) {
            throw new RuntimeException("Producto no encontrado");
        }
        return assembler.toModel(producto);
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener usuario de otro MS
    @GetMapping("/usuario/{id}")
    public Usuario obtenerUsuario(@PathVariable long id) {
        return restTemplate.getForObject("http://localhost:8081/api/usuarios/" + id, Usuario.class);
    }

    // Obtener carrito de otro MS
    @GetMapping("/carrito/{id}")
    public Carrito obtenerCarrito(@PathVariable long id) {
        return restTemplate.getForObject("http://localhost:8083/api/carritos/" + id, Carrito.class);
    }
}
