package Fachadas;

import DAOs.ActividadDAO;
import HBMs.Actividad;
import HBMs.Bitacora;
import Utils.ManejadorSession;
import java.util.Date;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class ActividadFachada{
    
    private static ActividadFachada instance = null;
    
    public static ActividadFachada getInstance() throws Exception{
        if(instance == null) {
            instance = new ActividadFachada();
        }
        return instance;
    }
    
    private ActividadFachada() throws Exception{}
    
    public void validar(Actividad actividad) throws Exception{
        //validacion de datos nulos
        if (actividad.getNombre() == null) throw new Exception("El campo Nombre no puede ser vacio.");
        
        //validacion de datos vacios
        if ("".equals(actividad.getNombre())) throw new Exception("El campo Nombre no puede ser vacio.");
        
        //validacion de datos enteros nulos
        if (actividad.getPrecio() == 0) throw new Exception("El campo Precio no puede ser vacio.");
        if (actividad.getDias()== 0) throw new Exception("El campo Dias no puede ser vacio.");
        if (actividad.getPeriodo()== 0) throw new Exception("El campo Periodo no puede ser vacio.");
        
    }
    
    public void guardar(Actividad actividad) throws Exception{
        validar(actividad);
        
        if (actividad.getId()>0) ActividadDAO.getInstance().update(actividad);
        
        if (actividad.getId()==0){
            Bitacora bitacora = new Bitacora();
            bitacora.setUsuario(ManejadorSession.getInstance().obtenerUsuarioLogeado());
            bitacora.setFecha(new Date());
            bitacora.setEstado("ACTIVO");
            actividad.setBitacora(bitacora);
            ActividadDAO.getInstance().guardar(actividad);
        }
        
    }
    
    public Actividad obtenerActividadPorIdCliente(int id_cliente) throws Exception{
        return ActividadDAO.getInstance().findById(id_cliente);
    }
    
    public List<Actividad> obtenerActividades() throws Exception{
        return ActividadDAO.getInstance().obtenerActividades();
    }
    
    public Actividad obtenerActividadPorId(int id) throws Exception{
        return ActividadDAO.getInstance().findById(id);
    }
    
    public Converter obtenerConversorActividad() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                Object id = (Object)value;
                try{
                    int index = (id instanceof String)? Integer.valueOf((String)id) : (int) id;
                    Actividad elemento = ActividadFachada.getInstance().obtenerActividadPorId(index);
                    return elemento;
                }catch (Exception e){
                    //TODO: persistir la excepcion
                }
                return null;
            }
            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                try{
                    if (value != null){
                        Actividad elemento = (Actividad) value;
                        Object o = ActividadDAO.getInstance().getIdentifier(elemento);
                        String result = String.valueOf(o);
                        return result;
                    }
                }catch (Exception e){
                    //TODO: persistir la excepcion
                }
                return null;
            }
        };
    }
}
