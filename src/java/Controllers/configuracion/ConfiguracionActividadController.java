
package Controllers.configuracion;

import DAOs.ActividadDAO;
import DAOs.GimnasioDAO;
import Fachadas.ActividadFachada;
import HBMs.Actividad;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import Utils.ConversorGenerico;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;


@Named(value = "configuracionActividadController")
@ViewScoped
public class ConfiguracionActividadController implements Serializable{
    
    Actividad actividad;
    
    ConfiguracionActividadController(){}
    
    public Actividad getActividad() {
        return actividad;
    }
    
    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }
    
    @PostConstruct
    public void init(){
        try{
            if (!ManejadorSession.getInstance().tienePermiso("CONFIGURACION_ACTIVIDADES")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String,String> params  =  context.getExternalContext().getRequestParameterMap();
            if (params.get("id_actividad")!=null){
                if (!StringUtils.isNumeric(String.valueOf(params.get("id_actividad")))) cancelar();
                int id_cliente = Integer.valueOf(params.get("id_actividad"));
                Actividad act = ActividadFachada.getInstance().obtenerActividadPorIdCliente(id_cliente);
                if (act == null) cancelar();
                if (GimnasioDAO.getInstance().obtenerGimnasioPorEntidad(act).getId() == ManejadorSession.getInstance().obtenerGimnasioLogeado().getId()) actividad = act;
            }else{
                actividad = new Actividad();
            }
        }catch(Exception e){
            cancelar();
        }
    }
    
    public void guardar(){
        try{
            ActividadFachada.getInstance().guardar(actividad);
            ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CONFIGURACION_ACTIVIDADES);
        }catch (Exception e){
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("toastr.error('"+e.getMessage()+"');");
        }
    }
    
    public void cancelar(){
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CONFIGURACION_ACTIVIDADES);
    }

}