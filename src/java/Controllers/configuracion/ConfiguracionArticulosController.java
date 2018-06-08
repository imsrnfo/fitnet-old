
package Controllers.configuracion;

import DAOs.ArticuloDAO;
import HBMs.Articulo;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "configuracionArticulosController")
@ViewScoped
public class ConfiguracionArticulosController implements Serializable{
    
    public List<Articulo> obtenerArticulos(){
        try{
            return ArticuloDAO.getInstance().Listar();
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return null;
    }
    
    public void crear(){
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CONFIGURACION_ARTICULO);
    }
    
    public void detalle(int id_articulo) throws IOException{
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CONFIGURACION_ARTICULO+"/"+id_articulo+"/detalle");
    }
    
    @PostConstruct
    public void init(){
        if (!ManejadorSession.getInstance().tienePermiso("CONFIGURACION_ARTICULOS")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
    }
}