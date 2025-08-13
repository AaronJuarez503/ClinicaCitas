# Script de las tablas

CREATE TABLE clinica (
    id_clinica INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(20)
);

CREATE TABLE medico (
    id_medico INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100),
    telefono VARCHAR(20),
    id_clinica INT,
    FOREIGN KEY (id_clinica) REFERENCES clinica(id_clinica)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

CREATE TABLE paciente (
    id_paciente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE,
    genero ENUM('Masculino', 'Femenino', 'Otro'),
    telefono VARCHAR(20),
    email VARCHAR(100)
);

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
    contraseña_hash VARCHAR(255) NOT NULL,
    rol ENUM('medico', 'paciente') NOT NULL,
    id_medico INT DEFAULT NULL,
    id_paciente INT DEFAULT NULL,
    FOREIGN KEY (id_medico) REFERENCES medico(id_medico)
        ON DELETE SET NULL,
    FOREIGN KEY (id_paciente) REFERENCES paciente(id_paciente)
        ON DELETE SET NULL
);

CREATE TABLE cita (
    id_cita INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente INT NOT NULL,
    id_medico INT NOT NULL,
    fecha DATETIME NOT NULL,
    motivo VARCHAR(255),
    estado ENUM('Programada', 'Cancelada', 'Completada') DEFAULT 'Programada',
    FOREIGN KEY (id_paciente) REFERENCES paciente(id_paciente)
        ON DELETE CASCADE,
    FOREIGN KEY (id_medico) REFERENCES medico(id_medico)
        ON DELETE CASCADE
);