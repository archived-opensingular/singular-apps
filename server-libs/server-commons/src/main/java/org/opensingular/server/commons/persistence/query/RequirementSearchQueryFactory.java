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

package org.opensingular.server.commons.persistence.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.opensingular.flow.core.TaskType;
import org.opensingular.lib.support.persistence.enums.SimNao;
import org.opensingular.server.commons.persistence.context.RequirementSearchAliases;
import org.opensingular.server.commons.persistence.context.RequirementSearchContext;
import org.opensingular.server.commons.persistence.filter.QuickFilter;
import org.opensingular.server.commons.query.QuickFilterBuilder;

import javax.annotation.Nonnull;

public class RequirementSearchQueryFactory {

    private static final String TO_CHAR_DATE = "TO_CHAR({0}, 'DD/MM/YYYY HH24:MI')";

    private final RequirementSearchContext ctx;

    private RequirementSearchAliases $;
    private RequirementSearchQuery query;
    private QuickFilter quickFilter;
    private BooleanBuilder whereClause;

    public RequirementSearchQueryFactory(RequirementSearchContext ctx) {
        this.ctx = ctx;
    }

    public RequirementSearchQuery make(Session session) {
        configure(session);
        appendSelect();
        appendWhere();
        applyExtenders();
        appendOrder();
        return query;
    }

    private void configure(Session session) {
        query = new RequirementSearchQuery(session);
        $ = ctx.getAliases();
        quickFilter = ctx.getQuickFilter();
        whereClause = query.getWhereClause();
        ctx.setQuery(query);
    }

    private void appendSelect() {
        if (Boolean.TRUE.equals(ctx.getCount())) {
            query.countBy($.requirement);
        } else {
            query.getSelect()
                    .add($.requirement.cod.as("codRequirement"))
                    .add($.requirement.description.as("description"))
                    .add($.taskVersion.name.as("situation"))
                    .add($.taskVersion.name.as("taskName"))
                    .add($.taskVersion.type.as("taskType"))
                    .add($.flowDefinitionEntity.name.as("processName"))
                    .addCase($case -> $case
                            .when($.currentFormDraftVersionEntity.isNull())
                            .then($.currentFormVersion.inclusionDate)
                            .otherwise($.currentFormDraftVersionEntity.inclusionDate)
                            .as("creationDate"))
                    .addCase($case -> $case
                            .when($.formType.abbreviation.isNull())
                            .then($.formDraftType.abbreviation)
                            .otherwise($.formType.abbreviation)
                            .as("type"))
                    .add($.flowDefinitionEntity.key.as("processType"))
                    .add($.task.beginDate.as("situationBeginDate"))
                    .add($.task.cod.as("taskInstanceId"))
                    .add($.flowInstance.beginDate.as("processBeginDate"))
                    .add($.currentDraftEntity.editionDate.as("editionDate"))
                    .add($.flowInstance.cod.as("flowInstanceId"))
                    .add($.requirement.rootRequirement.cod.as("rootRequirement"))
                    .add($.requirement.parentRequirement.cod.as("parentRequirement"))
                    .add($.taskDefinition.cod.as("taskId"))
                    .add($.task.versionStamp.as("versionStamp"))
                    .add($.allocatedUser.codUsuario.as("codUsuarioAlocado"))
                    .add($.allocatedUser.nome.as("nomeUsuarioAlocado"))
                    .add($.module.cod.as("moduleCod"))
                    .add($.module.connectionURL.as("moduleContext"))
                    .add($.requirementDefinition.cod.as("requirementDefinitionId"));
        }

        query
                .from($.requirement)
                .leftJoin($.requirement.applicant, $.applicantEntity)
                .leftJoin($.requirement.flowInstanceEntity, $.flowInstance)
                .leftJoin($.requirement.formRequirementEntities, $.formRequirementEntity).on($.formRequirementEntity.mainForm.eq(SimNao.SIM))
                .leftJoin($.formRequirementEntity.form, $.formEntity)
                .leftJoin($.formRequirementEntity.currentDraftEntity, $.currentDraftEntity)
                .leftJoin($.currentDraftEntity.form, $.formDraftEntity)
                .leftJoin($.formDraftEntity.currentFormVersionEntity, $.currentFormDraftVersionEntity)
                .leftJoin($.formEntity.currentFormVersionEntity, $.currentFormVersion)
                .leftJoin($.requirement.flowDefinitionEntity, $.flowDefinitionEntity)
                .leftJoin($.formEntity.formType, $.formType)
                .leftJoin($.formDraftEntity.formType, $.formDraftType)
                .leftJoin($.flowInstance.tasks, $.task)
                .leftJoin($.task.task, $.taskVersion)
                .leftJoin($.taskVersion.taskDefinition, $.taskDefinition)
                .leftJoin($.task.allocatedUser, $.allocatedUser)
                .leftJoin($.requirement.requirementDefinitionEntity, $.requirementDefinition)
                .leftJoin($.requirementDefinition.module, $.module);
    }

