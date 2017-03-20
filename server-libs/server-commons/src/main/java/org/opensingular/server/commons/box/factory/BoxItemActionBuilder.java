package org.opensingular.server.commons.box.factory;


import org.opensingular.server.commons.box.ItemBoxData;
import org.opensingular.server.commons.form.FormAction;
import org.opensingular.server.commons.service.dto.BoxItemAction;
import org.opensingular.server.commons.wicket.view.util.DispatcherPageUtil;

import static org.opensingular.server.commons.util.DispatcherPageParameters.FORM_NAME;

public class BoxItemActionBuilder {


    public PopupItemActionBuilder newPopupBox(ItemBoxData boxData) {
        return new PopupItemActionBuilder(boxData);
    }


    public static class PopupItemActionBuilder {

        private ItemBoxData boxData;
        private FormAction formAction;
        private String action;

        public PopupItemActionBuilder(ItemBoxData boxData) {
            this.boxData = boxData;
        }

        public PopupItemActionBuilder setFormAction(FormAction formAction) {
            this.formAction = formAction;
            return this;
        }

        public PopupItemActionBuilder setBoxData(ItemBoxData boxData) {
            this.boxData = boxData;
            return this;
        }

        public PopupItemActionBuilder setAction(String action) {
            this.action = action;
            return this;
        }

        public BoxItemAction build(){
            String endpoint = DispatcherPageUtil
                    .baseURL("")
                    .formAction(formAction.getId())
                    .petitionId(boxData.getPetitionCod())
                    .param(FORM_NAME, boxData.getType())
                    .build();

            final BoxItemAction boxItemAction = new BoxItemAction();
            boxItemAction.setName(action);
            boxItemAction.setEndpoint(endpoint);
            boxItemAction.setFormAction(formAction);

            return boxItemAction;
        }

    }

}