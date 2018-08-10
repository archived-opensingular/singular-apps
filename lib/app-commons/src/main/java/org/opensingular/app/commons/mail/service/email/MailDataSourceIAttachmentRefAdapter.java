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

package org.opensingular.app.commons.mail.service.email;

import org.opensingular.form.type.core.attachment.IAttachmentRef;

import javax.activation.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Simple Wrapper to adapt DataSource interface to IAttachment ref
 */
public class MailDataSourceIAttachmentRefAdapter implements DataSource {

    private IAttachmentRef delegate;

    public MailDataSourceIAttachmentRefAdapter(IAttachmentRef delegate) {
        this.delegate = delegate;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return delegate.getContentAsInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException("Método não suportador para " + IAttachmentRef.class);
    }

    @Override
    public String getContentType() {
        return delegate.getContentType();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }
}
