CREATE DATABASE IF NOT EXISTS crud;
USE crud;

-- Crear la tabla persona sin email
CREATE TABLE IF NOT EXISTS personas (
                                       idpersona INT AUTO_INCREMENT PRIMARY KEY,
                                       nombre VARCHAR(30) NOT NULL,
                                       apellidos VARCHAR(30) NOT NULL,
                                       experiencia INT NOT NULL,
                                       fechaNac DATE NOT NULL
);

-- Insertar registros de ejemplo
INSERT INTO personas (nombre, apellidos, experiencia, fechaNac) VALUES
                                                                   ('Alvaro', 'Wau', 5, '1993-08-21'),
                                                                   ('Ana', 'García', 3, '1992-07-20'),
                                                                   ('Luis', 'Martínez', 4, '1988-02-10'),
                                                                   ('María', 'López', 6, '1985-11-30'),
                                                                   ('Pedro', 'Hernández', 2, '1995-09-01'),
                                                                   ('Laura', 'Ramírez', 7, '1993-04-25'),
                                                                   ('Carlos', 'Torres', 1, '1998-12-12'),
                                                                   ('Sofía', 'Cruz', 8, '1989-03-18'),
                                                                   ('Javier', 'Morales', 2, '1996-08-30'),
                                                                   ('Lucía', 'González', 3, '1994-10-05');
