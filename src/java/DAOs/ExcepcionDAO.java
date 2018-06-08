package DAOs;

import DataBase.DBGenericClass;
import HBMs.Excepcion;
import org.hibernate.Session;

public class ExcepcionDAO extends DBGenericClass<Excepcion>{
    
    private Session session;
    
    private static ExcepcionDAO instance = null;
    
    public static ExcepcionDAO getInstance()  throws Exception{
        if(instance == null) {
            instance = new ExcepcionDAO();
        }
        return instance;
    }
    
    private ExcepcionDAO()  throws Exception{
        super(Excepcion.class);
        session=super.getSession();
    }

}
