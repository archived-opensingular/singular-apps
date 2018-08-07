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

package org.opensingular.requirement.module.service;


import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.opensingular.flow.core.TaskInstance;
import org.opensingular.flow.core.entity.IEntityTaskDefinition;
import org.opensingular.flow.core.service.IUserService;
import org.opensingular.flow.persistence.entity.TaskDefinitionEntity;
import org.opensingular.flow.persistence.entity.TaskInstanceEntity;
import org.opensingular.form.SIComposite;
import org.opensingular.form.SIList;
import org.opensingular.form.SInstance;
import org.opensingular.form.SType;
import org.opensingular.form.SingularFormException;
import org.opensingular.form.context.SFormConfig;
import org.opensingular.form.document.RefType;
import org.opensingular.form.document.SDocument;
import org.opensingular.form.document.SDocumentConsumer;
import org.opensingular.form.document.SDocumentFactory;
import org.opensingular.form.io.SFormXMLUtil;
import org.opensingular.form.persistence.FormKey;
import org.opensingular.form.persistence.dao.FormAnnotationDAO;
import org.opensingular.form.persistence.dao.FormAnnotationVersionDAO;
import org.opensingular.form.persistence.dao.FormAttachmentDAO;
import org.opensingular.form.persistence.dao.FormDAO;
import org.opensingular.form.persistence.dao.FormVersionDAO;
import org.opensingular.form.persistence.entity.FormAnnotationEntity;
import org.opensingular.form.persistence.entity.FormAnnotationVersionEntity;
import org.opensingular.form.persistence.entity.FormAttachmentEntity;
import org.opensingular.form.persistence.entity.FormEntity;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.form.service.FormFieldService;
import org.opensingular.form.service.IFormService;
import org.opensingular.form.util.transformer.Value;
import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.support.persistence.enums.SimNao;
import org.opensingular.requirement.module.exception.SingularServerException;
import org.opensingular.requirement.module.persistence.dao.form.DraftDAO;
import org.opensingular.requirement.module.persistence.dao.form.FormRequirementDAO;
import org.opensingular.requirement.module.persistence.entity.form.DraftEntity;
import org.opensingular.requirement.module.persistence.entity.form.FormRequirementEntity;
import org.opensingular.requirement.module.persistence.entity.form.FormVersionHistoryEntity;
import org.opensingular.requirement.module.persistence.entity.form.RequirementContentHistoryEntity;
import org.opensingular.requirement.module.persistence.entity.form.RequirementEntity;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class FormRequirementService<P extends RequirementEntity> {

    @Inject
    protected IFormService formPersistenceService;

    @Inject
    private FormFieldService formFieldService;

    @Inject
    private IUserService userService;

    @Inject
    protected FormRequirementDAO formRequirementDAO;

    @Inject
    protected DraftDAO draftDAO;

    @Inject
    protected FormDAO formDAO;

    @Inject
    protected FormVersionDAO formVersionDAO;

    @Inject
    protected FormAnnotationDAO formAnnotationDAO;

    @Inject
    protected FormAnnotationVersionDAO formAnnotationVersionDAO;

    @Inject
    private FormAttachmentDAO formAttachmentDAO;

    @Inject
    @Named("formConfigWithDatabase")
    private Optional<SFormConfig<String>> singularFormConfig;
    //TODO (Daniel) Tirar o optional a cima assim que o teste for ajustado

    @Nonnull
    public FormRequirementEntity findFormRequirementEntityByCod(@Nonnull Long cod) {
        return formRequirementDAO.findOrException(cod);
    }

    @Nonnull
    public Optional<FormRequirementEntity> findFormRequirementEntityByType(@Nonnull RequirementInstance requirement,
            @Nonnull String typeName) {
        Objects.requireNonNull(requirement);
        //TODO vericiar se esse método não deveria ser unificado com findLastFormRequirementEntityByType()
        //Aparentemente geram o mesmo resultado na DAO
        return formRequirementDAO.findFormRequirementEntityByTypeName(requirement.getCod(), typeName);
    }

    /** Na petição, encontra o formulário mais recente do tipo indicado e associado a tarefa indicada. */
    @Nonnull
    public Optional<FormRequirementEntity> findFormRequirementEntityByTypeAndTask(@Nonnull RequirementInstance requirement,
            @Nonnull Class<? extends SType<?>> typeClass, @Nonnull TaskInstance task) {
        return findFormRequirementEntityByTypeAndTask(requirement, RequirementUtil.getTypeName(typeClass), task);
    }

    /** Na petição, encontra o formulário mais recente do tipo indicado e associado a tarefa indicada. */
    @Nonnull
    public Optional<FormRequirementEntity> findFormRequirementEntityByTypeAndTask(@Nonnull RequirementInstance requirement,
            @Nonnull String typeName, @Nonnull TaskInstance task) {
        Objects.requireNonNull(requirement);
        Objects.requireNonNull(task);
        Integer taskDefinitionEntityPK =
                ((TaskInstanceEntity) task.getEntityTaskInstance()).getTaskVersion().getTaskDefinition().getCod();
        return formRequirementDAO.findFormRequirementEntityByTypeNameAndTask(requirement.getCod(), typeName, taskDefinitionEntityPK);
    }

    /** Na petição, encontra o formulário mais recente do tipo indicado. */
    @Nonnull
    public Optional<FormRequirementEntity> findLastFormRequirementEntityByType(@Nonnull RequirementInstance requirement,
            @Nonnull Class<? extends SType<?>> typeClass) {
        return findLastFormRequirementEntityByType(requirement, RequirementUtil.getTypeName(typeClass));
    }

    /** Na petição, encontra o formulário mais recente do tipo indicado. */
    public Optional<FormRequirementEntity> findLastFormRequirementEntityByType(@Nonnull RequirementInstance requirement, @Nonnull String typeName) {
        return formRequirementDAO.findLastFormRequirementEntityByTypeName(requirement.getCod(), typeName);
    }

    @Nonnull
    public Optional<FormRequirementEntity> findFormRequirementEntity(@Nonnull RequirementInstance requirement,
            @Nonnull Class<? extends SType<?>> typeClass, boolean mainForm) {
        return findFormRequirementEntity(requirement, RequirementUtil.getTypeName(typeClass), mainForm);
    }

    @Nonnull
    private static Optional<FormRequirementEntity> findFormRequirementEntity(@Nonnull RequirementInstance requirement,
            @Nonnull String typeName, boolean mainForm) {
        Stream<FormRequirementEntity> entities = requirement.getEntity().getFormRequirementEntities().stream();

        //Filter byName
        entities = entities.filter(x -> {
            if (x.getForm() != null) {
                return typeName.equals(RequirementUtil.getTypeName(x));
            } else if (x.getCurrentDraftEntity() != null) {
                return typeName.equals(RequirementUtil.getTypeName(x.getCurrentDraftEntity()));
            }
            return false;
        });

        if (!mainForm) {
            //Filter byTask
            Optional<IEntityTaskDefinition> currentTask = RequirementUtil.getCurrentTaskDefinitionOpt(requirement.getEntity());
            if (currentTask.isPresent()) {
                entities = entities.filter(x -> x.getTaskDefinitionEntity().equals(currentTask.get()));
            }
        }
        return entities.findFirst();
    }

    /** Procura na petição a versão mais recente do formulário do tipo informado. */
    @Nonnull
    public Optional<SInstance> findFormRequirementInstanceByTypeAndTask(@Nonnull RequirementInstance requirement,
            @Nonnull Class<? extends SType<? extends SInstance>> typeClass, @Nonnull TaskInstance task) {
        return findFormRequirementEntityByTypeAndTask(requirement, RequirementUtil.getTypeName(typeClass), task)
                .map(e -> getSInstance(e, typeClass));
    }

    /** Procura na petição a versão mais recente do formulário do tipo informado. */
    @Nonnull
    public <I extends SInstance, K extends SType<? extends I>> Optional<I> findLastFormRequirementInstanceByType(
            @Nonnull RequirementInstance requirement, @Nonnull Class<K> typeClass) {
        return findLastFormRequirementEntityByType(requirement, RequirementUtil.getTypeName(typeClass))
                .map(e -> getSInstance(e, typeClass));
    }

    /** Verifia se a instância informada é do tipo esperado e faz cast para a instância associada ao tipo. */
    @Nonnull
    static <I extends SInstance, K extends SType<? extends I>> I checkIfExpectedType(@Nonnull SInstance instance,
            @Nonnull Class<K> expectedTypeClass) {
        if (instance == null) {
            throw SingularServerException.rethrow("O resultado da recuperação da SInstance retornou null");
        } else if (! expectedTypeClass.isAssignableFrom(instance.getType().getClass())) {
            throw new SingularFormException(
                    "Era esperado a instância recuperada fosse do tipo " + expectedTypeClass.getName() +
                            " mas ela é do tipo " + instance.getType().getClass(), instance);
        }
        return (I) instance;
    }


    /**
     * Recupera a {@link SInstance} associada ao {@link FormRequirementEntity} informado e verifica se é do tipo da
     * classe esperada. Não sendo do tipo esperado, dispara exception.
     * @return Retorna a {@link SInstance} específica associada ao tipo informado.
     */
    @Nonnull
    public <I extends SInstance, K extends SType<? extends I>> I getSInstance(@Nonnull FormRequirementEntity f,
            @Nonnull Class<K> expectedTypeClass) {
        return checkIfExpectedType(getSInstance(f), expectedTypeClass);
    }

    /** Recupera a {@link SInstance} associada ao {@link FormVersionEntity} informado. */
    @Nonnull
    public SInstance getSInstance(@Nonnull FormRequirementEntity f) {
        return getSInstance(f.getForm());
    }

    /**
     * Recupera a {@link SInstance} associada ao {@link FormVersionEntity} informado e verifica se é do tipo da
     * classe esperada. Não sendo do tipo esperado, dispara exception.
     * @return Retorna a {@link SInstance} específica associada ao tipo informado.
     */
    @Nonnull
    public <I extends SInstance, K extends SType<? extends I>> I getSInstance(@Nonnull FormVersionEntity version,
            Class<K> expectedTypeClass) {
        return checkIfExpectedType(getSInstance(version), expectedTypeClass);
    }

    /** Recupera a {@link SInstance} associada ao {@link FormVersionEntity} informado. */
    @Nonnull
    public SInstance getSInstance(@Nonnull FormVersionEntity version) {
        FormEntity formEntity = version.getFormEntity();
        FormKey formKey = formKeyFromFormEntity(formEntity);
        RefType refType = loadRefType(RequirementUtil.getTypeName(formEntity));
        return formPersistenceService.loadSInstance(formKey, refType, getFactory(null), version.getCod());
    }

    /**
     * Recupera a {@link SInstance} associada ao {@link FormEntity} informado e verifica se é do tipo da classe
     * esperada. Não sendo do tipo esperado, dispara exception.
     * @return Retorna a {@link SInstance} específica associada ao tipo informado.
     */
    @Nonnull
    public <I extends SInstance, K extends SType<? extends I>> I getSInstance(@Nonnull FormEntity f,
            @Nonnull Class<K> expectedTypeClass) {
        return checkIfExpectedType(getSInstance(f), expectedTypeClass);
    }

    /** Recupera a {@link SInstance} associada a {@link FormEntity} informada.*/
    @Nonnull
    public SInstance getSInstance(@Nonnull FormEntity f) {
        FormKey formKey = formKeyFromFormEntity(f);
        RefType refType = loadRefType(RequirementUtil.getTypeName(f));
        return getSInstance(formKey, refType, null);
    }

    /** Recupera a Instância associada a chave de formulário informada. */
    @Nonnull
    public SInstance getSInstance(@Nonnull FormKey formKey, @Nonnull RefType refType) {
        return getSInstance(formKey, refType, null);
    }

    /**
     * Recupera a {@link SInstance} associada a chave de formulário informada.
     * @param extraFactorySetupSteps (opcional) Informados passos extras de configuração da nova instância (veja
     * {@link SDocumentFactory#extendAddingSetupStep(IConsumer)}).
     */
    @Nonnull
    public SInstance getSInstance(@Nonnull FormKey formKey, @Nonnull RefType refType,
            @Nullable SDocumentConsumer extraFactorySetupSteps) {
        return formPersistenceService.loadSInstance(formKey, refType, getFactory(extraFactorySetupSteps));
    }

    @Nonnull
    public List<FormVersionEntity> findTwoLastFormVersions(@Nonnull Long codForm) {
        return formRequirementDAO.findTwoLastFormVersions(Objects.requireNonNull(codForm));
    }

    @Nonnull
    public Long countVersions(@Nonnull Long codForm) {
        return formRequirementDAO.countVersions(Objects.requireNonNull(codForm));
    }

    @Nonnull
    public FormKey saveFormRequirement(@Nonnull RequirementInstance requirement, @Nonnull SInstance instance, boolean mainForm) {

        Optional<FormRequirementEntity> formRequirementEntity;

        Integer codActor = userService.getUserCodIfAvailable();
        formRequirementEntity = findFormRequirementEntity(requirement, instance.getType().getName(), mainForm);

        FormRequirementEntity fpe;
        if (! formRequirementEntity.isPresent()) {
            fpe = newFormRequirementEntity(requirement.getEntity(), mainForm);
            requirement.getEntity().getFormRequirementEntities().add(fpe);
        } else {
            fpe = formRequirementEntity.get();
        }

        DraftEntity currentDraftEntity = fpe.getCurrentDraftEntity();
        if (currentDraftEntity == null) {
            currentDraftEntity = createNewDraftWithoutSave();
        }

        saveOrUpdateDraft(instance, currentDraftEntity, codActor);
        fpe.setCurrentDraftEntity(currentDraftEntity);
        formRequirementDAO.saveOrUpdate(fpe);
        return formKeyFromFormEntity(currentDraftEntity.getForm());
    }

    @Nonnull
    private FormRequirementEntity newFormRequirementEntity(RequirementEntity requirement, boolean mainForm) {
        FormRequirementEntity formRequirementEntity = new FormRequirementEntity();
        formRequirementEntity.setRequirement(requirement);
        if (mainForm) {
            formRequirementEntity.setMainForm(SimNao.SIM);
        } else {
            formRequirementEntity.setMainForm(SimNao.NAO);
            formRequirementEntity.setTaskDefinitionEntity((TaskDefinitionEntity) RequirementUtil.getCurrentTaskDefinitionOpt(requirement).orElse(null));
        }
        formRequirementDAO.saveOrUpdate(formRequirementEntity);
        return formRequirementEntity;
    }

    private DraftEntity saveOrUpdateDraft(SInstance instance, DraftEntity draftEntity, Integer actor) {

        SInstance draft;

        if (draftEntity.getForm() != null) {
            draft = getSInstance(draftEntity.getForm());
        } else {
            draft = createInstance(loadRefType(instance.getType().getName()), null, false);
        }

        copyValuesAndAnnotations(instance.getDocument(), draft.getDocument());

        draftEntity.setForm(formPersistenceService.loadFormEntity(formPersistenceService.insertOrUpdate(draft, actor)));
        draftEntity.setEditionDate(new Date());

        //atualiza a instancia com a key salva
        copyFormKey(draft, instance);

        draftDAO.saveOrUpdate(draftEntity);

        draft.getDocument().persistFiles();

        return draftEntity;
    }

    private static void copyFormKey(@Nonnull SInstance origen, @Nonnull SInstance destiny) {
        FormKey.fromOpt(origen).ifPresent(key -> FormKey.set(destiny, key));
    }

    private DraftEntity createNewDraftWithoutSave() {
        final DraftEntity draftEntity = new DraftEntity();
        draftEntity.setStartDate(new Date());
        draftEntity.setEditionDate(new Date());
        return draftEntity;
    }

    /**
     * Consolida todos os rascunhos da petição
     * @return as novas entidades criadas
     */
    @Nonnull
    public List<FormEntity> consolidateDrafts(@Nonnull RequirementInstance requirement) {
        return requirement.getEntity().getFormRequirementEntities()
                .stream()
                .filter(formRequirementEntity -> formRequirementEntity.getCurrentDraftEntity() != null)
                .map(this::consolidadeDraft)
                .collect(Collectors.toList());
    }

    /**
     * Consolida o rascunho, copiando os valores do rascunho para o form principal criando versão inicial ou gerando nova versão
     * @return a nova versão criada
     */
    private FormEntity consolidadeDraft(FormRequirementEntity formRequirementEntity) {

        final DraftEntity draft;
        final SInstance draftInstance;
        final SInstance formInstance;
        final boolean     isFirstVersion;
        final FormKey     key;
        final Integer     userCod;

        draft = draftDAO.getOrException(formRequirementEntity.getCurrentDraftEntity().getCod());

        draftInstance = getSInstance(draft.getForm());

        isFirstVersion = formRequirementEntity.getForm() == null;
        if (isFirstVersion) {
            formInstance = createInstance(loadRefType(RequirementUtil.getTypeName(draft)), null, false);
        } else {
            formInstance = getSInstance(formRequirementEntity.getForm());
        }

        userCod = userService.getUserCodIfAvailable();

        //cria a versao antes de copiar os valores
        if (isFirstVersion) {
            key = formPersistenceService.insert(formInstance, userCod);
        } else {
            key = formPersistenceService.newVersion(formInstance, userCod);
        }

        copyValuesAndAnnotations(draftInstance.getDocument(), formInstance.getDocument());

        formPersistenceService.update(formInstance, userCod);

        FormEntity formEntity = formPersistenceService.loadFormEntity(key);
        formRequirementEntity.setForm(formEntity);
        formRequirementEntity.setCurrentDraftEntity(null);

        formInstance.getDocument().persistFiles();

        deassociateFormVersions(draft.getForm());
        draftDAO.delete(draft);
        formRequirementDAO.save(formRequirementEntity);

        formFieldService.saveFields(formInstance, formEntity.getFormType(), formEntity.getCurrentFormVersionEntity());
        return formRequirementEntity.getForm();
    }


    /**
     * Deletes all form versions associated with the given @param form.
     * It also delete all annotations and annotations versions associated with each version.
     */
    public void deassociateFormVersions(FormEntity form) {
        if (form != null) {
            form.setCurrentFormVersionEntity(null);
            formDAO.saveOrUpdate(form);
            List<FormVersionEntity> fves = formVersionDAO.findVersions(form);
            if (!CollectionUtils.isEmpty(fves)) {
                Iterator<FormVersionEntity> it = fves.iterator();
                while (it.hasNext()) {
                    FormVersionEntity fve = it.next();
                    deleteFormVersion(fve);
                }
            }
        }
    }

    private void deleteFormVersion(FormVersionEntity fve) {
        if (fve != null) {
            if (!CollectionUtils.isEmpty(fve.getFormAnnotations())) {
                Iterator<FormAnnotationEntity> it = fve.getFormAnnotations().iterator();
                while (it.hasNext()) {
                    FormAnnotationEntity fae = it.next();
                    deleteAnnotation(fae);
                    it.remove();
                }
            }
            formAttachmentDAO.findFormAttachmentByFormVersionCod(fve.getCod())
                    .forEach(this::deleteFormAttachmentEntity);
            formVersionDAO.delete(fve);
        }
    }

    private void deleteFormAttachmentEntity(FormAttachmentEntity fae) {
        formAttachmentDAO.delete(fae);
    }

    private void deleteAnnotation(FormAnnotationEntity fae) {
        if (fae != null) {
            FormAnnotationVersionEntity formAnnotationVersionEntity = fae.getAnnotationCurrentVersion();
            fae.setAnnotationCurrentVersion(null);
            formAnnotationDAO.saveOrUpdate(fae);
            deleteAnnotationVersion(formAnnotationVersionEntity);
            if (!CollectionUtils.isEmpty(fae.getAnnotationVersions())) {
                Iterator<FormAnnotationVersionEntity> it = fae.getAnnotationVersions().iterator();
                while (it.hasNext()) {
                    FormAnnotationVersionEntity fave = it.next();
                    deleteAnnotationVersion(fave);
                    it.remove();
                }
            }
            formAnnotationDAO.delete(fae);
        }
    }

    private void deleteAnnotationVersion(FormAnnotationVersionEntity fave) {
        formAnnotationVersionDAO.delete(fave);
    }

    private static void copyValuesAndAnnotations(SDocument source, SDocument target) {
        target.initRestoreMode();
        Value.copyValues(source, target);
        copyIdValues(source.getRoot(), target.getRoot());
        target.setLastId(source.getLastId());
        target.getDocumentAnnotations().copyAnnotationsFrom(source);
        target.finishRestoreMode();
    }

    private static void copyIdValues(SInstance source, SInstance target) {
        target.setId(source.getId());

        if (source instanceof SIComposite) {
            SIComposite sourceComposite = (SIComposite) source;
            SIComposite targetComposite = (SIComposite) target;
            for (int i = 0; i < sourceComposite.getFields().size() ; i++) {
                copyIdValues(sourceComposite.getField(i), targetComposite.getField(i));
            }
        } else if (source instanceof SIList) {
            SIList sourceList = (SIList) source;
            SIList targetList = (SIList) target;

            if (sourceList.getChildren() != null) {
                for (int i = 0; i < sourceList.getChildren().size() ; i++) {
                    SInstance sourceItem = (SInstance) sourceList.getChildren().get(i);
                    SInstance targetItem = (SInstance) targetList.getChildren().get(i);
                    copyIdValues(sourceItem, targetItem);
                }
            }
        }
    }

    public void removeFormRequirementEntity(@Nonnull RequirementInstance p, @Nonnull Class<? extends SType<?>> type) {
        TaskInstance task = p.getFlowInstance().getCurrentTaskOrException();
        Optional<FormRequirementEntity> formRequirementEntity = findFormRequirementEntityByTypeAndTask(p, type, task);

        formRequirementEntity.ifPresent(x -> {
            p.getEntity().getFormRequirementEntities().remove(x);
            formRequirementDAO.delete(x);
        });
    }

    public FormVersionHistoryEntity createFormVersionHistory(RequirementContentHistoryEntity contentHistory, FormRequirementEntity formRequirement) {

        final FormVersionHistoryEntity formVersionHistoryEntity;
        final FormEntity               currentFormEntity;

        formVersionHistoryEntity = new FormVersionHistoryEntity();
        currentFormEntity = formPersistenceService.loadFormEntity(formKeyFromFormEntity(formRequirement.getForm()));

        formVersionHistoryEntity.setMainForm(formRequirement.getMainForm());
        formVersionHistoryEntity.setCodFormVersion(currentFormEntity.getCurrentFormVersionEntity().getCod());
        formVersionHistoryEntity.setCodRequirementContentHistory(contentHistory.getCod());
        formVersionHistoryEntity.setFormVersion(currentFormEntity.getCurrentFormVersionEntity());
        formVersionHistoryEntity.setRequirementContentHistory(contentHistory);

        return formVersionHistoryEntity;

    }

    /**
     * Retornar a chave de recuperação da persistência do formulário para a
     * {@link org.opensingular.form.persistence.entity.FormEntity} informada.
     */
    @Nonnull
    public FormKey formKeyFromFormEntity(@Nonnull FormEntity formEntity) {
        return formPersistenceService.keyFromObject(formEntity.getCod());
    }

    /**
     * Retorna a fábrica de instância associada ao serviço ou uma versão extendida dela se for informado passos extras
     * de configuração (parâmentro extraFactorySetupSteps). Veja
     * {@link SDocumentFactory#extendAddingSetupStep(IConsumer)}.
     */
    @Nonnull
    private SDocumentFactory getFactory(@Nullable SDocumentConsumer extraFactorySetupSteps) {
        SFormConfig<String> form = singularFormConfig
                .orElseThrow(() -> new SingularServerException("singularFormConfig não encontrado !"));
        if (extraFactorySetupSteps != null) {
            return form.getDocumentFactory().extendAddingSetupStep(extraFactorySetupSteps);
        } else {
            return form.getDocumentFactory();
        }
    }

    /** Veja {@link #newTransientSInstance(FormKey, RefType, boolean, SDocumentConsumer)}. */
    @Nonnull
    public SInstance newTransientSInstance(@Nonnull FormKey formKey, @Nonnull RefType refType, boolean keepAnnotations) {
        return newTransientSInstance(formKey, refType, keepAnnotations, null);
    }

    /**
     * Carrega uma nova SInstance a partir da última versão de um formulário salvo em banco (formKey).
     * Essa SInstance não mantém rastrabilidade com o banco de dados e será salva como um novo formulário e uma nova
     * versão. Veja {@link IFormService#newTransientSInstance(FormKey, RefType, SDocumentFactory, boolean)}.
     * @param keepAnnotations informa se as anotações da versão utilizada como base devem ser mantidas
     * @param extraFactorySetupSteps (opcional) Informados passos extras de configuração da nova instância (veja
     * {@link SDocumentFactory#extendAddingSetupStep(IConsumer)}).
     */
    @Nonnull
    public SInstance newTransientSInstance(@Nonnull FormKey formKey, @Nonnull RefType refType, boolean keepAnnotations,
            @Nullable SDocumentConsumer extraFactorySetupSteps) {
        return formPersistenceService.newTransientSInstance(formKey, refType, getFactory(extraFactorySetupSteps),
                keepAnnotations);
    }

    /** Cria uma nova instância do tipo solicitado usando a fábrica de instância do serviço. */
    @Nonnull
    public SInstance createInstance(@Nonnull RefType refType) {
        return createInstance(refType, null);
    }


    /**
     * Cria uma nova instância do tipo solicitado usando a fábrica de instância do serviço.
     * @param extraFactorySetupSteps (opcional) Informados passos extras de configuração da nova instância (veja
     * {@link SDocumentFactory#extendAddingSetupStep(IConsumer)}).
     */
    @Nonnull
    public SInstance createInstance(@Nonnull RefType refType, @Nullable  SDocumentConsumer extraFactorySetupSteps) {
        return createInstance(refType, extraFactorySetupSteps, true);
    }

    private SInstance createInstance(@Nonnull RefType refType, @Nullable  SDocumentConsumer extraFactorySetupSteps, boolean init) {
        return getFactory(extraFactorySetupSteps).createInstance(refType, init);
    }

    /**
     * Gera o contéudo XML para a instância informada. Se a instância não possui dados, retorna um XML vazio. Veja
     * {@link SFormXMLUtil#toStringXMLOrEmptyXML(SInstance)}.
     */
    @Nonnull
    public String extractContentXml(@Nonnull SInstance instance) {
        return SFormXMLUtil.toStringXMLOrEmptyXML(instance);
    }

    /** Encontra a {@link FormVersionEntity} correspondente ao id informado ou dispara Exception senão encontrar. */
    @Nonnull
    public FormVersionEntity loadFormVersionEntity(@Nonnull Long versionId) {
        return formPersistenceService.loadFormVersionEntity(versionId);
    }

    /**
     * Atualiza a instância na base de dados, com base no atributo FormmKey contido na instância informada.
     * <p>Veja {@link IFormService#update(SInstance, Integer)}</p>
     */
    public void update(@Nonnull SInstance instance, Integer inclusionActor) {
        formPersistenceService.update(instance, inclusionActor);
    }

    /**
     * Busca a form version entity associado ao documento da instância. Veja
     * {@link IFormService#findFormEntity(SInstance)}.
     * @return a entidade ou Option null caso nao encontre
     */
    @Nonnull
    public Optional<FormEntity> findFormEntity(@Nonnull SInstance instance) {
        return formPersistenceService.findFormEntity(instance);
    }

    /** Retorna a referência para tipo indicado ou dispara exception senão for possível */
    @Nonnull
    public RefType loadRefType(@Nonnull String typeName) {
        if(singularFormConfig.isPresent()){
            return singularFormConfig.get().getTypeLoader().loadRefTypeOrException(Objects.requireNonNull(typeName));
        }
        throw new SingularServerException("singularFormConfig não encontrado !");
    }

    /** Retorna a referência para tipo indicado ou dispara exception senão for possível */
    @Nonnull
    public RefType loadRefType(@Nonnull Class<? extends SType<?>> typeClass) {
        return loadRefType(RequirementUtil.getTypeName(Objects.requireNonNull(typeClass)));
    }
}