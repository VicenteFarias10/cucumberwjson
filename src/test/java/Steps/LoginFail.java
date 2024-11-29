package Steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;

public class LoginFail {
    private WebDriver driver;
    private WebDriverWait wait;
    private JsonNode loginFallidoData; // Variable para los datos de LoginFallido

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/driver/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        loadTestData();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Cargar solo los datos de "LoginFallido"
    private void loadTestData() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("src/test/resources/testdata.json");

            if (!file.exists()) {
                throw new RuntimeException("El archivo testdata.json no se encuentra en la ruta especificada.");
            }

            JsonNode rootNode = objectMapper.readTree(file);

            // Filtrar solo el conjunto de datos de "LoginFallido"
            JsonNode loginTestsNode = rootNode.path("loginTests");
            loginFallidoData = loginTestsNode.path("LoginFallido");

            if (loginFallidoData.isMissingNode()) {
                throw new RuntimeException("No se encontraron datos válidos para LoginFallido en el archivo JSON.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo JSON: " + e.getMessage(), e);
        }
    }

    @Given("que puedo acceder a la URL valida con el identificador {string}")
    public void que_puedo_acceder_a_la_url_valida_con_el_identificador(String identificador) {
        if (!identificador.equals("LoginFallido")) {
            throw new RuntimeException("Identificador no coincide con LoginFallido");
        }
        String url = loginFallidoData.get("url").asText();
        driver.get(url);
    }

    @When("hacemos clic en el botón de login valido")
    public void hacemos_clic_en_el_botón_de_login_valido() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/header/div/div[2]/nav/a"))).click();
    }

    @When("ingresa el Correo en el campo de Correo valido")
    public void ingresa_el_correo_en_el_campo_de_correo_valido() {
        String correo = loginFallidoData.get("correo").asText();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys(correo);
    }

    @When("ingresa la contraseña en el campo de Contraseña valida")
    public void ingresa_la_contraseña_en_el_campo_de_contraseña_valida() {
        String contraseña = loginFallidoData.get("contraseña").asText();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys(contraseña);
    }

    @When("hacemos clic en el botón de iniciar sesión valido")
    public void hacemos_clic_en_el_botón_de_iniciar_sesión_valido() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("loginSubmitButton"))).click();
    }

    @Then("debería ver un mensaje indicando {string}")
    public void debería_ver_un_mensaje_indicando(String mensajeClave) {
        String mensajeObtenido = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/div[2]"))).getText();
        String mensajeEsperado = loginFallidoData.get(mensajeClave).asText();

        assert mensajeObtenido.trim().equals(mensajeEsperado) : "El mensaje no coincide. Esperado: " + mensajeEsperado + ", pero obtuvimos: " + mensajeObtenido;
    }
}
