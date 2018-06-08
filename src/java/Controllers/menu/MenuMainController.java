
package Controllers.menu;

import Controllers.*;
import DAOs.CobroDAO;
import Utils.ManejadorNavegacion;
import Utils.ManejadorSession;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;


@Named(value = "menuMainController")
@ViewScoped
public class MenuMainController implements Serializable{
    
    private String infoCobrosMes(boolean solicitarDatos){
        try{
            String resultDias = "";
            String resultDatos = "";
            
            Calendar start = Calendar.getInstance();
            start.setTime(new Date());
            start.set(Calendar.DAY_OF_MONTH, 1);
            Calendar end = Calendar.getInstance();
            end.setTime(new Date());
            end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
            
            List <Object[]> mapFechaCobroTotal = CobroDAO.getInstance().obtenerListaCobrosPorFecha(new Date());
            
            List<String> dias = new ArrayList<>();
            List<String> datos = new ArrayList<>();
            
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
            for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
                boolean tieneDatos = false;
                for (Object[] item : mapFechaCobroTotal){
                    if (fmt.format((Date)item[0]).equals(fmt.format(date))){
                        tieneDatos=true;
                        datos.add( String.valueOf(item[1]));
                    }
                }
                if (!tieneDatos){
                    datos.add("0");
                }
                dias.add(String.valueOf(start.get(Calendar.DAY_OF_MONTH)));
            }
            
            for (int i = 0; i < dias.size(); i++) {
                resultDias+= dias.get(i);
                if (i+1 < dias.size()) resultDias+=" , ";
            }
            
            for (int i = 0; i < datos.size(); i++) {
                resultDatos+= datos.get(i);
                if (i+1 < datos.size()) resultDatos+=" , ";
            }
            
            System.out.println(resultDias);
            System.out.println(resultDatos);
            
            if (solicitarDatos){
                return resultDatos;
            }else{
                return resultDias;
            }
        } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return null;
        
    }
    
    public String datosCobrosMes(){
        return infoCobrosMes(true);
    }
    
    public String diasCobrosMes(){
        return infoCobrosMes(false);
    }
    
    public String obtenerGananciasMesActual(){
        try{
        return String.valueOf(CobroDAO.getInstance().obtenerGananciasDelMesActual());
          } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return "";
    }
    
    public String obtenerGananciasMesAnterior(){
        try{
        return String.valueOf(CobroDAO.getInstance().obtenerGananciasDelMesAnterior());
          } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return "";
    }
    
    public String obtenerGananciasSemanaActual(){
        try{
        return String.valueOf(CobroDAO.getInstance().obtenerGananciasDeLaSemanaActual());
          } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return "";
    }
    
    public String obtenerGananciasSemanaAnterior(){
        try{
        return String.valueOf(CobroDAO.getInstance().obtenerGananciasDeLaSemanaAnterior());
          } catch (Exception ex) {
            //TODO: persistir la excepcion
        }
        return "";
    }
    
    @PostConstruct
    public void init(){
        if (ManejadorSession.getInstance().obtenerUsuarioLogeado()==null) ManejadorNavegacion.getInstance().redirigir("/");
    }
    
}
