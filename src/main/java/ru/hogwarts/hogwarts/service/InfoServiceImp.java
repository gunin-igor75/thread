package ru.hogwarts.hogwarts.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InfoServiceImp implements InfoService{
    @Value("${server.port}")
    private int port;


    @Override
    public int getPortApp() {
        return port;
    }
}
