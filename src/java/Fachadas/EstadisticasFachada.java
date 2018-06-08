package Fachadas;

public class EstadisticasFachada {
    
  
    private static EstadisticasFachada instance = null;
    
    public static EstadisticasFachada getInstance()  throws Exception{
        if(instance == null) {
            instance = new EstadisticasFachada();
        }
        return instance;
    }
    
    private EstadisticasFachada()  throws Exception{
       
    }
    
    
    
}
