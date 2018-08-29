/*
 * Copyright (C) 2016 Singular Studios (a.k.a Atom Tecnologia) - www.opensingular.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensingular.requirement.module.admin.healthsystem.stypes;

import org.opensingular.form.SIComposite;

public class SIPdfHealth extends SIComposite {

    public String getEndpoint() {
        return getField(getType().endpoint).getValue();
    }

    public String getHtmlToExport() {
        return getField(getType().htmlToExport).getValue();
    }

    @Override
    public STypePdfHealth getType() {
        return (STypePdfHealth) super.getType();
    }
}
