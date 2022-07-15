package org.grits.toolbox.util.structure.glycan.filter.visitor;

import java.util.HashMap;

import org.eurocarbdb.MolecularFramework.sugar.GlycoEdge;
import org.eurocarbdb.MolecularFramework.sugar.Linkage;
import org.eurocarbdb.MolecularFramework.sugar.Monosaccharide;
import org.eurocarbdb.MolecularFramework.sugar.NonMonosaccharide;
import org.eurocarbdb.MolecularFramework.sugar.Substituent;
import org.eurocarbdb.MolecularFramework.sugar.Sugar;
import org.eurocarbdb.MolecularFramework.sugar.SugarUnitAlternative;
import org.eurocarbdb.MolecularFramework.sugar.SugarUnitCyclic;
import org.eurocarbdb.MolecularFramework.sugar.SugarUnitRepeat;
import org.eurocarbdb.MolecularFramework.sugar.UnderdeterminedSubTree;
import org.eurocarbdb.MolecularFramework.sugar.UnvalidatedGlycoNode;
import org.eurocarbdb.MolecularFramework.util.traverser.GlycoTraverser;
import org.eurocarbdb.MolecularFramework.util.traverser.GlycoTraverserTreeSingle;
import org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitor;
import org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitorException;

public class GlycoVisitorLinakgePattern implements GlycoVisitor
{
    private MonosaccharidePattern m_pattern = null;
    private MonosaccharidePatternMatcher m_matcher = new MonosaccharidePatternMatcher();
    private boolean m_foundPattern = false;
    private boolean m_violatePattern = false;
    private boolean m_terminal = false;
    // the following fields are filled by parsing the linkagePattern string
    private HashMap<Integer, Boolean> m_parentLinkage = new HashMap<Integer, Boolean>();

    public MonosaccharidePattern getMonosaccharidePattern()
    {
        return m_pattern;
    }

    public void setMonosaccharidePattern(MonosaccharidePattern a_pattern)
    {
        m_pattern = a_pattern;
    }

    // number { "," number } ]
    public void linkagePattern(String a_pattern) throws GlycoVisitorException
    {
        // reset old settings
        this.m_parentLinkage.clear();
        // cut of the the string in the beginning, unused
        String t_pattern = a_pattern.trim();
        if (t_pattern.length() == 0)
        {
            throw new GlycoVisitorException("Invalid linkage pattern: missing linkage position");
        }
        String[] t_parts = t_pattern.split(",");
        for (String t_string : t_parts)
        {
            try
            {
                this.m_parentLinkage.put(Integer.parseInt(t_string.trim()), Boolean.TRUE);
            }
            catch (NumberFormatException e)
            {
                throw new GlycoVisitorException(
                        "Invalid linkage pattern: linkage position must be a number: " + t_string,
                        e);
            }
        }
    }

    @Override
    public void clear()
    {
        this.m_foundPattern = false;
        this.m_violatePattern = false;
        if (this.m_pattern.getAllowSubstituents() == null)
        {
            this.m_pattern.setAllowSubstituents(false);
        }
        if (this.m_pattern.getAllowModifications() == null)
        {
            this.m_pattern.setAllowModifications(false);
        }
    }

    @Override
    public GlycoTraverser getTraverser(GlycoVisitor a_visitor) throws GlycoVisitorException
    {
        return new GlycoTraverserTreeSingle(a_visitor);
    }

    @Override
    public void start(Sugar a_sugar) throws GlycoVisitorException
    {
        this.clear();
        GlycoTraverser t_traverser = this.getTraverser(this);
        t_traverser.traverseGraph(a_sugar);
        for (UnderdeterminedSubTree t_tree : a_sugar.getUndeterminedSubTrees())
        {
            t_traverser.traverseGraph(t_tree);
        }
    }

    @Override
    public void visit(NonMonosaccharide a_arg0) throws GlycoVisitorException
    {
        // nothing to do
    }

    @Override
    public void visit(SugarUnitRepeat a_repeat) throws GlycoVisitorException
    {
        GlycoTraverser t_traverser = this.getTraverser(this);
        t_traverser.traverseGraph(a_repeat);
        for (UnderdeterminedSubTree t_tree : a_repeat.getUndeterminedSubTrees())
        {
            t_traverser.traverseGraph(t_tree);
        }
    }

    @Override
    public void visit(Substituent a_arg0) throws GlycoVisitorException
    {
        // nothing to do
    }

    @Override
    public void visit(SugarUnitCyclic a_arg0) throws GlycoVisitorException
    {
        // nothing to do
    }

    @Override
    public void visit(SugarUnitAlternative a_arg0) throws GlycoVisitorException
    {
        throw new GlycoVisitorException("SugarUnitAlternative is not supported.");
    }

    @Override
    public void visit(UnvalidatedGlycoNode a_arg0) throws GlycoVisitorException
    {
        throw new GlycoVisitorException("UnvalidatedGlycoNode is not supported.");
    }

    @Override
    public void visit(GlycoEdge a_arg0) throws GlycoVisitorException
    {
        // nothing to do
    }

    @Override
    public void visit(Monosaccharide a_monosaccharide) throws GlycoVisitorException
    {
        if (this.m_matcher.matchMonosaccharide(this.m_pattern, a_monosaccharide))
        {
            // we found the right monosaccharide, now its time to look around
            if (a_monosaccharide.getParentEdge() != null)
            {
                if (this.m_terminal)
                {
                    if (a_monosaccharide.getChildEdges().size() == this.m_matcher.getSubstituents()
                            .size())
                    {
                        if (this.matchLinkage(this.m_parentLinkage, a_monosaccharide.getParentEdge()))
                        {
                            this.m_foundPattern = true;
                        }
                        else
                        {
                            this.m_violatePattern = true;
                        }
                    }
                }
                else
                {
                    if (this.matchLinkage(this.m_parentLinkage, a_monosaccharide.getParentEdge()))
                    {
                        this.m_foundPattern = true;
                    }
                    else
                    {
                        this.m_violatePattern = true;
                    }
                }
            }
        }
    }

    private boolean matchLinkage(HashMap<Integer, Boolean> a_linkagePositions,
            GlycoEdge a_parentEdge)
    {
        for (Linkage t_linkage : a_parentEdge.getGlycosidicLinkages())
        {
            for (Integer t_pos : t_linkage.getParentLinkages())
            {
                if (t_pos == Linkage.UNKNOWN_POSITION)
                {
                    return true;
                }
                if (a_linkagePositions.get(t_pos) != null)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFoundPattern()
    {
        if (this.m_foundPattern)
        {
            if (this.m_violatePattern)
            {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean isTerminal()
    {
        return m_terminal;
    }

    public void setTerminal(boolean a_terminal)
    {
        m_terminal = a_terminal;
    }

}
