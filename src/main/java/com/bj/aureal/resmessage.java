package com.bj.aureal;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "resmessage")
public class resmessage extends HttpServlet {

        ServletConfig config=null;                        //定义一个ServletConfig对象
        private String driverName="com.mysql.jdbc.Driver";                    //定义私有字符串常量并初始化
    private String username="founderc";                    //定义的数据库用户名
    private String password="ZAQ!2bgm";                    //定义的数据库连接密码
    private String dbName="founderdb";                        //定义的数据库名
        private Connection conn;                        //初始化连接
        private Statement stmt;                        //初始化数据库操作
        ResultSet rs=null;                            //初始化结果集
        public void init(ServletConfig config)throws ServletException{
            super.init(config);                            //继承父类的init()方法
            this.config=config;                            //获取配置信息
            driverName=config.getInitParameter("driverName");//从配置文件中获取JDBC驱动名
            username=config.getInitParameter("username");    //获取数据库用户名
            password=config.getInitParameter("password");    //获取数据库连接密码
            dbName=config.getInitParameter("dbName");    //获取要连接的数据库
        }
        public void doGet(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{


//            out.println("</center>");
//            out.println("</body>");
//            out.println("</html>");
        }

        public void doPost(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            resp.setContentType("text/html;charset=UTF-8");
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String subject = req.getParameter("subject");
            String message = req.getParameter("message");
            System.out.println(name);
            String result="";
            String SQL = "INSERT INTO contactus (`username`, `email`, `subject`, `info`) VALUES ( '"+name+"', '"+email+"', '"+subject+"', '"+message+"');";
            System.out.println(SQL);
            if(SqlHelper.executeUpdate(SQL))
            {
                result=("添加成功 ");
            }else
            {
                result=("添加失败 ");
            }
            resp.setContentType("text/html ;charset=utf-8");
            out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
            out.println("<HTML>");
            out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
            out.println("  <BODY>");
            out.print("    This is ");
            out.print(this.getClass());
            out.println(", using the POST method");
            out.println("已处理请求！处理结果为"+result);
            out.println("  </BODY>");
            out.println("</HTML>");
            out.flush();
            out.close();

        }

        public void destory(){
            config=null;
            driverName=null;
            username=null;
            password=null;
            dbName=null;
            conn=null;
            stmt=null;
            rs=null;
        }
    public String resultSetToJson(ResultSet rs) throws SQLException,JSONException
    {
        // json数组
        JSONArray array = new JSONArray();

        // 获取列数
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // 遍历ResultSet中的每条数据
        while (rs.next()) {
            JSONObject jsonObj = new JSONObject();

            // 遍历每一列
            for (int i = 1; i <= columnCount; i++) {
                String columnName =metaData.getColumnLabel(i);
                String value = rs.getString(columnName);
                jsonObj.put(columnName, value);
            }
            array.add(jsonObj);
        }

        return array.toString();
    }
}
