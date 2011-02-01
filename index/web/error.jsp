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
                    meta.setAdditionnalKeywords("erreur");
                    Pass pass = Pass.getPass(request);
                    pass.launchAuthentifiate();
        %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%=SiteElements.getOneColumnStyle()%>
        <%=SiteElements.getCommonCSSStyle()%>
        <%=SiteElements.getCommonScripts()%>
        <%=SiteDecorations.setFavIcon()%>
        <title>VoxNucleus : Erreur !</title>
    </head>
    <body>
        <%=SiteElements.displayBasicMenu(pass)%>

        <div id="container">
            <div id="content" >
                <h2>C'est génant !</h2>

                Il semblerait que le site ait un problème ! Peut-être la page que
                vous cherchez n'existe plus ou une erreur interne vous a redirigé là.<br>
                Nous nous excusons pour cela et nous travaillons à résoudre ce problème !

            </div>
        </div>

        <%= SiteElements.displayFooter(pass)%>
        <%=GoogleAnalytics.getAnalyticsCode()%>
    </body>
</html>