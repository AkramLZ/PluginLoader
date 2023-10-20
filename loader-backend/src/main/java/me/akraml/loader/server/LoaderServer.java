package me.akraml.loader.server;

import me.akraml.loader.LoaderBackend;
import me.akraml.loader.utility.FileUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * This server handles incoming connections and requests to load the requested plugin.
 */
public final class LoaderServer {

    private final ServerSocket serverSocket;
    private final Thread serverThread;
    private final String fileName;

    public LoaderServer(final int bindingPort,
                        final String fileName) throws IOException {
        this.serverSocket = new ServerSocket(bindingPort);
        this.fileName = fileName;
        this.serverThread = new Thread("LoaderServer-Thread") {
            @Override
            public void run() {
                startHandler();
            }
        };
    }

    private void startHandler() {
        while (!serverSocket.isClosed()) {
            try (final Socket socket = serverSocket.accept()) {
                // Log the received connection.
                final String hostname = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
                LoaderBackend.getLogger().info("Received connection from /"
                        + hostname);

                // Declare input & output stream variables.
                final DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                // Declare file to be sent, convert it into bytes then send it.
                final File file = new File(fileName);
                final byte[] bytes = FileUtils.toByteArray(file);
                outputStream.write(bytes);
            } catch (final Exception exception) {
                if (exception instanceof SocketException) return;
                exception.printStackTrace(System.err);
            }
        }
    }

    public void shutdownServer() {
        try {
            serverSocket.close();
            serverThread.interrupt();
        } catch (final Exception exception) {
            LoaderBackend.getLogger().severe("An error occurred when trying to shut down loader server");
            exception.printStackTrace(System.err);
        }
    }

}
