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
import java.util.Iterator;

public class LoginSuccesfull {
    private WebDriver driver;
    private WebDriverWait wait;

    // Variable que guarda todos los datos cargados del JSON
    private JsonNode loginTests;

    @Before
    public void setUp() {
        // Configura el driver con la ruta correcta
        System.setProperty("webdriver.chrome.driver", "src/test/resources/driver/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        loadTestData();  // Carga los datos del archivo JSON
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    // Método para cargar los datos del JSON
    private void loadTestData() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("src/test/resources/testdata.json");

            // Verifica si el archivo existe antes de cargarlo
            if (!file.exists()) {
                System.out.println("El archivo testdata.json no se encuentra en la ruta especificada.");
                return;
            }

            // Cargar el contenido del archivo JSON
            JsonNode rootNode = objectMapper.readTree(file);
            loginTests = rootNode.get("loginTests");  // Obtiene los datos de loginTests

            // Verifica si los datos fueron cargados correctamente
            if (loginTests == null || loginTests.isEmpty()) {
                System.out.println("No se encontraron datos en 'loginTests' del archivo JSON.");
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Given("que puedo acceder a la URL valida")
    public void que_puedo_acceder_a_la_url_valida() {
        // Verifica si los datos fueron cargados correctamente
        if (loginTests != null && loginTests.size() > 0) {
            Iterator<JsonNode> elements = loginTests.elements();
            while (elements.hasNext()) {
                JsonNode data = elements.next();
                driver.get(data.get("url").asText());
            }
        } else {
            System.out.println("Los datos no están disponibles o están vacíos.");
        }
    }

    @When("hacemos clic en el botón de login valido")
    public void hacemos_clic_en_el_botón_de_login_valido() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/header/div/div[2]/nav/a"))).click();
    }

    @When("ingresa el Correo en el campo de Correo valido")
    public void ingresa_el_correo_en_el_campo_de_correo_valido() {
        if (loginTests != null && loginTests.size() > 0) {
            Iterator<JsonNode> elements = loginTests.elements();
            while (elements.hasNext()) {
                JsonNode data = elements.next();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"email\"]"))).sendKeys(data.get("correo").asText());
            }
        }
    }

    @When("ingresa la contraseña en el campo de Contraseña valida")
    public void ingresa_la_contraseña_en_el_campo_de_contraseña_valida() {
        if (loginTests != null && loginTests.size() > 0) {
            Iterator<JsonNode> elements = loginTests.elements();
            while (elements.hasNext()) {
                JsonNode data = elements.next();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"password\"]"))).sendKeys(data.get("contraseña").asText());
            }
        }
    }

    @When("hacemos clic en el botón de iniciar sesión valido")
    public void hacemos_clic_en_el_botón_de_iniciar_sesión_valido() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"loginSubmitButton\"]"))).click();
    }

    @Then("debería ver un mensaje de éxito indicando")
    public void debería_ver_un_mensaje_de_éxito_indicando() {
        if (loginTests != null && loginTests.size() > 0) {
            Iterator<JsonNode> elements = loginTests.elements();
            while (elements.hasNext()) {
                JsonNode data = elements.next();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/div[2]")));
                String mensajeObtenido = driver.findElement(By.xpath("/html/body/div[1]/div/div/div[2]")).getText();
                if (mensajeObtenido.trim().equals(data.get("mensajeExito").asText())) {
                    System.out.println("Resultado: Inicio de sesión válido");
                } else {
                    System.out.println("Resultado: Inicio de sesión no válido");
                }
            }
        }
    }
}
