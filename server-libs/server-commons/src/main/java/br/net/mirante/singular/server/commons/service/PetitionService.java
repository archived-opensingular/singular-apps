package br.net.mirante.singular.server.commons.service;


import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

import br.net.mirante.singular.flow.core.Flow;
import br.net.mirante.singular.flow.core.ProcessDefinition;
import br.net.mirante.singular.flow.core.ProcessInstance;
import br.net.mirante.singular.form.service.IPersistenceService;
import br.net.mirante.singular.form.service.dto.FormDTO;
import br.net.mirante.singular.persistence.entity.ProcessGroupEntity;
import br.net.mirante.singular.persistence.entity.ProcessInstanceEntity;
import br.net.mirante.singular.server.commons.persistence.dao.flow.GrupoProcessoDAO;
import br.net.mirante.singular.server.commons.persistence.dao.form.PetitionDAO;
import br.net.mirante.singular.server.commons.persistence.dto.PeticaoDTO;
import br.net.mirante.singular.server.commons.persistence.entity.form.AbstractPetitionEntity;
import br.net.mirante.singular.server.commons.persistence.filter.QuickFilter;

@Transactional
public class PetitionService<T extends AbstractPetitionEntity> {

    @Inject
    private PetitionDAO<T> petitionDAO;

    @Inject
    private GrupoProcessoDAO grupoProcessoDAO;

    @Inject
    private IPersistenceService formPersistenceService;

    public void delete(PeticaoDTO peticao) {
        petitionDAO.delete(petitionDAO.find(peticao.getCod()));
    }


    public long countQuickSearch(QuickFilter filtro, String siglaProcesso) {
        return countQuickSearch(filtro, Collections.singletonList(siglaProcesso));
    }


    public Long countQuickSearch(QuickFilter filtro, List<String> siglasProcesso) {
        return petitionDAO.countQuickSearch(filtro, siglasProcesso);
    }


    public List<? extends PeticaoDTO> quickSearch(QuickFilter filtro, String siglaProcesso) {
        return quickSearch(filtro, Collections.singletonList(siglaProcesso));
    }


    public List<? extends PeticaoDTO> quickSearch(QuickFilter filtro, List<String> siglasProcesso) {
        return petitionDAO.quickSearch(filtro, siglasProcesso);
    }

    public void saveOrUpdate(T peticao, FormDTO form) {
        formPersistenceService.saveOrUpdate(form);
        peticao.setCodForm(form.getCod());
        
        petitionDAO.saveOrUpdate(peticao);
    }

    public void send(T peticao, FormDTO form) {
        ProcessDefinition<?> processDefinition = Flow.getProcessDefinitionWith(peticao.getProcessType());
        ProcessInstance processInstance = processDefinition.newInstance();
        processInstance.setDescription(peticao.getDescription());
        
        ProcessInstanceEntity processEntity = processInstance.saveEntity();
        peticao.setProcessInstanceEntity(processEntity);
        saveOrUpdate(peticao, form);
        
        processInstance.start();
    }

    public T find(Long cod) {
        return petitionDAO.find(cod);
    }

    public T findByProcessCod(Integer cod) {
        return petitionDAO.findByProcessCod(cod);
    }

    public List<ProcessGroupEntity> listarTodosGruposProcesso() {
        return grupoProcessoDAO.listarTodosGruposProcesso();
    }

    public ProcessGroupEntity findByProcessGroupName(String name) {
        return grupoProcessoDAO.findByName(name);
    }

    public ProcessGroupEntity findByProcessGroupCod(String cod) {
        return grupoProcessoDAO.get(cod);
    }
    
}
