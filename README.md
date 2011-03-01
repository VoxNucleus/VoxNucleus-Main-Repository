VoxNucleus
==========

Ce dépôt contient les sources complètes de VoxNucleus, à visiter à l'adresse : [.VoxNucleus](http://www.voxnucleus.fr).
Avant d'essayer d'installer le 

Organisation
____________

Les sources du site sont dans SourceWebsite/ tout est découpé par application. Chaque application a une LOGIQUE qu'il convient de comprendre avant de tenter de modifier le programme.

Les répertoires accessibles à partir de la source tels que solr/, cassandra/ contiennt les fichiers de configurations des logiciels utilisés mais ne contient pas les exécutables. Si vous voulez utiliser les logiciels en l'état veuillez à ne pas oublier de télécharger la version du logiciel indiqué dans Installation - Logiciel (voir plus bas)


Comment contribuer ?
____________________

Tout ceux qui le désirent peuvent contribuer à VoxNucleus.
Instructions :
1. "Forkez" le
2. Créez une branche
3. "Commitez" les changements. N'oubliez pas de préciser la nature des changements et ce que vous apportez de nouveau.
4. "Pushez" vers votre branche (Master vous renverra une erreur) 
5. Installez-vous confortablement et patientez que l'on intègre votre oeuvre à VoxNucleus

Installation
____________

### Logiciels

Ce point est un peu délicat et sera expliqué par la suite.
Il faut avoir installé les logiciels suivants (les fichiers de configuration seront fournis) :
* Apache Cassandra 0.6.8
* PostGreSQL 8.3+
* Apache Solr 1.4.1
* Apache Tomcat 6
* Memcached (dans le futur)

### Jar et librairies
La liste est trop longue pour être donnée et elle varie selon les applications. La liste sera donnée