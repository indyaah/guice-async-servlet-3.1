package pro.anuj.filter;

import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@WebFilter(asyncSupported = true, filterName = "CacheController", urlPatterns = "/*")
public class CacheControlFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        int expiresAfterInSeconds = 3600;

        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setDateHeader("Expires", expiresAfterInSeconds);
        resp.setHeader("Cache-Control", "public, max-age=" + expiresAfterInSeconds);

        chain.doFilter(request, response);
    }
}