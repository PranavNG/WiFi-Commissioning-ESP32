/* This Application allows user to control an LED
through the same WiFi network.
Written by:- Pranav Ghatigar
Completed on:- 30/07/2020
 */

                                                                                                    //All Pre-Processors used
package com.example.led;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

                                                                                                    //Main class
public class MainActivity extends AppCompatActivity{
                                                                                                    //Global variables(used throughout the class)
    private WifiManager wifiManager;
    private ArrayList<String> arrayList= new ArrayList<>();
    @SuppressWarnings("rawtypes")
    private ArrayAdapter adapter;
    public String ssid;
    public static final String sec_text="com.example.led.sec_text";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                                                                                                    //Assign variables to each Component of the Main Activity
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.on_board);
        Button buttonScan = findViewById(R.id.scanBtn);
        ListView listView = findViewById(R.id.wifiList);
        wifiManager= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            Toast.makeText(this,"WiFi is disabled", Toast.LENGTH_SHORT).show();
            wifiManager.setWifiEnabled(true);
        }
        adapter= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);
        scanWifi();
        registerClickCallBack();
                                                                                                    //Function called when button is clicked
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity2();                                                                //Function call
            }
        });
                                                                                                    //Function called when button is clicked
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanWifi();                                                                         //Function call
            }
        });
    }
                                                                                                    //Function to check which WiFi network is selected by user.
    private void registerClickCallBack() {
        final ListView listView = findViewById(R.id.wifiList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ssid=listView.getItemAtPosition(position).toString();
                    Toast.makeText(MainActivity.this, ssid ,Toast.LENGTH_SHORT).show();
            }
        });
    }
                                                                                                    //Function to scan WiFi Networks
    private void scanWifi(){
        arrayList.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Toast.makeText(this, "Scanning WiFi", Toast.LENGTH_SHORT).show();
    }
                                                                                                    //Function to add the WiFi network to a list On-Screen
    BroadcastReceiver wifiReceiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<ScanResult> results = wifiManager.getScanResults();
            for(ScanResult scanResult: results){
                arrayList.add(scanResult.SSID);
                adapter.notifyDataSetChanged();
            }
            unregisterReceiver(this);
        }
    };
                                                                                                    //Function to start Activity 2
    public void openMainActivity2() {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra(sec_text,ssid);
        startActivity(intent);
    }
}