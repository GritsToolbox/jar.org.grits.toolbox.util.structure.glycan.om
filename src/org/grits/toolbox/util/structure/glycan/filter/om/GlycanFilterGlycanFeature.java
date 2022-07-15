package org.grits.toolbox.util.structure.glycan.filter.om;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterException;

@XmlRootElement(name = "glycanFeatureFilter")
public class GlycanFilterGlycanFeature extends BooleanFilter
{
    private boolean m_unknownAnomerAllowed = true;
    private boolean m_unknownRingsizeAllowed = true;
    private boolean m_unknownBasetypeAllowed = true;
    private boolean m_residueSugAllowed = true;
    private boolean m_unknownModificationPositionAllowed = true;
    private boolean m_unknownLinkageTypeAllowed = true;
    private boolean m_unknownLinkagePositionAllowed = true;
    private boolean m_unitUndAllowed = true;
    private boolean m_unknownRepeatAllowed = true;
    private boolean m_unitRepeatAllowed = true;
    private boolean m_unconnectedTreeAllowed = true;
    private boolean m_unitCyclicAllowed = true;
    private boolean m_unitProbableAllowed = true;
    private boolean m_reducingAlditolAllowed = true;
    private boolean m_unknownConfigurationAllowed = true;
    
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
        GlycanFilterGlycanFeature copy = new GlycanFilterGlycanFeature();
        copy.setName(name);
        copy.setDescription(description);
        copy.setLabel(label);
        copy.setClassification(classification);
        copy.setUnknownAnomerAllowed( m_unknownAnomerAllowed );
        copy.setUnknownRingsizeAllowed(m_unknownRingsizeAllowed );
        copy.setUnknownBasetypeAllowed(m_unknownBasetypeAllowed );
        copy.setResidueSugAllowed(m_residueSugAllowed );
        copy.setUnknownModificationPositionAllowed(m_unknownModificationPositionAllowed );
        copy.setUnknownLinkageTypeAllowed(m_unknownLinkageTypeAllowed );
        copy.setUnknownLinkagePositionAllowed( m_unknownLinkagePositionAllowed );
        copy.setUnitUndAllowed( m_unitUndAllowed );
        copy.setUnknownRepeatAllowed(m_unknownRepeatAllowed );
        copy.setUnitRepeatAllowed( m_unitRepeatAllowed );
        copy.setUnconnectedTreeAllowed(m_unconnectedTreeAllowed );
        copy.setUnitCyclicAllowed( m_unitCyclicAllowed );
        copy.setUnitProbableAllowed(m_unitProbableAllowed );
        copy.setReducingAlditolAllowed(m_reducingAlditolAllowed );
        copy.setUnknownConfigurationAllowed(m_unknownConfigurationAllowed );
        return copy;
    }

    @XmlAttribute
    public boolean isUnknownAnomerAllowed()
    {
        return m_unknownAnomerAllowed;
    }

    public void setUnknownAnomerAllowed(boolean a_unknownAnomerAllowed)
    {
        m_unknownAnomerAllowed = a_unknownAnomerAllowed;
    }

    @XmlAttribute
    public boolean isUnknownRingsizeAllowed()
    {
        return m_unknownRingsizeAllowed;
    }

    public void setUnknownRingsizeAllowed(boolean a_unknownRingsizeAllowed)
    {
        m_unknownRingsizeAllowed = a_unknownRingsizeAllowed;
    }

    @XmlAttribute
    public boolean isUnknownBasetypeAllowed()
    {
        return m_unknownBasetypeAllowed;
    }

    public void setUnknownBasetypeAllowed(boolean a_unknownBasetypeAllowed)
    {
        m_unknownBasetypeAllowed = a_unknownBasetypeAllowed;
    }

    @XmlAttribute
    public boolean isResidueSugAllowed()
    {
        return m_residueSugAllowed;
    }

    public void setResidueSugAllowed(boolean a_residueSugAllowed)
    {
        m_residueSugAllowed = a_residueSugAllowed;
    }

    @XmlAttribute
    public boolean isUnknownModificationPositionAllowed()
    {
        return m_unknownModificationPositionAllowed;
    }

    public void setUnknownModificationPositionAllowed(
            boolean a_unknownModificationPositionAllowed)
    {
        m_unknownModificationPositionAllowed = a_unknownModificationPositionAllowed;
    }

    @XmlAttribute
    public boolean isUnknownLinkageTypeAllowed()
    {
        return m_unknownLinkageTypeAllowed;
    }

    public void setUnknownLinkageTypeAllowed(boolean a_unknownLinkageTypeAllowed)
    {
        m_unknownLinkageTypeAllowed = a_unknownLinkageTypeAllowed;
    }

    @XmlAttribute
    public boolean isUnknownLinkagePositionAllowed()
    {
        return m_unknownLinkagePositionAllowed;
    }

    public void setUnknownLinkagePositionAllowed(
            boolean a_unknownLinkagePositionAllowed)
    {
        m_unknownLinkagePositionAllowed = a_unknownLinkagePositionAllowed;
    }

    @XmlAttribute
    public boolean isUnitUndAllowed()
    {
        return m_unitUndAllowed;
    }

    public void setUnitUndAllowed(boolean a_unitUndAllowed)
    {
        m_unitUndAllowed = a_unitUndAllowed;
    }

    @XmlAttribute
    public boolean isUnknownRepeatAllowed()
    {
        return m_unknownRepeatAllowed;
    }

    public void setUnknownRepeatAllowed(boolean a_unknownRepeatAllowed)
    {
        m_unknownRepeatAllowed = a_unknownRepeatAllowed;
    }

    @XmlAttribute
    public boolean isUnitRepeatAllowed()
    {
        return m_unitRepeatAllowed;
    }

    public void setUnitRepeatAllowed(boolean a_unitRepeatAllowed)
    {
        m_unitRepeatAllowed = a_unitRepeatAllowed;
    }

    @XmlAttribute
    public boolean isUnconnectedTreeAllowed()
    {
        return m_unconnectedTreeAllowed;
    }

    public void setUnconnectedTreeAllowed(boolean a_unconnectedTreeAllowed)
    {
        m_unconnectedTreeAllowed = a_unconnectedTreeAllowed;
    }

    @XmlAttribute
    public boolean isUnitCyclicAllowed()
    {
        return m_unitCyclicAllowed;
    }

    public void setUnitCyclicAllowed(boolean a_unitCyclicAllowed)
    {
        m_unitCyclicAllowed = a_unitCyclicAllowed;
    }

    @XmlAttribute
    public boolean isUnitProbableAllowed()
    {
        return m_unitProbableAllowed;
    }

    public void setUnitProbableAllowed(boolean a_unitProbableAllowed)
    {
        m_unitProbableAllowed = a_unitProbableAllowed;
    }

    @XmlAttribute
    public boolean isReducingAlditolAllowed()
    {
        return m_reducingAlditolAllowed;
    }

    public void setReducingAlditolAllowed(boolean a_reducingAlditolAllowed)
    {
        m_reducingAlditolAllowed = a_reducingAlditolAllowed;
    }

    @XmlAttribute
    public boolean isUnknownConfigurationAllowed()
    {
        return m_unknownConfigurationAllowed;
    }

    public void setUnknownConfigurationAllowed(
            boolean a_unknownConfigurationAllowed)
    {
        m_unknownConfigurationAllowed = a_unknownConfigurationAllowed;
    }

}
