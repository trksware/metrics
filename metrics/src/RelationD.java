import java.util.Arrays;
import java.util.List;

public class RelationD{

	String strDecl;
	String relationName;
	String firstClass;
	String secondClass;
	
	public RelationD(String pStrDecl) throws Exception {
		this.strDecl = pStrDecl;
	}

	public void SetRelationD() throws Exception {

		String pStrDecl = this.strDecl;
		
		List<String> ListLines = Arrays.asList(pStrDecl.split("\n"));
		
		//check if first word in first line is RELATION
		if (!ListLines.get(0).trim().startsWith("RELATION")) {
			throw new Exception();
		}
		
		//extract relation name
		String tempName = ListLines.get(0).replace("RELATION","").trim();
		List<String> items = Arrays.asList(tempName.split("\\s+"));
		if (items.size() == 1 && items.get(0).matches("[a-zA-Z0-9_]+")) {
			this.relationName = items.get(0);
		}else {
			throw new Exception();	
		}
		
		//check if first word in second line is ROLES
		if (!ListLines.get(1).trim().startsWith("ROLES")) {
			throw new Exception();
		}
		
		//check if first word in first line starts with CLASS
		if (!ListLines.get(2).trim().startsWith("CLASS")) {
			throw new Exception();
		}
		
		//extract the classes
		int indexClasses = pStrDecl.indexOf("CLASS");
		String classesDecl = pStrDecl.substring(indexClasses);
		List<String> classes = Arrays.asList(classesDecl.split("\n"));
		if (classes.size() != 2) {
			throw new Exception();
		}
		for (int i = 0; i < classes.size(); i++) {
			classes.set(i, classes.get(i).trim());
			if (classes.get(i).startsWith("CLASS")){
				classes.set(i, classes.get(i).replace("CLASS","").trim());
			}else {throw new Exception();}
			if (i < classes.size() - 1 && classes.get(i).endsWith(",")) {
				classes.set(i, classes.get(i).replace(",", "").trim());
			}else if (i == classes.size()-1 && classes.get(i).endsWith(",")){
				throw new Exception();
			}
			String[] classToEvaluate = classes.get(i).split("\\s+");
			if (classes.get(i).split("\\s+").length == 2 ||
					classToEvaluate[0].matches("[a-zA-Z0-9_]+")||
					(classToEvaluate[1].equals("MANY") ||
					classToEvaluate[1].equals("ONE") ||
					classToEvaluate[1].equals("ONE_OR_MANY"))) {
				continue;
			} else {throw new Exception();}
		}
		
		this.firstClass = classes.get(0);
		this.secondClass = classes.get(1);
		
	}
	
	//methode to get the declaration of the relation as in the file
	public String getStrDecl() {
		return strDecl;
	}
	
	//methode to get the first class Name
	public String getFirstClass() {
		return firstClass;
	}

	//methode to get the second class Name
	public String getSecondClass() {
		return secondClass;
	}
	
	//methode to get the relation Name
	public String getRelationName() {
		return relationName;
	}

}
