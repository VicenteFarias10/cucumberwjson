Feature: Pruebas de inicio de sesión fallido

  @LoginFallido
  Scenario: Intento de inicio de sesión con credenciales invalidas
    Given que puedo acceder a la URL valida de inicio de sesion invalidas
    When hacemos clic en el botón de login invalido
    And ingresa el Correo en el campo de Correo invalido
    And ingresa la contraseña en el campo de Contraseña invalido
    And hacemos clic en el botón de iniciar sesión invalido
    Then debería ver un mensaje de error indicando "Invalid credentials"
