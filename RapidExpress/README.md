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

================================================================================
1. BÚSQUEDA DE PAQUETES POR DESTINATARIO (MUY PROBABLE)
================================================================================

// En IPaqueteDAO.java - Agregar este método al interface:
List<Paquete> buscarPorDestinatario(String nombreDestinatario) throws DataBaseException;

// En PaqueteDAO.java - Implementación:
@Override
public List<Paquete> buscarPorDestinatario(String nombreDestinatario) throws DataBaseException {
    List<Paquete> paquetes = new ArrayList<>();
    String sql = "SELECT * FROM paquetes WHERE LOWER(destinatario_nombre) LIKE LOWER(?)";

    try (Connection conn = DBConnection.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, "%" + nombreDestinatario + "%");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Paquete paquete = mapearResultSet(rs);
            paquetes.add(paquete);
        }
    } catch (SQLException e) {
        throw new DataBaseException("Error al buscar paquetes por destinatario: " + e.getMessage(), e);
    }

    return paquetes;
}

// En PaqueteService.java - Agregar método público:
public List<Paquete> buscarPorDestinatario(String nombreDestinatario) throws DataBaseException {
    if (nombreDestinatario == null || nombreDestinatario.trim().isEmpty()) {
        throw new DataBaseException("El nombre del destinatario es obligatorio");
    }

    List<Paquete> resultados = paqueteDAO.buscarPorDestinatario(nombreDestinatario.trim());

    registrarAuditoria("READ", "PAQUETE", "BUSQUEDA",
                      "Se buscaron paquetes por destinatario: " + nombreDestinatario);

    return resultados;
}

// En PaqueteController.java - Método para usar desde la vista:
public void mostrarPaquetesPorDestinatario() {
    try {
        System.out.print("Ingrese nombre del destinatario: ");
        String nombre = scanner.nextLine();

        List<Paquete> paquetes = paqueteService.buscarPorDestinatario(nombre);

        if (paquetes.isEmpty()) {
            System.out.println("No se encontraron paquetes para ese destinatario.");
        } else {
            System.out.println("\n=== PAQUETES ENCONTRADOS ===");
            for (Paquete p : paquetes) {
                System.out.println(p);
            }
        }
    } catch (DataBaseException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

================================================================================
2. VALIDAR PESO DEL PAQUETE VS CAPACIDAD DEL VEHÍCULO (MUY PROBABLE)
================================================================================

// En Paquete.java - Agregar método de utilidad:
public boolean puedeSerTransportadoPor(Vehiculo vehiculo) {
    if (vehiculo == null) {
        return false;
    }
    return this.peso <= vehiculo.getCapacidadCarga();
}

// En PaqueteService.java - Agregar método con validación:
public void validarPesoVsCapacidad(Paquete paquete, Vehiculo vehiculo) throws DataBaseException, CapacidadExcedidaException {
    if (paquete == null) {
        throw new DataBaseException("El paquete no puede ser nulo");
    }

    if (vehiculo == null) {
        throw new DataBaseException("El vehículo no puede ser nulo");
    }

    if (!paquete.puedeSerTransportadoPor(vehiculo)) {
        throw new CapacidadExcedidaException(
            "El peso del paquete (" + paquete.getPeso() + " kg) excede la capacidad del vehículo " +
            vehiculo.getPlaca() + " (" + vehiculo.getCapacidadCarga() + " kg)"
        );
    }
}

================================================================================
3. CONTAR PAQUETES POR ESTADO (REPORTE SIMPLE - MUY PROBABLE)
================================================================================

// En IPaqueteDAO.java - Agregar al interface:
int contarPorEstado(EstadoPaquete estado) throws DataBaseException;

// En PaqueteDAO.java - Implementación:
@Override
public int contarPorEstado(EstadoPaquete estado) throws DataBaseException {
    String sql = "SELECT COUNT(*) as total FROM paquetes WHERE estado = ?";

    try (Connection conn = DBConnection.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, estado.name());
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("total");
        }
    } catch (SQLException e) {
        throw new DataBaseException("Error al contar paquetes por estado: " + e.getMessage(), e);
    }

    return 0;
}

// En PaqueteService.java - Método público:
public Map<String, Integer> obtenerConteoPaquetesPorEstado() throws DataBaseException {
    Map<String, Integer> conteo = new HashMap<>();

    for (EstadoPaquete estado : EstadoPaquete.values()) {
        int cantidad = paqueteDAO.contarPorEstado(estado);
        conteo.put(estado.getDescripcion(), cantidad);
    }

    return conteo;
}

