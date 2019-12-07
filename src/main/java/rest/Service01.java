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

@Path("service01")
public class Service01 {

    private DataBase01 dataBase = DataBase01.getInstance();
    String name = "service01";

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
                for(Replica replica : dataBase.getReplicas()){
                    WebTarget t = client.target(UriBuilder.fromUri(replica.getEndpoint()).build());
                    Response response = t.path(replica.getId()).path("decision").request().put(Entity.entity(operation.getId(),MediaType.APPLICATION_JSON), Response.class);
                    System.out.println("PUT - Status de resposta: "+response.getStatus());
                }
                performOperation(operation);
                this.dataBase.addActions(operation.getId(), "success");
                return Response.status(201).build();
            }else{
                for(Replica replica : dataBase.getReplicas()){
                    WebTarget t = client.target(UriBuilder.fromUri(replica.getEndpoint()).build());
                    Response response = t.path(replica.getId()).path("decision").path("/"+operation.getId()).request().delete();
                    System.out.println("DELETE - Status de resposta: "+response.getStatus());
                }
                this.dataBase.removeOperation(operation.getId());
                this.dataBase.addActions(operation.getId(), "fail");
                return Response.status(403).build();
            }
        }else{
            this.dataBase.addOperation(operation);
            float number = dataBase.getRandomNumber();
            System.out.println("sorteio: "+number);
            if(number <= 0.7){
                return Response.ok().build();
            }else{
                return Response.status(403).build();
            }
        }
    }

    private void performOperation(Operation operation){
        for(Account account : dataBase.getAccounts()){
            if(account.getNumero().equals(operation.getConta())){
                String saldo = account.getSaldo();
                saldo = saldo.replaceAll(",", ".");
                System.out.println("GET SALDO : " + saldo);
                double dsaldo = Double.parseDouble(saldo);

                String valor = operation.getValor();
                valor = valor.replaceAll(",",".");
                double dvalor = Double.parseDouble(valor);

                if(operation.getOperacao().equals("debito")){
                    dsaldo = dsaldo - dvalor;
                }else{
                    dsaldo = dsaldo + dvalor;
                }
                saldo = ""+dsaldo;
                account.setSaldo(saldo);
                break;
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
        boolean flag = false;
        for(Operation operation : dataBase.getOperations()){
            if(operation.getId().equals(id)){
                flag = true;
                break;
            }
        }
        if(!flag){
            return Response.status(404).build();
        }
        performOperation(this.dataBase.removeOperation(id));
        this.dataBase.addActions(id, "success");
        return Response.ok().build();
    }

    @Path("decision/{id}")
    @DELETE
    public Response rollbackDecision(@PathParam("id") String id){
        //Se ID não existir - retornar o código HTTP 404 Not Found.
        if(dataBase.isCoordinator()){
            return Response.status(400).build();
        }
        boolean flag = false;
        for(Operation operation : dataBase.getOperations()){
            if(operation.getId().equals(id)){
                flag = true;
                break;
            }
        }
        if(!flag){
            return Response.status(404).build();
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
        if (dataBase.isCoordinator()){
            Replica replica = dataBase.getReplicas().get(0);
            WebTarget t = client.target(UriBuilder.fromUri(replica.getEndpoint()).build());
            Response response = t.path(replica.getId()).path("seed").request().post(Entity.entity(12322,MediaType.APPLICATION_JSON), Response.class);

            replica = dataBase.getReplicas().get(1);
            t = client.target(UriBuilder.fromUri(replica.getEndpoint()).build());
            response = t.path(replica.getId()).path("seed").request().post(Entity.entity(12333,MediaType.APPLICATION_JSON), Response.class);

            return Response.ok().build();
        }
        dataBase.setSeed(seed);
        return Response.ok().build();
    }

}