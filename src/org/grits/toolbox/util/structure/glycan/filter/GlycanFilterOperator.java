package org.grits.toolbox.util.structure.glycan.filter;

import java.util.ArrayList;
import java.util.List;

import org.eurocarbdb.MolecularFramework.sugar.ModificationType;
import org.eurocarbdb.MolecularFramework.sugar.SubstituentType;
import org.eurocarbdb.MolecularFramework.sugar.Sugar;
import org.eurocarbdb.MolecularFramework.util.similiarity.SearchEngine.SearchEngine;
import org.grits.toolbox.util.structure.glycan.filter.om.Filter;
import org.grits.toolbox.util.structure.glycan.filter.om.FilterSequence;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterAntenna;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterGlycanFeature;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterLinkage;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterModification;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterMonosaccharide;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterSubstituent;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterSubstructure;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterVisitor;
import org.grits.toolbox.util.structure.glycan.filter.om.MonosaccharideDefintion;
import org.grits.toolbox.util.structure.glycan.filter.visitor.GlycoVisitorCountAntenna;
import org.grits.toolbox.util.structure.glycan.filter.visitor.GlycoVisitorCountModification;
import org.grits.toolbox.util.structure.glycan.filter.visitor.GlycoVisitorCountMonosaccharide;
import org.grits.toolbox.util.structure.glycan.filter.visitor.GlycoVisitorCountSubstituent;
import org.grits.toolbox.util.structure.glycan.filter.visitor.GlycoVisitorGlycanFeature;
import org.grits.toolbox.util.structure.glycan.filter.visitor.GlycoVisitorLinakgePattern;
import org.grits.toolbox.util.structure.glycan.filter.visitor.MonosaccharidePattern;

public class GlycanFilterOperator
{
    public static boolean evaluate(Sugar a_sugar, Filter a_filter)
            throws GlycanFilterException
    {
        GlycanFilterVisitor t_visitor = new GlycanFilterVisitor();
        t_visitor.setFilter(a_filter);
        return t_visitor.evaluate(a_sugar);
    }

    public static Integer applyMonosaccharideFilter(Sugar a_sugar,
            GlycanFilterMonosaccharide a_filter) throws GlycanFilterException
    {
        try
        {
            GlycoVisitorCountMonosaccharide t_visitor = new GlycoVisitorCountMonosaccharide();
            MonosaccharidePattern t_pattern = MonosaccharidePattern
                    .fromMonosaccharideDefintion(a_filter.getMonosaccharide());
            t_visitor.setMonosaccharidePattern(t_pattern);
            t_visitor.setTerminalOnly(a_filter.getTerminalOnly());
            t_visitor.start(a_sugar);
            return t_visitor.getCount();
        }
        catch (Exception e)
        {
            throw new GlycanFilterException(e.getMessage(), e);
        }
    }

    public static Integer applySubstituentFilter(Sugar a_sugar, GlycanFilterSubstituent a_filter)
            throws GlycanFilterException
    {
        try
        {
            GlycoVisitorCountSubstituent t_visitor = new GlycoVisitorCountSubstituent();
            t_visitor.setSubstituent(SubstituentType.forName(a_filter.getSubstituent()));
            t_visitor.start(a_sugar);
            return t_visitor.getCount();
        }
        catch (Exception e)
        {
            throw new GlycanFilterException(e.getMessage(), e);
        }
    }

    public static Boolean applySubstructureFilter(Sugar a_sugar, GlycanFilterSubstructure a_filter)
            throws GlycanFilterException
    {
        try
        {
            for (FilterSequence t_sequence : a_filter.getSubstructure())
            {
                SearchEngine search = new SearchEngine();
                if (t_sequence.getReducingEnd())
                {
                    search.restrictToReducingEnds();
                }
                search.setQueryStructure(t_sequence.getSubStructure());
                search.setQueriedStructure(a_sugar);
                search.match();
                if (search.isExactMatch())
                {
                    return true;
                }
            }
            return false;
        }
        catch (Exception e)
        {
            throw new GlycanFilterException(e.getMessage(), e);
        }
    }

    public static Integer applyModificationFilter(Sugar a_sugar, GlycanFilterModification a_filter)
            throws GlycanFilterException
    {
        try
        {
            GlycoVisitorCountModification t_visitor = new GlycoVisitorCountModification();
            t_visitor.setModification(ModificationType.forName(a_filter.getModification()));
            t_visitor.setPositionOne(a_filter.getPositionOne());
            t_visitor.setPositionTwo(a_filter.getPositionTwo());
            t_visitor.start(a_sugar);
            return t_visitor.getCount();
        }
        catch (Exception e)
        {
            throw new GlycanFilterException(e.getMessage(), e);
        }
    }

    public static Integer applyAntennaFilter(Sugar a_sugar, GlycanFilterAntenna a_filter)
            throws GlycanFilterException
    {
        try
        {
            GlycoVisitorCountAntenna t_visitor = new GlycoVisitorCountAntenna();
            t_visitor.setFilterBisection(a_filter.isFilterBisection());
            List<MonosaccharidePattern> t_patterns = new ArrayList<>();
            for (MonosaccharideDefintion t_definitionMS : a_filter.getExcludeMonosaccharide())
            {
                t_patterns.add(MonosaccharidePattern.fromMonosaccharideDefintion(t_definitionMS));
            }
            t_visitor.setExcludeMS(t_patterns);
            t_visitor.start(a_sugar);
            return t_visitor.getCount();
        }
        catch (Exception e)
        {
            throw new GlycanFilterException(e.getMessage(), e);
        }
    }

    public static Boolean applyLinkagePatternFilter(Sugar a_sugar, GlycanFilterLinkage a_filter)
            throws GlycanFilterException
    {
        try
        {
            GlycoVisitorLinakgePattern t_visitor = new GlycoVisitorLinakgePattern();
            t_visitor.setMonosaccharidePattern(MonosaccharidePattern
                    .fromMonosaccharideDefintion(a_filter.getMonosaccharide()));
            t_visitor.linkagePattern(a_filter.getChosenLinkagePattern());
            t_visitor.setTerminal(a_filter.isTerminal());
            t_visitor.start(a_sugar);
            return t_visitor.isFoundPattern();
        }
        catch (Exception e)
        {
            throw new GlycanFilterException(e.getMessage(), e);
        }
    }

    public static Boolean applyGlycanFeatureFilter(Sugar a_sugar,
            GlycanFilterGlycanFeature a_glycanFilterGlycanFeature) throws GlycanFilterException
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
            t_visitor.start(a_sugar);
            return t_visitor.isValid();
        }
        catch (Exception e)
        {
            throw new GlycanFilterException("Unable to apply feature filter: " + e.getMessage(), e);
        }
    }
}
