package com.auxime.contract.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.auxime.contract.model.Cape;
import com.auxime.contract.model.CommercialContract;
import com.auxime.contract.model.Contract;
import com.auxime.contract.model.PermanentContract;
import com.auxime.contract.model.PortageConvention;
import com.auxime.contract.model.ProfileInfo;
import com.auxime.contract.model.Rates;
import com.auxime.contract.model.TemporaryContract;
import com.auxime.contract.model.enums.TypeRate;

public class GenerateListVariable {

	private GenerateListVariable() {
		throw new IllegalStateException("Utility class");
	}

	private static final String END_DATE = "${END_DATE}";

	public static Map<String, String> setListVariable(Cape cape, ProfileInfo profileInfo) {
		Map<String, String> list = setCommonFieldsContract(cape, new HashMap<>(), profileInfo);
		list.put(END_DATE, cape.getEndDate().toString());
		Comparator<Rates> comparator = Comparator.comparing(Rates::getCreatedAt);
		String qualiopyRate = "14";
		String activityRate = "10";
		if (cape.getRates() != null) {
			List<Rates> ratesActivity = cape.getRates().stream()
					.filter(rate -> rate.getTypeRate().equals(TypeRate.ACTIVITY)).collect(Collectors.toList());
			Optional<Rates> rateOpt = ratesActivity.stream().max(comparator);
			if (rateOpt.isPresent()) {
				activityRate = String.valueOf(rateOpt.get().getRate());
			}
			List<Rates> ratesQualiopy = cape.getRates().stream()
					.filter(rate -> rate.getTypeRate().equals(TypeRate.QUALIOPI)).collect(Collectors.toList());
			rateOpt = ratesQualiopy.stream().max(comparator);
			if (rateOpt.isPresent()) {
				qualiopyRate = String.valueOf(rateOpt.get().getRate());
			}
		}
		list.put("${ACTIVITY_RATE}", activityRate);
		list.put("${QUALIOPY_RATE}", qualiopyRate);
		return list;

	}

	public static Map<String, String> setListVariable(CommercialContract contract, ProfileInfo profileInfo) {
		Map<String, String> list = setCommonFieldsContract(contract, new HashMap<>(), profileInfo);
		list.put(END_DATE, contract.getEndDate().toString());
		list.put("${CLIENT_ID}", contract.getClientId().toString());
		list.put("${GLOBAL_AMOUNT}", String.valueOf(contract.getGlobalAmount()));
		list.put("${MONTHLY_AMOUNT}", String.valueOf(contract.getMonthlyAmount()));
		list.put("${MISSION_DURATION}", String.valueOf(contract.getMissionDuration()));
		list.put("${DURATION_UNITS}", String.valueOf(contract.getDurationUnit()));
		return list;
	}

	public static Map<String, String> setListVariable(PermanentContract contract, ProfileInfo profileInfo) {
		Map<String, String> list = setCommonFieldsContract(contract, new HashMap<>(), profileInfo);
		list.put("${RUPTURE_DATE}", contract.getRuptureDate().toString());
		list.put("${HOURLY_RATE}", String.valueOf(contract.getHourlyRate()));
		list.put("${WORK_TIME}", String.valueOf(contract.getWorkTime()));
		return list;
	}

	public static Map<String, String> setListVariable(PortageConvention cape, ProfileInfo profileInfo) {
		Map<String, String> list = setCommonFieldsContract(cape, new HashMap<>(), profileInfo);
		list.put(END_DATE, cape.getEndDate().toString());
		return list;
	}

	public static Map<String, String> setListVariable(TemporaryContract contract, ProfileInfo profileInfo) {
		Map<String, String> list = setCommonFieldsContract(contract, new HashMap<>(), profileInfo);
		list.put(END_DATE, contract.getEndDate().toString());
		list.put("${RUPTURE_DATE}", contract.getRuptureDate().toString());
		list.put("${HOURLY_RATE}", String.valueOf(contract.getHourlyRate()));
		list.put("${WORK_TIME}", String.valueOf(contract.getWorkTime()));
		return list;
	}

	private static Map<String, String> setCommonFieldsContract(Contract contract, Map<String, String> list,
			ProfileInfo profileInfo) {
		list.put("${ACTIVITY}", profileInfo.getActivity());
		if (profileInfo.getAddressComplement() == null) {
			list.put("${ADDRESS_COMPL}", "");
		} else {
			list.put("${ADDRESS_COMPL}", profileInfo.getAddressComplement());
		}
		list.put("${BIRTH_COUNTRY}", profileInfo.getBirthCountry());
		list.put("${BIRTH_PLACE}", profileInfo.getBirthPlace());
		list.put("${CITY}", profileInfo.getCity());
		if (profileInfo.getComplemement() == null) {
			list.put("${COMPLEMENT}", "");
		} else {
			list.put("${COMPLEMENT}", profileInfo.getComplemement());
		}
		list.put("${COUNTRY}", profileInfo.getCountry());
		list.put("${FIRST_NAME}", profileInfo.getFistName());
		list.put("${LAST_NAME}", profileInfo.getLastName());
		list.put("${NATIONALITY}", profileInfo.getNationality());
		list.put("${SOCIAL_SECURITY}", profileInfo.getSocialSecurityNumber());
		list.put("${STREET}", profileInfo.getStreet());
		list.put("${ZIP_CODE}", profileInfo.getZip());
		list.put("${BIRTHDATE}", profileInfo.getBirthdate().toString());
		list.put("${STREET_NUMBER}", Integer.toString(profileInfo.getNumber()));
		list.put("${TITLE}", profileInfo.getTitle());
		list.put("${STARTING_DATE}", contract.getStartingDate().toString());
		return list;
	}
}
