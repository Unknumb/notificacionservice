package com.perfulandia.notificacionservice.controller;

import com.perfulandia.notificacionservice.model.Carrito;
import com.perfulandia.notificacionservice.model.Notificacion;
import com.perfulandia.notificacionservice.service.NotificacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificacionController.class)
class NotificacionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private NotificacionService service;

    @MockBean
    private RestTemplate restTemplate;

    private Notificacion sample;

    @BeforeEach
    void setUp() {
        sample = new Notificacion();
        sample.setId(1L);
        sample.setUsuarioId(10L);
        sample.setCarritoId(20L);
        sample.setMensaje("Hola Mundo");
    }

    @Test
    void GET_api_notificaciones_deberiaRetornarLista() throws Exception {
        when(service.obtenerTodas()).thenReturn(Collections.singletonList(sample));

        mvc.perform(get("/api/notificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].mensaje").value("Hola Mundo"));

        verify(service, times(1)).obtenerTodas();
    }

    @Test
    void GET_api_notificaciones_id_deberiaRetornarEntidad() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(sample);

        mvc.perform(get("/api/notificaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value(10))
                .andExpect(jsonPath("$.carritoId").value(20));

        verify(service, times(1)).obtenerPorId(1L);
    }

    @Test
    void POST_api_notificaciones_deberiaCrearNotificacion() throws Exception {
        when(service.guardar(any(Notificacion.class))).thenReturn(sample);

        String body = "{"
                + "\"usuarioId\":10,"
                + "\"carritoId\":20,"
                + "\"mensaje\":\"Hola Mundo\""
                + "}";

        mvc.perform(post("/api/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
        verify(service, times(1)).guardar(captor.capture());
        Notificacion passed = captor.getValue();
        assert passed.getUsuarioId() == 10L;
        assert passed.getCarritoId() == 20L;
    }

    @Test
    void DELETE_api_notificaciones_id_deberiaEliminar() throws Exception {
        mvc.perform(delete("/api/notificaciones/1"))
                .andExpect(status().isOk());

        verify(service, times(1)).eliminar(1L);
    }

    @Test
    void GET_api_notificaciones_detallada_deberiaRetornarMapa() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(sample);

        Carrito fakeCarrito = new Carrito();
        fakeCarrito.setId(20L);
        fakeCarrito.setUsuarioId(10L);

        // Ahora stubeamos la URL con carritoId = 20
        when(restTemplate.getForObject(
                eq("http://localhost:8083/api/carritos/20"),
                eq(Carrito.class)))
                .thenReturn(fakeCarrito);

        mvc.perform(get("/api/notificaciones/detallada/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notificacion.id").value(1))
                .andExpect(jsonPath("$.carrito.id").value(20));

        verify(service, times(1)).obtenerPorId(1L);
        verify(restTemplate, times(1))
                .getForObject("http://localhost:8083/api/carritos/20", Carrito.class);
    }
}
