package com.auxime.contract.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.auxime.contract.constants.ExceptionMessageConstant;
import com.auxime.contract.exception.PdfGeneratorException;
import com.auxime.contract.model.Cape;
import com.auxime.contract.model.ProfileInfo;
import com.auxime.contract.proxy.AccountFeign;

@Service
public class PdfGenerator {

	private static final Logger logger = LogManager.getLogger(PdfGenerator.class);
	@Autowired
	private AccountFeign accountFeign;
	
	public void pdfGenerator(Cape cape) throws PdfGeneratorException {
		// Getting the info linked to the profile of contract account
		ProfileInfo profileInfo = accountFeign.getProfilesFromAccountId(cape.getAccountId());
		if (profileInfo == null) {
			logger.error(ExceptionMessageConstant.PROFILE_NOT_RETRIEVED);
			throw new PdfGeneratorException(ExceptionMessageConstant.PROFILE_NOT_RETRIEVED);
		}
		replaceTextModel(cape, profileInfo);
	}
	
	@Async
	public void replaceTextModel(Cape cape, ProfileInfo profileInfo) throws PdfGeneratorException {
		// Use to avoid exception
		ZipSecureFile.setMinInflateRatio(0);
		XWPFDocument doc;
		try {
			// Open the Model
			doc = new XWPFDocument(new FileInputStream("asset/CAPE VIERGE 2022.docx"));
		} catch (IOException e) {
			logger.error(ExceptionMessageConstant.MODEL_NOT_FOUND);
			throw new PdfGeneratorException(ExceptionMessageConstant.MODEL_NOT_FOUND);
		}
		// Setting a Map to read for text replacement
		Map<String, String> listWords = setListVariable(cape, profileInfo);
		for (Map.Entry<String, String> entry : listWords.entrySet()) {
			// POI function to read the paragraphs and find text
			for (XWPFParagraph p : doc.getParagraphs()) {
				List<XWPFRun> runs = p.getRuns();
				if (runs != null) {
					for (XWPFRun r : runs) {
						String text = r.getText(0);
						if (text != null && text.contains(entry.getKey())) {
							text = text.replace(entry.getKey(), entry.getValue());
							r.setText(text, 0);
						}
					}
				}
			}
		}
		FileOutputStream out = null;
		// Saving and closing the document
		try {
			String fileName = cape.getContractType().toString() + " CAPE " + profileInfo.getLastName() + " "
					+ profileInfo.getFistName() + " " + LocalDateTime.now().toString().replace("-", "_").replace(":", "_");
			out = new FileOutputStream("C:/contractGeneration/" + fileName + ".docx");
		} catch (FileNotFoundException e) {
			logger.error(ExceptionMessageConstant.PATH_NOT_FOUND);
			throw new PdfGeneratorException(ExceptionMessageConstant.PATH_NOT_FOUND);
		} finally {
			try {
				doc.write(out);
				out.close();
				doc.close();
			} catch (IOException e) {
				logger.error(ExceptionMessageConstant.CLOSE_DOC_ERROR);
				throw new PdfGeneratorException(ExceptionMessageConstant.CLOSE_DOC_ERROR);
			}

		}

	}

	private Map<String, String> setListVariable(Cape cape, ProfileInfo profileInfo) {
		Map<String, String> list = new HashMap<>();
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
		list.put("${END_DATE}", cape.getEndDate().toString());
		list.put("${STARTING_DATE}", cape.getStartingDate().toString());
		return list;
	}

}
