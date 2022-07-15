package org.grits.toolbox.util.structure.glycan.filter.om;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "filter-setting")
public class FilterSetting
{
    String name;
    String description;
    Filter filter;

    public FilterSetting()
    {
    }

    public FilterSetting(String n, String d, Filter f)
    {
        this.name = n;
        this.description = d;
        this.filter = f;
    }

    @XmlAttribute
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

    @XmlElementRef
    public Filter getFilter()
    {
        return filter;
    }

    public void setFilter(Filter filter)
    {
        this.filter = filter;
    }

    @Override
    public String toString()
    {
        if (filter != null)
            return filter.toString();
        return super.toString();
    }
}
