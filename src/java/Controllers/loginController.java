package Controllers;

import DAOs.ExcepcionDAO;
import HBMs.Usuario;
import DAOs.UsuarioDAO;
import HBMs.Excepcion;
import HBMs.Gimnasio;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.Serializable;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named(value = "loginController")
@RequestScoped
public class loginController implements Serializable{
    
    private Usuario usuario = new Usuario();
    
    public String validarLogin() throws Exception{
        try{
            Usuario usr = UsuarioDAO.getInstance().obtenerUsuarioNombrePassword(usuario);
            if(usr!=null){
                FacesContext context = FacesContext.getCurrentInstance();
                HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
                request.getSession().setAttribute("id_usuario_administrador", usr.getId());
                request.getSession().setAttribute("permisos_usuario", UsuarioDAO.getInstance().obtenerPermisosDelUsuario(usr));
                return "/vistas/menu/mainmenu.xhtml?faces-redirect=true";
            }else{
                return "";
            }
        } catch (Exception ex) {
            //TODO: persistir la excepcion
            Excepcion excepcion = new Excepcion();
            excepcion.setMensaje(ex.getMessage());
            excepcion.setFecha(new Date());
            ExcepcionDAO.getInstance().create(excepcion);
        }
        return "";
    }
    
    public String validarLoginModoCliente(){
        try{
        Usuario usr = UsuarioDAO.getInstance().obtenerUsuarioNombrePassword(usuario);
        if(usr!=null){
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            request.getSession().setAttribute("id_usuario_administrador", usr.getId());
            return "/vistas/menu/login.xhtml?faces-redirect=true";
        }else{
            return "";
        }
         } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return "";
    }
    
    public void logOut(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().invalidate();
        ManejadorNavegacion.getInstance().redirigir("/");
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String obtenerNombreGimnasio(){
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        return (gym!=null)?gym.getNombre():"";
    }
    
    public String obtenerImagenGimnasio(){
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        return (gym!=null)?gym.getImagen():"";
    }
    
    public String obtenerNombreUsuario(){
        Usuario usr = ManejadorSession.getInstance().obtenerUsuarioLogeado();
        return (usr!=null)?usr.getNombre():"";
    }
    
    public String obtenerImagenUsuario(){
        Usuario usr = ManejadorSession.getInstance().obtenerUsuarioLogeado();
        return (usr!=null)?usr.getImagen():"";
    }
    
    
}
