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

package org.opensingular.requirement.module.wicket.box;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class HelpFilterBoxPanel extends Panel {

    private String body;

    public HelpFilterBoxPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new Label("body", body).setEscapeModelStrings(false));
    }

    /**
     * Method responsible for configure the body of the help for filter.
     *
     * @param body The body text.
     * @return <code>this</code>
     */
    HelpFilterBoxPanel configureBody(String body) {
        if (StringUtils.isNotEmpty(body)) {
            this.body = body;
        } else {
            this.body = " <p>"
                    + "      A busca é feita apenas pelos campos presentes na tabela. <br>\n"
                    + "       Caso o termo de busca seja separado por espaço, a consulta realizará a busca por cada termo separado por espaço.\n"
                    + "       <br><i>Exemplo: João 10/10</i> Buscará todos os valores que tiverem João ou 10/10 \n"
                    + "   </p>\n"
                    + "   <p><b>Busca exata:</b>\n"
                    + "       É necessário utilizar o termo entre aspas duplas para realizar uma busca exata.\n"
                    + "       <br><i>Exemplo: \"termo exato\"</i>\n"
                    + "   </p>\n"
                    + "   <p style='margin-bottom:0px !important'>Obs:\n"
                    + "         Ao utilizar valores com máscara, é realizado uma busca com o mesmo valor sem a máscara.\n"
                    + "       <br><i>Exemplo: 000.000.000-00 </i>  Também buscará o valor 00000000000 \n"
                    + "   </p>";
        }
        return this;
    }


}
