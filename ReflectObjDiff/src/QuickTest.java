/**
* @author rfoote
*/
public class QuickTest {
	public static void main(String[] args) {

		// config test

		// 1. create (String) array of special object type
		String[] ignoreA = { "Time" };
		String[] ignoreB = { "Dog", "Timestamp" };

		// 2. create config object
		Configs con1 = new Configs();
		Configs con2 = new Configs();

		// 3. set (String) action and tie it to special object array
		con1.propSet("Ignore", ignoreA);
		con2.propSet("Ignore", ignoreB);

		System.out.println("CONFIG TEST START");
		System.out.println("____");

		// get "Ignore" object names
		String[] propListA = con1.propGet("Ignore", ignoreA.length);
		for (String property : propListA) {
			System.out.println(property);
		}

		System.out.println("____");

		// get "Ignore" object names
		String[] propListB = con2.propGet("Ignore", ignoreB.length);
		for (String property : propListB) {
			System.out.println(property);
		}
		System.out.println("");
		System.out.println("CONFIG TEST END");
		System.out.println("");
		System.out.println("###############");
		System.out.println("");

		// DiffObj test

		System.out.println("OBJDIFF TEST START");
		System.out.println("____");

		// create simple Objects for diff test
		Dog thisDog = new Dog();
		Dog thisDog2 = new Dog();
		Breed shiba = new Breed("Shiba");
		Dog thisDog3 = new Dog(4, shiba);
		Dog thisDogNull = null;
		thisDog3.setAge(4);
		thisDog2.setArray(0, 2);
		String string1 = "Hi";
		String string2 = "Ho";
		ObjDiffXml reflectVerb = new ObjDiffXml(true);
		ObjDiffXml reflectBrev = new ObjDiffXml(false);

		// test verbose false mode/constructor
		System.out.println(reflectBrev.diffObj(string1, string2));
		System.out.println("____");

		// test setVerbose
		reflectBrev.setVerbose(true);
		System.out.println(reflectBrev.diffObj(string1, string2));
		reflectBrev.setVerbose(false);
		System.out.println("____");

		// test arrays
		System.out.println(reflectVerb.diffObj(thisDog, thisDog2));
		System.out.println("____");
		System.out.println("OBJDIFF TEST END");
	}
}