// Uso desde el main o controller:
public void mostrarResumenPaquetes() {
    try {
        Map<String, Integer> resumen = paqueteService.obtenerConteoPaquetesPorEstado();

        System.out.println("\n=== RESUMEN DE PAQUETES ===");
        for (Map.Entry<String, Integer> entry : resumen.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    } catch (DataBaseException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

================================================================================
4. ASIGNAR PAQUETE A RUTA (RELACIÓN ENTRE ENTIDADES - PROBABLE)
================================================================================

// En IRutaDAO.java - Agregar al interface:
boolean asignarPaquete(Integer rutaId, Integer paqueteId) throws DataBaseException;

// En RutaDAO.java - Implementación:
@Override
public boolean asignarPaquete(Integer rutaId, Integer paqueteId) throws DataBaseException {
    String sql = "UPDATE paquetes SET ruta_id = ?, estado = 'ASIGNADO_A_RUTA' WHERE id = ?";

    try (Connection conn = DBConnection.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, rutaId);
        stmt.setInt(2, paqueteId);

        int filasAfectadas = stmt.executeUpdate();
        return filasAfectadas > 0;
    } catch (SQLException e) {
        throw new DataBaseException("Error al asignar paquete a ruta: " + e.getMessage(), e);
    }
}

// En RutaService.java - Método completo con validaciones:
public void asignarPaqueteARuta(String trackingId, Integer rutaId)
        throws DataBaseException, PaqueteNotFoundException, RutaInvalidaException {

    // Buscar paquete
    Paquete paquete = paqueteDAO.buscarPorTrackingId(trackingId);
    if (paquete == null) {
        throw new PaqueteNotFoundException(trackingId);
    }

    // Validar que el paquete esté en bodega
    if (!paquete.enBodega()) {
        throw new RutaInvalidaException("El paquete no está en bodega, estado actual: " + paquete.getEstado());
    }

    // Asignar a ruta
    rutaDAO.asignarPaquete(rutaId, paquete.getId());

    // Actualizar estado del paquete
    paquete.setEstado(EstadoPaquete.ASIGNADO_A_RUTA);
    paqueteDAO.actualizar(paquete);

    // Auditoría
    registrarAuditoria("UPDATE", "PAQUETE", trackingId,
                      "Se asignó paquete a ruta ID: " + rutaId);
}

================================================================================
5. FILTRAR CONDUCTORES POR TIPO DE LICENCIA (FILTRO - PROBABLE)
================================================================================

// En IConductorDAO.java - Agregar al interface:
List<Conductor> buscarPorTipoLicencia(String tipoLicencia) throws DataBaseException;

// En ConductorDAO.java - Implementación:
@Override
public List<Conductor> buscarPorTipoLicencia(String tipoLicencia) throws DataBaseException {
    List<Conductor> conductores = new ArrayList<>();
    String sql = "SELECT * FROM conductores WHERE LOWER(tipo_licencia) = LOWER(?)";

    try (Connection conn = DBConnection.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, tipoLicencia);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Conductor conductor = mapearResultSet(rs);
            conductores.add(conductor);
        }
    } catch (SQLException e) {
        throw new DataBaseException("Error al buscar conductores por licencia: " + e.getMessage(), e);
    }

    return conductores;
}

// En ConductorService.java - Método público:
public List<Conductor> filtrarPorTipoLicencia(String tipoLicencia) throws DataBaseException {
    if (tipoLicencia == null || tipoLicencia.trim().isEmpty()) {
        throw new DataBaseException("El tipo de licencia es obligatorio");
    }

    return conductorDAO.buscarPorTipoLicencia(tipoLicencia.trim());
}

================================================================================
6. CAMBIAR ESTADO DE CONDUCTOR A INACTIVO Y LIBERAR VEHÍCULO (PROBABLE)
================================================================================

// En ConductorService.java - Agregar nuevo método:
public void darDeBajaConductor(String numeroIdentificacion)
        throws DataBaseException, ConductorNoDisponibleException {

    // Buscar conductor
    Conductor conductor = conductorDAO.buscarPorNumeroIdentificacion(numeroIdentificacion);
    if (conductor == null) {
        throw new ConductorNoDisponibleException("No existe conductor con identificacion: " + numeroIdentificacion);
    }

    // Si tiene vehículo asignado, liberarlo
    if (conductor.getVehiculoAsignado() != null) {
        conductorDAO.liberarVehiculo(conductor.getId());
        conductor.setVehiculoAsignado(null);
    }

    // Cambiar estado a INACTIVO
    conductor.setEstado(EstadoConductor.INACTIVO);
    conductorDAO.actualizar(conductor);

    // Auditoría
    registrarAuditoria("UPDATE", "CONDUCTOR", String.valueOf(conductor.getId()),
                      "Se dio de baja al conductor: " + numeroIdentificacion);
}

