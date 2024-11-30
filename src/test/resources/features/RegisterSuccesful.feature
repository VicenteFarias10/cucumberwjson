Feature: Pruebas de registro

  @RegisterSucces
  Scenario: Intento de registro con datos válidos
    Given que puedo acceder a la URL de registro válida
    When ingreso el Nombre Completo válido
    And ingreso el Correo Electrónico válido
    And ingreso el Número de Celular válido
    And ingreso la Contraseña válida
    And hago clic en el botón de registrarme
    Then debería ver un mensaje de éxito indicando el mensaje esperado
