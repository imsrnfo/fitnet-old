
package Controllers.configuracion;

import DAOs.ArticuloDAO;
import Fachadas.ArticuloFachada;
import HBMs.Articulo;
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

@Named(value = "configuracionArticuloController")
@ViewScoped
public class ConfiguracionArticuloController extends ConversorGenerico<Articulo> implements Serializable{
    
    Articulo articulo;
    
    public Articulo getArticulo() {
        return articulo;
    }
    
    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }
    
    ConfiguracionArticuloController(){
        try{
        super.helper = ArticuloDAO.getInstance();
         } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
    }
    
    @PostConstruct
    public void init(){
        try{
            if (!ManejadorSession.getInstance().tienePermiso("CONFIGURACION_ARTICULOS")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String,String> params  =  context.getExternalContext().getRequestParameterMap();
            if (params.get("id_articulo")!=null){
                if (!StringUtils.isNumeric(String.valueOf(params.get("id_articulo")))) cancelar();
                int id_articulo = Integer.valueOf(params.get("id_articulo"));
                articulo = ArticuloDAO.getInstance().Obtener(id_articulo);
                if (articulo == null) cancelar();
            }else{
                articulo = new Articulo();
            }
        }catch(Exception e){
            cancelar();
        }
    }
    
    public void guardar(){
        try{
            ArticuloFachada.getInstance().guardar(articulo);
            ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CONFIGURACION_ARTICULOS);
        }catch (Exception e){
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("toastr.error('"+e.getMessage()+"');");
        }
    }
    
    public void cancelar(){
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CONFIGURACION_ARTICULOS);
    }

}