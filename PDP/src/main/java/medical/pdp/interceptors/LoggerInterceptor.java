package medical.pdp.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Logger;


public class LoggerInterceptor implements HandlerInterceptor
{
    private Logger logger = Logger.getLogger("");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        logger.info("Client: " + request.getHeaders("Host").nextElement() + ", Method: " + request.getMethod() + ", Path: " + request.getRequestURI());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
    {
        logger.info("Method: " + request.getMethod() + ", Path: " + request.getRequestURI() + ", Status: " + response.getStatus());
    }
}
