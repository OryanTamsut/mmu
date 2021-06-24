package com.mbj.server;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mbj.dm.DataModel;
import com.mbj.services.CacheUnitController;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;


/**
 * handle the request from the server
 * @param <T> the type of the data model
 */
public class HandleRequest<T> implements Runnable {

    private final Socket socket;
    private final CacheUnitController<T> controller;

    /**
     * constructor
     * @param s socket from the server
     * @param controller cache unit controller
     */
    public HandleRequest(Socket s, CacheUnitController<T> controller) {
        this.socket = s;
        this.controller = controller;
    }

    /**
     * translate the request from the server to json format
     * and then call to the corresponding function in the cache unit controller
     */
    @Override
    public void run() {
        DataOutputStream writer = null;
        Gson gson = new Gson();
        try {

            //parse the request to json format
            writer = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String content = "";

            do {
                content = in.readUTF();
                sb.append(content);
            } while (in.available() != 0);

            content = sb.toString();
            System.out.println(content);
            Type requestType = new TypeToken<Request<DataModel<T>[]>>() {}.getType();

            Request<DataModel<T>[]> request = new Gson().fromJson(content, requestType);
            DataModel<T>[] returnValues;

            String command=request.getHeaders().get("action");
            boolean ok;


            //call to the corresponding function in the controller and send the result back to the client
            switch (command) {
                case "GET":
                    returnValues = controller.get(request.getBody());
                    writer.writeUTF(gson.toJson(returnValues));
                    writer.flush();
                    break;
                case "UPDATE":
                    ok=controller.update(request.getBody());
                    writer.writeUTF(gson.toJson(ok));
                    writer.flush();
                    break;
                case "DELETE":
                    ok=controller.delete(request.getBody());
                    writer.writeUTF(gson.toJson(ok));
                    writer.flush();
                    break;
                case "STATISTICS":
                    String stat=controller.showStatistics();
                    writer.writeUTF(gson.toJson(stat));
                    writer.flush();
                    break;
                default:
                    writer.writeUTF(gson.toJson("error, cant recognize the command " + request.getHeaders().get("action")));
                    writer.flush();
                    break;
            }

            writer.close();
            in.close();

        } catch (IOException | IllegalArgumentException e ) {
            System.out.println("error: "+e.getMessage());
            try {
                writer.writeUTF(gson.toJson("error: "+e.getMessage()));
                writer.flush();
            } catch (IOException ignored) {
            }
        }
    }
}