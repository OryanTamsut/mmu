package com.mbj.client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CacheUnitClient {

    public String send (String request) throws IOException {
        //send the request and return the response
        StringBuilder response=new StringBuilder();
        String content = "";
        Socket socket = new Socket("127.0.0.1", 12345);
        DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        writer.writeUTF(request);
        writer.flush();
        do {
            content = in.readUTF();
            response.append(content);
        } while (in.available() != 0);
        content = response.toString();
        socket.close();
        return content;
    }
}
