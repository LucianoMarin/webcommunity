# Contrato de interacción

Fuente de negocio: instrucciones ISY2202 semana 3 y decisión del usuario de mantener eventos gamer y ejecutar en PC local. SecurityConfig es la autoridad ejecutable de permisos.

## Canonical UI Map
| Capability | Canonical owner | Source of truth | Allowed variants | Verification |
| --- | --- | --- | --- | --- |
| Form | Controllers y DTOs Jakarta Validation | DTOs y UX-CONTRACT.md | Alta, edición; validación del servidor; nunca conservar contraseñas | Pruebas HTTP y navegador |
| Select/Listbox | Select nativo HTML | UX-CONTRACT.md | Nativo, opciones en español | Navegador |
| Date | datetime-local nativo HTML | Evento.java y UX-CONTRACT.md | Nativo, ISO en servidor | Pruebas HTTP y navegador |
| Scrollbar | styles.css | DESIGN.md | Color compartido y espacio estable | Navegador |
| Toast | Mensajes inline .msg | UX-CONTRACT.md | Estado persistente y error en página | Navegador |
| CRUD | AdminController y servicios | SecurityConfig y UX-CONTRACT.md | Alta/edición, redirect a panel; eliminación POST con CSRF y confirmación inline | Pruebas HTTP |

Inicio y búsqueda son públicos, detalle y perfil requieren sesión, administración exige ADMIN. El acceso a una página privada guarda el destino para después del login. Logout sólo mediante POST. Los formularios bloquean doble envío y tienen salida mediante navegación normal.

Las eliminaciones son permanentes y se confirman en la fila antes de enviar el formulario. El administrador no puede borrarse ni degradarse a sí mismo. No hay sincronización offline ni cambios optimistas: las mutaciones se confirman únicamente mediante respuesta del servidor. Si expira el token, volver a cargar el formulario.

Las preferencias email/push se guardan como datos; el proyecto no envía notificaciones externas. El avatar es un recurso local permitido, sin URLs remotas arbitrarias. La búsqueda es GET explícito y ofrece limpiar filtros. No se agrega facturación ni integración externa.
