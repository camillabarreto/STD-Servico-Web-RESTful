package database;

import java.util.ArrayList;

public class DataBase {
    private static DataBase instance;
    private ArrayList<Account> accounts;
    private ArrayList<Replica> replicas;
    private ArrayList<Operation> operations; //lista temporaria
    private ArrayList<Action> actions;       //lista permanente
    private boolean coordinator;
    private int seed;

    public DataBase() {
        this.accounts = new ArrayList<>();
        this.replicas = new ArrayList<>();
        this.operations = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.coordinator = false;
        this.seed = 0;

        this.accounts.add(new Account("1234", "100,00"));
        this.accounts.add(new Account("4345", "50,00"));
        this.accounts.add(new Account("5678", "250,00"));
    }

    public static DataBase getInstance() {
        if(instance == null){
            instance = new DataBase();
        }
        return instance;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public ArrayList<Replica> getReplicas() {
        return replicas;
    }

    public ArrayList<Action> getActions(){ return actions; }

    public void setReplicas(ArrayList<Replica> replicas) {
        this.replicas = replicas;
    }

    public void addOperation(Operation operation) {
        this.operations.add(operation);
    }

    public void removeOperation(String id) {
        for(Operation operation : operations){
            if(operation.getId().equals(id)){
                this.operations.remove(operation);
                return;
            }
        }
    }

    public boolean isCoordinator(){ return this.coordinator; }

    public void setCoordinator(boolean coordinator){ this.coordinator = coordinator; }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public void clearReplicas(){
        this.replicas.clear();
    }

    public void addActions(String id, String status) {
        this.actions.add(new Action(id, status));
    }

}
