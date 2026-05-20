# Registro de Mascotas - Clínica Veterinaria

Aplicación de escritorio en Java para gestionar el registro de mascotas en una clínica veterinaria. Permite añadir, buscar, importar y exportar datos de mascotas vinculadas al DNI de su propietario.

## Funcionalidades

- **Añadir mascota**: registra una nueva mascota con nombre, especie, propietario, DNI y edad.
- **Buscar mascota**: consulta todas las mascotas asociadas a un DNI.
- **Exportar a .txt**: guarda los resultados de una búsqueda en un archivo de texto.
- **Importar desde .txt**: carga los datos de una mascota desde un archivo de texto.

## Tecnologías

- Java (Swing para la interfaz gráfica)
- MySQL (base de datos local)
- MySQL Connector/J 9.7.0

## Estructura del proyecto

```
vet-clinica/
├── src/
│   ├── App.java          # Punto de entrada
│   ├── Window.java       # Interfaz gráfica principal
│   ├── Pet.java          # Modelo de mascota
│   └── DBConection.java  # Conexión y operaciones con la BD
├── lib/
│   └── mysql-connector-j-9.7.0.jar
└── bin/                  # Archivos compilados
```

## Requisitos previos

- JDK 11 o superior
- MySQL corriendo en `localhost:3306`
- Base de datos `clinicaVet` con una tabla `mascotas`:

```sql
CREATE DATABASE clinicaVet;

USE clinicaVet;

CREATE TABLE mascotas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    especie VARCHAR(100),
    propietario VARCHAR(100),
    dni VARCHAR(20),
    edad INT
);
```

## Configuración de la base de datos

Las credenciales están definidas en `src/DBConection.java`:

```java
URL:      jdbc:mysql://localhost:3306/clinicaVet
Usuario:  root
Password: abcd1234
```

Modifícalas si tu entorno tiene configuración distinta.

## Ejecución

Compila y ejecuta desde VS Code con la extensión **Extension Pack for Java**, o desde la terminal:

```bash
javac -cp lib/mysql-connector-j-9.7.0.jar -d bin src/*.java
java -cp bin;lib/mysql-connector-j-9.7.0.jar App
```
