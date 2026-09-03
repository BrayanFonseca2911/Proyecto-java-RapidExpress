-- ============================================================
-- RapidExpress - Datos de prueba (DML)
-- Ejecutar despues de 1_schema_ddl.sql
-- Los estados usados coinciden exactamente con los enums Java
-- (rapidexpress.enums.*) y las maquinas de estado del dominio.
-- ============================================================

USE rapidexpress;

-- ------------------------------------------------------------
-- vehiculo (20 registros)
-- ------------------------------------------------------------
INSERT INTO vehiculo (id, placa, marca, modelo, anio, capacidad_carga, estado) VALUES
(1,  'ABC-101', 'Chevrolet',    'NHR',              2019, 1500.00, 'DISPONIBLE'),
(2,  'ABC-102', 'Chevrolet',    'NPR',              2020, 3000.00, 'DISPONIBLE'),
(3,  'ABC-103', 'Hino',         '300',              2021, 2500.00, 'DISPONIBLE'),
(4,  'ABC-104', 'Hino',         '500',              2018, 6000.00, 'EN_MANTENIMIENTO'),
(5,  'ABC-105', 'Ford',         'Transit',          2022, 1200.00, 'DISPONIBLE'),
(6,  'ABC-106', 'Renault',      'Master',           2020, 1400.00, 'DISPONIBLE'),
(7,  'ABC-107', 'Isuzu',        'NPR',              2019, 3200.00, 'DISPONIBLE'),
(8,  'ABC-108', 'Isuzu',        'ELF',              2021, 2000.00, 'EN_RUTA'),
(9,  'ABC-109', 'Mitsubishi',   'Fuso',             2017, 4500.00, 'DISPONIBLE'),
(10, 'ABC-110', 'Mitsubishi',   'Canter',           2020, 2800.00, 'DISPONIBLE'),
(11, 'ABC-111', 'Kenworth',     'T370',             2016, 8000.00, 'EN_MANTENIMIENTO'),
(12, 'ABC-112', 'Freightliner', 'M2',               2018, 7000.00, 'DISPONIBLE'),
(13, 'ABC-113', 'Chevrolet',    'NKR',              2022, 1800.00, 'DISPONIBLE'),
(14, 'ABC-114', 'Hyundai',      'HD65',             2019, 2200.00, 'DISPONIBLE'),
(15, 'ABC-115', 'JAC',          '1040',             2021, 1900.00, 'EN_RUTA'),
(16, 'ABC-116', 'Foton',        'Aumark',           2020, 2600.00, 'DISPONIBLE'),
(17, 'ABC-117', 'Dongfeng',     'DFL',              2017, 3400.00, 'DISPONIBLE'),
(18, 'ABC-118', 'Volkswagen',   'Delivery',         2022, 2100.00, 'DISPONIBLE'),
(19, 'ABC-119', 'Fiat',         'Ducato',           2020, 1300.00, 'INACTIVO'),
(20, 'ABC-120', 'Iveco',        'Daily',            2021, 1600.00, 'EN_MANTENIMIENTO');

