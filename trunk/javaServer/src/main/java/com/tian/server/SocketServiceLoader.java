package com.tian.server;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.tian.server.listener.ConnectionListener;
import com.tian.server.listener.DisconnectionListener;
import com.tian.server.listener.StreamJsonListener;
import com.tian.server.listener.StreamListener;
import net.sf.json.JSONObject;

/**
 * Created by PPX on 2017/6/8.
 */
public class SocketServiceLoader implements ServletContextListener {

    SocketIOServer server;

    public void contextInitialized(ServletContextEvent sce) {

        try {

            Configuration config = new Configuration();
            config.setHostname("::0");
            config.setPort(2020);

            server = new SocketIOServer(config);

            //监听建立连接、连接断开
            server.addConnectListener(new ConnectionListener());
            server.addDisconnectListener(new DisconnectionListener());



            //监听流服务
            StreamListener listner = new StreamListener();
            listner.setServer(server);
            server.addEventListener("stream", String.class, listner);

            //监听json数据服务
            StreamJsonListener jsonlistner = new StreamJsonListener();
            jsonlistner.setServer(server);
            server.addEventListener("unity_stream", JSONObject.class, jsonlistner);

            //启动服务
            server.start();

            System.out.println("server start success!");

        }catch (Exception e){
        }

    }

    public void contextDestroyed(ServletContextEvent sce) {

        server.stop();

    }
}
