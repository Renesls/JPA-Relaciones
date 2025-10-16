package com.example.empleados;

import com.example.empleados.model.Autor;
import com.example.empleados.model.Libro;
import com.example.empleados.model.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    
    private static final Scanner scanner = new Scanner(System.in);
    private static EntityManager em;
    
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BibliotecaPU");
        em = emf.createEntityManager();
        
        try {
            insertarDatosIniciales();
            mostrarMenu();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
            em.close();
            emf.close();
        }
    }
    
    private static void mostrarMenu() {
        int opcion;
        
        do {
            System.out.println("\n========== SISTEMA DE BIBLIOTECA ==========");
            System.out.println("1. Registrar autor");
            System.out.println("2. Registrar libro");
            System.out.println("3. Registrar categoria");
            System.out.println("4. Asignar categoria a libro");
            System.out.println("5. Listar libros");
            System.out.println("6. Listar autores");
            System.out.println("7. Listar categorias");
            System.out.println("8. Buscar libros por autor");
            System.out.println("0. Salir");
            System.out.print("\nOpcion: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1 -> registrarAutor();
                    case 2 -> registrarLibro();
                    case 3 -> registrarCategoria();
                    case 4 -> asignarCategoriaALibro();
                    case 5 -> listarLibros();
                    case 6 -> listarAutores();
                    case 7 -> listarCategorias();
                    case 8 -> buscarLibrosPorAutor();
                    case 0 -> System.out.println("\nHasta luego");
                    default -> System.out.println("\nOpcion invalida");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nIngrese un numero valido");
                opcion = -1;
            }
            
        } while (opcion != 0);
    }
    
    private static void registrarAutor() {
        System.out.println("\n--- Registrar Autor ---");
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Nacionalidad: ");
        String nacionalidad = scanner.nextLine();
        
        System.out.print("Fecha nacimiento (YYYY-MM-DD): ");
        String fecha = scanner.nextLine();
        LocalDate fechaNacimiento = LocalDate.parse(fecha);
        
        em.getTransaction().begin();
        Autor autor = new Autor(nombre, nacionalidad, fechaNacimiento);
        em.persist(autor);
        em.getTransaction().commit();
        
        System.out.println("Autor registrado con ID: " + autor.getId());
    }
    
    private static void registrarLibro() {
        System.out.println("\n--- Registrar Libro ---");
        
        listarAutores();
        
        System.out.print("\nID del autor: ");
        Long autorId = Long.parseLong(scanner.nextLine());
        
        Autor autor = em.find(Autor.class, autorId);
        if (autor == null) {
            System.out.println("Autor no encontrado");
            return;
        }
        
        System.out.print("Titulo: ");
        String titulo = scanner.nextLine();
        
        System.out.print("Año de publicacion: ");
        Integer anio = Integer.parseInt(scanner.nextLine());
        
        em.getTransaction().begin();
        Libro libro = new Libro(titulo, anio);
        libro.setAutor(autor);
        em.persist(libro);
        em.getTransaction().commit();
        
        System.out.println("Libro registrado con ID: " + libro.getId());
    }
    
    private static void registrarCategoria() {
        System.out.println("\n--- Registrar Categoria ---");
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        
        em.getTransaction().begin();
        Categoria categoria = new Categoria(nombre);
        em.persist(categoria);
        em.getTransaction().commit();
        
        System.out.println("Categoria registrada con ID: " + categoria.getId());
    }
    
    private static void asignarCategoriaALibro() {
        System.out.println("\n--- Asignar Categoria a Libro ---");
        
        listarLibros();
        System.out.print("\nID del libro: ");
        Long libroId = Long.parseLong(scanner.nextLine());
        
        listarCategorias();
        System.out.print("\nID de la categoria: ");
        Long categoriaId = Long.parseLong(scanner.nextLine());
        
        em.getTransaction().begin();
        Libro libro = em.find(Libro.class, libroId);
        Categoria categoria = em.find(Categoria.class, categoriaId);
        
        if (libro != null && categoria != null) {
            if (!libro.getCategorias().contains(categoria)) {
                libro.addCategoria(categoria);
                em.merge(libro);
                em.getTransaction().commit();
                System.out.println("Categoria asignada");
            } else {
                em.getTransaction().rollback();
                System.out.println("El libro ya tiene esta categoria");
            }
        } else {
            em.getTransaction().rollback();
            System.out.println("Libro o categoria no encontrados");
        }
    }
    
    private static void listarLibros() {
        TypedQuery<Libro> query = em.createQuery(
            "SELECT DISTINCT l FROM Libro l " +
            "LEFT JOIN FETCH l.autor " +
            "LEFT JOIN FETCH l.categorias", 
            Libro.class
        );
        
        List<Libro> libros = query.getResultList();
        
        System.out.println("\n========== Libros ==========");
        
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados");
            return;
        }
        
        for (Libro libro : libros) {
            System.out.println("\nID: " + libro.getId());
            System.out.println("Titulo: " + libro.getTitulo());
            System.out.println("Año: " + libro.getAnioPublicacion());
            System.out.println("Autor: " + libro.getAutor().getNombre());
            System.out.print("Categorias: ");
            
            if (libro.getCategorias().isEmpty()) {
                System.out.println("Sin categorias");
            } else {
                List<String> nombresCategorias = libro.getCategorias().stream()
                    .map(Categoria::getNombre)
                    .toList();
                System.out.println(String.join(", ", nombresCategorias));
            }
        }
        
        System.out.println("\nTotal: " + libros.size());
    }
    
    private static void listarAutores() {
        TypedQuery<Autor> query = em.createQuery(
            "SELECT a FROM Autor a ORDER BY a.nombre", 
            Autor.class
        );
        
        List<Autor> autores = query.getResultList();
        
        System.out.println("\n========== Autores ==========");
        
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados");
            return;
        }
        
        for (Autor autor : autores) {
            System.out.println("ID: " + autor.getId() + " - " + autor.getNombre() + 
                             " (" + autor.getNacionalidad() + ")");
        }
        
        System.out.println("\nTotal: " + autores.size());
    }
    
    private static void listarCategorias() {
        TypedQuery<Categoria> query = em.createQuery(
            "SELECT c FROM Categoria c ORDER BY c.nombre", 
            Categoria.class
        );
        
        List<Categoria> categorias = query.getResultList();
        
        System.out.println("\n========== Categorias ==========");
        
        if (categorias.isEmpty()) {
            System.out.println("No hay categorias registradas");
            return;
        }
        
        for (Categoria categoria : categorias) {
            System.out.println("ID: " + categoria.getId() + " - " + categoria.getNombre());
        }
        
        System.out.println("\nTotal: " + categorias.size());
    }
    
    private static void buscarLibrosPorAutor() {
        System.out.println("\n--- Buscar Libros por Autor ---");
        
        listarAutores();
        
        System.out.print("\nID del autor: ");
        Long autorId = Long.parseLong(scanner.nextLine());
        
        TypedQuery<Libro> query = em.createQuery(
            "SELECT l FROM Libro l " +
            "LEFT JOIN FETCH l.categorias " +
            "WHERE l.autor.id = :autorId", 
            Libro.class
        );
        query.setParameter("autorId", autorId);
        
        List<Libro> libros = query.getResultList();
        
        if (libros.isEmpty()) {
            System.out.println("\nNo se encontraron libros");
            return;
        }
        
        Autor autor = em.find(Autor.class, autorId);
        System.out.println("\nLibros de " + autor.getNombre() + ":");
        
        for (Libro libro : libros) {
            System.out.println("\n- " + libro.getTitulo() + " (" + libro.getAnioPublicacion() + ")");
            if (!libro.getCategorias().isEmpty()) {
                System.out.print("  Categorias: ");
                List<String> nombresCategorias = libro.getCategorias().stream()
                    .map(Categoria::getNombre)
                    .toList();
                System.out.println(String.join(", ", nombresCategorias));
            }
        }
    }
    
    private static void insertarDatosIniciales() {
        em.getTransaction().begin();
        
        Autor autor1 = new Autor("Gabriel Garcia Marquez", "Colombiana", LocalDate.of(1927, 3, 6));
        Autor autor2 = new Autor("J.K. Rowling", "Britanica", LocalDate.of(1965, 7, 31));
        
        Categoria ficcion = new Categoria("Ficcion");
        Categoria educativo = new Categoria("Educativo");
        Categoria ciencia = new Categoria("Ciencia");
        
        em.persist(ficcion);
        em.persist(educativo);
        em.persist(ciencia);
        
        Libro libro1 = new Libro("Cien Años de Soledad", 1967);
        libro1.setAutor(autor1);
        libro1.addCategoria(ficcion);
        
        Libro libro2 = new Libro("El Amor en los Tiempos del Colera", 1985);
        libro2.setAutor(autor1);
        libro2.addCategoria(ficcion);
        
        Libro libro3 = new Libro("Harry Potter y la Piedra Filosofal", 1997);
        libro3.setAutor(autor2);
        libro3.addCategoria(ficcion);
        libro3.addCategoria(educativo);
        
        em.persist(autor1);
        em.persist(autor2);
        
        em.getTransaction().commit();
        
        System.out.println("Datos iniciales cargados\n");
    }
}
