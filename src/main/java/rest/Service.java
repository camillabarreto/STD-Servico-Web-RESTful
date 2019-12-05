package rest;
import database.*;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
/**/
import org.glassfish.jersey.client.ClientConfig;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;


@Path("service")
public class Service {

    private DataBase dataBase = DataBase.getInstance();

    /***********************************************************/
    private ClientConfig config = new ClientConfig();
    private Client client = ClientBuilder.newClient(config);
    private final String URI = "http://localhost:8080/api/v1";
    private WebTarget target = client.target(UriBuilder.fromUri(URI).build());
    /***********************************************************/

    @Path("accounts")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAccounts() {
        HashMap<String, ArrayList<Account>> hashMap = new HashMap<>();
        hashMap.put("contas", this.dataBase.getAccounts());
        return Response.ok(hashMap).build();
    }

    @Path("replicas")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getReplicas() {
        //testando se está guardando a informação de coordenador
        if(dataBase.isCoordinator()) System.out.println("sou coordenador");
        HashMap<String, ArrayList<Replica>> hashMap = new HashMap<>();
        hashMap.put("replicas", dataBase.getReplicas());
        return Response.ok(hashMap).build();
    }

    @Path("replicas")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response setReplicas(HashMap<String, ArrayList<Replica>> hashMap) {
        dataBase.setReplicas(hashMap.get("replicas"));
        dataBase.setCoordinator(true);
        return Response.ok("Replicas recebidas\n").build();
    }

    @Path("replicas")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response clearReplicas(HashMap<String, ArrayList<Replica>> hashMap) {
        dataBase.clearReplicas();
        dataBase.setCoordinator(false);
        return Response.ok("Replicas apagadas\n").build();
    }

    @Path("operation")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response addOperation(Operation operation){
        if(dataBase.isCoordinator()){
            dataBase.addOperation(operation);
            boolean flag = true;
            for(Replica replica : dataBase.getReplicas()){
                WebTarget t = client.target(UriBuilder.fromUri(replica.getEndpoint()).build());
                Response response = t.path("service").path("operation").request(MediaType.APPLICATION_JSON).put(Entity.entity(operation,MediaType.APPLICATION_JSON), Response.class);
                if(response.getEntity().equals("403")){
                    flag = false;
                    break;
                }
            }
            if(flag){
                //enviar decisao PUT
                //realizar a operação
                this.dataBase.addActions(operation.getId(), "success");
                return Response.ok("200 OK\n").build();
            }else{
                //enviar decisao DELETE
                this.dataBase.addActions(operation.getId(), "fail");
                return Response.ok("403 Forbidden\n").build();
            }
        }else{
            this.dataBase.addOperation(operation);
            Random gerador = new Random(this.dataBase.getSeed());
            if(gerador.nextFloat() <= 0.7){
                return Response.ok("200").build();
            }else{
                return Response.ok("403").build();
            }
        }
    }

    @Path("decision")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response commitDecision(String id){
        if(dataBase.isCoordinator()){
            //return 400 Bad Request
        }
        this.dataBase.removeOperation(id);
        this.dataBase.addActions(id, "success");
        return Response.ok("ACK\n").build();
    }

    @Path("decision")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response rollbackDecision(String id){
        if(dataBase.isCoordinator()){
            return Response.ok("400 Bad Request\n").build();
        }
        this.dataBase.removeOperation(id);
        this.dataBase.addActions(id, "fail");
        return Response.ok("ACK\n").build();
    }

    @Path("historic")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getHistoric() {
        HashMap<String, ArrayList<Action>> hashMap = new HashMap<>();
        hashMap.put("acoes", this.dataBase.getActions());
        return Response.ok(hashMap).build();
    }

    @Path("seed")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response loadSeed(int seed) {
        dataBase.setSeed(seed);
        if (dataBase.isCoordinator()){
            WebTarget t = client.target(UriBuilder.fromUri(dataBase.getReplicas().get(0).getEndpoint()).build());
            t.path("service").path("seed").request(MediaType.APPLICATION_JSON).post(Entity.entity(12322,MediaType.APPLICATION_JSON), Response.class);

            t = client.target(UriBuilder.fromUri(dataBase.getReplicas().get(1).getEndpoint()).build());
            t.path("service").path("seed").request(MediaType.APPLICATION_JSON).post(Entity.entity(12333,MediaType.APPLICATION_JSON), Response.class);

            return Response.ok("200 OK").build();
        }
        return Response.ok().build();
    }

}