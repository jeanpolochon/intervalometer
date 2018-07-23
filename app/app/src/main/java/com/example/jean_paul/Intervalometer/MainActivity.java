package com.example.jean_paul.Intervalometer;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity  {

    private Menu menu;
    private BluetoothDevice device;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning  = false;
    private int SCAN_PERIOD = 10000;
    private boolean mConnected = false;
    BluetoothLeScanner mBluetoothLeScanner;
    BluetoothGatt mGatt;
    Handler mHandler;
    HashMap mScanResults;
    BtleScanCallback mScanCallback;
    UUID SERVICE_UUID = UUID.fromString("0000a000-0000-1000-8000-00805f9b34fb");


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_connect, menu);
        this.menu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ScanAndConnect:
                startScan(true);
                return true;
            case R.id.Scan:
                startScan(false);
                return true;
            case R.id.Disconnect:
                disconnectGattServer();
                return true;
            case R.id.StopScan:
                stopScan();
                return true;
            case R.id.Help:
                showHelp();
                return true;
            case R.id.About:
                showAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create an instance of the tab layout from the view.
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label2));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

    }


    @Override
    protected void onResume (){
        super.onResume();
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PlaceholderFragment1();
                case 1:
                    return new PlaceholderFragment2();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_label1);
                case 1:
                    return getString(R.string.tab_label2);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


    private void showHelp(){
        final AlertDialog.Builder d = new AlertDialog.Builder(MainActivity.this);
        d.setTitle("Help");
        d.setMessage("Who needs help anyway ?");
        d.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = d.create();
        alertDialog.show();
    }

    private void showAbout(){
        final AlertDialog.Builder d = new AlertDialog.Builder(MainActivity.this);
        d.setTitle("About");
        d.setMessage("This app was developed by Jean-Paul Marcade.");
        d.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = d.create();
        alertDialog.show();
    }



    /*BLUETOOTH LOW ENERGY CONNECTION*/
    private void startScan(boolean connectDirectly) {
        if (mScanning) {
            Log.d("BT", "Cannot scan now: already scanning)");
            Toast toast = Toast.makeText(this.getApplicationContext(), "Already scanning.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (!hasPermissions()) {
            Log.d("BT", "Requested user enables Bluetooth. Try starting the scan again.");
            return;
        }
        MenuItem scanOption = menu.findItem(R.id.ScanAndConnect);
        scanOption.setVisible(false);
        MenuItem stopScanOption = menu.findItem(R.id.StopScan);
        stopScanOption.setVisible(true);

        List<ScanFilter> filters = new ArrayList<>();
        ScanFilter scanFilter = new ScanFilter.Builder()
                .setServiceUuid(new ParcelUuid(SERVICE_UUID))
                .build();
        filters.add(scanFilter);
        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                .build();
        mScanResults = new HashMap<>();
        mScanCallback = new BtleScanCallback(connectDirectly);
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mBluetoothLeScanner.startScan(filters, settings, mScanCallback);
        mScanning = true;
        Log.d("BT", "Started scanning");
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopScan();
            }
        }, SCAN_PERIOD);
    }

    private void stopScan() {
        Log.d("BT", "Stopped scanning");
        if (mScanning && mBluetoothAdapter != null && mBluetoothAdapter.isEnabled() && mBluetoothLeScanner != null) {
            MenuItem stopScanOption = menu.findItem(R.id.StopScan);
            stopScanOption.setVisible(false);
            mBluetoothLeScanner.stopScan(mScanCallback);
            scanComplete();
        }

        mScanCallback = null;
        mScanning = false;
        mHandler = null;
    }

    private void scanComplete() {
        if (mScanResults.isEmpty()) {
            Log.d("BT", "No device found");
            Toast toast = Toast.makeText(getBaseContext(), "No device found", Toast.LENGTH_SHORT);
            toast.show();
            MenuItem scanOption = menu.findItem(R.id.ScanAndConnect);
            scanOption.setVisible(true);
            return;
        }
        Set<String> deviceAddress = mScanResults.keySet();
        for (String adress :deviceAddress) {
            Log.d("BT", "Found device: " + adress);
        }
    }

    private boolean hasPermissions() {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Toast toast = Toast.makeText(this.getApplicationContext(), "Please activate Bluetooth first and scan again.", Toast.LENGTH_LONG);
            toast.show();

            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
            return false;
        }
        return true;
    }


    private void connectDevice(BluetoothDevice device) {
        GattClientCallback gattClientCallback = new GattClientCallback();
        mGatt = device.connectGatt(this, false, gattClientCallback);
    }

    public void disconnectGattServer() {
        mConnected = false;
        if (mGatt != null) {
            mGatt.disconnect();
            mGatt.close();
            MenuItem disconnectOption = menu.findItem(R.id.Disconnect);
            disconnectOption.setVisible(false);
            MenuItem scanOption = menu.findItem(R.id.ScanAndConnect);
            scanOption.setVisible(true);
            Log.d("BT", "Disconnecting from Gatt");
            Toast toast = Toast.makeText(MainActivity.this.getApplicationContext(), "Disconnected from " + device.getName(), Toast.LENGTH_SHORT);
            toast.show();
        }
        else
            Log.d("BT", "Nothing to be disconnected from");
    }




    private class GattClientCallback extends BluetoothGattCallback {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (status == BluetoothGatt.GATT_FAILURE) {
                disconnectGattServer();
                Log.d("BT", "GATT Failure");
                return;
            } else if (status != BluetoothGatt.GATT_SUCCESS) {
                disconnectGattServer();
                Log.d("BT", "GATT Failure");
                return;
            }
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d("BT", "GATT Connected");
                //Advertise on main UI
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d("UI thread", "I am the UI thread");
                        Toast toast = Toast.makeText(MainActivity.this.getBaseContext(), "Connected to " + device.getName(), Toast.LENGTH_LONG);
                        toast.show();
                        MenuItem disconnectOption = menu.findItem(R.id.Disconnect);
                        disconnectOption.setTitle("Disconnect from " + device.getName());
                        disconnectOption.setVisible(true);
                    }
                });
                mConnected = true;
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                disconnectGattServer();
                Log.d("BT", "GATT Failure");
            }
        }
    }


    private class BtleScanCallback extends ScanCallback {
        private boolean connectDirectly;

        BtleScanCallback (boolean connectDirectly)
        {
            this.connectDirectly=connectDirectly;
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            addScanResult(result);
        }
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult result : results) {
                addScanResult(result);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("BT", "BLE Scan Failed with code " + errorCode);
        }

        private void addScanResult(ScanResult result) {
            device = result.getDevice();
            String deviceAddress = device.getAddress();
            if (connectDirectly)
            {
                Log.d("BT", "Found device with address: " + deviceAddress + " (" +device.getName() + "). Connecting directy to this one.");
                MenuItem stopScanOption = menu.findItem(R.id.StopScan);
                stopScanOption.setVisible(false);
                Toast toast = Toast.makeText(MainActivity.this.getApplicationContext(), "Found device " + device.getName(), Toast.LENGTH_SHORT);
                toast.show();
                mBluetoothLeScanner.stopScan(mScanCallback);
                mScanCallback = null;
                mHandler.removeCallbacksAndMessages(null);
                mScanning = false;
                mHandler = null;
                connectDevice(device);
            }
            else
                mScanResults.put(deviceAddress, device);
        }
    };

}
