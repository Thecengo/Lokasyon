package com.example.asuss.lokasyon;

import java.util.ArrayList;

/**
 * Created by asuss on 19.09.2017.
 */

public class Geometri {


    public ArrayList<Double> uzaklıkHesapla(double currentPointX,double currentPointY,
                                            ArrayList<Double> locx,ArrayList<Double> locY) {




        ArrayList<Double> uzaklık=new ArrayList<>();


        for(int i = 0;i<locx.size();i++) {
            uzaklık.add(Math.sqrt(Math.pow((currentPointX-locx.get(i)), 2)+
                    Math.pow((currentPointY-locY.get(i)), 2)));

        }


        return uzaklık;

    }
    public int indisDöndür(ArrayList<Double> uzaklıkPar) {
        int indis = 0;


        double min = uzaklıkPar.get(0);

        for(int i =1;i<uzaklıkPar.size();i++) {
            if(min >uzaklıkPar.get(i)) {
                min =uzaklıkPar.get(i);
                indis = i;
            }
        }

        return indis;


    }

}
