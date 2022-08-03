package stepsDefinition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.cucumber.java.en.Then;
import io.cucumber.java.it.Quando;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Test;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import io.github.bonigarcia.wdm.WebDriverManager;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import reportDocument.Report;
import util.Utils;
import actions.LoginActions;

public class LoginSteps extends Utils {

	private String Url_Salesforce;

	String userSF;
	String senhaSF;

	String scenario;
	String DATA_PATH;
	String Navegador;
	List<Map<String, String>> ListaParam;
	List<Map<String, String>> Ambientes;
	
	WebDriver driver;
	JavascriptExecutor js;
	ChromeDriverService service;
	LoginActions logAct;
	Actions actions;
	Actions actionsIE;
	Report report;
	
	@Before
	public void inicializar() throws InvalidFormatException, IOException {
		Ambientes = ExcelReader("Ambiente");

		// URL
		Url_Salesforce = ListMapReader("1", Ambientes, "URL");

		// Dados de acesso
		userSF = ListMapReader("1", Ambientes, "Acesso");
		senhaSF = ListMapReader("2", Ambientes, "Acesso");
	}
	
	// ======= descomentar .close() e .quit() quando for realizar manutenção
	// ======== //
	@After
	public void Finalizar() throws Exception {
		report.createReportDoc(scenario);
		if (driver != null) {
//			 driver.quit();
		}
		if (service != null) {
//			service.stop();
		}
	}
	
	@Dado("que inicializei os drivers para o {string}")
	public void que_inicializei_os_driver_para_o(String id) throws Throwable {
		scenario = id;
		iniciaStatus(id);
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		
//		service = new ChromeDriverService.Builder()
//				.usingDriverExecutable(new File(getChromeDriver()))
//				.usingPort(7644)
//				.build();
		
		//Configurações do chromedriver
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("profile.managed_default_content_settings.geolocation", 2);
		options.setExperimentalOption("prefs", prefs);
		options.addArguments("user-data-dir=/src/test/resources");
		options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
		options.addArguments("test-type");
		options.addArguments("start-maximized");
		options.addArguments("--disable-geolocation");
		options.addArguments("enable-automation");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--disable-web-security");
		options.addArguments("--incognito");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-infobars");
		options.addArguments("--disable-browser-side-navigation");
		options.addArguments("--disable-gpu");
		options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
		// alterar fator de escala do navegador, caso elementos estejam muito grandes
		// options.addArguments("-force-device-scale-factor=0.8");
		if (!OS.contains("windows")) {
			options.setHeadless(true);
			options.addArguments("--window-size=1920,1080");
		}

		driver = new ChromeDriver(options);
		actions = new Actions(driver);
		logAct = new LoginActions(driver);
		report = new Report(driver);
		driver.manage().deleteAllCookies();
		js = (JavascriptExecutor) driver;
		driver.navigate().to("http://www.google.com");
	}

	@Dado("Que recebi a planilha de parametros para o {string}")
	public void Que_recebi_a_planilha_de_parametros(String DATA_PATH) throws Throwable {
		ListaParam = ExcelReader(DATA_PATH);
		scenario = DATA_PATH;
	}

	@Dado("Que ao acessar o site do salesforce")
	public void que_ao_acessar_o_site_do_salesforce() throws Throwable {
		driver.get(Url_Salesforce);
		String id = "TESTE1";
		print(report, id, "TESTE1", driver);
	}
	
	@Quando("clicar em assista a demonstracao")
	public void clicar_em_assista_a_demonstracao() {
	    logAct.acessaDemonst();
	}

	@Quando("visualizar a tela seguinte")
	public void visualizar_a_tela_seguinte() throws IOException, InterruptedException {
		String id = "TESTE2";
		print(report, id, "TESTE2", driver);
		finalizaStatus();
	}
}