-- ------------------------------------------------------------
-- conductor (20 registros)
-- ------------------------------------------------------------
INSERT INTO conductor (id, numero_identificacion, nombre, tipo_licencia, contacto, estado, vehiculo_id) VALUES
(1,  '1001234567', 'Carlos Andres Ramirez',    'C2', '3001112233', 'ACTIVO',        NULL),
(2,  '1002234567', 'Maria Fernanda Lopez',     'C1', '3002223344', 'ACTIVO',        NULL),
(3,  '1003234567', 'Jorge Luis Torres',        'C3', '3003334455', 'ACTIVO',        NULL),
(4,  '1004234567', 'Diana Patricia Gomez',     'B2', '3004445566', 'DE_VACACIONES', NULL),
(5,  '1005234567', 'Andres Felipe Morales',    'C2', '3005556677', 'ACTIVO',        NULL),
(6,  '1006234567', 'Laura Camila Rodriguez',   'C1', '3006667788', 'ACTIVO',        NULL),
(7,  '1007234567', 'Sergio Ivan Castro',       'C3', '3007778899', 'ACTIVO',        NULL),
(8,  '1008234567', 'Paola Andrea Vargas',      'C2', '3008889900', 'EN_RUTA',       8),
(9,  '1009234567', 'Julian Esteban Herrera',   'D1', '3009990011', 'ACTIVO',        NULL),
(10, '1010234567', 'Natalia Andrea Pena',      'C1', '3010001122', 'ACTIVO',        NULL),
(11, '1011234567', 'Oscar Mauricio Salazar',   'C2', '3011112233', 'INACTIVO',      NULL),
(12, '1012234567', 'Claudia Milena Rojas',     'C3', '3012223344', 'ACTIVO',        NULL),
(13, '1013234567', 'Fabian Ricardo Suarez',    'C1', '3013334455', 'ACTIVO',        NULL),
(14, '1014234567', 'Viviana Alexandra Reyes',  'C2', '3014445566', 'ACTIVO',        NULL),
(15, '1015234567', 'Camilo Andres Ortiz',      'C3', '3015556677', 'EN_RUTA',       15),
(16, '1016234567', 'Alejandra Marcela Cortes', 'C1', '3016667788', 'ACTIVO',        NULL),
(17, '1017234567', 'Ivan Dario Mendoza',       'C2', '3017778899', 'DE_VACACIONES', NULL),
(18, '1018234567', 'Sandra Milena Guzman',     'C1', '3018889900', 'ACTIVO',        5),
(19, '1019234567', 'Ricardo Andres Molina',    'C3', '3019990011', 'ACTIVO',        NULL),
(20, '1020234567', 'Luisa Fernanda Aguirre',   'C2', '3020001122', 'ACTIVO',        9);

-- ------------------------------------------------------------
-- remitente (20 registros)
-- ------------------------------------------------------------
INSERT INTO remitente (id, nombre, direccion, telefono) VALUES
(1,  'Comercializadora El Dorado S.A.S.', 'Cra 15 #93-47, Bogota',       '6013001122'),
(2,  'Ana Maria Duarte',                  'Calle 10 #43-12, Medellin',   '3101234567'),
(3,  'Distribuidora Andina Ltda.',        'Av. Boyaca #12-34, Bogota',   '6014455667'),
(4,  'Pedro Pablo Jimenez',               'Cra 45 #23-10, Cali',         '3122345678'),
(5,  'TiendaMax S.A.S.',                  'Calle 72 #10-34, Barranquilla','6053344556'),
(6,  'Gloria Estela Nieto',               'Cra 8 #15-22, Cartagena',     '3133456789'),
(7,  'Ferreteria Central',                'Cra 30 #45-12, Bucaramanga',  '6076543210'),
(8,  'Manuel Alejandro Rios',             'Calle 19 #9-45, Pereira',     '3144567890'),
(9,  'Almacenes Rey',                     'Cra 50 #60-20, Manizales',    '6068889900'),
(10, 'Beatriz Elena Cardenas',            'Calle 5 #14-33, Cucuta',      '3155678901'),
(11, 'Importadora del Sur S.A.S.',        'Cra 20 #34-56, Santa Marta',  '6054443322'),
(12, 'Jorge Enrique Salcedo',             'Calle 27 #8-19, Ibague',      '3166789012'),
(13, 'Distribuciones Norte Ltda.',        'Cra 9 #22-11, Villavicencio', '6086667788'),
(14, 'Martha Cecilia Vanegas',            'Calle 40 #16-25, Neiva',      '3177890123'),
(15, 'TecnoHogar S.A.S.',                 'Cra 14 #33-40, Armenia',      '6067778899'),
(16, 'Luis Alberto Cifuentes',            'Calle 8 #5-16, Popayan',      '3188901234'),
(17, 'Comercial Los Andes',               'Cra 18 #29-13, Sincelejo',    '6052223344'),
(18, 'Patricia Lorena Bustos',            'Calle 12 #6-27, Monteria',    '3199012345'),
(19, 'Suministros del Caribe S.A.S.',     'Cra 6 #10-18, Valledupar',    '6055556677'),
(20, 'Camilo Ernesto Prada',              'Calle 22 #11-30, Tunja',      '3200123456');

