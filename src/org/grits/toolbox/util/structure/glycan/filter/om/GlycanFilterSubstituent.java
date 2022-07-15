package org.grits.toolbox.util.structure.glycan.filter.om;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterException;

@XmlRootElement(name = "substituentFilter")
public class GlycanFilterSubstituent extends IntegerFilter
{
    private String m_substituent = null;

    @XmlAttribute
    public String getSubstituent()
    {
        return m_substituent;
    }

    public void setSubstituent(String a_substituent)
    {
        m_substituent = a_substituent;
    }

    public boolean accept(GlycanFilterVisitor a_operator) throws GlycanFilterException
    {
        return a_operator.visit(this);
    }

    @Override
    public Filter copy()
    {
        GlycanFilterSubstituent copy = new GlycanFilterSubstituent();
        copy.setName(name);
        copy.setDescription(description);
        copy.setLabel(label);
        copy.setClassification(classification);
        copy.setSubstituent(getSubstituent());
        copy.setMin(m_min);
        copy.setMax(m_max);
        return copy;
    }

}
