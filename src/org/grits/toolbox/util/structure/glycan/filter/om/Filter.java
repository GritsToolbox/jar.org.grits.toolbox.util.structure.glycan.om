package org.grits.toolbox.util.structure.glycan.filter.om;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterException;

public abstract class Filter implements Comparable<Filter>
{
    protected String name;
    protected String description;
    protected String label;
    protected String classification;

    @XmlAttribute
    @XmlID
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @XmlAttribute
    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }
    
    @XmlAttribute
    public String getClassification() {
		return classification;
	}
    
    public void setClassification(String classification) {
    	this.classification = classification;
    }

    public abstract boolean accept(GlycanFilterVisitor a_operator) throws GlycanFilterException;

    /**
     * need to create a copy of the filter
     * 
     * @return a copy of the filter
     */
    public abstract Filter copy();

    @Override
    public int compareTo(Filter o)
    {
    	if (this.label != null && o.label != null)
    		return this.label.compareTo(o.label);
    	else
    		return 0;
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (obj instanceof Filter) {
    		if (this.name != null && ((Filter)obj).name != null) 
    			return this.name.equals(((Filter)obj).name);
    	}
    	return false;
    }
    
    @Override
    public int hashCode() {
    	return this.name == null ? super.hashCode() : this.name.hashCode();
    }
}
