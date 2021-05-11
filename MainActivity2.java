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
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
                                                                                                    //Main class
public class MainActivity2 extends AppCompatActivity {
                                                                                                    //Global variables(used throughout the class)
    public EditText Password;
    public String ssid;
    String mes, part2;
    private TextView name, data;
    public static final String ipad="com.example.led.ipad";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
                                                                                                    //Assign variables to each Component of the Main Activity
        name = (TextView) findViewById(R.id.editTextTextPersonName);
        Password=(EditText)findViewById(R.id.editTextTextPassword);
        Intent intent= getIntent();
        ssid= intent.getStringExtra(MainActivity.sec_text);
        name.setText(ssid);
        data= (TextView)findViewById(R.id.textView2);
                                                                                                    //Function called when button is clicked
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity3();                                                                //Function call
            }
        });
    }
                                                                                                    //Function to start Activity 3
    private void openMainActivity3()
    {
        Intent intent= new Intent(this, MainActivity3.class);
        intent.putExtra(ipad,part2);
        startActivity(intent);
    }
                                                                                                    //Function called when button is clicked
    public void button_click(View view) {
        mes=name.getText().toString()+"~"+Password.getText().toString();                            //Formatting the String
        BackgroundTask b= new BackgroundTask();                                                     //Create an Object
        b.execute("192.168.4.1",mes);                                                               //Parameterized Function call using object
    }
                                                                                                    //Function called when button is pressed
    public void button_scan(View view){
        startServerSocket();                                                                        //Function call
    }
                                                                                                    //Define a SubClass
    static class BackgroundTask extends AsyncTask<String,Void,String>
    {                                                                                               //This class is used to perform Socket communication.
                                                                                                    //Declare the variables used
        Socket s;
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
                s = new Socket(ip,80);                                                         //Open Socket to specified IP Address and PORT number
                dos=new DataOutputStream(s.getOutputStream());                                      //To send instructions via sockets
                dos.writeBytes(message);
                dos.close();                                                                        //Close the socket once instruction is sent
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
                                                                                                    //Function to start listening to broad casted info
    private void startServerSocket() {
                                                                                                    //Open a thread
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                byte[] msg = new byte[100000];
                DatagramPacket dp = new DatagramPacket(msg, msg.length);
                try (DatagramSocket ds = new DatagramSocket(9001)) {
                    ds.receive(dp);                                                                 //By using Datagram Packets and Sockets we receive info
                    String string = new String(msg, 0, dp.getLength());
                    String[] parts = string.split("~");                                       //Apply needed experiments to the received string
                    part2 = parts[1];
                    updateUI(part2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        thread.start();
    }
                                                                                                    //Function to update Text box in UI
    private void updateUI(String part2) {
        data.setText(part2);
    }
}