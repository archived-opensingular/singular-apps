package org.opensingular.requirement.module.pdf;

import com.testautomationguru.utility.CompareMode;
import com.testautomationguru.utility.PDFUtil;
import org.junit.Test;
import org.opensingular.lib.commons.dto.HtmlToPdfDTO;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.assertTrue;

public class FlyingSaucerConverterTest {

    @Test
    public void convert() throws IOException {
        FlyingSaucerConverter flyingSaucerConverter = new FlyingSaucerConverter();

        flyingSaucerConverter.setShowPageNumber(true);
        flyingSaucerConverter.setPageLabel("");
        flyingSaucerConverter.setOfLabel("/");

        String header = readFile("src/test/java/org/opensingular/requirement/module/pdf/html/header.html", Charset.forName("UTF-8"));
        String body = readFile("src/test/java/org/opensingular/requirement/module/pdf/html/body.html", Charset.forName("UTF-8"));
        String footer = readFile("src/test/java/org/opensingular/requirement/module/pdf/html/footer.html", Charset.forName("UTF-8"));

        HtmlToPdfDTO htmlToPdfDTO = new HtmlToPdfDTO(header, body, footer);


        Optional<File> convert = flyingSaucerConverter.convert(htmlToPdfDTO);

        if (convert.isPresent()) {
            File file = convert.get();
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            PDFUtil pdfUtil = new PDFUtil();

            pdfUtil.setCompareMode(CompareMode.VISUAL_MODE);
            assertTrue("PDF gerado diferente do esperado", pdfUtil.compare(file.getAbsolutePath(), "src/test/java/org/opensingular/requirement/module/pdf/testResult.pdf"));
        }

    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}