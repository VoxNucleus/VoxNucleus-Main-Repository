VoxNucleus
==========

Ce d�p�t contient les sources compl�tes de VoxNucleus, � visiter � l'adresse : [.VoxNucleus](http://www.voxnucleus.fr).
Avant d'essayer d'installer le 

Organisation
____________

Les sources du site sont dans SourceWebsite/ tout est d�coup� par application. Chaque application a une LOGIQUE qu'il convient de comprendre avant de tenter de modifier le programme.

Les r�pertoires accessibles � partir de la source tels que solr/, cassandra/ contiennt les fichiers de configurations des logiciels utilis�s mais ne contient pas les ex�cutables. Si vous voulez utiliser les logiciels en l'�tat veuillez � ne pas oublier de t�l�charger la version du logiciel indiqu� dans Installation - Logiciel (voir plus bas)


Comment contribuer ?
____________________

Tout ceux qui le d�sirent peuvent contribuer � VoxNucleus.
Instructions :
1. "Forkez" le
2. Cr�ez une branche
3. "Commitez" les changements. N'oubliez pas de pr�ciser la nature des changements et ce que vous apportez de nouveau.
4. "Pushez" vers votre branche (Master vous renverra une erreur) 
5. Installez-vous confortablement et patientez que l'on int�gre votre oeuvre � VoxNucleus

Installation
____________

### Logiciels

Ce point est un peu d�licat et sera expliqu� par la suite.
Il faut avoir install� les logiciels suivants (les fichiers de configuration seront fournis) :
* Apache Cassandra 0.6.8
* PostGreSQL 8.3+
* Apache Solr 1.4.1
* Apache Tomcat 6
* Memcached (dans le futur)

### Jar et librairies
La liste est trop longue pour �tre donn�e et elle varie selon les applications. La liste sera donn�e