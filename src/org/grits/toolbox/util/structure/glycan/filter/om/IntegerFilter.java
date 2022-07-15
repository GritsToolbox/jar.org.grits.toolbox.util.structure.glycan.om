package org.grits.toolbox.util.structure.glycan.filter.om;

import javax.xml.bind.annotation.XmlAttribute;

public abstract class IntegerFilter extends Filter {
	
	protected Integer m_min = 0;
    protected Integer m_max = null;

    @XmlAttribute
    public Integer getMin()
    {
        return m_min;
    }

    public void setMin(Integer a_min)
    {
        m_min = a_min;
    }

    @XmlAttribute
    public Integer getMax()
    {
        return m_max;
    }

    public void setMax(Integer a_max)
    {
        m_max = a_max;
    }

    public String toString()
    {
        if (name != null)
        {
            String toStr = "";
            toStr = name + "[";
            if (m_min == null)
                toStr += " ";
            else
                toStr += m_min;
            toStr += "-";
            if (m_max == null)
                toStr += " ";
            else
                toStr += m_max;
            toStr += "]";

            return toStr;
        }
        return super.toString();
    }
}
