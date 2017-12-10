package com.metizik.exploringandroid;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import com.google.protobuf.*;
import com.metizik.exploringandroid.proto.AddressBookProtos.Person;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private class ClientThread extends Thread{
        private void write(MessageLite msg, OutputStream out) throws IOException {
            msg.writeDelimitedTo(out);
        }

        public void run() {
            try {
                System.out.println("Welcome to Client side");

                Socket fromserver = null;
                String addr = "127.0.0.1";
                System.out.println("Connecting to... " + addr);

                fromserver = new Socket(addr, 4444);
                InputStream inputStream = fromserver.getInputStream();
                OutputStream outputStream = fromserver.getOutputStream();


                write((MessageLite) Person.newBuilder()
                        .setName("John Doe")
                        .build(), outputStream);
                outputStream.flush();
                write((MessageLite) Person.newBuilder()
                        .setName("Jane Doe")
                        .build(), outputStream);
                outputStream.flush();
                write((MessageLite) Person.newBuilder()
                        .setName("Jeff Doe")
                        .build(), outputStream);
                outputStream.flush();
                fromserver.close();
            } catch (IOException ex) {}
        }
    }

    @Test
    public void addition_isCorrect() throws Exception {
        try {
            System.out.println("MyThread running");
            System.out.println("Welcome to Server side");
            BufferedReader in = null;
            PrintWriter out= null;

            ServerSocket servers = null;
            Socket fromclient = null;

            servers = new ServerSocket(4444);
            System.out.print("Waiting for a client...");
            ClientThread client = new ClientThread();
            client.start();
            fromclient= servers.accept();
            System.out.println("Client connected");

            Person j = Person.parseDelimitedFrom(fromclient.getInputStream());
            System.out.println(j.getName());
            j = Person.parseDelimitedFrom(fromclient.getInputStream());
            System.out.println(j.getName());
            j = Person.parseDelimitedFrom(fromclient.getInputStream());
            System.out.println(j.getName());

            fromclient.close();
            servers.close();
        } catch (IOException ex) {
        }

    }
}