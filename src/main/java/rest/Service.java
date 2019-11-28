package rest;
import database.Account;
import database.DataBase;
import database.Operation;
import entities.Replica;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.HashMap;

@Path("service")
public class Service {

    private DataBase dataBase = new DataBase();
    private ArrayList<Replica> replicas;
    private boolean coordinator = false;
    private int seed;

    @Path("accounts")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response showAccounts() {
        HashMap<String, ArrayList<Account>> hashMap = new HashMap<>();
        hashMap.put("contas", this.dataBase.obterListaDeContas());
        return Response.ok(hashMap).build();
    }

    @Path("replicas")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response uploadReplicas(HashMap<String, ArrayList<Replica>> hashMap) {
        this.replicas = hashMap.get("replicas");
        this.coordinator = true;
        return Response.ok("Replicas recebidas\n").build();
    }

    @Path("replicas")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response deleteReplicas(HashMap<String, ArrayList<Replica>> hashMap) {
        this.replicas.clear();
        this.coordinator = false;
        return Response.ok("Replicas apagadas\n").build();
    }

    @Path("operation")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response operation(Operation operation){
        if(coordinator){
            if(operation.getOperacao().equals("saque")){
                //armazenamento temporário (write-ahead log)
                //verificar nas replicas
                    //200 OK - persistir dados
                    //403 Forbidden - apagar
            }else if(operation.getOperacao().equals("deposito")){
                //armazenamento temporário (write-ahead log)
                //verificar nas replicas
                    //200 OK - persistir dados
                    //403 Forbidden - apagar
            }else return Response.ok("Operação inválida\n").build();
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
        if(coordinator){
            //400 Bad Request
        }else{
            //remover do armazenamento temporario
            //adicionar no armazenamento permanente
        }
        return Response.ok("Decision\n").build();
    }

    @Path("decision")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response rollbackDecision(String id){
        if(coordinator){
            //400 Bad Request
        }else{
            //remover do armazenamento temporario
        }
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
        this.seed = seed;
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