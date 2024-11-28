Feature: Pruebas de inicio de sesión exitoso

  @LoginExitoso
  Scenario: Intento de inicio de sesión con credenciales válidas
    Given que puedo acceder a la URL valida
    When hacemos clic en el botón de login valido
    And ingresa el Correo en el campo de Correo valido
    And ingresa la contraseña en el campo de Contraseña valida
    And hacemos clic en el botón de iniciar sesión valido
    Then debería ver un mensaje de éxito indicando "Login Succesfull"
