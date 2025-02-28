package com.unir.back_end_ms_books_catalogue.stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

public class BookStepDefinitions {

    private final TestRestTemplate restTemplate;
    private ResponseEntity<String> response;
    private Long bookId = 1L; // ID de un libro existente con stock
    private int initialStock = 100;

    public BookStepDefinitions() {
        this.restTemplate = new TestRestTemplate();
    }

    // ✅ PRUEBA: Buscar un libro existente
    @Given("que el usuario está en la página de búsqueda de libros")
    public void que_el_usuario_esta_en_la_pagina_de_busqueda_de_libros() {
        // No se necesita acción aquí
    }

    @When("busca el libro con título {string}")
    public void busca_el_libro_con_titulo(String title) {
        String url = "http://localhost:8081/books/finds?title=" + title;
        response = restTemplate.getForEntity(url, String.class);
    }

    @Then("el sistema debe mostrar la información del libro con ISBN {string}")
    public void el_sistema_debe_mostrar_la_informacion_del_libro_con_isbn(String expectedIsbn) throws Exception {
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        Map<?, ?>[] books = objectMapper.readValue(response.getBody(), Map[].class);

        boolean found = false;
        for (Map<?, ?> book : books) {
            if (book.get("isbn").equals(expectedIsbn)) {
                found = true;
                break;
            }
        }
        Assertions.assertTrue(found, "El libro con el ISBN " + expectedIsbn + " no se encontró.");
    }

    // ✅ PRUEBA: Reducir stock de un libro existente
    @Given("que existe un libro con ID {long} y stock de {int} unidades")
    public void que_existe_un_libro_con_id_y_stock_de(Long id, Integer stock) {
        this.bookId = id;
        this.initialStock = stock;
    }

    @When("se intenta reducir el stock en {int} unidades")
    public void se_intenta_reducir_el_stock_en_unidades(Integer stockToDeduct) throws Exception {
        String url = "http://localhost:8081/books/" + bookId + "/stocks";

        // Crear el JSON manualmente con la estructura que espera la API
        String jsonBody = "{\"stock\":" + stockToDeduct + "}";

        // Configurar cabeceras
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Crear la petición con el cuerpo JSON
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        // Enviar la solicitud
        response = restTemplate.exchange(url, HttpMethod.PATCH, request, String.class);

        // Imprimir respuesta para depuración
        System.out.println("HTTP Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());
    }

    @Then("el stock del libro debe actualizarse a {int} unidades")
    public void el_stock_del_libro_debe_actualizarse_a(Integer expectedStock) {
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<?, ?> book = objectMapper.readValue(response.getBody(), Map.class);
            Assertions.assertEquals(expectedStock, book.get("stock"));
        } catch (Exception e) {
            Assertions.fail("Error al parsear la respuesta: " + e.getMessage());
        }
    }

    @Then("el sistema debe devolver un mensaje de error {string}")
    public void el_sistema_debe_devolver_un_mensaje_de_error(String expectedMessage) {
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verificar que el mensaje de error en la respuesta coincide con lo esperado
        Assertions.assertTrue(response.getBody().contains(expectedMessage),
                "El mensaje de error esperado no fue encontrado en la respuesta. Respuesta: " + response.getBody());
    }
}
