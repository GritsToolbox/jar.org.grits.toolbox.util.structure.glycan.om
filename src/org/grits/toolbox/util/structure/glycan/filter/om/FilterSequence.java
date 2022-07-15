package org.grits.toolbox.util.structure.glycan.filter.om;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eurocarbdb.MolecularFramework.io.SugarImporterException;
import org.eurocarbdb.MolecularFramework.io.GlycoCT.SugarImporterGlycoCTCondensed;
import org.eurocarbdb.MolecularFramework.sugar.Sugar;

@XmlRootElement(name = "sequence")
public class FilterSequence
{
    private Boolean m_reducingEnd = false;
    private String sequence;
    private Sugar m_subStructure = null;

    @XmlAttribute
    public Boolean getReducingEnd()
    {
        return m_reducingEnd;
    }

    public void setReducingEnd(Boolean a_reducingEnd)
    {
        m_reducingEnd = a_reducingEnd;
    }

    @XmlTransient
    public Sugar getSubStructure() throws SugarImporterException
    {
        if (this.m_subStructure == null)
        {
            // parse sequence in a sugar object
            SugarImporterGlycoCTCondensed t_importer = new SugarImporterGlycoCTCondensed();
            this.m_subStructure = t_importer.parse(sequence);
        }
        return m_subStructure;
    }

    public void setSubStructure(Sugar a_subStructure)
    {
        m_subStructure = a_subStructure;
    }

    public String getSequence()
    {
        return sequence;
    }

    public void setSequence(String sequenceStr)
    {
        this.sequence = sequenceStr;
    }
}
