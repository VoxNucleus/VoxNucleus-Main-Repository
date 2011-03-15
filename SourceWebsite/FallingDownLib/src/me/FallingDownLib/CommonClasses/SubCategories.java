package me.FallingDownLib.CommonClasses;

/**
 *
 * @author victork
 */
public class SubCategories {

    public static final String[][] SUB_CATEGORIES = {
        {"Tout"},
        {"Tout", "PC", "Mac", "Jeux-vidéos", "Mobile", "Linux", "Autres"},
        {"Tout", "Politique", "Argent", "International", "Culture", "Autres"},
        {"Tout", "Espace", "Physique", "Medecine", "Biologie", "Autres"},
        {"Tout", "Football", "Rugby", "Basket", "Tennis", "Automobile", "Autres"},
        {"Tout"},
        {"Tout", "Pas sûr au travail", "Humour", "Jeux", "Inclassable", "VoxNucleus"}};
    /**
     * Not implemented
     */
    public static final String[][] SUB_CATEGORIES_LINK = {
        {"Tout"},
        {"Tout", "PC", "Mac", "Jeux-videos", "Mobile", "Linux", "Autres"},
        {"Tout", "Politique", "Argent", "International", "Culture", "Autres"},
        {"Tout", "Espace", "Physique", "Medecine", "Biologie", "Autres"},
        {"Tout", "Football", "Rugby", "Basket", "Tennis", "Automobile", "Autres"},
        {"Tout"},
        {"Tout", "Pas sûr au travail", "Humour", "Jeux", "Inclassable", "VoxNucleus"}};



    public static final String[] SUB_CATEGORY_EXPLANATIONS_ALL = {"Cette page regroupe"
        + " tous les noyaux de toutes les catégories."};
    public static final String[] SUB_CATEGORY_EXPLANATIONS_TECHNOLOGY = {"Cette page "
        + "regroupe tous les noyaux de la catégorie Technologie.",
        "Toute l'actualité du PC et des technologies associées.",
        "Toutes les actualités importantes sur la firme de la pomme : iPod, iMac, MacBook,... ",
        "Toutes les actualités intéressantes relatives aux jeux-vidéos.",
        "L'actualité des systèmes mobiles iOS, Android, BlackBerry, Windows Mobile,...",
        "Des nouvelles sur le monde du libre et de Linux.",
        "Retrouvez ici tout ce qui a rapport aux nouvelles technologies "
        + "et qui n'a pas pu être inséré dans les autres sous-catégories"};
    public static final String[] SUB_CATEGORY_EXPLANATIONS_NEWS = {
        "Retrouvez dans cette partie l'intégralité les actualités récentes,"
                + " postées dans toutes les sous catégories",
        "L'actualités de la politique intérieure. Suivez en temps réel "
                + "les actions de la droite et de la gauche",
        "Suivez les actualités sur la bourse, les monnaies et tout ce qui a rapport "
                + "avec la finance en général.",
        "L'actualité internationale : les relations entre les pays, tensions dans"
                + " le monde résumées ici.",
        "Tout sur la culture : cinéma, livres, théâtre.",
        "L'actualité qui ne peut pas être classé dans aucune sous-catégorie."};
    public static final String[] SUB_CATEGORY_EXPLANATIONS_SCIENCES = {
        "Tous les noyaux créés dans la partie science.",
        "Espace : L'actualité des étoiles et de l'infini. Retrouvez ici les découvertes "
                + "de nouvelles planètes et des merveilles de l'univers.",
        "L'actualité des sciences physiques, suivez les découvertes.",
        "Les avancées de la medecine qui sauveront votre vie aujourd'hui et peut "
                + "être demain.",
        "Les sciences du vivant : les découvertes de la biologie, le monde du vivant.",
        "L'inclassable dans les sciences. Oui ça existe, et c'est ici..."};
    public static final String[] SUB_CATEGORY_EXPLANATIONS_SPORT = {
        "Toute l'actualité sportive du moment. Sachez en direct qui a gagné ou qui a perdu.",
        "Tout l'actualité du ballon rond : les transferts, les défaites et les "
                + "victoires de vos équipes et joueurs préférés.",
        "Le monde de l'ovalie de long en large.",
        "Basket",
        "Roland Garros, Wimbledon et tout ce qui compte dans le tennis.",
        "Formule 1, Moto GP, ... toute l'actualité des sports automobiles.",
        "L'actualité de tous les sports non listés plus haut."};
    public static final String[] SUB_CATEGORY_EXPLANATIONS_OPINION = {"Les avis"
            + " de nos utilisateurs venez dire ce que vous pensez et comparez "
            + "le à ce que le reste du monde dit."};
    public static final String[] SUB_CATEGORY_EXPLANATIONS_OTHER = {"Tout",
        "Attention cette section contient des images qui pourraient être "
        + "choquantes. Faites attention que votre patron ne regarde pas par "
        + "dessus votre épaule...",
        "Venez ici pour rire un petit peu..",
        "Le classement des meilleurs jeux que vous pouvez trouver sur internets. "
        + "Jeux flash, silverlight ou autre, bienvenue dans cette section !",
        "Tous les noyaux tellement inclassables que l'on ne peut les mettre "
        + "dans aucune autre section. Attention à ce que vous pourrez trouver ici...",
        "Vous retrouverez l'ensemble des informations relatives au site en lui "
        + "même. Vous pouvez proposer des fonctions et les voir "};

