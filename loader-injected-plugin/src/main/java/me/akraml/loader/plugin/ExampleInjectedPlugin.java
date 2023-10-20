package me.akraml.loader.plugin;

/**
 * This plugin is an example to test loader if it's performing well.
 */
public final class ExampleInjectedPlugin implements InjectedPlugin {

    @Override
    public void onEnable() {
        LoaderPlugin.getInstance().getLogger().info("Plugin is now injected!");
    }

    @Override
    public void onDisable() {
        LoaderPlugin.getInstance().getLogger().info("Plugin is now rejected!");
    }

}
