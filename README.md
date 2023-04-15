# Projet Application Web
## _Tiktok like website_

Dépot Git du backend Java pour le projet d'application web de l'ENSEEIHT.

Packages (et leur version utilisées pendant le développemenet):
- Java 11
- EAP 7.4.0
- GSON 2.10.1


## Front-Back séparés
Pour lancer le serveur back et y accéder avec une adresse différente de 127.0.0.1 (backend et frontend séparés par exemple):
```console
./bin/standalone.sh -b 0.0.0.0
```
## Cors
S'il y a ds problèmes de CORS entre le front et le back, il faut configuer JBOSS pour accepter les requetes Cross-Origin.
Il faut compléter le tag `subsystem xmlns="urn:jboss:domain:undertow:12.0"` du fichier `EAP-7.4.0/standalone/standalone.xml` :
```xml
<subsystem xmlns="urn:jboss:domain:undertow:12.0" default-server="default-server" default-virtual-host="default-host" default-servlet-container="default" default-security-domain="other" statistics-enabled="${wildfly.undertow.statistics-enabled:${wildfly.statistics-enabled:false}}">
            <buffer-cache name="default"/>
            <server name="default-server">
                <http-listener name="default" socket-binding="http" redirect-socket="https" enable-http2="true"/>
                <https-listener name="https" socket-binding="https" security-realm="ApplicationRealm" enable-http2="true"/>
                <host name="default-host" alias="localhost">
                    <location name="/" handler="welcome-content"/>
                    <http-invoker security-realm="ApplicationRealm"/>
                    <filter-ref name="server-header"/>
                    <filter-ref name="x-powered-by-header"/>
                    <filter-ref name="Access-Control-Allow-Origin"/>
                    <filter-ref name="Access-Control-Allow-Methods"/>
                    <filter-ref name="Access-Control-Allow-Headers"/>
                    <filter-ref name="Access-Control-Allow-Credentials"/>
                    <filter-ref name="Access-Control-Max-Age"/>
                </host>
            </server>
            <servlet-container name="default">
                <jsp-config/>
                <websockets/>
            </servlet-container>
            <handlers>
                <file name="welcome-content" path="${jboss.home.dir}/welcome-content"/>
            </handlers>
            <filters>
                <response-header name="server-header" header-name="Server" header-value="WildFly/10"/>
                <response-header name="x-powered-by-header" header-name="X-Powered-By" header-value="Undertow/1"/>
                <response-header name="Access-Control-Allow-Origin" header-name="Access-Control-Allow-Origin" header-value="*"/>
                <response-header name="Access-Control-Allow-Methods" header-name="Access-Control-Allow-Methods" header-value="GET, POST, OPTIONS, PUT"/>
                <response-header name="Access-Control-Allow-Headers" header-name="Access-Control-Allow-Headers" header-value="accept, authorization,  content-type, x-requested-with"/>
                <response-header name="Access-Control-Allow-Credentials" header-name="Access-Control-Allow-Credentials" header-value="true"/>
                <response-header name="Access-Control-Max-Age" header-name="Access-Control-Max-Age" header-value="1"/>
            </filters>
        </subsystem>
```

## BDD persistente
Pour créer la base de données en JBOSS.
Créer un utilisateur admin jboss avec:
```console
./bin/add_user.sh
```
Puis aller sur la console de management une fois le serveur lancé (généralement localhost:9990)
```console
Configuration > Subsystems > DataSources and Drivers > DataSources
```
Ajouter un datasource de type H2 avec en JNDI `java:/H2tiktokDS` et Connection URL `jdbc:h2:file:~/tiktokDB;DB_CLOSE_DELAY=-1`
Il faut probablement adapter le chemin apres `file:`, par exemple `/home/user/EAP-7.4.0/standalone/data/tiktokDB`
