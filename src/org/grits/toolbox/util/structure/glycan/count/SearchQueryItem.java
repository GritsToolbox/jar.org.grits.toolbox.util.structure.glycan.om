package org.grits.toolbox.util.structure.glycan.count;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="component")
public class SearchQueryItem {
	
	String name;
	String sequence;
	
	public SearchQueryItem() {
	}
	
	public SearchQueryItem(String name, String sequence) {
		this.name = name;
		this.sequence = sequence;
	}
	
	@XmlAttribute
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
}
