package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class InsertInfo {
    private WebDriver driver;
    private WebDriverWait wait;

    public InsertInfo(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void fillField(String xpath, String value) {
        try {
            // Verificar si el elemento está dentro de un iframe
            if (isElementInsideIframe(xpath)) {
                switchToIframeContainingElement(xpath);
            }

            // Esperar y rellenar el campo
            WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            field.clear();
            field.sendKeys(value);

            // Regresar al contexto principal
            driver.switchTo().defaultContent();
        } catch (Exception e) {
            System.err.println("Error al rellenar el campo con XPath: " + xpath);
            e.printStackTrace();
        }
    }

    // Verificar si el elemento está dentro de un iframe
    private boolean isElementInsideIframe(String xpath) {
        try {
            driver.findElement(By.xpath(xpath)); // Buscar elemento en el contexto actual
            return false; // Si lo encuentra, no está en un iframe
        } catch (Exception e) {
            return true; // Si no lo encuentra, probablemente esté en un iframe
        }
    }

    // Cambiar al iframe que contiene el elemento
    private void switchToIframeContainingElement(String xpath) {
        for (WebElement iframe : driver.findElements(By.tagName("iframe"))) {
            driver.switchTo().frame(iframe);
            try {
                driver.findElement(By.xpath(xpath)); // Verificar si el elemento está en este iframe
                return; // Salir si encontramos el elemento
            } catch (Exception ignored) {
                driver.switchTo().defaultContent(); // Volver al contexto principal si no está en este iframe
            }
        }
        throw new RuntimeException("Elemento con XPath: " + xpath + " no encontrado en ningún iframe.");
    }


}