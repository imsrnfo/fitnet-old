
package Controllers.anulaciones;

import DAOs.CobroDAO;
import HBMs.Cobro;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;


@Named(value = "anulacionesCobroController")
@ViewScoped
public class AnulacionesCobroController implements Serializable{
    
    
    public List<Cobro> obtenerCobros(){
        try{
            return  CobroDAO.getInstance().buscarInicializarCobros();
              } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return null;
    }
    
    public void anularCobro(String id_cobro){
    }

    @PostConstruct
    public void init(){
        if (!ManejadorSession.getInstance().tienePermiso("ANULACIONES_COBRO")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
    }
}