    /**
     * Map one subCategory to an explanation
     */

    public static final String[][] SUB_CATEGORIES_EXPLANATIONS = {
        SUB_CATEGORY_EXPLANATIONS_ALL,
        SUB_CATEGORY_EXPLANATIONS_TECHNOLOGY,
        SUB_CATEGORY_EXPLANATIONS_NEWS,
        SUB_CATEGORY_EXPLANATIONS_SCIENCES,
        SUB_CATEGORY_EXPLANATIONS_SPORT,
        SUB_CATEGORY_EXPLANATIONS_OPINION,
        SUB_CATEGORY_EXPLANATIONS_OTHER};


    /**
     *
     * @param category
     * @return return select for the considered subcategory
     */
    public static String getSELECT_HTML(int category) {
        if (SUB_CATEGORIES[category].length > 1) {
            StringBuilder builder = new StringBuilder();
            builder.append("<select id=\"sub_categories_").append(Categories.MAIN_CATEGORIES[category]).append("\" name=\"sub_categories_").append(Categories.MAIN_CATEGORIES[category]).append("\" class=\"sub_category\">");
            builder.append("<option value=\"\">Choisissez...</option>");
            for (int index = 1; index < SUB_CATEGORIES[category].length; index++) {
                builder.append("<option name=\"").append(SUB_CATEGORIES[category][index]).append("\">").append(SUB_CATEGORIES[category][index]).append("</option>");
            }
            builder.append("");
            builder.append("</select>");
            return builder.toString();
        } else {
            return "";
        }
    }

    /**
     *
     * @param category
     * @param subcat
     * @return the position of the sub_category
     */


    public static int getSubcategory(int category,String subcat){
        
        for(int index_subcategory=0; index_subcategory< SUB_CATEGORIES[category].length;index_subcategory++){
            if(SUB_CATEGORIES[category][index_subcategory].equals(subcat))
                return index_subcategory;
        }
        return -1;

    }

    /**
     *
     * @param category
     * @param subcategory
     * @return true if the combination is a valid pair, false if it is not
     */

    public static boolean isAValidPair(String category,String subcategory){
        if(category.equals(Categories.MAIN_CATEGORIES[Categories.CATEGORY_OPINION]) || category.equals(Categories.MAIN_CATEGORIES[Categories.CATEGORY_RANDOM]) ){
            return true;
        }else{
            int category_position = Categories.getCategory(category);
            if(category_position>0){
                for(int index_subcategory=1;index_subcategory<SUB_CATEGORIES[category_position].length;index_subcategory++)
                    if(SUB_CATEGORIES[category_position][index_subcategory].equals(subcategory))
                        return true;
            return false;
            }else{
                return false;
            }
        }

    }
}
