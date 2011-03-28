<%@page import="me.FallingDownLib.CommonClasses.UserFields"%>
<%@page import="java.io.UnsupportedEncodingException"%>
<%@page import="me.FallingDownLib.CommonClasses.identification.Pass"%>
<%@page import="me.FallingDownLib.CommonClasses.www.statistic.GoogleAnalytics"%>
<%@page import="me.FallingDownLib.CommonClasses.www.SiteElements" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%=SiteElements.getCommonScripts()%>
        <link rel="stylesheet" type="text/css" href="css/login.css" />
        <link rel="stylesheet" type="text/css" href="/css/general/layout_one_column.css" />
        <script type="text/javascript" src="/jsp/usermanagement/jlogin.js"></script>
        <%=SiteElements.getCommonCSSStyle()%>
        <title>VoxNucleus : se connecter</title>
    </head>
    <body>
        <%Pass pass = Pass.getPass(request);
                    pass.launchAuthentifiate();
                    String decodedWhereFrom;
                    if (request.getParameterValues("wherefrom") != null
                            && request.getParameterValues("wherefrom").length > 0) {
                        decodedWhereFrom = request.getParameterValues("wherefrom")[0];
                    } else {
                        decodedWhereFrom = "/";
                    }

        %>
        <%= SiteElements.displayBasicMenu(pass)%>

        <div id="container">
            <div id="content">
                <% if (!pass.getIsAuthentified()) {%>
                <h1>Se connecter sur VoxNucleus :</h1>

                <form id="login_form" method="POST" action="/usermanagement/login">
                    <fieldset id="login_zone">

                        <legend align="top">Connexion à son compte</legend>
                        <table>
                            <tr>
                                <td> Utilisateur </td>
                                <td> <INPUT type="text" required="required"
                                            NAME=<%=UserFields.HTTP_LOGIN_USERNAME%>> </td>
                            </tr>
                            <tr>
                                <td>Mot de passe</td>
                                <td><INPUT type="password"
                                           required="required" NAME="password"></td>
                            </tr>
                        </table>
                        <p align="center">
                            <input type="checkbox" name="long_session" checked="checked" value="true"> Se souvenir de moi
                        </p>
                        <input type="hidden" name="wherefrom" value="<%=decodedWhereFrom%>">
                        <p align="center">
                            <button type="SUBMIT"> Se connecter </button></p>
                    </fieldset>
                </form>

                <div class="mini_centered" id="subscribe"><a href="/usermanagement/subscribe.jsp">Pas
                        encore enregistré ? Créez un compte simplement en cliquant sur ce lien</a></div>
                <div class="mini_centered" id="pass_forgotten"><a href="usermanagement/modify/passwordreset">
                        Mot de passe oublié ? Cliquez ici pour résoudre ce problème.</a></div>
                        <%} else {%>

                <h2 style="text-align:center">Déjà connecté !</h2>
                Il semblerait que vous soyez déjà connecté, souhaitez vous vous déconnecter ?<br>
                <a href="/usermanagement/disconnect">Suivez ce lien pour vous déconnecter </a>
                <%}%>
            </div>
        </div>
        <%=SiteElements.displayFooter(pass)%>
        <%=GoogleAnalytics.getAnalyticsCode()%>
    </body>
</html>
