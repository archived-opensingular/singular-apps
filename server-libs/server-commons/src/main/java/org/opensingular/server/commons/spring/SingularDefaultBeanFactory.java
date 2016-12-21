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

package org.opensingular.server.commons.spring;

import org.opensingular.flow.core.renderer.IFlowRenderer;
import org.opensingular.flow.core.service.IUserService;
import org.opensingular.flow.schedule.IScheduleService;
import org.opensingular.form.document.SDocument;
import org.opensingular.form.persistence.dao.AttachmentContentDao;
import org.opensingular.form.persistence.dao.AttachmentDao;
import org.opensingular.form.persistence.dao.FormAnnotationDAO;
import org.opensingular.form.persistence.dao.FormAnnotationVersionDAO;
import org.opensingular.form.persistence.dao.FormAttachmentDAO;
import org.opensingular.form.persistence.dao.FormDAO;
import org.opensingular.form.persistence.dao.FormTypeDAO;
import org.opensingular.form.persistence.dao.FormVersionDAO;
import org.opensingular.form.service.FormService;
import org.opensingular.form.service.IFormService;
import org.opensingular.form.type.core.attachment.IAttachmentPersistenceHandler;
import org.opensingular.server.commons.cache.SingularKeyGenerator;
import org.opensingular.server.commons.flow.renderer.remote.YFilesFlowRemoteRenderer;
import org.opensingular.server.commons.persistence.dao.EmailAddresseeDao;
import org.opensingular.server.commons.persistence.dao.EmailDao;
import org.opensingular.server.commons.persistence.dao.ParameterDAO;
import org.opensingular.server.commons.persistence.dao.flow.ActorDAO;
import org.opensingular.server.commons.persistence.dao.flow.GrupoProcessoDAO;
import org.opensingular.server.commons.persistence.dao.flow.TaskInstanceDAO;
import org.opensingular.server.commons.persistence.dao.form.DraftDAO;
import org.opensingular.server.commons.persistence.dao.form.FormPetitionDAO;
import org.opensingular.server.commons.persistence.dao.form.PetitionContentHistoryDAO;
import org.opensingular.server.commons.persistence.dao.form.PetitionDAO;
import org.opensingular.server.commons.persistence.dao.form.PetitionerDAO;
import org.opensingular.server.commons.persistence.entity.form.PetitionEntity;
import org.opensingular.server.commons.schedule.TransactionalQuartzScheduledService;
import org.opensingular.server.commons.service.EmailPersistenceService;
import org.opensingular.server.commons.service.FormPetitionService;
import org.opensingular.server.commons.service.IEmailService;
import org.opensingular.server.commons.service.ParameterService;
import org.opensingular.server.commons.service.PetitionService;
import org.opensingular.server.commons.service.attachment.AttachmentService;
import org.opensingular.server.commons.service.attachment.FormAttachmentService;
import org.opensingular.server.commons.service.attachment.ServerAttachmentPersistenceService;
import org.opensingular.server.commons.service.attachment.ServerTemporaryAttachmentPersistenceService;
import org.opensingular.server.commons.spring.security.AuthorizationService;
import org.opensingular.server.commons.spring.security.DefaultUserDetailService;
import org.opensingular.server.commons.spring.security.PermissionResolverService;
import org.opensingular.server.commons.spring.security.SingularUserDetailsService;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;


@SuppressWarnings("rawtypes")
public class SingularDefaultBeanFactory {

    @Bean(name = "peticionamentoUserDetailService")
    public SingularUserDetailsService worklistUserDetailServiceFactory() {
        return new DefaultUserDetailService();
    }

    @Bean
    public <T extends PetitionEntity> PetitionDAO<T> peticaoDAO() {
        return new PetitionDAO<>();
    }

    @Bean
    public PetitionContentHistoryDAO petitionContentHistoryDAO() {
        return new PetitionContentHistoryDAO();
    }

    @Bean
    public FormPetitionDAO formPetitionDAO() {
        return new FormPetitionDAO();
    }

    @Bean
    public DraftDAO draftDAO() {
        return new DraftDAO();
    }

