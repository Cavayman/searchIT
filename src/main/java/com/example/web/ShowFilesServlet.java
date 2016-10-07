package com.example.web;

import com.example.repository.model.Metadata;
import com.example.service.FileService;
import com.example.service.MetadataService;
import com.example.util.FilterConfig;
import com.example.util.FilterMakerImpl.SimpleTextFilterImpl;
import com.example.util.FilterMakerInterface;
import com.example.util.JsonMaker;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by cavayman on 29.09.2016.
 */
@WebServlet(urlPatterns = "/showAllFiles")
@Resource
public class ShowFilesServlet extends HttpServlet {

    FileService fileService = new FileService();
    private MetadataService metadataService=new MetadataService();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Metadata> metadataList = metadataService.findAll();
        JSONArray jsonList = new JSONArray();
        if (metadataList!= null) {
            for (Metadata m: metadataList) {
                jsonList.put(metadataService.getMetadataJson(m));
            }
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonList);
        out.flush();
    }

    /**
     * Searching file by id.Geting only one param ID and gives a text file by it.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

       Integer file_id= Integer.parseInt(req.getParameter("id"));
        String appPath = req.getServletContext().getRealPath("");
        String savePath = appPath + File.separator + UploadServlet.SAVE_DIR;

        Metadata m=metadataService.findByID(file_id);
        JsonMaker jsonMaker=new JsonMaker();

        FilterConfig filterConfig=new FilterConfig(savePath + File.separator + m.getFileName());
        FilterMakerInterface filterMakerInterface=new SimpleTextFilterImpl();

        jsonMaker.setFilterMaker(filterMakerInterface);
        jsonMaker.setFilterConfig(filterConfig);

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(jsonMaker.make());
        out.flush();
    }
}
