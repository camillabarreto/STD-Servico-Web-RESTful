package database;

import java.util.ArrayList;

public class DataBase {
    private static ArrayList<Accounts> accounts;

    public DataBase() {
        this.accounts = new ArrayList<>();
        this.accounts.add(new Accounts("1234", "100,00"));
        this.accounts.add(new Accounts("4345", "50,00"));
        this.accounts.add(new Accounts("5678", "250,00"));
    }

    public ArrayList<Accounts> obterListaDeContas(){
        return accounts;
    }
}
