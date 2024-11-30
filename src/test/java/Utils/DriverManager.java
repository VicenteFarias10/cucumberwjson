package Utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverManager {
    private static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    // Método para inicializar el WebDriver
    public static void initializeDriver() {
        if (driverThread.get() == null) {
            System.setProperty("webdriver.chrome.driver", "src/test/resources/driver/chromedriver.exe");
            WebDriver driver = new ChromeDriver();
            driverThread.set(driver);
        }
    }

    // Método para obtener la instancia actual del WebDriver
    public static WebDriver getDriver() {
        if (driverThread.get() == null) {
            throw new IllegalStateException("El WebDriver no está inicializado. Llama a initializeDriver() primero.");
        }
        return driverThread.get();
    }

    // Método para cerrar y eliminar el WebDriver
    public static void quitDriver() {
        if (driverThread.get() != null) {
            driverThread.get().quit();
            driverThread.remove(); // Elimina la instancia del hilo actual
        }
    }
}
