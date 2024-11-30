package wkz.org.backend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import wkz.org.backend.services.UserService;

@Component
public class RoleInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        String role = userService.getRoleByUserId(userId);

        if("ADMIN".equals(role)) {
            return true;
        }

        // If the user is not an admin, return 403 Forbidden
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have sufficient permissions to access this resource.");
        return false;
    }
}