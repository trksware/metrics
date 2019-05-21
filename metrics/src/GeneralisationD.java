import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneralisationD{

	String strDecl;
	String generalisationName;
	List<String> subclasses = new ArrayList<String>();

	public GeneralisationD(String pStrDecl) throws Exception {
		this.strDecl = pStrDecl;
	}
	
	public void SetGeneralisationD() throws Exception {

		String pStrDecl = this.strDecl;
		
		List<String> ListLines = Arrays.asList(pStrDecl.split("\n"));
		
		//check if first word in first line is GENERALIZATION
		if (!ListLines.get(0).trim().startsWith("GENERALIZATION")) {
			throw new Exception();
		}

		//extract generalisation name (class name)
		String tempName = ListLines.get(0).replace("GENERALIZATION","").trim();
		List<String> items = Arrays.asList(tempName.split("\\s+"));
		if (items.size() == 1 && items.get(0).matches("[a-zA-Z0-9_]+")) {
			this.generalisationName = items.get(0);
		}else {
			throw new Exception();	
		}
		
		//check if second line starts with SUBCLASSES
		if (!ListLines.get(1).trim().startsWith("SUBCLASSES")) {
			throw new Exception();
		}
		
		//parse the subclasses
		int indexSubClasses = pStrDecl.indexOf("SUBCLASSES");
		String classesDecl = pStrDecl.substring(indexSubClasses);
		List<String> subClasses = Arrays.asList(classesDecl.split("\n"));
		if (subClasses.size() != 1) {
			throw new Exception();
		}
		String strSubClasses = subClasses.get(0);
		strSubClasses = strSubClasses.replace("SUBCLASSES", "").trim();
		this.subclasses = Arrays.asList(strSubClasses.split(","));
		for (int i = 0; i < this.subclasses.size(); i++) {
			this.subclasses.set(i, this.subclasses.get(i).trim());
			if (!this.subclasses.get(i).matches("[a-zA-Z0-9_]+")){
				throw new Exception();
			}
		}
	}
	
	//methode to get the generalisation Name (class name)
	public String getGeneralisationName() {
		return generalisationName;
	}

	//methode to get the declaration as in the file
	public String getStrDecl() {
		return strDecl;
	}

	//methode to get the list of sub-classes
	public List<String> getSubclasses() {
		return subclasses;
	}
}
