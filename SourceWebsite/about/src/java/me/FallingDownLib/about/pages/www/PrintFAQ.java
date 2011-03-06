package me.FallingDownLib.about.pages.www;

import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 *
 * @author victork
 */
public class PrintFAQ implements ToCodeConverter {

    private StringBuilder faq_builder;

    /**
     * Default constructor
     */
    protected PrintFAQ(){
        faq_builder = new StringBuilder();
    }

    /**
     *
     * @return an instance
     */
    public static PrintFAQ getInstance(){
        return new PrintFAQ();
    }

    public String getHTMLCode() {
        buildPage();
        return faq_builder.toString();
    }

    private void buildPage() {

        insertQuestionResponse(1,"Est ce que les boutons \"Tweet this\" ou \"Recommend this\" de"
                + " Twitter/Facebook vont être intégrées ?",
                "Non, je n'insèrerai pas ces boutons sur mon site. La raison est que "
                + "grâce à ce système Facebook et VoxNucleus peuvent épier vos moindres "
                + "faits et geste. J'ai fait ce site en croyant en des principes simples : "
                + "garder une liberté de parole et votre vie privée. Je préfère que mon site "
                + "meurre sans visiteur plutôt que de violer ce principe.");
        insertQuestionResponse(2,"Pourquoi le site est-il si rapide ?","La vitesse "
                + "du site vient des technologies que j'utilise. Si vous êtes "
                + "intéressés par les détails, n'hésitez pas à suivre ce lien.");
        insertQuestionResponse(3,"Pourquoi le site est-il si lent ?","La lenteur du site "
                + "vient des technologies utilisées. C'est uniquement leur faute,"
                + " je le jure ! :-)");
        insertQuestionResponse(4,"Le nombre de catégories est-il fixé ?","Pour "
                + "l'instant oui mais cela sera amené à changer. C'est un projet "
                + "que je ferai lorsque j'aurai le temps et que les technologies "
                + "seront disponibles !");
        insertQuestionResponse(5,"Le site sera-t-il mis en open-source ?","La "
                + "réponse est oui, ce sera fait dans les mois qui arrivent. Pour "
                + "l'instant le développement se concentre sur le fonctionnel "
                + "plutôt que sur la plastique du code.");
    }

    private void insertQuestionResponse(int number, String question,String answer){
        faq_builder.append("<div id=\"question_").append(number).append("\">");
        faq_builder.append(insertQuestion(number,question));
        faq_builder.append(insertResponse(answer));
        faq_builder.append("</div>");
    }

    private String insertQuestion(int number, String question){
        return "<h3> #"+number+" " + question + "</h3>";
    }

    private String insertResponse(String answser){
        return "<div class=\"answer\">"+answser+"</div>";
    }

}
