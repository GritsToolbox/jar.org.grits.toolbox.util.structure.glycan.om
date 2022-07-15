package org.grits.toolbox.util.structure.glycan.filter.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eurocarbdb.MolecularFramework.sugar.Anomer;
import org.eurocarbdb.MolecularFramework.sugar.GlycoEdge;
import org.eurocarbdb.MolecularFramework.sugar.Linkage;
import org.eurocarbdb.MolecularFramework.sugar.Modification;
import org.eurocarbdb.MolecularFramework.sugar.Monosaccharide;
import org.eurocarbdb.MolecularFramework.sugar.SubstituentType;
import org.eurocarbdb.MolecularFramework.sugar.Superclass;
import org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitorException;
import org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitorNodeType;

public class MonosaccharidePatternMatcher
{
    private GlycoVisitorNodeType m_visitorNodeType = new GlycoVisitorNodeType();
    private List<GlycoEdge> m_substituents = new ArrayList<GlycoEdge>();

    public List<GlycoEdge> getSubstituents()
    {
        return m_substituents;
    }

    public boolean matchMonosaccharide(MonosaccharidePattern a_pattern, Monosaccharide a_ms)
            throws GlycoVisitorException
    {
        if (this.matchMonosaccharide(a_ms, a_pattern.getMonosaccharide(),
                a_pattern.getAllowModifications()))
        {
            // matching MS found compare substituents
            this.m_substituents = new ArrayList<GlycoEdge>();
            for (GlycoEdge t_glycoEdge : a_ms.getChildEdges())
            {
                if (this.m_visitorNodeType.getSubstituent(t_glycoEdge.getChild()) != null)
                {
                    this.m_substituents.add(t_glycoEdge);
                }
            }
            HashMap<GlycoEdge, Boolean> t_usedSubstiutents = new HashMap<GlycoEdge, Boolean>();
            // looking good now check if the subst match
            for (GlycoEdge t_glycoEdge : a_pattern.getSubsitutentLinks())
            {
                if (!this.findMatchingSubsitutent(t_glycoEdge, this.m_substituents,
                        t_usedSubstiutents))
                {
                    return false;
                }
            }
            if (this.m_substituents.size() != a_pattern.getSubsitutentLinks().size()
                    && !a_pattern.getAllowSubstituents())
            {
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean findMatchingSubsitutent(GlycoEdge a_glycoEdge, List<GlycoEdge> a_substituents,
            HashMap<GlycoEdge, Boolean> a_usedSubstiutents) throws GlycoVisitorException
    {
        GlycoVisitorNodeType t_visitorNodeType = new GlycoVisitorNodeType();
        SubstituentType t_substPattern = t_visitorNodeType.getSubstituent(a_glycoEdge.getChild())
                .getSubstituentType();
        for (GlycoEdge t_glycoEdge : a_substituents)
        {
            if (a_usedSubstiutents.get(t_glycoEdge) == null)
            {
                SubstituentType t_subst = t_visitorNodeType.getSubstituent(t_glycoEdge.getChild())
                        .getSubstituentType();
                if (this.matchSubstituent(t_substPattern, t_subst, a_glycoEdge, t_glycoEdge))
                {
                    return true;
                }

            }
        }
        return false;
    }

    private boolean matchSubstituent(SubstituentType a_substPattern, SubstituentType a_subst,
            GlycoEdge a_glycoEdgePattern, GlycoEdge a_glycoEdge)
    {
        if (!a_substPattern.equals(a_subst))
        {
            return false;
        }
        // check the linkage
        HashMap<Linkage, Boolean> t_usedLinkage = new HashMap<Linkage, Boolean>();
        for (Linkage t_linkage : a_glycoEdgePattern.getGlycosidicLinkages())
        {
            if (!this.findMatchingLinkage(t_linkage, a_glycoEdge.getGlycosidicLinkages(),
                    t_usedLinkage))
            {
                return false;
            }
        }
        return true;
    }

    private boolean findMatchingLinkage(Linkage a_linkage, ArrayList<Linkage> a_glycosidicLinkages,
            HashMap<Linkage, Boolean> a_usedLinkage)
    {
        for (Linkage t_linkage : a_glycosidicLinkages)
        {
            if (a_usedLinkage.get(t_linkage) == null)
            {
                if (t_linkage.getParentLinkageType().equals(a_linkage.getParentLinkageType()))
                {
                    if (this.matchLinkagePosition(a_linkage.getParentLinkages(),
                            t_linkage.getParentLinkages()))
                    {
                        a_usedLinkage.put(t_linkage, Boolean.TRUE);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean matchLinkagePosition(ArrayList<Integer> a_parentLinkagesPattern,
            ArrayList<Integer> a_parentLinkages)
    {
        for (Integer t_position : a_parentLinkagesPattern)
        {
            if (!t_position.equals(Linkage.UNKNOWN_POSITION))
            {
                if (!a_parentLinkages.contains(t_position))
                {
                    return false;
                }
            }
        }
        if (a_parentLinkagesPattern.size() == a_parentLinkages.size())
        {
            return true;
        }
        return false;
    }

    private boolean matchMonosaccharide(Monosaccharide a_monosaccharide, Monosaccharide a_pattern,
            boolean a_allowModifications)
    {
        // anomer
        if (a_pattern.getAnomer() != Anomer.Unknown)
        {
            if (!a_pattern.getAnomer().equals(a_monosaccharide.getAnomer()))
            {
                return false;
            }
        }
        // configuration
        if (a_pattern.getBaseType().size() != 0)
        {
            if (a_pattern.getBaseType().size() != a_monosaccharide.getBaseType().size())
            {
                return false;
            }
            for (int t_i = 0; t_i < a_pattern.getBaseType().size(); t_i++)
            {
                if (!a_pattern.getBaseType().get(t_i)
                        .equals(a_monosaccharide.getBaseType().get(t_i)))
                {
                    return false;
                }
            }
        }
        // superclass
        if (a_pattern.getSuperclass() != Superclass.SUG)
        {
            if (!a_pattern.getSuperclass().equals(a_monosaccharide.getSuperclass()))
            {
                return false;
            }
        }
        // ring start
        if (a_pattern.getRingStart() != Monosaccharide.UNKNOWN_RING)
        {
            if (a_pattern.getRingStart() != a_monosaccharide.getRingStart())
            {
                return false;
            }
        }
        // ring end
        if (a_pattern.getRingEnd() != Monosaccharide.UNKNOWN_RING)
        {
            if (a_pattern.getRingEnd() != a_monosaccharide.getRingEnd())
            {
                return false;
            }
        }
        // modification
        HashMap<Modification, Boolean> t_usedModifications = new HashMap<Modification, Boolean>();
        for (Modification t_modification : a_pattern.getModification())
        {
            if (!this.findMatchingModification(t_modification, a_monosaccharide.getModification(),
                    t_usedModifications))
            {
                return false;
            }
        }
        if (!a_allowModifications)
        {
            if (a_pattern.getModification().size() != a_monosaccharide.getModification().size())
            {
                return false;
            }
        }
        return true;
    }

    private boolean findMatchingModification(Modification a_pattern,
            ArrayList<Modification> a_modifications,
            HashMap<Modification, Boolean> a_usedModifications)
    {
        for (Modification t_modification : a_modifications)
        {
            if (a_usedModifications.get(t_modification) == null)
            {
                if (a_pattern.getModificationType().equals(t_modification.getModificationType()))
                {
                    if (a_pattern.getPositionOne() == Modification.UNKNOWN_POSITION)
                    {
                        a_usedModifications.put(t_modification, Boolean.TRUE);
                        return true;
                    }
                    if (a_pattern.getPositionOne() == t_modification.getPositionOne())
                    {
                        if (a_pattern.getPositionTwo() == null)
                        {
                            if (t_modification.getPositionTwo() == null)
                            {
                                a_usedModifications.put(t_modification, Boolean.TRUE);
                                return true;
                            }
                        }
                        else
                        {
                            if (t_modification.getPositionTwo() != null)
                            {
                                if (a_pattern.getPositionTwo()
                                        .equals(t_modification.getPositionTwo()))
                                {
                                    a_usedModifications.put(t_modification, Boolean.TRUE);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

}
