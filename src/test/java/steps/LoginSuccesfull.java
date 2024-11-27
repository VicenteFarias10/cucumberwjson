package steps;

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
import java.util.ArrayList;
import java.util.List;

public class LoginSuccesfull {
    private WebDriver driver;
    private WebDriverWait wait;
    private List<JsonNode> testCases; // Lista para almacenar cada conjunto de datos de prueba

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

    private void loadTestData() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("src/test/resources/testdata.json");

            if (!file.exists()) {
                throw new RuntimeException("El archivo testdata.json no se encuentra en la ruta especificada.");
            }

            JsonNode rootNode = objectMapper.readTree(file);
            testCases = new ArrayList<>();

            if (rootNode.has("loginTests")) {
                rootNode.get("loginTests").forEach(testCases::add);
            } else {
                throw new RuntimeException("No se encontraron datos válidos en el archivo JSON.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo JSON: " + e.getMessage(), e);
        }
    }

    @Given("que puedo acceder a la URL valida")
    public void que_puedo_acceder_a_la_url_valida() {
        for (JsonNode testCase : testCases) {
            driver.get(testCase.get("url").asText());
        }
    }

    @When("hacemos clic en el botón de login valido")
    public void hacemos_clic_en_el_botón_de_login_valido() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/header/div/div[2]/nav/a"))).click();
    }

    @When("ingresa el Correo en el campo de Correo valido")
    public void ingresa_el_correo_en_el_campo_de_correo_valido() {
        for (JsonNode testCase : testCases) {
            String correo = testCase.get("correo").asText();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).clear();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys(correo);
        }
    }

    @When("ingresa la contraseña en el campo de Contraseña valida")
    public void ingresa_la_contraseña_en_el_campo_de_contraseña_valida() {
        for (JsonNode testCase : testCases) {
            String contraseña = testCase.get("contraseña").asText();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).clear();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys(contraseña);
        }
    }

    @When("hacemos clic en el botón de iniciar sesión valido")
    public void hacemos_clic_en_el_botón_de_iniciar_sesión_valido() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("loginSubmitButton"))).click();
    }

    @Then("debería ver un mensaje de éxito indicando")
    public void debería_ver_un_mensaje_de_éxito_indicando() {
        for (JsonNode testCase : testCases) {
            String mensajeExito = testCase.get("mensajeExito").asText();
            String mensajeObtenido = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/div[2]"))).getText();

            assert mensajeObtenido.trim().equals(mensajeExito) : "El mensaje no coincide con lo esperado.";
        }
    }
}
