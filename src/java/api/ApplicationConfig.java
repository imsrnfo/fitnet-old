package api;

import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(api.ActividadFacadeREST.class);
        resources.add(api.ArticuloFacadeREST.class);
        resources.add(api.ClienteFacadeREST.class);
        resources.add(api.CobroFacadeREST.class);
        resources.add(api.ConceptoFacadeREST.class);
        resources.add(api.GimnasioFacadeREST.class);
        resources.add(api.MarcaFacadeREST.class);
        resources.add(api.MovimientoFacadeREST.class);
        resources.add(api.UsuarioFacadeREST.class);
    }
    
}
