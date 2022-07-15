package org.grits.toolbox.util.structure.glycan.filter.om;

import java.util.ArrayList;
import java.util.List;

import org.eurocarbdb.MolecularFramework.sugar.ModificationType;
import org.eurocarbdb.MolecularFramework.sugar.SubstituentType;
import org.eurocarbdb.MolecularFramework.sugar.Sugar;
import org.eurocarbdb.MolecularFramework.util.similiarity.SearchEngine.SearchEngine;
import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterException;
import org.grits.toolbox.util.structure.glycan.filter.visitor.GlycoVisitorCountAntenna;
import org.grits.toolbox.util.structure.glycan.filter.visitor.GlycoVisitorCountModification;
import org.grits.toolbox.util.structure.glycan.filter.visitor.GlycoVisitorCountMonosaccharide;
import org.grits.toolbox.util.structure.glycan.filter.visitor.GlycoVisitorCountResidue;
import org.grits.toolbox.util.structure.glycan.filter.visitor.GlycoVisitorCountSubstituent;
import org.grits.toolbox.util.structure.glycan.filter.visitor.GlycoVisitorGlycanFeature;
import org.grits.toolbox.util.structure.glycan.filter.visitor.GlycoVisitorLinakgePattern;
import org.grits.toolbox.util.structure.glycan.filter.visitor.MonosaccharidePattern;

public class GlycanFilterVisitor
{
    protected Sugar m_sugar = null;
    protected Filter m_filter = null;

    public Filter getFilter()
    {
        return m_filter;
    }

    public void setFilter(Filter a_filter)
    {
        m_filter = a_filter;
    }

    public boolean evaluate(Sugar a_sugar) throws GlycanFilterException
    {
        this.m_sugar = a_sugar;
        if (this.m_filter == null)
        {
            throw new GlycanFilterException("No filter is set");
        }
        if (a_sugar == null) {
        	// no sugar object, cannot test the filter
        	return false;
        }
        return this.m_filter.accept(this);
    }

    public boolean visit(GlycanFilterAnd a_filterAnd) throws GlycanFilterException
    {
        for (Filter t_filter : a_filterAnd.getElements())
        {
            if (!t_filter.accept(this))
            {
                return false;
            }
        }
        return true;
    }

    public boolean visit(GlycanFilterNot a_filterNot) throws GlycanFilterException
    {
        return !a_filterNot.getFilter().accept(this);
    }

    public boolean visit(GlycanFilterOr a_filterOr) throws GlycanFilterException
    {
        for (Filter t_filter : a_filterOr.getElements())
        {
            if (t_filter.accept(this))
            {
                return true;
            }
        }
        return false;
    }

