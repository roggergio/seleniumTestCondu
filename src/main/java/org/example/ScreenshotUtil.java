package org.example;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ScreenshotUtil {
    private final WebDriver driver;

    public ScreenshotUtil(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Captura una pantalla y guarda la imagen en la ruta especificada.
     * @param filePath Ruta donde se guardará la captura de pantalla.
     */
    public void captureScreenshot(String filePath) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            BufferedImage image = ImageIO.read(srcFile);
            File screenshotFile = new File(filePath);
            ImageIO.write(image, "png", screenshotFile);
        } catch (Exception e) {
            System.err.println("Error al capturar la pantalla: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
