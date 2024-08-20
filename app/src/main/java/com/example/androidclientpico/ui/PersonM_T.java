package com.example.androidclientpico.ui;

import android.util.Log;

import java.io.Serializable;
import java.util.Scanner;

public class PersonM_T{


MainActivity mainActivity =new MainActivity();
//    PersonM_T personM_t =new PersonM_T();


    public String getDATA_HOT() {
        return  mainActivity.DATA_HOT;
    }
    public String sk(){
        return mainActivity.SK_CONNECTED;
    }
    public String getFree_Not() {
        return mainActivity.Free_Not;
    }
    public String getIn_Game() {
        return mainActivity.In_Game;
    }
    public String getDATA_TIME() {
        return mainActivity.DATA_TIME;
    }

    public String getSK_CONNECTED() {
        System.out.println("22222222222222");
//        System.out.println(this.SK_CONNECTED);
        System.out.println(sk());
        return sk();
    }
    public String getEND_CODE() {

        return mainActivity.END_CODE;
    }


//    public void setFree_Not(String p) {
//        this.Free_Not = p;
//    }
//    public void setIn_Game(String In_Gam) {
//        In_Game = In_Gam;
//    }
//    public void setDATA_TIME(String DATA_TI) {
//        DATA_TIME = DATA_TI;
//    }
//    public void setSK_CONNECTED(String SK_CONNECTEDs) {
//        System.out.println("===================================333333333333");
//        System.out.println(SK_CONNECTEDs);
//        Scanner scanner = new Scanner(SK_CONNECTEDs);
//        SK_CONNECTED = scanner.nextLine();
//        System.out.println("===================================scanner.nextLinescanner.nextLinescanner.nextLinescanner.nextLine");
//        System.out.println(SK_CONNECTEDs);
//        sk();
//        System.out.println("===================================333333333333");
//        System.out.println(getSK_CONNECTED());
//        ;
//
//
//    }
}
