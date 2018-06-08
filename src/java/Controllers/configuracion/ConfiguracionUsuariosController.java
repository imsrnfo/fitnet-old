package Controllers.configuracion;

import HBMs.Usuario;
import DAOs.UsuarioDAO;
import HBMs.Gimnasio;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "configuracionUsuariosController")
@ViewScoped
public class ConfiguracionUsuariosController implements Serializable{
    
    public List<Usuario> obtenerUsuarios(){
        try{
        //hacer que no muestre el usuario logueado
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        return UsuarioDAO.getInstance().obtenerUsuarios();
          } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return null;
    }
    
    public void crear(){
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CONFIGURACION_USUARIO);
    }
    
    public void modificar(int id_usuario){
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CONFIGURACION_USUARIO+"/"+id_usuario+"/modificar");
    }
    
    public void borrar(int id_cliente){

    }
    
    @PostConstruct
    public void init(){
        if (!ManejadorSession.getInstance().tienePermiso("CONFIGURACION_USUARIOS")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
    }
    
}
