package org.opensingular.server.commons.wicket.view.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.opensingular.form.SInstance;
import org.opensingular.form.wicket.component.SingularSaveButton;
import org.opensingular.lib.commons.extension.SingularExtensionUtil;
import org.opensingular.server.commons.service.PetitionInstance;
import org.opensingular.server.commons.wicket.view.extension.RequirementButtonExtension;

import java.util.List;

import static org.opensingular.lib.wicket.util.util.WicketUtils.$b;


public class ExtensionButtonsPanel<PI extends PetitionInstance> extends Panel {
    private IModel<PI>                       petInstanceModel;
    private IModel<? extends SInstance>      formModel;
    private List<RequirementButtonExtension> extensions;

    public ExtensionButtonsPanel(String id, IModel<PI> petInstanceModel, IModel<? extends SInstance> formModel) {
        super(id);
        this.petInstanceModel = petInstanceModel;
        this.formModel = formModel;
        this.extensions = lookupExtensions();
        addButtons();
    }

    private List<RequirementButtonExtension> lookupExtensions() {
        return SingularExtensionUtil.get().findExtensionsByClass(RequirementButtonExtension.class);
    }

    private void addButtons() {
        add(new ListView<RequirementButtonExtension>("buttons", extensions) {
            @Override
            protected void populateItem(ListItem<RequirementButtonExtension> item) {
                final RequirementButtonExtension itemModel = item.getModelObject();
                SingularSaveButton button = new SingularSaveButton("button", formModel, itemModel.shouldValidateForm()) {
                    @Override
                    protected void onValidationSuccess(AjaxRequestTarget target, Form<?> form, IModel<? extends SInstance> instanceModel) {
                        itemModel.onAction(new RequirementButtonExtension
                                .ActionContext(target, form, petInstanceModel.getObject(), instanceModel.getObject()));
                    }
                };
                RequirementButtonExtension.ButtonView buttonView = itemModel.getButtonView();
                WebMarkupContainer                    icon       = new WebMarkupContainer("icon");
                if (buttonView.getIcon() != null) {
                    icon.add($b.classAppender(buttonView.getIcon().getCssClass()));
                }
                else {
                    icon.setVisible(false);
                }
                button.add(icon);
                button.add(new Label("label", buttonView.getLabel()).setRenderBodyOnly(true));
                item.add(button);
            }
        }.setRenderBodyOnly(true));
    }
}