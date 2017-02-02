package com.bj.aureal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SqlHelper
{


    private static String driverName="com.mysql.jdbc.Driver";

    private static String dbURL="jdbc:mysql://54.255.202.138:3306/founderdb";

    private static String userName="founderc";

    private static String userPwd="ZAQ!2bgm";


    private static Connection  getCoonection()
    {
        try
        {
            Class.forName(driverName);
            Connection conn=DriverManager.getConnection(dbURL,userName,userPwd);
            return conn;
        }

        catch(Exception e)
        {
            e.printStackTrace();
            System.out.print("----------------连接失败");
        }
        return null;
    }

    public static ResultSet  executeQuery(String SQL)
    {
        try

        {

            Connection conn=getCoonection();
            System.out.println("---------------连接数据库成功");
            // String SQL = "SELECT PlanTypeID, PlanTypeName FROM C_PlanType ";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
             /* while (rs.next())  
              { 
                 System.out.println(rs.getString("PlanTypeID") + ", " + rs.getString("PlanTypeName")); 
              }*/
            // rs.close();
            // stmt.close();
            return  rs;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.print("----------------查询失败");
        }
        return null;
    }
    public static boolean  executeUpdate(String SQL)
    {
        try
        {
            Connection conn=getCoonection();
            System.out.println("---------------连接数据库成功");

            Statement stmt = conn.createStatement();
            int result = stmt.executeUpdate(SQL);
            if(result>0)
                return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.print("----------------更新失败");
        }
        return false;
    }
}  