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

public class LoginFail {
    private WebDriver driver;
    private WebDriverWait wait;
    private JsonNode loginFallidoData;

    @Before
    public void setUp() {
        DriverManager.initializeDriver();
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, 10);
        loadTestData();
    }

    @After
    public void tearDown() {
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
            loginFallidoData = loginTestsNode.path("LoginFallido");

            if (loginFallidoData.isMissingNode()) {
                throw new RuntimeException("No se encontraron datos válidos para LoginFallido en el archivo JSON.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo JSON: " + e.getMessage(), e);
        }
    }

    @Given("que puedo acceder a la URL valida de inicio de sesion invalidas")
    public void que_puedo_acceder_a_la_url_valida_de_inicio_de_sesion_invalidas() throws IOException {
        String url = loginFallidoData.get("url").asText();
        System.out.println("Navegando a la URL: " + url);
        driver.get(url);
        Utility.captureScreenShot(driver, "evidencias/Acceso_URL_Fallido_" + Utility.getTimeStampValue() + ".png");
    }

    @When("hacemos clic en el botón de login invalido")
    public void hacemos_clic_en_el_botón_de_login_invalido() throws IOException {
        System.out.println("Haciendo clic en el botón de login...");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/header/div/div[2]/nav/a"))).click();
        Utility.captureScreenShot(driver, "evidencias/Click_Login_Fallido_" + Utility.getTimeStampValue() + ".png");
    }

    @When("ingresa el Correo en el campo de Correo invalido")
    public void ingresa_el_correo_en_el_campo_de_correo_invalido() throws IOException {
        String correo = loginFallidoData.get("correo").asText();
        System.out.println("Ingresando correo: " + correo);
        By emailField = By.id("email");

        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(correo);
        Utility.captureScreenShot(driver, "evidencias/Ingreso_Correo_Fallido_" + Utility.getTimeStampValue() + ".png");
    }

    @When("ingresa la contraseña en el campo de Contraseña invalido")
    public void ingresa_la_contraseña_en_el_campo_de_contraseña_invalido() throws IOException {
        String contraseña = loginFallidoData.get("contraseña").asText();
        System.out.println("Ingresando contraseña...");
        By passwordField = By.id("password");

        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(contraseña);
        Utility.captureScreenShot(driver, "evidencias/Ingreso_Contraseña_Fallido_" + Utility.getTimeStampValue() + ".png");
    }

    @When("hacemos clic en el botón de iniciar sesión invalido")
    public void hacemos_clic_en_el_botón_de_iniciar_sesión_invalido() throws IOException {
        System.out.println("Haciendo clic en el botón de iniciar sesión...");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("loginSubmitButton"))).click();
        Utility.captureScreenShot(driver, "evidencias/Clic_Iniciar_Sesion_Fallido_" + Utility.getTimeStampValue() + ".png");
    }

    @Then("debería ver un mensaje de error indicando {string}")
    public void debería_ver_un_mensaje_de_error_indicando(String mensajeErrorEsperado) throws IOException {
        System.out.println("Verificando mensaje de error...");
        String mensajeObtenido = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/div[2]"))).getText();

        String mensajeEsperadoDelJson = loginFallidoData.get("mensajeError").asText();

        assert mensajeObtenido.trim().equals(mensajeEsperadoDelJson) :
                String.format("El mensaje no coincide con lo esperado. Esperado: '%s', pero obtenido: '%s'",
                        mensajeEsperadoDelJson, mensajeObtenido);

        Utility.captureScreenShot(driver, "evidencias/Mensaje_Error_Fallido_" + Utility.getTimeStampValue() + ".png");
    }
}