    public boolean visit(GlycanFilterSubstructure a_glycanFilterSubstructure)
            throws GlycanFilterException
    {
        try
        {
            for (FilterSequence t_sequence : a_glycanFilterSubstructure.getSubstructure())
            {
                SearchEngine search = new SearchEngine();
                if (t_sequence.getReducingEnd())
                {
                    search.restrictToReducingEnds();
                }
                search.setQueryStructure(t_sequence.getSubStructure());
                search.setQueriedStructure(this.m_sugar);
                search.match();
                if (search.isExactMatch())
                {
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            throw new GlycanFilterException(
                    "Unable to apply substructure filter: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean visit(GlycanFilterSubstituent a_glycanFilterSubstituent)
            throws GlycanFilterException
    {
        try
        {
            GlycoVisitorCountSubstituent t_visitor = new GlycoVisitorCountSubstituent();
            t_visitor.setSubstituent(
                    SubstituentType.forName(a_glycanFilterSubstituent.getSubstituent()));
            t_visitor.start(this.m_sugar);
            return this.evaluateNumericalFilter(a_glycanFilterSubstituent, t_visitor.getCount());
        }
        catch (Exception e)
        {
            throw new GlycanFilterException("Unable to apply substituent filter: " + e.getMessage(),
                    e);
        }
    }

    public boolean visit(GlycanFilterMonosaccharide a_glycanFilterMonosaccharide)
            throws GlycanFilterException
    {
        try
        {
            GlycoVisitorCountMonosaccharide t_visitor = new GlycoVisitorCountMonosaccharide();

            MonosaccharidePattern t_pattern = MonosaccharidePattern
                    .fromMonosaccharideDefintion(a_glycanFilterMonosaccharide.getMonosaccharide());
            t_visitor.setMonosaccharidePattern(t_pattern);

            t_visitor.setTerminalOnly(a_glycanFilterMonosaccharide.getTerminalOnly());
            if(this.m_sugar != null){
            	t_visitor.start(this.m_sugar);
            	return this.evaluateNumericalFilter(a_glycanFilterMonosaccharide, t_visitor.getCount());
            }
            return false;
        }
        catch (Exception e)
        {
            throw new GlycanFilterException(
                    "Unable to apply monosaccharide filter: " + e.getMessage(), e);
        }
    }
    
    public boolean visit(GlycanFilterResidueCount a_glycanFilterResidue)
            throws GlycanFilterException
    {
        try
        {
            GlycoVisitorCountResidue t_visitor = new GlycoVisitorCountResidue();
            t_visitor.start(this.m_sugar);
            
            return this.evaluateNumericalFilter(a_glycanFilterResidue, t_visitor.getBaseTypeCount());
           
        }
        catch (Exception e)
        {
            throw new GlycanFilterException(
                    "Unable to apply monosaccharide filter: " + e.getMessage(), e);
        }
    }

    public boolean visit(GlycanFilterModification a_glycanFilterModification)
            throws GlycanFilterException
    {
        try
        {
            GlycoVisitorCountModification t_visitor = new GlycoVisitorCountModification();
            t_visitor.setModification(
                    ModificationType.forName(a_glycanFilterModification.getModification()));
            t_visitor.setPositionOne(a_glycanFilterModification.getPositionOne());
            t_visitor.setPositionTwo(a_glycanFilterModification.getPositionTwo());
            t_visitor.start(this.m_sugar);
            return this.evaluateNumericalFilter(a_glycanFilterModification, t_visitor.getCount());
        }
        catch (Exception e)
        {
            throw new GlycanFilterException(
                    "Unable to apply modification filter: " + e.getMessage(), e);
        }
    }

    public boolean visit(GlycanFilterAntenna a_glycanFilterAntenna) throws GlycanFilterException
    {
        try
        {
            GlycoVisitorCountAntenna t_visitor = new GlycoVisitorCountAntenna();
            t_visitor.setFilterBisection(a_glycanFilterAntenna.isFilterBisection());
            List<MonosaccharidePattern> t_patterns = new ArrayList<>();
            for (MonosaccharideDefintion t_definitionMS : a_glycanFilterAntenna
                    .getExcludeMonosaccharide())
            {
                t_patterns.add(MonosaccharidePattern.fromMonosaccharideDefintion(t_definitionMS));
            }
            t_visitor.setExcludeMS(t_patterns);
            t_visitor.start(this.m_sugar);
            return this.evaluateNumericalFilter(a_glycanFilterAntenna, t_visitor.getCount());
        }
        catch (Exception e)
        {
            throw new GlycanFilterException(
                    "Unable to apply monosaccharide filter: " + e.getMessage(), e);
        }
    }

    public boolean visit(GlycanFilterLinkage a_glycanFilterLinkage) throws GlycanFilterException
    {
        try
        {
            GlycoVisitorLinakgePattern t_visitor = new GlycoVisitorLinakgePattern();
            t_visitor.setMonosaccharidePattern(MonosaccharidePattern
                    .fromMonosaccharideDefintion(a_glycanFilterLinkage.getMonosaccharide()));
            t_visitor.linkagePattern(a_glycanFilterLinkage.getChosenLinkagePattern());
            t_visitor.setTerminal(a_glycanFilterLinkage.isTerminal());
            t_visitor.start(this.m_sugar);
            return t_visitor.isFoundPattern();
        }
        catch (Exception e)
        {
            throw new GlycanFilterException(
                    "Unable to apply linkage pattern filter: " + e.getMessage(), e);
        }
    }

    protected boolean evaluateNumericalFilter(IntegerFilter a_filter, Integer a_number)
    {
        if (a_filter.getMin() != null)
        {
            if (a_filter.getMin() > a_number)
            {
                return false;
            }
        }
        if (a_filter.getMax() != null)
        {
            if (a_filter.getMax() < a_number)
            {
                return false;
            }
        }
        return true;
    }

    public boolean visit(GlycanFilterGlycanFeature a_glycanFilterGlycanFeature)
            throws GlycanFilterException
    {
        try
        {
            GlycoVisitorGlycanFeature t_visitor = new GlycoVisitorGlycanFeature();
            t_visitor.setUnknownAnomerAllowed(a_glycanFilterGlycanFeature.isUnknownAnomerAllowed());
            t_visitor.setUnknownRingsizeAllowed(
                    a_glycanFilterGlycanFeature.isUnknownRingsizeAllowed());
            t_visitor.setUnknownBasetypeAllowed(
                    a_glycanFilterGlycanFeature.isUnknownBasetypeAllowed());
            t_visitor.setResidueSugAllowed(a_glycanFilterGlycanFeature.isResidueSugAllowed());
            t_visitor.setUnknownModificationPositionAllowed(
                    a_glycanFilterGlycanFeature.isUnknownModificationPositionAllowed());
            t_visitor.setUnknownLinkageTypeAllowed(
                    a_glycanFilterGlycanFeature.isUnknownLinkageTypeAllowed());
            t_visitor.setUnknownLinkagePositionAllowed(
                    a_glycanFilterGlycanFeature.isUnknownLinkagePositionAllowed());
            t_visitor.setUnitUndAllowed(a_glycanFilterGlycanFeature.isUnitUndAllowed());
            t_visitor.setUnknownRepeatAllowed(a_glycanFilterGlycanFeature.isUnknownRepeatAllowed());
            t_visitor.setUnitRepeatAllowed(a_glycanFilterGlycanFeature.isUnitRepeatAllowed());
            t_visitor.setUnconnectedTreeAllowed(
                    a_glycanFilterGlycanFeature.isUnconnectedTreeAllowed());
            t_visitor.setUnitCyclicAllowed(a_glycanFilterGlycanFeature.isUnitCyclicAllowed());
            t_visitor.setUnitProbableAllowed(a_glycanFilterGlycanFeature.isUnitProbableAllowed());
            t_visitor.setReducingAlditolAllowed(
                    a_glycanFilterGlycanFeature.isReducingAlditolAllowed());
            t_visitor.setUnknownConfigurationAllowed(
                    a_glycanFilterGlycanFeature.isUnknownConfigurationAllowed());
            t_visitor.start(this.m_sugar);
            return t_visitor.isValid();
        }
        catch (Exception e)
        {
            throw new GlycanFilterException("Unable to apply feature filter: " + e.getMessage(), e);
        }
    }

	public boolean visit(ComboFilter comboFilter) throws GlycanFilterException {
		if (comboFilter.getSelected() != null) 
			return comboFilter.getSelected().accept(this);
		throw new GlycanFilterException("There is no selected filter. Cannot evaluate!");
		
	}
}
