package thread;

import database.Operation;
import database.Replica;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

public class ThreadOperation extends Thread {

    private Operation operation;
    private Replica replica;
    private ClientConfig config = new ClientConfig();
    private Client client = ClientBuilder.newClient(config);
    private int status;

    public ThreadOperation(Operation operation, Replica replica) {
        this.operation = operation;
        this.replica = replica;
        this.status = 0;
    }

    @Override
    public void run() {
        super.run();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> enviando operação");
        WebTarget t = this.client.target(UriBuilder.fromUri(this.replica.getEndpoint()).build());
        Response response = t.path("operation").request(MediaType.TEXT_PLAIN + ";charset=utf-8").post(Entity.entity(this.operation,MediaType.APPLICATION_JSON), Response.class);
        this.status = response.getStatus();
    }

    public int getStatus() {
        return status;
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }
}
