# 🎬 CinemaPro - Sistema de Gestión de Cine y Videoclub

Este proyecto es una aplicación web completa desarrollada con **Spring Boot** para la gestión de inventario, ventas y alquiler de películas. Está diseñado para ofrecer una experiencia administrativa robusta con analítica en tiempo real.

## 🚀 Funcionalidades Principales

### Para el Administrador:
* **Dashboard Dinámico:** Visualización de KPIs (Ganancias Totales, Película más/menos vendida, Alerta de Stock Agotado).
* **Gráficos en Tiempo Real:** Integración con Chart.js para visualizar el rendimiento de ventas por película.
* **Gestión de Inventario (CRUD):** Control total sobre el catálogo (Agregar, Editar, Eliminar).
* **Conversión de Moneda:** Cálculo automático de ganancias en Dólares (USD) basado en una tasa de cambio configurable en `application.properties`.

### Para el Cliente:
* **Catálogo Interactivo:** Navegación fluida por las películas disponibles.
* **Lógica de Compra y Alquiler:** Sistema que valida automáticamente el stock antes de procesar una transacción.
* **UX Inteligente:** Botones de compra que se desactivan y cambian de estado cuando un producto se agota.

## 🛠️ Tecnologías Utilizadas
* **Backend:** Java 17, Spring Boot, Spring Data JPA.
* **Frontend:** Thymeleaf, Bootstrap 5, JavaScript (Intl API para moneda).
* **Base de Datos:** MySQL.
* **Visualización:** Chart.js.

## ⚙️ Configuración
El sistema permite ajustar la tasa de cambio sin tocar el código fuente:
1. Ir a `src/main/resources/application.properties`.
2. Modificar la propiedad `tasa.cambio=60.0`.
