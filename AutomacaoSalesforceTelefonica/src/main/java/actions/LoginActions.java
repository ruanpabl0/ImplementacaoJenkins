package actions;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import com.aventstack.extentreports.ExtentTest;

import util.Utils;
import pageObject.LoginPageObj;

public class LoginActions extends Utils {
	
	private WebDriver driver;
	public int contadorValidaCarrinho = 0;
	int adicionalBoleto = 10;

	public int contadorValidaPedidoZeradoEComValor = 0;
	public int contadorVerificaTokenRecebido = 0;
	public int contadorValidaStatusDoPedido = 0;

	Actions actions;
	JavascriptExecutor js;

	public LoginActions(WebDriver driver) {
		this.driver = driver;
		this.actions = new Actions(driver);
	}
	
	public void acessaDemonst() {
		click(driver, LoginPageObj.login);
	}
}