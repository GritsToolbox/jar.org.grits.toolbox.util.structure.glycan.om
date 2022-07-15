package org.grits.toolbox.util.structure.glycan.filter.om;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterException;

@XmlRootElement(name="NOT")
public class GlycanFilterNot extends Filter
{
    private Filter m_filter = null;

    public boolean accept(GlycanFilterVisitor a_operator) throws GlycanFilterException
    {
        return a_operator.visit(this);
    }

    @XmlElementRef
    public Filter getFilter()
    {
        return m_filter;
    }

    public void setFilter(Filter a_filter)
    {
        m_filter = a_filter;
    }

    @Override
    public String toString()
    {
        String toString = "";
        if (m_filter != null)
        {
            toString += "NOT " + m_filter.toString();
        }
        return toString;
    }

	@Override
	public Filter copy() {
		GlycanFilterNot newCopy = new GlycanFilterNot();
		newCopy.setFilter(m_filter.copy());
		newCopy.setLabel(label);
		newCopy.setDescription(description);
		newCopy.setName(name);
		newCopy.setClassification(classification);
		return newCopy;
	}
	
	/** have to override equals to check for children equivalence
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GlycanFilterNot) {
			return m_filter == null ? ((GlycanFilterNot) obj).getFilter() == null : m_filter.equals(((GlycanFilterNot) obj).getFilter());
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return "GlycanFilterNot".hashCode();
	}
}
