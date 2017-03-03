package com.bj.aureal;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

import java.io.File;
import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class fileUpload extends HttpServlet {

    private String filePath;    // 文件存放目录
    private String tempPath;    // 临时文件目录

        public void init(ServletConfig config)throws ServletException{
            super.init(config);
            // 从配置文件中获得初始化参数
            filePath = config.getInitParameter("filepath");
            tempPath = config.getInitParameter("temppath");

            ServletContext context = getServletContext();

            filePath = context.getRealPath(filePath);
            tempPath = context.getRealPath(tempPath);
            System.out.println("文件存放目录、临时文件目录准备完毕 ...");
        }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException
    {
        res.setContentType("text/plain;charset=gbk");
        PrintWriter pw = res.getWriter();
        try{
            DiskFileItemFactory diskFactory = new DiskFileItemFactory();
            // threshold 极限、临界值，即硬盘缓存 1M
            diskFactory.setSizeThreshold(4 * 1024);
            // repository 贮藏室，即临时文件目录
            diskFactory.setRepository(new File(tempPath));

            ServletFileUpload upload = new ServletFileUpload(diskFactory);
            // 设置允许上传的最大文件大小 4M
            upload.setSizeMax(4 * 1024 * 1024);
            // 解析HTTP请求消息头
            List fileItems = upload.parseRequest(req);
            System.out.println("文件上传的数量..."+fileItems);
            Iterator iter = fileItems.iterator();
            while(iter.hasNext())
            {
                FileItem item = (FileItem)iter.next();

                if(item.isFormField())
              {
                    System.out.println("处理表单内容 ...");
 //                   processFormField(item, pw);
               }else{
                    System.out.println("处理上传的文件 ...");
                    processUploadFile(item, pw);
               }
            }// end while()
            pw.println("{}");
            pw.close();
        }catch(Exception e){
            System.out.println("使用 fileupload 包时发生异常 ...");
            e.printStackTrace();
        }// end try ... catch ...
    }// end doPost()

    private void processFormField(FileItem item, PrintWriter pw)
            throws Exception
    {
        String name = item.getFieldName();
        String value = item.getString();
        pw.println(name + " : " + value + "\r\n");
    }

    private void processUploadFile(FileItem item, PrintWriter pw)
            throws Exception
    {
        // 此时的文件名包含了完整的路径，得注意加工一下
        String filename = item.getName();
        System.out.println("完整的文件名：" + filename);
        int index = filename.lastIndexOf("\\");
        filename = filename.substring(index + 1, filename.length());

        long fileSize = item.getSize();

        if("".equals(filename) && fileSize == 0)
        {
            System.out.println("文件名为空 ...");
            return;
        }

        File uploadFile = new File(filePath + "/" + filename);
        uploadFile.setExecutable(true);
        uploadFile.setWritable(true,false);
        item.write(uploadFile);

    }

    // doGet
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException
    {
        doPost(req, res);
    }

}
