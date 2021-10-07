package com.hubble.web;

import com.hubble.data.domain.User;
import org.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "UploadServlet", urlPatterns = "/newseditor/upload")
@MultipartConfig(maxFileSize=1024*1024*16, maxRequestSize=1024*1024*16*4)
public class UploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        User user = (User)req.getSession().getAttribute("user");

        if ( user == null ) {
            System.out.println("An error occurred while creating news. Not authorized.");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("Время сессии истекло, пожалуйста, авторизуйтесь снова.");
            return;
        }
        if ( !user.hasRole(User.Role.EDITOR) ) {
            System.out.println("An error occurred while creating/editing news. Access denied.");
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("У Вас недостаточно прав для выполнения данной операции.");
            return;
        }

        // separate files from other data
        List<Part> files = req.getParts().stream().filter(part -> "file".equals(part.getName()) && part.getSize() > 0).collect(Collectors.toList());

        // check a number of files
        if ( files.size() == 0 ) {
            System.out.println("An error occurred in the "+getClass().getName()+": couldn't find attachments in the request.");
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            resp.getWriter().print(new JSONObject().put("msg", "Не удалось обнаружить вложения в запросе."));
            return;
        }

//        String destination = System.getProperty("catalina.base");
//        String destination = getServletContext().getRealPath("/");
        String destination = "C:/PRODUCTION/webapps/media/news";

        // prepare user files for storage
        for (Part file : files) {
            Files.copy(file.getInputStream(), Paths.get(destination, Paths.get(file.getSubmittedFileName()).toString()),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.copy(file.getInputStream(), Paths.get(destination, "small", Paths.get(file.getSubmittedFileName()).toString()),
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
