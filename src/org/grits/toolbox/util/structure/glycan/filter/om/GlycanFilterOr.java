package org.grits.toolbox.util.structure.glycan.filter.om;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterException;

@XmlRootElement(name="OR")
public class GlycanFilterOr extends Filter
{
    private List<Filter> m_elements = new ArrayList<Filter>();

    @XmlElementRef
    public List<Filter> getElements()
    {
        return m_elements;
    }

    public void setElements(List<Filter> a_elements)
    {
        m_elements = a_elements;
    }

    public boolean accept(GlycanFilterVisitor a_operator) throws GlycanFilterException
    {
        return a_operator.visit(this);
    }

    @Override
    public String toString()
    {
        String toString = "";
        if (m_elements != null)
        {
            int i = 0;
            int last = m_elements.size() - 1;
            for (Filter glycanFilter : m_elements)
            {
                toString += glycanFilter.toString();
                if (i < last)
                {
                    toString += " OR ";
                }
                i++;
            }
        }
        return toString;
    }

	@Override
	public Filter copy() {
		GlycanFilterOr newCopy = new GlycanFilterOr();
		newCopy.setDescription(description);
		newCopy.setLabel(label);
		newCopy.setName(name);
		newCopy.setClassification(classification);
		List<Filter> filterListCopy = new ArrayList<>();
		for (Filter glycanFilter : m_elements)
        {
			filterListCopy.add(glycanFilter.copy());
        }
		newCopy.setElements(filterListCopy);
		return newCopy;
	}
	
	/** have to override equals to check for children equivalence
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GlycanFilterOr) {
			if (m_elements == null && ((GlycanFilterOr) obj).getElements() == null)
				return true;
			for (Filter f: m_elements) {
				if (!((GlycanFilterOr) obj).getElements().contains(f))
					return false;
			}
			return true;
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return "GlycanFilterOr".hashCode();
	}
}
