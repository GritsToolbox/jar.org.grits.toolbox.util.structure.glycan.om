package org.grits.toolbox.util.structure.glycan.filter.om;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterException;

@XmlRootElement(name = "antennaFilter")
public class GlycanFilterAntenna extends IntegerFilter
{
    private List<MonosaccharideDefintion> m_excludeMonosaccharide = new ArrayList<>();
    private boolean m_filterBisection = true;

    @XmlAttribute
    public boolean isFilterBisection()
    {
        return m_filterBisection;
    }

    public void setFilterBisection(boolean a_filterBisection)
    {
        m_filterBisection = a_filterBisection;
    }

    public boolean accept(GlycanFilterVisitor a_operator) throws GlycanFilterException
    {
        return a_operator.visit(this);
    }

    @Override
    public Filter copy()
    {
        GlycanFilterAntenna copy = new GlycanFilterAntenna();
        copy.setName(name);
        copy.setDescription(description);
        copy.setLabel(label);
        copy.setClassification(classification);
        copy.setMin(m_min);
        copy.setMax(m_max);
        copy.setFilterBisection(this.m_filterBisection);
        List<MonosaccharideDefintion> t_definitions = new ArrayList<>();
        for (MonosaccharideDefintion t_monosaccharideDefintion : this.m_excludeMonosaccharide)
        {
            t_definitions.add(t_monosaccharideDefintion.copy());
        }
        copy.setExcludeMonosaccharide(t_definitions);
        return copy;
    }

    public List<MonosaccharideDefintion> getExcludeMonosaccharide()
    {
        return m_excludeMonosaccharide;
    }

    public void setExcludeMonosaccharide(List<MonosaccharideDefintion> a_excludeMonosaccharide)
    {
        m_excludeMonosaccharide = a_excludeMonosaccharide;
    }

}
