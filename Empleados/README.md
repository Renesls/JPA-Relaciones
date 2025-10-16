# Sistema de Gestión de Biblioteca 📚

Sistema de gestión de biblioteca desarrollado con JPA/Hibernate que permite registrar y consultar información sobre libros, autores y categorías.

## Descripción del Proyecto

Este sistema modela las siguientes relaciones:
- **Autor → Libro**: Un autor puede escribir varios libros (@OneToMany)
- **Libro → Autor**: Cada libro tiene un solo autor (@ManyToOne)
- **Libro ↔ Categoría**: Relación muchos a muchos - los libros pueden tener varias categorías y viceversa (@ManyToMany)

## Entidades

### Autor
- `id`: Identificador único
- `nombre`: Nombre del autor
- `nacionalidad`: Nacionalidad del autor
- `fechaNacimiento`: Fecha de nacimiento

### Libro
- `id`: Identificador único
- `titulo`: Título del libro
- `anioPublicacion`: Año de publicación
- `autor`: Referencia al autor (relación @ManyToOne)
- `categorias`: Lista de categorías (relación @ManyToMany)

### Categoria
- `id`: Identificador único
- `nombre`: Nombre de la categoría
- `libros`: Lista de libros asociados

## Funcionalidades del Menú

1. **Registrar nuevo autor**: Permite ingresar autores con su información personal
2. **Registrar nuevo libro**: Permite crear libros y asociarlos a un autor
3. **Registrar nueva categoría**: Crea nuevas categorías para clasificar libros
4. **Asignar categoría a libro**: Relaciona libros con categorías
5. **Listar todos los libros**: Muestra todos los libros con su autor y categorías
6. **Listar todos los autores**: Muestra el catálogo completo de autores
7. **Listar todas las categorías**: Muestra todas las categorías disponibles
8. **Buscar libros por autor**: Filtra libros de un autor específico

## Tecnologías Utilizadas

- Java 24
- Jakarta Persistence API (JPA) 3.2.0
- Hibernate 6.6.0.Final
- Base de datos H2 (en memoria)
- Maven para gestión de dependencias

## Cómo Ejecutar

1. Asegúrate de tener Java 24 y Maven instalados
2. Clona el repositorio
3. Navega a la carpeta del proyecto:
   ```bash
   cd Empleados
   ```
4. Compila y ejecuta:
   ```bash
   mvn clean compile exec:java -Dexec.mainClass="com.example.empleados.Main"
   ```
   
   O desde tu IDE, ejecuta la clase `Main.java`

## Datos Iniciales

El sistema carga automáticamente los siguientes datos de ejemplo:

**Autores:**
- Gabriel García Márquez (Colombiana)
- J.K. Rowling (Británica)

**Libros:**
- Cien Años de Soledad (1967) - Gabriel García Márquez
- El Amor en los Tiempos del Cólera (1985) - Gabriel García Márquez
- Harry Potter y la Piedra Filosofal (1997) - J.K. Rowling

**Categorías:**
- Ficción
- Educativo
- Ciencia

## Estructura del Proyecto

```
Empleados/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/empleados/
│       │       ├── Main.java
│       │       └── model/
│       │           ├── Autor.java
│       │           ├── Libro.java
│       │           └── Categoria.java
│       └── resources/
│           └── META-INF/
│               └── persistence.xml
├── pom.xml
└── README.md
```

## Configuración de Persistencia

El archivo `persistence.xml` está configurado para usar:
- **Persistence Unit**: BibliotecaPU
- **Base de datos**: H2 en memoria
- **Estrategia**: drop-and-create (se reinicia en cada ejecución)
- **Dialecto**: H2Dialect

## Autor

Proyecto desarrollado como caso de estudio para aprender JPA y relaciones entre entidades.
