import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Ray Allan Foote
 * Class sets XML properties in config.xml 
 */
public class Configs {
	public static Properties prop = new Properties();

	public void propSet(String propName, String propVal) {
		try {
			FileOutputStream fos = new FileOutputStream("config.xml");
			prop.setProperty(propName, propVal);
			prop.storeToXML(fos, "Obj Diff Config");
		} catch (IOException e) {
			System.out.println("Invalid arguments in propSet.");
		}
	}

	public void propSet(String propName, String[] propVals) {
		try {
			FileOutputStream fos = new FileOutputStream("config.xml");
			int i = 0;
			for (String property : propVals) {
				prop.setProperty(propName + i, property);
				i += 1;
			}
			prop.storeToXML(fos, "Obj Diff Config");
		} catch (IOException e) {
			System.out.println("Invalid arguments in propSet.");
		}
	}

	public String[] propGet(String propName, int propNum) {

		List<String> configList = new ArrayList<>();
		try {
			FileInputStream fis = new FileInputStream("config.xml");
			prop.loadFromXML(fis);
			for (int i = 0; i < propNum; ++i) {
				configList.add(prop.getProperty(propName + i));
			}
		} catch (IOException e) {
			System.out.println("Invalid arguments in propSet.");
		}
		return configList.toArray(new String[configList.size()]);
	}
}