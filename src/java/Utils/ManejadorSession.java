package Utils;

import DAOs.GimnasioDAO;
import DAOs.UsuarioDAO;
import HBMs.Gimnasio;
import HBMs.Permiso;
import HBMs.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class ManejadorSession {
    
    private static ManejadorSession instance = null;
    
    public static ManejadorSession getInstance() {
        if(instance == null) {
            instance = new ManejadorSession();
        }
        return instance;
    }
    
    private ManejadorSession() {
        
    }
    
    public Gimnasio obtenerGimnasioLogeado(){
        try{
            Usuario usr = obtenerUsuarioLogeado();
            if (usr!=null){
                Gimnasio gym = GimnasioDAO.getInstance().obtenerGimnasio(usr);
                return gym;
            }
        }catch (Exception e){
            //TODO: persistir la excepcion
        }
        return null;
    }
    
    public Usuario obtenerUsuarioLogeado(){
        try{
            FacesContext context = FacesContext.getCurrentInstance();
            if (context!=null){
                HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
                Integer id = 0;
                if (request.getSession().getAttribute("id_usuario_administrador")!=null){
                    //no se por que en el caso de configurarcion usuarios el id_usuario lo toma como un string
                    if (request.getSession().getAttribute("id_usuario_administrador") instanceof String)  id = Integer.valueOf((String) request.getSession().getAttribute("id_usuario_administrador"));
                    if (request.getSession().getAttribute("id_usuario_administrador") instanceof Integer) id = (Integer) request.getSession().getAttribute("id_usuario_administrador");
                }
                Usuario usr = UsuarioDAO.getInstance().findById(id);
                return usr;
            }
        }catch (Exception e){
            //TODO: persistir la excepcion
        }
        return null;
    }
    
    public List<Permiso> obtenerPermisos(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        List<Permiso> permisos = new ArrayList<>();
        if (request.getSession().getAttribute("permisos_usuario")!=null) permisos = (List<Permiso>) request.getSession().getAttribute("permisos_usuario");
        return permisos;
    }
    
    public boolean tienePermiso(String nombrePermiso){
        int idPermiso;
        switch (nombrePermiso) {
            case "ANULACIONES_COBRO":
                idPermiso = Permisos.ANULACIONES_COBRO;
                break;
            case "ANULACIONES_MOVIMIENTOS":
                idPermiso = Permisos.ANULACIONES_MOVIMIENTOS;
                break;
            case "CAJA_MOVIMIENTOS":
                idPermiso = Permisos.CAJA_MOVIMIENTOS;
                break;
            case "CONFIGURACION_ACTIVIDADES":
                idPermiso = Permisos.CONFIGURACION_ACTIVIDADES;
                break;
            case "CONFIGURACION_ARTICULOS":
                idPermiso = Permisos.CONFIGURACION_ARTICULOS;
                break;
            case "CONFIGURACION_CONCEPTOS":
                idPermiso = Permisos.CONFIGURACION_CONCEPTOS;
                break;
            case "CONFIGURACION_USUARIOS":
                idPermiso = Permisos.CONFIGURACION_USUARIOS;
                break;
            case "ESTADISTICAS_GENERALES":
                idPermiso = Permisos.ESTADISTICAS_GENERALES;
                break;
            case "ESTADISTICAS_SOCIOS":
                idPermiso = Permisos.ESTADISTICAS_SOCIOS;
                break;
            case "GESTIONAR_CLIENTES":
                idPermiso = Permisos.GESTIONAR_CLIENTES;
                break;
            case "GESTIONAR_COBRO":
                idPermiso = Permisos.GESTIONAR_COBRO;
                break;
            case "GESTIONAR_MARCAS":
                idPermiso = Permisos.GESTIONAR_MARCAS;
                break;
            case "GIMNASIO_GIMNASIOS":
                idPermiso = Permisos.GIMNASIO_GIMNASIOS;
                break;
            case "REPORTES_COBRO":
                idPermiso = Permisos.REPORTES_COBRO;
                break;
            case "REPORTE_DEUDA":
                idPermiso = Permisos.REPORTE_DEUDA;
                break;
            case "REPORTE_MOVIMIENTO":
                idPermiso = Permisos.REPORTE_MOVIMIENTO;
                break;
            default:
                idPermiso = -1;
                break;
        }
        for (Permiso p : ManejadorSession.getInstance().obtenerPermisos()){
            if (p.getId()== idPermiso) return true;
        }
        return false;
    }
    
}
