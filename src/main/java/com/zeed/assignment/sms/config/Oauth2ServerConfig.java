package com.zeed.assignment.sms.config;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

@Service
public class Oauth2ServerConfig  {

    private Socket socket;
    private ServerSocket serverSocket;

    @PostConstruct
    public void initiateSocket() throws IOException {
        InetAddress ip = InetAddress.getByName("localhost");
        serverSocket = new ServerSocket( 5056);
    }
}
