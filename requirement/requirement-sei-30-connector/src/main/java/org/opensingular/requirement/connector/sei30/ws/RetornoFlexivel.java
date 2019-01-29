package org.opensingular.requirement.connector.sei30.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import java.util.List;

/**
 * Classe base do retorno que faz com que
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class RetornoFlexivel {

    @XmlAnyElement(lax = true)
    private List<Object> camposExtra;

    public List<Object> getCamposExtra() {
        return camposExtra;
    }

    public void setCamposExtra(List<Object> camposExtra) {
        this.camposExtra = camposExtra;
    }
}
