package com.auxime.contract.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.auxime.contract.constants.ContractState;
import com.auxime.contract.constants.ContractStatus;
import com.auxime.contract.constants.DurationUnit;
import com.auxime.contract.model.Cape;
import com.auxime.contract.model.CommercialContract;
import com.auxime.contract.model.Contract;
import com.auxime.contract.model.PermanentContract;
import com.auxime.contract.model.PortageConvention;
import com.auxime.contract.model.ProfileInfo;
import com.auxime.contract.model.Rates;
import com.auxime.contract.model.TemporaryContract;
import com.auxime.contract.model.enums.ContractType;
import com.auxime.contract.model.enums.PortageCompanies;
import com.auxime.contract.model.enums.TypeRate;

class GenerateListVariableTest {

	private Cape cape;
	private ProfileInfo profileInfo;
	private CommercialContract commercialContract;
	private PermanentContract perCon;
	private PortageConvention porCon;
	private TemporaryContract tempContract;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		cape = (Cape) setCommonContract(new Cape());
		cape.setFse(true);
		Set<Rates> rates = new HashSet<>();
		Rates rate = new Rates();
		rate.setCreatedAt(LocalDateTime.now());
		rate.setRate(15);
		rate.setRateId(UUID.randomUUID());
		rate.setTypeRate(TypeRate.ACTIVITY);
		rates.add(rate);
		rate = new Rates();
		rate.setCreatedAt(LocalDateTime.now().minusMonths(3));
		rate.setRate(20);
		rate.setRateId(UUID.randomUUID());
		rate.setTypeRate(TypeRate.ACTIVITY);
		rates.add(rate);
		rate = new Rates();
		rate.setCreatedAt(LocalDateTime.now());
		rate.setRate(19);
		rate.setRateId(UUID.randomUUID());
		rate.setTypeRate(TypeRate.QUALIOPI);
		rates.add(rate);
		cape.setStatus(true);
		cape.setRates(rates);
		commercialContract = (CommercialContract) setCommonContract(new CommercialContract());
		commercialContract.setClientId(UUID.randomUUID());
		commercialContract.setGlobalAmount(10000);
		commercialContract.setMonthlyAmount(1000);
		commercialContract.setMissionDuration(10);
		commercialContract.setDurationUnit(DurationUnit.MONTHS);
		commercialContract.setContractStatus(ContractStatus.VALIDATED);
		perCon = (PermanentContract) setCommonContract(new PermanentContract());
		perCon.setRuptureDate(LocalDate.now().plusDays(7));
		perCon.setHourlyRate(12);
		perCon.setWorkTime(151.62);
		porCon = (PortageConvention) setCommonContract(new PortageConvention());
		porCon.setCommission(10);
		tempContract = (TemporaryContract) setCommonContract(new TemporaryContract());
		tempContract.setRuptureDate(LocalDate.now().plusDays(7));
		tempContract.setHourlyRate(12);
		tempContract.setWorkTime(151.62);
		profileInfo = new ProfileInfo();
		cape.setStructureContract(PortageCompanies.AUXIME);
		profileInfo.setActivity("Training HR");
		profileInfo.setAddressComplement("allée A");
		profileInfo.setBirthCountry("France");
		profileInfo.setBirthdate(LocalDate.now().minusYears(30));
		profileInfo.setBirthPlace("Paris");
		profileInfo.setBusinessManagerId(UUID.randomUUID());
		profileInfo.setCity("Lyon");
		profileInfo.setComplemement(null);
		profileInfo.setCountry("France");
		profileInfo.setFistName("Bryan");
		profileInfo.setLastName("Anderson");
		profileInfo.setManagerId(UUID.randomUUID());
		profileInfo.setNationality("Français");
		profileInfo.setNumber(8);
		profileInfo.setSocialSecurityNumber("1 90 8542613 21321321");
		profileInfo.setStreet("Rue de la République");
		profileInfo.setTitle("Monsieur");
		profileInfo.setZip("69001");
	}
	
	private Contract setCommonContract(Contract contract) {
		contract.setAccountId(UUID.randomUUID());
		LocalDate date = LocalDate.now();
		contract.setContractDate(date.minusMonths(1));
		contract.setContractId(UUID.randomUUID());
		contract.setContractState(ContractState.NOT_STARTED);
		contract.setContractTitle("Unit test contract");
		contract.setContractType(ContractType.CONTRACT);
		contract.setContractTypology("cape");
		contract.setCreatedAt(LocalDateTime.now());
		contract.setEndDate(date.plusYears(1));
		contract.setStartingDate(date.plusMonths(1));
		return contract;
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void givenObject_wheCreatingMap_thenReturnMap() {
		// GIVEN
		Cape testedCape = cape;
		ProfileInfo profileInfoTest = profileInfo;
		// WHEN
		Map<String, String> result = GenerateListVariable.setListVariable(testedCape, profileInfoTest);
		// THEN
		assertEquals("Training HR", result.get("${ACTIVITY}"));
		assertEquals(LocalDate.now().plusMonths(1).toString(), result.get("${STARTING_DATE}"));
		assertEquals("Bryan", result.get("${FIRST_NAME}"));
		assertEquals("Anderson", result.get("${LAST_NAME}"));
		assertEquals("", result.get("${COMPLEMENT}"));
		assertEquals("allée A", result.get("${ADDRESS_COMPL}"));
		assertEquals("8", result.get("${STREET_NUMBER}"));
		assertEquals("15", result.get("${ACTIVITY_RATE}"));
		assertEquals("19", result.get("${QUALIOPY_RATE}"));
	}

	@Test
	void givenObject_wheCreatingMapWithEmptyLines_thenReturnMap() {
		// GIVEN
		Cape testedCape = cape;
		ProfileInfo profileInfoTest = profileInfo;
		profileInfoTest.setComplemement("Bis");
		profileInfoTest.setAddressComplement(null);
		// WHEN
		Map<String, String> result = GenerateListVariable.setListVariable(testedCape, profileInfoTest);
		// THEN
		assertEquals("Training HR", result.get("${ACTIVITY}"));
		assertEquals(LocalDate.now().plusMonths(1).toString(), result.get("${STARTING_DATE}"));
		assertEquals("Bryan", result.get("${FIRST_NAME}"));
		assertEquals("Anderson", result.get("${LAST_NAME}"));
		assertEquals("Bis", result.get("${COMPLEMENT}"));
		assertEquals("", result.get("${ADDRESS_COMPL}"));
		assertEquals("8", result.get("${STREET_NUMBER}"));
		assertEquals("15", result.get("${ACTIVITY_RATE}"));
		assertEquals("19", result.get("${QUALIOPY_RATE}"));
	}

	@Test
	void givenObject_wheCreatingMapWithEmptyRates_thenReturnMapWithDefaultRate() {
		// GIVEN
		Cape testedCape = cape;
		ProfileInfo profileInfoTest = profileInfo;
		testedCape.setRates(null);
		// WHEN
		Map<String, String> result = GenerateListVariable.setListVariable(testedCape, profileInfoTest);
		// THEN
		assertEquals("10", result.get("${ACTIVITY_RATE}"));
		assertEquals("14", result.get("${QUALIOPY_RATE}"));
	}

	@Test
	void givenObject_wheCreatingMapWithMissingRate_thenReturnMapWithDefaultRate() {
		// GIVEN
		Cape testedCape = cape;
		ProfileInfo profileInfoTest = profileInfo;
		List<Rates> toRemove = testedCape.getRates().stream().filter(rate -> rate.getTypeRate() == TypeRate.ACTIVITY)
				.toList();
		testedCape.getRates().removeAll(toRemove);
		// WHEN
		Map<String, String> result = GenerateListVariable.setListVariable(testedCape, profileInfoTest);
		// THEN
		assertEquals("10", result.get("${ACTIVITY_RATE}"));
		assertEquals("19", result.get("${QUALIOPY_RATE}"));
	}
	
	@Test
	void givenObject_wheCreatingMapWithMissingQualiopiRate_thenReturnMapWithDefaultRate() {
		// GIVEN
		Cape testedCape = cape;
		ProfileInfo profileInfoTest = profileInfo;
		List<Rates> toRemove = testedCape.getRates().stream().filter(rate -> rate.getTypeRate() == TypeRate.QUALIOPI)
				.toList();
		testedCape.getRates().removeAll(toRemove);
		// WHEN
		Map<String, String> result = GenerateListVariable.setListVariable(testedCape, profileInfoTest);
		// THEN
		assertEquals("15", result.get("${ACTIVITY_RATE}"));
		assertEquals("14", result.get("${QUALIOPY_RATE}"));
	}
	
	@Test
	void givenObject_wheCreatingMapCommercialContract_thenReturnMap() {
		// GIVEN
		CommercialContract testedContract = commercialContract;
		ProfileInfo profileInfoTest = profileInfo;
		// WHEN
		Map<String, String> result = GenerateListVariable.setListVariable(testedContract, profileInfoTest);
		// THEN
		assertEquals(LocalDate.now().plusYears(1).toString(), result.get("${END_DATE}"));
		assertNotNull(result.get("${CLIENT_ID}"));
		assertEquals("10000.0", result.get("${GLOBAL_AMOUNT}"));
		assertEquals("1000.0", result.get("${MONTHLY_AMOUNT}"));
		assertEquals("10", result.get("${MISSION_DURATION}"));
		assertEquals("MONTHS", result.get("${DURATION_UNITS}"));
	}
	
	@Test
	void givenObject_wheCreatingMapPermanentContract_thenReturnMap() {
		// GIVEN
		PermanentContract testedContract = perCon;
		ProfileInfo profileInfoTest = profileInfo;
		// WHEN
		Map<String, String> result = GenerateListVariable.setListVariable(testedContract, profileInfoTest);
		// THEN
		assertEquals(LocalDate.now().plusDays(7).toString(), result.get("${RUPTURE_DATE}"));
		assertEquals("12.0", result.get("${HOURLY_RATE}"));
		assertEquals("151.62", result.get("${WORK_TIME}"));
	}
	
	@Test
	void givenObject_wheCreatingMapTemporaryContract_thenReturnMap() {
		// GIVEN
		TemporaryContract testedContract = tempContract;
		ProfileInfo profileInfoTest = profileInfo;
		// WHEN
		Map<String, String> result = GenerateListVariable.setListVariable(testedContract, profileInfoTest);
		// THEN
		assertEquals(LocalDate.now().plusDays(7).toString(), result.get("${RUPTURE_DATE}"));
		assertEquals("12.0", result.get("${HOURLY_RATE}"));
		assertEquals("151.62", result.get("${WORK_TIME}"));
	}
	
	@Test
	void givenObject_wheCreatingMapPortageConvention_thenReturnMap() {
		// GIVEN
		PortageConvention testedContract = porCon;
		ProfileInfo profileInfoTest = profileInfo;
		// WHEN
		Map<String, String> result = GenerateListVariable.setListVariable(testedContract, profileInfoTest);
		// THEN
		assertEquals(LocalDate.now().plusYears(1).toString(), result.get("${END_DATE}"));
	}

}
