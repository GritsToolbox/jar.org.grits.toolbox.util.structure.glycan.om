package org.grits.toolbox.util.structure.glycan.filter.om;

import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterException;

public abstract class BooleanFilter extends Filter {

	@Override
	public abstract boolean accept(GlycanFilterVisitor a_operator) throws GlycanFilterException;

	@Override
	public abstract Filter copy();

}
