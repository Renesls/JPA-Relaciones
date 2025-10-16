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

