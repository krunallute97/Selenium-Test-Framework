package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {

	protected static Properties prop; // static because Before suite code will fail when multiple test cases run
	/*
	 * protected static WebDriver driver; private static ActionDriver actionDriver;
	 */
	// Without threadlocal, parallel testing causes failure due to null instance of
	// WebDriver and ActionDriver

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	// private static ThreadLocal<SoftAssert> softAssert = new ThreadLocal<>();
	// OR
	private static ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}

	@BeforeSuite
	public void loadConfig() throws IOException {
		// Load the configuration file
		prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "/src/main/resources/config.properties");
		prop.load(fis);
		logger.info("config.properties file loaded");

		// Start the Extent Reporter
		// ExtentManager.getReporter(); --This has been implemented in TestListener now
	}

	@BeforeMethod
	// making method synchonized - so that only one thread can execute particular
	// method which is declared as synchronized
	public synchronized void setup() throws IOException {
		System.out.println("Setting up the WebDriver for: " + this.getClass().getSimpleName());
		launchBrowser();
		configBrowser();
		staticWait(2);
		logger.info("WebDriver Initiated and Browser Maximized");

		/*
		 * // Initialize action driver only once - Singleton design pattern if
		 * (actionDriver == null) { actionDriver = new ActionDriver(driver);
		 * logger.info("ActionDriver instance is created"); }
		 */

		// Initialize actionDriver for current thread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("Action Driver initialized for thread: " + Thread.currentThread().getId());

	}

	private synchronized void launchBrowser() {
		// Initialize the WebDriver based on browser defined in config.properties file
		String browser = prop.getProperty("browser");

		if (browser.equalsIgnoreCase("chrome")) {
			// driver = new ChromeDriver();

			// Create ChromeOptions
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless=new"); // Run chrome in headless mode
			options.addArguments("--disable-gpu");
			options.addArguments("--window-size=2560,1440");// disable GPU for headless mode
			options.addArguments("--force-device-scale-factor=0.8");
			//options.addArguments("--high-dpi-support=1"); // optional
			options.addArguments("--disable-notifications"); // Disable browser notification
			options.addArguments("--no-sandbox"); // Required for some CI environment
			options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resources
			options.addArguments("--start-maximized"); // Ensures all elements are in viewport
			options.addArguments("--remote-allow-origins=*"); // Useful for latest ChromeDriver versions
			options.addArguments("--disable-blink-features=AutomationControlled");
			
			
			
			// Auto-download correct ChromeDriver version
			// WebDriverManager.chromedriver().driverVersion("140.0.7339.207").setup();
            //New changes
			WebDriverManager.chromedriver().setup();

			driver.set(new ChromeDriver(options)); // New changes as per thread
			//getDriver().manage().window().setSize(new Dimension(2560, 1440));
			ExtentManager.registerDriver(getDriver());
			logger.info("ChromeDriver Instance is created");
		} else if (browser.equalsIgnoreCase("firefox")) {
			// driver = new FirefoxDriver();

			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless"); // Run chrome in headless mode
			options.addArguments("--disable-gpu"); // disable GPU for headless mode
			options.addArguments("--width=1920"); // Set browser width
			options.addArguments("--height=1080"); // Set browser height
			options.addArguments("--disable-notifications"); // Disable browser notification
			options.addArguments("--no-sandbox"); // Required for some CI environment
			options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resources

			driver.set(new FirefoxDriver(options)); // New changes as per thread
			ExtentManager.registerDriver(getDriver());
			logger.info("FirefoxDriver Instance is created");
		} else if (browser.equalsIgnoreCase("edge")) {
			// driver = new EdgeDriver();

			EdgeOptions options = new EdgeOptions();
			options.addArguments("--headless"); // Run chrome in headless mode
			options.addArguments("disable-gpu"); // disable GPU for headless mode
			options.addArguments("--window-size=1920,1080"); // Set window size
			options.addArguments("--disable-notifications"); // Disable browser notification
			options.addArguments("--no-sandbox"); // Required for some CI environment
			options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resources

			driver.set(new EdgeDriver(options)); // New changes as per thread
			ExtentManager.registerDriver(getDriver());
			logger.info("EdgeDriver Instance is created");
		} else {
			throw new IllegalArgumentException("Browser not supported : " + browser);
		}
	}

	private synchronized void configBrowser() {
		// Implicit Wait
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		// Maximize the browser
		getDriver().manage().window().maximize();

		// Navigate to Url
		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to Navigate to the URL: " + e.getMessage());
		}
	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Unable to quit the browser" + e.getMessage());
			}
		}
		logger.info("WebDriver instance is closed.");
		// driver = null;
		// actionDriver = null;
		driver.remove();
		actionDriver.remove();
		// ExtentManager.endTest(); This has been implemented in TestListener
	}

	// Getter method for prop
	public static Properties getProp() {
		return prop;
	}

	/*
	 * // Driver getter method public WebDriver getDriver() { return driver; }
	 */

	// Getter method for webDriver
	public static WebDriver getDriver() {
		if (driver.get() == null) {
			System.out.println("WebDriver is not initialized");
			throw new IllegalArgumentException("WebDriver is not initialized");
		}
		return driver.get();
	}

	// Getter method for ActionDriver
	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			System.out.println("ActionDriver is not initialized");
			throw new IllegalArgumentException("ActionDriver is not initialized");
		}
		return actionDriver.get();
	}

	// Driver setter method
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}

	// Static wait for pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}
}
