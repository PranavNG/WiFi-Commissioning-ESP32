/* This Application allows user to control an LED
        through the same WiFi network.
        Written by:- Pranav Ghatigar
        Completed on:- 30/07/2020
*/
                                                                                                    //All Pre-Processors used
package com.example.led;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
                                                                                                    //Main class
public class MainActivity3 extends AppCompatActivity {
                                                                                                    //Global variable(used throughout the class)
    String ipadd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
                                                                                                    //Variables assigned to each component
        Intent intent=getIntent();
        ipadd= intent.getStringExtra(MainActivity2.ipad);

    }
                                                                                                    //Function called when button is pressed
    public void button_click(View view){
        String mes="1";
        Toast.makeText(this,"LED ON",Toast.LENGTH_SHORT).show();                        //Make a Toast
        BackgroundTask b= new BackgroundTask();                                                     //Create an object
        b.execute(ipadd, mes);                                                                      //Using object, call function
    }
                                                                                                    //Function called when button is pressed
    public void button_lick(View v){
        String mes="0";
        Toast.makeText(this,"LED OFF",Toast.LENGTH_SHORT).show();                       //Make a Toast
        BackgroundTask b= new BackgroundTask();                                                     //Create and object
        b.execute(ipadd,mes);                                                                       //Using object, call function
    }
                                                                                                    //Define a SubClass
    static class BackgroundTask extends AsyncTask<String,Void,String>
    {                                                                                               //This class is used to perform Socket communication
        Socket s;                                                                                   //Declare variables used
        DataOutputStream dos;
        String ip;
        String message;
                                                                                                    //Function is called in the previous class of return type String
        @Override
        protected String doInBackground(String... strings) {
            try
            {
                ip= strings[0];
                message=strings[1];
                s = new Socket(ip, 80);                                                        //Open Socket to specified IP Address and PORT number
                dos=new DataOutputStream(s.getOutputStream());                                      //To send instructions via sockets
                dos.writeBytes(message);
                dos.close();
                s.close();                                                                          //Close Socket once instruction is sent
            } catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
