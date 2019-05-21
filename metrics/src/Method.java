import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Method {
	
	private String name;
	private String typeReturn;
	private List<Item> ListP = new ArrayList<Item>();

	public Method(String pstring) throws Exception {
		int startType = pstring.lastIndexOf(":");
		String type = pstring.substring(startType+1);
//		if (!(type.equals("void") || Controler.getTypes().contains(type) || Controler.getClassesNames().contains(type))) {
//			throw new Exception();
//		}else {
		this.typeReturn = type;
		pstring = pstring.replace("):"+type, ")");
//		}
		int startParentesys = pstring.indexOf("(");
		this.name = pstring.substring(0, startParentesys);
		pstring = pstring.replace(this.name, "");
		if (pstring.startsWith("(") && pstring.endsWith(")")){
			pstring = pstring.replace("(", "").replace(")", "");
			if (pstring.startsWith("(") || pstring.endsWith(")")){
				throw new Exception();
			}
		}
		List<String> ListParams = Arrays.asList(pstring.split(","));	
		if (ListParams.size() == 1 && ListParams.get(0).equals("")) {
			return;
		}
		for (int i= 0; i < ListParams.size(); i++) {
			startType = ListParams.get(i).lastIndexOf(":");
			String name = ListParams.get(i).substring(0, startType);
			type = ListParams.get(i).substring(startType+1);
			boolean b = Controler.getTypes().contains(type) || Controler.getClassesNames().contains(type);
			if (!name.matches("[a-zA-Z0-9_]+")) {
				throw new Exception();
			}else {
				this.ListP.add(new Item(type, name));
			}
		}
	}

	public String getName() {
		return name;
	}

	public String getTypeReturn() {
		return typeReturn;
	}

	public List<Item> getListP() {
		return ListP;
	}
	
}
