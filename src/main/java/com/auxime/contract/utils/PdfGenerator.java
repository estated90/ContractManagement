package com.auxime.contract.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.auxime.contract.constants.ExceptionMessageConstant;
import com.auxime.contract.exception.PdfGeneratorException;

@Service
public class PdfGenerator {

	private static final Logger logger = LogManager.getLogger(PdfGenerator.class);

	@Async
	public void replaceTextModel(Map<String, String> listWords, String fileName, String file)
			throws PdfGeneratorException {
		// Use to avoid exception
		ZipSecureFile.setMinInflateRatio(0);
		XWPFDocument doc;
		try {
			// Open the Model
			doc = new XWPFDocument(new FileInputStream("asset/" + file));
		} catch (IOException e) {
			logger.error(ExceptionMessageConstant.MODEL_NOT_FOUND);
			throw new PdfGeneratorException(ExceptionMessageConstant.MODEL_NOT_FOUND);
		}
		// Setting a Map to read for text replacement
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

}
