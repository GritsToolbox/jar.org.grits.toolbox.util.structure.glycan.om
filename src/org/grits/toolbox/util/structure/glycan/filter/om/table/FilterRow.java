package org.grits.toolbox.util.structure.glycan.filter.om.table;

import org.grits.toolbox.util.structure.glycan.filter.om.Filter;

public class FilterRow {

	Filter filter;
	Boolean include=true;
	
	public Filter getFilter() {
		return filter;
	}
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	public Boolean getInclude() {
		return include;
	}
	public void setInclude(Boolean include) {
		this.include = include;
	}
	
	
}
