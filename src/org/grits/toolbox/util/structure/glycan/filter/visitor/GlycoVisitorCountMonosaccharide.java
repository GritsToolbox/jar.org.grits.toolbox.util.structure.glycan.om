package org.grits.toolbox.util.structure.glycan.filter.visitor;

import org.eurocarbdb.MolecularFramework.sugar.GlycoEdge;
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

public class GlycoVisitorCountMonosaccharide implements GlycoVisitor
{
    private Boolean m_terminalOnly = null;
    private MonosaccharidePattern m_pattern = null;
    private MonosaccharidePatternMatcher m_matcher = new MonosaccharidePatternMatcher();

    public MonosaccharidePattern getMonosaccharidePattern()
    {
        return m_pattern;
    }

    public void setMonosaccharidePattern(MonosaccharidePattern a_pattern)
    {
        m_pattern = a_pattern;
    }

    private Integer m_count = 0;

    @Override
    public void clear()
    {
        this.m_count = 0;
        if (this.m_terminalOnly == null)
        {
            this.m_terminalOnly = false;
        }
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
            if (this.m_terminalOnly)
            {
                if (a_monosaccharide.getChildEdges().size() == this.m_matcher.getSubstituents()
                        .size())
                {
                    this.m_count++;
                }
            }
            else
            {
                this.m_count++;
            }
        }
    }

    public Integer getCount()
    {
        return m_count;
    }

    public Boolean getTerminalOnly()
    {
        return m_terminalOnly;
    }

    public void setTerminalOnly(Boolean a_terminalOnly)
    {
        m_terminalOnly = a_terminalOnly;
    }

}
