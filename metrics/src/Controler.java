import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Controler {
	
	private static ArrayList<ClassDec> arrClasses = new ArrayList<ClassDec>();
	private static List<String> types = Arrays.asList("Boolean", "Integer", "UnlimitedNatural", "String", "Real");
	
	private ArrayList<GeneralisationD> arrGeneralisations = new ArrayList<GeneralisationD>();
	private ArrayList<RelationD> arrRelations = new ArrayList<RelationD>();
	private ArrayList<AggregationD> arrAggregations = new ArrayList<AggregationD>();
	
	private String modelName = "";
	private String fileToREad = "";
	
	public Controler(String pfileToREad) throws Exception {
		
		this.fileToREad = pfileToREad;

		////////////////////////////////////////////////////////////////////////
    	//Read the file and put content in a list line by line, skip empty lines
		////////////////////////////////////////////////////////////////////////
        @SuppressWarnings("resource")
		BufferedReader buff = new BufferedReader(new FileReader(pfileToREad));
        String currentLine;
        ArrayList<String> arrOfLines = new ArrayList<String>();
        while ((currentLine = buff.readLine()) != null) {
        	if (currentLine.length() > 0) {
        		arrOfLines.add(currentLine);
        	}
        }
    
        ////////////////////////////////////////////////////////////////////////
        //First line must be Model declaration
        ////////////////////////////////////////////////////////////////////////
        ArrayList<String> arrOfDeclarations = new ArrayList<String>();
        if (arrOfLines.get(0).startsWith("MODEL")) {			
			this.modelName = arrOfLines.get(0).replace("MODEL","").trim();
			List<String> items = Arrays.asList(this.modelName.split("\\s+"));
			if (!(items.size() == 1 && items.get(0).matches("[a-zA-Z0-9_]+"))) {
				throw new Exception();
			}
        }else {
        	throw new Exception();
        }
        
        ////////////////////////////////////////////////////////////////////////
        //create array with declarations (starts a new one when it finds ';')
        ////////////////////////////////////////////////////////////////////////
        String declaration = arrOfLines.get(1);
        int line = 2;
        while (line < arrOfLines.size()) {
        	arrOfLines.set(line, arrOfLines.get(line).replaceAll("\t", ""));
        	if (arrOfLines.get(line).endsWith(";")){
        		String last_line = arrOfLines.get(line).substring(0, arrOfLines.get(line).length() - 1);
        		if (!last_line.isEmpty()) {
        			declaration += "\n" + last_line;
        		}
        		arrOfDeclarations.add(declaration.trim());
        		declaration = "";
        		line++;
        	}else {
        		declaration += "\n" + arrOfLines.get(line);
        		line++;
        	}
        }
        
        ////////////////////////////////////////////////////////////////////////
        //for each declaration in the file create the proper object (depends on the first
        //word (CLASS, GENERALIZATION...)
        ////////////////////////////////////////////////////////////////////////
        if (!arrClasses.isEmpty()) {
        	arrClasses = new ArrayList<ClassDec>();
        }
        for (int i= 0; i < arrOfDeclarations.size(); i++) {
        	if (arrOfDeclarations.get(i).startsWith("CLASS")) {
        		Controler.arrClasses.add(new ClassDec(arrOfDeclarations.get(i)));
        	}else if(arrOfDeclarations.get(i).startsWith("GENERALIZATION")) {
        		this.arrGeneralisations.add(new GeneralisationD(arrOfDeclarations.get(i)));
        	}else if(arrOfDeclarations.get(i).startsWith("RELATION")) {
        		this.arrRelations.add(new RelationD(arrOfDeclarations.get(i)));
        	}else if(arrOfDeclarations.get(i).startsWith("AGGREGATION")) {
        		this.arrAggregations.add(new AggregationD(arrOfDeclarations.get(i)));
        	}else {
        		throw new Exception();
        	}
        }
        
        ////////////////////////////////////////////////////////////////////////
        //Set Classes
        ////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < Controler.arrClasses.size(); i++) {
			Controler.arrClasses.get(i).SetClassDec();
		}
        
		////////////////////////////////////////////////////////////////////////
		//Set Classes
		////////////////////////////////////////////////////////////////////////
		for (int i = 0; i < this.arrGeneralisations.size(); i++) {
			this.arrGeneralisations.get(i).SetGeneralisationD();
		}
		
		////////////////////////////////////////////////////////////////////////
		//Set Classes
		////////////////////////////////////////////////////////////////////////
		for (int i = 0; i < this.arrRelations.size(); i++) {
			this.arrRelations.get(i).SetRelationD();
		}
		
		////////////////////////////////////////////////////////////////////////
		//Set Classes
		////////////////////////////////////////////////////////////////////////
		for (int i = 0; i < this.arrAggregations.size(); i++) {
			this.arrAggregations.get(i).SetAggregationD();
		}
		        
        ////////////////////////////////////////////////////////////////////////
        //check if there are duplicates classes
        ////////////////////////////////////////////////////////////////////////
        //list of names of classes
        ArrayList<String> classesNames = Controler.getClassesNames();
        for (String className : classesNames) {
        	boolean duplicate = false;
        	for (String className2 : classesNames) {
        		if (className.equalsIgnoreCase(className2) && !duplicate) {
        			duplicate = true;
        		} else if (className.equals(className2) && duplicate) {
        			throw new Exception();
        		}
        	}
        	duplicate = false;
        }
        
        ////////////////////////////////////////////////////////////////////////
        //Add for each generalization add sub-classes to the class
        ////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < this.arrGeneralisations.size(); i++) {
        	String generalisationClass = this.arrGeneralisations.get(i).getGeneralisationName();
        	if (!(classesNames.contains(generalisationClass))){
    			throw new Exception();
        	}
        	for (int j = 0; j < this.arrGeneralisations.get(i).getSubclasses().size(); j++) {
        		String currentSubClass = this.arrGeneralisations.get(i).getSubclasses().get(j);
        		if (!(classesNames.contains(currentSubClass))){
        			throw new Exception();
        		}else {
        			if (!Controler.getClassByName(generalisationClass).getListSubClaNames().contains(currentSubClass)) {
            			Controler.getClassByName(generalisationClass).addSubClass(Controler.getClassByName(currentSubClass));
        			}
        			if (!Controler.getClassByName(currentSubClass).getListSupClaNames().contains(generalisationClass)) {
            			Controler.getClassByName(currentSubClass).addSupClass(Controler.getClassByName(generalisationClass));
        			}
        		}
        	}
        }
        
       //list of names of relations
       ArrayList<String> relationsNames = this.getRealtionsNames();
       //check if there are duplicates relations
        for (String relationName : relationsNames) {
        	boolean duplicate = false;
        	for (String relationName2 : relationsNames) {
        		if (relationName.equals(relationName2) && !duplicate) {
        			duplicate = true;
        		} else if (relationName.equals(relationName2) && duplicate) {
        			throw new Exception();
        		}
        	}
        	duplicate = false;
        }
        
        //Adds each relation to the classes concerned
        for (int i= 0; i < this.arrRelations.size(); i++) {
        	if (!(classesNames.contains(this.arrRelations.get(i).getFirstClass().split("\\s+")[0]) &&
        			classesNames.contains(this.arrRelations.get(i).getSecondClass().split("\\s+")[0]))){
        		throw new Exception();
        	}else {
        		String currentRelation = this.arrRelations.get(i).getRelationName();
        		String firstClass = this.arrRelations.get(i).getFirstClass().split("\\s+")[0];
        		String secondClass = this.arrRelations.get(i).getSecondClass().split("\\s+")[0];
        		Controler.getClassByName(firstClass).addRelation(currentRelation);
        		Controler.getClassByName(secondClass).addRelation(currentRelation);
        	}
        }
        
        //For each aggregation adds parts classes to the container class
        for (int i = 0; i < this.arrAggregations.size(); i++) {
        	String currentClass = this.arrAggregations.get(i).getContainer().split("\\s+")[0];
        	if (!(classesNames.contains(currentClass))){
        		throw new Exception();
        	}
        	for (int j = 0; j < this.arrAggregations.get(i).getParts().size(); j++) {
        		String part = this.arrAggregations.get(i).getParts().get(j).split("\\s+")[0];
        		if (!(classesNames.contains(part))){
        			throw new Exception();
        		}else {
        			Controler.getClassByName(currentClass).addClassAgg(part);
        		}
        	}
        }
        
        ////////////////////////////////////////////////////////////////////////
        //Calculate metrics
        ////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < Controler.arrClasses.size(); i++) {
			Controler.arrClasses.get(i).calculateMetrics();
		}
	}
	
	//methode to get a list of the names "strings" of the classes
	public static List<String> getTypes() {
		return types;
	}
	
	public static ArrayList<ClassDec> getArrClasses() {
		return arrClasses;
	}
	
	//Get list of sub-classes
	public static ArrayList<String> getArrClassesNames() {
		ArrayList<String> arrClassesNames = new ArrayList<String>();
		for (int i= 0; i < arrClasses.size(); i++) {
			arrClassesNames.add(arrClasses.get(i).getClassName());
		}
		return arrClassesNames;
	}

	//methode to get a list of the names "strings" of the classes
	public static ArrayList<String> getClassesNames(){
		ArrayList<String> classesNames = new ArrayList<String>();
		for (int i = 0; i < Controler.arrClasses.size(); i++) {
			classesNames.add(Controler.arrClasses.get(i).getClassName());
		}
		return classesNames;
	}
	
	//Methode to get the class by name
	public static ClassDec getClassByName(String className) {
		for (int i = 0; i < Controler.arrClasses.size(); i++) {
			if (Controler.arrClasses.get(i).getClassName().equals(className)){
				return Controler.arrClasses.get(i);
			}
		}
		return null;
	}
	
	//methode to get a list of the names "strings" of the relations
	public ArrayList<String> getRealtionsNames(){
		ArrayList<String> relationsNames = new ArrayList<String>();
		for (int i = 0; i < this.arrRelations.size(); i++) {
			relationsNames.add(this.arrRelations.get(i).getRelationName());
		}
		return relationsNames;
	}
	
	//methode to get a Array of the names "strings" of the relations
	public String[] getClassesNamesTable(){
		return Controler.getClassesNames().stream().toArray(String[]::new);
	}
	
	//Methode to get the declaration (as in the file) of a GENERALISATION by the name of the class
	//and a given sub-class
	public String getStrDeclGen(String currentClassName, String subClass) {
		for (int i = 0; i < arrGeneralisations.size(); i++) {
			if (arrGeneralisations.get(i).getGeneralisationName().equals(currentClassName)) {
				List<String> subClasses = arrGeneralisations.get(i).getSubclasses();
				for (int j = 0; j < subClasses.size(); j++) {
					if (subClasses.get(j).equals(subClass)) {
						return arrGeneralisations.get(i).getStrDecl();
					}
				}
			}
		}
		return null;
	}

	//Methode to get the declaration (as in the file) of a RELATION with a given relation name
	public String getStrDeclRel(String relationName) {
		for (int i = 0; i < arrRelations.size(); i++) {
			if (arrRelations.get(i).getRelationName().equals(relationName)) {
				return arrRelations.get(i).getStrDecl();
			}
		}
		return null;
	}
	
	//Methode to get the declaration (as in the file) of a AGGREGATION with a given container
	//and a part
	public String getStrDeclAgg(String currentClassName, String partName) {
		for (int i = 0; i < arrAggregations.size(); i++) {
			String container = arrAggregations.get(i).getContainer().split("\\s+")[0];
			if (container.equals(currentClassName)) {
				List<String> parts = arrAggregations.get(i).getParts();
				for (int j = 0; j < parts.size(); j++) {
					if (parts.get(j).split("\\s+")[0].equals(partName)) {
						return arrAggregations.get(i).getStrDecl();
					}
				}
			}
		}
		return null;
	}
	
	public void generateCSV() {
		String output = "Nom_Class,ANA,NOM,NOA,ITC,ETC,CAC,DIT,CLD,NOC,NOD\n";
		for (int i = 0; i < this.arrClasses.size(); i++) {
			output += this.arrClasses.get(i).getClassName() + ",";
			for (int j = 0; j < this.arrClasses.get(i).getMetrics().length; j++) {
				output += this.arrClasses.get(i).getMetrics()[j].substring(6) + ",";
			}
			output += "\n";
		}
		try {
			FileOutputStream fos = new FileOutputStream(this.fileToREad.substring(0, this.fileToREad.length() - 4) + ".csv");
			fos.write(output.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
