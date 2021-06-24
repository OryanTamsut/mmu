package com.mbj.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class CLI implements Runnable {

    private final InputStream in;
    private final OutputStream out;
    private final PropertyChangeSupport observer;
    private String command;

    /**
     * constructor
     * @param in input stream to read from it the command
     * @param out output stream to write to it messages
     */
    public CLI(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
        this.command = "";
        this.observer = new PropertyChangeSupport(this);
    }

    /**
     * add PropertyChangeListener to the observer
     * @param pcl PropertyChangeListener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        observer.addPropertyChangeListener(pcl);
    }

    /**
     * remove PropertyChangeListener from the observer
     * @param pcl PropertyChangeListener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        observer.removePropertyChangeListener(pcl);
    }

    /**
     * the CLI process- get command from the user
     * start- to start the server
     * stop- to stop the server
     * exit- to finish all the program
     * if get a wrong command write this is not correct command
     */
    public void run() {
        write("please insert one of the following option: \n1. \"start\" to start the server\n2. \"shutdown" +
                "\" to stop the server\n3. \"exit\" to finish all the program\n" +
                "Please note - when you choose to close the server (stop) - the changes in file will only be visible after you send another request" +
                "or when you will close the all program (exit) ");
        Scanner scanner = new Scanner(in);
        String command = scanner.nextLine();
        boolean running=true;
        while (running) {
            switch (command) {
                case "start":
                    observer.firePropertyChange("command", this.command, command);
                    this.command = command;
                    write("server start...");
                    break;
                case "shutdown":
                    observer.firePropertyChange("command", this.command, command);
                    this.command = command;
                    write("server stop");
                    break;
                case "exit":
                    running=false;
                    observer.firePropertyChange("command", this.command, command);
                    this.command = command;
                    write("finish the program");
                    break;
                default:
                    write("not valid command");
            }
            if(running) {
                command = scanner.nextLine();
            }
        }

    }

    /**
     * write message to the output stream
     * @param string message to write
     */
    public void write(String string) {
        PrintWriter writer = new PrintWriter(out);
        writer.write(string+"\n");
        writer.flush();
    }

}