-- ------------------------------------------------------------
-- destinatario (20 registros)
-- ------------------------------------------------------------
INSERT INTO destinatario (id, nombre, direccion, telefono) VALUES
(1,  'Carolina Andrea Munoz',             'Cra 7 #64-32, Bogota',        '3001239876'),
(2,  'Distribuidora Pacifico S.A.S.',     'Av. 6N #23-18, Cali',         '6024567890'),
(3,  'Hernan Dario Zapata',               'Calle 33 #45-21, Medellin',   '3012349871'),
(4,  'Almacen La Economia',               'Cra 44 #50-12, Barranquilla', '6058887766'),
(5,  'Rosa Elvira Campos',                'Calle 15 #9-40, Cartagena',   '3023459872'),
(6,  'ElectroVentas S.A.S.',              'Cra 27 #19-33, Bucaramanga',  '6076665544'),
(7,  'Diego Fernando Salas',              'Calle 50 #20-15, Pereira',    '3034569873'),
(8,  'Comercial San Jose',                'Cra 12 #8-27, Manizales',     '6069998877'),
(9,  'Angela Maria Torres',               'Calle 18 #14-29, Cucuta',     '3045679874'),
(10, 'Repuestos El Motor',                'Cra 22 #33-10, Santa Marta',  '6056667788'),
(11, 'Fernando Luis Ospina',              'Calle 9 #17-24, Ibague',      '3056789875'),
(12, 'Textiles del Valle S.A.S.',         'Cra 16 #40-19, Villavicencio','6089990011'),
(13, 'Marcela Ivonne Prieto',             'Calle 28 #6-13, Neiva',       '3067899876'),
(14, 'Papeleria Central',                 'Cra 11 #24-38, Armenia',      '6067771122'),
(15, 'Wilson Alexander Bravo',            'Calle 14 #10-20, Popayan',    '3078909877'),
(16, 'Muebles y Disenos S.A.S.',          'Cra 19 #31-16, Sincelejo',    '6053332211'),
(17, 'Yolanda Patricia Duque',            'Calle 21 #7-33, Monteria',    '3089019878'),
(18, 'Insumos Agricolas del Cesar',       'Cra 5 #12-24, Valledupar',    '6055554433'),
(19, 'Esteban Camilo Rincon',             'Calle 17 #9-15, Tunja',       '3090129879'),
(20, 'Bodega Central S.A.S.',             'Cra 13 #26-44, Bogota',       '6017778899');

