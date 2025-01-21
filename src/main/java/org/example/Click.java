package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Click {
    private WebDriver driver;
    private WebDriverWait wait;

    public Click(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Tiempo de espera configurable
    }

    public void onElement(String xpath) {
        try {
            // Verificar y cambiar al iframe si es necesario
            if (isElementInsideIframe(xpath)) {
                switchToIframeContainingElement(xpath);
            }

            // Esperar y hacer clic en el elemento
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
            element.click();

            // Regresar al contexto principal
            driver.switchTo().defaultContent();
        } catch (Exception e) {
            System.err.println("Error al interactuar con el elemento: " + xpath);
            System.err.println("Detalles: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isElementInsideIframe(String xpath) {
        // Verificar si hay iframes en la página
        if (driver.findElements(By.tagName("iframe")).isEmpty()) {
            return false; // No hay iframes
        }

        // Verificar si el elemento está dentro de un iframe
        try {
            driver.findElement(By.xpath(xpath)); // Buscar elemento sin cambiar contexto
            return false;
        } catch (Exception e) {
            return true; // No encontrado, probablemente esté en un iframe
        }
    }

    private void switchToIframeContainingElement(String xpath) {
        for (WebElement iframe : driver.findElements(By.tagName("iframe"))) {
            driver.switchTo().frame(iframe);
            try {
                driver.findElement(By.xpath(xpath)); // Si encontramos el elemento, estamos en el iframe correcto
                return;
            } catch (Exception ignored) {
                driver.switchTo().defaultContent(); // Volver al contexto principal si no se encuentra
            }
        }
        throw new RuntimeException("Elemento con XPath: " + xpath + " no encontrado en ningún iframe.");
    }

}