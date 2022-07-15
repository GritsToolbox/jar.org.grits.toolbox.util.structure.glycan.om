package org.grits.toolbox.util.structure.glycan.filter.om;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterException;

@XmlRootElement(name = "monosaccharideFilter")
public class GlycanFilterMonosaccharide extends IntegerFilter
{
    private Boolean m_terminalOnly = null;
    private MonosaccharideDefintion m_monosaccharide = null;

    @XmlAttribute
    public Boolean getTerminalOnly()
    {
        return m_terminalOnly;
    }

    public void setTerminalOnly(Boolean a_terminalOnly)
    {
        m_terminalOnly = a_terminalOnly;
    }

    public boolean accept(GlycanFilterVisitor a_operator) throws GlycanFilterException
    {
        return a_operator.visit(this);
    }

    @Override
    public Filter copy()
    {
        GlycanFilterMonosaccharide copy = new GlycanFilterMonosaccharide();
        copy.setName(name);
        copy.setDescription(description);
        copy.setLabel(label);
        copy.setClassification(classification);
        copy.setMin(m_min);
        copy.setMax(m_max);
        copy.setTerminalOnly(m_terminalOnly);
        copy.setMonosaccharide(this.m_monosaccharide);
        return copy;
    }

    public MonosaccharideDefintion getMonosaccharide()
    {
        return m_monosaccharide;
    }

    public void setMonosaccharide(MonosaccharideDefintion a_monosaccharide)
    {
        m_monosaccharide = a_monosaccharide;
    }

}
