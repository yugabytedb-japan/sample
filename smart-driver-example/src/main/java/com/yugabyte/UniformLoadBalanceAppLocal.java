package com.yugabyte;


import com.yugabyte.jdbc.PgConnection;
import com.yugabyte.ysql.LoadBalanceProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UniformLoadBalanceAppLocal {


    public static void main(String[] args) {
        makeConnectionUsingDriverManager();
    }

    // DriverManager.getConnection()の利用

     public static void makeConnectionUsingDriverManager(){

        List<Connection> connectionList = new ArrayList<>();
        System.out.println("Create Connection to YugabyteDB.");
//        String yburl = "jdbc:yugabytedb://127.0.0.1:5433/yugabyte?user=yugabyte"; // Direct to server
        String yburl = "jdbc:yugabytedb://127.0.0.1:5433/yugabyte?user=yugabyte&load-balance=true"; //    With Load Balancer Option

        // yb_servers();

        try{
            for(int i=0;i<6;i++){
                Connection connection = DriverManager.getConnection(yburl);
                System.out.println("Connected to: " + ((PgConnection)connection).getQueryExecutor().getHostSpec().getHost());
                connectionList.add(connection);
            }

            if(LoadBalanceProperties.CONNECTION_MANAGER_MAP.size() > 0) {
//                System.out.println(LoadBalanceProperties.CONNECTION_MANAGER_MAP.keySet());
                LoadBalanceProperties.CONNECTION_MANAGER_MAP.get("simple").printHostToConnMap();
            }

            for(Connection connection : connectionList) {
                connection.close();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}
