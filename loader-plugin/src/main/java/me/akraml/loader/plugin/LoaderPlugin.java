package me.akraml.loader.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.DataInputStream;
import java.net.Socket;

/**
 * This plugin will load and injected the targeted plugin.
 */
public final class LoaderPlugin extends JavaPlugin {

    private static LoaderPlugin INSTANCE;
    private ByteClassLoader classLoader;
    private InjectedPlugin injectedPlugin;

    @Override
    public void onEnable() {
        INSTANCE = this;
        final long start = System.currentTimeMillis();
        // Load config & server information from it.
        saveDefaultConfig();
        final String address = getConfig().getString("loader-server.address");
        final int port = getConfig().getInt("loader-server.port");

        // Create a new socket to process loading.
        try (final Socket socket = new Socket(address, port);
             final DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {

            // Read sent information by the server in order.
            final String mainClassName = dataInputStream.readUTF();
            final int size = dataInputStream.readInt();
            final byte[] fileBytes = new byte[size];
            for (int i = 0; i < size; i++) {
                fileBytes[i] = dataInputStream.readByte();
            }

            // Process plugin injection.
            this.classLoader = new ByteClassLoader(fileBytes);
            //noinspection deprecation
            this.injectedPlugin = classLoader.loadClass(mainClassName, false)
                    .asSubclass(InjectedPlugin.class).newInstance();

        } catch (final Exception exception) {
            exception.printStackTrace(System.err);
            getLogger().severe("Failed to inject the plugin.");
            setEnabled(false);
            return;
        }
        getLogger().info("Successfully injected plugin, process took ");
        // Since everything performed well, it's the time to load the plugin.
        injectedPlugin.onEnable();
    }

    @Override
    public void onDisable() {
        // If any of the class loader or injected plugin is null, it means it wasn't loaded properly.
        if (classLoader == null || injectedPlugin == null) return;
        // Process injected plugin disabling.
        injectedPlugin.onDisable();
        // Kill the class loader (It will be just garbage collected).
        classLoader = null;
        // Unload the instance to be garbage collected.
        INSTANCE = null;
    }

    public static LoaderPlugin getInstance() {
        return INSTANCE;
    }

}
