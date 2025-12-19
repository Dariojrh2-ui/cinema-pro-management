<p align="center">
  ![Dashboard CinemaPro](<img width="1280" height="800" alt="Gemini_Generated_Image_etkkc7etkkc7etkk (1)" src="https://github.com/user-attachments/assets/0d53f7ab-b658-4d8c-a309-cda0655050ef" />)
</p>

# 🎬 CinemaPro - Sistema de Gestión de Cine y Videoclub
## 📺 Demo en Video
Haz clic en la imagen a continuación para ver el funcionamiento completo del sistema:

[![Demo CinemaPro](https://img.youtube.com/vi/J2-28IfSKF0/0.jpg)](https://www.youtube.com/watch?v=J2-28IfSKF0)

*Nota: En el video se muestra la gestión de inventario, el panel administrativo y la lógica de ventas.*

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
