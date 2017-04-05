package org.opensingular.server.commons.wicket.view.util;


import org.junit.Assert;
import org.junit.Test;
import org.opensingular.server.commons.form.FormAction;

public class ActionContextTest {

    private ActionContext newActionContext() {
        ActionContext actionContext = new ActionContext();
        actionContext.setDiffEnabled(true);
        actionContext.setFormAction(FormAction.FORM_ANALYSIS);
        actionContext.setFormName("Nomezinho");
        actionContext.setFormVersionId(1l);
        actionContext.setMenuLabel("menuzinho");
        actionContext.setParam("nada", "outro");
        actionContext.setParentPetitionId(2l);
        actionContext.setPetitionId(3l);
        actionContext.setProcessGroupName("Tox");
        actionContext.setProcessInstanceId(4);
        actionContext.setSelectedMenuItem("menuzeira");
        return actionContext;
    }

    @Test
    public void testSetGet() {
        ActionContext actionContext = newActionContext();

        Assert.assertTrue(actionContext.getDiffEnabled());
        Assert.assertEquals(actionContext.getFormAction().get(), FormAction.FORM_ANALYSIS);
        Assert.assertEquals(actionContext.getFormName().get(), "Nomezinho");
        Assert.assertEquals((long) actionContext.getFormVersionId().get(), 1l);
        Assert.assertEquals(actionContext.getMenuLabel().get(), "menuzinho");
        Assert.assertEquals(actionContext.getParam("nada").get(), "outro");
        Assert.assertEquals((long) actionContext.getParentPetitionId().get(), 2l);
        Assert.assertEquals((long) actionContext.getPetitionId().get(), 3l);
        Assert.assertEquals(actionContext.getProcessGroupName().get(), "Tox");
        Assert.assertEquals((int) actionContext.getProcessInstanceId().get(), 4);
        Assert.assertEquals(actionContext.getSelectedMenuItem().get(), "menuzeira");
    }

    @Test
    public void testURL() {
        ActionContext actionContext = newActionContext();
        String        url           = actionContext.toURL();
        ActionContext otherContext  = new ActionContext(url);
        Assert.assertEquals(actionContext, otherContext);
    }


}
