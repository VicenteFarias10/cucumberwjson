package Steps;

import Utils.DriverManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.io.File;
import java.io.IOException;

public class LoginFail {
    private WebDriver driver;
    private WebDriverWait wait;
    private JsonNode loginFallidoData;

    @Before
    public void setUp() {
        DriverManager.initializeDriver(); // Inicializa el WebDriver usando el DriverManager
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, 10);
        loadTestData(); // Carga los datos necesarios
    }

    @After
    public void tearDown() {
        DriverManager.quitDriver(); // Cierra y limpia la instancia del WebDriver
    }

    /**
     * Cargar los datos de prueba desde el archivo JSON.
     */
    private void loadTestData() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("src/test/resources/testdata.json");

            if (!file.exists()) {
                throw new RuntimeException("El archivo testdata.json no se encuentra en la ruta especificada.");
            }

            JsonNode rootNode = objectMapper.readTree(file);
            JsonNode loginTestsNode = rootNode.path("loginTests");
            loginFallidoData = loginTestsNode.path("LoginFallido");

            if (loginFallidoData.isMissingNode()) {
                throw new RuntimeException("No se encontraron datos válidos para LoginFallido en el archivo JSON.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo JSON: " + e.getMessage(), e);
        }
    }

    @Given("que puedo acceder a la URL valida de inicio de sesion invalidas")
    public void que_puedo_acceder_a_la_url_valida_de_inicio_de_sesion_invalidas() {
        String url = loginFallidoData.get("url").asText();
        driver.get(url);
    }

    @When("hacemos clic en el botón de login invalido")
    public void hacemos_clic_en_el_botón_de_login_invalido() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/header/div/div[2]/nav/a"))).click();
    }

    @When("ingresa el Correo en el campo de Correo invalido")
    public void ingresa_el_correo_en_el_campo_de_correo_invalido() {
        String correo = loginFallidoData.get("correo").asText();
        By emailField = By.id("email");

        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(correo);
    }

    @When("ingresa la contraseña en el campo de Contraseña invalido")
    public void ingresa_la_contraseña_en_el_campo_de_contraseña_invalido() {
        String contraseña = loginFallidoData.get("contraseña").asText();
        By passwordField = By.id("password");

        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(contraseña);
    }

    @When("hacemos clic en el botón de iniciar sesión invalido")
    public void hacemos_clic_en_el_botón_de_iniciar_sesión_invalido() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("loginSubmitButton"))).click();
    }

    @Then("debería ver un mensaje de error indicando {string}")
    public void debería_ver_un_mensaje_de_error_indicando(String mensajeErrorEsperado) {
        String mensajeObtenido = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/div[2]")))
                .getText();

        // Verifica si el mensaje obtenido coincide con el mensaje en el archivo JSON
        String mensajeEsperadoDelJson = loginFallidoData.get("mensajeError").asText();

        assert mensajeObtenido.trim().equals(mensajeEsperadoDelJson) :
                String.format("El mensaje no coincide con lo esperado. Esperado: '%s', pero obtenido: '%s'",
                        mensajeEsperadoDelJson, mensajeObtenido);
    }
}
