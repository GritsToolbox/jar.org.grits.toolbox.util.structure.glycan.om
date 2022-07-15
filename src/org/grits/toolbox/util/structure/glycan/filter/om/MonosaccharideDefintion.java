package org.grits.toolbox.util.structure.glycan.filter.om;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "monosaccharide")
public class MonosaccharideDefintion
{
    private String m_sequence = null;
    private Boolean m_allowSubstituents = false;
    private Boolean m_allowModifications = false;

    @XmlAttribute
    public Boolean getAllowSubstituents()
    {
        return m_allowSubstituents;
    }

    public void setAllowSubstituents(Boolean a_allowSubstituents)
    {
        m_allowSubstituents = a_allowSubstituents;
    }

    @XmlAttribute
    public Boolean getAllowModifications()
    {
        return m_allowModifications;
    }

    public void setAllowModifications(Boolean a_allowModifications)
    {
        m_allowModifications = a_allowModifications;
    }

    public String getSequence()
    {
        return m_sequence;
    }

    public void setSequence(String a_sequence)
    {
        m_sequence = a_sequence;
    }

    public MonosaccharideDefintion copy()
    {
        MonosaccharideDefintion t_copy = new MonosaccharideDefintion();
        t_copy.setAllowModifications(this.m_allowModifications);
        t_copy.setAllowSubstituents(this.m_allowSubstituents);
        t_copy.setSequence(this.m_sequence);
        return t_copy;
    }

}
