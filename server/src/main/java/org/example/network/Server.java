package org.example.network;

import org.example.utility.ConsoleOutput;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class Server {
    private final String host;
    private final int port;
    private ServerSocketChannel serverSocketChannel;
    private static final Logger logger = Logger.getLogger(Server.class.getName());


    public Server(String host,int port, ConsoleOutput consoleOutput) {
        this.host = host;
        this.port = port;
    }

    /**
     * Запускает серверный сокет и начинает прослушивание входящих соединений.
     */
    public void start()  {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(host, port));
            serverSocketChannel.configureBlocking(false);
            logger.info("Сервер запущен на хосте " + host + " по порту " + port);
        } catch (IOException e) {
            logger.warning("Произошла ошибка при запуске сервера: " + e.getMessage());
        }
    }




    /**
     * Корректно завершает работу сервера.
     */
    public void stop() {
        try {
            if (serverSocketChannel != null) {
                serverSocketChannel.close();
                logger.info("serverSocketChannel закрыт");
            }
        } catch (IOException e) {
            logger.warning("Ошибка при остановке сервера: " + e.getMessage());
        }
    }

    public ServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }
    public String getHost() {
        return host;
    }
    public int getPort() {
        return port;
    }
}