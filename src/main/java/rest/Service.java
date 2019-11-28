package rest;
import database.Account;
import database.DataBase;
import database.Pessoa;
import entities.Replica;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.HashMap;

@Path("service")
public class Service {

    private DataBase dataBase = new DataBase();
    private Replica replicas;
    private boolean coordinator = false;

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
    public Response uploadReplicas(HashMap<String, ArrayList<Replica>> hashMap, @Context UriInfo uriInfo) {
        System.out.println(hashMap.get("replicas").get(0));
        this.coordinator = true;
        return Response.ok("Replicas recebidas\n").build();
    }

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