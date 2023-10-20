package me.akraml.loader;

import lombok.Getter;
import me.akraml.loader.server.LoaderServer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The main class of the loader's backend server.
 */
public class LoaderBackend {

    @Getter
    private static final Logger logger = Logger.getLogger("main");

    public static void main(String... args) throws IOException {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tT] [%4$-3s] %5$s%n");
        final Map<String, String> argsMap = new HashMap<>();
        for (final String arg : args) {
            if (!arg.startsWith("--") || arg.replaceFirst("--", "").isEmpty() || !arg.contains("=")) continue;
            final String[] values = arg.replaceFirst("--", "").split("=");
            if (values.length < 2) continue;
            argsMap.put(values[0].toLowerCase(), args[1]);
        }
        final String serverPortValue = argsMap.get("port");
        if (serverPortValue == null) {
            logger.severe("Please specify a server port!");
            printUsage();
            return;
        }
        if (!isInt(serverPortValue)) {
            logger.severe("Please specify a valid server port!");
            printUsage();
            return;
        }
        final String fileName = argsMap.get("file");
        if (fileName == null) {
            logger.severe("Please specify a file!");
            printUsage();
            return;
        }
        if (!fileName.endsWith(".jar") || !new File(fileName).exists()) {
            logger.severe("Please specify a valid file!");
            printUsage();
            return;
        }
        final int port = Integer.parseInt(serverPortValue);
        final LoaderServer loaderServer = new LoaderServer(port, fileName);
        Runtime.getRuntime().addShutdownHook(new Thread(loaderServer::shutdownServer));
    }

    private static boolean isInt(final String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException ignore) {
            return false;
        }
        return true;
    }

    private static void printUsage() {
        logger.info("Usage: java -jar loader-backend.jar --port=<port> --file=<jar file>");
    }

}
