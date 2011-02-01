$(document).ready(function(){
    $("div#javascript_error").remove();
});


function retrieveComments(idPost){
    var begin=$("#lastpost").text();
    var options={
        type: 'POST',
        url:'/request/getcommentbydate',
        datatype:"xml",
        data:{
            'begin':begin,
            'end':parseInt(begin)+20,
            'postId':idPost
        },
        success:function(data){
            var xml;
            /* Fuck you IE */
            if($.browser.msie){
                xml=IE_XML(data)
            }else{
                xml=data;
            }
            analyseResult(xml);
            unbind_actions();
            bind_actions();
        },
        error:function(xhr,textStatus, errorThrown){
            errorCommentResult(textStatus, errorThrown);
        }
    };
    $.ajax(options);
}

/**
 * Called when an error has occured
 */
function errorCommentResult(textStatus, errorThrown){
    
}
/**
 * This function analyse data.
 */

function analyseResult(data){
    var nbComments = $(data).find("comment").length;
    if(nbComments==0){
        displayNoComments();
    }
    else{
        displayComments(data);
    }
}

/**
 * Called when no comments is found
 */

function displayNoComments(){
    var nbComment = $(".comment").length + $(".nocomment").length;
    if(nbComment == 0){
        var noComment = '<div class="nocomment" > Pas de commentaire pour l\'instant </div>';
        $(noComment).insertAfter("#lastpost");

    }
}

function displayComments(data){
    $(".nocomment").remove();
    var nbComments = $(data).find("comment").length;
    var i;
    for(i=0; i<nbComments; i++){
        var commentInfo=$(data).find("comment").filter("#"+i);
        var comment = constructDivComment(i,commentInfo);
        insert(comment);
    }
    $('#lastpost').text( parseInt($('#lastpost').text())+parseInt(nbComments));
}

function insert(comment){
    $(comment).insertAfter('#lastpost');
    
}

function constructDivComment(i, commentInfo){
    
    var comment_code;
    if($.cookies.get('username')==commentInfo.find("author").text())
        comment_code=comment_code+'<div class="comment mycomment" id="u_'+  commentInfo.find("uuid").text() +'" >';
    else
        comment_code= comment_code+'<div class="comment" id="u_'+  commentInfo.find("uuid").text() +'" >';

    comment_code=comment_code+build_comment_head(commentInfo);
    comment_code = comment_code+ '<div class=\"corpse_comment\">';
    comment_code = comment_code + '<div class=\"title_comment\">'+commentInfo.find("title").text()+'</div>';
    comment_code= comment_code+commentInfo.find("text").text()+'</div>';
    comment_code = comment_code+ '<div class=\"bottom_comment\">' + formatDate(commentInfo) + '</div>';
    comment_code = comment_code+'</div>';
    return comment_code;
}
/* Build the comment head : author + vote*/
function build_comment_head(commentInfo){
    var head_code = '<div class=\"top_comment\">';
    head_code = head_code + '<span class=\"comment_author\">'+ commentInfo.find("author").text() + '</span>';
    head_code = head_code + '<span class=\"comment_vote plus_vote\">+</span>';
    head_code = head_code + '<span class=\"comment_vote minus_vote\">-</span>';
    head_code = head_code +'</div>';
    return head_code;
}
/* Build date */
function formatDate(commentInfo){
    var comment_Date =new Date(parseInt(commentInfo.find("timestamp").text()));
    var yyyy = comment_Date.getFullYear();
    var dd= comment_Date.getDate();
    var mm= comment_Date.getMonth();
    var hh = comment_Date.getHours();
    var min= comment_Date.getMinutes();
    var ss = comment_Date.getSeconds();
    return "<span class=\"date_comment\" >" +dd+"/"+mm+"/"+ yyyy+" à "+ hh+":" +min+" et "+ ss+" secondes </span>";
}
/* Bind the action to the comments */
function bind_actions(){
    $("div.mycomment").each(function(){
        $(this).hoverIntent({
            over:addDeleteOption,
            timeout:500,
            out:removeDeleteOption
        });
    /*
        $(this).hover(function(){
            addDeleteOption(this);
        }, function(){
            removeDeleteOption(this);
        });*/
    /*commentDeleter($("div.post_content").attr("id"),$(this).parent().attr("id"));*/
    });
    $("span.comment_vote").vote_comment();
}

/*To kick off hover*/
function unbind_actions(){
    $("div.mycomment").unbind('mouseenter mouseleave click');
}


/*For IE browser only*/
function IE_XML( data){
    var xml;
    xml = new ActiveXObject("Microsoft.XMLDOM");
    xml.async=false;
    xml.loadXML(data.xml);
    return xml;
}


