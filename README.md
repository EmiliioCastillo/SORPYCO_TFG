# SORPYCO_TFG
# Demostración del prototipo SORPYCO

## 1. Presentación e introducción

Para complementar el desarrollo de este trabajo, voy a realizar una demostración de SORPYCO, un prototipo de plataforma orientado a la planificación y optimización de operaciones de distribución logística.

El objetivo principal del sistema es facilitar la organización de los pedidos, la asignación de vehículos y la planificación de recorridos, teniendo en cuenta diferentes restricciones operativas.

Para su desarrollo se tomó como referencia el relevamiento realizado sobre el funcionamiento de una distribuidora, utilizando ese contexto como base para identificar necesidades y definir las funcionalidades del prototipo.

Es importante aclarar que se trata de una solución de carácter genérico, que no está destinada exclusivamente a una empresa en particular.

## 2. Pantalla principal

Al ingresar al sistema encontramos el panel principal de planificación, desde donde podemos comenzar a organizar las operaciones de distribución.

La interfaz permite seleccionar una fecha para la cual queremos generar una planificación. A partir de esa selección, el sistema consulta los pedidos correspondientes y procesa la información necesaria para organizar las entregas.

## 3. Generación de la planificación

Para realizar la demostración voy a utilizar un conjunto de datos ficticios, correspondientes a diferentes pedidos y sucursales.

Una vez seleccionada la fecha, ejecutamos la generación de la planificación.

En esta etapa interviene el algoritmo Clarke & Wright, utilizado para optimizar la distribución de los recorridos, considerando principalmente las ubicaciones de entrega y las capacidades de los vehículos disponibles.

El sistema procesa los datos y genera una propuesta de distribución, asignando los pedidos a los vehículos y estableciendo una secuencia de paradas.

## 4. Indicadores y resultados

Una vez finalizado el procesamiento, podemos observar diferentes indicadores que permiten analizar el resultado obtenido.

Entre ellos encontramos la distancia inicial y la distancia optimizada, lo que permite visualizar la reducción del recorrido calculado.

También se muestra el porcentaje de ahorro de distancia, la utilización de la capacidad de los vehículos, el tiempo estimado, la cantidad de vehículos utilizados y los pedidos que no pudieron ser asignados.

Estos indicadores permiten obtener una visión general del resultado de la planificación y facilitan su análisis desde una perspectiva operativa.

## 5. Visualización de recorridos

Además de los indicadores, el sistema permite visualizar geográficamente las ubicaciones de entrega mediante un mapa interactivo.

En él podemos identificar las diferentes sucursales, observar el orden de las paradas y consultar información asociada a cada ubicación.

Esto permite complementar los resultados numéricos con una representación visual de la distribución, facilitando la interpretación de los recorridos generados.

## 6. Cierre

En conclusión, SORPYCO busca demostrar cómo la aplicación de técnicas de optimización puede contribuir a mejorar la planificación de operaciones de distribución logística.

A través de este prototipo se integran diferentes elementos del proceso, como la gestión de pedidos, la disponibilidad de vehículos, la asignación de entregas, la optimización de recorridos y la visualización de resultados.

De esta manera, se presenta una alternativa tecnológica que permite abordar problemas habituales de planificación logística y explorar las posibilidades de mejorar la organización de las operaciones de distribución.
