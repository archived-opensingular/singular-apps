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
package org.opensingular.server.commons.service;

import org.apache.commons.lang3.StringUtils;
import org.opensingular.app.commons.mail.service.dto.Email;
import org.opensingular.app.commons.mail.service.email.IEmailService;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.server.commons.exception.SingularServerException;
import org.opensingular.server.commons.spring.security.SingularUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.transaction.Transactional;

@Named
public class SendEmailToSupportService implements Loggable {
    private static final String SINGULAR_SUPPORT_ADDRESS = "singular.mail.support.address";

    @Inject
    private Provider<SingularUserDetails> singularUserDetails;

    @Inject
    private IEmailService<Email> emailService;

    @Transactional
    public void sendEmailToSupport(String errorCode, String stackTrace, String urlException) {
        String supportEmail = SingularProperties.get().getProperty(SINGULAR_SUPPORT_ADDRESS, "");
        if (StringUtils.isNotBlank(supportEmail)) {
            try {
                Email email = new Email();

                String[] emails = supportEmail.split(",");
                for (int i = 0; i < emails.length; i++) {
                    emails[i] = emails[i].trim();
                }
                email.addTo(emails);
                email.withSubject("Exception in production");

                String loggedUser = getLoggedUser();

                email.withContent(
                        "<pre>"
                                + "Url: " + urlException + "\n\n"
                                + loggedUser + "\n\n"
                                + "Error code: " + errorCode + "\n\n"
                                + "Stack Trace:\n\n" + stackTrace
                                + "</pre>");

                emailService.send(email);
            } catch (SingularServerException e) {
                getLogger().warn("Error ocurred while send e-mail to singular support", e);
            }
        }
    }

    private String getLoggedUser() {
        StringBuilder returnString = new StringBuilder();
        try {
            SingularUserDetails userDetails = singularUserDetails.get();
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String) {
                returnString.append("Username: ").append(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            } else if (userDetails != null) {
                returnString.append("Username: ").append(userDetails.getUsername()).append("\nDisplay name: ").append(userDetails.getDisplayName());
            } else {
                returnString.append("User: - ");
            }
        } catch (Exception e) {
            getLogger().warn("Error ocurred while retrieving logged User", e);
            returnString.setLength(0);
            returnString.append("User not found");
        }

        return returnString.toString();
    }
}
