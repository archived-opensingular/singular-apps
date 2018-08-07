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

package org.opensingular.studio.core.menu;

import org.opensingular.lib.commons.lambda.IPredicate;
import org.opensingular.lib.commons.ui.Icon;

public abstract class AbstractMenuEntry implements MenuEntry {

    private Icon                  icon;
    private String                name;
    private MenuEntry             parent;
    private IPredicate<MenuEntry> visibilityFunction;

    public AbstractMenuEntry(Icon icon, String name, IPredicate<MenuEntry> visibilityFunction) {
        this.icon = icon;
        this.name = name;
        this.visibilityFunction = visibilityFunction;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MenuEntry getParent() {
        return parent;
    }

    @Override
    public void setParent(MenuEntry parent) {
        this.parent = parent;
    }

    @Override
    public boolean isVisible() {
        return visibilityFunction == null || visibilityFunction.test(this);
    }
}