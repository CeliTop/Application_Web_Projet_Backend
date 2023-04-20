# Projet Application Web - Backend
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
Si les requêtes fetch depuis le front-end sont bloquées, il faut entrer l'adresse du fetch directement dans le navigateur pour autoriser le certificat auto-signé.

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

## TODO
- Faire l'entity commentaire
- entity VIDEO: ajouter hashtag, description, commentaires, lieux, nb_vues
- entity Compte: bio, pdp, depuis quand il a rejoint, abonnés, abonnements, videosVues, videosLikes
