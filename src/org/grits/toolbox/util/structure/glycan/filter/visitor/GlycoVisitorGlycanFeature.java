package org.grits.toolbox.util.structure.glycan.filter.visitor;

import org.eurocarbdb.MolecularFramework.sugar.Anomer;
import org.eurocarbdb.MolecularFramework.sugar.BaseType;
import org.eurocarbdb.MolecularFramework.sugar.GlycoEdge;
import org.eurocarbdb.MolecularFramework.sugar.GlycoconjugateException;
import org.eurocarbdb.MolecularFramework.sugar.Linkage;
import org.eurocarbdb.MolecularFramework.sugar.LinkageType;
import org.eurocarbdb.MolecularFramework.sugar.Modification;
import org.eurocarbdb.MolecularFramework.sugar.ModificationType;
import org.eurocarbdb.MolecularFramework.sugar.Monosaccharide;
import org.eurocarbdb.MolecularFramework.sugar.NonMonosaccharide;
import org.eurocarbdb.MolecularFramework.sugar.Substituent;
import org.eurocarbdb.MolecularFramework.sugar.Sugar;
import org.eurocarbdb.MolecularFramework.sugar.SugarUnitAlternative;
import org.eurocarbdb.MolecularFramework.sugar.SugarUnitCyclic;
import org.eurocarbdb.MolecularFramework.sugar.SugarUnitRepeat;
import org.eurocarbdb.MolecularFramework.sugar.Superclass;
import org.eurocarbdb.MolecularFramework.sugar.UnderdeterminedSubTree;
import org.eurocarbdb.MolecularFramework.sugar.UnvalidatedGlycoNode;
import org.eurocarbdb.MolecularFramework.util.traverser.GlycoTraverser;
import org.eurocarbdb.MolecularFramework.util.traverser.GlycoTraverserSimple;
import org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitor;
import org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitorException;

public class GlycoVisitorGlycanFeature implements GlycoVisitor
{
    private boolean m_unknownAnomerAllowed = true;
    private boolean m_unknownRingsizeAllowed = true;
    private boolean m_unknownBasetypeAllowed = true;
    private boolean m_residueSugAllowed = true;
    private boolean m_unknownModificationPositionAllowed = true;
    private boolean m_unknownLinkageTypeAllowed = true;
    private boolean m_unknownLinkagePositionAllowed = true;
    private boolean m_unitUndAllowed = true;
    private boolean m_unknownRepeatAllowed = true;
    private boolean m_unitRepeatAllowed = true;
    private boolean m_unconnectedTreeAllowed = true;
    private boolean m_unitCyclicAllowed = true;
    private boolean m_unitProbableAllowed = true;
    private boolean m_reducingAlditolAllowed = true;
    private boolean m_unknownConfigurationAllowed = true;

    private boolean m_valid = true;

    public boolean isUnknownAnomerAllowed()
    {
        return m_unknownAnomerAllowed;
    }

    public void setUnknownAnomerAllowed(boolean a_unknownAnomerAllowed)
    {
        m_unknownAnomerAllowed = a_unknownAnomerAllowed;
    }

    public boolean isUnknownRingsizeAllowed()
    {
        return m_unknownRingsizeAllowed;
    }

    public void setUnknownRingsizeAllowed(boolean a_unknownRingsizeAllowed)
    {
        m_unknownRingsizeAllowed = a_unknownRingsizeAllowed;
    }

    public boolean isUnknownBasetypeAllowed()
    {
        return m_unknownBasetypeAllowed;
    }

    public void setUnknownBasetypeAllowed(boolean a_unknownBasetypeAllowed)
    {
        m_unknownBasetypeAllowed = a_unknownBasetypeAllowed;
    }

    public boolean isResidueSugAllowed()
    {
        return m_residueSugAllowed;
    }

    public void setResidueSugAllowed(boolean a_residueSugAllowed)
    {
        m_residueSugAllowed = a_residueSugAllowed;
    }

    public boolean isUnknownModificationPositionAllowed()
    {
        return m_unknownModificationPositionAllowed;
    }

    public void setUnknownModificationPositionAllowed(boolean a_unknownModificationPositionAllowed)
    {
        m_unknownModificationPositionAllowed = a_unknownModificationPositionAllowed;
    }

    public boolean isUnknownLinkageTypeAllowed()
    {
        return m_unknownLinkageTypeAllowed;
    }

    public void setUnknownLinkageTypeAllowed(boolean a_unknownLinkageTypeAllowed)
    {
        m_unknownLinkageTypeAllowed = a_unknownLinkageTypeAllowed;
    }

    public boolean isUnknownLinkagePositionAllowed()
    {
        return m_unknownLinkagePositionAllowed;
    }

