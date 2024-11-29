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

public class LoginSuccesful {
    private WebDriver driver;
    private WebDriverWait wait;
    private JsonNode loginExitosoData; // Solo una variable para almacenar los datos de LOGINEXITOSO

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

    // Cargar solo los datos de "LOGINEXITOSO"
    private void loadTestData() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("src/test/resources/testdata.json");

            if (!file.exists()) {
                throw new RuntimeException("El archivo testdata.json no se encuentra en la ruta especificada.");
            }

            JsonNode rootNode = objectMapper.readTree(file);

            // Filtrar solo el conjunto de datos de "LoginExitoso"
            JsonNode loginTestsNode = rootNode.path("loginTests");
            loginExitosoData = loginTestsNode.path("LoginExitoso"); // Seleccionamos solo los datos de "LoginExitoso"

            if (loginExitosoData.isMissingNode()) {
                throw new RuntimeException("No se encontraron datos válidos para LoginExitoso en el archivo JSON.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo JSON: " + e.getMessage(), e);
        }
    }

    @Given("que puedo acceder a la URL valida")
    public void que_puedo_acceder_a_la_url_valida() throws InterruptedException {
        String url = loginExitosoData.get("url").asText();
        driver.get(url); // Usamos solo la URL del login exitoso
        Thread.sleep(1500); // Esperamos 3 segundos antes de proceder
    }

    @When("hacemos clic en el botón de login valido")
    public void hacemos_clic_en_el_botón_de_login_valido() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/header/div/div[2]/nav/a"))).click();
        Thread.sleep(1500); // Esperamos 2 segundos después de hacer clic
    }

    @When("ingresa el Correo en el campo de Correo valido")
    public void ingresa_el_correo_en_el_campo_de_correo_valido() throws InterruptedException {
        String correo = loginExitosoData.get("correo").asText(); // Solo obtenemos el correo del login exitoso
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys(correo);
        Thread.sleep(1500); // Esperamos 2 segundos después de ingresar el correo
    }

    @When("ingresa la contraseña en el campo de Contraseña valida")
    public void ingresa_la_contraseña_en_el_campo_de_contraseña_valida() throws InterruptedException {
        String contraseña = loginExitosoData.get("contraseña").asText(); // Solo obtenemos la contraseña del login exitoso
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys(contraseña);
        Thread.sleep(2000); // Esperamos 2 segundos después de ingresar la contraseña
    }

    @When("hacemos clic en el botón de iniciar sesión valido")
    public void hacemos_clic_en_el_botón_de_iniciar_sesión_valido() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("loginSubmitButton"))).click();
        Thread.sleep(1500); // Esperamos 3 segundos después de hacer clic
    }

    @Then("debería ver un mensaje de éxito indicando {string}")
    public void debería_ver_un_mensaje_de_éxito_indicando(String mensajeExitoEsperado) {
        String mensajeObtenido = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/div[2]"))).getText();

        // Verifica si el mensaje obtenido coincide con el mensaje en el archivo JSON
        String mensajeEsperadoDelJson = loginExitosoData.get("mensajeExito").asText();

        assert mensajeObtenido.trim().equals(mensajeEsperadoDelJson) : "El mensaje no coincide con lo esperado. Mensaje esperado: " + mensajeEsperadoDelJson + ", pero obtuvimos: " + mensajeObtenido;
    }

}