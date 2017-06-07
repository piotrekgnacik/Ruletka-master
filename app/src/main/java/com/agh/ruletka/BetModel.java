package com.agh.ruletka;


import android.support.annotation.ColorRes;


// klasa do ustawiania bet

public class BetModel {



    int mMoneyBet;
    int mPosition;
    Integer mColor;

    public BetModel(int moneyBet, int position){
        mMoneyBet = moneyBet;
        mPosition = position;
        mColor = null;
    }

    public BetModel(int moneyBet, int position, @ColorRes Integer color){
        mMoneyBet = moneyBet;
        mPosition = position;
        mColor = color;
    }

    //metoda do zwrotu ilosci postawionych pieniedzy
    public int getMoneyBet() {
        return mMoneyBet;
    }
    // metoda dodania ilosci postawienia pieniedzy
    public void addMoneyBet(int betAdd) {
        mMoneyBet += betAdd;
    }
    // do zwrotu pozycji
    public int getPosition() {
        return mPosition;
    }
    // do zwrotu koloru
    public Integer getColor() {
        return mColor;
    }
}
