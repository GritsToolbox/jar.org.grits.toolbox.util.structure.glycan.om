package org.grits.toolbox.util.structure.glycan.database;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class GlycanStructure
{
    private String m_id = null;
    private String m_GWBSequence = null;
    private String m_Glytoucanid = null;

    public String getId()
    {
        return m_id;
    }

    @XmlAttribute(name = "id")
    public void setId(String a_id)
    {
        m_id = a_id;
    }

    public String getGWBSequence()
    {
        return m_GWBSequence;
    }

    @XmlAttribute(name = "GWBSequence")
    public void setGWBSequence(String GWBSequence)
    {
        this.m_GWBSequence = GWBSequence;
    }
    
    @XmlAttribute(name="GlytoucanId")
    public String getGlytoucanid () {
    	return this.m_Glytoucanid;
    }
    
    public void setGlytoucanid (String gid) {
    	this.m_Glytoucanid = gid;
    }

    @Override
    public boolean equals(Object a_object)
    {
        // test if its of the same class
        if (!(a_object instanceof GlycanStructure))
        {
            return false;
        }
        //
        GlycanStructure other = (GlycanStructure) a_object;
        if ((m_id != null && other.getId() == null) || (m_id == null && other.getId() != null)
                || !m_id.equals(other.getId()))
            return false;

        if ((m_GWBSequence != null && other.getGWBSequence() == null)
                || (m_GWBSequence == null && other.getGWBSequence() != null)
                || !m_GWBSequence.equals(other.getGWBSequence()))
            return false;

        return true;
    }

}
