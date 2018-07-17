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

package org.opensingular.requirement.module.spring;

import org.opensingular.app.commons.mail.persistence.dao.EmailAddresseeDao;
import org.opensingular.app.commons.mail.persistence.dao.EmailDao;
import org.opensingular.app.commons.mail.schedule.TransactionalQuartzScheduledService;
import org.opensingular.app.commons.mail.service.email.EmailPersistenceService;
import org.opensingular.app.commons.mail.service.email.IEmailService;
import org.opensingular.app.commons.spring.security.SingularUserDetailsFactoryBean;
import org.opensingular.flow.core.service.IUserService;
import org.opensingular.flow.persistence.dao.ModuleDAO;
import org.opensingular.flow.schedule.IScheduleService;
import org.opensingular.form.document.SDocument;
import org.opensingular.form.persistence.dao.*;
import org.opensingular.form.service.FormService;
import org.opensingular.form.service.FormTypeService;
import org.opensingular.form.service.IFormService;
import org.opensingular.form.spring.SingularUserDetails;
import org.opensingular.form.type.core.attachment.IAttachmentPersistenceHandler;
import org.opensingular.form.type.core.attachment.IAttachmentRef;
import org.opensingular.form.type.core.attachment.helper.IAttachmentPersistenceHelper;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.commons.context.spring.SpringServiceRegistry;
import org.opensingular.lib.commons.pdf.HtmlToPdfConverter;
import org.opensingular.lib.support.spring.security.DefaultRestUserDetailsService;
import org.opensingular.lib.support.spring.security.RestUserDetailsService;
import org.opensingular.requirement.module.SingularModuleConfigurationBean;
import org.opensingular.requirement.module.WorkspaceConfigurationMetadata;
import org.opensingular.requirement.module.cache.SingularKeyGenerator;
import org.opensingular.requirement.module.config.IServerContext;
import org.opensingular.requirement.module.config.ServerStartExecutorBean;
import org.opensingular.requirement.module.connector.LocalModuleDriver;
import org.opensingular.requirement.module.connector.ModuleDriver;
import org.opensingular.requirement.module.extrato.ExtratoGenerator;
import org.opensingular.requirement.module.extrato.ExtratoGeneratorImpl;
import org.opensingular.requirement.module.persistence.dao.BoxDAO;
import org.opensingular.requirement.module.persistence.dao.ParameterDAO;
import org.opensingular.requirement.module.persistence.dao.flow.ActorDAO;
import org.opensingular.requirement.module.persistence.dao.flow.TaskInstanceDAO;
import org.opensingular.requirement.module.persistence.dao.form.*;
import org.opensingular.requirement.module.persistence.entity.form.RequirementEntity;
import org.opensingular.requirement.module.rest.ModuleBackstageService;
import org.opensingular.requirement.module.service.*;
import org.opensingular.requirement.module.service.attachment.*;
import org.opensingular.requirement.module.spring.security.*;
import org.opensingular.ws.wkhtmltopdf.client.MockHtmlToPdfConverter;
import org.opensingular.ws.wkhtmltopdf.client.RestfulHtmlToPdfConverter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@SuppressWarnings("rawtypes")
@Lazy(false)
public class SingularDefaultBeanFactory {

    @Order(1)
    @Bean
    @Lazy(false)
    public SpringServiceRegistry getSpringServiceRegistry() {
        return new SpringServiceRegistry();
    }

    @Primary
    @Bean(name = "peticionamentoUserDetailService")
    public SingularUserDetailsService workListUserDetailServiceFactory() {
        return new DefaultUserDetailService();
    }

    @Bean
    public SingularDiffService singularDiffService() {
        return new SingularDiffService();
    }

    @Bean
    public <T extends RequirementEntity> RequirementDAO<T> requirementDAO() {
        return new RequirementDAO<>();
    }

    @Bean
    public RequirementContentHistoryDAO requirementContentHistoryDAO() {
        return new RequirementContentHistoryDAO();
    }

    @Bean
    public FormRequirementDAO formRequirementDAO() {
        return new FormRequirementDAO();
    }

