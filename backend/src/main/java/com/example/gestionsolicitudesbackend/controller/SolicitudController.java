
package com.example.gestionsolicitudesbackend.controller;

import com.example.gestionsolicitudesbackend.model.Solicitud;
import com.example.gestionsolicitudesbackend.repository.SolicitudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudRepository repository;

    public SolicitudController(SolicitudRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Solicitud> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> obtener(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Solicitud crear(@RequestBody Solicitud solicitud) {
        solicitud.setId(null);
        return repository.save(solicitud);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Solicitud> actualizar(
            @PathVariable Long id,
            @RequestBody Solicitud datos
    ) {
        return repository.findById(id).map(solicitud -> {
            solicitud.setTitulo(datos.getTitulo());
            solicitud.setDescripcion(datos.getDescripcion());
            solicitud.setCategoria(datos.getCategoria());
            solicitud.setServicioRelacionado(datos.getServicioRelacionado());
            solicitud.setEstado(datos.getEstado());

            return ResponseEntity.ok(repository.save(solicitud));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
