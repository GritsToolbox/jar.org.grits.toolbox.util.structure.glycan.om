package org.grits.toolbox.util.structure.glycan.count;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eurocarbdb.MolecularFramework.io.SugarImporterException;
import org.eurocarbdb.MolecularFramework.io.GlycoCT.SugarImporterGlycoCTCondensed;
import org.eurocarbdb.MolecularFramework.sugar.Sugar;
import org.eurocarbdb.MolecularFramework.util.similiarity.SearchEngine.SearchEngineException;
import org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitorException;
import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterException;

public class GlycanCompositionCountOperator {
	
	/**
	 * Uses a searchEngine to count the number of given list of query components in the provided Sugar object. The query components
	 * are searched in the order given and the matched and counted items are removed from the Sugar object to count the next
	 * query item in each iteration. The Sugar object should be completely covered by the list of query components. In other words, 
	 * if there are other components left (in the given Sugar) that didn't match after searching for all the query components, 
	 * the method returns false.
	 * 
	 * @param sugar Sugar to search
	 * @param queryItems list of components (monosaccharides, substituents etc.) to count in the given Sugar object
	 * @param a ordered map of names of the query items and their corresponding counts in the Sugar object (to be filled in)
	 * @return true if the Sugar objects contains only the listed queryItems, 
	 *         false if there are left over components (monosaccharides, substituents etc.) that didn't match with any of the queryItems
	 * @throws GlycanFilterException
	 */
	public static boolean matchAndCountComposition (Sugar sugar, List<SearchQueryItem> queryItems, LinkedHashMap<String, Integer> countsMap) throws GlycanFilterException {
		boolean match = false;
		SugarImporterGlycoCTCondensed importer = new SugarImporterGlycoCTCondensed();
		
		List<Sugar> parsedQueryItems = new ArrayList<>();
		for (SearchQueryItem structure : queryItems) {
			try {
				Sugar queryStr = importer.parse(structure.getSequence());
				parsedQueryItems.add(queryStr);
			} catch (SugarImporterException e) {
				throw new GlycanFilterException(e.getMessage(), e);
			}
		}
		
		CompositionSearchEngine searchEngine = new CompositionSearchEngine();
		try {
			searchEngine.setQueriedStructure(sugar);
			int i=0;
			for (Sugar sugar2 : parsedQueryItems) {
				searchEngine.setQueryStructure(sugar2);
				int count = 0;
				while (true) {
					if (searchEngine.isMatch()) {
						count++;
						searchEngine.removeMatchedFromQueried();
					} else
						break;
				}
				
				countsMap.put(queryItems.get(i).getName(), count);
				i++;
			}
			
			if (searchEngine.getNotMatchedCount() == 0) {  // nothing left, everything is matched
				match = true;
			}
		} catch (GlycoVisitorException | SearchEngineException e) {
			throw new GlycanFilterException(e.getMessage(), e);
		}
		
		return match;
	}
	
