package me.voxnucleus.www.helpers;

import me.FallingDownLib.CommonClasses.PostFields;

/**
 *
 * @author victork
 */
public class NucleusCreationHelper extends Helper{

    protected NucleusCreationHelper(){
        super();
        super.addHelp(PostFields.HTTP_CATEGORY, "Choisissez la catégorie de votre noyau.");
        super.addHelp(PostFields.HTTP_SUB_CATEGORY, "Choisissez la sous-catégorie.");
        super.addHelp(PostFields.HTTP_LINK, "Lien vers lequel pointe ce que vous "
                + "voulez faire partager. Ce doit être une adresse valide.");
        super.addHelp(PostFields.HTTP_TITLE, "Titre que vous souhaitez donner à votre "
                + "noyau. Il sera visible par tous, alors choisissez le bien.");
        super.addHelp(PostFields.HTTP_LANGUAGE, "Choisissez la langue de la page que "
                + "vous proposez ou simplement du texte que vous écrivez");
        super.addHelp(PostFields.HTTP_DESCRIPTION, "Donner une description plus "
                + "ou moins longue de ce que vous souhaitez dire.");
        super.addHelp(PostFields.HTTP_TAGS, "Mots clefs que vous pouvez associer à votre "
                + "noyau. Plus il y en a, plus les gens viendront.");
    }

    public static NucleusCreationHelper getHelper(){
        return new NucleusCreationHelper();
    }

}
