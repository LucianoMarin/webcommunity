---
version: alpha
colors:
  background: "#0f0f1a"
  surface: "#1a1a2e"
  accent: "#7f5af0"
  text: "#e6e6f0"
  focus: "#d2c3ff"
typography:
  body:
    fontFamily: "Segoe UI, Tahoma, sans-serif"
rounded:
  card: "10px"
  button: "6px"
spacing:
  page: "2rem"
components:
  card:
    background: "#1a1a2e"
---
# Gamer Community
## Overview
Aplicación de eventos gamer para estudiantes y comunidad chilena. Se conserva la identidad existente: fondo índigo oscuro, tarjetas y acento violeta. No se agrega un rediseño de marketing.
## Colors
La fuente canónica de ejecución es `src/main/resources/static/css/styles.css`; este documento registra sus valores. El violeta identifica navegación y acciones, los avisos incluyen texto y nunca dependen sólo del color.
## Typography
Segoe UI y Tahoma, con controles que heredan la fuente. Español de Chile y fechas dd/MM/yyyy HH:mm.
## Layout
Contenido de hasta 1200 px, tarjetas adaptables y navegación que se distribuye en varias líneas en móvil. Tablas con desplazamiento horizontal.
## Elevation & Depth
Tarjetas con borde, sin añadir sombras decorativas.
## Shapes
Tarjetas de 10 px, botones de 6 px según CSS compartido.
## Components
Formularios Thymeleaf, mensajes en página, controles nativos y confirmación de eliminación mediante details y formulario POST.
## Do's and Don'ts
Conservar la identidad gamer. Mantener foco visible, errores legibles y reducir movimiento si lo solicita el sistema.
