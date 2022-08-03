package reportDocument;

import java.awt.Color;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.TextFormFieldType;

import constantsSuite.ConstantsReport;

public class Doc {

	public Doc(ArrayList<?> array, ArrayList<?> arrayText, String id) throws Exception {
		int verticalPosition = 0;
		String dataPath = ConstantsReport.DOC_PATH;
		DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
		Date date = new Date();
		Document doc = new Document(dataPath);
		DocumentBuilder builder = new DocumentBuilder(doc);

		for (int i = 0; i < array.size(); i++) {
			String imagePath = (String) array.get(i);
			String text = (String) arrayText.get(i);
			FileInputStream in = new FileInputStream(imagePath);
			
			builder.getFont().setColor(Color.black);
			builder.getFont().setEmboss(false);
			builder.getFont().setEngrave(false);
			builder.getFont().setHighlightColor(Color.WHITE);
			
			builder.insertTextInput("", TextFormFieldType.REGULAR, "", "\n\n" + text, 0);
			
			builder.getFont().setColor(Color.black);
			builder.getFont().setEmboss(false);
			builder.getFont().setEngrave(false);
			builder.getFont().setHighlightColor(Color.WHITE);
			
			builder.insertImage(in, 540, 320);
			verticalPosition = verticalPosition + 400;
		}
		doc.save(
				ConstantsReport.EVIDENCIA_PATH + id + "_" + "Automation_Evid_Exec" + dateFormat.format(date) + ".docx");
	}
}