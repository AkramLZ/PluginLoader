package me.akraml.loader.plugin;

/**
 * This interface will be inherited by the injected plugin as a main class to identify it.
 */
public interface InjectedPlugin {

    /**
     * This method will be fired during enabling phase of the loader.
     */
    void onEnable();

    /**
     * This method will be fired during disabling phase of the loader.
     */
    void onDisable();

}
