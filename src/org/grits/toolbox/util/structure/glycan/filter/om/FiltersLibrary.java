package org.grits.toolbox.util.structure.glycan.filter.om;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "filtersLibrary")
public class FiltersLibrary
{
    String name;
    String version = "1.0";

    List<Filter> filters;
    List<Category> categories;

    public void setFilters(List<Filter> filters)
    {
        this.filters = filters;
    }

    @XmlElementRef()
    public List<Filter> getFilters()
    {
        return filters;
    }

    public void addFilter(Filter filter)
    {
        if (this.filters == null)
            this.filters = new ArrayList<Filter>();
        this.filters.add(filter);
    }

    @XmlAttribute
    public String getName()
    {
        return name;
    }

    @XmlAttribute
    public String getVersion()
    {
        return version;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }
    
    public List<Category> getCategories() {
		return categories;
	}
    
    public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
