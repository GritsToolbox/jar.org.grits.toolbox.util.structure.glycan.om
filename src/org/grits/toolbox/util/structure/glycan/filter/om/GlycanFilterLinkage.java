package org.grits.toolbox.util.structure.glycan.filter.om;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterException;

@XmlRootElement(name = "linkageFilter")
public class GlycanFilterLinkage extends BooleanFilter
{
    private MonosaccharideDefintion m_monosaccharide = null;
    private String m_chosenLinkagePattern = null;
    private boolean m_terminal = false;

    public boolean accept(GlycanFilterVisitor a_operator) throws GlycanFilterException
    {
        return a_operator.visit(this);
    }

    @Override
    public String toString()
    {
        if (name != null)
        {
            return name;
        }
        return super.toString();
    }

    @Override
    public Filter copy()
    {
        GlycanFilterLinkage copy = new GlycanFilterLinkage();
        copy.setName(name);
        copy.setDescription(description);
        copy.setLabel(label);
        copy.setClassification(classification);
        copy.setTerminal(this.m_terminal);
        copy.setChosenLinkagePattern(this.m_chosenLinkagePattern);
        copy.setMonosaccharide(this.m_monosaccharide.copy());
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

    @XmlAttribute
    public String getChosenLinkagePattern()
    {
        return m_chosenLinkagePattern;
    }

    public void setChosenLinkagePattern(String a_chosenLinkagePattern)
    {
        m_chosenLinkagePattern = a_chosenLinkagePattern;
    }

    @XmlAttribute
    public boolean isTerminal()
    {
        return m_terminal;
    }

    public void setTerminal(boolean a_terminal)
    {
        m_terminal = a_terminal;
    }

}
