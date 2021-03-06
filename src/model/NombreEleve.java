package model;

import controller.ElementControl;

import java.math.BigDecimal;

public class NombreEleve implements AbstractFormula {
    /**
     * Méthode permettant de mettre à jour une valeur de l'indicateur à partir d'un levier
     * @param indicator L'indicateur sur lequel agit le levier
     * @param lever Le levier qui agit sur l'indicateur
     */
    @Override
    public void updateByOneLever(Indicator indicator, Lever lever) { }

    /**
     * Méthode permettant de mettre à jour la valeur de l'indicateur à partir de ses leviers
     * @param indicator L'indicateur sur lequel le levier agit
     */
    @Override
    public void updateByLevers(Indicator indicator) {
        BigDecimal value = new BigDecimal(85000).multiply(new BigDecimal(courbeMalus(ElementControl.getInstance().getElement("réputation des formations").getValue())/100.0));
        System.out.println("---#--# 85 000 * "+courbeMalus(ElementControl.getInstance().getElement("réputation des formations").getValue())/100.0+" = "+value+" #--#---");
        value = value.multiply(new BigDecimal(courbe1(ElementControl.getInstance().getElement("fFrais d'inscription").getValue())));
        System.out.println("---#--# "+value+" #--#---");
        long valueL = (value.longValue() + indicator.getValue()*3) /4;
        indicator.setValue(valueL);
    }

    /**
     * Méthode renvoyant la valeur Y sur une courbe pour un X donné
     * @param value Valeur X dont on cherche la valeur Y sur la courbe
     * @return Valeur Y correspondant au X donné sur la courbe
     */
    public static double courbe1(long value){
        double valCourbe;
        if (value <=0){
            valCourbe = 1;
        }
        else if (value <=234){
            valCourbe = -0.0012820512*value+1;
        }
        else if (value <= 5000){
            valCourbe = -0.0000839278*value+0.7196;
        }
        else if (value <= 10000){
            valCourbe = -0.0000218*value + 0.409;
        }
        else if (value <= 20000){
            valCourbe = -0.00000027*value+0.0057;
        }
        else if (value <= 30000){
            valCourbe = -0.000000027*value+0.00084;
        }
        else if (value <= 40000){
            valCourbe = -0.000000002*value+0.00009;
        }
        else if (value <= 50000){
            valCourbe = -0.0000000001*value +0.000014;
        }
        else if (value <= 60000){
            valCourbe = -0.0000000006*value+0.000039;
        }
        else {
            valCourbe = 0.000002;
        }
        return valCourbe;
    }

    /**
     * Méthode renvoyant la valeur Y sur une courbe pour un X donné
     * @param value Valeur X dont on cherche la valeur Y sur la courbe
     * @return Valeur Y correspondant au X donné sur la courbe
     */
    public static long courbeMalus(long value){
        long valCourbe;
        if(value<0){
            valCourbe=(long)(0.05);
        } else if(value<=16){
            valCourbe = (long)(0.621875*value+0.005);
        } else if (value<=50){
            valCourbe = (long)(1.1764706*value-8.8235294);
        }
        else if (value<=100){
            valCourbe=(long)(value);
        }
        else {
            valCourbe=100;
        }
        return valCourbe;
    }
}
