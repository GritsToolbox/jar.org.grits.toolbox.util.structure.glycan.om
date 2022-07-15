package org.grits.toolbox.util.structure.glycan.filter.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eurocarbdb.MolecularFramework.io.SugarImporterException;
import org.eurocarbdb.MolecularFramework.io.GlycoCT.SugarImporterGlycoCTCondensed;
import org.eurocarbdb.MolecularFramework.sugar.GlycoEdge;
import org.eurocarbdb.MolecularFramework.sugar.GlycoNode;
import org.eurocarbdb.MolecularFramework.sugar.GlycoconjugateException;
import org.eurocarbdb.MolecularFramework.sugar.Monosaccharide;
import org.eurocarbdb.MolecularFramework.sugar.Substituent;
import org.eurocarbdb.MolecularFramework.sugar.Sugar;
import org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitorException;
import org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitorNodeType;
import org.grits.toolbox.util.structure.glycan.filter.om.MonosaccharideDefintion;

public class MonosaccharidePattern
{
    private Monosaccharide m_monosaccharide = null;
    private List<GlycoEdge> m_subsitutentLinks = new ArrayList<GlycoEdge>();
    private Boolean m_allowSubstituents = false;
    private Boolean m_allowModifications = false;

    public Monosaccharide getMonosaccharide()
    {
        return m_monosaccharide;
    }

    public void setMonosaccharide(Monosaccharide a_monosaccharide)
    {
        m_monosaccharide = a_monosaccharide;
    }

    public List<GlycoEdge> getSubsitutentLinks()
    {
        return m_subsitutentLinks;
    }

    public void setSubsitutentLinks(List<GlycoEdge> a_subsitutentLinks)
    {
        m_subsitutentLinks = a_subsitutentLinks;
    }

    public Boolean getAllowSubstituents()
    {
        return m_allowSubstituents;
    }

    public void setAllowSubstituents(Boolean a_allowSubstituents)
    {
        m_allowSubstituents = a_allowSubstituents;
    }

    public Boolean getAllowModifications()
    {
        return m_allowModifications;
    }

    public void setAllowModifications(Boolean a_allowModifications)
    {
        m_allowModifications = a_allowModifications;
    }

    public static MonosaccharidePattern fromSugar(Sugar a_monosaccharideDescription)
            throws GlycoVisitorException
    {
        MonosaccharidePattern t_result = new MonosaccharidePattern();
        try
        {
            if (a_monosaccharideDescription.getRootNodes().size() != 1)
            {
                throw new GlycoVisitorException(
                        "Invalid monosaccharide description: To many roots.");
            }
            for (GlycoNode t_root : a_monosaccharideDescription.getRootNodes())
            {
                GlycoVisitorNodeType t_typeVisitor = new GlycoVisitorNodeType();
                Monosaccharide t_monosaccharide = t_typeVisitor.getMonosaccharide(t_root);
                if (t_monosaccharide == null)
                {
                    throw new GlycoVisitorException(
                            "Invalid monosaccharide description: Root is not a monosaccharide.");
                }
                t_result.setMonosaccharide(t_monosaccharide);
                List<GlycoEdge> t_edge = new ArrayList<>();
                for (GlycoEdge t_glycoEdge : t_root.getChildEdges())
                {
                    Substituent t_subst = t_typeVisitor.getSubstituent(t_glycoEdge.getChild());
                    if (t_subst == null)
                    {
                        throw new GlycoVisitorException(
                                "Invalid monosaccharide description: Can not have more than one monosaccharide.");
                    }
                    if (t_subst.getChildEdges().size() != 0)
                    {
                        throw new GlycoVisitorException(
                                "Invalid monosaccharide description: Substituent can not have child nodes.");
                    }
                    t_edge.add(t_glycoEdge);
                }
                t_result.setSubsitutentLinks(t_edge);
            }
        }
        catch (GlycoconjugateException t_e)
        {
            throw new GlycoVisitorException(t_e.getMessage(), t_e);
        }
        return t_result;
    }

    public static MonosaccharidePattern fromString(String a_sequence)
            throws GlycoVisitorException, SugarImporterException
    {
        SugarImporterGlycoCTCondensed t_importer = new SugarImporterGlycoCTCondensed();
        return MonosaccharidePattern.fromSugar(t_importer.parse(a_sequence));
    }

    public static MonosaccharidePattern fromMonosaccharideDefintion(
            MonosaccharideDefintion a_defintion)
                    throws GlycoVisitorException, SugarImporterException
    {
        MonosaccharidePattern t_patter = MonosaccharidePattern
                .fromString(a_defintion.getSequence());
        t_patter.setAllowModifications(a_defintion.getAllowModifications());
        t_patter.setAllowSubstituents(a_defintion.getAllowSubstituents());
        return t_patter;
    }
}
