package Steps;

import Utils.DriverManager;
import Utils.Utility;
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

public class LoginSuccesful {
    private WebDriver driver;
    private WebDriverWait wait;
    private JsonNode loginExitosoData;

    @Before
    public void setUp() {
        DriverManager.initializeDriver();
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, 10);
        loadTestData();
        System.out.println("Driver inicializado. Prueba comenzará.");
    }

    @After
    public void tearDown() {
        System.out.println("Prueba finalizada. Cerrando el driver...");
        DriverManager.quitDriver();
    }

    private void loadTestData() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("src/test/resources/testdata.json");

            if (!file.exists()) {
                throw new RuntimeException("El archivo testdata.json no se encuentra en la ruta especificada.");
            }

            JsonNode rootNode = objectMapper.readTree(file);
            JsonNode loginTestsNode = rootNode.path("loginTests");
            loginExitosoData = loginTestsNode.path("LoginExitoso");

            if (loginExitosoData.isMissingNode()) {
                throw new RuntimeException("No se encontraron datos válidos para LoginExitoso en el archivo JSON.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo JSON: " + e.getMessage(), e);
        }
    }

    @Given("que puedo acceder a la URL valida de inicio de sesión")
    public void que_puedo_acceder_a_la_url_valida_de_inicio_de_sesion() throws IOException {
        String url = loginExitosoData.get("url").asText();
        System.out.println("Navegando a la URL: " + url);
        driver.get(url);
        Utility.captureScreenShot(driver, "evidencias/Acceso_URL_" + Utility.getTimeStampValue() + ".png");
    }

    @When("hacemos clic en el botón de login válido")
    public void hacemos_clic_en_el_botón_de_login_valido() throws IOException {
        System.out.println("Haciendo clic en el botón de login...");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/header/div/div[2]/nav/a"))).click();
        Utility.captureScreenShot(driver, "evidencias/Click_Login_" + Utility.getTimeStampValue() + ".png");
    }

    @When("ingresa el Correo en el campo de Correo válido")
    public void ingresa_el_correo_en_el_campo_de_correo_valido() throws IOException {
        String correo = loginExitosoData.get("correo").asText();
        System.out.println("Ingresando correo: " + correo);
        By emailField = By.id("email");

        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(correo);
        Utility.captureScreenShot(driver, "evidencias/Ingreso_Correo_" + Utility.getTimeStampValue() + ".png");
    }

    @When("ingresa la contraseña en el campo de Contraseña válida")
    public void ingresa_la_contraseña_en_el_campo_de_contraseña_valida() throws IOException {
        String contraseña = loginExitosoData.get("contraseña").asText();
        System.out.println("Ingresando contraseña...");
        By passwordField = By.id("password");

        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(contraseña);
        Utility.captureScreenShot(driver, "evidencias/Ingreso_Contraseña_" + Utility.getTimeStampValue() + ".png");
    }

    @When("hacemos clic en el botón de iniciar sesión válido")
    public void hacemos_clic_en_el_botón_de_iniciar_sesion_valido() throws IOException {
        System.out.println("Haciendo clic en el botón de iniciar sesión...");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("loginSubmitButton"))).click();
        Utility.captureScreenShot(driver, "evidencias/Clic_Iniciar_Sesion_" + Utility.getTimeStampValue() + ".png");
    }

    @Then("debería ver un mensaje de éxito indicando {string}")
    public void debería_ver_un_mensaje_de_éxito_indicando(String mensajeExitoEsperado) throws IOException {
        System.out.println("Verificando mensaje de éxito...");
        String mensajeObtenido = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/div[2]")))
                .getText();

        String mensajeEsperadoDelJson = loginExitosoData.get("mensajeExito").asText();

        assert mensajeObtenido.trim().equals(mensajeEsperadoDelJson) :
                String.format("El mensaje no coincide con lo esperado. Esperado: '%s', pero obtenido: '%s'",
                        mensajeEsperadoDelJson, mensajeObtenido);

        Utility.captureScreenShot(driver, "evidencias/Mensaje_Exito_" + Utility.getTimeStampValue() + ".png");
    }
}
