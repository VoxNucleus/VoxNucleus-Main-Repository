package me.FallingDownLib.CommonClasses.www.boxes;

import me.FallingDownLib.CommonClasses.www.post.PrintMiniListPost;
import me.FallingDownLib.functions.similarpost.RetrieveSimilarPosts;
import me.FallingDownLib.interfaces.www.ToCodeConverter;

/**
 * Class that 
 * @author victork
 */
public class SimilarPostsBox implements ToCodeConverter {
    StringBuilder similarPost_builder;
    String postId;


    private SimilarPostsBox(){
        similarPost_builder = new StringBuilder();
    }
    
    private SimilarPostsBox(String asked_PostId){
        this();
        postId = asked_PostId;
    }

    public static SimilarPostsBox getInstance(String asked_postId){
        return (new SimilarPostsBox(asked_postId));
    }


    /**
     * This method builds the boxs around;
     */
    private void buildBox() {
        RetrieveSimilarPosts similarRetriever = RetrieveSimilarPosts.getInstance(postId, 3);
        similarPost_builder.append("<div id=\"similar_box\" class=\"lateral_box\">");
        similarPost_builder.append("Noyaux similaires :");
        try {
            
            PrintMiniListPost miniPosts = PrintMiniListPost.getInstance(similarRetriever.getKeys());
            similarPost_builder.append(miniPosts.getHTMLCode());
        } catch (Exception ex) {
            ex.toString();
            //TODO If exception de solR
        }
        similarPost_builder.append("</div>");
    }






    public String getHTMLCode() {
        buildBox();
        return similarPost_builder.toString();
        
    }

}
