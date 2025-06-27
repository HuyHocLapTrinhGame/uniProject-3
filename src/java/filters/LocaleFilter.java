/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package filters;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Locale;

/**
 *
 * @author Admin
 */
// @WebFilter(filterName="LocaleFilter", urlPatterns={"/*"})
public class LocaleFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Không cần xử lý gì khi khởi tạo filter
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            String lang = httpRequest.getParameter("lang");
            HttpSession session = httpRequest.getSession(false);

            if (lang != null && !lang.trim().isEmpty()) {
                Locale locale = "vi".equalsIgnoreCase(lang) ? new Locale("vi") : new Locale("en");

                if (session == null) {
                    session = httpRequest.getSession();
                }
                session.setAttribute("locale", locale);

                // Redirect để xoá lang khỏi URL
                String requestURI = httpRequest.getRequestURI();
                String queryString = httpRequest.getQueryString();

                // Xoá lang=... khỏi query string
                String cleanedQuery = removeLangParam(queryString);
                requestURI = (cleanedQuery == null || cleanedQuery.isEmpty())
                        ? requestURI
                        : requestURI + "?" + cleanedQuery;

                // /myapp/main/controller/action
                String redirectURL = httpRequest.getContextPath() + "/main";
                String[] parts = requestURI.split("/");
                if (parts.length >= 4) {
                    String controller = parts[3]; // controller
                    String action = parts.length >= 5 ? parts[4] : ""; // action

                    if ((controller.equals("return") || controller.equals("delivery"))
                            && action.equals("create")) {
                        controller = "invoice";
                    }

                    if (action.equals("update-profile")){
                        controller = "product";
                    }
                    
                    action = "";
                    StringBuilder urlResult = new StringBuilder(redirectURL);
                    urlResult.append("/").append(controller);
                    if (!action.isEmpty()) {
                        urlResult.append("/").append(action);
                    }

                    redirectURL = urlResult.toString();
                }

                httpResponse.sendRedirect(redirectURL);
                return; // Dừng filter tại đây
            }
        }

        chain.doFilter(request, response);
    }

    private String removeLangParam(String queryString) {
        if (queryString == null) {
            return null;
        }

        String[] params = queryString.split("&");
        StringBuilder cleaned = new StringBuilder();

        for (String param : params) {
            if (!param.startsWith("lang=")) {
                if (cleaned.length() > 0) {
                    cleaned.append("&");
                }
                cleaned.append(param);
            }
        }

        return cleaned.toString();
    }
}
