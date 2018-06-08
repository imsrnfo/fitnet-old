package Utils;

import DAOs.AbstractDAO;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class ConversorGenerico<T> {
    protected AbstractDAO helper;
    
    public Converter obtenerConversor() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                // This method is called when HTTP request parameter is to be converted to item value.
                // You need to convert the student ID back to Student.
                try{
                    Object id = (Object)value;
                    int index = (id instanceof String)? Integer.valueOf((String)id) : (int) id;
                    T elemento = (T) helper.Obtener(index);
                    return elemento;
                }catch (Exception e){
                    //TODO: persistir la excepcion
                }
                return null;
            }
            
            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                // This method is called when item value is to be converted to HTTP request parameter.
                // Normal practice is to return an unique identifier here, such as student ID.
                try{
                    T elemento = (T) value;
                    Object o = helper.getIdentifier(elemento);
                    String result = String.valueOf(o);
                    return result;
                }catch (Exception e){
                    //TODO: persistir la excepcion
                }
                return null;
            }
        };
    }
}
