# RapidExpress

Sistema de backend por linea de comandos (CLI) para la gestion integral de una
flota de vehiculos, su personal de conduccion, la planificacion de rutas y el
seguimiento de paquetes de una empresa de mensajeria.

## Descripcion del proyecto

RapidExpress centraliza en una sola aplicacion Java lo que antes se manejaba en
hojas de calculo y comunicacion manual entre operadores de logistica y
conductores:

- **Flota de vehiculos**: registro, consulta y cambio de estado
  (`Disponible`, `En Ruta`, `En Mantenimiento`), con historial de
  mantenimientos por vehiculo.
- **Conductores**: registro, consulta y gestion de estado
  (`Activo`, `De Vacaciones`, `Inactivo`), con asignacion de un vehiculo por
  conductor (nunca mas de uno a la vez).
- **Paquetes y envios**: alta con tracking ID unico, remitente y
  destinatario, y ciclo de vida completo
  (`En Bodega` → `Asignado a Ruta` → `En Transito` → `Entregado` / `Devuelto`).
- **Rutas**: creacion de hojas de ruta que agrupan paquetes en bodega bajo un
  vehiculo y conductor sin exceder la capacidad de carga, inicio/fin de ruta
  con actualizacion coordinada de estados, y monitoreo de rutas activas.
- **Reportes y auditoria**: entregas por conductor en un rango de fechas,
  historial de rutas por vehiculo, resumen de paquetes por estado, y un
  registro de auditoria de toda operacion critica, persistido en el archivo
  de texto `auditoria.log`.

## Tecnologias utilizadas

| Componente        | Tecnologia                                   |
|-------------------|-----------------------------------------------|
| Lenguaje          | Java 21+ (compilado con `--release 25`)       |
| Build             | Maven                                          |
| Persistencia      | MySQL 8 (JDBC, `mysql-connector-j`)            |
| Interfaz          | CLI (linea de comandos, `java.util.Scanner`)   |
| Patrones          | MVC, DAO, Singleton, inyeccion por constructor |
| Programacion func.| Stream API, lambdas, `Collectors`, `Optional`  |

## Arquitectura

El proyecto sigue **Modelo-Vista-Controlador** con una capa de acceso a datos
separada (DAO), organizada en paquetes por responsabilidad:

```
rapidexpress/
├── dominio/       Entidades del negocio (Vehiculo, Conductor, Paquete, Ruta, ...)
├── enums/         Estados de cada entidad, con su propia maquina de estados
├── repositorio/   Contratos IxxxDAO + implementaciones JDBC (persistencia)
├── servicio/      Reglas de negocio, validaciones y transacciones (Modelo)
├── controlador/   Orquestan flujo de entrada del usuario (Controlador)
├── vista/         Impresion en consola (Vista)
├── auditoria/     Registro de auditoria en archivo de texto
├── excepciones/   Excepciones de negocio propias (checked)
└── util/          Configuracion, conexion a BD, validaciones, utilidades
```

**Nota sobre el paquete raiz:** el punto de entrada (`RapidExpress.main`) vive
en `com.mycompany.rapidexpress` por convencion del arquetipo Maven inicial,
mientras que el resto de la aplicacion vive en el paquete `rapidexpress.*`.
Se mantiene asi para no forzar un movimiento masivo de archivos sin valor
funcional; la separacion por capas (`dominio`, `servicio`, `repositorio`, ...)
es la que importa para la arquitectura.

**Principios aplicados:**

- **Inversion de dependencias (DIP):** cada servicio depende de una interfaz
  (`IVehiculoDAO`, `IConductorDAO`, `IPaqueteDAO`, `IRutaDAO`,
  `IMantenimientoDAO`, `IAuditoriaDAO`), no de la implementacion JDBC
  concreta. Cada servicio expone ademas un constructor que recibe esas
  interfaces, para poder sustituirlas por dobles de prueba.
- **Segregacion de interfaces (ISP):** `IAuditoriaDAO` no extiende el `IDAO`
  generico porque los registros de auditoria son de solo creacion/consulta
  (no se actualizan ni se eliminan).
