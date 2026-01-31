package Test901;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Google {

    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        // Use environment variable if set, otherwise fallback to localhost (Windows Jenkins fix)
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

        driver.get("https://www.google.com/");
        System.out.println("Title: " + driver.getTitle());
        System.out.println("URL: " + driver.getCurrentUrl());

        driver.quit();
    }

    private static void waitForSelenium(String seleniumUrl) throws InterruptedException {
        while (true) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(seleniumUrl + "/status").openConnection();
                connection.setConnectTimeout(2000);
                connection.connect();
                if (connection.getResponseCode() == 200) {
                    System.out.println("Selenium server is ready!");
                    break;
                }
            } catch (Exception e) {
                System.out.println("Waiting for Selenium to start...");
                Thread.sleep(2000);
            }
        }
    }
}
