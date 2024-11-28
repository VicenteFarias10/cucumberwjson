Feature: Pruebas de inicio de sesión fallido

  @LoginFallido
  Scenario: Intento de inicio de sesión con credenciales inválidas
    Given que puedo acceder a la URL valida con el identificador "LoginFallido"
    When hacemos clic en el botón de login valido
    And ingresa el Correo en el campo de Correo valido
    And ingresa la contraseña en el campo de Contraseña valida
    And hacemos clic en el botón de iniciar sesión valido
    Then debería ver un mensaje indicando "mensajeError"
