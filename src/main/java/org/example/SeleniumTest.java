package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;

public class SeleniumTest {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            // Leer archivos
            //Nota: NO DEJAR FILAS VACIAS
            Map<String, String> credentials = FileReaderUtil.readCredentials("src/main/resources/credentials.txt");
            Map<String, String> Xpths = FileReaderUtil.readCredentials("src/main/resources/xpaths.txt");


            // Inicializar las clases de acciones
            InsertInfo insertInfo = new InsertInfo(driver);
            Click click = new Click(driver);

            // Abrir la página
            driver.get(credentials.get("webPage"));
            driver.manage().window().maximize();

            // Login
            insertInfo.fillField(Xpths.get("xpUsuario"), credentials.get("usuario"));
            insertInfo.fillField(Xpths.get("xpClave"), credentials.get("clave"));
            click.onElement(Xpths.get("xpLogin"));

            //aviso de privacidad
            click.onElement(Xpths.get("xpacepta_avipriv"));
            click.onElement(Xpths.get("xpbtnacep_avipriv"));

            // Espera a que el iframe esté disponible
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                    By.xpath("//iframe[contains(@src, 'tramites.condusef.gob.mx')]")
            ));

            // Formulario
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpths.get("xpNombre"))));
            insertInfo.fillField(Xpths.get("xpNombre"), credentials.get("nombre"));


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //driver.quit();
        }
    }
}

