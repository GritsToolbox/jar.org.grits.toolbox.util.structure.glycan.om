package org.grits.toolbox.util.structure.glycan.filter.visitor;

import org.eurocarbdb.MolecularFramework.sugar.GlycoEdge;
import org.eurocarbdb.MolecularFramework.sugar.Modification;
import org.eurocarbdb.MolecularFramework.sugar.ModificationType;
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

public class GlycoVisitorCountModification implements GlycoVisitor
{
    private ModificationType m_modification = null;
    private Integer m_positionOne = null;
    private Integer m_positionTwo = null;
    private Integer m_count = 0;

    @Override
    public void clear()
    {
        this.m_count = 0;
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
    public void visit(NonMonosaccharide a_arg0) throws GlycoVisitorException
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
    public void visit(Substituent a_substituent) throws GlycoVisitorException
    {
        // nothing to do
    }

    public Integer getCount()
    {
        return m_count;
    }

    public ModificationType getModification()
    {
        return m_modification;
    }

    public void setModification(ModificationType a_modification)
    {
        m_modification = a_modification;
    }

    @Override
    public void visit(Monosaccharide a_monosaccharide) throws GlycoVisitorException
    {
        for (Modification t_modification : a_monosaccharide.getModification())
        {
            if (t_modification.getModificationType().equals(this.m_modification))
            {
                // found a matching modification
                if (this.matchPositions(t_modification))
                {
                    this.m_count++;
                }
            }
        }
    }

    private boolean matchPositions(Modification a_modification)
    {
        if (this.m_positionOne == null)
        {
            return true;
        }
        if (this.m_positionOne.equals(Modification.UNKNOWN_POSITION))
        {
            return true;
        }
        if (a_modification.getPositionOne() == Modification.UNKNOWN_POSITION)
        {
            return true;
        }
        if (a_modification.getPositionOne() == this.m_positionOne)
        {
            // now we need to look for the second position
            if (this.m_positionTwo == null)
            {
                return true;
            }
            if (this.m_positionTwo.equals(Modification.UNKNOWN_POSITION))
            {
                return true;
            }
            if (a_modification.getPositionTwo() == null)
            {
                return false;
            }
            if (a_modification.getPositionTwo() == Modification.UNKNOWN_POSITION)
            {
                return true;
            }
            if (a_modification.getPositionTwo() == this.m_positionTwo)
            {
                return true;
            }
        }
        return false;
    }

    public Integer getPositionOne()
    {
        return m_positionOne;
    }

    public void setPositionOne(Integer a_positionOne)
    {
        m_positionOne = a_positionOne;
    }

    public Integer getPositionTwo()
    {
        return m_positionTwo;
    }

    public void setPositionTwo(Integer a_positionTwo)
    {
        m_positionTwo = a_positionTwo;
    }
}
