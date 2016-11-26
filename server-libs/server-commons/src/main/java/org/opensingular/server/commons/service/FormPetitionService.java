package org.opensingular.server.commons.service;


import org.opensingular.flow.core.service.IUserService;
import org.opensingular.flow.persistence.entity.ProcessInstanceEntity;
import org.opensingular.flow.persistence.entity.TaskDefinitionEntity;
import org.opensingular.form.*;
import org.opensingular.form.context.SFormConfig;
import org.opensingular.form.document.RefType;
import org.opensingular.form.document.SDocument;
import org.opensingular.form.persistence.FormKey;
import org.opensingular.form.persistence.SPackageFormPersistence;
import org.opensingular.form.persistence.entity.FormEntity;
import org.opensingular.form.persistence.entity.FormTypeEntity;
import org.opensingular.form.persistence.entity.FormVersionEntity;
import org.opensingular.form.service.IFormService;
import org.opensingular.form.type.core.annotation.AtrAnnotation;
import org.opensingular.form.type.core.annotation.SIAnnotation;
import org.opensingular.form.util.transformer.Value;
import org.opensingular.lib.support.persistence.enums.SimNao;
import org.opensingular.server.commons.persistence.dao.form.DraftDAO;
import org.opensingular.server.commons.persistence.dao.form.FormPetitionDAO;
import org.opensingular.server.commons.persistence.entity.form.*;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;
import java.util.function.Predicate;

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

    public FormPetitionEntity findFormPetitionEntityByCod(Long cod) {
        return formPetitionDAO.find(cod);
    }

    public Optional<FormPetitionEntity> findFormPetitionEntityByTypeName(Long petitionPK, String typeName) {
        return Optional.ofNullable(formPetitionDAO.findFormPetitionEntityByTypeName(petitionPK, typeName));
    }

    public Optional<FormPetitionEntity> findFormPetitionEntityByTypeNameAndTask(Long petitionPK, String typeName, Integer taskDefinitionEntityPK) {
        return Optional.ofNullable(formPetitionDAO.findFormPetitionEntityByTypeNameAndTask(petitionPK, typeName, taskDefinitionEntityPK));
    }

    public Optional<FormPetitionEntity> findLastFormPetitionEntityByTypeName(Long petitionPK, String typeName) {
        return Optional.ofNullable(formPetitionDAO.findLastFormPetitionEntityByTypeName(petitionPK, typeName));
    }

    public FormKey saveFormPetition(P petition,
                                    SInstance instance,
                                    boolean createNewDraftIfDoesntExists,
                                    boolean mainForm,
                                    SFormConfig config) {

        final Integer      codActor;
        FormKey            key;
        FormPetitionEntity formPetitionEntity;

        codActor = userService.getUserCodIfAvailable();
        key = instance.getAttributeValue(SPackageFormPersistence.ATR_FORM_KEY);
        formPetitionEntity = findFormPetitionEntity(petition, instance.getType().getName(), mainForm);

        if (key == null) {
            key = formPersistenceService.insert(instance, codActor);
        }

        if (formPetitionEntity == null) {
            formPetitionEntity = newFormPetitionEntity(petition, mainForm);
            petition.getFormPetitionEntities().add(formPetitionEntity);
        }

        if (formPetitionEntity.getCurrentDraftEntity() != null) {
            formPetitionEntity.setCurrentDraftEntity(saveOrUpdateDraft(instance, formPetitionEntity.getCurrentDraftEntity(), config, codActor));
        } else if (createNewDraftIfDoesntExists) {
            formPetitionEntity.setCurrentDraftEntity(saveOrUpdateDraft(instance, createNewDraftWithoutSave(), config, codActor));
            formPetitionDAO.saveOrUpdate(formPetitionEntity);
        } else {
            formPersistenceService.update(instance, codActor);
        }

        return key;
    }

    private FormPetitionEntity newFormPetitionEntity(P petition, boolean mainForm) {
        FormPetitionEntity formPetitionEntity = new FormPetitionEntity();
        formPetitionEntity.setPetition(petition);
        if (mainForm) {
            formPetitionEntity.setMainForm(SimNao.SIM);
        } else {
            formPetitionEntity.setMainForm(SimNao.NAO);
            formPetitionEntity.setTaskDefinitionEntity(petition.getProcessInstanceEntity().getCurrentTask().getTask().getTaskDefinition());
        }
        formPetitionDAO.saveOrUpdate(formPetitionEntity);
        return formPetitionEntity;
    }


    public FormPetitionEntity findFormPetitionEntity(P petition, String typeName, boolean mainForm) {

        FormPetitionEntity formPetitionEntity;

        final Predicate<FormPetitionEntity> byName = x -> {
            if (x.getForm() != null) {
                return typeName.equals(x.getForm().getFormType().getAbbreviation());
            } else if (x.getCurrentDraftEntity() != null) {
                return typeName.equals(x.getCurrentDraftEntity().getForm().getFormType().getAbbreviation());
            }
            return false;
        };

        final Predicate<FormPetitionEntity> byTask = x -> {
            return x.getTaskDefinitionEntity().equals(getCurrentTaskDefinition(petition).orElse(null));
        };

        if (mainForm) {
            formPetitionEntity = petition.getFormPetitionEntities().stream()
                    .filter(byName)
                    .findFirst().orElse(null);
        } else {
            formPetitionEntity = petition.getFormPetitionEntities().stream()
                    .filter(byName)
                    .filter(byTask)
                    .findFirst()
                    .orElse(null);
        }

        return formPetitionEntity;
    }

    private DraftEntity saveOrUpdateDraft(SInstance instance, DraftEntity draftEntity, SFormConfig config, Integer actor) {

        SIComposite draft;

        if (draftEntity.getForm() != null) {
            draft = loadByCodAndType(config, draftEntity.getForm().getCod(), instance.getType().getName());
        } else {
            draft = newInstance(config, instance.getType().getName());
        }

        copyValuesAndAnnotations(instance.getDocument(), draft.getDocument());

        //TODO (by Daniel) A linha abaixo parece que vai deixar formulário orfãos. Na medida que apontar o rascunho
        // para o novo formulário, não vai deixar o anterior abandonado no banco? Verificar.
        FormKey newFormKey = formPersistenceService.insert(draft, actor);
        draftEntity.setForm(formPersistenceService.loadFormEntity(newFormKey));
        draftEntity.setEditionDate(new Date());

        draftDAO.saveOrUpdate(draftEntity);

        return draftEntity;
    }

    private DraftEntity createNewDraftWithoutSave() {
        final DraftEntity draftEntity = new DraftEntity();
        draftEntity.setStartDate(new Date());
        draftEntity.setEditionDate(new Date());
        return draftEntity;
    }

    public void consolidateDrafts(P petition, SFormConfig formConfig) {
        petition.getFormPetitionEntities()
                .stream()
                .filter(formPetitionEntity -> formPetitionEntity.getCurrentDraftEntity() != null)
                .forEach(formPetitionEntity -> consolidadeDraft(formConfig, formPetitionEntity));
    }

    /**
     * Consolida o rascunho, copiando os valores do formulário rascunho para o form principal criando versão inicial do
     * formulário ou gerando nova versão do formulário. Ou seja, transforma um rascunho em formulário efetivamente.
     */
    private void consolidadeDraft(SFormConfig formConfig, FormPetitionEntity formPetitionEntity) {

        final DraftEntity draft = formPetitionEntity.getCurrentDraftEntity();

        // salva para colocar a entidade com seu form entity na session
        draftDAO.saveOrUpdate(draft);

        final String type = draft.getForm().getFormType().getAbbreviation();
        final SIComposite draftInstance = loadByCodAndType(formConfig, draft.getForm().getCod(), type);

        final SIComposite formInstance;
        final FormKey     key;
        if (formPetitionEntity.getForm() == null) { //If first version
            formInstance = newInstance(formConfig, type);
            copyValuesAndAnnotations(draftInstance.getDocument(), formInstance.getDocument());
            Integer codActor = userService.getUserCodIfAvailable();
            key = formPersistenceService.insert(formInstance, codActor);
        } else {
            formInstance = loadByCodAndType(formConfig, formPetitionEntity.getForm().getCod(), type);
            copyValuesAndAnnotations(draftInstance.getDocument(), formInstance.getDocument());
            key = formPersistenceService.newVersion(formInstance, userService.getUserCodIfAvailable());
        }

        formPetitionEntity.setForm(formPersistenceService.loadFormEntity(key));
        formPetitionEntity.setCurrentDraftEntity(null);

        draftDAO.delete(draft);
        formPetitionDAO.save(formPetitionEntity);
    }

    private void copyValuesAndAnnotations(SDocument source, SDocument target) {
        Value.copyValues(source, target);
        target.getDocumentAnnotations().copyAnnotationsFrom(source);
    }

    public SIComposite loadByCodAndType(SFormConfig config, Long cod, String type) {
        final FormKey formKey = formPersistenceService.keyFromObject(cod);
        final RefType refTypeByName = new RefType() {
            @Override
            protected SType<?> retrieve() {
                return config.getTypeLoader().loadTypeOrException(type);
            }
        };
        return (SIComposite) formPersistenceService.loadSInstance(formKey, refTypeByName, config.getDocumentFactory());
    }

    public SIComposite newInstance(SFormConfig config, String type) {
        final RefType refTypeByName = new RefType() {
            @Override
            protected SType<?> retrieve() {
                return config.getTypeLoader().loadTypeOrException(type);
            }
        };
        return (SIComposite) config.getDocumentFactory().createInstance(refTypeByName);
    }

    private Optional<TaskDefinitionEntity> getCurrentTaskDefinition(P petition) {
        ProcessInstanceEntity processInstanceEntity = petition.getProcessInstanceEntity();
        if (processInstanceEntity != null) {
            return Optional.of(processInstanceEntity.getCurrentTask().getTask().getTaskDefinition());
        }
        return Optional.empty();
    }

    public FormTypeEntity findFormTypeFromVersion(Long formVersionPK) {
        final FormVersionEntity formVersionEntity = formPersistenceService.loadFormVersionEntity(formVersionPK);
        return formVersionEntity.getFormEntity().getFormType();
    }

    public void removeFormPetitionEntity(PetitionEntity p, Class<? extends SType<?>> type, TaskDefinitionEntity taskDefinition) {
        findFormPetitionEntityByTypeNameAndTask(
                p.getCod(),
                SFormUtil.getTypeName(type),
                taskDefinition.getCod()
        ).ifPresent(x -> {
            p.getFormPetitionEntities().remove(x);
            formPetitionDAO.delete(x);
        });
    }

    public FormVersionHistoryEntity createFormVersionHistory(PetitionContentHistoryEntity contentHistory, FormPetitionEntity formPetition) {

        final FormVersionHistoryEntity formVersionHistoryEntity;
        final FormEntity               currentFormEntity;

        formVersionHistoryEntity = new FormVersionHistoryEntity();
        currentFormEntity = formPersistenceService.loadFormEntity(formPersistenceService.keyFromObject(formPetition.getForm().getCod()));

        formVersionHistoryEntity.setMainForm(formPetition.getMainForm());
        formVersionHistoryEntity.setCodFormVersion(currentFormEntity.getCurrentFormVersionEntity().getCod());
        formVersionHistoryEntity.setCodPetitionContentHistory(contentHistory.getCod());
        formVersionHistoryEntity.setFormVersion(currentFormEntity.getCurrentFormVersionEntity());
        formVersionHistoryEntity.setPetitionContentHistory(contentHistory);

        return formVersionHistoryEntity;

    }

}