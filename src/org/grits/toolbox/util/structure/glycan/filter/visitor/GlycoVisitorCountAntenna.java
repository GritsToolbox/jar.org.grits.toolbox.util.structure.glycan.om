package org.grits.toolbox.util.structure.glycan.filter.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eurocarbdb.MolecularFramework.io.SugarImporterException;
import org.eurocarbdb.MolecularFramework.sugar.GlycoEdge;
import org.eurocarbdb.MolecularFramework.sugar.GlycoNode;
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
import org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitor;
import org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitorException;
import org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitorNodeType;

public class GlycoVisitorCountAntenna implements GlycoVisitor
{
    private Integer m_count = 0;
    private List<MonosaccharidePattern> m_excludeMS = new ArrayList<>();
    private MonosaccharidePatternMatcher m_matcher = new MonosaccharidePatternMatcher();
    private HashMap<GlycoNode, Boolean> m_hashExcluded = new HashMap<>();
    private GlycoVisitorNodeType m_visitorNodeType = new GlycoVisitorNodeType();
    private boolean m_filterBisection = true;
    private MonosaccharidePattern m_bisectionGlcNAc = null;
    private MonosaccharidePattern m_bisectionCoreMan = null;
    private MonosaccharidePattern m_bisectionCoreGlcNAc = null;

    public GlycoVisitorCountAntenna() throws GlycoVisitorException, SugarImporterException
    {
        super();
        this.m_bisectionGlcNAc = MonosaccharidePattern
                .fromString("RES\n1b:x-dglc-HEX-1:5\n2s:n-acetyl\nLIN\n1:1d(2+1)2n");
        this.m_bisectionCoreMan = MonosaccharidePattern.fromString("RES\n1b:x-dman-HEX-1:5");
        this.m_bisectionCoreGlcNAc = MonosaccharidePattern
                .fromString("RES\n1b:x-dglc-HEX-1:5\n2s:n-acetyl\nLIN\n1:1d(2+1)2n");
    }

    @Override
    public void clear()
    {
        this.m_hashExcluded.clear();
        this.m_count = 0;
    }

    @Override
    public GlycoTraverser getTraverser(GlycoVisitor a_visitor) throws GlycoVisitorException
    {
        return new GlycoTraverserTreeProstOrder(a_visitor);
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
        throw new GlycoVisitorException("NonMonosaccharide is not supported.");
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
        if (this.terminalMonosaccharide(a_monosaccharide))
        {
            this.m_count++;
        }
    }

    private boolean terminalMonosaccharide(Monosaccharide a_monosaccharide)
            throws GlycoVisitorException
    {
        GlycoVisitorNodeType t_visitorNodeType = new GlycoVisitorNodeType();
        for (GlycoEdge t_glycoEdge : a_monosaccharide.getChildEdges())
        {
            // if the child MS was excluded this one can still be considered a
            // leaf
            if (this.m_hashExcluded.get(t_glycoEdge.getChild()) == null)
            {
                // check if it is anything else than a substituent
                if (t_visitorNodeType.getSubstituent(t_glycoEdge.getChild()) == null)
                {
                    return false;
                }
            }
        }
        // we found a leaf monosaccharide
        if (this.m_filterBisection)
        {
            // look for bisection
            if (this.isBisection(a_monosaccharide))
            {
                return false;
            }
        }
        // now we test if it is in the exclude list
        for (MonosaccharidePattern t_monosaccharidePattern : this.m_excludeMS)
        {
            if (this.m_matcher.matchMonosaccharide(t_monosaccharidePattern, a_monosaccharide))
            {
                // matches a pattern, now we test if the parent MS has another
                // child MS
                if (this.hasTwoChildMS(a_monosaccharide.getParentNode()))
                {
                    this.m_hashExcluded.put(a_monosaccharide, Boolean.TRUE);
                    return false;
                }
                // its a leaf after all, independent what the exclude list
                // says
                return true;
            }
        }
        return true;
    }

    private boolean isBisection(Monosaccharide a_monosaccharide) throws GlycoVisitorException
    {
        if (this.m_matcher.matchMonosaccharide(this.m_bisectionGlcNAc, a_monosaccharide))
        {
            // its a GlcNAc see if the parent is Man
            GlycoNode t_parent = a_monosaccharide.getParentNode();
            if (t_parent != null)
            {
                Monosaccharide t_parentMS = this.m_visitorNodeType.getMonosaccharide(t_parent);
                if (t_parentMS != null)
                {
                    if (this.m_matcher.matchMonosaccharide(this.m_bisectionCoreMan, t_parentMS))
                    {
                        // ok parent is a man, is its parent a GlcNAc?
                        t_parent = t_parent.getParentNode();
                        if (t_parent != null)
                        {
                            t_parentMS = this.m_visitorNodeType.getMonosaccharide(t_parent);
                            if (t_parentMS != null)
                            {
                                if (this.m_matcher.matchMonosaccharide(this.m_bisectionCoreGlcNAc,
                                        t_parentMS))
                                {
                                    // terminal-GlcNAc-Man-GlcNAc = bisection
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

    private boolean hasTwoChildMS(GlycoNode a_parentNode) throws GlycoVisitorException
    {
        if (a_parentNode == null)
        {
            return false;
        }
        int t_childMS = 0;
        for (GlycoEdge t_edge : a_parentNode.getChildEdges())
        {
            if (this.m_hashExcluded.get(t_edge.getChild()) == null)
            {
                if (this.m_visitorNodeType.getMonosaccharide(t_edge.getChild()) != null)
                {
                    t_childMS++;
                }
            }
        }
        if (t_childMS < 2)
        {
            return false;
        }
        return true;
    }

    public Integer getCount()
    {
        return this.m_count;
    }

    public List<MonosaccharidePattern> getExcludeMS()
    {
        return m_excludeMS;
    }

    public void setExcludeMS(List<MonosaccharidePattern> a_excludeMS)
    {
        m_excludeMS = a_excludeMS;
    }

    public boolean isFilterBisection()
    {
        return m_filterBisection;
    }

    public void setFilterBisection(boolean a_filterBisection)
    {
        m_filterBisection = a_filterBisection;
    }

}
