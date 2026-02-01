package Test901;

import java.net.HttpURLConnection;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Google {
    public static void main(String[] args) throws Exception {
        String seleniumURL = System.getenv("SELENIUM_URL");
        if (seleniumURL == null || seleniumURL.isEmpty()) {
            seleniumURL = "http://localhost:4444/wd/hub";
        }

        waitForSelenium(seleniumURL);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = new RemoteWebDriver(new URL(seleniumURL), options);

        try {
            driver.get("https://www.google.com/");
            System.out.println("Ashish123567");
            System.out.println("SUCCESS - Title: " + driver.getTitle());
            System.out.println("SUCCESS - URL: " + driver.getCurrentUrl());
        } finally {
            driver.quit();
        }
    }

    private static void waitForSelenium(String seleniumUrl) throws Exception {
        int attempts = 0;
        while (attempts < 60) {  // 2min max
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(seleniumUrl + "/status").openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.connect();
                if (connection.getResponseCode() == 200) {
                    System.out.println("Selenium server is ready!");
                    return;
                }
            } catch (Exception e) {
                // Expected during startup
            }
            System.out.println("Waiting for Selenium... (" + (attempts + 1) + "/60)");
            Thread.sleep(2000);
            attempts++;
        }
        throw new RuntimeException("Selenium did not start within 2 minutes");
    }
}
