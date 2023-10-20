package me.akraml.loader.server;

import me.akraml.loader.LoaderBackend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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

    public LoaderServer(final int bindingPort) throws IOException {
        serverSocket = new ServerSocket(bindingPort);
        serverThread = new Thread("LoaderServer-Thread") {
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
                final DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                final DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
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
