package org.grits.toolbox.util.structure.glycan.filter.om;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterException;

@XmlRootElement(name = "substructureFilter")
public class GlycanFilterSubstructure extends BooleanFilter
{
    private List<FilterSequence> m_substructure = new ArrayList<FilterSequence>();

    public List<FilterSequence> getSubstructure()
    {
        return m_substructure;
    }

    public void setSubstructure(List<FilterSequence> a_substructure)
    {
        m_substructure = a_substructure;
    }

    public boolean accept(GlycanFilterVisitor a_operator) throws GlycanFilterException
    {
        return a_operator.visit(this);
    }

    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public Filter copy()
    {
        GlycanFilterSubstructure copy = new GlycanFilterSubstructure();
        copy.setName(name);
        copy.setDescription(description);
        copy.setLabel(label);
        copy.setClassification(classification);
        copy.setSubstructure(m_substructure);
        return copy;
    }

}
