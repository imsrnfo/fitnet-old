package Utils;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = {
    "/vistas/*, "
            + "/anulaciones/*, "
            + "/caja/*, "
            + "/configuracion/*, "
            + "/estadisticas/*, "
            + "/gestionar/*, "
            + "/gimnasio/*, "
            + "/menu/*, "
            + "/reportes/*, "
            + "/inicio"
})
public class FiltroAcceso implements Filter{
    
    @Override
    public void init(FilterConfig fc) throws ServletException {
        
    }
    
    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) sr;
        HttpServletResponse resp = (HttpServletResponse) sr1;
        
        if (req.getSession().getAttribute("id_usuario_administrador") != null) {
            fc.doFilter(req, resp);
        } else {
            HttpServletResponse res = (HttpServletResponse) resp;
            res.sendRedirect(req.getContextPath() + "/Error404.html");
        }
    }
    
    @Override
    public void destroy() {
        
    }
    
}

