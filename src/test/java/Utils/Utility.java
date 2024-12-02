package Utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class Utility {

    public static void captureScreenShot(WebDriver webdriver, String filePath) throws IOException {
        File screenshotsFolder = new File(System.getProperty("user.dir") + "/evidencias");
        if (!screenshotsFolder.exists()) {
            boolean isCreated = screenshotsFolder.mkdirs();
            System.out.println("Creando carpeta de evidencias en: " + screenshotsFolder.getAbsolutePath());
            if (!isCreated) {
                throw new RuntimeException("No se pudo crear la carpeta de evidencias.");
            }
        }

        System.out.println("Guardando captura en: " + filePath);
        TakesScreenshot screenshot = (TakesScreenshot) webdriver;
        File screenFile = screenshot.getScreenshotAs(OutputType.FILE);
        File destinationFile = new File(filePath);
        FileUtils.copyFile(screenFile, destinationFile);
    }

    public static String getTimeStampValue() {
        Calendar cal = Calendar.getInstance();
        Date time = cal.getTime();
        String timestamp = time.toString().replace(":", "-").replace(" ", "_");
        return timestamp;
    }
}
