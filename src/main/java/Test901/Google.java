package Test901;




import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;



import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.remote.RemoteWebDriver;





public class Google {

	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		// TODO Auto-generated method stub
		//System.setProperty("webdriver.chrome.driver", "C:/Users/HP/Downloads/chromedriver-win64 (7)/chromedriver-win64/chromedriver.exe");
		//WebDriverManager.chromedriver().cachePath("C:/Users/HP/Downloads/chromedriver-win64 (7)/chromedriver-win64/chromedriver.exe").setup();
		String seleniumURL = "http://selenium:4444/wd/hub";
		waitForSelenium(seleniumURL);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless=new"); // new headless mode for Chrome 109+
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		//options.addArguments("--remote-allow-origins=*");

		WebDriver driver = new RemoteWebDriver(new URL(seleniumURL), options);

		//WebDriver driver = new ChromeDriver();
		//driver.manage().window().maximize();
		driver.get("https://www.google.com/");
		System.out.println("Title...:|"+driver.getTitle());
		System.out.println("URL....:|"+driver.getCurrentUrl());	
		driver.quit();
	}
	
	// Wait until Selenium server is ready
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