    public void setUnknownLinkagePositionAllowed(boolean a_unknownLinkagePositionAllowed)
    {
        m_unknownLinkagePositionAllowed = a_unknownLinkagePositionAllowed;
    }

    public boolean isUnitUndAllowed()
    {
        return m_unitUndAllowed;
    }

    public void setUnitUndAllowed(boolean a_unitUndAllowed)
    {
        m_unitUndAllowed = a_unitUndAllowed;
    }

    public boolean isUnknownRepeatAllowed()
    {
        return m_unknownRepeatAllowed;
    }

    public void setUnknownRepeatAllowed(boolean a_unknownRepeatAllowed)
    {
        m_unknownRepeatAllowed = a_unknownRepeatAllowed;
    }

    public boolean isUnitRepeatAllowed()
    {
        return m_unitRepeatAllowed;
    }

    public void setUnitRepeatAllowed(boolean a_unitRepeatAllowed)
    {
        m_unitRepeatAllowed = a_unitRepeatAllowed;
    }

    public boolean isUnconnectedTreeAllowed()
    {
        return m_unconnectedTreeAllowed;
    }

    public void setUnconnectedTreeAllowed(boolean a_unconnectedTreeAllowed)
    {
        m_unconnectedTreeAllowed = a_unconnectedTreeAllowed;
    }

    public boolean isUnitCyclicAllowed()
    {
        return m_unitCyclicAllowed;
    }

    public void setUnitCyclicAllowed(boolean a_unitCyclicAllowed)
    {
        m_unitCyclicAllowed = a_unitCyclicAllowed;
    }

    public boolean isUnitProbableAllowed()
    {
        return m_unitProbableAllowed;
    }

    public void setUnitProbableAllowed(boolean a_unitProbableAllowed)
    {
        m_unitProbableAllowed = a_unitProbableAllowed;
    }

    public boolean isReducingAlditolAllowed()
    {
        return m_reducingAlditolAllowed;
    }

    public void setReducingAlditolAllowed(boolean a_reducingAlditolAllowed)
    {
        m_reducingAlditolAllowed = a_reducingAlditolAllowed;
    }

    public boolean isUnknownConfigurationAllowed()
    {
        return m_unknownConfigurationAllowed;
    }

    public void setUnknownConfigurationAllowed(boolean a_unknownConfigurationAllowed)
    {
        m_unknownConfigurationAllowed = a_unknownConfigurationAllowed;
    }

    public void clear()
    {
        this.m_valid = true;
    }

    @Override
    public GlycoTraverser getTraverser(GlycoVisitor a_visitor) throws GlycoVisitorException
    {
        return new GlycoTraverserSimple(a_visitor);
    }

    public void start(Sugar a_objSugar) throws GlycoVisitorException
    {
        this.clear();
        try
        {
            if (a_objSugar.getRootNodes().size() > 1 && !this.m_unconnectedTreeAllowed)
            {
                this.m_valid = false;
            }
            GlycoTraverser g = this.getTraverser(this);
            g.traverseGraph(a_objSugar);
            for (UnderdeterminedSubTree t : a_objSugar.getUndeterminedSubTrees())
            {
                if (!this.m_unitUndAllowed)
                {
                    this.m_valid = false;
                }
                else
                {
                    g = this.getTraverser(this);
                    if (t.getRootNodes().size() > 1 && !this.m_unconnectedTreeAllowed)
                    {
                        this.m_valid = false;
                    }
                    g.traverseGraph(t);
                    this.visit(t.getConnection());
                    if (t.getProbabilityLower() < 100.0 && !this.m_unitProbableAllowed)
                    {
                        this.m_valid = false;
                    }
                }
            }
        }
        catch (GlycoconjugateException e)
        {
            throw new GlycoVisitorException(e.getMessage(), e);
        }
    }

