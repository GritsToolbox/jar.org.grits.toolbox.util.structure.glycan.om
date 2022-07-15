package org.grits.toolbox.util.structure.glycan.filter.om;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="category")
public class Category {
	String name;
	String label;
	String description;
	List<String> filterNames;
	
	@XmlAttribute
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@XmlAttribute
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@XmlElementWrapper(name="filters")
	public List<String> getFilters() {
		return filterNames;
	}
	
	public void setFilters(List<String> filters) {
		this.filterNames = filters;
	}
	
	public boolean containsFilter(String filterName) {
		return filterNames.contains(filterName);
	}
	
	@Override
	public String toString() {
		return label;
	}
}