================================================================================
7. CALCULAR COSTO DE ENVÍO (MÉTODO DE UTILIDAD - PROBABLE)
================================================================================

// En Paquete.java - Agregar método:
public double calcularCostoEnvio(double costoPorKilo, double costoPorDistancia) {
    if (costoPorKilo <= 0 || costoPorDistancia <= 0) {
        throw new IllegalArgumentException("Los costos deben ser mayores a cero");
    }

    // Calcular distancia aproximada (simplificado)
    double distancia = Math.abs(this.origen.hashCode() - this.destino.hashCode()) % 1000;

    return (this.peso * costoPorKilo) + (distancia * costoPorDistancia);
}

// O versión simplificada:
public double calcularCostoEnvioSimple(double tarifaBase, double costoPorKilo) {
    if (tarifaBase < 0 || costoPorKilo < 0) {
        throw new IllegalArgumentException("Las tarifas no pueden ser negativas");
    }

    return tarifaBase + (this.peso * costoPorKilo);
}

================================================================================
8. LISTAR VEHÍCULOS DISPONIBLES VS EN MANTENIMIENTO (REPORTE - PROBABLE)
================================================================================

// En VehiculoService.java - Agregar método:
public Map<String, Integer> obtenerResumenEstadoVehiculos() throws DataBaseException {
    Map<String, Integer> resumen = new HashMap<>();

    List<Vehiculo> todos = vehiculoDAO.listarTodos();

    int disponibles = 0;
    int enRuta = 0;
    int mantenimiento = 0;
    int inactivos = 0;

    for (Vehiculo v : todos) {
        switch (v.getEstado()) {
            case DISPONIBLE:
                disponibles++;
                break;
            case EN_RUTA:
                enRuta++;
                break;
            case EN_MANTENIMIENTO:
                mantenimiento++;
                break;
            case INACTIVO:
                inactivos++;
                break;
        }
    }

    resumen.put("Disponibles", disponibles);
    resumen.put("En Ruta", enRuta);
    resumen.put("En Mantenimiento", mantenimiento);
    resumen.put("Inactivos", inactivos);

    return resumen;
}

================================================================================
9. ORDENAR PAQUETES POR FECHA DE REGISTRO (ORDENAMIENTO - PROBABLE)
================================================================================

// En PaqueteService.java - Agregar método:
public List<Paquete> listarPaquetesOrdenadosPorFecha() throws DataBaseException {
    List<Paquete> paquetes = paqueteDAO.listarTodos();

    // Ordenar por fecha de registro (más reciente primero)
    paquetes.sort((p1, p2) -> {
        if (p1.getFechaRegistro() == null) return 1;
        if (p2.getFechaRegistro() == null) return -1;
        return p2.getFechaRegistro().compareTo(p1.getFechaRegistro());
    });

    return paquetes;
}

// O usando Stream API (más moderno):
public List<Paquete> listarPaquetesOrdenadosPorFechaStream() throws DataBaseException {
    return paqueteDAO.listarTodos().stream()
        .sorted(Comparator.comparing(Paquete::getFechaRegistro, Comparator.nullsLast(Comparator.reverseOrder())))
        .collect(Collectors.toList());
}

================================================================================
10. IMPEDIR ELIMINAR PAQUETE SI ESTÁ EN TRANSITO O ENTREGADO (VALIDACIÓN)
================================================================================

// En PaqueteService.java - Agregar método:
public void eliminarPaquete(String trackingId)
        throws DataBaseException, PaqueteNotFoundException {

    Paquete paquete = paqueteDAO.buscarPorTrackingId(trackingId);
    if (paquete == null) {
        throw new PaqueteNotFoundException(trackingId);
    }

    // Validar que no esté en estados protegidos
    if (paquete.enTransito() || paquete.entregado()) {
        throw new DataBaseException(
            "No se puede eliminar un paquete que está en tránsito o entregado. " +
            "Estado actual: " + paquete.getEstado()
        );
    }

    // Eliminar (si tu DAO tiene el método)
    paqueteDAO.eliminar(paquete.getId());

    // Auditoría
    registrarAuditoria("DELETE", "PAQUETE", trackingId,
                      "Se eliminó paquete: " + trackingId);
}

