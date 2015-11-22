package ca.toronto.csc301.chat;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TimerTask;

/**
 * Created by Priyen on 2015-11-22.
 */
public class KeepAliveTask extends TimerTask {
    private static KeepAliveTask ourInstance = new KeepAliveTask();
    private HashMap<String, Boolean> stamps = new HashMap<String, Boolean>();
    public static KeepAliveTask getInstance() {
        return ourInstance;
    }

    private KeepAliveTask() {
    }

    public void event(String mac){
        stamps.put(mac, true);
    }

    public void run() {
        Event e = new Event();
        e.setSender(BluetoothAdapter.getDefaultAdapter().getAddress());
        e.setSenderName(BluetoothAdapter.getDefaultAdapter().getName());
        e.setType(5);
        ConnectionsList.getInstance().sendEvent(e);
        Set<String> macs = stamps.keySet();
        Iterator<String> maci = macs.iterator();
        while(maci.hasNext()){
            String mac = maci.next();
            Boolean alive = stamps.get(mac);
            if(alive == null){
                continue;
            }
            if(alive){
                stamps.put(mac, false);
            }
            else{
                stamps.put(mac, null);
                ConnectionsList.getInstance().closeConnection(mac);
            }
        }
    }
}
