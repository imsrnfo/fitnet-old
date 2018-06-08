
package Controllers.reportes;

import DAOs.CobroDAO;
import HBMs.Cobro;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;


@Named(value = "reportesCobrosController")
@ViewScoped
public class ReportesCobrosController implements Serializable{
    
    List<Cobro> cobros = new ArrayList<>();
    
    public List<Cobro> getCobros() {
        return cobros;
    }
    
    public void setCobros(List<Cobro> cobros) {
        this.cobros = cobros;
    }
    
    @PostConstruct
    public void init(){
        try{
            if (!ManejadorSession.getInstance().tienePermiso("REPORTES_COBRO")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
            cobros = CobroDAO.getInstance().buscarInicializarCobros();
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
    }
    
}
