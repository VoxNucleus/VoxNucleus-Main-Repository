/**
 *Anonymous function introduced.
 */
function fadeElements(data,textStatus){
    $('.posts').fadeOut("slow",  analyzeXML(data,textStatus));
}

/**
 *This functions begins by hiding elements, then delete then add new elements.
 */

function analyzeXML(data, textStatus){
    $('.post').remove();
    var fin = $(data).find("post").length;
    for(i=(fin-1); i>=0; i--){
        var postInfo=$(data).find("post").filter("#"+i);
        var post = constructDivPost(i,postInfo);
        insert(post);
    }
    $('.post').fadeIn(500, null);
    attachEffectHoverVote();
}

function insert(post){
    $(post).insertAfter('#listposts');
}
/**
 * Construct a div containing informations on a post
 */
function isOpinion(xmldata){
    if(xmldata.find("category").text()=="Opinion"){
        return true
    }
    else{
        return false;
    }
}
function constructDivPost(i,xmldata){
    var text_reduced= xmldata.find("description").text().substr(0, 400);
    var post='<div class="post" id="p_'+xmldata.find("key").text()+'" style="display:none;">' ;
    post = post + '<div class="rank"> <div class="nbVotes"><div class="score">' + xmldata.find("nbVotes").text() + '</div><div class=\"text_vote\">Votes</div></div>';
    post = post + '<div class="vote_zone"> <a quicklogin=\"/usermanagement/quicklogin\" rel=\"#logsub\" onclick="doVote(\''+ xmldata.find("key").text() +'\',this)">'+ "Voter" + "</a> </div> </div> ";
    post = post + '<div class="post_content" >'+ insertImageText(xmldata);
    post = post + '<a  href="/post/'+ xmldata.find("key").text() +'">' + xmldata.find("title").text() +"</a>" + "<br>";
    post = post + '<div class=\"description\">'+ insertDescription(xmldata,text_reduced) + "</div>";
    post = post +"</div>";
    post = post + '<div class ="bottom" > <div class="author"> <a href="/user/'+
    xmldata.find("author").text() +'">'+ xmldata.find("author").text() +
    '</a></div> <div class="comments">'+ xmldata.find("nbComments").text()+ ' commentaires </div> </div>'
    post= post  + "</div> ";
    return post;
}
/*insert inside description div*/
function insertDescription(xmldata,text_reduced){
    var text_descript_with_link= "<a href=\"/post/"+xmldata.find("key").text()+"\" >";
    text_descript_with_link+=insertURLShortened(xmldata);
    text_descript_with_link+=text_reduced;
    text_descript_with_link+="</a>";
    return text_descript_with_link;
}
function insertURLShortened(xmldata){
    if(!isOpinion(xmldata)){
        var shortenedPattern= new RegExp("^http://[^/]+","m");
        var shortenedURL="<span class=\"short_url\">"+(shortenedPattern.exec(xmldata.find("link").text())[0]).substring(7);
        shortenedURL+="</span>";
        return shortenedURL;
    }
    else
        return "";
}
/* Image Zone */
function insertImageText(xmldata){
    var imageZone='<a href="/r/'+xmldata.find("key").text() +'"';
    if(xmldata.find("category").text()=="Opinion"){
        imageZone=imageZone+'>';
        imageZone=imageZone+'<img src="/image/website/post/imagepostdefault.png" alt="image_default" />';
    }else{
        imageZone=imageZone+' target="_blank" >';
        imageZone=imageZone+'<img src="/image/post/'+xmldata.find("key").text() +'/'+
        xmldata.find("fileName").text()+'"/>';
    }
    imageZone=imageZone+'</a>';
    return imageZone;
}

function fillInfos(i,xmldata){
    (jQuery(document).find('#descriptionpost'+i)).text(xmldata.find("description").text());
}

function failLoadingPosts(functionname){
    if($("div#error_box").length<1){
        $("#content").prepend('<div id=\"error_box\" function=\"'+functionname+'\"> Erreur dans le chargement, cliquez ici pour relancer </div>');
        $("div#error_box").bind('click',function(){
            $(this).fadeOut('slow', function(){
                var funcCall=$(this).attr("function")+"()";
                eval(funcCall);
                $(this).remove();
            });
        });
    }
}
