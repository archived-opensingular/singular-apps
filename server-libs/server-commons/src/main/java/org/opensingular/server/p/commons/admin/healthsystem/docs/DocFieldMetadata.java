package org.opensingular.server.p.commons.admin.healthsystem.docs;

import org.opensingular.lib.commons.util.Loggable;

import java.io.Serializable;

public class DocFieldMetadata implements Serializable, Loggable {

    private String stypeName;
    private String nomeCampo;
    private String habilitado;
    private String tamanho;
    private String siglaTipo;
    private String obrigatorio;
    private String observacao;
    private String mensagens;
    private String regras;

    public DocFieldMetadata() {

    }

    public DocFieldMetadata(String stypeName, String nomeCampo, String habilitado, String tamanho, String siglaTipo, String obrigatorio, String observacao, String mensagens, String regras) {
        this.stypeName = stypeName;
        this.nomeCampo = nomeCampo;
        this.habilitado = habilitado;
        this.tamanho = tamanho;
        this.siglaTipo = siglaTipo;
        this.obrigatorio = obrigatorio;
        this.observacao = observacao;
        this.mensagens = mensagens;
        this.regras = regras;
    }


    public String isRequired() {
        return obrigatorio;
    }

    public String getFieldName() {
        return nomeCampo;
    }

    public String isEnabled() {
        return habilitado;
    }

    public String getFieldSize() {
        return tamanho;
    }

    public String getFieldTypeAbbreviation() {
        return siglaTipo;
    }

    public String getGeneralInformation() {
        return observacao;
    }

    public String getBusinessRules() {
        return regras;
    }

    public String getMessages() {
        return mensagens;
    }

    public String getStypeName() {
        return stypeName;
    }
}
