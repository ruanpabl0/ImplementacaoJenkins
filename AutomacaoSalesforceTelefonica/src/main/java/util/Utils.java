package util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import io.cucumber.java.Scenario;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import pageObject.LoginPageObj;
import reportDocument.Report;
import spreadsheet.excelReader;

public class Utils {
	public static final String OS = System.getProperty("os.name").toLowerCase();

	//Indica o tipo de report que será gerado, no caso HTML
	private static ExtentHtmlReporter htmlReporter;
	//Gerenciador de reports
	private static ExtentReports extentReport;
	//É o teste propriamente dito. 
	private static ExtentTest extentTest;	
	
	String date = new SimpleDateFormat("yyyy-MM-dd HH-mm").format(Calendar.getInstance().getTime()); // para tirar os
	// prints
	JavascriptExecutor js;
	
	public static void iniciaStatus(String scenario) {
		if(extentReport == null) {
			//Inicializa a variável extentReport
			extentReport = new ExtentReports();
			//Inicializa a variável htmlReport, já passando como parâmetro o caminho onde a evidência será gerada
			htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/Evidencias/Status_Report/" + scenario + ".html");
			//Adiciona o htmlReporter ao extendReport, para que o report seja gerenciado
			extentReport.attachReporter(htmlReporter);
		}
		extentTest = extentReport.createTest(scenario);
	}
	
	public static void finalizaStatus() {
		//Indica o status da execução do cenário junto a um texto descritivo do step
		extentTest.log(Status.PASS, "Cenário executado com sucesso");
		//Realiza a gravação das informações coletadas no report
		extentReport.flush();
	}
	
	public void print(Report report, String id, String nomePrint, WebDriver driver)
			throws IOException, InterruptedException {
		Thread.sleep(800);
		extentTest.addScreenCaptureFromPath(report.capturar(id, nomePrint, driver));
		report.capturarText(id + "   " + nomePrint + "     Data e Hora: " + date, id);
//		report.capturar(id, nomePrint, driver);		
	}

	public void input(String element, String dado, WebDriver driver) {
		driver.findElement(By.id(element)).click();
		driver.findElement(By.id(element)).clear();
		driver.findElement(By.id(element)).sendKeys(dado);
		System.out.println("Dados do cliente - " + dado + " OK");
	}

	public void specialInput(String element, String dado, WebDriver driver) throws Throwable {
		driver.findElement(By.id(element)).click();
		driver.findElement(By.id(element)).clear();

		WebElement elemento = driver.findElement(By.id(element));
		char[] text = dado.toCharArray();
		for (char a : text) {
			String s = String.valueOf(a);
			elemento.sendKeys(s);
			Thread.sleep(60);
		}
		System.out.println("Dados do cliente - " + dado + " OK");
	}

	public void specialInputt(By by, String dado, WebDriver driver) throws Throwable {

		try {
			WebDriverWait wait = new WebDriverWait(driver, 40);
			wait.until(ExpectedConditions.elementToBeClickable(by));
			WebElement elemento = driver.findElement(by);
//			elemento.click();
			elemento.clear();

			char[] text = dado.toCharArray();
			for (char a : text) {
				String s = String.valueOf(a);
				elemento.sendKeys(s);
				Thread.sleep(60);
			}
		} catch (Exception e) {
			Assert.fail("Erro na acao de click? " + e.toString());
		}

	}

	public void selection(String element, String dado, WebDriver driver) {
		new Select(driver.findElement(By.id(element))).selectByVisibleText(dado);
		System.out.println("Dados do cliente - " + dado + " OK");
	}

	public List<Map<String, String>> ExcelReader(String DATA_PATH) throws IOException, InvalidFormatException {
		excelReader externalData = new excelReader();
		DATA_PATH = System.getProperty("user.dir") + "/Evidencias/spreadsheets/" + DATA_PATH + ".xlsx";
		List<Map<String, String>> testData = externalData.getData(DATA_PATH, 0);
		return testData;
	}

	public String ListMapReader(String Index, List<Map<String, String>> testData, String Coluna) {
		int Row = Integer.parseInt(Index);
		String Data = testData.get(Row).get(Coluna).trim();
		return Data;
	}

