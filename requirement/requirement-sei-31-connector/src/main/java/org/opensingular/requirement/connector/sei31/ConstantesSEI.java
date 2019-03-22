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

package org.opensingular.requirement.connector.sei31;

import org.opensingular.requirement.connector.sei31.ws.ArrayOfAssunto;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfDestinatario;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfDocumento;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfDocumentoFormatado;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfIdUnidade;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfInteressado;
import org.opensingular.requirement.connector.sei31.ws.ArrayOfProcedimentoRelacionado;

/**
 * Classe ConstantesSEI.
 */
public interface ConstantesSEI {

    /** O campo documentos empty. */
    ArrayOfDocumento DOCUMENTOS_EMPTY = new ArrayOfDocumento();

    /** O campo assuntos empty. */
    ArrayOfAssunto ASSUNTOS_EMPTY = new ArrayOfAssunto();

    /** O campo interessados empty. */
    ArrayOfInteressado INTERESSADOS_EMPTY = new ArrayOfInteressado();

    /** O campo destinatarios empty. */
    ArrayOfDestinatario DESTINATARIOS_EMPTY = new ArrayOfDestinatario();

    /** O campo procedimento relacionados empty. */
    ArrayOfProcedimentoRelacionado PROCEDIMENTO_RELACIONADOS_EMPTY = new ArrayOfProcedimentoRelacionado();

    /** O campo id unidade empty. */
    ArrayOfIdUnidade ID_UNIDADE_EMPTY = new ArrayOfIdUnidade();

    /** O campo documentos formatados empty */
    ArrayOfDocumentoFormatado DOCUMENTOS_FORMATADOS_EMPTY = new ArrayOfDocumentoFormatado();

}
