package reportDocument;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TakesScreenshot;
import constantsSuite.ConstantsReport;
import util.Utils;

public class Report {

	WebDriver driver;
	String pathPrint = ConstantsReport.PRINTS_PATH;
	static ArrayList<String> prints;
	ArrayList<String> texts;	

	/**
	 * Construtor da classe Report
	 * 
	 * @param Webdriver driver
	 * @author rodrigo.c.almeida
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public Report(WebDriver driver) {
		this.driver = driver;
		this.prints = new ArrayList();
		this.texts = new ArrayList();
	}

	/**
	 * Método para capturar prints que serão utilizados nos relatórios DOC e PDF.
	 * 
	 **/
	public String capturar(String id, String nomePrint, WebDriver driver) {
		String dateDay = new SimpleDateFormat("yyyy-MM-dd HH-mm").format(Calendar.getInstance().getTime());
		String screenshotFilename = pathPrint + id + "_" + dateDay + " " + nomePrint + ".jpg";
		Robot robot = null;

		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedImage screenShot = robot
				.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		try {
			if (Utils.OS.contains("windows")) {
				@SuppressWarnings("unused")
				boolean scrFile2 = ImageIO.write(screenShot, "JPG", new File(screenshotFilename));
			} else {
				File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(scrFile, new File(screenshotFilename));
			}		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		prints.add(screenshotFilename);

		return screenshotFilename;
	}

	public void capturarText(String txt, String id) {

		texts.add(txt);

	}

	/**
	 * Método para gerar o relatório DOC..
	 * 
	 * @return void
	 * @author rodrigo.c.almeida
	 */
	public void createReportDoc(String id) throws Exception {
		@SuppressWarnings("unused")
		Doc report = new Doc(prints, texts, id);
	}
}
