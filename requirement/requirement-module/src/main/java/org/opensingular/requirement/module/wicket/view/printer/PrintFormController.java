/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.requirement.module.wicket.view.printer;

import org.apache.commons.io.IOUtils;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.module.box.action.defaults.ExtratoAction;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.service.ExtratoGeneratorService;
import org.opensingular.requirement.module.spring.security.AuthorizationService;
import org.opensingular.requirement.module.spring.security.SingularRequirementUserDetails;
import org.opensingular.requirement.module.wicket.SingularSession;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

@RestController
public class PrintFormController implements Loggable {

    @Inject
    private ExtratoGeneratorService extratoGeneratorService;

    @Inject
    private AuthorizationService authorizationService;

    @Inject
    private ObjectFactory<IServerContext> serverContextObjectFactory;


    /**
     * Method responsible for find the requiriment by the UUID, and generate the PDF in a new tab, if the requiriment exists.
     * <p>
     * This method is called by ExtratoAction of the requiriment's box.
     *
     * @param response     The httpServlet response, responsible for show the 410 error, if the UUID have already be used, or the file PDF.
     * @param requirmentId The id of the requiriment.
     * @throws IOException
     */
    @RequestMapping(value = {"**/printmf/{requirmentId}"}, method = RequestMethod.GET)
    public void printMainForm(HttpServletRequest request, HttpServletResponse response, @PathVariable Long requirmentId) throws IOException {

        if (requirmentId == null || !SingularSession.exists()) {
            response.sendRedirect("/public/error/410");
            return;
        }

        SingularRequirementUserDetails userDetails = SingularSession.get().getUserDetails();
        String idUsuarioLogado = userDetails.getUsername();
        String idApplicant = userDetails.getApplicantId();

        boolean hasPermission = authorizationService.hasPermission(requirmentId, null, idUsuarioLogado, idApplicant, ExtratoAction.EXTRATO, serverContextObjectFactory.getObject(), true);

        if (!hasPermission) {
            response.sendRedirect("/public/error/403");
            return;
        }
        Optional<File> optPdf = extratoGeneratorService.generatePdfFile(requirmentId);

        optPdf.ifPresent(pdf -> {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"" + pdf.getName() + "\"");
            response.setContentLength((int) pdf.length());

            try (FileInputStream fis = new FileInputStream(pdf)) {
                IOUtils.copy(fis, response.getOutputStream());
            } catch (IOException e) {
                getLogger().error("Error obtaing the File", e);
            }
        });

    }
}