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

package org.opensingular.requirement.commons.wicket.view.util;


import org.junit.Assert;
import org.junit.Test;
import org.opensingular.requirement.module.form.FormAction;
import org.opensingular.requirement.module.wicket.view.util.ActionContext;

public class ActionContextTest {

    private ActionContext newActionContext() {
        ActionContext actionContext = new ActionContext();
        actionContext.setDiffEnabled(true);
        actionContext.setFormAction(FormAction.FORM_ANALYSIS);
        actionContext.setFormName("Nomezinho");
        actionContext.setFormVersionId(1l);
        actionContext.setParam("nada", "outro");
        actionContext.setParentRequirementId(2l);
        actionContext.setRequirementId(3l);
        actionContext.setFlowInstanceId(4);
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
        Assert.assertEquals(actionContext.getParam("nada").get(), "outro");
        Assert.assertEquals((long) actionContext.getParentRequirementId().get(), 2l);
        Assert.assertEquals((long) actionContext.getRequirementId().get(), 3l);
        Assert.assertEquals((int) actionContext.getFlowInstanceId().get(), 4);
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
