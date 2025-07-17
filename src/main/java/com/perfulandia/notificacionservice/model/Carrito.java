package com.perfulandia.notificacionservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Carrito {
    private long id;
    private long usuarioId;
    private List<Long> productoIds;
    private Double total;
}
