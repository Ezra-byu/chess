import serverFacade.ServerFacade;
import server.Server;

public class PostLoginMenu {
    private static Server server;
    static ServerFacade serverFacade;

    public PostLoginMenu(String serverUrl) {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var url = "http://localhost:" + port;
        serverFacade = new ServerFacade(url);
    }
}