    @Nonnull
    private void appendWhere() {
        appendFilterByApplicant();
        appendFilterByFlowDefinitionAbbreviation();
        appendFilterByQuickFilter();
        appendFilterByTasks();
        if (quickFilter.isRascunho()) {
            appendFilterByRequirementsWithoutFlowInstance();
        } else {
            appendFilterByRequirementsWithFlowInstance();
            appendFilterByCurrentTask();
        }
    }

    private void appendFilterByCurrentTask() {
        if (quickFilter.getEndedTasks() == null) {
            whereClause.and($.taskVersion.type.eq(TaskType.END).or($.taskVersion.type.ne(TaskType.END).and($.task.endDate.isNull())));
        } else if (Boolean.TRUE.equals(quickFilter.getEndedTasks())) {
            whereClause.and($.taskVersion.type.eq(TaskType.END));
        } else {
            whereClause.and($.task.endDate.isNull());
        }
    }

    private void appendFilterByRequirementsWithFlowInstance() {
        whereClause.and($.requirement.flowInstanceEntity.isNotNull());
    }

    private void appendFilterByRequirementsWithoutFlowInstance() {
        whereClause.and($.requirement.flowInstanceEntity.isNull());
    }

    private void appendFilterByTasks() {
        if (!CollectionUtils.isEmpty(quickFilter.getTasks())) {
            whereClause.and($.taskVersion.name.in(quickFilter.getTasks()));
        }
    }

    private void appendFilterByQuickFilter() {
        if (ctx.getQuickFilter().hasFilter()) {

            QuickFilterBuilder quickFilterBuilder = query.getQuickFilter();
            quickFilterBuilder.addTokens(quickFilter.listFilterTokens());

            quickFilterBuilder.registerFieldFilter("nomeUsuarioAlocado", $.allocatedUser.nome::likeIgnoreCase);
            quickFilterBuilder.registerFieldFilter("description", $.requirement.description::likeIgnoreCase);
            quickFilterBuilder.registerFieldFilter("processName", $.flowDefinitionEntity.name::likeIgnoreCase);
            quickFilterBuilder.registerFieldFilter("situation", $.taskVersion.name::likeIgnoreCase);
            quickFilterBuilder.registerFieldFilter("taskName", $.taskVersion.name::likeIgnoreCase);
            quickFilterBuilder.registerFieldFilter("codRequirement", $.requirement.cod::like);
            quickFilterBuilder.registerFieldFilter("creationDate", toCharDate($.currentFormVersion.inclusionDate)::like);
            quickFilterBuilder.registerFieldFilter("creationDate", toCharDate($.currentFormDraftVersionEntity.inclusionDate)::like);
            quickFilterBuilder.registerFieldFilter("editionDate", toCharDate($.currentDraftEntity.editionDate)::like);
            quickFilterBuilder.registerFieldFilter("situationBeginDate", toCharDate($.task.beginDate)::like);
            quickFilterBuilder.registerFieldFilter("processBeginDate", toCharDate($.flowInstance.beginDate)::like);

        }
    }

    private void appendFilterByApplicant() {
        if (quickFilter.getIdPessoa() != null) {
            whereClause.and($.applicantEntity.idPessoa.eq(quickFilter.getIdPessoa()));
        }
    }

    private void appendFilterByFlowDefinitionAbbreviation() {
        if (!quickFilter.isRascunho()
                && quickFilter.getProcessesAbbreviation() != null
                && !quickFilter.getProcessesAbbreviation().isEmpty()) {
            BooleanExpression expr = $.flowDefinitionEntity.key.in(quickFilter.getProcessesAbbreviation());
            if (quickFilter.getTypesNames() != null && !quickFilter.getTypesNames().isEmpty()) {
                expr = expr.or($.formType.abbreviation.in(quickFilter.getTypesNames()));
            }
            whereClause.and(expr);
        }
    }

    private void applyExtenders() {
        if (ctx.getExtenders() != null) {
            ctx.getExtenders().forEach(extender -> extender.extend(ctx));
        }
    }

    private void appendOrder() {
        if (quickFilter.getSortProperty() != null) {
            Order order = quickFilter.isAscending() ? Order.ASC : Order.DESC;
            query.orderBy(new OrderSpecifier<>(order, Expressions.stringPath(quickFilter.getSortProperty())));
        } else if (!Boolean.TRUE.equals(ctx.getCount())) {
            if (quickFilter.isRascunho()) {
                query.orderBy(new OrderSpecifier<>(Order.ASC, Expressions.stringPath("creationDate")));
            } else {
                query.orderBy(new OrderSpecifier<>(Order.ASC, Expressions.stringPath("processBeginDate")));
            }
        }
    }

    @Nonnull
    private StringTemplate toCharDate(Path<?> path) {
        return Expressions.stringTemplate(TO_CHAR_DATE, path);
    }

}
