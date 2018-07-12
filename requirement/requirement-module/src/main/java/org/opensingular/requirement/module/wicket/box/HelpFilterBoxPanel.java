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
