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

package org.opensingular.requirement.module.wicket;

import org.apache.commons.collections4.MapUtils;
import org.opensingular.requirement.module.form.FormAction;
import org.opensingular.requirement.module.wicket.view.util.DispatcherPageUtil;

import java.util.Map;

import static org.opensingular.requirement.module.wicket.view.util.ActionContext.*;

/**
 * Responsible for generating a redirect link to a new requirement page.
 */
public class NewRequirementUrlBuilder {

    private final String baseURL;
    private final Long   requirementDefinitionId;

    public NewRequirementUrlBuilder(String baseURL, Long requirementDefinitionId) {
        this.baseURL = baseURL;
        this.requirementDefinitionId = requirementDefinitionId;
    }

    public String getURL(Map<String, String> params) {

        DispatcherPageUtil.DispatcherPageUrlAdditionalParamsBuilder builder = DispatcherPageUtil
                .baseURL(baseURL)
                .formAction(FormAction.FORM_FILL.getId())
                .requirementId(null)
                .param(REQUIREMENT_DEFINITION_ID, requirementDefinitionId)
                .params(params);
        return builder.build();
    }

    public String getURL() {
        return getURL(MapUtils.EMPTY_SORTED_MAP);
    }

}
