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
package org.opensingular.app.commons.mail.service.email;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.activation.DataHandler;
import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.BooleanUtils;
import org.opensingular.app.commons.mail.service.dto.Email;
import org.opensingular.form.type.core.attachment.IAttachmentRef;
import org.opensingular.lib.commons.base.SingularProperties;
import org.opensingular.lib.commons.util.Loggable;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.opensingular.lib.commons.base.SingularProperties.SINGULAR_EMAIL_TEST_RECPT;

public class EmailSender extends JavaMailSenderImpl implements Loggable {

    private static final String EMAIL_DEVELOPMENT = "opensingular@gmail.com";

    private String                from;
    private Cache<Class, Boolean> errorCache;

    @PostConstruct
    public void init() {
        SingularProperties properties = SingularProperties.get();
        setHost(properties.getPropertyOpt("singular.mail.host").orElse(null));
        if (getHost() != null) {
            from = properties.getPropertyOpt("singular.mail.from").orElse(null);
            setPort(properties.getPropertyOpt("singular.mail.port").orElse(null));
            setUsername(properties.getPropertyOpt("singular.mail.username").orElse(null));
            setPassword(properties.getPropertyOpt("singular.mail.password").orElse(null));
            setProtocol(properties.getPropertyOpt("singular.mail.protocol").orElse(null));

            getJavaMailProperties().setProperty("mail.smtp.host", getHost());
            getJavaMailProperties().setProperty("mail.smtp.port", String.valueOf(getPort()));
            if (getUsername() != null) {
                getJavaMailProperties().setProperty("mail.smtp.user", getUsername());
            }
            properties.getPropertyOpt("singular.mail.auth").ifPresent(
                    v -> getJavaMailProperties().put("mail.smtp.auth", v));

            properties.getPropertyOpt("singular.mail.smtp.starttls.enable").ifPresent(
                    v -> getJavaMailProperties().put("mail.smtp.starttls.enable", v));

            properties.getPropertyOpt("singular.mail.smtp.ssl.trust").ifPresent(
                    v -> getJavaMailProperties().put("mail.smtp.ssl.trust", v));

            getLogger().info("SMTP mail sender Enabled.");
        } else {
            getLogger().warn("SMTP mail sender Disabled.");
        }

        errorCache = CacheBuilder.newBuilder()
                .concurrencyLevel(4)
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build();
    }

    public boolean send(Email.Addressee addressee) {
        if (getHost() == null) {
            getLogger().info("SMTP mail sender Disabled.");
            return false;
        }
        if (addressee.getSentDate() == null) {
            try {
                Email             e   = addressee.getEmail();
                final MimeMessage msg = createMimeMessage();

                // Para que email "out of office" não seja enviados como resposta automatica
                msg.setHeader("Precedence", "bulk");
                msg.setHeader("X-Auto-Response-Suppress", "OOF");

                msg.setSubject(e.getSubject());
                msg.setSentDate(Optional.ofNullable(e.getCreationDate()).orElseGet(Date::new));
                msg.setFrom(new InternetAddress(Optional.ofNullable(from).orElseGet(this::getUsername), addressee.getEmail().getAliasFrom()));
                // destinatários
                Message.RecipientType recipientType = addressee.getType().getRecipientType();
                if (SingularProperties.get().isTrue(SingularProperties.SINGULAR_SEND_EMAIL)) {
                    msg.addRecipient(recipientType, new InternetAddress(addressee.getAddress()));
                } else {
                    msg.addRecipient(recipientType, new InternetAddress(SingularProperties.get().getProperty(SINGULAR_EMAIL_TEST_RECPT, EMAIL_DEVELOPMENT)));
                }

                // Cria o "contêiner" das várias partes do e-mail
                final Multipart content = new MimeMultipart("related");

                final MimeBodyPart mainContent = new MimeBodyPart();
                mainContent.setContent(e.getContent(), "text/html; charset=iso-8859-1");
                content.addBodyPart(mainContent);

                // Adiciona anexos
                for (IAttachmentRef attachmentRef : e.getAttachments()) {
                    MimeBodyPart part = new MimeBodyPart();
                    part.setDisposition(MimeBodyPart.ATTACHMENT);
                    part.setHeader("Content-ID", "<" + attachmentRef.getName() + ">");
                    part.setDataHandler(new DataHandler(new MailDataSourceIAttachmentRefAdapter(attachmentRef)));
                    part.setFileName(attachmentRef.getName());
                    part.setDescription(attachmentRef.getName());
                    content.addBodyPart(part);
                }
                msg.setContent(content);
                msg.saveChanges();

                send(msg);

                addressee.setSentDate(new Date());

                getLogger().info("Email enviado para o destinatário(cod={})={}", addressee.getCod(), addressee.getAddress());
            } catch (Exception ex) {
                addressee.setSentDate(null);
                String msg = "ERRO ao enviar email para o destinatário(cod=" + addressee.getCod() + ")=" + addressee.getAddress();

                // Esse errorCache evita que a stack completa da exceção seja
                // impressa no log mais do que uma vez ao dia.
                if (BooleanUtils.isTrue(errorCache.getIfPresent(ex.getClass()))) {
                    getLogger().error(msg);
                } else {
                    getLogger().error(msg, ex);
                    errorCache.put(ex.getClass(), Boolean.TRUE);
                }
                return false;
            }
        }
        return true;
    }

    public void setPort(String port) {
        if (port != null) {
            super.setPort(Integer.parseInt(port));
        }
    }
}
