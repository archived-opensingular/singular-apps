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

package org.opensingular.server.commons.wicket;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.requirement.commons.wicket.builder.HTMLParameters;
import org.opensingular.requirement.commons.wicket.builder.MarkupCreator;

public class MarkupCreatorTest {

    @Test
    public void tagsTest(){
        Assert.assertEquals("<p wicket:id='wicketId' ></p>", MarkupCreator.p("wicketId"));
        Assert.assertEquals("<hr wicket:id='wicketId' ></hr>", MarkupCreator.hr("wicketId"));
        Assert.assertEquals("<span wicket:id='wicketId' ></span>", MarkupCreator.span("wicketId"));
        Assert.assertEquals("<h5 wicket:id='wicketId' ></h5>", MarkupCreator.h5("wicketId"));
        Assert.assertNotNull(new MarkupCreator());


        HTMLParameters parameters = new HTMLParameters();
        parameters.styleClass("col-md-12");
        parameters.add("style", "height:100%");
        Assert.assertFalse(parameters.getParametersMap().isEmpty());

        Assert.assertEquals("<h5 wicket:id='wicketId' style='height:100%' class='col-md-12' ></h5>",
                MarkupCreator.h5("wicketId", parameters));
    }
}
