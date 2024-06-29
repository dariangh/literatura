package com.challenge.literatura.principal;

import com.challenge.literatura.model.*;
import com.challenge.literatura.repository.AutorRepository;
import com.challenge.literatura.repository.LibroRepository;
import com.challenge.literatura.service.ConsumoAPI;
import com.challenge.literatura.service.ConvierteDatos;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
//    private final String API_KEY = "&apikey=c35f9683";
    private ConvierteDatos conversor = new ConvierteDatos();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private List<DatosAutor> datosAutor = new ArrayList<>();
    private LibroRepository repositorio;
    private AutorRepository autorsRepository;

    private List<Libro> libros;
    private List<Autor> autores;
    private Optional<Libro> libroBuscado;
    private Optional<Autor> autorBuscado;

    public Principal(LibroRepository repository, AutorRepository autorRepository) {
        this.repositorio = repository;
        this.autorsRepository=autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    
                    1 - Agregar Libro a la BD
                    2 - Buscar Libro por titulo
                    3 - Listar libros Registrados
                    4 - Listar Autores Registrados
                    5 - Listar Autores vivos en un determinado año
                    6 - Listar libros por idioma 
                    7 - Listar Top 5 libros mas descargados 
                    8 - Buscar Autores por Nombre
                    
                    0 - Salir
                    
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    buscarLibroEnBD();
                    break;
                case 3:
                    listarLibrosRegistrados();
                    break;
                case 4:
                    buscarAutoresRegistrados();
                    break;
                case 5:
                    isliveAuthor();
                    break;
                case 6:
                    librosPorIdioma();
                    break;
                case 7:
                    top5LibrosDescargados();
                    break;
                case 8:
                    buscarAutor();
                    break;
//                case 9:
//                    buscarTop5Episodios();
//                    break;
//                case 0:
//                    System.out.println("Cerrando la aplicación...");
//                    break;
//                default:
//                    System.out.println("Opción inválida");
            }
        }

    }

    private DatosLibro getDatoslibro() {
        System.out.println("Escribe el nombre del Libro que desea agregar");
        var nombreLibro = teclado.nextLine();
        try {
            String url = URL_BASE + nombreLibro.replace(" ", "+");
            System.out.println(url);

            var json = consumoApi.obtenerDatos(url);
//            System.out.println(json);

            RespuestaApi respuestaApi = conversor.obtenerDatos(json, RespuestaApi.class);
            if (respuestaApi.results().isEmpty()) {
                System.out.println("No se encontraron resultados para el libro especificado.");
                return null;
            }else {
                System.out.println("\nLibro Encontrado");
                DatosLibro datos = respuestaApi.results().get(0);
                TextoDatosLibros(datos);
                return datos;
            }

        } catch (Exception e) {
            System.err.println("Ocurrió un error inesperado: " + e.getMessage());
        }

        return null;

    }

    private void buscarLibro() {
        DatosLibro datos = getDatoslibro();
        
        DatosAutor authorData = datos != null ? datos.autores().get(0) : null;

        Autor autor = autorsRepository.findByNombreContainsIgnoreCase(authorData.nombre());


        if (autor!=null) {
            System.out.println("**** El autor ya existe");

        }
        else{
            autor = new Autor(authorData);
            autorsRepository.save(autor);
        }
        Libro libroExist = repositorio.findFirstByTitulo(datos.titulo());
        if(libroExist!=null){
            System.out.println("\n*****Libro ya existente****");
            System.out.println(libroExist.toString());
        }else{
            Libro libro = new Libro(datos, autor);
            libro.getIdioma().replace("[", "").replace("]", "");
            repositorio.save(libro);
            System.out.println("\nDatos agregados exitosamente\n");
//            System.out.println(datos.titulo());
            TextoDatosLibros(datos);
        }

    }

    private void TextoDatosLibros(DatosLibro datos) {
        System.out.println("\nTitulo: " + datos.titulo());
        System.out.println("Autor: " + datos.autores().stream().map(DatosAutor::nombre).collect(Collectors.joining(", ")));
        System.out.println("Idioma: " + datos.idioma().toString().replace("[", "").replace("]", ""));
        System.out.println("Descargas: " + datos.numeroDescargas());
        System.out.println("\n");
    }

    private void buscarLibroEnBD(){

        System.out.println("Escribe el nombre del libro de la cual desea buscar");
        var nombreLibro = teclado.nextLine();
        libroBuscado = repositorio.findByTituloContainsIgnoreCase(nombreLibro);
        if(libroBuscado.isPresent()){
            System.out.println("\nEl libro Encontrado es\n "+libroBuscado.get());
        }else {
            System.out.println("\nLibro no encontrado");
        }
    }
    private void listarLibrosRegistrados(){
        libros = repositorio.findAll();
        libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(libro -> {
                    System.out.println("\n*****Libros Registrados en la BD *****");
                    System.out.println(libro);
                    System.out.println(); // Línea en blanco entre libros
                });


    }
    private void buscarAutoresRegistrados(){
         autores = autorsRepository.findAll();
        autores.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(autor -> {
                    System.out.println("\n*****Autores Registrados en la BD *****");
                    System.out.println(autor);
                    System.out.println(); // Línea en blanco entre libros
                });
    }
    private void isliveAuthor(){
        System.out.println("Ingrese el AÑO que desea saber que autores estaban vivos");
        if(teclado.hasNextInt()){
            var year= teclado.nextInt();

            autores=autorsRepository.obtenerAutoresVivosPorYear(year);
            if (autores.isEmpty()){
                System.out.println("\nno hubo autores registrados en la bd vivos en ese año");

            }else{
                System.out.println("\n***** Autores vivos  *****");

                autores.stream()
                        .sorted(Comparator.comparing(Autor::getNombre))
                        .forEach(autor -> {
                            System.out.println(autor);
                            System.out.println(); // Línea en blanco entre libros
                        });
            }
        }
    }
    private void librosPorIdioma(){
        System.out.println("selecciona en que idioma para buscar libros");
        var menu = """
                    es - Español
                    en - Ingles
                    fr - Frances
                    pr - Portugues
                    
                    """;
        System.out.println(menu);
        var idioma = teclado.nextLine().toLowerCase();

        System.out.println(idioma);
        if (idioma.equals("es") || idioma.equals("en") || idioma.equals("fr") || idioma.equals("pr")) {

            libros = repositorio.findByIdioma(idioma);
            if (libros.isEmpty()) {
                System.out.println("No se encontraron libros en el idioma seleccionado.");
            } else {
                libros.forEach(libro -> System.out.println(libro.toString()));
            }

        } else {
            System.out.println("Idioma no válido. Mostrando todos los libros.");
        }
    }
    private void top5LibrosDescargados(){
        List<Libro> topLibros=repositorio.findTop5ByOrderByNumeroDescargasDesc();
        topLibros.forEach(s-> System.out.println("\nLibro: "+s.getTitulo() + "\nDescargas: "+s.getNumeroDescargas() + "\nIdioma: "+s.getIdioma()));
    }

    private void buscarAutor(){
        System.out.println("Escribe el nombre del del autor el cual desea buscar");
        var nombreAutor = teclado.nextLine();
        Autor autor = autorsRepository.findByNombreContainsIgnoreCase(nombreAutor);
        if(autor==null){
            System.out.println("\n*** Autor no encontrado ***");

        }else
            System.out.println(autor.toString());
//            autores.stream()
//                .sorted(Comparator.comparing(Autor::getNombre))
//                .forEach(autores -> {
//                    System.out.println("\n*****Autores Registrados en la BD *****");
//                    System.out.println(autor.getLibro());
//                    System.out.println(); // Línea en blanco entre libros
//                });
    }

}