	@SuppressWarnings("deprecation")
	public static WebElement findElement(WebDriver driver, final By by) {

		int i = 1;
		int Constantes_MAX_SEGUNDOS = 2;
		WebElement element = null;
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)/*
																									 * Esperando 25
																									 * segundos para que
																									 * o elemento esteja
																									 * presente na
																									 * página
																									 */
				.pollingEvery(1, TimeUnit.SECONDS)/* Verificando por sua presença uma vez a cada 1 segundos */
				.ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class)
				.ignoring(TimeoutException.class);
		try {
			while (element == null && i < Constantes_MAX_SEGUNDOS) {
				try {
					if (null != ExpectedConditions.visibilityOfElementLocated(by)) {
						element = wait.until(new Function<WebDriver, WebElement>() {
							public WebElement apply(WebDriver driver) {
								return driver.findElement(by);
							}
						});
						if (element.getText().equals("Nenhum registro encontrado")) {
							element = null;
						}
					}
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
					i++;
					element = null;
					continue;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.assertNotNull("Elemento não foi encontrado", ex);
		}
		return element;
	}

	@SuppressWarnings("deprecation")
	public static WebElement findElement(WebDriver driver, final By by, int timeout) {

		int i = 1;
		int Constantes_MAX_SEGUNDOS = 2;
		WebElement element = null;
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeout, TimeUnit.SECONDS)/*
																										 * Esperando 25
																										 * segundos para
																										 * que o
																										 * elemento
																										 * esteja
																										 * presente na
																										 * página
																										 */
				.pollingEvery(1, TimeUnit.SECONDS)/* Verificando por sua presença uma vez a cada 1 segundos */
				.ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class)
				.ignoring(TimeoutException.class);
		try {
			while (element == null && i < Constantes_MAX_SEGUNDOS) {
				try {
					if (null != ExpectedConditions.visibilityOfElementLocated(by)) {
						element = wait.until(new Function<WebDriver, WebElement>() {
							public WebElement apply(WebDriver driver) {
								return driver.findElement(by);
							}
						});
						if (element.getText().equals("Nenhum registro encontrado")) {
							element = null;
						}
					}
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
					i++;
					element = null;
					continue;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.assertNotNull("Elemento não foi encontrado", ex);
		}
		return element;
	}

	public static String getText(WebDriver driver, By by) {
		WebElement element = null;
		element = findElement(driver, by);
		String elementText = element.getText();

		return elementText;
	}

	public static String getValue(WebDriver driver, By by) {
		WebElement element = null;
		element = findElement(driver, by);
		String elementText = element.getAttribute("value");

		return elementText;
	}

	public static String getCssValue(WebDriver driver, By by, String elemento) {
		WebElement element = null;
		element = findElement(driver, by);
		String elementText = element.getCssValue(elemento);

		return elementText;
	}

	public static String getChecked(WebDriver driver, By by) {
		WebElement element = null;
		element = findElement(driver, by);
		String elementText = element.getAttribute("checked").toString();

		return elementText;
	}

	public static String getIneerText(WebDriver driver, By by) {
		WebElement element = null;
		element = findElement(driver, by);
		String elementText = element.getAttribute("innerText");

		return elementText;
	}

	public static String getTextContent(WebDriver driver, By by) {
		WebElement element = null;
		element = findElement(driver, by);
		String elementText = element.getAttribute("textContent");

		return elementText;
	}

	public WebElement fillInput(WebDriver driver, By by, String valor) {

		int timeout = 2;
		while (!(timeout <= 0)) {
			timeout--;
			try {

				WebDriverWait wait = new WebDriverWait(driver, 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(by));

				WebElement element = findElement(driver, by);
				element.click();
				element.clear();
				char[] text = valor.toCharArray();
				for (char a : text) {
					String s = String.valueOf(a);
					element.sendKeys(s);
					Thread.sleep(60);
				}
				return element;

			} catch (Exception e) {
				System.out.println("Erro na acao de input do framework: " + e);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public WebElement fillInputSelect(WebDriver driver, By by, String opcao) {
		int timeout = 3;
		while (!(timeout <= 0)) {
			timeout--;
			try {
				WebElement element = null;
				Select select = null;
				element = findElement(driver, by);
				select = new Select(element);
				select.selectByVisibleText(opcao);
				return element;

			} catch (Exception e) {
				System.out.println(e.getCause());
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void selectElementByValue(WebDriver driver, By by, String value) {
		WebElement element = driver.findElement(by);
		Select selectItem = new Select(element);
		selectItem.selectByValue(value);
	}

	public static void selectElementVisibleText(WebDriver driver, By by, String Name) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(by));
		WebElement element = driver.findElement(by);
		Select selectitem = new Select(element);
		selectitem.selectByVisibleText(Name);
	}

	public static void click(WebDriver driver, By by) {
		WebElement element = null;
		int timeout = 3;
		while (!(timeout <= 0)) {
			timeout--;
			try {
				element = (new WebDriverWait(driver, 3)).until(ExpectedConditions.elementToBeClickable(by));
				Actions actions = new Actions(driver);
				WebElement mainMenu = element;
				actions.moveToElement(mainMenu).build().perform();
				Thread.sleep(1000);
				element.click();
				return;
			} catch (AssertionError | Exception e) {
				System.err.println("Erro na acao de click: " + e + " Elemento: " + element);
				driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
				if(timeout == 0) {
					//Indica o status da execução do cenário junto a um texto descritivo do step
					extentTest.log(Status.FAIL, "Cenário executado sem sucesso - Erro na ação de clique");
					//Realiza a gravação das informações coletadas no report
					extentReport.flush();
				}
			}
		}
		 Assert.fail("Erro na acao de click do framework: " + element);
	}

	public static void click(WebDriver driver, By by, int timeWait) {
		WebElement element = null;
		int timeout = 2;
		while (!(timeout <= 0)) {
			timeout--;
			try {
				element = (new WebDriverWait(driver, timeWait)).until(ExpectedConditions.elementToBeClickable(by));
				Actions actions = new Actions(driver);
				WebElement mainMenu = element;
				actions.moveToElement(mainMenu).build().perform();
				Thread.sleep(1000);
				element.click();
				return;
			} catch (Exception e) {
				System.err.println("Erro na acao de click: " + element);
				driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			}
		}
		Assert.fail("Erro na acao de click do framework: " + element);
	}

	/**
	 * Aguarda o Iframe e da switchTo.
	 * 
	 * @param driver
	 * @param by
	 */
	public void abreIframe(WebDriver driver, By by) {
		(new WebDriverWait(driver, 60)).until(ExpectedConditions.presenceOfElementLocated(by));
		driver.switchTo().frame(driver.findElement(by));
		driver.switchTo().activeElement();
	}

	/**
	 * Da switchTo default
	 * 
	 * @param driver
	 */
	public void fechaIframe(WebDriver driver) {
		driver.switchTo().defaultContent();
	}

	/**
	 * Abre uma nova aba no navegador.
	 * 
	 * @throws AWTException
	 * @throws InterruptedException
	 */
	public void abrirNovaAba() throws AWTException, InterruptedException {
		Thread.sleep(2000);
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_T);
		Thread.sleep(500);
		robot.keyRelease(KeyEvent.VK_T);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}

	/**
	 * Abre uma nova aba no navegador, utilizando javascripexecutor
	 * 
	 * @throws AWTException
	 * @throws InterruptedException
	 * 
	 */
	public void abrirNovaAba(WebDriver driver) throws AWTException, InterruptedException {
		Thread.sleep(2000);
		((JavascriptExecutor) driver).executeScript("window.open();");
	}

	/**
	 * Alterna entre as abas abertas no navegador, passando como parametro o indice
	 * da aba desejada e o numero de abas que ficará abertas após a chamada.
	 * 
	 * @param driver
	 * @param indice
	 */
	public void alterarAba(WebDriver driver, int indice, int quantAbas) {
		// WebDriverWait wait = new WebDriverWait(driver, 10);
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.numberOfWindowsToBe(quantAbas));

		List<String> windowHandles = new ArrayList<>(driver.getWindowHandles());
		driver.switchTo().window(windowHandles.get(indice));
	}

	public void refresh(WebDriver driver) {
		driver.navigate().refresh();
	}

	public void waitPreenchimento(WebDriver driver, By by) throws InterruptedException {

		String campo = getText(driver, by);
		int timeOut = 0;
		while ((campo.equals("")) && (timeOut <= 10)) {
			Thread.sleep(3000);
			campo = getText(driver, by);
			timeOut++;
		}
	}

	public void focar(WebDriver driver, By by) {
		js = (JavascriptExecutor) driver;
		WebElement element = null;
		element = findElement(driver, by);
		js.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	/**
	 * Converte a string em Double com o valor desejato e soma o segundo
	 * par�metro(int) ao Double recem convertidos. retornando o valor final como
	 * String.
	 * 
	 * @param valor
	 * @param adicional
	 * @return
	 */
	public static String converteESoma(String valor, int adicional) {
		valor = valor.replace(",", ".");
		Double valorConvertido = Double.parseDouble(valor);
		valorConvertido = valorConvertido + adicional;
		valor = String.valueOf(valorConvertido);
		String valorFinal = valor.replace(".", ",").replace("000000000001", "");

		return valorFinal;
	}

	public static String converteESubtrai(String valor, int desconto) {
		valor = valor.replace(",", ".");
		Double valorConvertido = Double.parseDouble(valor);
		valorConvertido = valorConvertido - desconto;
		valor = String.valueOf(valorConvertido);
		String valorFinal = valor.replace(".", ",").replace("000000000001", "");

		return valorFinal;
	}

	public static String converteIntESoma(String valor, int adicional) {
		int valorConvertido = Integer.parseInt(valor);
		valorConvertido = valorConvertido + adicional;
		valor = Integer.toString(valorConvertido).replace("000000000001", "");
		return valor;
	}

	public int exist(WebDriver driver, By by) {
		int element = driver.findElements(by).size();

		System.err.println("Numero de elementos detectados: " + element);

		return element;
	}

	public static String montarXpath(String coisinho, String tag) {
		String[] splitCoiso = coisinho.split(" ");
		int tamanhoSplitCoiso = splitCoiso.length;
		String xpath = "//" + tag + "[";
		for (int i = 0; i <= tamanhoSplitCoiso - 1; i = i + 1) {
			if (i == tamanhoSplitCoiso - 1) {
				xpath = xpath + "contains(text(), '" + splitCoiso[i] + "')]";
			} else {
				xpath = xpath + "contains(text(), '" + splitCoiso[i] + "') and ";
			}
		}
		return xpath;
	}

	public void viewElementScroll(WebDriver driver, By by) throws Throwable {
		js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, 0)");
		Thread.sleep(900);
		WebElement Element2 = driver.findElement(by);
		js.executeScript("arguments[0].scrollIntoView();", Element2);
		Thread.sleep(900);
	}

	public void clickjs(WebDriver driver, By by) throws InterruptedException {
		WebElement Element2 = (new WebDriverWait(driver, 3)).until(ExpectedConditions.elementToBeClickable(by));
		js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, 0)");
		Thread.sleep(900);
		Element2 = driver.findElement(by);
		js.executeScript("arguments[0].scrollIntoView();", Element2);
		Thread.sleep(900);
		js.executeScript("arguments[0].click();", Element2);
	}

	public void mouseGlide(int x1, int y1, int x2, int y2, int t, int n) {
		try {
			Robot r = new Robot();
			double dx = (x2 - x1) / ((double) n);
			double dy = (y2 - y1) / ((double) n);
			double dt = t / ((double) n);
			for (int step = 1; step <= n; step++) {
				Thread.sleep((int) dt);
				r.mouseMove((int) (x1 + dx * step), (int) (y1 + dy * step));
			}
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static boolean waitTextToBe(WebDriver driver, By by, String attributeValueExpected, int timeout) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			wait.until(ExpectedConditions.textToBe(by, attributeValueExpected));
			return true;

		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}

	public static boolean waitTextContains(WebDriver driver, By by, String attributeValueExpected, int timeout) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			wait.until(ExpectedConditions.textToBePresentInElement(null, attributeValueExpected));
			return true;

		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}

	/**
	 * Verificar se após determinado tempo o elemento está visível
	 * 
	 * @param driver      Webdriver
	 * @param by          Locator do elemento
	 * @param tempoEspera Tempo maximo de espera para elemento ser exibido na página
	 * @return boolean
	 */
	public static void scrollToViewJS(WebDriver driver, By by, int tempoEspera) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, tempoEspera);
			wait.until(ExpectedConditions.presenceOfElementLocated(by));

			WebElement element = driver.findElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

		} catch (Exception e) {
			Assert.fail("Erro ação de scroll, elemento - " + e.toString());
		}

	}

	/**
	 * Método responsável pelo preenchimento de campos do tipo INPUT atráves do js.
	 * Utiliza o By para localizar o componente.
	 * 
	 * @param driver Webdriver
	 * @param by     Locator do elemento
	 * @param text   Valor a ser preenchido no campo
	 * @param time   Tempo maximo de espera para elemento ser exibido na página
	 * @return void
	 */
	public static void fillInputjs(WebDriver driver, By locator, String text, int time) {

		WebDriverWait wait = new WebDriverWait(driver, time);
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		WebElement element = driver.findElement(locator);

		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].value='" + text + "'", element);
	}

	public static int gerarRandom() {
		Random random = new Random();
		int numero = random.nextInt(92839) + 1234;
		return numero;
	}

	public static boolean isElementoPresente(WebDriver driver, By by, int tempoEspera, boolean isQuerMover) {
		boolean isPresente;
		try {
			WebDriverWait wait = new WebDriverWait(driver, tempoEspera);
			wait.until(ExpectedConditions.presenceOfElementLocated(by));

			WebElement element = driver.findElement(by);

			if (isQuerMover) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			}

			isPresente = true;

		} catch (Exception e) {
			isPresente = false;

		}
		return isPresente;
	}

	public String getChromeDriver() {
		final String cwd = System.getProperty("user.dir");

		if (OS.contains("windows")) {
			return cwd.concat("/src/test/resources/chromedriver.exe");
		}

		return "/usr/bin/chromedriver";
	}
}
