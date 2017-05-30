package org.opensingular.server.commons.service;


import org.apache.commons.collections.CollectionUtils;
import org.opensingular.flow.core.TaskInstance;
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
import org.opensingular.form.service.IFormService;
import org.opensingular.form.util.transformer.Value;
import org.opensingular.lib.commons.lambda.IConsumer;
import org.opensingular.lib.support.persistence.enums.SimNao;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.persistence.dao.form.DraftDAO;
import org.opensingular.server.commons.persistence.dao.form.FormPetitionDAO;
import org.opensingular.server.commons.persistence.entity.form.DraftEntity;
import org.opensingular.server.commons.persistence.entity.form.FormPetitionEntity;
import org.opensingular.server.commons.persistence.entity.form.FormVersionHistoryEntity;
import org.opensingular.server.commons.persistence.entity.form.PetitionContentHistoryEntity;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.springframework.transaction.annotation.Transactional;

import com.sun.tools.javac.main.JavacOption.Option;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
public class FormPetitionService<P extends PetitionEntity> {

    @Inject
    protected IFormService formPersistenceService;

    @Inject
    private IUserService userService;

    @Inject
    protected FormPetitionDAO formPetitionDAO;

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
    public FormPetitionEntity findFormPetitionEntityByCod(@Nonnull Long cod) {
        return formPetitionDAO.findOrException(cod);
    }

    @Nonnull
    public Optional<FormPetitionEntity> findFormPetitionEntityByType(@Nonnull PetitionInstance petition,
            @Nonnull String typeName) {
        Objects.requireNonNull(petition);
        //TODO vericiar se esse método não deveria ser unificado com findLastFormPetitionEntityByType()
        //Aparentemente geram o mesmo resultado na DAO
        return formPetitionDAO.findFormPetitionEntityByTypeName(petition.getCod(), typeName);
    }

    /** Na petição, encontra o formulário mais recente do tipo indicado e associado a tarefa indicada. */
    @Nonnull
    public Optional<FormPetitionEntity> findFormPetitionEntityByTypeAndTask(@Nonnull PetitionInstance petition,
            @Nonnull Class<? extends SType<?>> typeClass, @Nonnull TaskInstance task) {
        return findFormPetitionEntityByTypeAndTask(petition, PetitionUtil.getTypeName(typeClass), task);
    }

    /** Na petição, encontra o formulário mais recente do tipo indicado e associado a tarefa indicada. */
    @Nonnull
    public Optional<FormPetitionEntity> findFormPetitionEntityByTypeAndTask(@Nonnull PetitionInstance petition,
            @Nonnull String typeName, @Nonnull TaskInstance task) {
        Objects.requireNonNull(petition);
        Objects.requireNonNull(task);
        Integer taskDefinitionEntityPK =
                ((TaskInstanceEntity) task.getEntityTaskInstance()).getTaskVersion().getTaskDefinition().getCod();
        return formPetitionDAO.findFormPetitionEntityByTypeNameAndTask(petition.getCod(), typeName, taskDefinitionEntityPK);
    }

    /** Na petição, encontra o formulário mais recente do tipo indicado. */
    @Nonnull
    public Optional<FormPetitionEntity> findLastFormPetitionEntityByType(@Nonnull PetitionInstance petition,
            @Nonnull Class<? extends SType<?>> typeClass) {
        return findLastFormPetitionEntityByType(petition, PetitionUtil.getTypeName(typeClass));
    }

    /** Na petição, encontra o formulário mais recente do tipo indicado. */
    public Optional<FormPetitionEntity> findLastFormPetitionEntityByType(@Nonnull PetitionInstance petition, @Nonnull String typeName) {
        return formPetitionDAO.findLastFormPetitionEntityByTypeName(petition.getCod(), typeName);
    }

    @Nonnull
    public Optional<FormPetitionEntity> findFormPetitionEntity(@Nonnull PetitionInstance petition,
            @Nonnull Class<? extends SType<?>> typeClass, boolean mainForm) {
        return findFormPetitionEntity(petition, PetitionUtil.getTypeName(typeClass), mainForm);
    }

