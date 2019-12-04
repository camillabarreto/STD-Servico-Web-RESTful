package rest;
import database.Account;
import database.DataBase;
import database.Operation;
import database.Replica;
import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.HashMap;

/***********************************************************/
import org.glassfish.jersey.client.ClientConfig;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
/***********************************************************/

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
    public Response showAccounts() {
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
    public Response uploadReplicas(HashMap<String, ArrayList<Replica>> hashMap) {
        dataBase.setReplicas(hashMap.get("replicas"));
        dataBase.setCoordinator(true);
        return Response.ok("Replicas recebidas\n").build();
    }

    @Path("replicas")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response deleteReplicas(HashMap<String, ArrayList<Replica>> hashMap) {
        dataBase.clearReplicas();
        dataBase.setCoordinator(false);
        return Response.ok("Replicas apagadas\n").build();
    }

    @Path("operation")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response operation(Operation operation){
        if(dataBase.isCoordinator()){

            //armazenamento temporário (write-ahead log)
            dataBase.addOperation(operation);

            //verificar nas replicas
            boolean flag = true;
//            for(Replica replica : replicas){
//                WebTarget t = client.target(UriBuilder.fromUri(replica.getEndpoint()).build());
//                t.path("service/operation").request().accept(MediaType.TEXT_PLAIN).get(String.class);
//
//                if(t.equals("403")){
//                    flag = false;
//                    break;
//                }
//            }

            if(flag){
                //200 OK - persistir dados
            }else{
                //403 Forbidden - apagar
            }
        }else{
            //armazenamento temporário (write-ahead log)
            //responder 200 OK ou 403 Forbidden (P[200 OK] = 0.7)

        }
        return Response.ok("Operação realizada\n").build();
    }

    @Path("decision")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response commitDecision(String id){
        if(dataBase.isCoordinator()){
            //return 400 Bad Request
        }
        //remover do armazenamento temporario
        //adicionar no armazenamento permanente
        return Response.ok("Decision\n").build();
    }

    @Path("decision")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response rollbackDecision(String id){
        if(dataBase.isCoordinator()){
            //return 400 Bad Request
        }
        //remover do armazenamento temporario
        return Response.ok("Decision\n").build();
    }

    @Path("historic")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response showHistoric() {
        //mostrar acoes (id e status)
        return Response.ok().build();
    }

    @Path("seed")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response loadSeed(int seed) {
        dataBase.setSeed(seed);
        return Response.ok().build();
    }

    /***********************************************************/


    @GET
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response olaMundo() {
        String mensagem = "Ola mundo\n";
        return Response.ok(mensagem).build();
    }

    @Path("{nome}")
    @GET
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response olaNome(@PathParam("nome") String nome) {
        String mensagem = "Ola " + nome + "\n";
        return Response.ok(mensagem).build();
    }

}