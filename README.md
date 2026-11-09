# Gamer Community

Proyecto ISY2202 de Bastian Ortiz y Luciano Romario. Docente: Carlos Valverde. Ingeniería en Desarrollo de Software, Duoc UC.

## Abrir en este PC

1. Abre `INICIAR.cmd` con doble clic, o ejecuta `powershell -ExecutionPolicy Bypass -File .\INICIAR.ps1` desde esta carpeta.
2. Visita http://127.0.0.1:8080/home. El alias http://127.0.0.1:8080/recetas también muestra el inicio gamer, para compatibilidad con la guía que mezcla ambos casos.
3. Consulta `.local-credentials.txt` para las claves aleatorias de `admin`, `usuario` y `usuario2`. No incluyas ese archivo en la entrega ni compartas contraseñas en el video.
4. Para detener el proceso iniciado en segundo plano usa `DETENER.ps1`. Si se ejecuta en consola, usa Ctrl+C.

El JDK portátil y Maven están en `../.tools`, en este PC. El ZIP incluye el JAR compilado para iniciar directamente en otro equipo con Java 17. Para recompilar usa `COMPILAR.ps1`. Se conserva Maven Wrapper 3.9.11 para descargar Maven automáticamente cuando no se utiliza la carpeta portátil. La primera compilación requiere Internet.

## Base de datos local

Se usa H2 persistente en `data/gamer_events.mv.db`, sin consola web habilitada. El arranque crea roles, tres usuarios y cuatro eventos si no existen. Las contraseñas se generan en el primer arranque y se cifran con DPAPI en `.local-secrets.xml`, ligado a tu usuario Windows. No borres ese archivo si quieres conservar el acceso a la base existente. Se generan cuentas distintas en cada instalación.

`database/local-schema-data.sql` contiene la exportación de esquema y datos de ejemplo, con las cuentas de ejemplo deshabilitadas, para cumplir la entrega de creación e inserción. Es una instantánea para revisión o restauración en una base nueva, no debe importarse sobre una base que ya contenga datos. El inicio normal usa el inicializador Java y no necesita importar esta instantánea. La exportación no distribuye hashes de las contraseñas locales: una restauración requiere establecer hashes BCrypt propios antes de iniciar sesión. Las claves del PC y su base activa no se modifican.

La aplicación sólo escucha en 127.0.0.1, conforme al alcance local solicitado. El perfil local usa HTTP para loopback. Si se cambia a un despliegue público, configurar HTTPS y cookies Secure; no publicar la configuración local tal cual.

## MySQL opcional

`game_event.sql` crea esquema, roles y eventos en una base vacía sin eliminar bases previas. Ejecutarlo con una cuenta de instalación. Crear aparte una cuenta de ejecución con únicamente SELECT, INSERT, UPDATE y DELETE en `gamer_events.*`, sin `GRANT OPTION` ni privilegios globales. Proporcionar `SPRING_PROFILES_ACTIVE=mysql`, `DB_URL`, `DB_USER` y `DB_PASSWORD` por entorno. El perfil valida el esquema y exige TLS por defecto. Para crear las tres cuentas, habilitar temporalmente `SEED_ENABLED=true` y suministrar `DEMO_ADMIN_PASSWORD`, `DEMO_USUARIO_PASSWORD` y `DEMO_USUARIO2_PASSWORD` (12 caracteres mínimo y 72 bytes máximo). No se ensayó MySQL en esta entrega local.

## Permisos

| Ruta | Visitante | Usuario | Administrador |
| --- | --- | --- | --- |
| /, /home, /recetas | Sí | Sí | Sí |
| GET /eventos y filtros | Sí | Sí | Sí |
| /login, /registro | Sí | Sí | Sí |
| /eventos/{id}, /perfil | Login | Sí | Sí |
| /admin y subrutas | Login | 403 | Sí |

Las modificaciones requieren POST y token CSRF. Las eliminaciones piden confirmación en la fila. El administrador no puede borrar su propia cuenta ni cambiar su propio rol durante la sesión. Al borrar otra cuenta o cambiar sus roles se revocan sus sesiones. Los avatares se limitan al recurso local del proyecto. Las preferencias email/push sólo se guardan: no hay servicio externo de envío.

## Verificar

Ejecuta `COMPILAR.ps1`. Los resultados de pruebas están en `target/surefire-reports`. Las evidencias de navegador, ZAP y dependencias están en la carpeta `../evidencias`. Consulta el formato de respuesta completado en Word para conocer el alcance y los límites de la comprobación; un escaneo limpio no demuestra ausencia universal de vulnerabilidades.

Para abrir la interfaz de ZAP en este PC usa `ABRIR_ZAP.cmd` con el puerto 8090 libre. Desde ZAP abre el navegador integrado y recorre la aplicación local. La instalación portátil se conserva en `.tools` junto al proyecto, pero no se incluye en el ZIP académico.
