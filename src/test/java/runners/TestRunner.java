package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features", // Ruta a las características (puede incluir todos los .feature)
        glue = {"Steps"}, // Ruta a las clases de pasos
        plugin = {"pretty", "json:target/cucumber-report/cucumber.json"}, // Reporte en formato JSON
        tags = "@LoginExitoso,@LoginFallido,@RegisterSucces" // Sin filtro de etiquetas para ejecutar todo
)
public class TestRunner {
    // Este runner ejecutará todos los archivos de .feature dentro de la carpeta especificada
}
