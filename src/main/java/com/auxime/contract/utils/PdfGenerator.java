package com.auxime.contract.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.auxime.contract.constants.ExceptionMessageConstant;
import com.auxime.contract.exception.PdfGeneratorException;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;

@Service
public class PdfGenerator {

	private static final Logger logger = LogManager.getLogger(PdfGenerator.class);
	@Value("#{systemEnvironment['AUXIME_CONTRACT_PATH']}")
	private String contractPath;
	@Value("#{systemEnvironment['AUXIME_CONTRACT_MODELS']}")
	private String modelsPath;

	@Async
	public void replaceTextModel(Map<String, String> listWords, String fileName, String file)
			throws PdfGeneratorException {
		// Use to avoid exception
		ZipSecureFile.setMinInflateRatio(0);
		XWPFDocument doc;
		try {
			// Open the Model
			doc = new XWPFDocument(new FileInputStream(modelsPath + file));
		} catch (IOException e) {
			logger.error(ExceptionMessageConstant.MODEL_NOT_FOUND);
			throw new PdfGeneratorException(ExceptionMessageConstant.MODEL_NOT_FOUND);
		}
		// Setting a Map to read for text replacement
		replaceTextInDoc(listWords, doc);
		saveFilePdf(fileName, doc);
	}

	private XWPFDocument replaceTextInDoc(Map<String, String> listWords, XWPFDocument doc) {
		for (Map.Entry<String, String> entry : listWords.entrySet()) {
			// POI function to read the paragraphs and find text
			for (XWPFParagraph p : doc.getParagraphs()) {
				List<XWPFRun> runs = p.getRuns();
				if (runs != null) {
					findReplaceText(runs, doc, entry);
				}
			}
		}
		return doc;
	}

	private XWPFDocument findReplaceText(List<XWPFRun> runs, XWPFDocument doc, Map.Entry<String, String> entry) {
		for (XWPFRun r : runs) {
			String text = r.getText(0);
			if (text != null && text.contains(entry.getKey())) {
				text = text.replace(entry.getKey(), entry.getValue());
				r.setText(text, 0);
			}
		}
		return doc;
	}

	public void saveFilePdf(String fileName, XWPFDocument doc) throws PdfGeneratorException {

		String savingPath = contractPath + fileName + ".docx";
		try (FileOutputStream out = new FileOutputStream(savingPath)) {
			doc.write(out);
			doc.close();
		} catch (FileNotFoundException e) {
			logger.error(ExceptionMessageConstant.PATH_NOT_FOUND);
			throw new PdfGeneratorException(ExceptionMessageConstant.PATH_NOT_FOUND);
		} catch (IOException e) {
			logger.error(ExceptionMessageConstant.CLOSE_DOC_ERROR);
		}
		try {
			convertToPDF(savingPath, contractPath, fileName);
		} catch (InterruptedException | ExecutionException | IOException e) {
			Thread.currentThread().interrupt();
			throw new PdfGeneratorException(ExceptionMessageConstant.CONVERTION_ERROR);
		}
	}

	public void convertToPDF(String savingPath, String pdfPath, String fileName)
			throws InterruptedException, ExecutionException, IOException {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(savingPath))) {
			IConverter converter = LocalConverter.builder().baseFolder(new File(pdfPath))
					.workerPool(20, 25, 2, TimeUnit.SECONDS).processTimeout(5, TimeUnit.SECONDS).build();

			Future<Boolean> conversion = converter.convert(in).as(DocumentType.MS_WORD).to(bo).as(DocumentType.PDF)
					.prioritizeWith(1000) // optional
					.schedule();
			conversion.get();
		} catch (IOException e) {
			logger.error(ExceptionMessageConstant.READING_ERROR);
		}

		try (OutputStream outputStream = new FileOutputStream(pdfPath + fileName + ".pdf")) {
			bo.writeTo(outputStream);
		} catch (IOException e) {
			logger.error(ExceptionMessageConstant.CONVERTION_ERROR);
		} finally {
			bo.close();
		}
	}
}
