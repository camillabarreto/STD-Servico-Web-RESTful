package database;

import java.util.ArrayList;

public class DataBase {
    private static ArrayList<Account> accounts;

    public DataBase() {
        this.accounts = new ArrayList<>();
        this.accounts.add(new Account("1234", "100,00"));
        this.accounts.add(new Account("4345", "50,00"));
        this.accounts.add(new Account("5678", "250,00"));
    }

    public ArrayList<Account> obterListaDeContas(){
        return accounts;
    }
}
