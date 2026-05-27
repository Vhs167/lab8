package lab8.server;


import lab8.common.dto.Chunk;
import lab8.common.dto.Request;
import lab8.common.dto.Response;
import lab8.common.network.ChunkUtils;
import lab8.common.utils.Serializer;
import lab8.server.auth.AuthService;
import lab8.server.database.UserRepository;
import lab8.server.handlers.RequestHandler;
import lab7.server.managers.*;
import lab8.server.managers.CollectionManager;
import lab8.server.managers.CommandManager;
import lab8.server.network.ChunkProcessor;
import lab8.server.network.DTLSTransport;
import lab7.server.utils.*;
import lab8.server.utils.ServerLogger;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;


public class Server {
    private final DTLSTransport transport;
    private final ChunkProcessor processor = new ChunkProcessor();
    private final RequestHandler handler;
    private final AuthService authService;

    private final ExecutorService readPool = Executors.newCachedThreadPool();
    private final ExecutorService processPool = Executors.newFixedThreadPool(8);
    private final ExecutorService sendPool = Executors.newFixedThreadPool(4);

    public Server(int port) throws Exception {


        this.transport = new DTLSTransport(port);
        this.authService = new AuthService(new UserRepository());

        CollectionManager cm = new CollectionManager();
        CommandManager cmd = new CommandManager(cm, authService);
        cm.loadCollection();

        this.handler = new RequestHandler(cmd);
    }

    public void start() throws Exception {

        transport.start((data, client) -> readPool.submit(() -> {
                try {
                    byte[] full = processor.onChunk(data);

                    if (full == null) return;

                    Request req = Serializer.deserialize(full);

                    processPool.submit(() -> {

                        try {
                            Response resp = handler.handle(req);

                            sendPool.submit(() -> {

                                try {
                                    UUID id = UUID.randomUUID();

                                    byte[] respBytes = Serializer.serialize(resp);

                                    List<Chunk> chunks = ChunkUtils.split(respBytes, id);

                                    for (Chunk c : chunks) {
                                        transport.send(Serializer.serialize(c), client);
                                    }
                                } catch (Exception e) {
                                    ServerLogger.logger.log(Level.WARNING, "Ошибка отправки", e);
                                }
                            });
                        } catch (Exception e) {
                            ServerLogger.logger.log(Level.WARNING, "Ошибка обработки");
                        }
                    });
                } catch (Exception e) {
                    ServerLogger.logger.log(Level.WARNING, "Ошибка чтения");
                }
            }));
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(11111);
        server.start();
        Thread.currentThread().join();
    }
}