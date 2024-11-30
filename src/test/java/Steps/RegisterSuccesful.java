package Steps;

import Utils.DriverManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
        DriverManager.initializeDriver(); // Inicializa el WebDriver usando DriverManager
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, 10);
        loadTestData(); // Carga los datos del JSON
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
    public void que_puedo_acceder_a_la_url_de_registro_válida() {
        String url = registerData.get("url").asText();
        driver.get(url);
    }

    @When("ingreso el Nombre Completo válido")
    public void ingreso_el_nombre_completo_válido() {
        String nombre = registerData.get("nombre").asText();
        WebElement nombreCompletoField = driver.findElement(By.xpath("//*[@id=\"fullName\"]"));
        nombreCompletoField.sendKeys(nombre);
    }

    @When("ingreso el Correo Electrónico válido")
    public void ingreso_el_correo_electrónico_válido() {
        String correo = registerData.get("correo").asText();
        WebElement correoField = driver.findElement(By.xpath("//*[@id=\"email\"]"));
        correoField.sendKeys(correo);
    }

    @When("ingreso el Número de Celular válido")
    public void ingreso_el_número_de_celular_válido() {
        String celular = registerData.get("celular").asText();
        WebElement celularField = driver.findElement(By.xpath("//*[@id=\"phone\"]"));
        celularField.sendKeys(celular);
    }

    @When("ingreso la Contraseña válida")
    public void ingreso_la_contraseña_válida() {
        String contraseña = registerData.get("contraseña").asText();
        WebElement contraseñaField = driver.findElement(By.xpath("//*[@id=\"password\"]"));
        contraseñaField.sendKeys(contraseña);
    }

    @When("hago clic en el botón de registrarme")
    public void hago_clic_en_el_botón_de_registrarme() {
        WebElement registrarseButton = driver.findElement(By.xpath("//*[@id=\"registerSubmitButton\"]"));
        registrarseButton.click();
    }

    @Then("debería ver un mensaje de éxito indicando el mensaje esperado")
    public void debería_ver_un_mensaje_de_éxito_indicando_el_mensaje_esperado() {
        String mensajeEsperado = registerData.get("mensajeExito").asText();

        // Espera hasta que el mensaje de éxito sea visible
        String mensajeObtenido = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/div[2]")))
                .getText();

        // Verifica si el mensaje obtenido coincide con el mensaje esperado
        assert mensajeObtenido.trim().equals(mensajeEsperado) :
                String.format("El mensaje no coincide con lo esperado. Esperado: '%s', pero obtenido: '%s'",
                        mensajeEsperado, mensajeObtenido);

        // Espera un poco más antes de cerrar
        try {
            Thread.sleep(2000); // Espera 2 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
