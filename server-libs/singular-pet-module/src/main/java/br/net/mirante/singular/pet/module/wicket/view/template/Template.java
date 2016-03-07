package br.net.mirante.singular.pet.module.wicket.view.template;

import static br.net.mirante.singular.util.wicket.util.WicketUtils.$b;
import static br.net.mirante.singular.util.wicket.util.WicketUtils.$m;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;

import br.net.mirante.singular.pet.module.wicket.PetModulePage;
import br.net.mirante.singular.pet.module.wicket.view.behavior.SingularJSBehavior;
import br.net.mirante.singular.pet.module.wicket.view.skin.SkinOptions;
import br.net.mirante.singular.pet.module.wicket.view.util.ToastrHelper;
import de.alpharogroup.wicket.js.addon.toastr.ToastrType;

public abstract class Template extends PetModulePage {

    protected SkinOptions option;
    private List<String> initializerJavascripts = Collections.singletonList("App.init();");

    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.option = SkinOptions.op();

        add(configurePageTitle("pageTitle"));
        add(new WebMarkupContainer("pageBody")
                .add($b.attrAppender("class", "page-full-width", " ", $m.ofValue(!withMenu()))));
//        queue(new HeaderResponseContainer("css", "css"));
        queue(new HeaderResponseContainer("scripts", "scripts"));
        queue(configureHeader("_Header"));
        if (withMenu()) {
            queue(configureMenu("_Menu"));
        } else {
            queue(new WebMarkupContainer("_Menu"));
        }
        queue(configureContent("_Content"));
        queue(new Footer("_Footer"));

        add(new SingularJSBehavior());
    }

    protected Menu configureMenu(String id) {
        return new Menu(id);
    }

    protected Header configureHeader(String id) {
        return new Header(id, withMenu(), withTopAction(), withSideBar(), option);
    }

    protected Label configurePageTitle(String id) {
        return new Label(id, new ResourceModel(getPageTitleLocalKey()));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptReferenceHeaderItem.forReference(new PackageResourceReference(Template.class, "singular.js")));
        response.render(CssHeaderItem.forReference(new PackageResourceReference(Template.class, "Template.css")));
        if (withSideBar()) {
            addQuickSidebar(response);
        }
        for (String script : initializerJavascripts) {
            response.render(OnDomReadyHeaderItem.forScript(script));
        }
        setSkin(response);
    }

    private void setSkin(IHeaderResponse response) {
        Optional<SkinOptions.Skin> skin = option.currentSkin();
        if (skin.isPresent()) {
            response.render(skin.get().ref);
        }
    }

    protected StringResourceModel getMessage(String prop) {
        return new StringResourceModel(prop.trim(), this, null);
    }

    protected boolean withTopAction() {
        return false;
    }

    protected boolean withSideBar() {
        return false;
    }

    protected boolean withMenu() {
        return true;
    }

    protected String getPageTitleLocalKey() {
        return "label.page.title.local";
    }

    protected abstract Content getContent(String id);

    private Content configureContent(String contentId) {
        if (withSideBar()) {
            return getContent(contentId).addSideBar();
        } else {
            return getContent(contentId);
        }
    }

    private void addQuickSidebar(IHeaderResponse response) {
        response.render(JavaScriptReferenceHeaderItem.forUrl("/singular-static/resources/metronic/layout4/scripts/quick-sidebar.js"));
        StringBuilder script = new StringBuilder();
        script.append("jQuery(document).ready(function () {\n")
                .append("    QuickSidebar.init(); // init quick sidebar\n")
                .append("});");
        response.render(OnDomReadyHeaderItem.forScript(script));
    }

    @Override
    public void onEvent(IEvent<?> event) {
        super.onEvent(event);
        Object payload = event.getPayload();
        if (payload instanceof AjaxRequestTarget) {
            AjaxRequestTarget target = (AjaxRequestTarget) payload;
            target.addListener(new AjaxRequestTarget.IListener() {
                @Override
                public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
                }

                @Override
                public void onAfterRespond(Map<String, Component> map, AjaxRequestTarget.IJavaScriptResponse response) {
                    if (!map.isEmpty()) {
                        initializerJavascripts.forEach(response::addJavaScript);
                    }
                }

                @Override
                public void updateAjaxAttributes(AbstractDefaultAjaxBehavior behavior, AjaxRequestAttributes attributes) {

                }
            });
        }
    }

    public void addToastrSuccessMessage(String messageKey, String... args) {
        new ToastrHelper(this).
                addToastrMessage(ToastrType.SUCCESS, messageKey, args);
    }

    public void addToastrErrorMessage(String messageKey, String... args) {
        new ToastrHelper(this).
                addToastrMessage(ToastrType.ERROR, messageKey, args);
    }

    public void addToastrWarningMessage(String messageKey, String... args) {
        new ToastrHelper(this).
                addToastrMessage(ToastrType.WARNING, messageKey, args);
    }

    public void addToastrInfoMessage(String messageKey, String... args) {
        new ToastrHelper(this).
                addToastrMessage(ToastrType.INFO, messageKey, args);
    }

    public void addToastrSuccessMessageWorklist(String messageKey, String... args) {
        new ToastrHelper(this).
                addToastrMessageWorklist(ToastrType.SUCCESS, messageKey, args);
    }

    public void addToastrErrorMessageWorklist(String messageKey, String... args) {
        new ToastrHelper(this).
                addToastrMessageWorklist(ToastrType.ERROR, messageKey, args);
    }

    public void addToastrWarningMessageWorklist(String messageKey, String... args) {
        new ToastrHelper(this).
                addToastrMessageWorklist(ToastrType.WARNING, messageKey, args);
    }

    protected void addToastrInfoMessageWorklist(String messageKey, String... args) {
        new ToastrHelper(this).
                addToastrMessageWorklist(ToastrType.INFO, messageKey, args);
    }

}
