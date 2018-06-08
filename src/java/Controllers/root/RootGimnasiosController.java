
package Controllers.root;

import DAOs.GimnasioDAO;
import HBMs.Gimnasio;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "rootGimnasiosController")
@ViewScoped
public class RootGimnasiosController implements Serializable{
    
    
    
    public List<Gimnasio> obtenerGimnasios(){
        try{
            return GimnasioDAO.getInstance().findAll();
        }catch (Exception e){
            //TODO: persistir la excepcion
        }
        return null;
    }
    
    public void crear(){
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.GIMNASIO_GIMANSIO);
    }
    
    public void detalle(int id_gimnasio) throws IOException{
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.GIMNASIO_GIMANSIO+"/"+id_gimnasio+"/detalle");
    }
    
    public void modificar(int id_gimnasio) throws IOException{
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.GIMNASIO_GIMANSIO+"/"+id_gimnasio+"/modificar");
    }
    
    public void borrar(int id_gimnasio){
        try{
            GimnasioDAO.getInstance().delete(GimnasioDAO.getInstance().findById(id_gimnasio));
        }catch (Exception e){
            //TODO: persistir la excepcion
        }
        
    }
    
    @PostConstruct
    public void init(){
        if (!ManejadorSession.getInstance().tienePermiso("GIMNASIO_GIMNASIOS")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
    }
    
}