    @Nonnull
    private static Optional<FormPetitionEntity> findFormPetitionEntity(@Nonnull PetitionInstance petition,
            @Nonnull String typeName, boolean mainForm) {
        Stream<FormPetitionEntity> entities = petition.getEntity().getFormPetitionEntities().stream();

        //Filter byName
        entities = entities.filter(x -> {
            if (x.getForm() != null) {
                return typeName.equals(PetitionUtil.getTypeName(x));
            } else if (x.getCurrentDraftEntity() != null) {
                return typeName.equals(PetitionUtil.getTypeName(x.getCurrentDraftEntity()));
            }
            return false;
        });

        if (!mainForm) {
            //Filter byTask
            Optional<TaskDefinitionEntity> currentTask = PetitionUtil.getCurrentTaskDefinitionOpt(petition.getEntity());
            if (currentTask.isPresent()) {
                entities = entities.filter(x -> x.getTaskDefinitionEntity().equals(currentTask.get()));
            }
        }
        return entities.findFirst();
    }

    /** Procura na petição a versão mais recente do formulário do tipo informado. */
    @Nonnull
    public Optional<SInstance> findFormPetitionInstanceByTypeAndTask(@Nonnull PetitionInstance petition,
            @Nonnull Class<? extends SType<?>> typeClass, @Nonnull TaskInstance task) {
        return findFormPetitionEntityByTypeAndTask(petition, PetitionUtil.getTypeName(typeClass), task)
                .map(e -> getSInstance(e, typeClass));
    }

