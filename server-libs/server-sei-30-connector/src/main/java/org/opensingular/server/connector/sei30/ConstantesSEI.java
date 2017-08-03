package org.opensingular.server.connector.sei30;

import org.opensingular.server.connector.sei30.ws.ArrayOfAssunto;
import org.opensingular.server.connector.sei30.ws.ArrayOfDestinatario;
import org.opensingular.server.connector.sei30.ws.ArrayOfDocumento;
import org.opensingular.server.connector.sei30.ws.ArrayOfDocumentoFormatado;
import org.opensingular.server.connector.sei30.ws.ArrayOfIdUnidade;
import org.opensingular.server.connector.sei30.ws.ArrayOfInteressado;
import org.opensingular.server.connector.sei30.ws.ArrayOfProcedimentoRelacionado;

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
