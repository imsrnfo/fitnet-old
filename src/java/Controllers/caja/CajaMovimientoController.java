
package Controllers.caja;

import DAOs.ConceptoDAO;
import Fachadas.MovimientoFachada;
import HBMs.Concepto;
import HBMs.Movimiento;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

@Named(value = "cajaMovimientoController")
@ViewScoped
public class CajaMovimientoController  implements Serializable{
    
    Concepto concepto;
    
    Movimiento movimiento;
    
    public Movimiento getMovimiento() {
        return movimiento;
    }
    
    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }
    
    public Concepto getConcepto() {
        return concepto;
    }
    
    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }
    
    public List<Concepto> obtenerConceptos(){
        try{
            return ConceptoDAO.getInstance().obtenerConceptos();
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return null;
    }
    @PostConstruct
    public void init(){
        if (!ManejadorSession.getInstance().tienePermiso("CAJA_MOVIMIENTOS")) ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.INICIO);
        movimiento = new Movimiento();
    }
    
    
    public void guardar(){
        try{
            MovimientoFachada.getInstance().guardar(movimiento,concepto);
            ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CAJA_MOVIMIENTOS);
        }catch (Exception e){
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("toastr.error('"+e.getMessage()+"');");
        }
    }
    
    public void cancelar(){
        ManejadorNavegacion.getInstance().redirigir(ManejadorNavegacion.CAJA_MOVIMIENTOS);
    }
    
    
    
    public Converter obtenerConversorConcepto() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                Object id = (Object)value;
                try{
                    int index = (id instanceof String)? Integer.valueOf((String)id) : (int) id;
                    Concepto elemento = (Concepto) ConceptoDAO.getInstance().findById(index);
                    return elemento;
                } catch (Exception ex) {
                    //TODO: persistir la excepcion
                }
                return null;
            }
            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                try{
                    if (value != null){
                        Concepto elemento = (Concepto) value;
                        Object o = ConceptoDAO.getInstance().getIdentifier(elemento);
                        String result = String.valueOf(o);
                        return result;
                    }
                } catch (Exception ex) {
                    //TODO: persistir la excepcion
                }
                return null;
            }
        };
    }
    
}