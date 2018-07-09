package org.opensingular.requirement.module.wicket.box;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class HelpFilterBoxPanel extends Panel {

    private String title;
    private String body;

    public HelpFilterBoxPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new Label("title", title));
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
            this.body = " <p><b>Busca exata:</b>\n"
                    + "                    É necessário utilizar o termo entre aspas duplas para realizar uma busca exata.\n"
                    + "                    <br><i>Exemplo: \"termo exato\"</i>\n"
                    + "                </p>\n"
                    + "                <p><b>Caracteres especiais:</b>\n"
                    + "                    Ao utilizar caracteres especiais é criado um filtro extra sem a máscara.\n"
                    + "                    <br><i>Exemplo: 000.000.000-00 vai criar um filtro extra com o valor 00000000000 </i>\n"
                    + "                </p>";
        }
        return this;
    }

    /**
     * Method to be used for configure the title of the help for filter.
     *
     * @param title The title text.
     * @return <code>this</code>
     */
    HelpFilterBoxPanel configureTitle(String title) {
        if (StringUtils.isNotEmpty(title)) {
            this.title = title;
        } else {
            this.title = "Pesquisa Rápida";
        }
        return this;
    }

}
