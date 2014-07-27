package calc;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import api.AdministrationInstance;
import api.DrugPair;
import api.SingleDrugAdministration;

public class DrugPairCalculatorTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DrugPairCalculatorTest.class);

	private static final LocalDate DATE_1 = new LocalDate(2012, 12, 31);
	private static final LocalDate DATE_2 = new LocalDate(2013, 1, 1);

	@Test
	public void testFindAllDrugPairsNone() {
		LOGGER.info("================ testFindAllDrugPairsNone ================");
		Set<String> drugs = new HashSet<String>();
		Set<DrugPair> actual = DrugPairCalculator.findAllDrugPairs(drugs);
		Assert.assertTrue(actual.isEmpty());
	}

	@Test
	public void testFindAllDrugPairsOne() {
		LOGGER.info("================ testFindAllDrugPairsOne ================");
		Set<String> drugs = new HashSet<String>();
		drugs.add("A");
		Set<DrugPair> actual = DrugPairCalculator.findAllDrugPairs(drugs);
		Assert.assertTrue(actual.isEmpty());
	}

	@Test
	public void testFindAllDrugPairsTwo() {
		LOGGER.info("================ testFindAllDrugPairsTwo ================");
		Set<String> drugs = new HashSet<String>();
		drugs.add("A");
		drugs.add("B");
		Set<DrugPair> actual = DrugPairCalculator.findAllDrugPairs(drugs);
		Set<DrugPair> expected = Collections.singleton(new DrugPair("A", "B"));
		Assert.assertTrue(actual.containsAll(expected));
		Assert.assertTrue(expected.containsAll(actual));
		Set<DrugPair> expectedReverse = Collections.singleton(new DrugPair("B",
				"A"));
		Assert.assertTrue(actual.containsAll(expectedReverse));
		Assert.assertTrue(expectedReverse.containsAll(actual));
	}

	@Test
	public void testFindAllDrugPairsThree() {
		LOGGER.info("================ testFindAllDrugPairsThree ================");
		Set<String> drugs = new HashSet<String>();
		drugs.add("A");
		drugs.add("B");
		drugs.add("C");
		Set<DrugPair> actual = DrugPairCalculator.findAllDrugPairs(drugs);
		Set<DrugPair> expected = new HashSet<DrugPair>();
		expected.add(new DrugPair("A", "B"));
		expected.add(new DrugPair("B", "C"));
		expected.add(new DrugPair("A", "C"));
		Assert.assertTrue(actual.containsAll(expected));
		Assert.assertTrue(expected.containsAll(actual));
	}

	@Test
	public void testGetMapOfDrugPairtoOccurrenceSimple() {
		LOGGER.info("================ testGetMapOfDrugPairtoOccurrenceSimple ================");
		Map<AdministrationInstance, Set<String>> drugAdminMap = new HashMap<AdministrationInstance, Set<String>>();
		drugAdminMap.put(new AdministrationInstance("123", DATE_1),
				Collections.singleton("A"));
		Set<String> drugList = new HashSet<String>();
		drugList.add("B");
		drugList.add("A");
		drugAdminMap.put(new AdministrationInstance("123", DATE_2), drugList);
		Map<DrugPair, Integer> actual = DrugPairCalculator
				.getMapOfDrugPairToOccurrence(drugAdminMap);
		Map<DrugPair, Integer> expected = Maps.newHashMap();
		expected.put(new DrugPair("A", "B"), 1);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGetMapOfDrugPairtoOccurrenceComplex() {
		LOGGER.info("================ testGetMapOfDrugPairtoOccurrenceComplex ================");
		Map<AdministrationInstance, Set<String>> drugAdminMap = new HashMap<AdministrationInstance, Set<String>>();
		drugAdminMap.put(new AdministrationInstance("123", DATE_1),
				Collections.singleton("A"));
		Set<String> drugList1 = new HashSet<String>();
		drugList1.add("A");
		drugAdminMap.put(new AdministrationInstance("456", DATE_2), drugList1);
		Set<String> drugList2 = new HashSet<String>();
		drugList2.add("B");
		drugList2.add("A");
		drugAdminMap.put(new AdministrationInstance("123", DATE_1), drugList2);
		Set<String> drugList3 = new HashSet<String>();
		drugList3.add("B");
		drugList3.add("C");
		drugList3.add("A");
		drugAdminMap.put(new AdministrationInstance("789", DATE_2), drugList3);
		Map<DrugPair, Integer> actual = DrugPairCalculator
				.getMapOfDrugPairToOccurrence(drugAdminMap);
		Map<DrugPair, Integer> expected = Maps.newHashMap();
		expected.put(new DrugPair("A", "B"), 2);
		expected.put(new DrugPair("B", "C"), 1);
		expected.put(new DrugPair("A", "C"), 1);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGetMapOfAdminInstanceToDrugsAdministered() {
		LOGGER.info("================ testGetMapOfAdminInstanceToDrugsAdministered ================");
		List<SingleDrugAdministration> drugAdministrations = new LinkedList<SingleDrugAdministration>();
		drugAdministrations.add(new SingleDrugAdministration(
				new AdministrationInstance("123", DATE_1), "A"));
		drugAdministrations.add(new SingleDrugAdministration(
				new AdministrationInstance("123", DATE_1), "B"));
		drugAdministrations.add(new SingleDrugAdministration(
				new AdministrationInstance("123", DATE_2), "A"));
		drugAdministrations.add(new SingleDrugAdministration(
				new AdministrationInstance("123", DATE_2), "B"));
		drugAdministrations.add(new SingleDrugAdministration(
				new AdministrationInstance("456", DATE_1), "A"));
		drugAdministrations.add(new SingleDrugAdministration(
				new AdministrationInstance("456", DATE_2), "A"));
		drugAdministrations.add(new SingleDrugAdministration(
				new AdministrationInstance("456", DATE_2), "C"));
		Map<AdministrationInstance, Set<String>> actual = DrugPairCalculator
				.getMapOfAdminInstanceToDrugsAdministered(drugAdministrations);
		Map<AdministrationInstance, Set<String>> expected = new HashMap<AdministrationInstance, Set<String>>();
		Set<String> abDrugCombo = new HashSet<String>();
		abDrugCombo.add("A");
		abDrugCombo.add("B");
		Set<String> aDrugCombo = new HashSet<String>();
		aDrugCombo.add("A");
		Set<String> acDrugCombo = new HashSet<String>();
		acDrugCombo.add("A");
		acDrugCombo.add("C");
		expected.put(new AdministrationInstance("123", DATE_1), abDrugCombo);
		expected.put(new AdministrationInstance("123", DATE_2), abDrugCombo);
		expected.put(new AdministrationInstance("456", DATE_1), aDrugCombo);
		expected.put(new AdministrationInstance("456", DATE_2), acDrugCombo);
		Assert.assertEquals(expected, actual);

	}
}
