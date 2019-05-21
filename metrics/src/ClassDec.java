import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassDec{

	private String strDecl;
	private String className;
	private ArrayList<Item> ListAttsI = new ArrayList<Item>();
	private ArrayList<Method> ListOpsI = new ArrayList<Method>();
	private ArrayList<ClassDec> ListSubCla = new ArrayList<ClassDec>();
	private ArrayList<ClassDec> ListSupCla = new ArrayList<ClassDec>();
	
	private ArrayList<String> ListRela = new ArrayList<String>();
	private ArrayList<String> ListAgg = new ArrayList<String>();
	
	private ArrayList<String> ListAttsAll;
	private ArrayList<String> ListOpsAll;
	private int depthU;
	private ArrayList<String> ListSubClaNamesAll = new ArrayList<String>();
	private int depthD;
	private ArrayList<String> ListMetrics = new ArrayList<String>();
	
	
	private double ANA;
	private int NOM;
	private int NOA;
	private int ITC;
	private int ETC;
	private int CAC;
	private int DIT;
	private int CLD;
	private int NOC;
	private int NOD;
	
	public ClassDec(String pStrDecl) throws Exception {

		//Backup details of declaration of class in file
		this.strDecl = pStrDecl;
		
		//list of lines in declaration
		List<String> ListLines = Arrays.asList(pStrDecl.split("\n"));
		
		////////////////////////////////////////////////////////////////////////
		//Get the name of the class
		////////////////////////////////////////////////////////////////////////
		String tempName = ListLines.get(0).replace("CLASS","").trim();
		List<String> items = Arrays.asList(tempName.split("\\s+"));
		if (items.size() == 1 && items.get(0).matches("[a-zA-Z0-9_]+")) {
			this.className = items.get(0);
		}else {
			throw new Exception();	
		}
	}
	
	public void SetClassDec() throws Exception {
		
		String pStrDecl = this.strDecl;

		//list of lines in declaration
		List<String> ListLines = Arrays.asList(pStrDecl.split("\n"));
		
		//Check if second line starts with ATTRIBUTES
		if (!ListLines.get(1).trim().startsWith("ATTRIBUTES")) {
			throw new Exception();
		}
		
		String attsDecl = "";
		String opsDecl = "";
		int indexAtts = pStrDecl.indexOf("ATTRIBUTES");
		int indexOpe = pStrDecl.indexOf("OPERATIONS");
	
		//Get the atts and methodes
		if(indexAtts != -1 && indexOpe != -1 ) {
			attsDecl = pStrDecl.substring(indexAtts, indexOpe);
			opsDecl = pStrDecl.substring(indexOpe);
		}
		//Parse atts and methods
		attsDecl = attsDecl.replace("ATTRIBUTES","").replace(" ", "").replaceFirst("\n", "");
		opsDecl = opsDecl.replace("OPERATIONS","").replace(" ", "").replaceFirst("\n", "");
		
		List<String> ListAtts = new ArrayList<String>();
		List<String> ListOps = new ArrayList<String>();
		
		if (!attsDecl.isEmpty()) {
			ListAtts = Arrays.asList(attsDecl.split("\n"));
		}
		if (!opsDecl.isEmpty()) {
			ListOps = Arrays.asList(opsDecl.split("\n"));
		}
		
		for (int i= 0; i < ListAtts.size(); i++) {
			if (i < ListAtts.size() - 1 && ListAtts.get(i).endsWith(",")) {
				ListAtts.set(i, ListAtts.get(i).replace(",", "").trim());
			}else if (i == ListAtts.size()-1 && ListAtts.get(i).endsWith(",")){
				throw new Exception();
			}
			int startType = ListAtts.get(i).lastIndexOf(":");
			String varName = ListAtts.get(i).substring(0, startType);
			String type = ListAtts.get(i).substring(startType+1);
			
			//boolean b = Controler.getTypes().contains(type) || Controler.getClassesNames().contains(type);
			if (!varName.matches("[a-zA-Z0-9_]+")) {
				throw new Exception();
			}else {
				this.ListAttsI.add(new Item(type, varName));
			}
		}

		for (int i= 0; i < ListOps.size(); i++) {
			if (i < ListOps.size()-1) {
				if (ListOps.get(i).endsWith(",")) {
					ListOps.set(i, ListOps.get(i).substring(0,ListOps.get(i).length()-1).trim());
				}else {
					throw new Exception();
				}	
			}
			this.ListOpsI.add(new Method(ListOps.get(i)));
		}
			
       //check if there are duplicates attributes
        for (Item att : this.ListAttsI) {
        	boolean duplicate = false;
        	for (Item att2 : this.ListAttsI) {
        		if (att.getName().equals(att2.getName()) && !duplicate) {
        			duplicate = true;
        		} else if (att.equals(att2) && duplicate) {
        			throw new Exception();
        		}
        	}
        	duplicate = false;
        }
    
        //check if there are duplicates methods
         for (Method op : this.ListOpsI) {
         	boolean duplicate = false;
         	for (Method op2 : this.ListOpsI) {
         		if (op.getName().equals(op2.getName()) && !duplicate) {
         			duplicate = true;
         		} else if (op.equals(op2) && duplicate) {
         			throw new Exception();
         		}
         	}
         	duplicate = false;
         }
	}
	
	//Get class name
	public String getClassName() {
		return className;
	}

	//Get class declaration in file
	public String getStrDecl() {
		return strDecl;
	}
	
	public ArrayList<Item> getListAttsI() {
		return ListAttsI;
	}

	public ArrayList<Method> getListOpsI() {
		return ListOpsI;
	}

	//Add Sub-class to the class
	public void addSubClass(ClassDec subClassName) {
		this.ListSubCla.add(subClassName);
	}
	
	//Add Sub-class to the class
	public void addSupClass(ClassDec subClassName) {
		this.ListSupCla.add(subClassName);
	}
	
	//Get list of attibutes of class
	public ArrayList<String> getListAttsNames() {
		ArrayList<String> ListAttsNames = new ArrayList<String>();
		for (int i= 0; i < ListAttsI.size(); i++) {
			ListAttsNames.add(ListAttsI.get(i).getName());
		}
		return ListAttsNames;
	}
	
	//Get list of attibutes of class
	public String[] getListAttsNamesSTR() {
		ArrayList<String> ListAttsNames = new ArrayList<String>();
		for (int i= 0; i < ListAttsI.size(); i++) {
			ListAttsNames.add(ListAttsI.get(i).getName() + " : " + ListAttsI.get(i).getType());
		}
		return this.convertListToTable(ListAttsNames);
	}

	//Get list of methods of the class
	public ArrayList<String> getListOpsNames() {
		ArrayList<String> ListOpsNames = new ArrayList<String>();
		for (int i= 0; i < ListOpsI.size(); i++) {
			ListOpsNames.add(ListOpsI.get(i).getName());
		}
		return ListOpsNames;
	}
		
	//Get list of methods of the class
	public String[] getListOpsNamesSTR() {
		ArrayList<String> ListOpsNames = new ArrayList<String>();
		for (int i= 0; i < ListOpsI.size(); i++) {
			ListOpsNames.add(ListOpsI.get(i).getName() + " : " + ListOpsI.get(i).getTypeReturn());
		}
		return this.convertListToTable(ListOpsNames);
	}
		
	//Get list of sub-classes
	public ArrayList<String> getListSubClaNames() {
		ArrayList<String> ListSubClassesNames = new ArrayList<String>();
		for (int i= 0; i < ListSubCla.size(); i++) {
			ListSubClassesNames.add(ListSubCla.get(i).getClassName());
		}
		return ListSubClassesNames;
	}
		
	//Get list of sub-classes
	public String[] getListSubClaNamesSTR() {
		return this.convertListToTable(this.getListSubClaNames());
	}
	
	//Get list of sub-classes
	public ArrayList<String> getListSupClaNames() {
		ArrayList<String> ListSupClassesNames = new ArrayList<String>();
		for (int i= 0; i < ListSupCla.size(); i++) {
			ListSupClassesNames.add(ListSupCla.get(i).getClassName());
		}
		return ListSupClassesNames;
	}
		
	//Get list of sub-classes
	public String[] getListSupClaNamesSTR() {
		return this.convertListToTable(this.getListSupClaNames());

	}
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	//Get list of relations of the class
	public String[] getListRela() {
		return this.convertListToTable(ListRela);
	}
	
	//Get list of classes aggregated to the current class
	public String[] getListAgg() {
		return this.convertListToTable(ListAgg);
	}
	
	//Add parts aggregation to class
	public void addClassAgg(String aggClass) {
		this.ListAgg.add(aggClass);
	}
	
	//Add relation to the class
	public void addRelation(String relationName) {
		this.ListRela.add(relationName);
	}
	
	public String[] getListAggRela() {
		ArrayList<String> concat = new ArrayList<String>();
		for (int i = 0; i < ListAgg.size(); i++) {
			concat.add("(A) " + ListAgg.get(i));
		}
		for (int i = 0; i < ListRela.size(); i++) {
			concat.add("(R) " + ListRela.get(i));
		}
		return this.convertListToTable(concat);
	}
	
	public String[] getMetrics() {
		return this.convertListToTable(this.ListMetrics);
	}
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	
	//Methode to convert ArrayList to []
	public String [] convertListToTable(List<String> listOfStrings) {
		return listOfStrings.stream().toArray(String[]::new);
	}
	
	public void calculateMetrics() {
		
		//ANA (moyenne params methods)
		float countANA = 0;
		for (int i= 0; i < ListOpsI.size(); i++) {
			countANA += ListOpsI.get(i).getListP().size();
		}
		if (ListOpsI.size() != 0) {
			this.ANA = countANA / ListOpsI.size();
		}
		
		//ITC (autres classes comme type de params ici)
		int count = 0;
		ArrayList<String> par = new ArrayList<String>();
		for (int j = 0; j < this.getListOpsI().size(); j++) {
			for (int k = 0; k < this.getListOpsI().get(j).getListP().size(); k++) {
				String t = this.getListOpsI().get(j).getListP().get(k).getType();
				if (Controler.getArrClassesNames().contains(t) && !par.contains(t) && !t.equals(this.className)) {
					par.add(t);
					count++;
				}
			}
		}
		this.ITC = count;
		
		//ETC (ici type de params de methods d'autres classes)
		count = 0;
		for (int i= 0; i < Controler.getArrClasses().size(); i++) {
			if (Controler.getArrClasses().get(i).getClassName().equals(this.className)){
				continue;
			}
			for (int j = 0; j < Controler.getArrClasses().get(i).getListOpsI().size(); j++) {
				for (int k = 0; k < Controler.getArrClasses().get(i).getListOpsI().get(j).getListP().size(); k++) {
					if(Controler.getArrClasses().get(i).getListOpsI().get(j).getListP().get(k).getType().equals(this.getClassName())) {
						count++;
					}
				}
			}
		}
		this.ETC = count;
		
		this.ListAttsAll = new ArrayList<String>(this.getListAttsNames());
		this.ListOpsAll = new ArrayList<String>(this.getListOpsNames());
		this.depthU = 0;
		this.depthD = 0;
		this.CLD =  this.recSub(this);
		this.DIT =  this.recSup(this);
		
		this.NOM = this.ListOpsAll.size();
		this.NOA = this.ListAttsAll.size();
		this.CAC = this.ListRela.size() + this.ListAgg.size();
		this.NOC =  this.getListSubClaNames().size();
		this.NOD =  this.ListSubClaNamesAll.size();

		ListMetrics.add(String.format("ANA = %.2f", this.ANA));
		ListMetrics.add("NOM = " + this.NOM);
		ListMetrics.add("NOA = " + this.NOA);
		ListMetrics.add("ITC = " + this.ITC);
		ListMetrics.add("ETC = " + this.ETC);
		ListMetrics.add("CAC = " + this.CAC);
		ListMetrics.add("DIT = " + this.DIT);
		ListMetrics.add("CLD = " + this.CLD);
		ListMetrics.add("NOC = " + this.NOC);
		ListMetrics.add("NOD = " + this.NOD);
	}
	
	private int recSub(ClassDec ci) {
		int depthCi = 0;
		int maxDepth = 0;
		if (ci.getListSubClaNames().isEmpty()) {
			return 0;
		}else {
			for (int i= 0; i < ci.getListSubClaNames().size(); i++) {
				
				ClassDec subCi = Controler.getClassByName(ci.getListSubClaNames().get(i));
				this.ListSubClaNamesAll.add(subCi.getClassName());
				
				depthCi = 1;
				depthCi += this.recSub(subCi);
				if (depthCi > maxDepth) { maxDepth = depthCi; }
			}
		}
		return maxDepth;
	}
	
	/**
	 * @param ci
	 * @return 
	 */
	private int recSup(ClassDec ci) {
		int depthCi = 0;
		int maxDepth = 0;
		if (ci.getListSupClaNames().isEmpty()) {
			return 0;
		}else {
			for (int i= 0; i < ci.getListSupClaNames().size(); i++) {
				depthCi = 1;
				ClassDec supCi = Controler.getClassByName(ci.getListSupClaNames().get(i));
				
				if (supCi.getListAttsNames().size() != 0) {
					for (int j= 0; j < supCi.getListAttsNames().size(); j++) {
						if (!this.ListAttsAll.contains(supCi.getListAttsNames().get(j))) {
							this.ListAttsAll.add(supCi.getListAttsNames().get(j));
						}
					}
				}
				
				if (supCi.getListOpsNames().size() != 0) {
					for (int j= 0; j < supCi.getListOpsNames().size(); j++) {
						if (!this.ListOpsAll.contains(supCi.getListOpsNames().get(j))) {
							this.ListOpsAll.add(supCi.getListOpsNames().get(j));
						}
					}
				}
				
				depthCi += this.recSup(supCi);
				if (depthCi > maxDepth) { maxDepth = depthCi; }
			}
		}
		return maxDepth;
	}
}





