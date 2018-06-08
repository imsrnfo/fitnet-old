package DAOs;

import DataBase.DBGenericClass;
import DataBase.HibernateUtil;
import HBMs.Actividad;
import HBMs.Articulo;
import HBMs.Bitacora;
import HBMs.Cliente;
import HBMs.Cobro;
import HBMs.CobroArticulo;
import HBMs.Gimnasio;
import Utils.ManejadorSession;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class CobroDAO extends DBGenericClass<Cobro>{
    
    private Session session;
    
    private static CobroDAO instance = null;
    
    public static CobroDAO getInstance()  throws Exception{
        if(instance == null) {
            instance = new CobroDAO();
        }
        return instance;
    }
    
    private CobroDAO()  throws Exception{
        super(Cobro.class);
        session=super.getSession();
    }
    
    public List<Cobro> buscarInicializarCobros()  throws Exception{
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        List<Cobro> list = null;
        try{
            if (!session.isOpen()) session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select c from " + Cobro.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio");
            query.setParameter("id_gimnasio", gym.getId());
            list = query.list();
            for (Cobro cobro : list) {
                Hibernate.initialize(cobro.getCliente());
                Hibernate.initialize(cobro.getBitacora());
                Hibernate.initialize(cobro.getCobroArticulos());
                List<CobroArticulo> listActCob = new ArrayList<>(cobro.getCobroArticulos());
                for (CobroArticulo actCob : listActCob) {
                    Hibernate.initialize(actCob.getArticulo());
                }
            }
        }catch(Exception e){
            throw e;
        }finally{
            session.close();
        }
        return list;
    }
    
    public List<Cobro> buscarInicializarCobrosPorCliente(Cliente cli)  throws Exception{
        List<Cobro> list = null;
        try{
            Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select c from " + Cobro.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio and c.cliente.id = :id_cliente");
            query.setParameter("id_gimnasio", gym.getId());
            query.setParameter("id_cliente", cli.getId());
            list = query.list();
            for (Cobro cobro : list) {
                Hibernate.initialize(cobro.getCliente());
                Hibernate.initialize(cobro.getCobroArticulos());
                List<CobroArticulo> listActCob = new ArrayList<CobroArticulo>(cobro.getCobroArticulos());
                for (CobroArticulo actCob : listActCob) {
                    Hibernate.initialize(actCob.getArticulo());
                }
            }
        }catch(Exception e){
            throw e;
        }finally{
            session.close();
        }
        return list;
    }
    
    public List<Cobro> buscarInicializarActividadCobrosPorCliente(Cliente cli)  throws Exception{
        List<Cobro> list = null;
        try{
            Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select c from " + Cobro.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio and c.cliente.id = :id_cliente");
            query.setParameter("id_gimnasio", gym.getId());
            query.setParameter("id_cliente", cli.getId());
            list = query.list();
            for (Cobro cobro : list) {
                Hibernate.initialize(cobro.getActividad());
            }
        }catch(Exception e){
            throw e;
        }finally{
            session.close();
        }
        return list;
    }
    
    public void guardarCobroYCobroActividad(Cobro cobro) throws Exception{
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            trans = session.beginTransaction();
            
            //obtengo lista de CobroActividadArticulo para no perder los datos
            List<CobroArticulo> listaCobroActividad = new ArrayList<>();
            for(Object object : cobro.getCobroArticulos()){
                if (object instanceof CobroArticulo){
                    CobroArticulo cobroArticulo = (CobroArticulo) object;
                    listaCobroActividad.add(cobroArticulo);
                }
            }
            
            //seteo la lista de CobroActividadArticulo del cobro a null para poder persistirlo
            cobro.setCobroArticulos(null);
            
            //persisto la bitacora para tener su fk al persistir cobro
            Bitacora bitacora = cobro.getBitacora();
            session.persist(bitacora);
            session.flush();
            
            //persistir Cobro vacio
            session.persist(cobro);
            session.flush();
            
            //persistir lista de CobroActividad
            for (CobroArticulo cobroArticulo : listaCobroActividad){
                session.merge(cobroArticulo);
                session.flush();
            }
            
            trans.commit();
        }catch(Exception e){
            trans.rollback();
            throw e;
        }finally{
            session.close();
        }
    }
    
    public List <CobroArticulo> obtenerListaCobroArticuloPorCliente(Cliente cli) throws Exception{
        List <CobroArticulo> listaCobroActividad = new ArrayList<>();
        try{
            Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select ca from " + CobroArticulo.class.getName() + " ca " + "where ca.cobro.cliente.id = :id_cli and ca.cobro.bitacora.usuario.gimnasio.id = :id_gimnasio");
            query.setParameter("id_cli", cli.getId());
            query.setParameter("id_gimnasio", gym.getId());
            listaCobroActividad = query.list();
            
            for (CobroArticulo item : listaCobroActividad) {
                Hibernate.initialize(item.getCobro());
                Hibernate.initialize(item.getArticulo());
                session.flush();
            }
            
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return listaCobroActividad;
    }
    
    public List <Object[]> obtenerListaCobrosPorFecha(Date mes) throws Exception{
        List <Object[]> result = new ArrayList<>();
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select ca.cobro.fecha, sum(ca.cobro.entrega) from " + CobroArticulo.class.getName() + " ca " + "where ca.cobro.bitacora.usuario.gimnasio.id = :id_gimnasio " + "group by "
                    + "ca.cobro.fecha"
            );
            query.setParameter("id_gimnasio", gym.getId());
            //query.setParameter("id_cli", cli.getId());
            result = query.list();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return result;
    }
    
    public Long obtenerGananciasDelMesActual() throws Exception{
        Long result = 0L;
        try{
            Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select SUM(c.entrega) from " + Cobro.class.getName() + " c where c.bitacora.usuario.gimnasio.id = :id_gimnasio and YEAR(c.fecha) = YEAR(current_date) AND MONTH(c.fecha) = MONTH(current_date)" );
            query.setParameter("id_gimnasio", gym.getId());
            if (query.uniqueResult()!=null) result = (Long) query.uniqueResult();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return result;
    }
    
    public Long obtenerGananciasDelMesAnterior() throws Exception{
        Long result = 0L;
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.MONTH, -1);
            Date mesAnterior = cal.getTime();
            Query query = session.createQuery("select SUM(c.entrega) from " + Cobro.class.getName() + " c where c.bitacora.usuario.gimnasio.id = :id_gimnasio and YEAR(c.fecha) = YEAR(:mes_anterior) AND MONTH(c.fecha) = MONTH(:mes_anterior) " );
            query.setParameter("id_gimnasio", gym.getId());
            query.setParameter("mes_anterior", mesAnterior);
            if (query.uniqueResult()!=null) result = (Long) query.uniqueResult();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return result;
    }
    
    public Long obtenerGananciasDeLaSemanaActual() throws Exception{
        Long result = 0L;
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select SUM(c.entrega) from " + Cobro.class.getName() + " c where c.bitacora.usuario.gimnasio.id = :id_gimnasio and YEARWEEK(c.fecha) = YEARWEEK(current_date)" );
            query.setParameter("id_gimnasio", gym.getId());
            if (query.uniqueResult()!=null) result = (Long) query.uniqueResult();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return result;
    }
    
    public Long obtenerGananciasDeLaSemanaAnterior() throws Exception{
        Long result = 0L;
        Gimnasio gym = ManejadorSession.getInstance().obtenerGimnasioLogeado();
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            Date semanaAnterior = cal.getTime();
            Query query = session.createQuery("select SUM(c.entrega) from " + Cobro.class.getName() + " c where c.bitacora.usuario.gimnasio.id = :id_gimnasio and YEARWEEK(c.fecha) = YEARWEEK(:semana_anterior)" );
            query.setParameter("id_gimnasio", gym.getId());
            query.setParameter("semana_anterior", semanaAnterior);
            if (query.uniqueResult()!=null) result = (Long) query.uniqueResult();
        }catch(HibernateException e){
            throw e;
        }finally{
            session.close();
        }
        return result;
    }
    
    public List <Object[]> obtenerDeudasDeClientes(Gimnasio gym)  throws Exception{
        List <Object[]> list = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select c.cliente, SUM(c.debe) from " + Cobro.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio group by c.cliente.id");
            query.setParameter("id_gimnasio", gym.getId());
            list = query.list();
        }catch(Exception e){
            throw e;
        }finally{
            session.close();
        }
        return list;
    }
    
    public List<Cobro> obtenerCobrosREST(String key)  throws Exception{
        Gimnasio gym = GimnasioDAO.getInstance().obtenerGimnasioRest(key);
        List<Cobro> list = null;
        try{
            if (!session.isOpen()) session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select c from " + Cobro.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio");
            query.setParameter("id_gimnasio", gym.getId());
            list = query.list();
            for (Cobro cobro : list) {
                cobro.setBitacora(null);
                Hibernate.initialize(cobro.getCliente());
                Hibernate.initialize(cobro.getActividad());
                Cliente cli = cobro.getCliente();
                cli.setCobros(null);
                cli.setMarcas(null);
                cli.setBitacora(null);
                Hibernate.initialize(cobro.getCobroArticulos());
                List<CobroArticulo> listActCob = new ArrayList<>(cobro.getCobroArticulos());
                for (CobroArticulo actCob : listActCob) {
                    Hibernate.initialize(actCob.getArticulo());
                    actCob.setCobro(null);
                    actCob.setArticulo(null);//TODO: quitar esto porque aun no esta implementado el agregar articulos al cobro.
                    Articulo art = actCob.getArticulo();
                    if (art!=null){
                        art.setBitacora(null);
                        art.setCobroArticulos(null);
                    }
                }
                Actividad act = cobro.getActividad();
                //un cobro puede tener unicamente articulos
                if (act!=null){
                    act.setBitacora(null);
                    act.setCobros(null);
                }
            }
        }catch(Exception e){
            throw e;
        }finally{
            session.close();
        }
        return list;
    }
    
    public Cobro obtenerCobroREST(String id, String key)  throws Exception{
        Gimnasio gym = GimnasioDAO.getInstance().obtenerGimnasioRest(key);
        List<Cobro> list = null;
        try{
            if (!session.isOpen()) session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select c from " + Cobro.class.getName() + " c " + "where c.bitacora.usuario.gimnasio.id = :id_gimnasio and c.id = :id");
            query.setParameter("id_gimnasio", gym.getId());
            query.setParameter("id", Integer.valueOf(id));
            list = query.list();            
            for (Cobro cobro : list) {
                Hibernate.initialize(cobro.getCliente());
                session.flush();
                Hibernate.initialize(cobro.getActividad());
                session.flush();
                cobro.setBitacora(null);
                Cliente cli = cobro.getCliente();
                cli.setCobros(null);
                cli.setMarcas(null);
                cli.setBitacora(null);
                Hibernate.initialize(cobro.getCobroArticulos());
                List<CobroArticulo> listActCob = new ArrayList<>(cobro.getCobroArticulos());
                for (CobroArticulo actCob : listActCob) {
                    Hibernate.initialize(actCob.getArticulo());
                    Articulo act = actCob.getArticulo();
                    actCob.setCobro(null);
                    act.setBitacora(null);
                    act.setCobroArticulos(null);
                }
                Actividad act = cobro.getActividad();
                //un cobro puede tener unicamente articulos
                if (act!=null){
                    act.setBitacora(null);
                    act.setCobros(null);
                }
            }
        }catch(Exception e){
            throw e;
        }finally{
            session.close();
        }
        return (list!=null && list.size()>0) ? list.get(0) : null;
    }
    
    public void crearCobroREST(Cobro entidad) throws Exception{
        
        try{
            session = HibernateUtil.getSessionFactory().openSession();

            //obtengo lista de CobroActividadArticulo para no perder los datos
            List<CobroArticulo> listaCobroActividad = new ArrayList<>();
            for(Object object : entidad.getCobroArticulos()){
                if (object instanceof CobroArticulo){
                    CobroArticulo cobroArticulo = (CobroArticulo) object;
                    listaCobroActividad.add(cobroArticulo);
                }
            }
            
            //seteo la lista de CobroActividadArticulo del cobro a null para poder persistirlo
            entidad.setCobroArticulos(null);
            
            //persisto la bitacora para tener su fk al persistir cobro
            Bitacora bitacora = entidad.getBitacora();
            session.persist(bitacora);
            session.flush();
            
            //persistir Cobro vacio
            session.persist(entidad);
            session.flush();
            
            //persistir lista de CobroActividad
            for (CobroArticulo cobroActividad : listaCobroActividad){
                session.merge(cobroActividad);
                session.flush();
            }
            
            
        }catch(Exception e){
            trans.rollback();
            throw e;
        }finally{
            session.close();
        }
    }
}
