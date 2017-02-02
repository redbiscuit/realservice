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
import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@WebServlet(name = "mailadapter")
public class mailadapter extends HttpServlet {

        ServletConfig config=null;                        //定义一个ServletConfig对象
        private String driverName="com.mysql.jdbc.Driver";                    //定义私有字符串常量并初始化
        private String username="root";                    //定义的数据库用户名
        private String password="asdfasdf1";                    //定义的数据库连接密码
        private String dbName="realestate";                        //定义的数据库名
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

            String reals = req.getParameter("method");
            String id = req.getParameter("id");
            resp.setContentType("application/Json;charset=UTF-8");    //设置字符编码格式
            PrintWriter out=resp.getWriter();                //实例化对象，用于页面输出
//            out.println("<html>");                    //实现生成静态Html
//            out.println("<head>");
//            out.println("<meta http-equiv=\"Content-Type\"content=\"text/html;charset=GBK\">");
//            out.println("<title>DataBase Connection</title>");
//            out.println("</head>");
//            out.println("<body bgcolor=\"white\">");
//            out.println("<center>");
            String url="jdbc:mysql://127.0.0.1:3333/realestate";
            try{
                Class.forName("com.mysql.jdbc.Driver");
                conn=DriverManager.getConnection(url,"root","asdfasdf1");
                stmt=conn.createStatement();
                String sql = null;
                if(reals.equals("houses_cn")){
                    sql="SELECT id,Type,Name,Keyword,S_Intro,Intro,Detail,Fac,Price,Img,Img_D,HouseMap,Date FROM realestate.house Where Type='Houses';";
                }else if(reals.equals("houses_en")){
                    sql="SELECT id,Type,EN_Name,EN_Keyword,EN_S_Intro,EN_Intro,EN_Detail,EN_Fac,Price,Img,Img_D,HouseMap,Date FROM realestate.house Where Type='Houses';";
                }else if(reals.equals("news_cn")) {
                    sql = "SELECT id,Type,Name,Keyword,S_Intro,Img FROM realestate.house Where Type='News'";
                }else if(reals.equals("news_en")){
                    sql = "SELECT id,Type,EN_Name,EN_Keyword,EN_S_Intro,Img FROM realestate.house Where Type='News'";
                }else if(reals.equals("house_cn")){
                    sql = "SELECT id,Type,Name,Keyword,S_Intro,Intro,Detail,Fac,Price,Img,Img_D,HouseMap,Date FROM realestate.house Where Type='Houses' AND id = "+id;
                }else if(reals.equals("house_en")){
                    sql = "SELECT id,Type,EN_Name,EN_Keyword,EN_S_Intro,EN_Intro,EN_Detail,EN_Fac,Price,Img,Img_D,HouseMap,Date FROM realestate.house Where Type='Houses' and id = "+id;
                }else if(reals.equals("new_cn")){
                    sql = "SELECT id,Type,Name,Keyword,S_Intro,Img FROM realestate.house Where Type='News' and id="+id;
                }else if(reals.equals("new_en")) {
                    sql = "SELECT id,Type,EN_Name,EN_Keyword,EN_S_Intro,Img FROM realestate.house Where Type='News' and id=" + id;
                }else if(reals.equals("others_cn")){
                    sql = "SELECT id,Type,Name,Keyword,S_Intro,Img FROM realestate.house Where Type='Others'";
                }else if(reals.equals("others_en")){
                    sql = "SELECT id,Type,EN_Name,EN_Keyword,EN_S_Intro,Img FROM realestate.house Where Type='Others'";
                }else{
                    sql="SELECT * FROM realestate.house";
                }

                rs=stmt.executeQuery(sql);
//                out.println("Servlet访问数据库成功");
//                out.println("<table border=1 bordercolorlight=#000000>");
//                out.println("<tr><td width=40>序号</td>");
//                out.println("<td>书名</td>");
//                out.println("<td>城市</td>");
//                out.println("<td>州</td>");
//                out.println("<td>国</td></tr>");
                String test = resultSetToJson(rs);
                out.println(test);
//                while(rs.next()){
//                    out.println("<tr><td>"+rs.getString(1)+"</td>");
//                    out.println("<td>"+rs.getString(2)+"</td>");
//                    out.println("<td>"+rs.getString(3)+"</td>");
//                    out.println("<td>"+rs.getString(4)+"</td>");
//                    out.println("<td>"+rs.getString(5)+"</td>");
//                    out.println("<tr>");
//                }
          //      out.println("</table>");
                rs.close();
                stmt.close();
                conn.close();

            }catch(Exception e){
                e.printStackTrace();
                out.println(e.toString());
            }
//            out.println("</center>");
//            out.println("</body>");
//            out.println("</html>");
        }
        public void doPost(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{
            this.doGet(req,resp);
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
