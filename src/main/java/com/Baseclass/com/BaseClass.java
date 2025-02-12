package com.Baseclass.com;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.Listeners;

import com.aventstack.extentreports.Status;
import com.constants.com.FrameworkConstants;
import com.utils.ExtentReportManager;
import com.utils.RetryTransformer;

import io.github.bonigarcia.wdm.WebDriverManager;

@Listeners({ RetryTransformer.class })

public class BaseClass extends FrameworkConstants {

	public static WebDriver driver;

	public static ExtentReportManager extentReportManager;

	private static Actions ac;

	private static Select sc;

	public static void setUpSuite() {
		try {
			extentReportManager = new ExtentReportManager();
			ExtentReportManager.createInstance("test-output/ExtentReport.html"); // Report will store in test output

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static WebDriver getDriver(String browserName) throws Exception {

		try {
			switch (browserName.toLowerCase()) {
			case "chrome":
				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver();
				break;
			case "firefox":
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver();
				break;
			case "edge":
				WebDriverManager.edgedriver().setup();
				driver = new EdgeDriver();
				break;
			default:
				throw new Exception("Invalid Browser name Entered: " + browserName);

			}

		} catch (Exception e) {
			e.printStackTrace();

			throw new Exception("Failed to initialize WebDriver for browser: " + browserName, e);
		}

		driver.manage().window().maximize();

		return driver;

	}

	public static void navigateGetUrl(String url) throws Exception {

		try {

			driver.get(url);

		} catch (Exception e) {

			String errorMessage = "Failed to navigate to URL: " + url + ". " + e.getMessage();
			System.err.println(errorMessage);
			throw new Exception(errorMessage, e);
		}

	}

	public static void setText(WebElement element, String value) throws Exception {

		waitForElementVisibility(element);

		try {

			element.clear();

			element.sendKeys(value);

		} catch (Exception e) {

			String errorMessage = "Failed to set text to element: " + e.getMessage();
			System.err.println(errorMessage);
			throw new Exception(errorMessage, e);
		}

	}

	public static void waitForElementVisibility(WebElement element) {

		try {

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

			wait.until(ExpectedConditions.visibilityOf(element));

		} catch (Exception e) {
			String errorMessage = "Element not visible within timeout: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void waitForElementToBeClickable(WebElement element) {

		try {

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.elementToBeClickable(element));

		} catch (Exception e) {
			String errorMessage = "Element is not clickable within timeout: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void waitForElementToBePresent(WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.presenceOfElementLocated((By) element));
		} catch (Exception e) {
			String errorMessage = "Element not found within timeout: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void clickElement(WebElement element) {

		try {

			waitForElementToBeClickable(element);

			element.click();

		} catch (Exception e) {

			String errorMessage = "Failed to click on element: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}

	}

	public static String getTextFromElement(WebElement element) {

		try {

			waitForElementVisibility(element);
			return element.getText();

		} catch (Exception e) {

			String errorMessage = "Failed to get text from element: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static String getAttributeValue(WebElement element, String attributeName) {

		try {
			waitForElementVisibility(element);
			String attributeValue = element.getDomAttribute(attributeName);
			if (attributeValue != null) {
				return attributeValue;
			} else {
				System.out.println("Attribute '" + attributeName + "' not found on the element.");
				return null;
			}
		} catch (Exception e) {
			String errorMessage = "Failed to get attribute '" + attributeName + "' from element: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static boolean takeScreenshot(String pictureStorePath) throws IOException {

		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File screenshotAs = ts.getScreenshotAs(OutputType.FILE);
			File saveFile = new File(pictureStorePath);
			FileUtils.copyFile(screenshotAs, saveFile);
			System.out.println("Screenshot saved to: " + pictureStorePath);

		} catch (Exception e) {

			String errorMessage = "Failed to take screenshot: " + e.getMessage();
			System.err.println(errorMessage);
			throw new IOException(errorMessage, e);
		}
		return false;
	}

	public static void closeCurrentBrowser(ITestResult result, String pictureStorePath) {

		try {

			if (result.getStatus() == ITestResult.FAILURE) {
				// Capture screenshot on test failure

				// TakesScreenshot()
				// String screenshotPath = ScreenshotUtils.captureScreenshot(driver,
				// result.getMethod().getMethodName());

				// Attach screenshot to the Extent Report
				if (takeScreenshot(pictureStorePath)) {
					ExtentReportManager.log(Status.FAIL, "Test Failed: " + result.getThrowable());
					ExtentReportManager.test.addScreenCaptureFromPath(pictureStorePath);
				} else {
					ExtentReportManager.log(Status.FAIL, "Test Failed: " + result.getThrowable());
				}
			} else if (result.getStatus() == ITestResult.SUCCESS) {
				ExtentReportManager.log(Status.PASS, "Test Passed");
			} else {
				ExtentReportManager.log(Status.SKIP, "Test Skipped");
			}

			if (driver != null) {
				driver.close();
			}

		} catch (Exception e) {

			String errorMessage = "Failed to close the current browser: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void closeAllBrowsers(ITestResult result, String pictureStorePath) {

		try {
			if (result.getStatus() == ITestResult.FAILURE) {
				// Capture screenshot on test failure
				if (takeScreenshot(pictureStorePath)) {
					ExtentReportManager.log(Status.FAIL, "Test Failed: " + result.getThrowable());
					ExtentReportManager.test.addScreenCaptureFromPath(pictureStorePath);
				} else {
					ExtentReportManager.log(Status.FAIL, "Test Failed: " + result.getThrowable());
				}
			} else if (result.getStatus() == ITestResult.SUCCESS) {
				ExtentReportManager.log(Status.PASS, "Test Passed");
			} else {
				ExtentReportManager.log(Status.SKIP, "Test Skipped");
			}
			if (driver != null) {
				driver.quit();
			}

		} catch (Exception e) {

			String errorMessage = "Failed to close all browsers: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void actionLeftClick(WebElement element) {

		try {
			waitForElementToBeClickable(element);

			ac = new Actions(driver);
			ac.click(element).perform();

		} catch (Exception e) {
			String errorMessage = "Failed to click on element: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}

	}

	public static void actionRightClick(WebElement element) {

		try {
			waitForElementToBeClickable(element);

			ac = new Actions(driver);
			ac.contextClick(element).perform();

		} catch (Exception e) {

			String errorMessage = "Failed to right-click on element: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}

	}

	public static void actionDoubleClick(WebElement element) {

		try {
			waitForElementToBeClickable(element);

			ac = new Actions(driver);
			ac.doubleClick(element).perform();

		} catch (Exception e) {

			String errorMessage = "Failed to double-click on element: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}

	}

	public static void actionMoveToElement(WebElement element) {

		try {
			waitForElementVisibility(element);
			waitForElementToBeClickable(element);

			ac = new Actions(driver);
			ac.moveToElement(element).perform();

		} catch (Exception e) {

			String errorMessage = "Failed to move to the element: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void actionDragAndDrop(WebElement sourceElement, WebElement targetElement) {

		try {
			waitForElementVisibility(sourceElement);
			waitForElementVisibility(targetElement);

			ac = new Actions(driver);
			ac.dragAndDrop(sourceElement, targetElement).perform();

		} catch (Exception e) {

			String errorMessage = "Failed to drag and drop element: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void selectFromDropdown(WebElement element, String value, String selectionType) {

		try {
			waitForElementVisibility(element);
			sc = new Select(element);

			switch (selectionType.toLowerCase()) {
			case "index":
				sc.selectByIndex(Integer.parseInt(value));
				break;
			case "value":
				sc.selectByValue(value);
				break;
			case "text":
				sc.selectByVisibleText(value);
				break;
			default:
				throw new IllegalArgumentException(
						"Invalid selection type: " + selectionType + ". Expected 'index', 'value', or 'text'.");
			}

		} catch (Exception e) {

			String errorMessage = "Failed to select option from dropdown: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static List<String> getDropdownOptions(WebElement element) {

		try {
			waitForElementVisibility(element);
			sc = new Select(element);
			List<WebElement> options = sc.getOptions();
			List<String> optionTexts = new ArrayList<>();

			for (WebElement option : options) {
				optionTexts.add(option.getText());
			}

			return optionTexts;

		} catch (Exception e) {

			String errorMessage = "Failed to get options from dropdown: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static String getFirstSelectedOptionText(WebElement element) {

		try {
			waitForElementVisibility(element);
			sc = new Select(element);
			WebElement firstSelectedOption = sc.getFirstSelectedOption();
			return firstSelectedOption.getText();

		} catch (Exception e) {

			String errorMessage = "Failed to get the first selected option from the dropdown: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void handleAlert(WebElement element, String action) {

		try {
			element.click();

			Alert alert = driver.switchTo().alert();

			if ("accept".equalsIgnoreCase(action)) {

				alert.accept();

			} else if ("dismiss".equalsIgnoreCase(action)) {

				alert.dismiss();

			} else {

				throw new IllegalArgumentException("Invalid action: " + action + ". Expected 'accept' or 'dismiss'.");
			}

		} catch (Exception e) {

			System.err.println("Error handling alert: " + e.getMessage());

			throw new RuntimeException("Failed to handle alert.", e);
		}
	}

	public static void sendKeysToAlert(WebElement element, String value) {

		try {
			waitForElementToBeClickable(element);
			element.click();

			Alert alert = driver.switchTo().alert();
			alert.sendKeys(value);
			alert.accept();

		} catch (Exception e) {

			String errorMessage = "Failed to send keys to alert: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static boolean isElementDisplayed(WebElement element) {

		try {

			return element.isDisplayed();

		} catch (Exception e) {

			System.err.println("Error checking element visibility: " + e.getMessage());
			return false;
		}
	}

	public static boolean isElementSelected(WebElement element) {

		try {

			return element.isSelected();

		} catch (Exception e) {

			System.err.println("Error checking element selection status: " + e.getMessage());
			return false;
		}
	}

	public static boolean isElementEnabled(WebElement element) {

		try {

			return element.isEnabled();

		} catch (Exception e) {

			System.err.println("Error checking element enabled status: " + e.getMessage());
			return false;
		}
	}

	public static boolean switchToWindowByTitle(String windowTitle) {

		try {

			Set<String> windowHandles = driver.getWindowHandles();

			for (String handle : windowHandles) {

				driver.switchTo().window(handle);

				if (driver.getTitle().equals(windowTitle)) {

					return true;
				}
			}

		} catch (NoSuchWindowException e) {

			System.err.println("No window found with title: " + windowTitle + ". " + e.getMessage());
			return false;
		}
		return false;
	}

	public static boolean swithToWindowByIndex(int index) {

		try {

			Set<String> windowHandles = driver.getWindowHandles();
			List<String> windowHandlesList = new ArrayList<>(windowHandles);

			if (index >= 0 && index < windowHandlesList.size()) {

				String windowHandle = windowHandlesList.get(index);
				driver.switchTo().window(windowHandle);
				return true;
			}

		} catch (NoSuchWindowException e) {
			System.err.println("No window found at the specified index: " + index + ". " + e.getMessage());
			return false;
		}
		return false;
	}

	public static void closeCurrentWindowAndSwitchToMain() {

		try {

			Set<String> windowHandles = driver.getWindowHandles();
			String currentWindow = driver.getWindowHandle();

			if (windowHandles.size() > 1) {
				driver.close();

				Iterator<String> iterator = windowHandles.iterator();
				String mainWindowHandle = null;

				while (iterator.hasNext()) {
					String handle = iterator.next();
					if (!handle.equals(currentWindow)) {
						mainWindowHandle = handle;
						break;
					}
				}

				if (mainWindowHandle != null) {
					driver.switchTo().window(mainWindowHandle);
				} else {
					System.err.println("No other windows found to switch to.");
				}
			} else {
				System.err.println("Only one window is open. Cannot switch to another.");
			}

		} catch (NoSuchWindowException e) {
			System.err.println("Error switching to main window: " + e.getMessage());
			throw new RuntimeException("Failed to switch to main window.", e);
		}
	}

	public static boolean switchToIFrameByIndex(int index) {
		try {

			driver.switchTo().frame(index);

			return true;

		} catch (NoSuchFrameException e) {

			System.out.println("Iframe with index " + index + " not found.");

			return false;
		}
	}

	public static boolean switchToIFrameByNameOrId(String nameOrId) {

		try {

			driver.switchTo().frame(nameOrId);

			return true;

		} catch (NoSuchFrameException e) {

			System.out.println("Iframe with name or ID '" + nameOrId + "' not found.");

			return false;
		}
	}

	public static void switchToDefaultContent() {

		try {
			driver.switchTo().defaultContent();

		} catch (NoSuchFrameException e) {

			System.err.println("Error switching to default content: " + e.getMessage());
			throw new RuntimeException("Failed to switch to default content.", e);
		}

	}

	public static void switchToParentFrame() {

		try {
			driver.switchTo().parentFrame();

		} catch (NoSuchFrameException e) {

			System.err.println("Error switching to parent frame: " + e.getMessage());
			throw new RuntimeException("Failed to switch to parent frame.", e);
		}

	}

	public static void pressEnter() {

		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

		} catch (AWTException e) {
			System.err.println("Failed to press Enter key: " + e.getMessage());
			throw new RuntimeException("Failed to press Enter key using Robot class.", e);
		}
	}

	public static void pressTab() {

		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);

		} catch (AWTException e) {
			System.err.println("Failed to press Tab key: " + e.getMessage());
			throw new RuntimeException("Failed to press Tab key using Robot class.", e);
		}
	}

	public static void keyDown(int keyCode) {

		try {
			Robot robot = new Robot();
			robot.keyPress(keyCode);

		} catch (AWTException e) {
			System.err.println("Failed to press key: " + keyCode + ". " + e.getMessage());
			throw new RuntimeException("Failed to press key using Robot class.", e);
		}
	}

	public static void keyUp(int keyCode) {

		try {
			Robot robot = new Robot();
			robot.keyRelease(keyCode);

		} catch (AWTException e) {

			System.err.println("Failed to release key: " + keyCode + ". " + e.getMessage());
			throw new RuntimeException("Failed to release key using Robot class.", e);
		}
	}

	public static void navigateToUrl(String url) {

		try {

			driver.navigate().to(url);

		} catch (Exception e) {

			String errorMessage = "Failed to navigate to URL: " + url + ". " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void navigateBack() {

		try {

			driver.navigate().back();

		} catch (Exception e) {

			String errorMessage = "Failed to navigate back: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void navigateForward() {

		try {

			driver.navigate().forward();

		} catch (Exception e) {

			String errorMessage = "Failed to navigate forward: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void navigateRefresh() {

		try {

			driver.navigate().refresh();

		} catch (Exception e) {

			String errorMessage = "Failed to refresh the page: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void handleSpecificCheckBoxes(List<WebElement> checkBoxList, Set<String> valuesToSelect,
			boolean select) {
		if (checkBoxList == null || valuesToSelect == null) {
			throw new IllegalArgumentException("Checkbox list or values to select cannot be null.");
		}

		for (WebElement checkbox : checkBoxList) {
			if (checkbox == null) {
				System.err.println("Null checkbox encountered. Skipping...");
				continue;
			}

			String checkboxValue = checkbox.getDomProperty("value");
			if (checkboxValue == null || checkboxValue.isEmpty()) {
				System.err.println("Checkbox with missing or empty 'value' attribute encountered. Skipping...");
				continue;
			}

			if (valuesToSelect.contains(checkboxValue)) { // Check if the value is in the selection set
				try {
					if ((select && !checkbox.isSelected()) || (!select && checkbox.isSelected())) {
						checkbox.click();
						System.out.println("Checkbox " + (select ? "selected" : "deselected") + ": " + checkboxValue);
					} else {
						System.out.println(
								"Checkbox already " + (select ? "selected" : "deselected") + ": " + checkboxValue);
					}
				} catch (Exception e) {
					System.err.println("Error interacting with checkbox (" + checkboxValue + "): " + e.getMessage());
				}
			}
		}
	}

}
