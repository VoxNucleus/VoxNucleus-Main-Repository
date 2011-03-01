VoxNucleus 
==========
Note : Pardon pour les accents ils ne sont pas supportes par Github

Ce depot contient les sources completes de VoxNucleus, a visiter a l'adresse : [.VoxNucleus](http://www.voxnucleus.fr).

Organisation
____________

Les sources du site sont dans SourceWebsite/ tout est decoupe par application. Chaque application a une LOGIQUE qu'il convient de comprendre avant de tenter de modifier le programme.

Les repertoires accessibles a partir de la source tels que SolR/, cassandra/ contiennt les fichiers de configurations des logiciels utilises mais ne contient pas les executables. Si vous voulez utiliser les logiciels en l'etat veuillez a ne pas oublier de telecharger la version du logiciel indique dans Installation - Logiciel (voir plus bas)


Comment contribuer ?
____________________

Tout ceux qui le desirent peuvent contribuer a VoxNucleus.
Instructions :
1. "Forkez" le
2. Creez une branche
3. "Commitez" les changements. N'oubliez pas de preciser la nature des changements et ce que vous apportez de nouveau.
4. "Pushez" vers votre branche (Master vous renverra une erreur) 
5. Installez-vous confortablement et patientez pendant que l'on integre votre travail a VoxNucleus

Installation
____________

### Logicielse
Ce point est un peu delicat et sera expliqué par la suite.
Il faut avoir installe les logiciels suivants (les fichiers de configuration seront fournis) :
* Apache Cassandra 0.6.8
* PostGreSQL 8.3+
* Apache Solr 1.4.1
* Apache Tomcat 6
* Redis (Dans le proche futur)

### Jar et librairies
La liste est longue et elle varie selon les applications. Je travaillerai sur ce point dans le futur.