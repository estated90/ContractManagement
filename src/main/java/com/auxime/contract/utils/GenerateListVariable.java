package com.auxime.contract.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.auxime.contract.model.Cape;
import com.auxime.contract.model.Contract;
import com.auxime.contract.model.ProfileInfo;
import com.auxime.contract.model.Rates;
import com.auxime.contract.model.enums.TypeRate;

public class GenerateListVariable {

	public static Map<String, String> setListVariable(Cape cape, ProfileInfo profileInfo) {
		Map<String, String> list = setCommonFieldsContract(cape, new HashMap<>(), profileInfo);
		list.put("${END_DATE}", cape.getEndDate().toString());
		Comparator<Rates> comparator = Comparator.comparing(Rates::getCreatedAt);
		List<Rates> ratesActivity = cape.getRates().stream().filter(rate -> rate.getTypeRate().equals(TypeRate.ACTIVITY)).collect(Collectors.toList());
		list.put("${ACTIVITY_RATE}", Integer.toString(ratesActivity.stream().max(comparator).get().getRate()));
		List<Rates> ratesQualiopy = cape.getRates().stream().filter(rate -> rate.getTypeRate().equals(TypeRate.QUALIOPI)).collect(Collectors.toList());
		list.put("${QUALIOPY_RATE}", Integer.toString(ratesQualiopy.stream().max(comparator).get().getRate()));
		return list;
	}

	private static Map<String, String> setCommonFieldsContract(Contract contract, Map<String, String> list, ProfileInfo profileInfo){
		list.put("${ACTIVITY}", profileInfo.getActivity());
		if(profileInfo.getAddressComplement()==null) {
			list.put("${ADDRESS_COMPL}", "");
		} else {
			list.put("${ADDRESS_COMPL}", profileInfo.getAddressComplement());
		}
		list.put("${BIRTH_COUNTRY}", profileInfo.getBirthCountry());
		list.put("${BIRTH_PLACE}", profileInfo.getBirthPlace());
		list.put("${CITY}", profileInfo.getCity());
		if(profileInfo.getComplemement()==null) {
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
