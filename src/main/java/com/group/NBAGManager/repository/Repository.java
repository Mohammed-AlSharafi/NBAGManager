package com.group.NBAGManager.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public abstract class Repository<E> implements RepositoryInterface<E> {
    Connection con;
    public Repository(){
        String propertiesLocation = "nba.properties";
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(propertiesLocation));
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        //connection to database
        try{
            String url = properties.getProperty("sql-connection");
            con = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public abstract void save(E obj);

    public abstract E findById(int id);

    public abstract List<E> findAll();

    public abstract void update(E obj);

    public abstract void deleteById(int id);

    public abstract void delete(E obj);

    public void close(){
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
