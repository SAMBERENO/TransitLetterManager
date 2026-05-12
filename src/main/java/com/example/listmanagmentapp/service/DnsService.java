package com.example.listmanagmentapp.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.InetAddress;

@Service
public class DnsService {

    private final JmDNS dnsConnection;

    public DnsService(JmDNS dnsConnection) {
        this.dnsConnection = dnsConnection;
    }

    @PostConstruct
    public void dnsConnection(){
        try{
            String pcName = InetAddress.getLocalHost().getHostName();
            ServiceInfo serviceInfo = ServiceInfo.create("_http._tcp.local.", pcName + " | ListManagementApp", 8080, "PC Java ListManagementApp");
            dnsConnection.registerService(serviceInfo);
        } catch (IOException e) {
            System.out.println("DNS Blad: " + e.getMessage());
        }
    }

    @PreDestroy
    public void closeConnection(){
        try{
            dnsConnection.unregisterAllServices();
            dnsConnection.close();
        } catch (IOException e) {
            System.out.println("DNS Blad: " + e.getMessage());
        }
    }
}
