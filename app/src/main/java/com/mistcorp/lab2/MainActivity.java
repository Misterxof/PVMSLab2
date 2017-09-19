package com.mistcorp.lab2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView tvInfo, tvSign, tvCount;
    EditText etInput;
    Button bControl, bExit, bChange, bRu, bEn;
    ImageView image;

    private Locale myLocale;
    Boolean check;
    Toast toast;
    int number, count, countRem;
    boolean gameFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = (TextView)findViewById(R.id.textView);
        tvSign = (TextView)findViewById(R.id.textView2);
        tvCount = (TextView)findViewById(R.id.textView3);
        etInput = (EditText)findViewById(R.id.editText);
        bControl = (Button)findViewById(R.id.button);
        bExit = (Button)findViewById(R.id.button2);
        bChange = (Button)findViewById(R.id.button3);
        bRu = (Button)findViewById(R.id.button4);
        bEn = (Button)findViewById(R.id.button5);
        image = (ImageView)findViewById(R.id.imageView);

        number = (int)(Math.random()*100);
        gameFinished = false;
        check = false;
        count = 0;
        countRem = 20;

        tvCount.setTextColor(Color.RED);
        tvSign.setTextColor(Color.RED);
        tvCount.setText("" + countRem);
        loadLocale();
    }

    public void onClick(View v){
        if (!gameFinished){
            if(etInput.getText().toString().equals("")){
                tvInfo.setText(getResources().getString(R.string.error));
                toast = Toast.makeText(this, getResources().getString(R.string.error), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 50, 0);
                toast.show();
            }
            else {
                if (count==0){
                    tvInfo.setTextColor(Color.LTGRAY);
                    tvInfo.setTextSize(14);
                }
                int userNumber = Integer.parseInt(etInput.getText().toString());

                if (userNumber < 0 || userNumber > 100) {
                    tvInfo.setText(getResources().getString(R.string.error));
                    toast = Toast.makeText(this, getResources().getString(R.string.error), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 50);
                    toast.show();
                }
                else {
                    if (userNumber > number) {
                        tvInfo.setText(getResources().getString(R.string.ahead));
                        toast = Toast.makeText(this, getResources().getString(R.string.ahead), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 50);
                        toast.show();
                        count++;
                        countRem--;
                        tvCount.setText("" + countRem);
                    }
                    if (userNumber < number) {
                        tvInfo.setText(getResources().getString(R.string.behind));
                        toast = Toast.makeText(this, getResources().getString(R.string.behind), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 50);
                        toast.show();
                        count++;
                        countRem--;
                        tvCount.setText("" + countRem);
                    }
                    if (userNumber == number)
                    {
                        tvInfo.setText(getResources().getString(R.string.hit));
                        toast = Toast.makeText(this, getResources().getString(R.string.hit), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 50);
                        toast.show();
                        bControl.setText(getResources().getString(R.string.play_more));
                        gameFinished = true;
                        image.setVisibility(View.VISIBLE);
                        count = 0;
                        countRem = 20;
                        tvCount.setText("" + countRem);
                    }
                }
            }
            if (count == 10){
                tvInfo.setTextColor(Color.RED);
                tvInfo.setTextSize(20);
                tvInfo.setText(getResources().getString(R.string.miss));
                count = 0;
            }
            if(countRem == 0){
                number = (int)(Math.random()*100);
                bControl.setText(getResources().getString(R.string.input_value));
                tvInfo.setText(getResources().getString(R.string.over));
                bControl.setText(getResources().getString(R.string.play_more));
                toast = Toast.makeText(this, getResources().getString(R.string.over), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 50);
                toast.show();
                gameFinished = true;
                count = 0;
                countRem = 20;

                tvCount.setText("" + countRem);
            }
        }
        else {
            number = (int)(Math.random()*100);
            bControl.setText(getResources().getString(R.string.input_value));
            tvInfo.setText(getResources().getString(R.string.try_to_guess));
            gameFinished = false;
            image.setVisibility(View.INVISIBLE);
            count = 0;
        }
        etInput.setText("");
    }

    public void onClickExit(View v){
        this.finish();
    }

    public void onClickChange(View v){
        if(!check) {
            tvInfo.setTextColor(Color.RED);
            RelativeLayout fon = (RelativeLayout) findViewById(R.id.activity_main);
            fon.setBackgroundColor(Color.BLUE);
            bControl.setBackgroundColor(Color.GREEN);
            check = true;
        }
        else {
            tvInfo.setTextColor(Color.LTGRAY);
            RelativeLayout fon = (RelativeLayout) findViewById(R.id.activity_main);
            fon.setBackgroundColor(Color.DKGRAY);
            bControl.setBackgroundColor(Color.GRAY);
            check = false;
        }
    }

    public void onClickRu(View v){
        String lang = "ru";
        changeLang(lang);
    }

    public void onClickEn(View v){
        String lang = "en";
        changeLang(lang);
    }

    public void changeLang(String lang)
    {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        updateTexts();
    }

    public void saveLocale(String lang)
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    public void loadLocale()
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }

    private void updateTexts()
    {
        setTitle(R.string.app_name);

        tvInfo.setText(R.string.try_to_guess);
        bControl.setText(getResources().getString(R.string.input_value));
        bRu.setText(getResources().getString(R.string.ru));
        bEn.setText(getResources().getString(R.string.en));
        bExit.setText(getResources().getString(R.string.exit));
        bChange.setText(getResources().getString(R.string.change));
        tvSign.setText(getResources().getString(R.string.count));
    }
}
