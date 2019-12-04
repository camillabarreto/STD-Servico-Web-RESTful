package database;

import java.util.ArrayList;

public class DataBase {
    private static DataBase instance;
    private ArrayList<Account> accounts;
    private ArrayList<Replica> replicas;
    private ArrayList<Operation> log;
    private boolean coordinator;
    private int seed;

    public DataBase() {
        this.accounts = new ArrayList<>();
        this.replicas = new ArrayList<>();
        this.log = new ArrayList<>();
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

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public ArrayList<Replica> getReplicas() {
        return replicas;
    }

    public void setReplicas(ArrayList<Replica> replicas) {
        this.replicas = replicas;
    }

    public ArrayList<Operation> getLog() {
        return log;
    }

    public void addOperation(Operation operation) {
        this.log.add(operation);
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

    //public boolean saque
    //public boolean deposito
}
