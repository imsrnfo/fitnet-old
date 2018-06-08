# Fitnet

## Directorios de archivos

#### las imagenes de perfil de los administradores y de los clientes se guardan en:

```
homeDirectorio/fitnet/profilePicture/nombreArchivo.extension
```

el nombreArchivo para los administradores es su nick

el nombreArchivo para los clientes es su cedula

#### las imagenes de los gimnasios se guardan en:

```
homeDirectorio/fitnet/gimnasio/nombreArchivo.extension
```

el nombreArchivo para los gimansios es el nombre del gimansio.

hay que configurar wildfly para que tome la ruta de las imagenes:

```xml
<subsystem xmlns="urn:jboss:domain:undertow:2.0">
	<buffer-cache name="default"/>
	<server name="default-server">
		<ajp-listener name="ajp" socket-binding="ajp"/>
		<http-listener name="default" socket-binding="http" redirect-socket="https"/>
		<host name="default-host" alias="localhost">
			<location name="/" handler="welcome-content"/>ç
			<!--aqui se le especifica como se va a referenciar la ruta en el navegador-->
			<location name="/img" handler="images"/>
			<filter-ref name="server-header"/>
			<filter-ref name="x-powered-by-header"/>
		</host>
	</server>
	<servlet-container name="default">
		<jsp-config/>
		<websockets/>
	</servlet-container>
	<handlers>
		<file name="welcome-content" path="${jboss.home.dir}/welcome-content"/>
		<!--aqui se le especifica cual es la ruta absoluta de la carpeta referenciada-->
		<file name="images" path="/home/wildfly/fitnet" directory-listing="true"/>
	</handlers>
	<filters>
		<response-header name="server-header" header-name="Server" header-value="WildFly/9"/>
		<response-header name="x-powered-by-header" header-name="X-Powered-By" header-value="Undertow/1"/>
	</filters>
</subsystem>
```

## Guardar multiples clases en una transaccion.

al guardar multiples clases en una transaccion por ejemplo CobroActividad que requiere que se guarde la lista de Actividades y el Cobro
antes de persistir la clase CobroActividad, se debe setear en el HBM de Actividad y en el de Cobro:

### HBM
```xml
<id name="id" type="int">
    <column name="id" />
    <!--hace que retorne el id en cuantos se persiste-->
    <generator class="identity" />
</id>
```

### El valor por defecto del HBM es:
```xml
<id name="id" type="int">
    <column name="id" />
    <generator class="assigned" />
</id>
```

lo cual no nos retorna la ID al persistirlo hasta comitear y eso tiene por consecuencia no poder guardar las entidades que necesitan 
setear su fk.
