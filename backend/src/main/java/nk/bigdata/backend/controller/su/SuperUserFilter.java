package nk.bigdata.backend.controller.su;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/su/*")
public class SuperUserFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("content-type", "application/json;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = httpServletRequest.getSession();
        String su = (String) session.getAttribute("su");
        if (su != null && su.equals("true")) {
            chain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}
