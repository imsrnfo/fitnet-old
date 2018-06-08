package Controllers.configuracion;

import DAOs.ConceptoDAO;
import Fachadas.ConceptoFachada;
import HBMs.Concepto;
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

@Named(value = "configuracionConceptoController")
@ViewScoped
public class ConfiguracionConceptoController implements Serializable{
    
    Concepto concepto;
    
    public Concepto getConcepto() {
        return concepto;
    }
    
    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }
    
    ConfiguracionConceptoController(){
    }
    
    @PostConstruct
    public void init(){
        try{
            if (!ManejadorSession.getInstance().tienePermiso("CONFIGURACION_CONCEPTOS")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String,String> params  =  context.getExternalContext().getRequestParameterMap();
            if (params.get("id_concepto")!=null){
                if (!StringUtils.isNumeric(String.valueOf(params.get("id_concepto")))) cancelar();
                int id_concepto = Integer.valueOf(params.get("id_concepto"));
                concepto = ConceptoDAO.getInstance().findById(id_concepto);
                if (concepto == null) cancelar();
            }else{
                concepto = new Concepto();
            }
        }catch(Exception e){
            cancelar();
        }
    }
    
    public void guardar(){
        try{
            ConceptoFachada.getInstance().guardar(concepto);
            ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CONFIGURACION_CONCEPTOS);
        }catch (Exception e){
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("toastr.error('"+e.getMessage()+"');");
        }
    }
    
    public void cancelar(){
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CONFIGURACION_CONCEPTOS);
    }
    
    
    
}