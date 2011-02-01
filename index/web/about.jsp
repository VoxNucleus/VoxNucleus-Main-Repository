<%-- 
    Document   : about
    Created on : May 14, 2010, 6:50:53 PM
    Author     : victork
--%>
<%@page import="me.FallingDownLib.CommonClasses.identification.Pass"%>
<%@page import="me.FallingDownLib.CommonClasses.www.statistic.GoogleAnalytics"%>
<%@page import="me.FallingDownLib.CommonClasses.www.Metadata"%>
<%@page import="me.FallingDownLib.CommonClasses.www.SiteDecorations"%>
<%@page import="me.FallingDownLib.CommonClasses.www.SiteElements" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <% Metadata meta = Metadata.getInstance();
                    meta.setAdditionnalKeywords("à propos, Florian, Duraffourg, Victor, Kabdebon, équipe");
                    Pass pass = Pass.getPass(request);
                    pass.launchAuthentifiate();
        %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%=SiteElements.getOneColumnStyle()%>
        <%=SiteElements.getCommonCSSStyle()%>
        <%=SiteElements.getCommonScripts()%>
        <%=SiteDecorations.setFavIcon()%>
        <title>VoxNucleus - A propos...</title>
    </head>
    <body>
        <%=SiteElements.displayBasicMenu(pass)%>

        <div id="container">
            <div id="content" >
                <h1>VoxNucleus</h1>

                <h3>Pourquoi ce site ?</h3>
                <p align="center"><i>Tout d'abord un peu de culture : <br>
                        Vox : (latin) la voix, la parole <br>
                        Nucleus : (latin) le noyau </i><br></p>

                Aujourd'hui internet est un univers sans cesse en mouvement et il
                devient difficile de sélectionner les contenus intéressants.<br>

                Ce site tente d'apporter une solution à ce problème. Sur VoxNucleus 
                chacun a le droit (tant que ce qui est posté est légal)
                de poster des messages (appelés "noyaux" ou "nucleus") et chacun peut
                contribuer à faire grandir ce noyau en votant pour lui. Tout ce qui
                aura été choisi par la majorité sera parmi les premiers.

                <h3>Comment ?</h3>
                Ce site a été codé depuis zéro. Afin d'avoir un résultat optimal je
                n'ai jamais utilisé de CMS. Toutefois j'ai profité largement des
                outils créés par la Fondation apache tels que Tomcat et Cassandra.
                Je tiens donc à les remercier de fournir un si brillant travail
                de façon opensource.<br>
                Je tiens aussi à saluer la communauté des développeurs de la
                bibliothèque jQuery dont le travail autour de Javascript permet
                de simplifier le développement d'interfaces interactives riches
                et solides.


                <h1>L'équipe</h1>
                L'équipe est pour l'instant constituée de deux personnes. Pour
                nous contacter nous écrire à l'adresse suivante :
                <a href="mailto:team@voxnucleus.fr"></a>
                <h3>Développement</h3>
                <b>Développeur :</b> Victor Kabdebon<br>
                Etudiant en double diplôme à Telecom SudParis et à Georgia Tech,
                je suis passionné par les nouvelles technologies et je suis l'actualité
                de très près. Cette passion m'a amené à créer ce site.
                <br> <b>Contact : <a href="mailto:victor@voxnucleus.fr">victor@voxnucleus.fr</a> </b>
                <br> <b>Autres liens : <a href="http://victor-kabdebon.blogspot.com" target="_blank">Blog (technique)</a> </b>

                <h3>Administration Système</h3>
                <b>Administrateur : </b> Florian Duraffourg <br>
                Diplômé de Telecom SudParis Florian administre les serveurs du site.
                Il travaille actuellement en tant qu'ingénieur réseau/ sécurité
                chez l'opérateur mobile Orange.
                <br> <b>Contact : <a href="mailto:florian@voxnucleus.fr">florian@voxnucleus.fr</a> </b>

                <h3>WebDesign</h3>
                <b>Webdesigner : </b> Hugo Leygnac <br>
                Etudiant à Supinfocom Arles, passionné par le cinéma d'animation et
                le graphisme, c'est avec plaisir qu'il a accepté de rejoindre les rangs
                du staff pour travailler le design de VoxNucleus.
                <br> <b>Contact : <a href="mailto:hugo@voxnucleus.fr">hugo@voxnucleus.fr</a> </b>


                <h1>Comment participer ?</h1>
                <h3>Publicité</h3>
                <i>Message aux utilisateurs :</i><br>
                Ce site est gratuit pour vous, mais l'entretien des serveurs coûte
                de l'argent à l'équipe. Si vous appréciez ce site et vous
                souhaitez continuer à pouvoir aller dessus et profiter de ce
                qu'il offre <b>merci de désactiver les programmes anti-publicité</b>
                tels que AdBlock. Nous tentons de faire notre possible pour
                rendre les publicités discrètes mais si vous jugez la publicité
                trop envahissante envoyez un mail à l'équipe, le problème pourra
                être réglé de la sorte.<br>

                <i>Message aux entreprises :</i><br>
                Si vous êtes intéressés pour faire de la publicité sur ce site
                (hors publicités Google) veuillez contacter l'adresse suivante :
                <a href="mailto:pub@voxnucleus.fr">pub@voxnucleus.fr</a>

                <h3>Le site</h3>
                Comme vous pouvez le constater il existe quelques (...d'accord de
                nombreux) défauts sur le site. Mais ce n'est pas une fatalité
                et il y a des solutions à tout. Nous recherchons des personnes
                intéressées par les domaines suivants :<br>
                <b>Développement Java/Scala : </b> Ce site a été codé en Java et 
                des parties vont être petit à petit migrées vers Scala. Nous recherchons
                des personnes motivées pour travailler avec nous.<br>
                <b>Base de données : </b> Sur ce site nous utilisons des bases
                de données orientées colonnes. Plus précisément les bases de données 
                <a href="http://cassandra.apache.org/">Cassandra</a> Si vous
                souhaitez vous investir dans le développement sur ce qui sera
                probablement les prochaines bases de données vous pouvez nous
                rejoindre.<br>
                <b>Autres :</b> Il existe bien d'autres domaines dans lequel
                il est possible de s'investir : développement jQuery, java etc...
                N'hésitez pas à prposer spontanément <br>
                <br>
                Si vous êtes intéressés par les offres listées n'hésitez pas à
                nous contacter à l'adresse suivante : 
                <a href="mailto:offres@voxnucleus.fr">offres@voxnucleus.fr</a>
            </div>
        </div>

        <%= SiteElements.displayFooter(pass)%>
        <%=GoogleAnalytics.getAnalyticsCode()%>
    </body>
</html>
