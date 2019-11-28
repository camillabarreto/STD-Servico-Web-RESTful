package rest;
import database.Accounts;
import database.DataBase;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.HashMap;

@Path("service")
public class Service {

    private DataBase contas = new DataBase();

    @Path("accounts")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response contas() {
        HashMap<String, ArrayList<Accounts>> hashMap = new HashMap<>();
        hashMap.put("contas", contas.obterListaDeContas());
        return Response.ok(hashMap).build();
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