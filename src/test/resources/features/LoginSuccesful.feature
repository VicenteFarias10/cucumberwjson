Feature: Pruebas de inicio de sesión exitoso

  @LoginExitoso
  Scenario: Intento de inicio de sesión con credenciales válidas
    Given que puedo acceder a la URL valida de inicio de sesión
    When hacemos clic en el botón de login válido
    And ingresa el Correo en el campo de Correo válido
    And ingresa la contraseña en el campo de Contraseña válida
    And hacemos clic en el botón de iniciar sesión válido
    Then debería ver un mensaje de éxito indicando "Login Successful"
