package org.opensingular.server.commons.wicket;

import org.junit.Assert;
import org.junit.Test;
import org.opensingular.server.commons.wicket.builder.HTMLParameters;
import org.opensingular.server.commons.wicket.builder.MarkupCreator;

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
