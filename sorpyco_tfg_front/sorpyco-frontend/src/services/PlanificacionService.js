import axios from "axios";

const api = axios.create({
  baseURL:"http://localhost:8080/api",
});

export const generarPlanificacion = async (fecha) => {
  const response = await api.post(
    `/planificaciones/generar`,
    null,
    { params: { fecha } }
  );

  return response.data;
};