	public static void main (String[] args) {
		
		List<SearchQueryItem> queryList = new ArrayList<>();		
		queryList.add(new SearchQueryItem("HexNac", "RES\n"
				+ "1b:x-HEX-1:5\n"
				+ "2s:n-acetyl\n"
				+ "LIN\n"
				+ "1:1d(2+1)2n"));
		queryList.add(new SearchQueryItem("HexN", "RES\n"
				+ "1b:x-HEX-x:x|1:a\n"
				+ "2s:amino\n"
				+ "LIN\n"
				+ "1:1d(2+1)2n"));
		queryList.add(new SearchQueryItem("NeuAC", "RES\n"
				+ "1b:x-dgro-dgal-NON-x:x|1:a|2:keto|3:d\n"
				+ "2s:n-acetyl\n"
				+ "LIN\n"
				+ "1:1d(5+1)2n"));
		queryList.add(new SearchQueryItem("NeuGC", "RES\n"
				+ "1b:x-dgro-dgal-NON-x:x|1:a|2:keto|3:d\n"
				+ "2s:n-glycolyl\n"
				+ "LIN\n"
				+ "1:1d(5+1)2n"));
		queryList.add(new SearchQueryItem("KDN", "RES\n"
				+ "1b:x-dgro-dgal-NON-x:x|1:a|2:keto|3:d"));
		queryList.add(new SearchQueryItem("dHex", "RES\n"
				+ "1b:x-HEX-x:x|6:d"));
		queryList.add(new SearchQueryItem("Hex", "RES\n"
				+ "1b:x-HEX-x:x"));
		queryList.add(new SearchQueryItem("Pent", "RES\n"
				+ "1b:x-PEN-x:x"));
		queryList.add(new SearchQueryItem("Methyl", "RES\n1s:methyl"));
		queryList.add(new SearchQueryItem("GlcA", "RES\n" 
                   +  "1b:x-dglc-HEX-1:5|6:a"));
	    queryList.add(new SearchQueryItem("IdoA", "RES\n1b:x-lido-HEX-1:5|6:a"));
		queryList.add(new SearchQueryItem("Phosphate", "RES\n1s:phosphate"));
		queryList.add(new SearchQueryItem("Sulfate", "RES\n1s:sulfate"));
		
		String sequence = "RES\n" +
				"1b:b-dglc-HEX-1:5\n" +
				"2s:n-acetyl\n" +
				"3b:b-dglc-HEX-1:5\n" +
				"4s:n-acetyl\n" +
				"5b:b-dman-HEX-1:5\n" +
				"6b:a-dman-HEX-1:5\n" +
				"7b:b-dglc-HEX-1:5\n" +
				"8s:n-acetyl\n" +
				"9b:b-dgal-HEX-1:5\n" +
				"10s:n-acetyl\n" +
				"11s:sulfate\n" +
				"12b:a-dman-HEX-1:5\n" +
				"13b:b-dglc-HEX-1:5\n" +
				"14s:n-acetyl\n" +
				"15b:b-dgal-HEX-1:5\n" +
				"LIN\n" +
				"1:1d(2+1)2n\n" +
				"2:1o(4+1)3d\n" +
				"3:3d(2+1)4n\n" +
				"4:3o(4+1)5d\n" +
				"5:5o(3+1)6d\n" +
				"6:6o(2+1)7d\n" +
				"7:7d(2+1)8n\n" +
				"8:7o(4+1)9d\n" +
				"9:9d(2+1)10n\n" +
				"10:9o(4+1)11n\n" +
				"11:5o(6+1)12d\n" +
				"12:12o(2+1)13d\n" +
				"13:13d(2+1)14n\n" +
				"14:13o(4+1)15d\n";
               /* "RES\n" + "1b:x-dglc-HEX-1:5\n" + "2s:n-acetyl\n" + "3b:b-dglc-HEX-1:5\n"
                        + "4s:n-acetyl\n" + "5b:b-dman-HEX-1:5\n" + "6b:a-dman-HEX-1:5\n"
                        + "7b:b-dglc-HEX-1:5\n" + "8s:n-acetyl\n" + "9b:a-dman-HEX-1:5\n" + "LIN\n"
                        + "1:1d(2+1)2n\n" + "2:1o(4+1)3d\n" + "3:3d(2+1)4n\n" + "4:3o(4+1)5d\n"
                        + "5:5o(3+1)6d\n" + "6:5o(4+1)7d\n" + "7:7d(2+1)8n\n" + "8:5o(6+1)9d";*/
		
		SugarImporterGlycoCTCondensed importer = new SugarImporterGlycoCTCondensed();
		try {
			Sugar sugar = importer.parse(sequence);
			LinkedHashMap<String, Integer> counts = new LinkedHashMap<>();
			boolean match = GlycanCompositionCountOperator.matchAndCountComposition(sugar, queryList, counts);
			if (match) {
				for (String queryItem : counts.keySet()) {
					System.out.println(queryItem + "(" + counts.get(queryItem) + ")");
					
				}
			} else System.out.println("Not matched");
		} catch (SugarImporterException | GlycanFilterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
