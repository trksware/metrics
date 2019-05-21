import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AggregationD{

	String strDecl;
	String container;
	List<String> parts = new ArrayList<String>();

	public AggregationD(String pStrDecl) throws Exception {
		this.strDecl = pStrDecl;
	}

	public void SetAggregationD() throws Exception {
		String pStrDecl = this.strDecl;
		List<String> ListLines = Arrays.asList(pStrDecl.split("\n"));

		//check if first word in first line is AGGREGATION
		if (!ListLines.get(0).trim().equals("AGGREGATION")) {
			throw new Exception();
		}

		//check if first word in second line is CONTAINER
		if (!ListLines.get(1).trim().equals("CONTAINER")) {
			throw new Exception();
		}

		int indexContainer = pStrDecl.indexOf("CONTAINER");
		int indexParts = pStrDecl.indexOf("PARTS");
		String containerDecl = "";
		String partsDecl = "";

		//Extract container declaration and parts declaration
		if (indexContainer != -1 && indexParts != -1) {
			containerDecl = pStrDecl.substring(indexContainer, indexParts);
			partsDecl = pStrDecl.substring(indexParts);
		}

		//Parses container and parts
		containerDecl = containerDecl.replace("CONTAINER", "").replaceFirst("\n", "");
		partsDecl = partsDecl.replace("PARTS", "").replaceFirst("\n", "");

		List<String> containerList = new ArrayList<String>();
		if (!containerDecl.isEmpty()) {
			containerList = Arrays.asList(containerDecl.split("\n"));
		}
		if (containerList.size() > 1) {
			throw new Exception();
		}
		
		//Check if containers is properly declared
		this.container = containerList.get(0);
		this.container = this.container.replace("CLASS", "").trim();
		String[] container = this.container.split("\\s+");
		if (container.length == 2 || container[0].matches("[a-zA-Z0-9_]+")
				|| (container[1].equals("MANY") || container[1].equals("ONE")
						|| container[1].equals("ONE_OR_MANY"))) {
			;
		} else {
			throw new Exception();
		}

		//check if there is at least one part
		if (partsDecl.isEmpty()) {
			throw new Exception();
		}
		
		//parses the parts and put them in a list
		this.parts = Arrays.asList(partsDecl.split("\n"));
		for (int i = 0; i < this.parts.size(); i++) {
			this.parts.set(i, this.parts.get(i).trim());
			if (this.parts.get(i).startsWith("CLASS")) {
				this.parts.set(i, this.parts.get(i).replace("CLASS", "").trim());
			} else {
				throw new Exception();
			}
			if (i < this.parts.size() - 1 && this.parts.get(i).endsWith(",")) {
				this.parts.set(i, this.parts.get(i).replace(",", "").trim());
			}else if (i == this.parts.size()-1 && this.parts.get(i).endsWith(",")){
				throw new Exception();
			}
			String[] part = this.parts.get(i).split("\\s+");
			if (part.length == 2 || part[0].matches("[a-zA-Z0-9_]+")
				|| (part[1].equals("MANY") || part[1].equals("ONE")
							|| part[1].equals("ONE_OR_MANY"))) {
				continue;
			} else {
				throw new Exception();
			}
		}
	}
	
	//methode to get the containers name
	public String getContainer() {
		return container;
	}
	
	//methode to get the list of parts
	public List<String> getParts() {
		return parts;
	}
	
	//methode to get the declaration as in the file
	public String getStrDecl() {
		return strDecl;
	}
}
