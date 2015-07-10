package com.telekeys.research.telekeys;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryankelley on 2/3/15.
 */
public class MyKeyboardView extends KeyboardView {

    public ArrayList<ArrayList<String>> badKeys;
    private int querty = -1;
    private boolean caps;
    private int keyboardType;
    private boolean color=false;
    private boolean superRare = true;

    public MyKeyboardView(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    public MyKeyboardView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }
    public boolean getColor(){
        return color;
    }
    public void turnOnColor(boolean state){
        color=state;
    }
    public void setBadKeys(ArrayList<ArrayList<String>> str, int type){
        if(type==-2)
            badKeys=null;
        else {
            keyboardType = type;
            badKeys = str;
        }
    }
    public void isCaps(boolean test){
        caps=test;
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(45);
        paint.setColor(Color.RED);

        String tmpKey;
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            superRare=true;
            tmpKey=key.label.toString();
           if(caps) {
               tmpKey = tmpKey.toUpperCase();
            }
            if (color) {
                if (tmpKey.equals("\u2022")) {
                    paint.setTextSize(100);
                    paint.setColor(Color.BLUE);
                    canvas.drawText("\u25B6", key.x + 78, key.y + 132, paint);
                    paint.setTextSize(45);
                    paint.setColor(Color.RED);
                }
                if(badKeys.size()!=0){
                        for (int j = 0; j < badKeys.get(0).size(); j++) {
                            if (badKeys.size() != 0 && keyboardType != querty && tmpKey.equals(badKeys.get(0).get(j))) {
                                paint.setColor(Color.RED);
                                canvas.drawText("\u25CF", key.x + 23, key.y + 48, paint);
                                superRare=false;
                            }
                        }
                        for (int j = 0; j < badKeys.get(1).size(); j++) {
                            if (badKeys.size() != 0 && keyboardType != querty && tmpKey.equals(badKeys.get(1).get(j))) {
                                paint.setColor(Color.rgb(255, 127, 0));
                                canvas.drawText("\u25CF", key.x + 23, key.y + 48, paint);
                                superRare=false;
                            }
                        }
                        for (int j = 0; j < badKeys.get(2).size(); j++) {
                            if (badKeys.size() != 0 && keyboardType != querty && tmpKey.equals(badKeys.get(2).get(j))) {
                                paint.setColor(Color.YELLOW);
                                canvas.drawText("\u25CF", key.x + 23, key.y + 48, paint);
                                superRare=false;
                            }
                        }
                        for (int j = 0; j < badKeys.get(3).size(); j++) {
                            if (badKeys.size() != 0 && keyboardType != querty && tmpKey.equals(badKeys.get(3).get(j))) {
                                paint.setColor(Color.rgb(127, 200, 0));
                                canvas.drawText("\u25CF", key.x + 23, key.y + 48, paint);
                                superRare=false;
                            }
                        }
                        for (int j = 0; j < badKeys.get(4).size(); j++) {
                            if (badKeys.size() != 0 && keyboardType != querty && tmpKey.equals(badKeys.get(4).get(j))) {
                                paint.setColor(Color.rgb(0, 170, 0));
                                canvas.drawText("\u25CF", key.x + 23, key.y + 48, paint);
                                superRare=false;
                            }
                        }
                }
                if(superRare    && !tmpKey.equals("\u2022")
                                && !tmpKey.equals("SPACE")
                                && !tmpKey.equals("DONE")
                                && !tmpKey.equals("CYCLE")
                                && !tmpKey.equals("\u21E6")
                                && !tmpKey.equals("\u21E7")
                                && keyboardType != querty){
                    paint.setColor(Color.rgb(0, 170, 0));
                    canvas.drawText("\u25CF", key.x + 23, key.y + 48, paint);
                }
            }
            if(!color) {
                if (tmpKey.equals("\u2022")) {
                    paint.setTextSize(100);
                    paint.setColor(Color.WHITE);
                    canvas.drawText("\u25B6", key.x + 78, key.y + 132, paint);
                    paint.setTextSize(45);
                    paint.setColor(Color.RED);
                }
            }
        }
    }
}
