package Controllers.configuracion;

import HBMs.Usuario;
import DAOs.UsuarioDAO;
import Fachadas.UsuarioFachada;
import HBMs.Permiso;
import Utils.FileUpload;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

@Named(value = "configuracionUsuarioController")
@ViewScoped
public class ConfiguracionUsuarioController implements Serializable{
    
    private Usuario usuario = new Usuario();
    private Map<Integer, Boolean> permisosSeleccionados;
    private List<Permiso> todosPermisos;
    
    UploadedFile file;
    FileUpload foto;
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    @PostConstruct
    public void init(){
        try{
            if (!ManejadorSession.getInstance().tienePermiso("CONFIGURACION_USUARIOS")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
            if (ManejadorNavegacion.getInstance().obtenerRequestParametro("id_usuario")!=null){
                if (!StringUtils.isNumeric(String.valueOf(ManejadorNavegacion.getInstance().obtenerRequestParametro("id_usuario")))) cancelar();
                int id_usuario = Integer.valueOf(ManejadorNavegacion.getInstance().obtenerRequestParametro("id_usuario"));
                Usuario usr = UsuarioDAO.getInstance().obtenerUsuarioConPermisos(id_usuario);
                 if (usr == null) cancelar();
                if (usr.getGimnasio().getId() == ManejadorSession.getInstance().obtenerGimnasioLogeado().getId()){
                    usuario = usr;
                    permisosSeleccionados = new HashMap<>();
                    todosPermisos = UsuarioDAO.getInstance().obtenerTodosLosPermisos();
                    for (Permiso iterPermiso : todosPermisos){
                        permisosSeleccionados.put(iterPermiso.getId(), tienePermisoUsuario(iterPermiso));
                    }
                }
            }else{
                todosPermisos = UsuarioDAO.getInstance().obtenerTodosLosPermisos();
                usuario = new Usuario();
                permisosSeleccionados = new HashMap<>();
            }
        }catch(Exception e){
            cancelar();
        }
    }
    
    public Map<Integer, Boolean> getPermisosSeleccionados() {
        return permisosSeleccionados;
    }
    
    public void setPermisosSeleccionados(Map<Integer, Boolean> permisosSeleccionados) {
        this.permisosSeleccionados = permisosSeleccionados;
    }
    
    public List<Permiso> getTodosPermisos() {
        return todosPermisos;
    }
    
    public void setTodosPermisos(List<Permiso> todosPermisos) {
        this.todosPermisos = todosPermisos;
    }
    
    public void guardar() throws ParseException{
        try{
            UsuarioFachada.getInstance().guardar( usuario,  foto, permisosSeleccionados,  todosPermisos);
            ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CONFIGURACION_USUARIOS);
        }catch (Exception e){
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("toastr.error('"+e.getMessage()+"');");
        }
    }
    
    public void cancelar(){
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CONFIGURACION_USUARIOS);
    }

    private boolean tienePermisoUsuario(Permiso p){
        try{
        List<Permiso> usuarioPermisos = UsuarioDAO.getInstance().obtenerPermisosDelUsuario(usuario);
        for (Permiso iterPermiso : usuarioPermisos){
            if (iterPermiso.getId() == p.getId()) return true;
        }
        return false;
          } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return false;
    }

    public void cargarSubirImagen(){
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("subirImagen();");
    }
    
    public void cargarImagen() throws IOException{
        foto = new FileUpload().cargarImagenEnBuffer(file);
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
}
