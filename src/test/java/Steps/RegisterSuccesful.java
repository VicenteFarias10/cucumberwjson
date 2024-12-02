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

public class RegisterSuccesful {
    private WebDriver driver;
    private WebDriverWait wait;
    private JsonNode registerData;

    @Before
    public void setUp() {
        DriverManager.initializeDriver();
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, 10);
        loadTestData();
    }

    @After
    public void tearDown() {
        System.out.println("Cerrando el driver...");
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
            JsonNode registerTestsNode = rootNode.path("registerTests");
            registerData = registerTestsNode.path("RegistroValido");

            if (registerData.isMissingNode()) {
                throw new RuntimeException("No se encontraron datos válidos para RegistroValido en el archivo JSON.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo JSON: " + e.getMessage(), e);
        }
    }

    @Given("que puedo acceder a la URL de registro válida")
    public void que_puedo_acceder_a_la_url_de_registro_válida() throws IOException {
        driver.get(registerData.get("url").asText());
        Utility.captureScreenShot(driver, "evidencias/Acceso_URL_Registro_" + Utility.getTimeStampValue() + ".png");
    }

    @When("ingreso el Nombre Completo válido")
    public void ingreso_el_nombre_completo_válido() throws IOException {
        driver.findElement(By.xpath("//*[@id=\"fullName\"]"))
                .sendKeys(registerData.get("nombre").asText());
        Utility.captureScreenShot(driver, "evidencias/Ingreso_Nombre_Registro_" + Utility.getTimeStampValue() + ".png");
    }

    @When("ingreso el Correo Electrónico válido")
    public void ingreso_el_correo_electrónico_válido() throws IOException {
        driver.findElement(By.xpath("//*[@id=\"email\"]"))
                .sendKeys(registerData.get("correo").asText());
        Utility.captureScreenShot(driver, "evidencias/Ingreso_Correo_Registro_" + Utility.getTimeStampValue() + ".png");
    }

    @When("ingreso el Número de Celular válido")
    public void ingreso_el_número_de_celular_válido() throws IOException {
        driver.findElement(By.xpath("//*[@id=\"phone\"]"))
                .sendKeys(registerData.get("celular").asText());
        Utility.captureScreenShot(driver, "evidencias/Ingreso_Celular_Registro_" + Utility.getTimeStampValue() + ".png");
    }

    @When("ingreso la Contraseña válida")
    public void ingreso_la_contraseña_válida() throws IOException {
        driver.findElement(By.xpath("//*[@id=\"password\"]"))
                .sendKeys(registerData.get("contraseña").asText());
        Utility.captureScreenShot(driver, "evidencias/Ingreso_Contraseña_Registro_" + Utility.getTimeStampValue() + ".png");
    }

    @When("hago clic en el botón de registrarme")
    public void hago_clic_en_el_botón_de_registrarme() throws IOException {
        driver.findElement(By.xpath("//*[@id=\"registerSubmitButton\"]")).click();
        Utility.captureScreenShot(driver, "evidencias/Clic_Registro_" + Utility.getTimeStampValue() + ".png");
    }

    @Then("debería ver un mensaje de éxito indicando el mensaje esperado")
    public void debería_ver_un_mensaje_de_éxito_indicando_el_mensaje_esperado() throws IOException {
        String mensajeObtenido = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/div[2]")))
                .getText();

        String mensajeEsperado = registerData.get("mensajeExito").asText();

        assert mensajeObtenido.trim().equals(mensajeEsperado) :
                String.format("El mensaje no coincide con lo esperado. Esperado: '%s', pero obtenido: '%s'",
                        mensajeEsperado, mensajeObtenido);

        Utility.captureScreenShot(driver, "evidencias/Mensaje_Exito_Registro_" + Utility.getTimeStampValue() + ".png");
    }
}
