package com.hubble.web;

import com.hubble.data.domain.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ViewServlet", urlPatterns = "/newseditor/view")
public class ViewServlet extends HttpServlet {

    private static final String LIST_VIEW = "/WEB-INF/pages/newseditor/news/list.jsp";
    private static final String SHOW_VIEW = "/WEB-INF/pages/newseditor/news/show.jsp";
    private static final String CREATE_VIEW = "/WEB-INF/pages/newseditor/news/create.jsp";
    private static final String PREVIEW = "/WEB-INF/pages/newseditor/news/preview.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User user = (User)req.getSession().getAttribute("user");

        if ( user == null ) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String view = req.getParameter("view");

        if ( view == null ) {
            BaseHttpServlet.includePage(LIST_VIEW, req, resp);
            return;
        }

        switch ( view ) {
            case "show":
                BaseHttpServlet.includePage(SHOW_VIEW, req, resp);
                break;
            case "create":
            case "edit":
                BaseHttpServlet.includePage(CREATE_VIEW, req, resp);
                break;
            case "preview":
                BaseHttpServlet.includePage(PREVIEW, req, resp);
                break;
            default:
                BaseHttpServlet.includePage(LIST_VIEW, req, resp);
                break;
        }

    }

}