-- ------------------------------------------------------------
-- paquete (20 registros)
-- ------------------------------------------------------------
INSERT INTO paquete (id, tracking_id, descripcion, peso, dimensiones, origen, destino, estado, remitente_id, destinatario_id, fecha_registro, fecha_entrega) VALUES
(1,  'RPX-A1B2C3D4', 'Repuestos electronicos',        12.50, '30x20x15 cm', 'Bogota',        'Cali',          'EN_BODEGA',       1,  2,  '2026-08-25 09:00:00', NULL),
(2,  'RPX-B2C3D4E5', 'Ropa deportiva',                 8.30, '40x30x20 cm', 'Medellin',      'Barranquilla',  'EN_BODEGA',       2,  4,  '2026-08-26 10:15:00', NULL),
(3,  'RPX-C3D4E5F6', 'Equipos de computo',             15.00, '50x40x30 cm', 'Bogota',       'Bucaramanga',   'EN_BODEGA',       3,  6,  '2026-08-27 11:30:00', NULL),
(4,  'RPX-D4E5F6A7', 'Material de oficina',             6.75, '35x25x20 cm', 'Cali',         'Pereira',       'EN_BODEGA',       4,  7,  '2026-08-28 08:45:00', NULL),
(5,  'RPX-E5F6A7B8', 'Herramientas',                   20.00, '45x35x25 cm', 'Barranquilla', 'Manizales',     'EN_BODEGA',       5,  8,  '2026-08-29 14:00:00', NULL),
(6,  'RPX-F6A7B8C9', 'Autopartes',                     25.40, '50x30x30 cm', 'Cartagena',    'Cucuta',        'ASIGNADO_A_RUTA', 6,  9,  '2026-08-20 09:00:00', NULL),
(7,  'RPX-A7B8C9D0', 'Electrodomesticos pequenos',     18.60, '40x40x35 cm', 'Bucaramanga',  'Santa Marta',   'ASIGNADO_A_RUTA', 7,  10, '2026-08-21 10:00:00', NULL),
(8,  'RPX-B8C9D0E1', 'Textiles',                       10.20, '35x30x20 cm', 'Pereira',      'Ibague',        'ASIGNADO_A_RUTA', 8,  11, '2026-08-22 11:00:00', NULL),
(9,  'RPX-C9D0E1F2', 'Insumos medicos',                 5.50, '25x20x15 cm', 'Manizales',    'Villavicencio', 'EN_TRANSITO',     9,  12, '2026-08-15 09:00:00', NULL),
(10, 'RPX-D0E1F2A3', 'Componentes electronicos',        9.80, '30x25x20 cm', 'Cucuta',       'Neiva',         'EN_TRANSITO',     10, 13, '2026-08-16 10:00:00', NULL),
(11, 'RPX-E1F2A3B4', 'Calzado',                        14.30, '40x30x25 cm', 'Santa Marta',  'Armenia',       'EN_TRANSITO',     11, 14, '2026-08-17 11:00:00', NULL),
(12, 'RPX-F2A3B4C5', 'Juguetes',                        7.90, '35x30x25 cm', 'Ibague',       'Popayan',       'EN_TRANSITO',     12, 15, '2026-08-18 12:00:00', NULL),
(13, 'RPX-A3B4C5D6', 'Documentos legales',              1.20, '30x20x5 cm',  'Villavicencio','Sincelejo',     'ENTREGADO',       13, 16, '2026-08-01 09:00:00', '2026-08-03 15:30:00'),
(14, 'RPX-B4C5D6E7', 'Libros',                         11.00, '35x25x20 cm', 'Neiva',        'Monteria',      'ENTREGADO',       14, 17, '2026-08-02 10:00:00', '2026-08-04 16:00:00'),
(15, 'RPX-C5D6E7F8', 'Accesorios de moda',               4.60, '25x20x15 cm', 'Armenia',     'Valledupar',    'ENTREGADO',       15, 18, '2026-08-03 08:30:00', '2026-08-05 14:45:00'),
(16, 'RPX-D6E7F8A9', 'Productos de aseo',               13.75, '40x30x30 cm', 'Popayan',     'Tunja',         'ENTREGADO',       16, 19, '2026-08-04 09:15:00', '2026-08-06 13:20:00'),
(17, 'RPX-E7F8A9B0', 'Alimentos no perecederos',        22.00, '50x40x30 cm', 'Sincelejo',   'Bogota',        'ENTREGADO',       17, 20, '2026-08-05 10:45:00', '2026-08-07 17:10:00'),
(18, 'RPX-F8A9B0C1', 'Repuestos industriales',          30.50, '55x45x35 cm', 'Monteria',    'Cali',          'ENTREGADO',       18, 1,  '2026-08-06 11:20:00', '2026-08-08 16:40:00'),
(19, 'RPX-A9B0C1D2', 'Muebles pequenos',                40.00, '60x50x40 cm', 'Valledupar',  'Medellin',      'DEVUELTO',        19, 3,  '2026-08-07 09:00:00', NULL),
(20, 'RPX-B0C1D2E3', 'Equipos de refrigeracion',        35.60, '55x45x40 cm', 'Tunja',       'Barranquilla',  'DEVUELTO',        20, 5,  '2026-08-08 10:30:00', NULL);

