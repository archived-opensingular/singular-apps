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

package org.opensingular.requirement.module.extrato;

import org.opensingular.form.SInstance;
import org.opensingular.form.flatview.FlatViewContext;
import org.opensingular.lib.commons.canvas.HtmlCanvas;
import org.opensingular.lib.commons.canvas.bootstrap.BootstrapHtmlCanvas;

import static org.opensingular.form.flatview.FlatViewGenerator.ASPECT_FLAT_VIEW_GENERATOR;

public class ExtratoGeneratorImpl implements ExtratoGenerator {

    /**
     * This is a method default that used BootstrapHtmlCanvas to generate the html of the requirement.
     *
     * @param root doc in the super class.
     * @return doc in the super class.
     */
    @Override
    public String generate(SInstance root) {
        HtmlCanvas htmlCanvas = new BootstrapHtmlCanvas(true);
        root.getAspect(ASPECT_FLAT_VIEW_GENERATOR).ifPresent(i -> i.writeOnCanvas(htmlCanvas, new FlatViewContext(root)));
        return htmlCanvas.build();
    }
}
