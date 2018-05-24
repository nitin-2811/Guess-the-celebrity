package com.example.nitin.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> celebUrl;
    ArrayList<String> celebimg;
    ImageView imageView;
    Button button1,button2,button3,button4;
    int correctPosition=0;

    public void nextQuestion(View view){

        ImageDownload imagetask=new ImageDownload();
        Random random=new Random();
        Bitmap myImage;
        int incorrectPosition=0;
        int l=celebimg.size();
        correctPosition=random.nextInt(l);
        imageView=(ImageView) findViewById(R.id.imageView);
        int pos=random.nextInt(4);

        try{
            Log.i(celebUrl.get(correctPosition),celebimg.get(correctPosition));
            myImage=imagetask.execute(celebimg.get(correctPosition)).get();
            imageView.setImageBitmap(myImage);
            String array[]=new String[4];
            for(int i=0;i<4;i++)
            {
                if(i==pos){
                    array[i]=celebUrl.get(correctPosition);

                }
                else {
                    incorrectPosition = random.nextInt(l);
                    while(correctPosition==incorrectPosition){
                        incorrectPosition = random.nextInt(l);
                    }
                    array[i]=celebUrl.get(incorrectPosition);
                }
            }
            button1.setText(array[0]);
            button2.setText(array[1]);
            button3.setText(array[2]);
            button4.setText(array[3]);
        }
        catch(Exception e){
            e.printStackTrace();
        }



    }


    public void checkAnswer(View view) {
        Button button = (Button) view;
        String c = (String) button.getText();
        if (c.equals(celebUrl.get(correctPosition))) {
            Toast.makeText(getApplicationContext(), "Correct Answer", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(getApplicationContext(), "Wrong Answer...the answer is "+celebUrl.get(correctPosition), Toast.LENGTH_SHORT).show();

        }
        nextQuestion(button);
    }
    public  class ImageDownload extends AsyncTask<String,Void,Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... urls) {
            URL url;
            HttpURLConnection connection;
            String result="";
            try {
                url = new URL(urls[0]);
                Log.i("Link",urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                Log.i("dsffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff","sdddddddddddddddddddddddddddddddddddddddddddd");

                return bitmap;
            }
            catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }
    public class DownloadUrl extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection connection;
            String result="";
            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while (data != -1) {
                    result += (char) data;
                    data = reader.read();
                }
                return result;
            }
            catch (Exception e){
                e.printStackTrace();
                return "Failed";
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String  result=null;
        celebUrl=new ArrayList<String>();
        celebimg=new ArrayList<String>();
        DownloadUrl task=new DownloadUrl();
        button1=(Button) findViewById(R.id.button1);
        button2=(Button) findViewById(R.id.button2);
        button3=(Button) findViewById(R.id.button3);
        button4=(Button) findViewById(R.id.button4);
        try{
            result=task.execute("http://www.posh24.se/kandisar").get();

            String[] splitstring=result.split("<div class=\"col-xs-12 col-sm-6 col-md-4\">");


            Pattern pattern = Pattern.compile("img src=\"(.*?)\"");
            Matcher matcher = pattern.matcher(splitstring[0]);
            while (matcher.find()) {
                celebimg.add(matcher.group(1));
                System.out.println(matcher.group(1));
            }

            pattern = Pattern.compile("alt=\"(.*?)\"");
            matcher = pattern.matcher(splitstring[0]);
            while (matcher.find()) {
                celebUrl.add(matcher.group(1));
                System.out.println(matcher.group(1));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        nextQuestion(button1);


    }
}
