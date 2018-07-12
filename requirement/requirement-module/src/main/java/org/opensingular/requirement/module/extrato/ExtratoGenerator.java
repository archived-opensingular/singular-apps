package org.opensingular.requirement.module.extrato;

import org.opensingular.form.SInstance;

public interface ExtratoGenerator {

    /**
     * Method called to generate the extrato of the requirement.
     *
     * @param root The instance of requirement.
     * @return String containing all Html of the page.
     */
    String generate(SInstance root);
}
