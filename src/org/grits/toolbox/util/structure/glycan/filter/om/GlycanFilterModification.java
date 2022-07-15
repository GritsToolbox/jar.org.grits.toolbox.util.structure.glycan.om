package org.grits.toolbox.util.structure.glycan.filter.om;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterException;

@XmlRootElement(name = "modificationFilter")
public class GlycanFilterModification extends IntegerFilter
{
    private String m_modification = null;
    private Integer m_postionOne = null;
    private Integer m_postionTwo = null;

    public boolean accept(GlycanFilterVisitor a_operator) throws GlycanFilterException
    {
        return a_operator.visit(this);
    }

    @Override
    public Filter copy()
    {
        GlycanFilterModification copy = new GlycanFilterModification();
        copy.setName(name);
        copy.setDescription(description);
        copy.setLabel(label);
        copy.setClassification(classification);
        copy.setModification(m_modification);
        copy.setMin(m_min);
        copy.setMax(m_max);
        copy.setPositionOne(m_postionOne);
        copy.setPositionTwo(m_postionTwo);
        return copy;
    }

    @XmlAttribute
    public String getModification()
    {
        return m_modification;
    }

    public void setModification(String a_modification)
    {
        m_modification = a_modification;
    }

    @XmlAttribute
    public Integer getPositionOne()
    {
        return m_postionOne;
    }

    public void setPositionOne(Integer a_postionOne)
    {
        m_postionOne = a_postionOne;
    }

    @XmlAttribute
    public Integer getPositionTwo()
    {
        return m_postionTwo;
    }

    public void setPositionTwo(Integer a_postionTwo)
    {
        m_postionTwo = a_postionTwo;
    }

}
