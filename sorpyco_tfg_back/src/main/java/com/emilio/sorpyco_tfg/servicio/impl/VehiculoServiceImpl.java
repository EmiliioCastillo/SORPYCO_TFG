package com.emilio.sorpyco_tfg.servicio.impl;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emilio.sorpyco_tfg.dto.VehiculoDTO;
import com.emilio.sorpyco_tfg.entidades.Vehiculo;
import com.emilio.sorpyco_tfg.enums.TipoVehiculo;
import com.emilio.sorpyco_tfg.repositorio.VehiculoRepository;
import com.emilio.sorpyco_tfg.servicio.VehiculoService;

@Service
@Transactional
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoServiceImpl(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    @Override
    public void registrar(VehiculoDTO datos) {

        validarDatos(datos);

        if (vehiculoRepository.findByPatente(
                datos.patente().trim()).isPresent()) {
            throw new IllegalArgumentException(
                "Ya existe un vehículo con esa patente.");
        }

        Vehiculo vehiculo = new Vehiculo();

        vehiculo.setPatente(datos.patente().trim().toUpperCase());
        vehiculo.setTipoVehiculo(datos.tipoVehiculo());
        vehiculo.setCapacidad(datos.capacidad());
        vehiculo.setDisponibilidad(datos.disponibilidad());

        vehiculoRepository.save(vehiculo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoDTO> listarTodos() {

        return vehiculoRepository.findAll()
            .stream()
            .map(VehiculoDTO::desdeEntidad)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculoDTO buscarPorId(Long id) {

        return VehiculoDTO.desdeEntidad(obtenerVehiculo(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoDTO> listarDisponibles() {

        return vehiculoRepository.findByDisponibilidadTrue()
            .stream()
            .map(VehiculoDTO::desdeEntidad)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoDTO> listarPorTipo(TipoVehiculo tipo) {

        if (tipo == null) {
            throw new IllegalArgumentException(
                "Debe indicar el tipo de vehículo.");
        }

        return vehiculoRepository.findByTipoVehiculo(tipo)
            .stream()
            .map(VehiculoDTO::desdeEntidad)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoDTO> listarDisponiblesPorTipo(
            TipoVehiculo tipo) {

        if (tipo == null) {
            throw new IllegalArgumentException(
                "Debe indicar el tipo de vehículo.");
        }

        return vehiculoRepository
            .findByDisponibilidadTrueAndTipoVehiculo(tipo)
            .stream()
            .map(VehiculoDTO::desdeEntidad)
            .toList();
    }

    @Override
    public void actualizar(Long id, VehiculoDTO datos) {

        validarDatos(datos);

        Vehiculo vehiculo = obtenerVehiculo(id);

        vehiculoRepository.findByPatente(
                datos.patente().trim())
            .ifPresent(otro -> {
                if (!otro.getId().equals(id)) {
                    throw new IllegalArgumentException(
                        "La patente ya pertenece a otro vehículo.");
                }
            });

        vehiculo.setPatente(datos.patente().trim().toUpperCase());
        vehiculo.setTipoVehiculo(datos.tipoVehiculo());
        vehiculo.setCapacidad(datos.capacidad());
        vehiculo.setDisponibilidad(datos.disponibilidad());

        vehiculoRepository.save(vehiculo);
    }

    @Override
    public void cambiarDisponibilidad(
            Long id, boolean disponibilidad) {

        Vehiculo vehiculo = obtenerVehiculo(id);

        vehiculo.setDisponibilidad(disponibilidad);

        vehiculoRepository.save(vehiculo);
    }

    @Override
    public void eliminar(Long id) {

        Vehiculo vehiculo = obtenerVehiculo(id);

        vehiculoRepository.delete(vehiculo);
    }

    private Vehiculo obtenerVehiculo(Long id) {

        if (id == null) {
            throw new IllegalArgumentException(
                "El identificador del vehículo es obligatorio.");
        }

        return vehiculoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró el vehículo con ID: " + id));
    }

    private void validarDatos(VehiculoDTO datos) {

        if (datos == null) {
            throw new IllegalArgumentException(
                "Los datos del vehículo son obligatorios.");
        }

        if (datos.patente() == null || datos.patente().isBlank()) {
            throw new IllegalArgumentException(
                "La patente es obligatoria.");
        }

        if (datos.tipoVehiculo() == null) {
            throw new IllegalArgumentException(
                "Debe indicar el tipo de vehículo.");
        }

        if (datos.capacidad() == null
                || datos.capacidad().signum() <= 0) {
            throw new IllegalArgumentException(
                "La capacidad debe ser mayor que cero.");
        }
    }
}
