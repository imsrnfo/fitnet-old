
package Controllers.gestionar;

import DAOs.MarcaDAO;
import HBMs.Gimnasio;
import HBMs.Marca;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;


@Named(value = "gestionarMarcasController")
@ViewScoped
public class GestionarMarcasController implements Serializable{
    
    public List<Marca> obtenerMarcas(){
        try{
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        return MarcaDAO.getInstance().obtenerMarcas();
          } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return null;
    }

     @PostConstruct
    public void init(){
        if (!ManejadorSession.getInstance().tienePermiso("GESTIONAR_MARCAS")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
    }
}