    @Bean
    public <T extends PetitionEntity> PetitionService<T> worklistPetitionServiceFactory() {
        return new PetitionService<>();
    }

    @Bean
    public TaskInstanceDAO taskInstanceDAO() {
        return new TaskInstanceDAO();
    }

    @Bean
    public ActorDAO actorDAO() {
        return new ActorDAO();
    }

    @Bean
    public GrupoProcessoDAO grupoProcessoDAO() {
        return new GrupoProcessoDAO();
    }

    @Bean(name = SDocument.FILE_PERSISTENCE_SERVICE)
    public IAttachmentPersistenceHandler attachmentPersistenceService() {
        return new ServerAttachmentPersistenceService();
    }

    @Bean(name = SDocument.FILE_TEMPORARY_SERVICE)
    public IAttachmentPersistenceHandler attachmentTemporaryService() {
        return new ServerTemporaryAttachmentPersistenceService();
    }

    @Bean
    public AttachmentDao attachmentDao() {
        return new AttachmentDao();
    }

    @Bean
    public AttachmentContentDao attachmentContentDao() {
        return new AttachmentContentDao();
    }

    @Bean
    public IFormService formService() {
        return new FormService();
    }

    @Bean
    public PetitionerDAO petitionerDAO() {
        return new PetitionerDAO();
    }

    @Bean
    public FormDAO formDAO() {
        return new FormDAO();
    }

    @Bean
    public FormVersionDAO formVersionDAO() {
        return new FormVersionDAO();
    }

    @Bean
    public FormAnnotationDAO formAnnotationDAO() {
        return new FormAnnotationDAO();
    }

    @Bean
    public FormAnnotationVersionDAO formAnnotationVersionDAO() {
        return new FormAnnotationVersionDAO();
    }

    @Bean
    public FormTypeDAO formTypeDAO() {
        return new FormTypeDAO();
    }

    @Bean
    public IUserService userService() {
        return new SingularDefaultUserService();
    }

    @Bean
    public PermissionResolverService getPermissionResolverService() {
        return new PermissionResolverService();
    }

    @Bean
    public AuthorizationService getAuthorizationService() {
        return new AuthorizationService();
    }

    @Bean
    public EmailDao<?> emailDao() {
        return new EmailDao<>();
    }

    @Bean
    public EmailAddresseeDao<?> emailAddresseeDao() {
        return new EmailAddresseeDao<>();
    }

    @Bean
    @DependsOn(SDocument.FILE_PERSISTENCE_SERVICE)
    public IEmailService<?> emailService() {
        return new EmailPersistenceService();
    }

    @Bean
    public IScheduleService scheduleService() {
        return new TransactionalQuartzScheduledService();
    }

    @Bean
    public IFlowRenderer flowRenderer() {
        return new YFilesFlowRemoteRenderer(null);
    }

    @Bean
    public ParameterDAO parameterDAO() {
        return new ParameterDAO();
    }

    @Bean
    public ParameterService parameterService() {
        return new ParameterService();
    }

    @Bean
    public <T extends PetitionEntity> FormPetitionService<T> formPetitionService() {
        return new FormPetitionService<>();
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheCacheManager() {
        EhCacheManagerFactoryBean cacheManager = new EhCacheManagerFactoryBean();
        cacheManager.setConfigLocation(new ClassPathResource("default-singular-ehcache.xml"));
        cacheManager.setShared(true);
        return cacheManager;
    }

    @Bean
    public CacheManager cacheManager(EhCacheManagerFactoryBean ehCacheManagerFactoryBean) {
        return new EhCacheCacheManager(ehCacheManagerFactoryBean.getObject());
    }

    @Bean(name = "singularKeyGenerator")
    public KeyGenerator singularKeyGenerator() {
        return new SingularKeyGenerator();
    }

    @Bean
    public FormAttachmentDAO formAttachmentDAO() {
        return new FormAttachmentDAO();
    }

    @Bean
    public AttachmentService<?, ?> attachmentService() {
        return new AttachmentService<>();
    }

    @Bean
    public FormAttachmentService formAttachmentService() {
        return new FormAttachmentService();
    }
}
