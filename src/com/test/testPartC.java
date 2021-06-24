package com.test;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mbj.dm.DataModel;
import com.mbj.server.Request;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class testPartC {

    private BufferedReader br;

    @Test
    public void testUpdate() {

        //create update request
        Map<String, String> headerReq = new HashMap<>();
        headerReq.put("action", "UPDATE");

        DataModel<String>[] dmArray = new DataModel[]{new DataModel<String>(1L, "a"), new DataModel<String>(2L, "b")};

        Request<DataModel<String>[]> req = new Request<>(headerReq, dmArray);
        req.getHeaders();
        Gson gson = new Gson();
        try {
            //send request to the server
            Socket socket = new Socket("127.0.0.1", 12345);
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            writer.writeUTF(gson.toJson(req));
            writer.flush();

            //get the response from the server
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String content = "";

            do {
                content = in.readUTF();
                sb.append(content);
            } while (in.available() != 0);

            content = sb.toString();
            //check if the content is json type
            try {
                JsonParser parser = new JsonParser();
                parser.parse(content);
            } catch (JsonSyntaxException e) {
                System.out.println(content);
                return;
            }
            Boolean response = true;
            //parse the content to json type and return it to the client
            response = new Gson().fromJson(content, response.getClass());
            System.out.println("message from server: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testGet() {
        //create get request
        Map<String, String> headerReq = new HashMap<>();
        headerReq.put("action", "GET");

        DataModel<String>[] dmArray = new DataModel[]{new DataModel<String>(1L, "bb"), new DataModel<String>(2L, "aa")};
        Request<DataModel<String>[]> req = new Request<>(headerReq, dmArray);

        Gson gson = new Gson();
        try {
            //send request to the server
            Socket socket = new Socket("127.0.0.1", 12345);
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            writer.writeUTF(gson.toJson(req));
            writer.flush();
            //get the response from the server
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String content = "";

            do {
                content = in.readUTF();
                sb.append(content);
            } while (in.available() != 0);

            content = sb.toString();
            //check if the content is json type
            try {
                JsonParser parser = new JsonParser();
                parser.parse(content);
            } catch (JsonSyntaxException e) {
                System.out.println(content);
                return;
            }
            //parse the content to json type and return it to the client
            Type requestType = new TypeToken<DataModel<String>[]>() {
            }.getType();
            DataModel<String>[] response = new Gson().fromJson(content, requestType);
            System.out.println("message from server: " + Arrays.toString(response));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testDelete() {
        //create delete request
        Map<String, String> headerReq = new HashMap<>();
        headerReq.put("action", "DELETE");

        DataModel<String>[] dmArray = new DataModel[]{new DataModel<String>(1L, "b")};
        Request<DataModel<String>[]> req = new Request<>(headerReq, dmArray);

        Gson gson = new Gson();
        try {
            //send request to the server
            Socket socket = new Socket("127.0.0.1", 12345);
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            writer.writeUTF(gson.toJson(req));
            writer.flush();

            //get the response from the server
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String content = "";

            do {
                content = in.readUTF();
                sb.append(content);
            } while (in.available() != 0);

            content = sb.toString();
            //check if the content is json type
            try {
                JsonParser parser = new JsonParser();
                parser.parse(content);
            } catch (JsonSyntaxException e) {
                System.out.println(content);
                return;
            }
            //parse the content to json type and return it to the client
            Boolean response = true;
            response = new Gson().fromJson(content, response.getClass());
            System.out.println("message from server: " + response);


        } catch (IOException e) {
            System.out.println("error: "+e.getMessage());
        }

    }


}
