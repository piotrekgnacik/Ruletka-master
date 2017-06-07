package com.agh.ruletka;


//klasa urzytkownika
public class MyUser {



    private String name;
    private int money;
    private int iloscLosowan;

    MyUser(){
    }

    //konstruktor ustawiajacy imie gracza + ilsoc startowych pieniedzy
    MyUser(String name){
        this.name = name;
        money = 200;
        iloscLosowan = 0;
    }

    // metody zwracajace ilosc pieniedzy, ilosc losowan oraz imie gracza

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public int getIloscLosowan() {
        return iloscLosowan;
    }

    // metody obliczajace ilosc pieniedzy oraz obliczajace ilosc losowan

    protected void putMoney(int addMoney){
        money += addMoney;
    }

    protected void addIloscLosowan(){
        iloscLosowan++;
    }
}