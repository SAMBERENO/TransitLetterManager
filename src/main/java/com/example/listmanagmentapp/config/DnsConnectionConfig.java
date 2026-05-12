package com.example.listmanagmentapp.config;

import javax.jmdns.JmDNS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetAddress;

@Configuration
public class DnsConnectionConfig {

    public DnsConnectionConfig() {}

    @Bean
    public JmDNS dnsConnection() throws IOException{
        return JmDNS.create(InetAddress.getLocalHost());
    }
}
