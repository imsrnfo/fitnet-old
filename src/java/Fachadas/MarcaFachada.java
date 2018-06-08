package Fachadas;

public class MarcaFachada {
    
   
    
    private static MarcaFachada instance = null;
    
    public static MarcaFachada getInstance() throws Exception {
        if(instance == null) {
            instance = new MarcaFachada();
        }
        return instance;
    }
    
    private MarcaFachada()  throws Exception{
       
    }
    
   
}
