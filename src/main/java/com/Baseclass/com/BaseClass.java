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
import org.testng.annotations.BeforeSuite;
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

	@BeforeSuite
	public static void setUpSuite() {
		try {
			extentReportManager = new ExtentReportManager();
			ExtentReportManager.createInstance();

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
				ExtentReportManager.log(Status.INFO, "Chrome browser initialized successfully.");
				break;
			case "firefox":
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver();
				ExtentReportManager.log(Status.INFO, "Firefox browser initialized successfully.");
				break;
			case "edge":
				WebDriverManager.edgedriver().setup();
				driver = new EdgeDriver();
				ExtentReportManager.log(Status.INFO, "Edge browser initialized successfully.");
				break;
			default:
				throw new Exception("Invalid Browser name Entered: " + browserName);
			}

			driver.manage().window().maximize();
			ExtentReportManager.log(Status.INFO, "Browser window maximized.");

			return driver;
		} catch (Exception e) {
			ExtentReportManager.log(Status.FAIL,
					"Failed to initialize WebDriver for browser: " + browserName + " | Error: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("Failed to initialize WebDriver for browser: " + browserName, e);
		}
	}

	public static void navigateGetUrl(String url) throws Exception {
		try {
			driver.get(url);
			ExtentReportManager.log(Status.INFO, "Navigated to URL: " + url);
		} catch (Exception e) {
			String errorMessage = "Failed to navigate to URL: " + url + ". " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new Exception(errorMessage, e);
		}
	}

	public static void setText(WebElement element, String value) throws Exception {
		waitForElementVisibility(element);

		try {
			element.clear();
			element.sendKeys(value);
			ExtentReportManager.log(Status.INFO, "Set text '" + value + "' in element: " + element);
		} catch (Exception e) {
			String errorMessage = "Failed to set text to element: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new Exception(errorMessage, e);
		}
	}

	public static void waitForElementVisibility(WebElement element) {

		try {

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));

			ExtentReportManager.log(Status.INFO, "Element is visible: " + element);

		} catch (Exception e) {
			String errorMessage = "Element not visible within timeout: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void waitForElementToBeClickable(WebElement element) {

		try {

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.elementToBeClickable(element));

			ExtentReportManager.log(Status.INFO, "Element is clickable: " + element);

		} catch (Exception e) {
			String errorMessage = "Element is not clickable within timeout: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void waitForElementToBePresent(By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));

			ExtentReportManager.log(Status.INFO, "Element is present: " + locator);

		} catch (Exception e) {
			String errorMessage = "Element not found within timeout: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void clickElement(WebElement element) {

		try {

			waitForElementToBeClickable(element);

			element.click();
			ExtentReportManager.log(Status.PASS, "Clicked on element: " + element.toString());
		} catch (Exception e) {

			String errorMessage = "Failed to click on element: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static String getTextFromElement(WebElement element) {
		try {
			waitForElementVisibility(element); // Ensure the element is visible
			String text = element.getText();

			ExtentReportManager.log(Status.PASS, "Retrieved text from element: '" + text + "'");

			return text;

		} catch (Exception e) {
			String errorMessage = "Failed to get text from element: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static String getAttributeValue(WebElement element, String attributeName) {

		try {
			waitForElementVisibility(element);
			String attributeValue = element.getDomAttribute(attributeName);
			if (attributeValue != null) {
				ExtentReportManager.log(Status.PASS,
						"Retrieved attribute '" + attributeName + "' value: '" + attributeValue + "'");
				return attributeValue;
			} else {
				String warningMessage = "Attribute '" + attributeName + "' not found on the element.";
				System.out.println(warningMessage);

				ExtentReportManager.log(Status.WARNING, warningMessage);
				return null;
			}
		} catch (Exception e) {
			String errorMessage = "Failed to get attribute '" + attributeName + "' from element: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static boolean takeScreenshot(String screenShotPath) throws IOException {

		if (driver == null) {
			System.err.println("Driver is null. Cannot take a screenshot.");
			return false;
		}

		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File screenshotAs = ts.getScreenshotAs(OutputType.FILE);
			File saveFile = new File(screenShotPath);

			// Ensure parent directories exist
			saveFile.getParentFile().mkdirs();

			FileUtils.copyFile(screenshotAs, saveFile);
			System.out.println("Screenshot saved to: " + screenShotPath);

			if (ExtentReportManager.test != null) {
				ExtentReportManager.test.get().addScreenCaptureFromPath(screenShotPath);
				ExtentReportManager.log(Status.INFO, "Screenshot captured: " + screenShotPath);
			}

			return true; // Return true on success
		} catch (Exception e) {
			String errorMessage = "Failed to take screenshot: " + e.getMessage();
			System.err.println(errorMessage);
			throw new IOException(errorMessage, e);
		}
	}

	public static void closeCurrentBrowser(ITestResult result, String screenShotPath) {
		try {
			if (result.getStatus() == ITestResult.FAILURE) {
				String failureMessage = "Test Failed: "
						+ (result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown error");

				ExtentReportManager.log(Status.FAIL, failureMessage);

				// Take screenshot only if path is valid
				if (screenShotPath != null && !screenShotPath.trim().isEmpty() && takeScreenshot(screenShotPath)) {
					ExtentReportManager.test.get().addScreenCaptureFromPath(screenShotPath);
				}
			} else if (result.getStatus() == ITestResult.SUCCESS) {
				ExtentReportManager.log(Status.PASS, "Test Passed");
			} else {
				ExtentReportManager.log(Status.SKIP, "Test Skipped");
			}
		} catch (Exception e) {
			String errorMessage = "Failed to close the current browser: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		} finally {
			// Ensure the driver is closed
			if (driver != null) {
				driver.close();
			}
		}
	}

	public static void closeAllBrowsers(ITestResult result, String screenShotPath) {
		try {
			if (result.getStatus() == ITestResult.FAILURE) {
				// Capture screenshot only if the path is valid
				boolean screenshotTaken = (screenShotPath != null && !screenShotPath.trim().isEmpty())
						&& takeScreenshot(screenShotPath);

				String failureMessage = "Test Failed: "
						+ (result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown error");

				ExtentReportManager.log(Status.FAIL, failureMessage);

				if (screenshotTaken) {
					ExtentReportManager.test.get().addScreenCaptureFromPath(screenShotPath);
				}
			} else if (result.getStatus() == ITestResult.SUCCESS) {
				ExtentReportManager.log(Status.PASS, "Test Passed");
			} else {
				ExtentReportManager.log(Status.SKIP, "Test Skipped");
			}
		} catch (Exception e) {
			String errorMessage = "Failed to close all browsers: " + e.getMessage();
			System.err.println(errorMessage);
			throw new RuntimeException(errorMessage, e);
		} finally {
			// Ensure the driver is closed
			if (driver != null) {
				driver.quit();
			}
		}
	}

	public static void actionLeftClick(WebElement element) {

		try {
			waitForElementToBeClickable(element);

			ac = new Actions(driver);
			ac.click(element).perform();
			ExtentReportManager.log(Status.PASS,
					"Successfully performed left-click on the element: " + element.toString());

		} catch (Exception e) {
			String errorMessage = "Failed to perform lefy click on element: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void actionRightClick(WebElement element) {

		try {
			waitForElementToBeClickable(element);

			ac = new Actions(driver);
			ac.contextClick(element).perform();
			ExtentReportManager.log(Status.PASS,
					"Successfully performed right-click on the element: " + element.toString());

		} catch (Exception e) {

			String errorMessage = "Failed to perform right-click on element: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void actionDoubleClick(WebElement element) {

		try {
			waitForElementToBeClickable(element);

			ac = new Actions(driver);
			ac.doubleClick(element).perform();
			ExtentReportManager.log(Status.PASS,
					"Successfully performed double-click on the element: " + element.toString());

		} catch (Exception e) {

			String errorMessage = "Failed to perform double-click on element: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void actionMoveToElement(WebElement element) {

		try {
			waitForElementVisibility(element);
			waitForElementToBeClickable(element);

			ac = new Actions(driver);
			ac.moveToElement(element).perform();
			ExtentReportManager.log(Status.PASS, "Successfully moved to the element: " + element.toString());
		} catch (Exception e) {

			String errorMessage = "Failed to move to the element: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void actionDragAndDrop(WebElement sourceElement, WebElement targetElement) {

		try {
			waitForElementVisibility(sourceElement);
			waitForElementVisibility(targetElement);

			ac = new Actions(driver);
			ac.dragAndDrop(sourceElement, targetElement).perform();
			ExtentReportManager.log(Status.PASS, "Successfully dragged element: " + sourceElement.toString()
					+ " and dropped it on: " + targetElement.toString());

		} catch (Exception e) {

			String errorMessage = "Failed to drag and drop element: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
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
				ExtentReportManager.log(Status.PASS, "Selected option by index: " + value);
				break;
			case "value":
				sc.selectByValue(value);
				ExtentReportManager.log(Status.PASS, "Selected option by value: " + value);
				break;
			case "text":
				sc.selectByVisibleText(value);
				ExtentReportManager.log(Status.PASS, "Selected option by visible text: " + value);
				break;
			default:
				String invalidMessage = "Invalid selection type: " + selectionType
						+ ". Expected 'index', 'value', or 'text'.";
				ExtentReportManager.log(Status.FAIL, invalidMessage);
				throw new IllegalArgumentException(invalidMessage);
			}

		} catch (Exception e) {

			String errorMessage = "Failed to select option from dropdown: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
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

			ExtentReportManager.log(Status.PASS, "Dropdown options retrieved successfully: " + optionTexts);
			return optionTexts;
		} catch (Exception e) {
			String errorMessage = "Failed to get options from dropdown: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static String getFirstSelectedOptionText(WebElement element) {

		try {
			waitForElementVisibility(element);
			sc = new Select(element);
			WebElement firstSelectedOption = sc.getFirstSelectedOption();
			String selectedText = firstSelectedOption.getText();
			ExtentReportManager.log(Status.PASS, "First selected option retrieved successfully: " + selectedText);

			return selectedText;

		} catch (Exception e) {

			String errorMessage = "Failed to get the first selected option from the dropdown: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void handleAlert(WebElement element, String action) {

		try {
			element.click();

			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();

			if ("accept".equalsIgnoreCase(action)) {
				alert.accept();
				ExtentReportManager.log(Status.PASS, "Alert accepted successfully. Alert message: " + alertText);
			} else if ("dismiss".equalsIgnoreCase(action)) {
				alert.dismiss();
				ExtentReportManager.log(Status.PASS, "Alert dismissed successfully. Alert message: " + alertText);
			} else {

				throw new IllegalArgumentException("Invalid action: " + action + ". Expected 'accept' or 'dismiss'.");
			}

		} catch (Exception e) {

			String errorMessage = "Failed to handle alert: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, "Error handling alert: " + e.getMessage());
			throw new RuntimeException("Failed to handle alert.", e);
		}
	}

	public static void sendKeysToAlert(WebElement element, String value) {

		try {
			waitForElementToBeClickable(element);
			element.click();

			Alert alert = driver.switchTo().alert();
			alert.sendKeys(value);
			ExtentReportManager.log(Status.INFO, "Entered text into alert: " + value);
			alert.accept();
			ExtentReportManager.log(Status.PASS, "Alert accepted successfully after entering text.");

		} catch (Exception e) {

			String errorMessage = "Failed to send keys to alert: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, "Error while sending keys to alert: " + e.getMessage());
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static boolean isElementDisplayed(WebElement element) {

		try {
			boolean isDisplayed = element.isDisplayed();
			if (isDisplayed) {
				ExtentReportManager.log(Status.PASS, "Element is displayed: " + element.toString());
			} else {
				ExtentReportManager.log(Status.WARNING, "Element is not displayed: " + element.toString());
			}
			return isDisplayed;
		} catch (Exception e) {

			String errorMessage = "Error checking element visibility: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, "Failed to check element visibility: " + e.getMessage());

			return false;
		}
	}

	public static boolean isElementSelected(WebElement element) {

		try {
			boolean isSelected = element.isSelected();

			if (isSelected) {
				ExtentReportManager.log(Status.PASS, "Element is selected: " + element.toString());
			} else {
				ExtentReportManager.log(Status.WARNING, "Element is not selected: " + element.toString());
			}
			return isSelected;

		} catch (Exception e) {

			String errorMessage = "Error checking element selection status: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, "Failed to check element selection status: " + e.getMessage());

			return false;
		}
	}

	public static boolean isElementEnabled(WebElement element) {

		try {
			boolean isEnabled = element.isEnabled();

			if (isEnabled) {
				ExtentReportManager.log(Status.PASS, "Element is enabled: " + element.toString());
			} else {
				ExtentReportManager.log(Status.WARNING, "Element is disabled: " + element.toString());
			}
			return isEnabled;

		} catch (Exception e) {

			String errorMessage = "Error checking element enabled status: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, "Failed to check element enabled status: " + e.getMessage());

			return false;
		}
	}

	public static boolean switchToWindowByTitle(String windowTitle) {

		try {

			Set<String> windowHandles = driver.getWindowHandles();

			for (String handle : windowHandles) {

				driver.switchTo().window(handle);

				if (driver.getTitle().equals(windowTitle)) {

					ExtentReportManager.log(Status.PASS, "Switched to window with title: " + windowTitle);
					return true;
				}
			}

			ExtentReportManager.log(Status.FAIL, "No window found with title: " + windowTitle);
			return false;

		} catch (NoSuchWindowException e) {

			String errorMessage = "No window found with title: " + windowTitle + ". " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);

			return false;
		}
	}

	public static boolean swithToWindowByIndex(int index) {

		try {

			Set<String> windowHandles = driver.getWindowHandles();
			List<String> windowHandlesList = new ArrayList<>(windowHandles);

			if (index >= 0 && index < windowHandlesList.size()) {

				String windowHandle = windowHandlesList.get(index);
				driver.switchTo().window(windowHandle);
				ExtentReportManager.log(Status.PASS, "Switched to window at index: " + index);
				return true;
			} else {
				ExtentReportManager.log(Status.FAIL, "Invalid window index: " + index);
				return false;
			}

		} catch (NoSuchWindowException e) {
			String errorMessage = "No window found at the specified index: " + index + ". " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);

			return false;
		}
	}

	public static void closeCurrentWindowAndSwitchToMain() {

		try {

			Set<String> windowHandles = driver.getWindowHandles();
			String currentWindow = driver.getWindowHandle();

			if (windowHandles.size() > 1) {
				driver.close();
				ExtentReportManager.log(Status.INFO, "Closed the current window: " + currentWindow);

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
					ExtentReportManager.log(Status.PASS, "Switched to main window: " + mainWindowHandle);
				} else {
					String errorMessage = "No other windows found to switch to.";
					System.err.println(errorMessage);
					ExtentReportManager.log(Status.FAIL, errorMessage);
				}
			} else {
				String errorMessage = "Only one window is open. Cannot switch to another.";
				System.err.println(errorMessage);
				ExtentReportManager.log(Status.FAIL, errorMessage);
			}

		} catch (NoSuchWindowException e) {
			String errorMessage = "Error switching to main window: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException("Failed to switch to main window.", e);
		}
	}

	public static boolean switchToIFrameByIndex(int index) {
		try {

			driver.switchTo().frame(index);

			String successMessage = "Switched to iframe with index: " + index;
			System.out.println(successMessage);
			ExtentReportManager.log(Status.PASS, successMessage);
			return true;
		} catch (NoSuchFrameException e) {

			String errorMessage = "Iframe with index " + index + " not found. " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);

			return false;
		}
	}

	public static boolean switchToIFrameByNameOrId(String nameOrId) {

		try {

			driver.switchTo().frame(nameOrId);

			String successMessage = "Switched to iframe with name or ID: " + nameOrId;
			System.out.println(successMessage);
			ExtentReportManager.log(Status.PASS, successMessage);
			return true;

		} catch (NoSuchFrameException e) {

			String errorMessage = "Iframe with name or ID '" + nameOrId + "' not found. " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);

			return false;
		}
	}

	public static void switchToDefaultContent() {

		try {
			driver.switchTo().defaultContent();

			String successMessage = "Switched to default content successfully.";
			System.out.println(successMessage);
			ExtentReportManager.log(Status.PASS, successMessage);

		} catch (NoSuchFrameException e) {
			String errorMessage = "Error switching to default content: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException("Failed to switch to default content.", e);
		}
	}

	public static void switchToParentFrame() {

		try {
			driver.switchTo().parentFrame();

			String successMessage = "Switched to parent frame successfully.";
			System.out.println(successMessage);
			ExtentReportManager.log(Status.PASS, successMessage);

		} catch (NoSuchFrameException e) {
			String errorMessage = "Error switching to parent frame: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException("Failed to switch to parent frame.", e);
		}
	}

	public static void pressEnter() {

		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

			String successMessage = "Pressed Enter key successfully.";
			System.out.println(successMessage);
			ExtentReportManager.log(Status.PASS, successMessage);

		} catch (AWTException e) {
			String errorMessage = "Failed to press Enter key: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException("Failed to press Enter key using Robot class.", e);
		}
	}

	public static void pressTab() {

		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);

			String successMessage = "Pressed Tab key successfully.";
			System.out.println(successMessage);
			ExtentReportManager.log(Status.PASS, successMessage);

		} catch (AWTException e) {
			String errorMessage = "Failed to press Tab key: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException("Failed to press Tab key using Robot class.", e);
		}
	}

	public static void keyDown(int keyCode) {

		try {
			Robot robot = new Robot();
			robot.keyPress(keyCode);

			String successMessage = "Pressed key with keyCode: " + keyCode + " successfully.";
			System.out.println(successMessage);
			ExtentReportManager.log(Status.PASS, successMessage);

		} catch (AWTException e) {
			String errorMessage = "Failed to press key with keyCode: " + keyCode + ". " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException("Failed to press key using Robot class.", e);
		}
	}

	public static void keyUp(int keyCode) {

		try {
			Robot robot = new Robot();
			robot.keyRelease(keyCode);

			String successMessage = "Released key with keyCode: " + keyCode + " successfully.";
			System.out.println(successMessage);
			ExtentReportManager.log(Status.PASS, successMessage);

		} catch (AWTException e) {

			String errorMessage = "Failed to release key with keyCode: " + keyCode + ". " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException("Failed to release key using Robot class.", e);
		}
	}

	public static void navigateToUrl(String url) {

		try {

			driver.navigate().to(url);
			String successMessage = "Navigated to URL: " + url + " successfully.";
			System.out.println(successMessage);
			ExtentReportManager.log(Status.PASS, successMessage);

		} catch (Exception e) {
			String errorMessage = "Failed to navigate to URL: " + url + ". " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void navigateBack() {

		try {

			driver.navigate().back();
			String successMessage = "Navigated back successfully.";
			System.out.println(successMessage);
			ExtentReportManager.log(Status.PASS, successMessage);

		} catch (Exception e) {

			String errorMessage = "Failed to navigate back: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void navigateForward() {

		try {

			driver.navigate().forward();

			String successMessage = "Navigated forward successfully.";
			System.out.println(successMessage);
			ExtentReportManager.log(Status.PASS, successMessage);

		} catch (Exception e) {

			String errorMessage = "Failed to navigate forward: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void navigateRefresh() {

		try {

			driver.navigate().refresh();
			String successMessage = "Page refreshed successfully.";
			System.out.println(successMessage);
			ExtentReportManager.log(Status.PASS, successMessage);

		} catch (Exception e) {

			String errorMessage = "Failed to refresh the page: " + e.getMessage();
			System.err.println(errorMessage);
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new RuntimeException(errorMessage, e);
		}
	}

	public static void handleSpecificCheckBoxes(List<WebElement> checkBoxList, Set<String> valuesToSelect,
			boolean select) {
		if (checkBoxList == null || valuesToSelect == null) {
			String errorMessage = "Checkbox list or values to select cannot be null.";
			ExtentReportManager.log(Status.FAIL, errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}

		for (WebElement checkbox : checkBoxList) {
			if (checkbox == null) {
				String warningMessage = "Null checkbox encountered. Skipping...";
				System.err.println(warningMessage);
				ExtentReportManager.log(Status.WARNING, warningMessage);
				continue;
			}

			String checkboxValue = checkbox.getDomProperty("value");
			if (checkboxValue == null || checkboxValue.trim().isEmpty()) {
				String warningMessage = "Checkbox with missing or empty 'value' attribute encountered. Skipping...";
				System.err.println(warningMessage);
				ExtentReportManager.log(Status.WARNING, warningMessage);
				continue;
			}

			if (valuesToSelect.contains(checkboxValue)) { // Check if the value is in the selection set
				try {
					if ((select && !checkbox.isSelected()) || (!select && checkbox.isSelected())) {
						checkbox.click();
						String successMessage = "Checkbox " + (select ? "selected" : "deselected") + ": "
								+ checkboxValue;
						System.out.println(successMessage);
						ExtentReportManager.log(Status.PASS, successMessage);
					} else {
						String infoMessage = "Checkbox already " + (select ? "selected" : "deselected") + ": "
								+ checkboxValue;
						System.out.println(infoMessage);
						ExtentReportManager.log(Status.INFO, infoMessage);
					}
				} catch (Exception e) {
					String errorMessage = "Error interacting with checkbox (" + checkboxValue + "): " + e.getMessage();
					System.err.println(errorMessage);
					ExtentReportManager.log(Status.FAIL, errorMessage);
				}
			}
		}
	}
}
