package com.desafio_literalura.literalura.principal;

import com.desafio_literalura.literalura.controller.Funcionalidades;
import com.desafio_literalura.literalura.model.*;
import com.desafio_literalura.literalura.repositoy.AutorRepository;
import com.desafio_literalura.literalura.repositoy.LibroRepository;
import com.desafio_literalura.literalura.services.ConsumoApi;
import com.desafio_literalura.literalura.services.ConvierteDatos;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private final String URL_BASE = "http://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository repositorio;
    private AutorRepository repositorioAutor;
    private List<Libro> libros;
    private List<Autor> autor;

    public Principal(){}

    public Principal(LibroRepository repository, AutorRepository repositoryAutor) {
        this.repositorio = repository;
        this.repositorioAutor = repositoryAutor;
    }

    public void iniciarApp(){
        Funcionalidades obtenerRecurso = new Funcionalidades();
        int opcion;

        while (true){
            obtenerRecurso.menuOpciones();

            try {
                opcion = teclado.nextInt();

                var url = obtenerRecurso.url(opcion,URL_BASE);

                var json = consumoApi.obtenerDatos(url);

                teclado.nextLine();

                var datosLibro = conversor.obtenerDatos(json, ResultadoBusqueda.class);

                if (opcion == 1){
                    var tituloBuscado = obtenerRecurso.getParametroBusqueda();

                    Optional<DatosLibro> libroBuscado = datosLibro.libros().stream()
                            .filter(l -> l.titulo().toUpperCase().contains(tituloBuscado.toUpperCase()))
                            .findFirst();
                    if (libroBuscado.isPresent()){
                        DatosLibro datosLibros = libroBuscado.get();
                        DatosAutor datosAutor = datosLibros.datosAutor().get(0);
                        Autor autor = repositorioAutor.findByNombre(datosAutor.nombre());

                        if (autor == null) {
                            autor = new Autor(datosAutor);
                            repositorioAutor.save(autor);
                        }

                        Libro libro = repositorio.findByTituloContainsIgnoreCase(datosLibros.titulo());


                        if (libro == null) {
                            System.out.println("¡Libro encontrado!");
                            libro = new Libro(datosLibros, autor);
                            repositorio.save(libro);
                            System.out.println(libro);
                        }else {
                            System.out.println("Libro ya esta Registrado");
                        }

                    }else {
                        System.out.println("Libro no encontrado");
                    }
                }

                if (opcion == 2){
                    LibrosRegistrados();
                }

                if (opcion == 3){
                    AutoresRegistrados();
                }

                if (opcion == 4) {
                    autorVivoEnAnio();

                }

                if (opcion == 5){
                    librosPorLenguaje();
                }

                if (opcion == 0 || opcion < 0 || opcion > 5){
                    System.out.println("Gracias por haber usado la aplicación! Hasta la proxima.");
                    break;
                }

            } catch (InputMismatchException e){
                System.out.println("Error al ingresar el valor a elegir: " + e);
                teclado.next();
            }


        }

    }

    private void LibrosRegistrados(){
        libros = repositorio.findAll();
        libros.forEach(System.out::println);
    }

    private void AutoresRegistrados(){
        autor = repositorioAutor.findAll();
        autor.forEach(System.out::println);
    }

    private void librosPorLenguaje(){
        System.out.println("""
        --------------------------------
        Ingrese numero de idioma deseado
        
        1- Ingles
        2- Español
        3- Portuges
        4- Italiano
        
        -------------------------------
        """);
        admitirSoloNumeros();
        var  numero = teclado.nextInt();
        switch (numero) {
            case 1:
                buscadorDeIdioma("en");
                break;
            case 2:
                buscadorDeIdioma("es");
                break;
            case 3:
                buscadorDeIdioma("pt");
                break;
            case 4:
                buscadorDeIdioma("it");
                break;
            default:
                System.out.println("Opción inválida");
        }
    }

    private void admitirSoloNumeros() {
        while (!teclado.hasNextInt()) {
            System.out.println("ingrese solo numeros");
            teclado.next();
        }

    }

    private void buscadorDeIdioma(String idioma) {
        try {
            libros = repositorio.findByIdiomas(idioma);

            if (libros == null) {
                System.out.println("No hay Libros registrados");
            } else {
                libros.forEach(System.out::println);
            }
        }catch (Exception e){
            System.out.println("Error en la busqueda");
        }

    }

    private void autorVivoEnAnio() {
        System.out.println("Ingrese año");
        admitirSoloNumeros();
        var año = teclado.nextInt();
        autor = repositorioAutor.autoresVivosEnDeterminadoAño(año);
        if (autor == null) {
            System.out.println("No hay registro de autores en ese año");
        } else {
            autor.forEach(System.out::println);
        }
    }

    private void LibrosPorLenguajes(){
        System.out.println("""
        --------------------------------
        Ingrese numero de idioma deseado
        
        1- Ingles
        2- Español
        3- Portuges
        4- Italiano
        
        -------------------------------
        """);
        admitirSoloNumeros();
        var  numero = teclado.nextInt();
        switch (numero) {
            case 1:
                buscadorDeIdioma("en");
                break;
            case 2:
                buscadorDeIdioma("es");
                break;
            case 3:
                buscadorDeIdioma("pt");
                break;
            case 4:
                buscadorDeIdioma("it");
                break;
            default:
                System.out.println("Opción inválida");
        }
    }



}
