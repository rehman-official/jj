package it.polito.po.test;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import schools.Region;
import junit.framework.TestCase;

public class TestR4_Queries extends TestCase {

	Region r ;
	public void setUp() throws IOException{
		r = new Region("Piemonte");		
		r.readData  (TestR3_ReadData.url);
	}

	/*
	 * returns a map having the description as the key
	 * and the number of corresponding schools as the value
	 * 
	 */
	public void testCountSchoolsPerDescription(){
		Map<String,Long> res = r.countSchoolsPerDescription();
		
		assertNotNull("Missing count of schools per description",res);
		
		String liceo = res.keySet().stream().filter(k -> k.matches("LICEO SCIENTIFICO.*")).findFirst().get();
		String primaria = res.keySet().stream().filter(k -> k.matches("SCUOLA PRIMARIA.*")).findFirst().get();
		assertEquals(65, res.get(liceo).longValue());
		assertEquals(1414, res.get(primaria).longValue());
	}

	
	/*
	 * returns a map having the municipality name as the key
	 * and the number of branches in that municipality
	 * as the value. 
	 */
	public void testCountBranchesPerMunicipality(){
		Map<String,Long> res = r.countBranchesPerMunicipality();
		
		assertNotNull("Missing count of branches per municipality",res);
		
		assertEquals(610,res.get("TORINO").longValue());
		assertEquals(69,res.get("ASTI").longValue());
	}

	/*
	 * return a list of strings with the format "### - XXXXXX"
	 * where ### is the number of schools (NOT branches!!)
	 * and XXXXXX is the name of the municipality.
	 * If a school has more than one branch in the municipality
	 * it must be counted only once. 
	 */
	public void testCountSchoolsPerMunicipality(){
		Collection<String> res=r.countSchoolsPerMunicipality();
		
		assertNotNull("Missing count of schools per municipality",res);
		
		Comparator<String> cmp = Comparator.comparing(s -> Integer.parseInt(s.replaceAll("\\s*-.*","")));
		
		String min = res.stream().min(cmp).get();
		String med = res.stream().sorted(cmp).skip(440).findFirst().get();
		String max = res.stream().max(cmp).get();
		
		assertTrue(max.matches("[0-9 -]*TORINO"));
		assertTrue(med.matches("2.*"));
		assertTrue(min.matches("1.*"));
		
	}
	
	/*
	 * return a list of strings with the format "### - XXXXXX"
	 * where ### is the number of schools (NOT branches!!)
	 * and XXXXXX is the name of the community.
	 * If a school has more than one branch a municipality
	 * of the community it must be counted only once.
	 * The strings has to be sorted in descending order
	 * by the number of schools. 
	 */
	public void testCountSchoolsPerCommunity(){
		List<String> res = r.countSchoolsPerCommunity();
		
		assertNotNull("Missing count of schools per community",res);
		
		assertEquals(76,res.size());
		assertTrue(res.get(0).matches("[0-9 -]*BASSA VAL DI SUSA E DELLA VAL CENISCHIA"));
	}
	
	/*
	 * restituisce una mappa contenente come chiave il nome della 
	 * provincia e come valore la media del numero di sedi per comune.
	 * @return
	 */
	public void testAverageBranchesPerMunicipality(){
		Map<String,Double> res = r.averageBranchesPerMunicipality();
		assertNotNull("Missing count of branches per municipality",res);
		assertEquals(7.2,res.get("TORINO"),0.1);
		assertEquals(3.6,res.get("ASTI"),0.1);
		assertEquals(3.9,res.get("ALESSANDRIA"),0.1);
	}


}