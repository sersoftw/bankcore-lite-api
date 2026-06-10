INSERT INTO clientes (id, nombre, apellidos, dni, email, telefono, fecha_alta, activo) VALUES
(1, 'Sergio', 'Bernal Galvez', '12345678A', 'sergio@example.com', '600111222', CURRENT_TIMESTAMP, true),
(2, 'Laura', 'Garcia Perez', '87654321B', 'laura@example.com', '600333444', CURRENT_TIMESTAMP, true);

INSERT INTO cuentas (id, iban, cliente_id, saldo, tipo_cuenta, estado, fecha_creacion) VALUES
(1, 'ES7620770024003102575766', 1, 2500.00, 'CORRIENTE', 'ACTIVA', CURRENT_TIMESTAMP),
(2, 'ES1200491500051234567892', 2, 8000.00, 'AHORRO', 'ACTIVA', CURRENT_TIMESTAMP);
