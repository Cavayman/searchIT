package com.example.web;

import com.example.service.FileService;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

/**
 * Created by cavayman on 29.09.2016.
 */
@WebServlet(urlPatterns = "/uploadFile")
@MultipartConfig
public class UploadServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads";
    private  FileService fileService=new FileService();

    /**
     * Getting file throu request and saving it in uploads directory
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        // gets absolute path of the web application
        String applicationPath = request.getServletContext().getRealPath("");
        // constructs path of the directory to save uploaded file
        String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;

        // creates the save directory if it does not exists
        File fileSaveDir = new File(uploadFilePath);

        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }

        System.out.println("Upload File Directory="+fileSaveDir.getAbsolutePath());

        String fileName ="";
        //Get all the parts from request and write it to the file on server
        for (Part part : request.getParts()) {
           fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
            fileService.file=new File(uploadFilePath + File.separator + fileName);
            part.write(uploadFilePath + File.separator + fileName);
        }

        request.setAttribute("message", fileName + " File uploaded successfully!");

        JSONObject obj=new JSONObject();
        obj.put("Name", fileName);
        obj.put("Text", fileService.getTextFile(FileService.file));

        response.setContentType("application/json");
// Get the printwriter object from response to write the required json object to the output stream
        PrintWriter out = response.getWriter();
// Assuming your json object is **jsonObject**, perform the following, it will return your json object
        out.print(obj);
        out.flush();
    }
}