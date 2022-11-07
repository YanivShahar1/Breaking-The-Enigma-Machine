package Servlets.AlliesServlets;

import DTO.DTOContestData;
import SystemEngine.SystemEngine;
import Utils.ServletUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.DataTruncation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "getAllContestList", urlPatterns = {"/allContests"})
public class GetAllContestsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        //need list of ctocontestdata

        Map<String, SystemEngine> allUboats = ServletUtils.getUboatUserManager(getServletContext()).getAllUboats();
        List<DTOContestData> dtoContestDataList = new ArrayList<>();


        //allUboats.forEach((uboatName, uboatSystemEngine) -> dtoContestDataList.add(uboatSystemEngine.getContestData()) );
        for (String uboat : allUboats.keySet()) {

            dtoContestDataList.add(allUboats.get(uboat).getContestData());
        }
        Type type = new TypeToken<List<DTOContestData>>(){}.getType();
        String json = gson.toJson(dtoContestDataList, type);
        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.println(json);
        out.flush();
    }
}
