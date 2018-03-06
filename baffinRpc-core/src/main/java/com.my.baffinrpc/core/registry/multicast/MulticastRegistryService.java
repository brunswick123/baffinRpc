package com.my.baffinrpc.core.registry.multicast;

import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.registry.NotifyListener;
import com.my.baffinrpc.core.registry.RegistryService;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MulticastRegistryService implements RegistryService{

    private MulticastSocket multicastSocket;
    private final ConcurrentHashMap<String,List<URL>> urlMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String,NotifyListener> notifyListenerMap = new ConcurrentHashMap<>();

    private static final String CMD_REGISTER = "cmd-register:";
    private static final String CMD_UNREGISTER = "cmd-unregister";
    private static final String CMD_SUBSCRIBE= "cmd-subscribe";


    public MulticastRegistryService(String address)
    {
        try {
            InetAddress inetAddress = InetAddress.getByName(address);
            multicastSocket = new MulticastSocket(6789);
            multicastSocket.setLoopbackMode(false);
            multicastSocket.joinGroup(inetAddress);
            final Thread receiveThread = new Thread(new Runnable() {
                DatagramPacket datagramPacket = new DatagramPacket(new byte[1024],1024);
                @Override
                public void run() {
                    while (!multicastSocket.isClosed()) {
                        try {
                            multicastSocket.receive(datagramPacket);
                            String msg = new String(datagramPacket.getData()).trim();
                            int index = msg.indexOf("\n");
                            if (index > 0)
                            {
                                String message = msg.substring(0,index);
                                MulticastRegistryService.this.received(message);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            receiveThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void received(String message)
    {
        if (message.startsWith(CMD_REGISTER))
        {
            String urlString = message.substring(CMD_REGISTER.length());
            URL url = URL.buildURLFromString(urlString);
            register(url.getInterfaceName(),url);
        }else if (message.startsWith(CMD_UNREGISTER))
        {
            String urlString = message.substring(CMD_UNREGISTER.length());
            URL url = URL.buildURLFromString(urlString);
            unregister(url.getInterfaceName());

        }else if (message.startsWith(CMD_SUBSCRIBE))
        {
            String urlString = message.substring(CMD_SUBSCRIBE.length());
            URL url = URL.buildURLFromString(urlString);

        }
    }

    @Override
    public void register(String serviceName, URL url) {
        //todo sync
        List<URL> urlList = urlMap.get(serviceName);
        if (urlList == null)
        {
            urlList = new ArrayList<>();
        }
        urlList.add(url);
        urlMap.put(serviceName, urlList);
        NotifyListener notifyListener = notifyListenerMap.get(serviceName);
        if (notifyListener != null)
            notifyListener.notify(urlList);
    }

    @Override
    public void unregister(String serviceName) {

    }

    @Override
    public void subscribe(String serviceName, NotifyListener listener) {
        notifyListenerMap.put(serviceName, listener);
    }

    @Override
    public List<URL> find(String serviceName) {
        return urlMap.get(serviceName);
    }
}
