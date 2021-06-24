package com.mbj.server;

import com.mbj.services.CacheUnitController;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.EventListener;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server implements PropertyChangeListener, Runnable, EventListener {
    private String command;
    private ServerSocket server;
    private final ExecutorService executor;
    private final CacheUnitController<String> controller;

    /**
     * constructor- initializes the server attributes
     */
    public Server() {
        this.command = "";
        this.executor = Executors.newFixedThreadPool(5);
        this.controller = new CacheUnitController<String>();
        try {
            this.server = new ServerSocket(12345);
        } catch (IOException e) {
            System.out.println("error: "+e.getMessage());
        }
    }

    /**
     * the server progress- wait to request from client and send the request to the handleRequest class
     */
    @Override
    public void run(){
        try {
            //Raises the server again
            if (server.isClosed()) {
                this.server = new ServerSocket(12345);
            }
            while (command.equals("start")) {
                //wait to the next request
                Socket socket = server.accept();
                if(!command.equals("start")) {
                    DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
                    writer.writeUTF("the server is closed and therefore can not get back a response");
                    writer.flush();
                    break;
                }
                executor.execute(new HandleRequest<String>(socket, controller));
            }
            //when the user choose to close the server
            server.close();
            controller.updateFile();
            if(command.equals("exit")){
                executor.shutdownNow();
            }
        } catch (IOException e) {
            if(command.equals("exit")){
                return;
            }
            System.out.println("error: "+e.getMessage());
        }
    }

    /**
     * get the command from CLI class
     * @param evt the command from CLI
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        this.command = ((String) evt.getNewValue());
        //start the server
        if (command.equals("start")) {
            executor.execute(this);
        }

        //close the server
        if(command.equals("exit")){
            try {
                server.close();
                controller.updateFile();
                executor.shutdownNow();
            } catch (IOException e) {
                System.out.println("error: "+e.getMessage());
            }
        }

    }
}
