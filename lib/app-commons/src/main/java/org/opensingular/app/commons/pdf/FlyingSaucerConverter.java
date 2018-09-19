package org.opensingular.app.commons.pdf;

import com.itextpdf.text.DocumentException;
import org.apache.commons.io.IOUtils;
import org.opensingular.lib.commons.dto.HtmlToPdfDTO;
import org.opensingular.lib.commons.pdf.HtmlToPdfConverter;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.apache.logging.log4j.util.Strings.EMPTY;

/**
 * Class for converting html files into PDF using the Java only Flying Saucer library.
 */
public class FlyingSaucerConverter implements HtmlToPdfConverter {

    /**
     * Toggle the page counter in the right lower corner of the generated PDF pages.
     */
    private boolean showPageNumber;

    /**
     * These fields pageLabel and ofLabel are displayed at the end of the PDF page
     * in the page counter like this: "pageLabel X ofLabel Y".
     * Where X is the current page and Y the total amount of pages.
     * e.g. "Página 4 de 10" - by default.
     * e.g. "pág. 4 / 10".
     * e.g. "Page 4 of 10".
     * Can be customized by it's setter methods.
     */
    private String pageLabel = "Página";
    private String ofLabel = "de";

    public FlyingSaucerConverter() {
        this.showPageNumber = true;
    }

    public FlyingSaucerConverter(boolean showPageNumber) {
        this.showPageNumber = showPageNumber;
    }

    public void setShowPageNumber(boolean showPageNumber) {
        this.showPageNumber = showPageNumber;
    }

    public void setPageLabel(String pageLabel) {
        this.pageLabel = pageLabel;
    }

    public void setOfLabel(String ofLabel) {
        this.ofLabel = ofLabel;
    }

    /**
     * Generates the File from the HtmlToPdfDTO.
     *
     * @param htmlToPdfDTO the HtmlToPdfDTO.
     * @return Optional of File with the PDF.
     */
    @Override
    public Optional<File> convert(HtmlToPdfDTO htmlToPdfDTO) {
        InputStream in = convertStream(htmlToPdfDTO);
        if (in != null) {
            return Optional.ofNullable(convertHtmlToPdf(in));
        }
        return Optional.empty();
    }

    /**
     * Converts the html into a PDF file using Flying Saucer library via ITextRenderer.
     *
     * @param in InputStream with the PDF information (html).
     * @return PDF File.
     * @apiNote even though the Flying Saucer api does not support scripts,
     * if the html contains any <script> tag, it's content must be
     * surrounded with  <![CDATA[ ... ]]> tag in order to avoid conflicts
     * with html entities and scripts syntax during PDF generation.
     */
    private File convertHtmlToPdf(InputStream in) {
        String content = readFromInputStream(in);
        try {
            Path path = Files.createTempFile(generateFileName(), ".pdf");

            if (createPdfFile(in, content, path)) return path.toFile();
        } catch (IOException e) {
            getLogger().error("Não foi possivel criar o arquivo temporario", e);
        }

        return null;
    }

    private boolean createPdfFile(InputStream in, String content, Path path) {
        try (OutputStream out = Files.newOutputStream(path)) {

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(content);
            renderer.layout();
            renderer.createPDF(out);

            IOUtils.copy(in, out);
            return true;
        } catch (IOException ex) {
            getLogger().error("Não foi possivel escrever o arquivo temporario", ex);
        } catch (DocumentException e) {
            getLogger().error("Problema na geração de PDF", e);
        }
        return false;
    }

    /**
     * Generates the name for the temporary File for the PDF.
     *
     * @return the name for the temporary File.
     */
    private static String generateFileName() {
        return String.format("singular-fs-html2pdf-%s.pdf", UUID.randomUUID());
    }

    /**
     * Converts the InputStream into a String with it's information (html in this case).
     *
     * @param inputStream the InputStream.
     * @return the String information of the InputStream
     */
    private String readFromInputStream(InputStream inputStream) {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append('\n');
            }
        } catch (IOException e) {
            getLogger().error("Erro ao ler InputStream", e);
        }
        return resultStringBuilder.toString();
    }

    /**
     * Converts the HtmlToPdfDTO into an InputStream.
     *
     * @param htmlToPdfDTO the HtmlToPdfDTO.
     * @return InputStream with the whole PDF information (html).
     */
    @Override
    public InputStream convertStream(HtmlToPdfDTO htmlToPdfDTO) {
        return new ByteArrayInputStream(getPagehtml(htmlToPdfDTO).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * It returns the body of the HtmlToPdfDTO.
     * Adds the page counter if it is mean to.
     *
     * @param htmlToPdfDTO the HtmlToPdfDTO.
     * @return full formatted html of the page to be converted in PDF
     */
    private String getPagehtml(HtmlToPdfDTO htmlToPdfDTO) {
        try {
            /*TODO: evolve API for header and footer support
            String header = "<div id=\"flying-saucer-header\">" + htmlToPdfDTO.getHeader() +  "</div>";
            String footer = "<div id=\"flying-saucer-footer\">" + htmlToPdfDTO.getFooter() +  "</div>";*/

            if (!htmlToPdfDTO.getHeader().isEmpty() || !htmlToPdfDTO.getFooter().isEmpty()) {
                getLogger().warn("The contents of the HtmlToPdfDTO's header and footer are ignored in the final PDF file.");
            }

            String body = "<div id=\"flying-saucer-body\">" + htmlToPdfDTO.getBody() + "</div>";
            return formatHtml(getPageNumberHtml() /*+ header + footer*/ + body);
        } catch (UnsupportedEncodingException e) {
            getLogger().error("Erro ao formatar html", e);
        }
        return EMPTY;
    }

    /**
     * Merges the template "FlyingSaucerTemplatePage.ftl" with pageLabel and ofLabel and
     * returns the html String with the component for page counting in the
     * generated PDF.
     *
     * @return the html String for page counting.
     */
    private String getPageNumberHtml() {
        Map<String, Object> map = new HashMap<>();
        map.put("showPageNumber", showPageNumber ? "block" : "none");
        map.put("page", pageLabel);
        map.put("of", ofLabel);

        return PServerFreeMarkerUtil.mergeWithFreemarker("FlyingSaucerTemplatePage.ftl", map);
    }

    /**
     * It takes a mal-formed html and restructures it, encoding in UTF-8.
     *
     * @param data mal-formed html.
     * @return String well-formed html.
     * @throws UnsupportedEncodingException
     */
    private String formatHtml(String data) throws UnsupportedEncodingException {
        Tidy tidy = new Tidy();
        tidy.setWraplen(Integer.MAX_VALUE);
        tidy.setXmlOut(true);
        tidy.setSmartIndent(true);
        tidy.setCharEncoding(Configuration.UTF8);
        tidy.setWrapScriptlets(true);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);
        return outputStream.toString("UTF-8");
    }

}