-- ------------------------------------------------------------
-- ruta (20 registros)
-- ------------------------------------------------------------
INSERT INTO ruta (id, vehiculo_id, conductor_id, fecha, estado, peso_total) VALUES
(1,  8,  8,  '2026-08-15 08:00:00', 'EN_CURSO',   15.30),
(2,  15, 15, '2026-08-17 08:00:00', 'EN_CURSO',   22.20),
(3,  2,  2,  '2026-08-22 07:00:00', 'PLANIFICADA', 25.40),
(4,  3,  3,  '2026-08-23 07:00:00', 'PLANIFICADA', 18.60),
(5,  6,  6,  '2026-08-24 07:00:00', 'PLANIFICADA', 10.20),
(6,  7,  7,  '2026-08-10 07:00:00', 'CANCELADA',    0.00),
(7,  9,  9,  '2026-08-01 07:30:00', 'COMPLETADA',   1.20),
(8,  10, 10, '2026-08-02 07:30:00', 'COMPLETADA',  11.00),
(9,  12, 12, '2026-08-03 07:30:00', 'COMPLETADA',   4.60),
(10, 13, 13, '2026-08-04 07:30:00', 'COMPLETADA',  13.75),
(11, 14, 14, '2026-08-05 07:30:00', 'COMPLETADA',  22.00),
(12, 16, 16, '2026-08-06 07:30:00', 'COMPLETADA',  30.50),
(13, 17, 17, '2026-08-07 07:30:00', 'COMPLETADA',  40.00),
(14, 18, 19, '2026-08-08 07:30:00', 'COMPLETADA',  35.60),
(15, 1,  1,  '2026-07-15 07:00:00', 'COMPLETADA',  45.00),
(16, 2,  4,  '2026-07-16 07:00:00', 'COMPLETADA',  38.20),
(17, 3,  5,  '2026-07-18 07:00:00', 'COMPLETADA',  27.80),
(18, 4,  11, '2026-07-20 07:00:00', 'COMPLETADA',  19.40),
(19, 11, 2,  '2026-07-22 07:00:00', 'COMPLETADA',  52.10),
(20, 20, 3,  '2026-07-25 07:00:00', 'COMPLETADA',  33.00);

-- ------------------------------------------------------------
-- ruta_paquete (relacion N:M ruta <-> paquete)
-- ------------------------------------------------------------
INSERT INTO ruta_paquete (ruta_id, paquete_id) VALUES
(1, 9), (1, 10),
(2, 11), (2, 12),
(3, 6),
(4, 7),
(5, 8),
(7, 13),
(8, 14),
(9, 15),
(10, 16),
(11, 17),
(12, 18),
(13, 19),
(14, 20);

-- ------------------------------------------------------------
-- mantenimiento (20 registros)
-- ------------------------------------------------------------
INSERT INTO mantenimiento (id, vehiculo_id, fecha, descripcion, costo, kilometraje) VALUES
(1,  1,  '2026-06-01 09:00:00', 'Cambio de aceite y filtros',            180000.00, 45000),
(2,  2,  '2026-06-03 09:00:00', 'Revision de frenos',                    220000.00, 52000),
(3,  3,  '2026-06-05 09:00:00', 'Alineacion y balanceo',                 150000.00, 38000),
(4,  4,  '2026-06-07 09:00:00', 'Cambio de llantas',                    1200000.00, 61000),
(5,  4,  '2026-08-30 09:00:00', 'Revision general de motor',             950000.00, 63500),
(6,  5,  '2026-06-10 09:00:00', 'Cambio de bateria',                     320000.00, 29000),
(7,  6,  '2026-06-12 09:00:00', 'Revision de suspension',                410000.00, 47000),
(8,  7,  '2026-06-14 09:00:00', 'Cambio de aceite y filtros',            175000.00, 55000),
(9,  8,  '2026-06-16 09:00:00', 'Mantenimiento preventivo 20.000 km',    280000.00, 20000),
(10, 9,  '2026-06-18 09:00:00', 'Cambio de correas',                     260000.00, 71000),
(11, 10, '2026-06-20 09:00:00', 'Revision de frenos',                    230000.00, 34000),
(12, 11, '2026-08-28 09:00:00', 'Reparacion de transmision',            2100000.00, 98000),
(13, 12, '2026-06-24 09:00:00', 'Cambio de aceite y filtros',            190000.00, 41000),
(14, 13, '2026-06-26 09:00:00', 'Alineacion y balanceo',                 160000.00, 25000),
(15, 14, '2026-06-28 09:00:00', 'Revision electrica',                    340000.00, 33000),
(16, 15, '2026-07-01 09:00:00', 'Mantenimiento preventivo 30.000 km',    300000.00, 30000),
(17, 16, '2026-07-03 09:00:00', 'Cambio de llantas',                    1100000.00, 58000),
(18, 17, '2026-07-05 09:00:00', 'Revision de frenos',                    210000.00, 44000),
(19, 19, '2026-07-08 09:00:00', 'Diagnostico general',                   150000.00, 67000),
(20, 20, '2026-08-25 09:00:00', 'Cambio de aceite y filtros',            185000.00, 39500);

