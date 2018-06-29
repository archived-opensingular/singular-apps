package org.opensingular.requirement.module.wicket.view.printer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.commons.box.action.config.EnabledPrintsPerSessionMap;
import org.opensingular.requirement.commons.service.ExtratoGeneratorService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrintFormController implements Loggable {

    @Inject
    private ExtratoGeneratorService extratoGeneratorService;

    @Autowired
    private ObjectFactory<EnabledPrintsPerSessionMap> enabledPrintsPerSessionMapObjectFactory;

    /**
     * Method responsible for find the requiriment by the UUID, and generate the PDF in a new tab, if the requiriment exists.
     * <p>
     * This method is called by ExtratoAction of the requiriment's box.
     *
     * @param response The httpServlet response, responsible for show the 410 error, if the UUID have already be used, or the file PDF.
     * @param uuid     The uuid of the requiriment.
     * @throws IOException
     */
    @RequestMapping(value = {"**/printmf/{uuid}"}, method = RequestMethod.GET)
    public void printMainForm(HttpServletResponse response, @PathVariable String uuid) throws IOException {
        Optional<Long> possibleRequirementCod = enabledPrintsPerSessionMapObjectFactory.getObject().get(uuid);
        if (!possibleRequirementCod.isPresent()) {
            response.sendRedirect("/public/error/410");
            return;
        }

        Optional<File> optPdf = extratoGeneratorService.generatePdfFile(possibleRequirementCod.get());

        optPdf.ifPresent(pdf -> {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"" + pdf.getName() + "\"");
            response.setContentLength((int) pdf.length());

            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(pdf))) {
                FileCopyUtils.copy(bufferedInputStream, response.getOutputStream());
            } catch (IOException e) {
                getLogger().error("Error obtaing the File", e);
            }
        });

    }
}