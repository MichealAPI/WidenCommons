package it.mikeslab.widencommons.api.lang;

/**
 * Interface for config-oriented enums
 */
public interface ConfigurableEnum {

    /**
     * Get the path of the configuration
     * @return the path
     */
    String getPath();

    /**
     * Get the default value of the configuration
     * @return the default value
     */
    Object getDefaultValue();

}
