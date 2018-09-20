package org.opensingular.app.commons.test.pdf;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Assert;
import org.junit.Test;
import org.opensingular.app.commons.pdf.FlyingSaucerConverter;
import org.opensingular.lib.commons.dto.HtmlToPdfDTO;
import org.opensingular.lib.commons.util.Loggable;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class FlyingSaucerConverterTest implements Loggable {

    private boolean showPDF = false;

    @Test
    public void convertPdfWithHeaderBodyFooter() throws IOException {
        FlyingSaucerConverter flyingSaucerConverter = new FlyingSaucerConverter();

        flyingSaucerConverter.setShowPageNumber(true);
        flyingSaucerConverter.setPageLabel("");
        flyingSaucerConverter.setOfLabel("/");

        String header = readFile("src/test/java/org/opensingular/app/commons/test/pdf/html/header.html", Charset.forName("UTF-8"));
        String body = readFile("src/test/java/org/opensingular/app/commons/test/pdf/html/body.html", Charset.forName("UTF-8"));
        String footer = readFile("src/test/java/org/opensingular/app/commons/test/pdf/html/footer.html", Charset.forName("UTF-8"));

        HtmlToPdfDTO htmlToPdfDTO = new HtmlToPdfDTO(header, body, footer);


        Optional<File> convert = flyingSaucerConverter.convert(htmlToPdfDTO);

        Assert.assertTrue(convert.isPresent() && FileUtils.sizeOf(convert.get()) > 1L);

        if (showPDF) {
            File file = convert.get();
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(file);
            } catch (IOException e) {
                getLogger().error("Erro ao abrir o arquivo", e);
            }

        }

    }

    @Test
    public void testGeneratePdfWithSpecialCaracteres() {
        FlyingSaucerConverter flyingSaucerConverter = new FlyingSaucerConverter();

        flyingSaucerConverter.setShowPageNumber(true);
        flyingSaucerConverter.setPageLabel("");
        flyingSaucerConverter.setOfLabel("/");

        String body = "<div> <span> Teste caracteres especiais: ç^`%$&*#áéíóú</span></div>";
        HtmlToPdfDTO htmlToPdfDTO = new HtmlToPdfDTO(body);


        Optional<File> convert = flyingSaucerConverter.convert(htmlToPdfDTO);

        Assert.assertTrue(convert.isPresent() && FileUtils.sizeOf(convert.get()) > 1L);


        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        try {
            PDFParser parser = new PDFParser((RandomAccessRead) new FileInputStream(convert.get()));
            parser.parse();
            cosDoc = parser.getDocument();
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            String parsedText = pdfStripper.getText(pdDoc);
            Assert.assertEquals("ç^`%$&*#áéíóú", parsedText);
        } catch (Exception e) {
            getLogger().error("Erro ao abrir o arquivo", e);
            try {
                if (cosDoc != null)
                    cosDoc.close();
                if (pdDoc != null)
                    pdDoc.close();
            } catch (Exception e1) {
                getLogger().error("Erro ao abrir o arquivo", e);
            }

        }

        if (showPDF) {
            File file = convert.get();
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(file);
            } catch (IOException e) {
                getLogger().error("Erro ao abrir o arquivo", e);
            }

        }

    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}