- **Responsabilidad unica:** las vistas solo imprimen, los controladores solo
  orquestan, los servicios contienen las reglas de negocio y los DAO solo
  hablan SQL.

## Diseno de la base de datos

Motor: **MySQL 8**, InnoDB. El modelo relacional tiene 9 tablas:

- `vehiculo`, `conductor`, `remitente`, `destinatario`, `paquete`, `ruta` y
  `mantenimiento` como entidades principales.
- `ruta_paquete` como tabla puente N:M entre `ruta` y `paquete` (una ruta
  agrupa varios paquetes; un paquete pertenece a una ruta a la vez).
- `auditoria` como bitacora independiente (no referencia otras tablas).

El diagrama entidad-relacion completo esta en
[`database/diagrama_entidad_relacion.png`](database/diagrama_entidad_relacion.png)
(fuente editable en [`database/diagrama_entidad_relacion.mmd`](database/diagrama_entidad_relacion.mmd),
formato Mermaid ER).

Scripts SQL en [`database/`](database):

- [`1_schema_ddl.sql`](database/1_schema_ddl.sql): `CREATE TABLE` con llaves
  primarias, foraneas, restricciones `UNIQUE`/`CHECK` e indices de apoyo.
- [`2_data_dml.sql`](database/2_data_dml.sql): datos de prueba (20+ registros
  por entidad principal), consistentes con las maquinas de estado del
  dominio.

## Instalacion y ejecucion

### 1. Requisitos

- JDK 21 o superior
- Maven (o el Maven embebido de tu IDE)
- Una instancia de MySQL 8 accesible por red — se recomienda un servicio en
  la nube gratuito o de bajo costo (AWS RDS Free Tier, Google Cloud SQL,
  Azure Database for MySQL, Aiven, Railway, Clever Cloud, etc.)

### 2. Preparar la base de datos en la nube

1. Crea una instancia de MySQL en el proveedor que elijas y anota host,
   puerto, usuario y contrasena.
2. Conectate con tu cliente SQL favorito (DBeaver, MySQL Workbench, `mysql`
   CLI) y ejecuta, en orden:
   ```sql
   SOURCE database/1_schema_ddl.sql;
   SOURCE database/2_data_dml.sql;
   ```

### 3. Configurar las credenciales

```bash
cd RapidExpress
cp database.properties.example database.properties
```

Edita `database.properties` con los datos reales de tu base de datos en la
nube:

```properties
db.url=jdbc:mysql://<host>:<puerto>/rapidexpress
db.user=<usuario>
db.password=<contrasena>
db.driver=com.mysql.cj.jdbc.Driver
```

`database.properties` esta excluido en `.gitignore`: nunca subas tus
credenciales reales al repositorio.

### 4. Compilar y ejecutar

```bash
cd RapidExpress
mvn clean compile
mvn exec:java
```

O, desde un IDE (NetBeans/IntelliJ/Eclipse), importa el proyecto como Maven y
ejecuta la clase `com.mycompany.rapidexpress.RapidExpress`.

## Guia de uso

Al iniciar, la aplicacion valida la conexion a la base de datos y muestra el
menu principal:

```
1. Gestion de Vehiculos
2. Gestion de Conductores
3. Gestion de Paquetes
4. Planificacion de Rutas
5. Reportes
6. Salir
```

Flujo tipico de una operacion diaria:

1. **Vehiculos** → registrar los vehiculos de la flota.
2. **Conductores** → registrar conductores y asignarles vehiculo.
3. **Paquetes** → registrar paquetes en bodega con remitente/destinatario.
4. **Rutas** → crear una hoja de ruta (vehiculo + conductor + paquetes en
   bodega, validando capacidad de carga), iniciarla y, mientras esta en
   curso, actualizar el estado de sus paquetes hasta `Entregado`.
5. **Reportes** → consultar entregas por conductor en un rango de fechas,
   historial de rutas de un vehiculo, o el resumen de paquetes por estado.

Toda operacion critica (creacion de paquetes, inicio/fin de ruta, cambios de
estado) queda registrada en `auditoria.log`, en la raiz desde donde se
ejecuta la aplicacion.

## Autores

- Brayan Fonseca ([@BrayanFonseca2911](https://github.com/BrayanFonseca2911))
