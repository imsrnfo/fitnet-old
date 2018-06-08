package Fachadas;

public class ExcepcionFachada {
    
   
    
    private static ExcepcionFachada instance = null;
    
    public static ExcepcionFachada getInstance()  throws Exception{
        if(instance == null) {
            instance = new ExcepcionFachada();
        }
        return instance;
    }
    
    private ExcepcionFachada()  throws Exception{
      
    }

}
