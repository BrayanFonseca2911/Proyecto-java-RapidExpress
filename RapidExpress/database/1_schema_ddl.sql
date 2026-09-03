-- ============================================================
-- RapidExpress - Esquema de base de datos (DDL)
-- Motor: MySQL 8.x
--
-- Nomenclatura: nombres de tabla en singular (vehiculo, conductor,
-- paquete, ruta, ...) porque asi los referencia el codigo JDBC en
-- rapidexpress.repositorio.*DAO. No renombrar sin actualizar el DAO.
-- ============================================================

CREATE DATABASE IF NOT EXISTS rapidexpress
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE rapidexpress;

-- ------------------------------------------------------------
-- Tabla: vehiculo
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS vehiculo (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    placa             VARCHAR(10)  NOT NULL,
    marca             VARCHAR(50)  NOT NULL,
    modelo            VARCHAR(50)  NOT NULL,
    anio              INT          NOT NULL,
    capacidad_carga   DECIMAL(10,2) NOT NULL,
    estado            ENUM('DISPONIBLE', 'EN_RUTA', 'EN_MANTENIMIENTO', 'INACTIVO')
                      NOT NULL DEFAULT 'DISPONIBLE',
    CONSTRAINT uq_vehiculo_placa UNIQUE (placa),
    CONSTRAINT chk_vehiculo_anio CHECK (anio BETWEEN 1900 AND 2100),
    CONSTRAINT chk_vehiculo_capacidad CHECK (capacidad_carga > 0)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabla: conductor
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS conductor (
    id                      INT AUTO_INCREMENT PRIMARY KEY,
    numero_identificacion   VARCHAR(15)  NOT NULL,
    nombre                  VARCHAR(100) NOT NULL,
    tipo_licencia           VARCHAR(5)   NOT NULL,
    contacto                VARCHAR(20),
    estado                  ENUM('ACTIVO', 'DE_VACACIONES', 'INACTIVO', 'EN_RUTA')
                            NOT NULL DEFAULT 'ACTIVO',
    vehiculo_id             INT NULL,
    CONSTRAINT uq_conductor_identificacion UNIQUE (numero_identificacion),
    CONSTRAINT fk_conductor_vehiculo FOREIGN KEY (vehiculo_id)
        REFERENCES vehiculo (id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabla: remitente
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS remitente (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    direccion  VARCHAR(200) NOT NULL,
    telefono   VARCHAR(20)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabla: destinatario
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS destinatario (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    direccion  VARCHAR(200) NOT NULL,
    telefono   VARCHAR(20)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabla: paquete
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS paquete (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    tracking_id      VARCHAR(20)  NOT NULL,
    descripcion      VARCHAR(200) NOT NULL,
    peso             DECIMAL(10,2) NOT NULL,
    dimensiones      VARCHAR(50),
    origen           VARCHAR(200) NOT NULL,
    destino          VARCHAR(200) NOT NULL,
    estado           ENUM('EN_BODEGA', 'ASIGNADO_A_RUTA', 'EN_TRANSITO', 'ENTREGADO', 'DEVUELTO')
                     NOT NULL DEFAULT 'EN_BODEGA',
    remitente_id     INT NOT NULL,
    destinatario_id  INT NOT NULL,
    fecha_registro   DATETIME NOT NULL,
    fecha_entrega    DATETIME NULL,
    CONSTRAINT uq_paquete_tracking UNIQUE (tracking_id),
    CONSTRAINT chk_paquete_peso CHECK (peso > 0),
    CONSTRAINT fk_paquete_remitente FOREIGN KEY (remitente_id)
        REFERENCES remitente (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_paquete_destinatario FOREIGN KEY (destinatario_id)
        REFERENCES destinatario (id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabla: ruta
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS ruta (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    vehiculo_id   INT NOT NULL,
    conductor_id  INT NOT NULL,
    fecha         DATETIME NOT NULL,
    estado        ENUM('PLANIFICADA', 'EN_CURSO', 'COMPLETADA', 'CANCELADA')
                  NOT NULL DEFAULT 'PLANIFICADA',
    peso_total    DECIMAL(10,2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_ruta_vehiculo FOREIGN KEY (vehiculo_id)
        REFERENCES vehiculo (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_ruta_conductor FOREIGN KEY (conductor_id)
        REFERENCES conductor (id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabla: ruta_paquete (relacion N:M entre ruta y paquete)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS ruta_paquete (
    ruta_id      INT NOT NULL,
    paquete_id   INT NOT NULL,
    PRIMARY KEY (ruta_id, paquete_id),
    CONSTRAINT fk_rutapaquete_ruta FOREIGN KEY (ruta_id)
        REFERENCES ruta (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_rutapaquete_paquete FOREIGN KEY (paquete_id)
        REFERENCES paquete (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabla: mantenimiento
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS mantenimiento (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    vehiculo_id   INT NOT NULL,
    fecha         DATETIME NOT NULL,
    descripcion   VARCHAR(200) NOT NULL,
    costo         DECIMAL(10,2) NOT NULL,
    kilometraje   INT NOT NULL,
    CONSTRAINT fk_mantenimiento_vehiculo FOREIGN KEY (vehiculo_id)
        REFERENCES vehiculo (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_mantenimiento_costo CHECK (costo >= 0),
    CONSTRAINT chk_mantenimiento_km CHECK (kilometraje >= 0)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabla: auditoria (registro de auditoria en base de datos;
-- ademas se escribe en el archivo de texto auditoria.log)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS auditoria (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    fecha            DATETIME NOT NULL,
    usuario          VARCHAR(50)  NOT NULL,
    accion           VARCHAR(30)  NOT NULL,
    tabla_afectada   VARCHAR(50)  NOT NULL,
    registro_id      VARCHAR(50),
    detalle          VARCHAR(500),
    ip               VARCHAR(50)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Indices de apoyo para las consultas mas frecuentes del DAO
-- ------------------------------------------------------------
CREATE INDEX idx_paquete_estado ON paquete (estado);
CREATE INDEX idx_ruta_estado ON ruta (estado);
CREATE INDEX idx_ruta_vehiculo ON ruta (vehiculo_id);
CREATE INDEX idx_mantenimiento_vehiculo ON mantenimiento (vehiculo_id);
CREATE INDEX idx_auditoria_fecha ON auditoria (fecha);
CREATE INDEX idx_auditoria_accion ON auditoria (accion);
