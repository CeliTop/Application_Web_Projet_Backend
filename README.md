# Projet Application Web - Backend
## _Tiktok like website_

Dépot Git du backend Java pour le projet d'application web de l'ENSEEIHT.

Packages (et leur version utilisées pendant le développemenet):
- Java 11
- EAP 7.4.0
- GSON 2.10.1

## Documentation de l'API
On a utilisé Postman pour tester l'API et créer la [documentation](https://documenter.getpostman.com/view/22501258/2s93XyT35u).

## Front-Back séparés
Pour lancer le serveur back et y accéder avec une adresse différente de 127.0.0.1 (backend et frontend séparés par exemple):
```console
./bin/standalone.sh -b 0.0.0.0
```
Si les requêtes fetch depuis le front-end sont bloquées, il faut entrer l'adresse du fetch directement dans le navigateur pour autoriser le certificat auto-signé.

## BDD persistente
Pour créer la base de données en JBOSS.
Créer un utilisateur admin jboss avec:
```console
./bin/add-user.sh
```
Puis aller sur la console de management une fois le serveur lancé (généralement localhost:9990)
```console
Configuration > Subsystems > DataSources and Drivers > DataSources
```
Ajouter un datasource de type H2 avec en JNDI `java:/H2tiktokDS` et Connection URL `jdbc:h2:file:~/tiktokDB;DB_CLOSE_DELAY=-1`
Il faut probablement adapter le chemin apres `file:`, par exemple `/home/user/EAP-7.4.0/standalone/data/tiktokDB`

## Analyse de la BDD
En téléchargeant [h2-console](https://developers.redhat.com/quickstarts/eap-archive/h2-console), puis en extrayant le `.war` situé dans le `zip/h2-console/` et en le déployant dans le dossier `standalone/deployments`, il est possible d'acceder directement à la bdd en utilisant le login et mdp par défaut `SA` et le bon jdbc url utilisé dans le datasource sur le lien `http://localhost:8080/h2-console/`.

## TODO
- entity VIDEO: (facade get video par lieu ?), savoir si l'utilisateur qui recoit la vidéo est abonné au compte de celui qui upload
- entity Compte: pdp (entier avatar), abonnés, abonnements, nbAbonnés, nbVideoUpload
