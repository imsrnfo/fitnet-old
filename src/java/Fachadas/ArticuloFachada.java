package Fachadas;

import DAOs.ArticuloDAO;
import HBMs.Articulo;
import HBMs.Bitacora;
import Utils.ManejadorSession;
import java.util.Date;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class ArticuloFachada{
    
    private static ArticuloFachada instance = null;
    
    public static ArticuloFachada getInstance()  throws Exception{
        if(instance == null) {
            instance = new ArticuloFachada();
        }
        return instance;
    }
    
    private ArticuloFachada()  throws Exception{
        
    }
    
    public void validar(Articulo articulo) throws Exception{
        //validacion de datos nulos
        if (articulo.getNombre() == null) throw new Exception("El campo Nombre no puede ser vacio.");
        
        //validacion de datos vacios
        if ("".equals(articulo.getNombre())) throw new Exception("El campo Nombre no puede ser vacio.");
        
        //validacion de datos enteros nulos
        if (articulo.getPrecio() == 0) throw new Exception("El campo Precio no puede ser vacio.");
        
    }
    
    public void guardar(Articulo articulo) throws Exception{
        validar(articulo);
        
        if (articulo.getId()>0) ArticuloDAO.getInstance().Modificar(articulo);
        
        if (articulo.getId()==0){
            Bitacora bitacora = new Bitacora();
            bitacora.setUsuario(ManejadorSession.getInstance().obtenerUsuarioLogeado());
            bitacora.setFecha(new Date());
            bitacora.setEstado("ACTIVO");
            articulo.setBitacora(bitacora);
            ArticuloDAO.getInstance().Crear(articulo);
        } 
    }
    
     public Converter obtenerConversorArticulo() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                Object id = (Object)value;
                try{
                    int index = (id instanceof String)? Integer.valueOf((String)id) : (int) id;
                    Articulo elemento = ArticuloFachada.getInstance().obtenerArticuloPorId(index);
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
                        Articulo elemento = (Articulo) value;
                        Object o = ArticuloDAO.getInstance().getIdentifier(elemento);
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
    
     public Articulo obtenerArticuloPorId(int id) throws Exception{
        return ArticuloDAO.getInstance().Obtener(id);
    }
     
     public List<Articulo> obtenerArticulos() throws Exception{
        return ArticuloDAO.getInstance().Listar();
    }
}
