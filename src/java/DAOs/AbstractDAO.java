package DAOs;

import DataBase.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.metadata.ClassMetadata;

public abstract class AbstractDAO<T> {
    
    private static final SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();  // Create the SessionFactory from standard (hibernate.cfg.xml) config file.
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex); // Log the exception. 
            throw new ExceptionInInitializerError(ex);
        }
    }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    private Session session;
    protected Transaction trans;
    private Class<T> entityClass;
    
    public AbstractDAO(Class<T> entityClass) throws Exception {
        try{
            session = getSessionFactory().getCurrentSession();
            this.entityClass = entityClass;
        }catch(Exception e){
                //TODO: persistir el error
            throw e;
        }
    }
    
    public Session getSession() throws Exception{
        return this.session;
    }
    
    public abstract void Crear(T entidad, String... params) throws Exception;
    
    public abstract void Modificar(T entidad, String... params) throws Exception;
    
    public abstract T Obtener(int idEntidad, String... params) throws Exception;
    
    public abstract List<T> Listar(String... params) throws Exception;
    
    public Object getIdentifier(Object entity) throws Exception{
        Object result;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            ClassMetadata cmd = session.getSessionFactory().getClassMetadata(entityClass);
            result = cmd.getIdentifier(entity);
        }catch(Exception e){
            throw e;
        }finally{
            session.close();
        }
        return result;
    }

}
