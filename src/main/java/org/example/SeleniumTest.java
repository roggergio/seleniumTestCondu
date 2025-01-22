package org.example;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeleniumTest {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
    // Leer archivos
    // Nota: NO DEJAR FILAS VACÍAS
            Map<String, String> credentials = FileReaderUtil.readCredentials("src/main/resources/credentials.txt");
            Map<String, String> Xpths = FileReaderUtil.readCredentials("src/main/resources/xpaths.txt");

    // Declaracion de clases
            InsertInfo insertInfo = new InsertInfo(driver);
            Click click = new Click(driver);
            ScreenshotUtil screenshotUtil = new ScreenshotUtil(driver);
            List<String> screenshots = new ArrayList<>();

    // Abrir la página
            driver.get(credentials.get("webPage"));
            driver.manage().window().maximize();

    // Login
            insertInfo.fillField(Xpths.get("xpUsuario"), credentials.get("usuario"));
            insertInfo.fillField(Xpths.get("xpClave"), credentials.get("clave"));
        // Tomar captura de pantalla en login y agregarlo añ arreglo de capturas que se enviaran a word, siguientes 3 lineas de codigo
            String loginScreenshotPath = "login.png";
            screenshotUtil.captureScreenshot(loginScreenshotPath);
            screenshots.add(loginScreenshotPath);
        //click para ingresar
            click.onElement(Xpths.get("xpLogin"));

    // Aviso de privacidad
            click.onElement(Xpths.get("xpacepta_avipriv"));
        // Tomar captura de pantalla en login y agregarlo añ arreglo de capturas que se enviaran a word, siguientes 3 lineas de codigo

            String avisoScreenshotPath = "avisoPrivacidad.png";
            screenshotUtil.captureScreenshot(avisoScreenshotPath);
            screenshots.add(avisoScreenshotPath);
        //click para avanzar
            click.onElement(Xpths.get("xpbtnacep_avipriv"));

    // Espera a que el iframe esté disponible
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                    By.xpath("//iframe[contains(@src, 'tramites.condusef.gob.mx')]")
            ));

    // Formulario datos usuario
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpths.get("xpNombre"))));
            insertInfo.fillField(Xpths.get("xpNombre"), credentials.get("nombre"));
            insertInfo.fillField(Xpths.get("xpApellidoPat"), credentials.get("apellidoPat"));

        // Tomar captura de pantalla en formulario de datos de usuario
            String datosUsuarioScreenshotPath = "datosUsuario.png";
            screenshotUtil.captureScreenshot(datosUsuarioScreenshotPath);
            screenshots.add(datosUsuarioScreenshotPath);

    //formulario direccion de usuario
            insertInfo.fillField(Xpths.get("xpApellidoPat"), credentials.get("apellidoPat"));


    // Crear documento Word
            try (XWPFDocument document = new XWPFDocument();
                 FileOutputStream out = new FileOutputStream("FormularioCompleto.docx")) {

    // Sección: Valores introducidos a la prueba
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.setText("Valores introducidos a la prueba:");
                run.addBreak();

    // Listar los valores del archivo credentials.txt
                for (Map.Entry<String, String> entry : credentials.entrySet()) {
                    run.setText(entry.getKey() + ": " + entry.getValue());
                    run.addBreak();
                }

    // Espacio entre secciones
                run.addBreak();

    // Sección: Capturas del formulario completado
                paragraph = document.createParagraph();
                run = paragraph.createRun();
                run.setText("Capturas del formulario completado:");
                run.addBreak();

    // Añadir todas las capturas de pantalla desde la lista
                for (String screenshotPath : screenshots) {
                    File imageFile = new File(screenshotPath);
                    run.addPicture(new FileInputStream(imageFile), XWPFDocument.PICTURE_TYPE_PNG, imageFile.getName(),
                            Units.toEMU(500), Units.toEMU(300)); // Ajusta el tamaño de la imagen
                    run.addBreak(); // Añade un salto de línea entre imágenes
                }

    // Guardar el documento Word
                document.write(out);
            };

            System.out.println("Documento guardado como FormularioCompleto.docx");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //driver.quit(); // Asegúrate de cerrar el navegador al final
        }
    }
}
