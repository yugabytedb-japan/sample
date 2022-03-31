package com.yugabyte;

import com.yugabyte.ysql.LoadBalanceProperties;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TopologyAwareLoadBalanceApp {

    public static void main(String[] args) {
        makeConnectionUsingDriverManager();
    }

    public static void makeConnectionUsingDriverManager(){

        String dbhost = "YOUR HOST ADDRESS";
        String dbport = "5433";
        String dbuser = "yugabyte"; // or you can change it
        String dbpass = "YOUR DATABASE PASSWORD";
        String topologyKey = "aws.ap-northeast-1.ap-northeast-1a"; // or you can change it

        List<Connection> connectionList = new ArrayList<>();
        String yburl = "jdbc:yugabytedb://"+dbhost+":"+dbport+"/yugabyte?user="+dbuser+"&password="+dbpass+"&load-balance=true"
                + "&topology-keys="+topologyKey;

        try{
            for (int i=0; i<6;i++){
                Connection connection = DriverManager.getConnection(yburl);
                connectionList.add(connection);
            }

            if(LoadBalanceProperties.CONNECTION_MANAGER_MAP.size() > 0) {
//                System.out.println(LoadBalanceProperties.CONNECTION_MANAGER_MAP.keySet());
                LoadBalanceProperties.CONNECTION_MANAGER_MAP.get("aws.ap-northeast-1.ap-northeast-1a").printHostToConnMap();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}

