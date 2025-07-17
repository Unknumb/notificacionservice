package com.perfulandia.notificacionservice.service;

import com.perfulandia.notificacionservice.model.Notificacion;
import com.perfulandia.notificacionservice.repository.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificacionServiceTest {

    @Mock
    private NotificacionRepository repo;

    @InjectMocks
    private NotificacionService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtenerTodas_deberiaRetornarListaDeNotificaciones() {
        // dado
        Notificacion n1 = Notificacion.builder().id(1L).usuarioId(5L).mensaje("Hola").build();
        Notificacion n2 = Notificacion.builder().id(2L).usuarioId(6L).mensaje("Chao").build();
        when(repo.findAll()).thenReturn(Arrays.asList(n1, n2));

        // cuando
        List<Notificacion> resultado = service.obtenerTodas();

        // entonces
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(n1) && resultado.contains(n2));
        verify(repo, times(1)).findAll();
    }

    @Test
    void obtenerPorId_cuandoExiste_deberiaRetornarNotificacion() {
        // dado
        Notificacion esperado = Notificacion.builder()
                .id(7L)
                .usuarioId(3L)
                .carritoId(9L)
                .mensaje("Test")
                .build();
        when(repo.findById(7L)).thenReturn(Optional.of(esperado));

        // cuando
        Notificacion resultado = service.obtenerPorId(7L);

        // entonces
        assertNotNull(resultado);
        assertEquals(7L, resultado.getId());
        assertEquals(3L, resultado.getUsuarioId());
        assertEquals(9L, resultado.getCarritoId());
        verify(repo, times(1)).findById(7L);
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaRetornarNull() {
        // dado
        when(repo.findById(99L)).thenReturn(Optional.empty());

        // cuando
        Notificacion resultado = service.obtenerPorId(99L);

        // entonces
        assertNull(resultado);
        verify(repo, times(1)).findById(99L);
    }

    @Test
    void guardar_deberiaPersistirYRetornarEntidad() {
        // dado
        Notificacion input = Notificacion.builder()
                .usuarioId(11L)
                .carritoId(22L)
                .mensaje("Guardar")
                .build();
        Notificacion saved = Notificacion.builder()
                .id(13L)
                .usuarioId(11L)
                .carritoId(22L)
                .mensaje("Guardar")
                .build();
        when(repo.save(input)).thenReturn(saved);

        // cuando
        Notificacion resultado = service.guardar(input);

        // entonces
        assertNotNull(resultado);
        assertEquals(13L, resultado.getId());
        verify(repo, times(1)).save(input);
    }

    @Test
    void eliminar_deberiaInvocarDeleteById() {
        // cuando
        service.eliminar(17L);

        // entonces
        verify(repo, times(1)).deleteById(17L);
    }
}

