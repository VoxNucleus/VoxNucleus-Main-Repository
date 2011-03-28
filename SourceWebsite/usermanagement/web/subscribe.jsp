<%@page import="me.FallingDownLib.CommonClasses.identification.Pass"%>
<%@page import="me.FallingDownLib.CommonClasses.www.statistic.GoogleAnalytics"%>
<%@page import="me.FallingDownLib.CommonClasses.www.SiteDecorations"%>
<%@page import="me.FallingDownLib.CommonClasses.UserFields"%>
<%@page import="me.FallingDownLib.CommonClasses.www.SiteElements" %>
<%@page import="me.FallingDownLib.CommonClasses.SupportedLanguages" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<% SupportedLanguages.getInstance();%>

<html>
    <head>
        <%=SiteElements.getCommonScripts()%>
        <script type="text/javascript" src="/jsp/usermanagement/jsubscribe.js"></script>
        <link rel="stylesheet" type="text/css" href="css/subscribe.css" >
        <link rel="stylesheet" type="text/css" href="/css/general/menu.css">
        <link rel="stylesheet" type="text/css" href="/css/general/general_layout.css">
        <link rel="stylesheet" type="text/css" href="/css/general/footer.css" >
        <%=SiteElements.getOneColumnStyle()%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%=SiteDecorations.setFavIcon()%>
        <title>VoxNucleus - Créer un nouvel utilisateur</title>
    </head>
    <body>
        <% Pass suscribePass = Pass.getPass(request);
        suscribePass.launchAuthentifiate();%>
        <%= SiteElements.displayBasicMenu(suscribePass)%>
        <div id="container">
            <div id="content">
                <h1>Création de compte VoxNucleus</h1>
                <div id="disclaimer" >
                    Nous accordons une grande valeur à la vie privée. C'est pourquoi
                    avant de vous inscrire nous vous invitons à consulter les
                    conditions d'utilisation du site.
                </div>
                <form validated="false" id="subscribe_form" method="POST" action="/usermanagement/createUser">
                    <div id="informations" >

                        <fieldset id="mandatory_informations">
                            <legend align="top">Informations obligatoires</legend>
                            <table>
                                <tr>
                                    <td class="left_col">Nom d'utilisateur*</td>
                                    <td><input type="text" required="required" pattern="[a-zA-Z0-9]{2,15}" maxlength="15" NAME="<%=UserFields.HTTP_USERNAME%>"></td>
                                </tr>
                                <tr>
                                    <td class="left_col">Mot de passe*</td>
                                    <td><input type="password" required="required"
                                               pattern="[a-zA-Z0-9]{5,12}" maxlength="12" NAME="<%=UserFields.HTTP_PASSWORD%>" maxlength="15"> </td>
                                </tr>
                                <tr>
                                    <td class="left_col">Confirmation mot de passe* </td>
                                    <td> <input type="password" required="required"
                                                pattern="[a-zA-Z0-9]{5,12}" maxlength="12" 
                                                data-equals="<%=UserFields.HTTP_PASSWORD%>"

                                                NAME="<%=UserFields.HTTP_PASSWORD_CONFIRM%>" maxlength="15"> </td>
                                </tr>
                                <tr>
                                    <td class="left_col">Email (actif)*</td>
                                    <td><input type="email" required="required"
                                               NAME="<%=UserFields.HTTP_EMAIL%>"> </td>
                                </tr>
                                <tr>
                                    <td class="left_col">Retapez l'email*</td>
                                    <td><input type="email" required="required" data-equals="<%=UserFields.HTTP_EMAIL%>" NAME="<%=UserFields.HTTP_EMAIL_CONFIRM%>"> </td>
                                </tr>
                                <tr>
                                    <td class="left_col">Langue* :</td>
                                    <td><%=SupportedLanguages.getSELECT_HTML("fr")%></td>
                                </tr>
                            </table>
                        </fieldset>

                        <fieldset id="verification">
                            <legend align="top">Vérification</legend>
                            <table>
                                <tr>
                                    <td class="left_col">Entrez le texte ci-dessous*</td>
                                    <td> <input type="text" required="required" 
                                                pattern="[a-zA-Z0-9]{1,8}"
                                                name="<%=UserFields.HTTP_CAPTCHA_TEXT%>" /></td>
                                </tr>
                                <tr>
                                    <td class="left_col"></td>
                                    <td><img src="CaptchaCreate" alt="Erreur Captcha"/></td>
                                </tr>
                            </table>
                            
                        </fieldset>
                        <table>
                            <tr>
                                <td class="left_col">Conditions d'utilisation</td>
                                <td><i>Veuillez prendre connaissances des conditions d'utilisation
                                 du site avant de vous enregistrer.</i></td>
                            </tr>
                            <tr>
                                <td class="left_col"></td>
                                <td><textarea rows="10"  cols="60" readonly="readonly"><%=UserFields.HTTP_DISCLAIMER_TEXT_FR%>
                                </textarea></td>
                            </tr>
                        </table>

                        <p align="center"> <button type="submit">Accepter et s'enregistrer</button>

                    </div>
                </form>
            </div>
        </div>
        <%=SiteElements.displayFooter(suscribePass)%>
        <%=GoogleAnalytics.getAnalyticsCode()%>
    </body>
</html>
