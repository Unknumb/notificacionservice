package com.perfulandia.notificacionservice.controller;

import com.perfulandia.notificacionservice.model.Carrito;
import com.perfulandia.notificacionservice.model.Notificacion;
import com.perfulandia.notificacionservice.service.NotificacionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final RestTemplate restTemplate;

    public NotificacionController(NotificacionService notificacionService, RestTemplate restTemplate) {
        this.notificacionService = notificacionService;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public List<Notificacion> obtenerTodas() {
        return notificacionService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public Notificacion obtenerPorId(@PathVariable Long id) {
        return notificacionService.obtenerPorId(id);
    }

    @PostMapping
    public Notificacion guardar(@RequestBody Notificacion notificacion) {
        return notificacionService.guardar(notificacion);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        notificacionService.eliminar(id);
    }

    @GetMapping("/detallada/{id}")
    public Map<String, Object> obtenerNotificacionConCarrito(@PathVariable Long id) {
        Notificacion notificacion = notificacionService.obtenerPorId(id);
        Carrito carrito = restTemplate.getForObject(
                "http://localhost:8083/api/carritos/" + notificacion.getCarritoId(),
                Carrito.class
        );
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("notificacion", notificacion);
        respuesta.put("carrito", carrito);
        return respuesta;
    }
}
