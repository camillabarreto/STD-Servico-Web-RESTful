package rest;

import database.*;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@Path("service02")
public class Service02 {

    private DataBase02 dataBase = DataBase02.getInstance();
    String name = "service02";

    /***********************************************************/
    private ClientConfig config = new ClientConfig();
    private Client client = ClientBuilder.newClient(config);
    /***********************************************************/

    @Path("accounts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccounts() {
        HashMap<String, ArrayList<Account>> hashMap = new HashMap<>();
        hashMap.put("contas", this.dataBase.getAccounts());
        return Response.ok(hashMap).build();
    }

    @Path("replicas")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReplicas() {
        HashMap<String, ArrayList<Replica>> hashMap = new HashMap<>();
        hashMap.put("replicas", dataBase.getReplicas());
        return Response.ok(hashMap).build();
    }

    @Path("replicas")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setReplicas(HashMap<String, ArrayList<Replica>> hashMap) {
        dataBase.setReplicas(hashMap.get("replicas"));
        dataBase.setCoordinator(true);
        return Response.ok().build();
    }

    @Path("replicas")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response clearReplicas(HashMap<String, ArrayList<Replica>> hashMap) {
        dataBase.clearReplicas();
        dataBase.setCoordinator(false);
        return Response.ok().build();
    }

    @Path("operation")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOperation(Operation operation){
        if(dataBase.isCoordinator()){
            System.out.println("sou coordenador "+name);
            dataBase.addOperation(operation);
            boolean flag = true;
            for(Replica replica : dataBase.getReplicas()){
                WebTarget t = client.target(UriBuilder.fromUri(replica.getEndpoint()).build());
                Response response = t.path(replica.getId()).path("operation").request(MediaType.TEXT_PLAIN + ";charset=utf-8").put(Entity.entity(operation,MediaType.APPLICATION_JSON), Response.class);
                System.out.println("Status de resposta: "+response.getStatus());

                if(response.getStatus() == 403){
                    flag = false;
                    break;
                }
            }
            if(flag){
                //enviar decisao PUT
                System.out.println("\nDecisão PUT");
                //realizar a operação
                this.dataBase.addActions(operation.getId(), "success");
                return Response.ok().build();
            }else{
                //enviar decisao DELETE
                System.out.println("\nDecisão DELETE");
                this.dataBase.addActions(operation.getId(), "fail");
                return Response.status(403).build();
            }
        }else{
            System.out.println("NAO sou coordenador "+name);
            this.dataBase.addOperation(operation);
            float number = dataBase.getRandomNumber();
            System.out.println("number: "+number);
            if(number <= 0.7){
                return Response.ok().build();
            }else{
                return Response.status(403).build();
            }
        }
    }

    @Path("decision")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response commitDecision(String id){
        if(dataBase.isCoordinator()){
            return Response.status(400).build();
        }
        this.dataBase.removeOperation(id);
        this.dataBase.addActions(id, "success");
        return Response.ok().build();
    }

    @Path("decision")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response rollbackDecision(String id){
        if(dataBase.isCoordinator()){
            return Response.status(400).build();
        }
        this.dataBase.removeOperation(id);
        this.dataBase.addActions(id, "fail");
        return Response.ok().build();
    }

    @Path("historic")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHistoric() {
        HashMap<String, ArrayList<Action>> hashMap = new HashMap<>();
        hashMap.put("acoes", this.dataBase.getActions());
        return Response.ok(hashMap).build();
    }

    @Path("seed")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loadSeed(int seed) {
        dataBase.setSeed(seed);
        if (dataBase.isCoordinator()){
            Replica replica = dataBase.getReplicas().get(0);
            WebTarget t = client.target(UriBuilder.fromUri(replica.getEndpoint()).build());
            Response response = t.path(replica.getId()).path("seed").request().post(Entity.entity(12322,MediaType.APPLICATION_JSON), Response.class);
            System.out.println("Status de resposta: "+response.getStatus());

            replica = dataBase.getReplicas().get(1);
            t = client.target(UriBuilder.fromUri(replica.getEndpoint()).build());
            response = t.path(replica.getId()).path("seed").request().post(Entity.entity(12333,MediaType.APPLICATION_JSON), Response.class);
            System.out.println("Status de resposta: "+response.getStatus());

            return Response.ok().build();
        }
        return Response.ok().build();
    }

}