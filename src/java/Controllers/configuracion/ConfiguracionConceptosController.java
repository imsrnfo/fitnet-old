
package Controllers.configuracion;

import DAOs.ConceptoDAO;
import HBMs.Concepto;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "configuracionConceptosController")
@ViewScoped
public class ConfiguracionConceptosController implements Serializable{
    
    
    public List<Concepto> obtenerConceptos(){
        try{
            return ConceptoDAO.getInstance().obtenerConceptos();
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return null;
    }
    
    public void crear(){
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CONFIGURACION_CONCEPTO);
    }
    
    public void detalle(int id_concepto) throws IOException{
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CONFIGURACION_CONCEPTO+"/"+id_concepto+"/detalle");
    }
    
    public void modificar(int id_concepto) throws IOException{
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CONFIGURACION_CONCEPTO+"/"+id_concepto+"/modificar");
    }
    
    public void borrar(int id_concepto){
        try{
            ConceptoDAO.getInstance().delete(ConceptoDAO.getInstance().findById(id_concepto));
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
    }
    
    @PostConstruct
    public void init(){
        if (!ManejadorSession.getInstance().tienePermiso("CONFIGURACION_CONCEPTOS")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
    }
    
}