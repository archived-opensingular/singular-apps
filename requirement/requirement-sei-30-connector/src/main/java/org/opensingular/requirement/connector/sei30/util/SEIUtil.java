package org.opensingular.requirement.connector.sei30.util;

/**
 * Classe utilitária para lidar com as formatações de objetos necessárias aos serviços do SEI!
 */
public class SEIUtil {

    private SEIUtil() {
        throw new UnsupportedOperationException("No " + SEIUtil.class.getSimpleName() + " instances for you!");
    }

    /**
     * Trunca a String value se o seu tamanho for maior que maxLenght, adicionando "..." ao final
     *
     * @param value     o valor a ser truncado
     * @param maxLenght seu maior tamanho possível
     * @return o valor trucado e concatenado com "..." se o seu tamanho for maior que maxLenght
     */
    public static String truncate(String value, int maxLenght) {
        if (value.length() > maxLenght) {
            return value.substring(0, maxLenght - 3).concat("...");
        }
        return value;
    }

}
