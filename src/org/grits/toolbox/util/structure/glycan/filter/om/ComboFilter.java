package org.grits.toolbox.util.structure.glycan.filter.om;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterException;

@XmlRootElement(name = "combinationFilter")
public class ComboFilter extends Filter
{
    private List<Filter> filters;
    private List<FilterOrder> filterOrders;
    private Filter selected = null;
    
    FilterOrderComparator comp = new FilterOrderComparator();

    @Override
    public boolean accept(GlycanFilterVisitor a_operator) throws GlycanFilterException
    {
    	return a_operator.visit(this);
    }

    @XmlElementRef
    public List<Filter> getFilters()
    {
        return filters;
    }
    
    public List<Filter> getFiltersInFilterOrder() {
    	if (filterOrders != null) 
    		Collections.sort((List<Filter>)filters, comp);
    	return filters;
    }

    public void setFilters(List<Filter> filters)
    {
        this.filters = filters;
        Collections.sort((List<Filter>) filters);
    }

    @XmlIDREF
    @XmlAttribute
    public Filter getSelected()
    {
        return selected;
    }

    public void setSelected(Filter selected)
    {
        this.selected = selected;
    }
    
    @Override
    public String toString() {
    	if (selected != null)
    		return selected.toString();
    	return super.toString();
    }

    @Override
    public Filter copy()
    {
        ComboFilter copy = new ComboFilter();
        copy.setName(name);
        copy.setDescription(description);
        copy.setLabel(label);
        copy.setClassification(classification);
        copy.setFilters(filters);
        if (selected != null)
            copy.setSelected(selected.copy());
        copy.setFilterOrders(filterOrders);
        return copy;
    }
    
    @XmlElement(name="order")
	public List<FilterOrder> getFilterOrders() {
		return filterOrders;
	}

	public void setFilterOrders(List<FilterOrder> filterOrders) {
		this.filterOrders = filterOrders;
	}
	
	class FilterOrderComparator implements Comparator<Filter> {
		@Override
		public int compare(Filter o1, Filter o2) {
			return findFilterOrder (o1).compareTo (findFilterOrder(o2));
		}

		private Integer findFilterOrder(Filter o1) {
			if (filterOrders != null) {
				for (FilterOrder order: filterOrders) {
					if (order.getFilterName().equals(o1.getName()))
						return order.getOrder();
				}
			}
			return 0;
		}
	}
}
