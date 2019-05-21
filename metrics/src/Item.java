import java.util.ArrayList;

public class Item {
	private String name;
	private String type;
	private Object Ctype;
	
	public Item(String ptype, String pname) {
		switch (ptype) {
			case "Boolean":
				this.name = pname;
				this.type = ptype;
			case "Integer":
				this.name = pname;
				this.type = ptype;
				break;
			case "UnlimitedNatural":
				this.name = pname;
				this.type = ptype;
				break;
			case "String":
				this.name = pname;
				this.type = ptype;
				break;
			case "Real":
				this.name = pname;
				this.type = ptype;
				break;
			default:
				this.name = pname;
				this.type = ptype;
				break;
		}
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public Object getCtype() {
		return Ctype;
	}
	

	
}
