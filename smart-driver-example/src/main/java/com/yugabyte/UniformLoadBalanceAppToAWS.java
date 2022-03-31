package com.yugabyte;


import com.yugabyte.jdbc.PgConnection;

import java.sql.*;

public class UniformLoadBalanceAppToAWS {


    public static void main(String[] args) {
        makeConnectionUsingDriverManager();
    }

    // DriverManager.getConnection()の利用

     public static void makeConnectionUsingDriverManager(){

        String dbhost = "YOUR HOST ADDRESS";
        String dbport = "5433";
        String dbuser = "yugabyte"; // or you can change it
        String dbpass = "YOUR DATABASE PASSWORD";

        System.out.println("Create Connection to YugabyteDB.");

//        String yburl = "jdbc:yugabytedb://"+dbhost+":"+dbport+"/yugabyte?user="+dbuser+"&password="+dbpass+"&load-balance=true";
        String yburl = "jdbc:yugabytedb://"+dbhost+":"+dbport+"/yugabyte?user="+dbuser+"&password="+dbpass;

        try{

            Connection conn = DriverManager.getConnection(yburl);
            System.out.println(((PgConnection)conn).getQueryExecutor().getHostSpec().getHost());
            Statement stmt = conn.createStatement();
            stmt.execute("set yb_read_from_followers=true");
            stmt.execute("START TRANSACTION READ ONLY");

            try {
                while (true) {
                    stmt.executeQuery("select count(*),current_setting('listen_addresses') from task;");
                }
            }catch (SQLException e){
                conn.close();
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}
