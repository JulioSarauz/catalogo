Feature: Gestión de libros y stock

  @BuscarLibro
  Scenario: ✅ Buscar un libro existente por título
    Given que el usuario está en la página de búsqueda de libros
    When busca el libro con título "Spring Boot en Acción"
    Then el sistema debe mostrar la información del libro con ISBN "9781617292545"

  @ReducirStock
  Scenario: ✅ Reducir stock de un libro existente
    Given que existe un libro con ID 1 y stock de 100 unidades
    When se intenta reducir el stock en 5 unidades
    Then el stock del libro debe actualizarse a 95 unidades

  @ReducirStockInsuficiente
  Scenario: ❌ Intentar reducir más stock del disponible
    Given que existe un libro con ID 1 y stock de 100 unidades
    When se intenta reducir el stock en 150 unidades
    Then el sistema debe devolver un mensaje de error "No hay suficiente stock disponible"
