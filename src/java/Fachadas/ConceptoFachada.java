package Fachadas;

import DAOs.ConceptoDAO;
import HBMs.Bitacora;
import HBMs.Concepto;
import Utils.ManejadorSession;
import java.util.Date;

public class ConceptoFachada {
    
    private static ConceptoFachada instance = null;
    
    public static ConceptoFachada getInstance()  throws Exception{
        if(instance == null) {
            instance = new ConceptoFachada();
        }
        return instance;
    }
    
    private ConceptoFachada() throws Exception {}
    
    public void validar(Concepto concepto) throws Exception{
        //validacion de datos nulos
        if (concepto.getNombre() == null) throw new Exception("El campo Nombre no puede ser vacio.");
        if (concepto.getDescripcion()== null) throw new Exception("El campo Descripcion no puede ser vacio.");
        
        //validacion de datos vacios
        if ("".equals(concepto.getNombre())) throw new Exception("El campo Nombre no puede ser vacio.");
        if (concepto.getDescripcion() == null) throw new Exception("El campo Descripcion no puede ser vacio.");
    }
    
    public void guardar(Concepto concepto) throws Exception{
        ConceptoFachada.getInstance().validar(concepto);
        if (concepto.getId()>0) ConceptoDAO.getInstance().update(concepto);
        if (concepto.getId()==0){
            Bitacora bitacora = new Bitacora();
            bitacora.setUsuario(ManejadorSession.getInstance().obtenerUsuarioLogeado());
            bitacora.setFecha(new Date());
            bitacora.setEstado("ACTIVO");
            concepto.setBitacora(bitacora);
            ConceptoDAO.getInstance().guardar(concepto);
        }
        
    }
    
}
