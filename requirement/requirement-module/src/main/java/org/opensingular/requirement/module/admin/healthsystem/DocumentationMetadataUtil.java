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

package org.opensingular.requirement.module.admin.healthsystem;

import org.opensingular.form.AtrRef;
import org.opensingular.form.SAttributeUtil;
import org.opensingular.form.SFormUtil;
import org.opensingular.form.SType;
import org.opensingular.form.type.basic.AtrBasic;
import org.opensingular.form.type.basic.SPackageBasic;
import org.opensingular.form.type.core.SPackageDocumentation;
import org.opensingular.requirement.module.exception.SingularRequirementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DocumentationMetadataUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentationMetadataUtil.class.getName());

    /**
     * Fields that are hard-coded invisible, or do not exists or explicit marked as hidden for documentation should not appear in the documentation as it is not related to anything functional
     * for users.
     */
    public static boolean isHiddenForDocumentation(SType<?> sType) {
        boolean visible = getAttribute(sType, SPackageBasic.ATR_EXISTS).orElse(Boolean.TRUE);
        visible &= getAttribute(sType, SPackageBasic.ATR_VISIBLE).orElse(Boolean.TRUE);
        visible &= !getAttribute(sType, SPackageDocumentation.ATR_DOC_HIDDEN).orElse(Boolean.FALSE);
        return !visible;
    }

    public static <V> Optional<V> getAttribute(SType<?> type, AtrRef<?, ?, V> ref) {
        if (SAttributeUtil.hasAttributeDefinitionInHierarchy(type, ref)) {
            return Optional.ofNullable(type.getAttributeValue(ref));
        }
        return Optional.empty();
    }

    @SuppressWarnings("CheckReturnValue")
    public static TreeSet<String> resolveDependsOn(SType<?> rootType, SType<?> type) {
        TreeSet<String> values = new TreeSet<>();
        Collection<AtrBasic.DelayedDependsOnResolver> dependOnResolverList = getAttribute(type, SPackageBasic.ATR_DEPENDS_ON_FUNCTION).map(Supplier::get).orElse(Collections.emptyList());
        for (AtrBasic.DelayedDependsOnResolver func : dependOnResolverList) {
            if (func != null) {
                try {
                    func.resolve(rootType, type).stream().map(DocumentationMetadataUtil::getLabelForType).collect(
                            Collectors.toCollection(() -> values));
                } catch (Exception e) {
                    throw new SingularRequirementException(
                            "Could not resolve dependent types for type: {}" + type.getName(), e);
                }
            }
        }
        return values;
    }

    public static String getLabelForType(SType<?> type) {
        return getLabelForType(null, type);
    }

    public static String getLabelForType(String defaultString, SType<?> type) {
        if (defaultString == null) {
            String label = type.asAtr().getLabel();
            if (label == null) {
                label = SFormUtil.getTypeLabel((Class<? extends SType<?>>) type.getClass()).orElse(null);
                if (label == null) {
                    label = type.getNameSimple();
                }
            }
            return label;
        }
        return defaultString;
    }
}