    public void visit(Monosaccharide a_objMonosaccharid) throws GlycoVisitorException
    {
        if (a_objMonosaccharid.getAnomer() == Anomer.Unknown && !this.m_unknownAnomerAllowed)
        {
            this.m_valid = false;
        }

        if ((a_objMonosaccharid.getRingEnd() == Monosaccharide.UNKNOWN_RING
                || a_objMonosaccharid.getRingStart() == Monosaccharide.UNKNOWN_RING)
                && !this.m_unknownRingsizeAllowed)
        {
            this.m_valid = false;
        }

        if (a_objMonosaccharid.getSuperclass() == Superclass.SUG && !this.m_residueSugAllowed)
        {
            this.m_valid = false;
        }

        for (BaseType b : a_objMonosaccharid.getBaseType())
        {
            if (b.absoluteConfigurationUnknown() && !this.m_unknownConfigurationAllowed)
            {
                this.m_valid = false;
            }
        }
        if (a_objMonosaccharid.getBaseType().size() == 0 && !this.m_unknownBasetypeAllowed)
        {
            this.m_valid = false;
        }
        boolean t_bAlditol = false;
        for (Modification m : a_objMonosaccharid.getModification())
        {

            if (m.hasPositionOne() && m.getPositionOne() == Modification.UNKNOWN_POSITION
                    && !this.m_unknownModificationPositionAllowed)
            {
                this.m_valid = false;
            }
            if (m.hasPositionTwo() && m.getPositionTwo() == Modification.UNKNOWN_POSITION
                    && !this.m_unknownModificationPositionAllowed)
            {
                this.m_valid = false;
            }
            if (m.getModificationType().equals(ModificationType.ALDI))
            {
                t_bAlditol = true;
            }
        }
        if (t_bAlditol && a_objMonosaccharid.getParentEdge() == null
                && !this.m_reducingAlditolAllowed)
        {
            this.m_valid = false;
        }
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
    public void visit(NonMonosaccharide a_arg0) throws GlycoVisitorException
    {
        throw new GlycoVisitorException("NonMonosaccharide is not supported.");
    }

    public void visit(SugarUnitRepeat a_objRepeat) throws GlycoVisitorException
    {
        if (!this.m_unitRepeatAllowed)
        {
            this.m_valid = false;
        }
        this.visit(a_objRepeat.getRepeatLinkage());
        if ((a_objRepeat.getMaxRepeatCount() == SugarUnitRepeat.UNKNOWN
                || a_objRepeat.getMinRepeatCount() == SugarUnitRepeat.UNKNOWN)
                && !this.m_unknownRepeatAllowed)
        {
            this.m_valid = false;
        }
        if (a_objRepeat.getMinRepeatCount() != a_objRepeat.getMaxRepeatCount()
                && !this.m_unknownRepeatAllowed)
        {
            this.m_valid = false;
        }
        try
        {
            if (a_objRepeat.getRootNodes().size() > 1 && !this.m_unconnectedTreeAllowed)
            {
                this.m_valid = false;
            }
            GlycoTraverser g = this.getTraverser(this);
            g.traverseGraph(a_objRepeat);
            for (UnderdeterminedSubTree t : a_objRepeat.getUndeterminedSubTrees())
            {
                if (!this.m_unitUndAllowed)
                {
                    this.m_valid = false;
                }
                else
                {
                    g = this.getTraverser(this);
                    if (t.getRootNodes().size() > 1 && !this.m_unconnectedTreeAllowed)
                    {
                        this.m_valid = false;
                    }
                    g.traverseGraph(t);
                    this.visit(t.getConnection());
                    if (t.getProbabilityLower() < 100.0 && !this.m_unitProbableAllowed)
                    {
                        this.m_valid = false;
                    }
                }
            }
        }
        catch (GlycoconjugateException e)
        {
            throw new GlycoVisitorException(e.getMessage(), e);
        }
    }

    public void visit(Substituent a_objSubstituent) throws GlycoVisitorException
    {
        // nothing to do
    }

    public void visit(SugarUnitCyclic a_objCyclic) throws GlycoVisitorException
    {
        if (!this.m_unitCyclicAllowed)
        {
            this.m_valid = false;
        }
    }

    public void visit(GlycoEdge a_objLinkage) throws GlycoVisitorException
    {
        for (Linkage lin : a_objLinkage.getGlycosidicLinkages())
        {
            if (lin.getParentLinkageType() == LinkageType.UNKNOWN
                    && !this.m_unknownLinkageTypeAllowed)
            {
                this.m_valid = false;
            }
            if (lin.getChildLinkageType() == LinkageType.UNKNOWN
                    && !this.m_unknownLinkageTypeAllowed)
            {
                this.m_valid = false;
            }
            if (lin.getParentLinkages().size() > 1 && !this.m_unknownLinkagePositionAllowed)
            {
                this.m_valid = false;
            }
            if (lin.getChildLinkages().size() > 1 && !this.m_unknownLinkagePositionAllowed)
            {
                this.m_valid = false;
            }

            for (Integer t_i : lin.getParentLinkages())
            {
                if (t_i == Linkage.UNKNOWN_POSITION && !this.m_unknownLinkagePositionAllowed)
                {
                    this.m_valid = false;
                }
            }

            for (Integer t_i : lin.getChildLinkages())
            {
                if (t_i == Linkage.UNKNOWN_POSITION && !this.m_unknownLinkagePositionAllowed)
                {
                    this.m_valid = false;
                }
            }
        }
    }

    public boolean isValid()
    {
        return m_valid;
    }
}