    @Bean
    public DraftDAO draftDAO() {
        return new DraftDAO();
    }

    @Bean
    public RequirementService<?, ?> workListRequirementServiceFactory() {
        return new DefaultRequirementService();
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
    public ModuleDAO moduleDAO() {
        return new ModuleDAO();
    }

    @Bean
    public BoxDAO boxDAO() {
        return new BoxDAO();
    }

    @Bean(name = SDocument.FILE_PERSISTENCE_SERVICE)
    public IAttachmentPersistenceHandler<IAttachmentRef> attachmentPersistenceService() {
        return new ServerAttachmentPersistenceService();
    }

    @Bean(name = SDocument.FILE_TEMPORARY_SERVICE)
    public IAttachmentPersistenceHandler<IAttachmentRef> attachmentTemporaryService() {
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
    public FormTypeService formTypeService() {
        return new FormTypeService();
    }

    @Bean
    public FormCacheFieldDAO formCacheFieldDAO() {
        return new FormCacheFieldDAO();
    }

    @Bean
    public FormCacheValueDAO formCacheValueDAO() {
        return new FormCacheValueDAO();
    }

    @Bean
    public ApplicantDAO applicantDAO() {
        return new ApplicantDAO();
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
        return new AuthorizationServiceImpl();
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
    public RequirementDefinitionDAO<?> requirementDefinitionDAO() {
        return new RequirementDefinitionDAO<>();
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
    public ParameterDAO parameterDAO() {
        return new ParameterDAO();
    }

    @Bean
    public ParameterService parameterService() {
        return new ParameterService();
    }

    @Bean
    public <T extends RequirementEntity> FormRequirementService<T> formRequirementService() {
        return new FormRequirementService<>();
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheCacheManager() {
        EhCacheManagerFactoryBean cacheManager = new EhCacheManagerFactoryBean();
        cacheManager.setConfigLocation(new ClassPathResource("default-singular-ehcache.xml"));
        cacheManager.setShared(true);
        return cacheManager;
    }

    @Bean
    @Primary
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
    public IFormAttachmentService formAttachmentService() {
        return new FormAttachmentService();
    }

    @Bean
    public IAttachmentPersistenceHelper serverAttachmentPersistenceHelper(IFormService formService, IFormAttachmentService attachmentService) {
        return new ServerAttachmentPersistenceHelper(formService, attachmentService);
    }

    @Bean
    public RestUserDetailsService restUserDetailsService() {
        return new DefaultRestUserDetailsService();
    }

    @Bean
    public DefaultRequirementSender defaultRequirementSender() {
        return new DefaultRequirementSender();
    }

    @Bean
    public ServerStartExecutorBean lifecycle() {
        return new ServerStartExecutorBean();
    }

    @Bean
    public ModuleDriver moduleDriver() {
        return new LocalModuleDriver();
    }

    @Bean
    public SingularUserDetailsFactoryBean<? extends SingularRequirementUserDetails> singularUserDetails() {
        return new SingularUserDetailsFactoryBean<>(SingularRequirementUserDetails.class);
    }

    @Bean
    public HtmlToPdfConverter htmlToPdfConverter() {
        if (SingularProperties.get().isTrue(SingularProperties.SINGULAR_DEV_MODE)) {
            return new MockHtmlToPdfConverter();
        } else {
            return RestfulHtmlToPdfConverter.createUsingDefaultConfig();
        }
    }

    @Bean
    public ExtratoGenerator extratoGenerator() {
        return new ExtratoGeneratorImpl();
    }

    @Bean
    @Scope(WebApplicationContext.SCOPE_REQUEST)
    public WorkspaceConfigurationMetadata workspaceConfigurationMetadata(
            SingularModuleConfigurationBean singularServerConfiguration, ModuleBackstageService moduleBackstageService,
            SingularUserDetails singularUserDetails) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = sra.getRequest();
        IServerContext menuContext = IServerContext.getContextFromRequest(req, singularServerConfiguration.getContexts());
        return moduleBackstageService.loadWorkspaceConfiguration(menuContext.getName(), singularUserDetails.getUsername());
    }

}