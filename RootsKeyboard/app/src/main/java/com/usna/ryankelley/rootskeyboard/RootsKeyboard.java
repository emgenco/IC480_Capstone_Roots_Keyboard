package com.usna.ryankelley.rootskeyboard;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.nfc.Tag;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryankelley on 1/19/15.
 */
public class RootsKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener{

    private MyKeyboardView k;
    private Keyboard keyboard;
    private boolean caps = false;
    private String[] badKeys = new String[3];
    private int KEYBOARD_TYPE;
    public List<String> probs;
    ArrayList<ArrayList<String>> master = new ArrayList<ArrayList<String>>();
    public com.usna.ryankelley.rootskeyboard.bigramModel.bigram b;

    @Override
    public View onCreateInputView() {
        b = new com.usna.ryankelley.rootskeyboard.bigramModel.bigram();
        System.out.println(System.getProperty("user.dir"));
        Resources res = getResources();
        InputStream readTxt = res.openRawResource(R.raw.bigram_probs);

        probs = b.readBigrams(readTxt);
        k = (MyKeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.roots);
        k.setKeyboard(keyboard);
        k.setOnKeyboardActionListener(this);
        return k;
    }

    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        switch(attribute.inputType&EditorInfo.TYPE_MASK_CLASS){
            case EditorInfo.TYPE_CLASS_TEXT:
                int variation = attribute.inputType & EditorInfo.TYPE_MASK_VARIATION;
                if(variation==EditorInfo.TYPE_TEXT_VARIATION_PASSWORD) {
                    KEYBOARD_TYPE=0;
                    k.setBadKeys(master, 0);
                    k.invalidateAllKeys();
                    keyboard = new Keyboard(this, R.xml.roots);
                    k.setKeyboard(keyboard);
                }
                else {
                    KEYBOARD_TYPE=-1;
                    k.setBadKeys(master, -1);
                    k.invalidateAllKeys();
                    keyboard = new Keyboard(this, R.xml.qwerty);
                    k.setKeyboard(keyboard);
                }
                break;
            default:
                KEYBOARD_TYPE=-1;
                k.setBadKeys(master, -1);
                k.invalidateAllKeys();
                keyboard = new Keyboard(this, R.xml.qwerty);
                k.setKeyboard(keyboard);
        }

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes){
        InputConnection ic = getCurrentInputConnection();
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                k.isCaps(caps);
                k.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                master=null;
                k.setBadKeys(master, -2);
                k.invalidateAllKeys();
                master=new ArrayList<ArrayList<String>>();
                requestHideSelf(0);
                break;
            case -7:
               keyboard = new Keyboard(this, R.xml.roots2);
               k.setKeyboard(keyboard);
               break;
            case -8:
               keyboard = new Keyboard(this, R.xml.roots3);
               k.setKeyboard(keyboard);
               break;
            case -10:
                keyboard = new Keyboard(this, R.xml.roots);
                k.setKeyboard(keyboard);
                break;
            case -11:
                keyboard = new Keyboard(this, R.xml.querty2);
                k.setKeyboard(keyboard);
                break;
            case -12:
                keyboard = new Keyboard(this, R.xml.qwerty);
                k.setKeyboard(keyboard);
                break;
            case -13:
                keyboard = new Keyboard(this, R.xml.querty3);
                k.setKeyboard(keyboard);
                break;
            case -14:
                if(k.getColor())
                    k.turnOnColor(false);
                else
                    k.turnOnColor(true);
                k.invalidateAllKeys();
                break;
            default:
                char code = (char)primaryCode;
                if(Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code), 1);
                master = b.getKeyColorList(String.valueOf(code), probs);
                //new stuff
                for(int i=0;i<5;i++) {
                    for (int j = 0; j < master.get(i).size(); j++) {
                        if (caps)
                            master.get(i).set(j, master.get(i).get(j).toUpperCase());
                    }
                }
                if(KEYBOARD_TYPE!=-1)
                    k.setBadKeys(master, 0);
                 else
                    k.setBadKeys(master, -1);
                k.invalidateAllKeys();
        }
    }
    @Override
    public void onPress(int primaryCode){

    }

    @Override
    public void onRelease(int primaryCode){

    }
    @Override
    public void onText(CharSequence text){

    }
    @Override
    public void swipeDown(){

    }
    @Override
    public void swipeLeft(){

    }
    @Override
    public void swipeRight(){

    }
    @Override
    public void swipeUp(){

    }

}
