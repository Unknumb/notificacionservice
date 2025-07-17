package com.perfulandia.notificacionservice.repository;

import com.perfulandia.notificacionservice.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
}