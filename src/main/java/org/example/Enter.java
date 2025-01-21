package org.example;

import org.openqa.selenium.WebDriver;

public class Enter {
    private WebDriver driver;

    public Enter(WebDriver driver) {
        this.driver = driver;
    }

    public void switchToIframe(String iframeNameOrId) {
        driver.switchTo().frame(iframeNameOrId);
    }

    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }
}
