package org.opensingular.requirement.module.pdf;

import org.junit.Test;
import org.opensingular.lib.commons.dto.HtmlToPdfDTO;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class FlyingSaucerConverterTest {

    @Test
    public void convert() throws IOException {
        FlyingSaucerConverter flyingSaucerConverter = new FlyingSaucerConverter();

        String header = readFile("src/test/java/org/opensingular/requirement/module/pdf/html/header.html", Charset.forName("UTF-8"));
        String body = readFile("src/test/java/org/opensingular/requirement/module/pdf/html/body.html", Charset.forName("UTF-8"));
        String footer = readFile("src/test/java/org/opensingular/requirement/module/pdf/html/footer.html", Charset.forName("UTF-8"));

        HtmlToPdfDTO htmlToPdfDTO = new HtmlToPdfDTO(header, body, footer);


        Optional<File> convert = flyingSaucerConverter.convert(htmlToPdfDTO);

        convert.ifPresent(file -> {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}