================================================================================
IMPORTANTE: IMPORTS NECESARIOS
================================================================================

// Agrega estos imports según necesites en cada archivo:

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

================================================================================
RECOMENDACIONES
================================================================================

1. Lee cuidadosamente lo que te pide el profesor
2. Identifica en qué capa va la funcionalidad:
   - Dominio: Métodos de negocio en las clases (Paquete.java, Conductor.java, etc.)
   - Servicio: Lógica con validaciones (PaqueteService.java, etc.)
   - Repositorio: Acceso a base de datos (PaqueteDAO.java, etc.)
   - Controlador: Interacción con usuario (PaqueteController.java, etc.)

3. Sigue el patrón que ya usa tu proyecto:
   - Validar datos primero
   - Ejecutar operación
   - Registrar auditoría

4. Si te piden algo que no está en esta lista, piensa:
   - ¿Es una búsqueda? → Agrega método en DAO y Service
   - ¿Es una validación? → Agrega if con throw DataBaseException
   - ¿Es un reporte? → Usa streams o ciclos para contar/agrupar
   - ¿Es cambiar estado? → Busca la entidad, valida, actualiza
  
Implementar la funcionalidad de manejo de archivos (exportar a .txt y .json) en tu proyecto RapidExpress:
Código para agregar en PaqueteService.java
Imports necesarios:
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

METODOS PARA EXPORTAR:
// FUNCIONALIDAD 1: Exportar a JSON
public void exportarPaquetesAJson(List<Paquete> paquetes, String nombreArchivo) {
    String rutaCompleta = "reportes/" + nombreArchivo; 
    
    try {
        Files.createDirectories(Paths.get("reportes"));
        
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(rutaCompleta))) {
            writer.write("[\n");
            
            for (int i = 0; i < paquetes.size(); i++) {
                Paquete p = paquetes.get(i);
                writer.write("  {\n");
                writer.write("    \"id\": \"" + p.getId() + "\",\n");
                writer.write("    \"destinatario\": \"" + p.getDestinatario() + "\",\n");
                writer.write("    \"peso\": " + p.getPeso() + ",\n");
                writer.write("    \"estado\": \"" + p.getEstado() + "\"\n");
                writer.write("  }");
                
                if (i < paquetes.size() - 1) {
                    writer.write(",\n");
                } else {
                    writer.write("\n");
                }
            }
            
            writer.write("]");
        }
        System.out.println("✅ Archivo JSON generado exitosamente en: " + rutaCompleta);
        
    } catch (IOException e) {
        System.err.println("❌ Error al escribir el archivo JSON: " + e.getMessage());
        throw new RuntimeException("No se pudo generar el reporte", e);
    }
}

// FUNCIONALIDAD 2: Exportar a TXT
public void exportarReporteTexto(List<Paquete> paquetes, String nombreArchivo) {
    String rutaCompleta = "reportes/" + nombreArchivo;
    
    try {
        Files.createDirectories(Paths.get("reportes"));
        
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(rutaCompleta))) {
            writer.write("=== REPORTE DE PAQUETES RAPIDEXPRESS ===\n");
            writer.write("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n");
            writer.write("Total paquetes: " + paquetes.size() + "\n");
            writer.write("=======================================\n\n");
            
            for (Paquete p : paquetes) {
                writer.write("ID: " + p.getId() + " | Dest: " + p.getDestinatario());
                writer.write(" | Peso: " + p.getPeso() + "kg | Estado: " + p.getEstado() + "\n");
            }
            
            writer.write("\n=== FIN DEL REPORTE ===");
        }
        System.out.println("✅ Archivo TXT generado exitosamente en: " + rutaCompleta);
        
    } catch (IOException e) {
        System.err.println("❌ Error al escribir el archivo TXT: " + e.getMessage());
        throw new RuntimeException("No se pudo generar el reporte", e);
    }
}

METODO PARA LLAMAR EN EL CONTROLLER
public void generarReportes() {
    List<Paquete> todosLosPaquetes = paqueteDAO.listarTodos();
    String fechaHoy = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    
    paqueteService.exportarPaquetesAJson(todosLosPaquetes, "paquetes_" + fechaHoy + ".json");
    paqueteService.exportarReporteTexto(todosLosPaquetes, "reporte_" + fechaHoy + ".txt");
    
    System.out.println("Proceso de exportación finalizado.");
}

## Autores

- Brayan Fonseca ([@BrayanFonseca2911](https://github.com/BrayanFonseca2911))
