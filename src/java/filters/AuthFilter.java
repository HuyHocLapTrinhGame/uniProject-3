package filters;

import constants.MessageKey;
import constants.Role;
import constants.Url;
import dtos.User;
import utils.Message;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

// @WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"}) - đã config trong web.xml rồi
public class AuthFilter implements Filter {

    public static final Set<String> publicUrls = new HashSet<>(Arrays.asList(
        "/welcome.jsp",
        "/login.jsp",
        "/register.jsp",
        "/main/auth/login",
        "/main/auth/logout",
        "/main/auth/register",
        "/main/product",
        "/main/promotion",
        "/main/category",
        "/main/priceHistory"
    ));

    public static final Map<String, Role[]> exactUrls = new HashMap<>();
    public static final Map<String, Role[]> prefixUrls = new LinkedHashMap<>(); // Linked để ưu tiên prefix dài hơn

    static {
        // ==== Pass Authen ====
        exactUrls.put("/main/user/update-profile", Role.values());
        
        // ==== Buyer ====
        prefixUrls.put("/main/cart", new Role[]{Role.BUYER});
        prefixUrls.put("/main/invoice", new Role[]{Role.BUYER});
        exactUrls.put("/main/delivery/create", new Role[]{Role.BUYER});
        exactUrls.put("/main/return/create", new Role[]{Role.BUYER});
        exactUrls.put("/main/customerCare/create", new Role[]{Role.BUYER});

        // ==== Customer Support ====
        exactUrls.put("/main/customerCare/getAllViewModel", new Role[]{Role.CUSTOMER_SUPPORT});
        exactUrls.put("/main/customerCare/update", new Role[]{Role.CUSTOMER_SUPPORT});
        exactUrls.put("/main/customerCare/delete", new Role[]{Role.CUSTOMER_SUPPORT});

        // ==== Buyer & Support đều có quyền GET ở /main/customerCare ====
        prefixUrls.put("/main/customerCare", new Role[]{Role.BUYER, Role.CUSTOMER_SUPPORT});

        // ==== Seller ====
        prefixUrls.put("/main/product", new Role[]{Role.SELLER});

        // ==== Marketing ====
        prefixUrls.put("/main/promotion", new Role[]{Role.MARKETING});

        // ==== Admin ====
        prefixUrls.put("/main/category", new Role[]{Role.ADMIN});
        prefixUrls.put("/main/return", new Role[]{Role.ADMIN});
        prefixUrls.put("/main/user", new Role[]{Role.ADMIN});

        // ==== Delivery ====
        prefixUrls.put("/main/delivery", new Role[]{Role.DELIVERY}); // đã tách create ở trên cho Buyer
        
        prefixUrls.put("/main/priceHistory", Role.values());
    }


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;

        String fullPath = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = fullPath.substring(contextPath.length());

        String method = request.getMethod();
        HttpSession session = request.getSession(false);

        // Bỏ qua file tĩnh
        if (path.startsWith("/css/") || path.endsWith(".css")) {
            chain.doFilter(req, res);
            return;
        }

        // Bỏ qua nếu là public URL
        if (isPublic(path, method)) {
            chain.doFilter(req, res);
            return;
        }

        // Kiểm tra login
        if (session == null || session.getAttribute("currentUser") == null) {
            request.setAttribute("MSG", Message.get(session, MessageKey.UNAUTHENTICATED));
            request.getRequestDispatcher(Url.LOGIN_PAGE).forward(req, res);
            return;
        }

        // Kiểm tra quyền
        User user = (User) session.getAttribute("currentUser");
        Role userRole = user.getRole();

        if (isAuthorized(path, method, userRole)) {
            chain.doFilter(req, res);
        } else {
            request.setAttribute("MSG", Message.get(session, MessageKey.UNAUTHORIZED));
            request.getRequestDispatcher(Url.ERROR_PAGE).forward(req, res);
        }
    }

    private boolean isPublic(String path, String method) {
        // GET /main/product thì public
        if ((path.startsWith("/main/product") 
                || path.startsWith("/main/promotion") 
                || path.startsWith("/main/category")) 
                && method.equalsIgnoreCase("POST")) {
            return false;
        }

        if (path.equals("/") || path.isEmpty()) {
            return true;
        }

        for (String pub : publicUrls) {
            if (path.startsWith(pub)) {
                return true;
            }
        }

        return false;
    }

    private boolean isAuthorized(String path, String method, Role userRole) {
        // Check exact match
        if (exactUrls.containsKey(path)) {
            Role[] roles = exactUrls.get(path);
            return userRole != null && Arrays.asList(roles).contains(userRole);
        }

        // 3. Check prefix match (ưu tiên prefix dài hơn)
        for (Map.Entry<String, Role[]> entry : prefixUrls.entrySet()) {
            if (path.startsWith(entry.getKey())) {
                Role[] roles = entry.getValue();
                return userRole != null && Arrays.asList(roles).contains(userRole);
            }
        }
        
        return false;
    }
}
