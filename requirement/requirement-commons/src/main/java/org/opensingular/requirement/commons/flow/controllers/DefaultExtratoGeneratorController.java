/*
 *
 *  * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.opensingular.requirement.commons.flow.controllers;

import java.io.File;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.opensingular.lib.commons.dto.HtmlToPdfDTO;
import org.opensingular.lib.commons.pdf.HtmlToPdfConverter;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.commons.box.action.ActionRequest;
import org.opensingular.requirement.commons.box.action.ActionResponse;
import org.opensingular.requirement.commons.extrato.Generator;
import org.opensingular.requirement.commons.service.RequirementInstance;
import org.springframework.stereotype.Controller;


@Controller
public class DefaultExtratoGeneratorController extends IController implements Loggable {

    @Inject
    private HtmlToPdfConverter htmlToPdfConverter;

    @Inject
    private Generator extratoGenerator;

    @Override
    public ActionResponse execute(@Nonnull RequirementInstance requirement, ActionRequest action) {
        String result = extratoGenerator.generate(requirement.getMainForm());
        Optional<File> file = htmlToPdfConverter.convert(new HtmlToPdfDTO(result));

        System.out.println("\n\n" + result + "\n\n");
        return new ActionResponse("Extrato gerado com sucesso", true);
    }

}
