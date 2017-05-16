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

package org.opensingular.server.commons.wicket.view.form;


import org.apache.wicket.markup.html.panel.Panel;
import org.opensingular.lib.wicket.util.modal.BSModalBorder;

public abstract class FlowConfirmPanel extends Panel {

    private BSModalBorder modalBorder;
    private String        transition;

    public FlowConfirmPanel(String id, String transition) {
        super(id);
        this.transition = transition;
        this.modalBorder = makeBorder();
        addComponentsToPanel();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        addComponentsToModalBorder(modalBorder);
    }

    private void addComponentsToPanel() {
        add(modalBorder);
    }

    private BSModalBorder makeBorder() {
        return new BSModalBorder("modal");
    }

    abstract void addComponentsToModalBorder(BSModalBorder modalBorder);

    public String getTransition() {
        return transition;
    }

    public BSModalBorder getModalBorder() {
        return modalBorder;
    }
}