-- ------------------------------------------------------------
-- auditoria (registros de ejemplo; en produccion se generan
-- automaticamente desde AuditLogger/AuditoriaService)
-- ------------------------------------------------------------
INSERT INTO auditoria (fecha, usuario, accion, tabla_afectada, registro_id, detalle, ip) VALUES
('2026-08-01 07:30:00', 'system', 'CREATE', 'RUTA',        '7',  'Se creo ruta con 1 paquetes',                   'localhost'),
('2026-08-01 07:31:00', 'system', 'UPDATE', 'RUTA',        '7',  'Se inicio ruta con ID: 7',                      'localhost'),
('2026-08-03 15:30:00', 'system', 'UPDATE', 'PAQUETE',     'RPX-A3B4C5D6', 'Se actualizo estado del paquete a: Entregado', 'localhost'),
('2026-08-03 15:31:00', 'system', 'UPDATE', 'RUTA',        '7',  'Se completo ruta con ID: 7',                    'localhost'),
('2026-08-15 08:00:00', 'system', 'CREATE', 'RUTA',        '1',  'Se creo ruta con 2 paquetes',                   'localhost'),
('2026-08-15 08:01:00', 'system', 'UPDATE', 'RUTA',        '1',  'Se inicio ruta con ID: 1',                      'localhost'),
('2026-08-17 08:00:00', 'system', 'CREATE', 'RUTA',        '2',  'Se creo ruta con 2 paquetes',                   'localhost'),
('2026-08-17 08:01:00', 'system', 'UPDATE', 'RUTA',        '2',  'Se inicio ruta con ID: 2',                      'localhost'),
('2026-08-20 09:00:00', 'system', 'CREATE', 'PAQUETE',     'RPX-F6A7B8C9', 'Se registro paquete con tracking ID: RPX-F6A7B8C9', 'localhost'),
('2026-08-22 07:00:00', 'system', 'CREATE', 'RUTA',        '3',  'Se creo ruta con 1 paquetes',                   'localhost'),
('2026-08-25 09:00:00', 'system', 'CREATE', 'VEHICULO',    '20', 'Se cambio el estado del vehiculo ABC-120 a: En Mantenimiento', 'localhost'),
('2026-08-28 09:00:00', 'system', 'CREATE', 'MANTENIMIENTO','12','Se programo mantenimiento para vehiculo ABC-111', 'localhost'),
('2026-08-29 14:00:00', 'system', 'CREATE', 'PAQUETE',     'RPX-E5F6A7B8', 'Se registro paquete con tracking ID: RPX-E5F6A7B8', 'localhost'),
('2026-08-30 09:00:00', 'system', 'CREATE', 'MANTENIMIENTO','5',  'Se programo mantenimiento para vehiculo ABC-104', 'localhost'),
('2026-09-01 10:00:00', 'system', 'CREATE', 'CONDUCTOR',   '20', 'Se asigno vehiculo ABC-109 al conductor 1020234567', 'localhost');
