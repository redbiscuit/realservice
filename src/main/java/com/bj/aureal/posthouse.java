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

@WebServlet(name = "posthouse")
public class posthouse extends HttpServlet {

        ServletConfig config=null;                        //定义一个ServletConfig对象
        private String driverName="com.mysql.jdbc.Driver";                    //定义私有字符串常量并初始化
        private String username="root";                    //定义的数据库用户名
        private String password="asdfasdf1";                    //定义的数据库连接密码
        private String dbName="realestate";                        //定义的数据库名
        private Connection conn;                        //初始化连接
        private Statement stmt;                        //初始化数据库操作
        ResultSet rs=null;                            //初始化结果集

        public void doGet(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{

        }

        public void doPost(HttpServletRequest req,HttpServletResponse resp)throws IOException,ServletException{
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            resp.setContentType("text/html;charset=UTF-8");
            System.out.println("正在处理请求... ");
            String id = req.getParameter("id");
            String name_cn= req.getParameter("name_cn");
            String name_en= req.getParameter("name_en");
            String type_cn= req.getParameter("type_cn");
            String sintro_cn= req.getParameter("sintro_cn");
            String sintro_en= req.getParameter("sintro_en");
            String intro_cn= req.getParameter("intro_cn");
            String intro_en= req.getParameter("intro_en");
            String detail_cn= req.getParameter("detail_cn");
            String detail_en= req.getParameter("detail_en");
            String fac_cn= req.getParameter("fac_cn");
            String fac_en= req.getParameter("fac_en");
            float price= Float.parseFloat(req.getParameter("price"));
            String price_cn= req.getParameter("price_cn");
            String price_en= req.getParameter("price_en");
            String img= req.getParameter("img");
            String img_d= req.getParameter("img_d");
            String house_img= req.getParameter("house_img");
            String display = req.getParameter("display");
            System.out.println("参数获取完成...");
            String result="";
            String SQL ="";

            System.out.println(id);
            if(id.equals("none")) {
                SQL = "INSERT INTO houses (`type`, `name`, `name_en`, `keyword`, `keyword_en`, `sintro`, `sintro_en`, `intro`, `intro_en`, `detail`, `detail_en`, `fac`, `fac_en`, `price_des`, `price_des_en`, `price`, `img`, `img_d`, `housemap`,`display`,`enable`) VALUES ( 'Houses','" + name_cn + "', '" + name_en + "', '" + type_cn + "', '" + "" + "', '" + sintro_cn + "', '" + sintro_en + "', '" + intro_cn + "', '" + intro_en + "', '" + detail_cn + "', '" + detail_en + "', '" + fac_cn + "', '" + fac_en + "', '" + price_cn + "', '" + price_en + "', " + price + ", '" + img + "', '" + img_d + "', '" + house_img + "',"+display+",1);";
                System.out.println(SQL);
            }else{
                SQL = "UPDATE houses SET name='"+name_cn+"', name_en='"+name_en+"',keyword='"+type_cn+"',sintro='"+sintro_cn+"',sintro_en='"+sintro_en+"',intro='"+intro_cn+"',intro_en='"+intro_en+"',detail='"+detail_cn+"',detail_en='"+detail_en+"',fac='"+fac_cn+"',fac_en='"+fac_en+"',price_des='"+price_cn+"',price_des_en='"+price_en+"',price="+price+",img='"+img+"',img_d='"+img_d+"',housemap='"+house_img+"',display="+display+" WHERE id="+id;
                System.out.println(SQL);
            }
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
