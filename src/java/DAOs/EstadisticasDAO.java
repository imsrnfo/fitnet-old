package DAOs;

import DataBase.HibernateUtil;
import HBMs.Cliente;
import HBMs.Cobro;
import HBMs.CobroArticulo;
import HBMs.Gimnasio;
import HBMs.Marca;
import Utils.ManejadorSession;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class EstadisticasDAO {
    
    private Session session;
    protected Transaction trans;
    
    private static EstadisticasDAO instance = null;
    
    public static EstadisticasDAO getInstance()  throws Exception{
        if(instance == null) {
            instance = new EstadisticasDAO();
        }
        return instance;
    }
    
    private EstadisticasDAO()  throws Exception{
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        session = sessionFactory.getCurrentSession();
    }
    
    public Long obtenerAccesos(String fechaInicio, String fechaFin) throws Exception{
        Long result = 0L;
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select count(m) from " + Marca.class.getName() + " m " + "where m.bitacora.usuario.gimnasio.id = :id_gimnasio and "
                    + "DATE(m.fecha) >= STR_TO_DATE(:fechaInicio, '%d/%m/%Y') and "
                    + "DATE(m.fecha) <= STR_TO_DATE(:fechaFin, '%d/%m/%Y') ");
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
             query.setParameter("id_gimnasio", gym.getId());
            result = (Long)query.uniqueResult();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return result!=null?result:0L;
    }
    
    //TODO:para corregir
    // retorna cuanto se deberia haber cobrado por las actividades
    public Long obtenerGananciasSupuestasActividades(String fechaInicio, String fechaFin) throws Exception{
        Long result = 0L;
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery(""
                    + "select SUM(caa.actividad.precio) from " + CobroArticulo.class.getName() + " caa " + "where caa.cobro.bitacora.usuario.gimnasio.id = :id_gimnasio and "
                            + "DATE(caa.cobro.bitacora.fecha) >= STR_TO_DATE(:fechaInicio, '%d/%m/%Y') and "
                            + "DATE(caa.cobro.bitacora.fecha) <= STR_TO_DATE(:fechaFin, '%d/%m/%Y')"
            );
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
             query.setParameter("id_gimnasio", gym.getId());
            result = (Long)query.uniqueResult();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return result!=null?result:0L;
    }
    
    public Long obtenerGananciasSupuestasArticulos(String fechaInicio, String fechaFin) throws Exception{
        Long result = 0L;
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery(""
                    + "select SUM(caa.articulo.precio) from " + CobroArticulo.class.getName() + " caa " + "where caa.cobro.bitacora.usuario.gimnasio.id = :id_gimnasio and "
                            + "DATE(caa.cobro.bitacora.fecha) >= STR_TO_DATE(:fechaInicio, '%d/%m/%Y') and "
                            + "DATE(caa.cobro.bitacora.fecha) <= STR_TO_DATE(:fechaFin, '%d/%m/%Y')"
            );
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
             query.setParameter("id_gimnasio", gym.getId());
            result = (Long)query.uniqueResult();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        
        return result!=null?result:0L;
    }
    
    public Long obtenerDeudas(String fechaInicio, String fechaFin) throws Exception{
        Long result = 0L;
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery(""
                    + "select SUM(c.debe) from " + Cobro.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio and "
                            + "DATE(c.bitacora.fecha) >= STR_TO_DATE(:fechaInicio, '%d/%m/%Y') and "
                            + "DATE(c.bitacora.fecha) <= STR_TO_DATE(:fechaFin, '%d/%m/%Y')"
            );
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
             query.setParameter("id_gimnasio", gym.getId());
            result = (Long)query.uniqueResult();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return result!=null?result:0L;
    }
    
    public Long obtenerCantClientesActivos() throws Exception{
        Long result = 0L;
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery(""
                    + "select count(c) from " + Cliente.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio and  "
                            + "c.bitacora.estado = 'ACTIVO'"
            );
             query.setParameter("id_gimnasio", gym.getId());
            result = (Long)query.uniqueResult();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return result!=null?result:0L;
    }
    
    public Long obtenerCantClientesInactivos() throws Exception{
        Long result = 0L;
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery(""
                    + "select count(c) from " + Cliente.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio and "
                            + "c.bitacora.estado = 'INACTIVO'"
            );
             query.setParameter("id_gimnasio", gym.getId());
            result = (Long)query.uniqueResult();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return result!=null?result:0L;
    }
    
    public Long obtenerCantClientesMujeres() throws Exception{
        Long result = 0L;
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery(""
                    + "select count(c) from " + Cliente.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio and "
                            + "c.sexo = 'Femenino'"
            );
             query.setParameter("id_gimnasio", gym.getId());
            result = (Long)query.uniqueResult();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return result!=null?result:0L;
    }
    
    public Long obtenerCantClientesHombres() throws Exception{
        Long result = 0L;
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery(""
                    + "select count(c) from " + Cliente.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio and "
                            + "c.sexo = 'Masculino'"
            );
             query.setParameter("id_gimnasio", gym.getId());
            result = (Long)query.uniqueResult();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return result!=null?result:0L;
    }
    
    public Long obtenerCantClientesPorRangoEdad(int mayorIgual, int menorEstricto) throws Exception{
        BigInteger result = BigInteger.ZERO;
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        //PARA FILTRAR CLIENTES POR EL GIMNASIO LOGEADO
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createSQLQuery(
                    "SELECT " +
                    "	COUNT(c.id)  " +
                    "FROM  " +
                    "       fitnet.cliente c,  " +
                    "       fitnet.bitacora b, " +
                    "       fitnet.usuario u " +
                    "WHERE " +
                    "   (CAST(YEAR(CURRENT_DATE) - YEAR(c.fnac) - (RIGHT(CURRENT_DATE, 5) < RIGHT(c.fnac, 5)) AS DECIMAL(2,0) )) >= "+mayorIgual+" AND " +
                    "   (CAST(YEAR(CURRENT_DATE) - YEAR(c.fnac) - (RIGHT(CURRENT_DATE, 5) < RIGHT(c.fnac, 5)) AS DECIMAL(2,0) )) < "+menorEstricto+" AND " +
                    "   c.id_bitacora = b.id AND " +
                    "   b.id_usuario = u.id AND " +
                    "   u.id_gimnasio = 1");
            result = (BigInteger)query.uniqueResult();
            
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return result!=null?result.longValue():0L;
    }
    
    public Long obtenerCantClientesRegistradosRango(String fechaInicio, String fechaFin) throws Exception{
        Long result = 0L;
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery(""
                    + "select count(c) from " + Cliente.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio and "
                            + "DATE(c.bitacora.fecha) >= STR_TO_DATE(:fechaInicio, '%d/%m/%Y') and "
                            + "DATE(c.bitacora.fecha) <= STR_TO_DATE(:fechaFin, '%d/%m/%Y')"
            );
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
             query.setParameter("id_gimnasio", gym.getId());
            result = (Long)query.uniqueResult();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return result!=null?result:0L;
    }
    
    public List <Object[]> infoAccesosHora(String fechaInicio, String fechaFin) throws Exception{
         List <Object[]> result = new ArrayList<>();
         Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery(""
                    + "select count(m.id), HOUR(m.fecha) from " + Marca.class.getName() + " m " + "where m.bitacora.usuario.gimnasio.id = :id_gimnasio and "
                            + "DATE(m.fecha) >= STR_TO_DATE(:fechaInicio, '%d/%m/%Y') and "
                            + "DATE(m.fecha) <= STR_TO_DATE(:fechaFin, '%d/%m/%Y') "
                            + "group by HOUR(m.fecha), m.id order by HOUR(m.fecha) asc"
            );
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
             query.setParameter("id_gimnasio", gym.getId());
             result = query.list();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return result;
    }
    
    public List <Object[]> infoCobrosDia(String fechaInicio, String fechaFin) throws Exception{
         List <Object[]> result = new ArrayList<>();
         Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery(""
                    + "select sum(c.entrega), DAY(c.bitacora.fecha), MONTH(c.bitacora.fecha), YEAR(c.bitacora.fecha) from " + Cobro.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio and "
                            + "DATE(c.bitacora.fecha) >= STR_TO_DATE(:fechaInicio, '%d/%m/%Y') and "
                            + "DATE(c.bitacora.fecha) <= STR_TO_DATE(:fechaFin, '%d/%m/%Y') "
                            + "group by DAY(c.bitacora.fecha), MONTH(c.bitacora.fecha), YEAR(c.bitacora.fecha) order by DAY(c.bitacora.fecha) asc, MONTH(c.bitacora.fecha) asc, YEAR(c.bitacora.fecha) asc"
            );
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
             query.setParameter("id_gimnasio", gym.getId());
             result = query.list();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return result;
    }
    
}
