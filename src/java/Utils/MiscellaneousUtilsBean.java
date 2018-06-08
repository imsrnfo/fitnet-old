package Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named(value = "miscellaneousUtilsBean")
@RequestScoped

public class MiscellaneousUtilsBean {
  
   public String setearFormatoFechaHora(Date fecha){
       DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");      
        return df.format(fecha);
   }
   
   public String setearFormatoFecha(Date fecha){
       DateFormat df = new SimpleDateFormat("dd/MM/yyyy");      
        return df.format(fecha);
   }
   
   public String obtenerPath(String recurso){
       String result = "";
       result+= ManejadorNavegacion.getInstance().obtenerDireccion(recurso);
       return result;
   }
   
}
