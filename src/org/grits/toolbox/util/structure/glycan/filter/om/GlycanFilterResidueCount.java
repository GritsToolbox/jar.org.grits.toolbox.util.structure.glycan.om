package org.grits.toolbox.util.structure.glycan.filter.om;

import javax.xml.bind.annotation.XmlRootElement;

import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterException;

@XmlRootElement(name = "residueFilter")
public class GlycanFilterResidueCount extends IntegerFilter {
	
	@Override
	public boolean accept(GlycanFilterVisitor a_operator) throws GlycanFilterException {
		return a_operator.visit(this);
	}

	@Override
	public Filter copy() {
		GlycanFilterResidueCount copy = new GlycanFilterResidueCount();
        copy.setName(name);
        copy.setDescription(description);
        copy.setLabel(label);
        copy.setClassification(classification);
        copy.setMin(m_min);
        copy.setMax(m_max);
        return copy;
	}

}
