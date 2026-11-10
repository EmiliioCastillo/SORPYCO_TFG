package com.emilio.sorpyco_tfg.servicio;

import java.util.List;

import com.emilio.sorpyco_tfg.dto.VehiculoDTO;
import com.emilio.sorpyco_tfg.enums.TipoVehiculo;

public interface VehiculoService {

    void registrar(VehiculoDTO vehiculo);

    List<VehiculoDTO> listarTodos();

    VehiculoDTO buscarPorId(Long id);

    List<VehiculoDTO> listarDisponibles();

    List<VehiculoDTO> listarPorTipo(TipoVehiculo tipo);

    List<VehiculoDTO> listarDisponiblesPorTipo(TipoVehiculo tipo);

    void actualizar(Long id, VehiculoDTO datos);

    void cambiarDisponibilidad(Long id, boolean disponibilidad);

    void eliminar(Long id);
}
