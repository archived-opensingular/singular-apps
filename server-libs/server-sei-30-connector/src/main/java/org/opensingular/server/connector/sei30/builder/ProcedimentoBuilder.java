package org.opensingular.server.connector.sei30.builder;

import org.opensingular.server.connector.sei30.ConstantesSEI;
import org.opensingular.server.connector.sei30.model.AssuntoSei;
import org.opensingular.server.connector.sei30.model.NivelAcesso;
import org.opensingular.server.connector.sei30.model.TipoProcedimento;
import org.opensingular.server.connector.sei30.ws.Interessado;
import org.opensingular.server.connector.sei30.ws.Procedimento;

import java.util.Arrays;

/**
 * Classe ProcedimentoBuilder.
 */
public class ProcedimentoBuilder {

    private Procedimento procedimento;

    /**
     * Instancia um novo objeto procedimento builder.
     */
    public ProcedimentoBuilder() {
        this.procedimento = new Procedimento();
        this.procedimento.setAssuntos(ConstantesSEI.ASSUNTOS_EMPTY);
        this.procedimento.setInteressados(ConstantesSEI.INTERESSADOS_EMPTY);
        this.procedimento.setNivelAcesso(NivelAcesso.PUBLICO.getCodigo());

    }

    /**
     * Atualiza o novo valor de tipo procedimento.
     * 
     * @param tipoProcedimento
     *            o(a) value.
     * @return o valor de procedimento builder
     */
    public ProcedimentoBuilder setTipoProcedimento(TipoProcedimento tipoProcedimento) {
        procedimento.setIdTipoProcedimento(tipoProcedimento != null ? tipoProcedimento.getId() : null);
        return this;
    }

    /**
     * Atualiza o novo valor de especificacao.
     * 
     * @param value
     *            o(a) value.
     * @return o valor de procedimento builder
     */
    public ProcedimentoBuilder setEspecificacao(String value) {
        procedimento.setEspecificacao(value);
        return this;
    }

    /**
     * Atualiza o novo valor de assuntos.
     * 
     * @param assuntos
     *            o(a) assuntos.
     * @return o valor de procedimento builder
     */
    public ProcedimentoBuilder setAssuntos(AssuntoSei... assuntos) {
        procedimento.getAssuntos().getItem().clear();
        if (assuntos != null) {
            for (AssuntoSei assuntoSei : Arrays.asList(assuntos)) {
                procedimento.getAssuntos().getItem().add(assuntoSei.toAssunto());
            }
        }
        return this;
    }

    /**
     * Atualiza o novo valor de interessados.
     * 
     * @param interessados
     *            o(a) interessados.
     * @return o valor de procedimento builder
     */
    public ProcedimentoBuilder setInteressados(String... interessados) {
        procedimento.getInteressados().getItem().clear();
        if (interessados != null) {
            for (String nome : interessados) {
                Interessado e = new Interessado();
                e.setNome(nome);
                procedimento.getInteressados().getItem().add(e);

            }

        }
        return this;
    }

    /**
     * Atualiza o novo valor de observacao.
     * 
     * @param value
     *            o(a) value.
     * @return o valor de procedimento builder
     */
    public ProcedimentoBuilder setObservacao(String value) {
        procedimento.setObservacao(value);
        return this;
    }

    /**
     * Atualiza o novo valor de nivel acesso.
     * 
     * @param nivelAcesso
     *            o(a) nivel acesso.
     * @return o valor de procedimento builder
     */
    public ProcedimentoBuilder setNivelAcesso(NivelAcesso nivelAcesso) {
        procedimento.setNivelAcesso(nivelAcesso != null ? nivelAcesso.getCodigo() : NivelAcesso.PUBLICO.getCodigo());
        return this;
    }

    /**
     * Cria o procedimento.
     * 
     * @return o valor de procedimento
     */
    public Procedimento createProcedimento() {
        return procedimento;
    }
}
