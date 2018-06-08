
package Controllers.configuracion;

import DAOs.ActividadDAO;
import Fachadas.ActividadFachada;
import HBMs.Actividad;
import HBMs.Gimnasio;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;


@Named(value = "configuracionActividadesController")
@ViewScoped
public class ConfiguracionActividadesController implements Serializable{
    
    public List<Actividad> obtenerActividades(){
        try{
            Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
            return ActividadFachada.getInstance().obtenerActividades();
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return null;
    }
    
    public void crear(){
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CONFIGURACION_ACTIVIDAD);
    }
    
    public void detalle(int id_actividad) throws IOException{
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CONFIGURACION_ACTIVIDAD+"/"+id_actividad+"/detalle");
    }
    
    @PostConstruct
    public void init(){
        if (!ManejadorSession.getInstance().tienePermiso("CONFIGURACION_ACTIVIDADES")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
    }
}
