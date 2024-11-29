Feature: Pruebas de inicio de sesión fallido

  @LoginFallido
  Scenario: Intento de inicio de sesión con credenciales válidas
    Given que puedo acceder a la URL valida de inicio de sesion
    When hacemos clic en el botón de login invalido
    And ingresa el Correo en el campo de Correo invalido
    And ingresa la contraseña en el campo de Contraseña invalido
    And hacemos clic en el botón de iniciar sesión invalido
    Then debería ver un mensaje de éxito indicando "Invalid credentials"
