package com.example.listmanagmentapp.helpmethods;

import com.example.listmanagmentapp.config.DBConnectionConfig;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Service
public class IdManager {

    private final DBConnectionConfig dbConnectionConfig;
    private int id;
    private int prevId;

    public IdManager() {
        this.dbConnectionConfig = new DBConnectionConfig();
    }

    public int provideId(){
        try(Connection connection = dbConnectionConfig.dbConnection()){
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(10);
            ResultSet rs = statement.executeQuery("SELECT id FROM DaneJson ORDER BY id ASC");
            while(rs.next()){
                id = rs.getInt(1);
            }
            if(id == prevId){
                id = 0;
            }
            prevId = id;
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            id++;
        }
        return id;
    }

}
