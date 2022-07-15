package org.grits.toolbox.util.structure.glycan.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.grits.toolbox.util.structure.glycan.filter.om.ComboFilter;
import org.grits.toolbox.util.structure.glycan.filter.om.Filter;
import org.grits.toolbox.util.structure.glycan.filter.om.FiltersLibrary;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterAnd;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterAntenna;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterGlycanFeature;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterLinkage;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterModification;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterMonosaccharide;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterNot;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterOr;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterResidueCount;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterSubstituent;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterSubstructure;


public class FilterUtils {
	@SuppressWarnings("rawtypes")
	public static Class[] filterClassContext = {GlycanFilterMonosaccharide.class, GlycanFilterSubstituent.class, 
			GlycanFilterSubstructure.class, GlycanFilterModification.class, GlycanFilterGlycanFeature.class,
			GlycanFilterAnd.class, GlycanFilterOr.class, GlycanFilterNot.class, GlycanFilterAntenna.class, ComboFilter.class, 
			GlycanFilterLinkage.class, GlycanFilterResidueCount.class};
        
	/**
	 * load all the filters from the given filters xml file
	 * 
	 * @param filepath should be the full path for the filters xml file
	 * @return the library containing the list of filters
	 * @throws UnsupportedEncodingException
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("rawtypes")
	public static FiltersLibrary readFilters(String filepath) throws UnsupportedEncodingException, JAXBException, FileNotFoundException {
		FileInputStream inputStream = new FileInputStream(filepath);
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
        List<Class> contextList = new ArrayList<>(Arrays.asList(filterClassContext));
        contextList.add(FiltersLibrary.class);
        JAXBContext context = JAXBContext.newInstance( contextList.toArray(new Class[contextList.size()]));
        Unmarshaller unmarshaller = context.createUnmarshaller();
        FiltersLibrary filtersLibrary  = (FiltersLibrary) unmarshaller.unmarshal(reader);
		return filtersLibrary;
	}
	
	public static FiltersLibrary readCustomFilters (String filepath, JAXBContext context) throws UnsupportedEncodingException, JAXBException, FileNotFoundException {
		FileInputStream inputStream = new FileInputStream(filepath);
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
        Unmarshaller unmarshaller = context.createUnmarshaller();
        FiltersLibrary filtersLibrary  = (FiltersLibrary) unmarshaller.unmarshal(reader);
		return filtersLibrary;
	}
	
	/** 
	 * write the filters library back to the given file
	 * increment the last digit of the version number each time it is updated
	 * 
	 * @param library updated filters library
	 * @param filepath the full path of the file for the filter library (xml file)
	 * @param context JAXBContext that contains all the required (filter) classes for the given filter library object
	 * @throws JAXBException in case of errors during xml marshalling
	 * @throws IOException  in case version number cannot be parsed and modified or the file cannot be written into
	 */
	public static void updateLibrary (FiltersLibrary library, String filepath, JAXBContext context) throws JAXBException, IOException {
		String version = library.getVersion();
		String[] versionBits = version.split("\\.");
		if (versionBits != null && versionBits.length == 3) { // major.minor.localChanges
			String localVersion = versionBits[2];
			try {
				Integer vInt = Integer.parseInt(localVersion);
				vInt ++;
				version = versionBits[0] + "." + versionBits[1] + "." + String.valueOf(vInt);
				library.setVersion(version);
			} catch (NumberFormatException e) {
				throw new IOException("Version format is incorrect! Cannot update the version", e);
			}
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING,"UTF-8");
        marshaller.marshal(library, os);

        FileWriter fileWriter = new FileWriter(filepath);
        fileWriter.write(os.toString((String) marshaller.getProperty(Marshaller.JAXB_ENCODING)));
        fileWriter.close();
        os.close();
	}
	
	/**
	 * checks whether we need to copy the filter library from jar to the configuration directory
	 * check the versions (major.minor.localVersion) of two libraries to decide
	 * 	- if major.minor are the same, then there is no change in the original library, no need to copy it over
	 *  - if major.minor is different, then we need to copy it over (need to check further if merge is necessary)
	 *  
	 * @param library original filter library (commonly from the jar file) 
	 * @param localLibrary user's modified local filter library (in configuration folder)
	 * @return true if jar filter library needs to be copied over, false otherwise
	 * @throws Exception
	 */
	public static boolean needToCopyFilterLibraryFromJar (FiltersLibrary library, FiltersLibrary localLibrary) throws Exception {
		String version1 = library.getVersion();
		String[] version1Split = version1.split("\\.");
		String version2 = localLibrary.getVersion();
		String[] version2Split = version2.split("\\.");
		
		if (version2Split.length == 3 && version1Split.length == 3) {
			String majorMinor = version1Split[0] + "." + version1Split[1];
			String localMajorMinor = version2Split[0] + "." + version2Split[1];
			return !majorMinor.equals(localMajorMinor);
		}
		else {
			throw new Exception ("Version numbers are invalid!");
		}
	}
	
	/**
	 * check the versions (major.minor.localVersion) of two libraries and merge them 
	 * 	-  if the localVersion has been changed in the localLibrary, then need to merge
	 * 
	 * get all filters from the library and merge it with the filters in local library,
	 * keep the categories in the local library intact, add any new filters/changes from original library into the local library
	 * 
	 * @param library original filter library (commonly from the jar file) 
	 * @param localLibrary (user's modified local filter library) - this will be updated with the changes from library
	 * 
	 * @return true if the versions necessitated a merge and merge is performed, false if merge is not required (local filter library can we overridden)
	 */
	public static boolean mergeFilterLibraries (FiltersLibrary library, FiltersLibrary localLibrary) throws Exception {
		if (library == null || localLibrary == null)
			return false;
		String version1 = library.getVersion();
		String[] version1Split = version1.split("\\.");
		String version2 = localLibrary.getVersion();
		String[] version2Split = version2.split("\\.");
		
		if (version2Split.length == 3 && version1Split.length == 3) {
			Integer lastDigitLocal = Integer.parseInt(version2Split[2]);
			if (lastDigitLocal > 0) {
				String newVersion = version1Split[0] + "." + version1Split[1] + "." + version2Split[2];
				localLibrary.setVersion(newVersion);
				// need to keep the local category changes
				// do not override category changes
				for(Filter f: library.getFilters()) {
					if (localLibrary.getFilters().contains(f)) { // first remove existing
						int idx = localLibrary.getFilters().indexOf(f);
						localLibrary.getFilters().remove(idx);	
					}
					// add the new filter
					localLibrary.addFilter(f);
				}
				return true;
			}
			else // no need to merge
				return false; // this means we can simply overwrite the local filter file
		}
				
		return false;
	}
}