    /** Procura na petição a versão mais recente do formulário do tipo informado. */
    @Nonnull
    public <I extends SInstance, K extends SType<? extends I>> Optional<I> findLastFormPetitionInstanceByType(
            @Nonnull PetitionInstance petition, @Nonnull Class<K> typeClass) {
        return findLastFormPetitionEntityByType(petition, PetitionUtil.getTypeName(typeClass))
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
     * Recupera a {@link SInstance} associada ao {@link FormPetitionEntity} informado e verifica se é do tipo da
     * classe esperada. Não sendo do tipo esperado, dispara exception.
     * @return Retorna a {@link SInstance} específica associada ao tipo informado.
     */
    @Nonnull
    public <I extends SInstance, K extends SType<? extends I>> I getSInstance(@Nonnull FormPetitionEntity f,
            @Nonnull Class<K> expectedTypeClass) {
        return checkIfExpectedType(getSInstance(f), expectedTypeClass);
    }

    /** Recupera a {@link SInstance} associada ao {@link FormVersionEntity} informado. */
    @Nonnull
    public SInstance getSInstance(@Nonnull FormPetitionEntity f) {
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
        RefType refType = loadRefType(PetitionUtil.getTypeName(formEntity));
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
        RefType refType = loadRefType(PetitionUtil.getTypeName(f));
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
        return formPetitionDAO.findTwoLastFormVersions(Objects.requireNonNull(codForm));
    }

    @Nonnull
    public Long countVersions(@Nonnull Long codForm) {
        return formPetitionDAO.countVersions(Objects.requireNonNull(codForm));
    }

    @Nonnull
    public FormKey saveFormPetition(@Nonnull PetitionInstance petition, @Nonnull SInstance instance, boolean mainForm) {

        Optional<FormPetitionEntity> formPetitionEntity;

        Integer codActor = userService.getUserCodIfAvailable();
        formPetitionEntity = findFormPetitionEntity(petition, instance.getType().getName(), mainForm);

        if (! formPetitionEntity.isPresent()) {
            formPetitionEntity = Optional.of(newFormPetitionEntity(petition.getEntity(), mainForm));
            if(formPetitionEntity.isPresent()){
                petition.getEntity().getFormPetitionEntities().add(formPetitionEntity.get());
            }
        }
        
        FormPetitionEntity fpe = formPetitionEntity.orElseThrow(()-> new SingularServerException("FormPetitionEntity não encontrado !"));
        
        DraftEntity currentDraftEntity = fpe.getCurrentDraftEntity();
        if (currentDraftEntity == null) {
            currentDraftEntity = createNewDraftWithoutSave();
        }

        saveOrUpdateDraft(instance, currentDraftEntity, codActor);
        fpe.setCurrentDraftEntity(currentDraftEntity);
        formPetitionDAO.saveOrUpdate(fpe);
        return formKeyFromFormEntity(currentDraftEntity.getForm());
    }

    private FormPetitionEntity newFormPetitionEntity(PetitionEntity petition, boolean mainForm) {
        FormPetitionEntity formPetitionEntity = new FormPetitionEntity();
        formPetitionEntity.setPetition(petition);
        if (mainForm) {
            formPetitionEntity.setMainForm(SimNao.SIM);
        } else {
            formPetitionEntity.setMainForm(SimNao.NAO);
            formPetitionEntity.setTaskDefinitionEntity(PetitionUtil.getCurrentTaskDefinitionOpt(petition).orElse(null));
        }
        formPetitionDAO.saveOrUpdate(formPetitionEntity);
        return formPetitionEntity;
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
    public List<FormEntity> consolidateDrafts(@Nonnull PetitionInstance petition) {
        return petition.getEntity().getFormPetitionEntities()
                .stream()
                .filter(formPetitionEntity -> formPetitionEntity.getCurrentDraftEntity() != null)
                .map(this::consolidadeDraft)
                .collect(Collectors.toList());
    }

    /**
     * Consolida o rascunho, copiando os valores do rascunho para o form principal criando versão inicial ou gerando nova versão
     * @return a nova versão criada
     */
    private FormEntity consolidadeDraft(FormPetitionEntity formPetitionEntity) {

        final DraftEntity draft;
        final SInstance draftInstance;
        final SInstance formInstance;
        final boolean     isFirstVersion;
        final FormKey     key;
        final Integer     userCod;

        draft = draftDAO.getOrException(formPetitionEntity.getCurrentDraftEntity().getCod());

        draftInstance = getSInstance(draft.getForm());

        isFirstVersion = formPetitionEntity.getForm() == null;
        if (isFirstVersion) {
            formInstance = createInstance(loadRefType(PetitionUtil.getTypeName(draft)), null, false);
        } else {
            formInstance = getSInstance(formPetitionEntity.getForm());
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

        formPetitionEntity.setForm(formPersistenceService.loadFormEntity(key));
        formPetitionEntity.setCurrentDraftEntity(null);

        formInstance.getDocument().persistFiles();

        deassociateFormVersions(draft.getForm());
        draftDAO.delete(draft);
        formPetitionDAO.save(formPetitionEntity);

        return formPetitionEntity.getForm();
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
        Value.copyValues(source, target);
        copyIdValues(source.getRoot(), target.getRoot());
        target.getDocumentAnnotations().copyAnnotationsFrom(source);
    }

    private static void copyIdValues(SInstance source, SInstance target) {
        target.setId(source.getId());

        if (source instanceof SIComposite) {
            SIComposite sourceComposite = (SIComposite) source;
            SIComposite targetComposite = (SIComposite) target;

            if (sourceComposite.getFields() != null) {
                for (int i = 0; i < sourceComposite.getFields().size() ; i++) {
                    copyIdValues(sourceComposite.getField(i), targetComposite.getField(i));
                }
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

    public void removeFormPetitionEntity(@Nonnull PetitionInstance p, @Nonnull Class<? extends SType<?>> type) {
        TaskInstance task = p.getProcessInstance().getCurrentTaskOrException();
        Optional<FormPetitionEntity> formPetitionEntity = findFormPetitionEntityByTypeAndTask(p, type, task);

        formPetitionEntity.ifPresent(x -> {
            p.getEntity().getFormPetitionEntities().remove(x);
            formPetitionDAO.delete(x);
        });
    }

    public FormVersionHistoryEntity createFormVersionHistory(PetitionContentHistoryEntity contentHistory, FormPetitionEntity formPetition) {

        final FormVersionHistoryEntity formVersionHistoryEntity;
        final FormEntity               currentFormEntity;

        formVersionHistoryEntity = new FormVersionHistoryEntity();
        currentFormEntity = formPersistenceService.loadFormEntity(formKeyFromFormEntity(formPetition.getForm()));

        formVersionHistoryEntity.setMainForm(formPetition.getMainForm());
        formVersionHistoryEntity.setCodFormVersion(currentFormEntity.getCurrentFormVersionEntity().getCod());
        formVersionHistoryEntity.setCodPetitionContentHistory(contentHistory.getCod());
        formVersionHistoryEntity.setFormVersion(currentFormEntity.getCurrentFormVersionEntity());
        formVersionHistoryEntity.setPetitionContentHistory(contentHistory);

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
        return loadRefType(PetitionUtil.getTypeName(Objects.requireNonNull(typeClass)));
    }
}