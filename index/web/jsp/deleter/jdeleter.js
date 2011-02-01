function commentDeleter(postId, commentUUID){
    var options={
        type: 'POST',
        url:'/administration/deletecomment',
        dataType:'xml',
        data:{
            'postId':postId,
            'uuid':commentUUID
        },
        error: function(xhr,textStatus, errorThrown){
        },
        success:function(data,textStatus){
            analyzeDeleteCommentResponse(data,textStatus);
        }
    };
    $.ajax(options);
}

function postDeleter(postId){
    var options={
        type: 'POST',
        url:'/administration/deletepost',
        dataType:'xml',
        data:{
            'postId':postId
        },
        error: function(xhr,textStatus, errorThrown){
        },
        success:function(data,textStatus){
            analyzeDeletePostResponse(data,textStatus);
        }
    };
    $.ajax(options);
}

function addDeleteOption(){
    $(this).append("<div style=\"display:none;\" class=\"comment_del_overlay\">Supprimer votre " +
        "commentaire ? <span class=\"yes\"> Oui</span> / <span class=\"no\"> Non </span></div>");
    $(".comment_del_overlay").slideDown("fast");
    $("div.comment_del_overlay > span.yes").click(function(){
        commentDeleter($("div.post_content").attr("id"),$(this).parent().parent().attr("id"));
    });
    $("div.comment_del_overlay > span.no").click(function(){
        $(this).parent().fadeOut('fast', function(){
            $(this).remove();
        });
    });
}

function removeDeleteOption(){
    $(this).find("div.comment_del_overlay").slideUp("fast", function(){
        $(this).remove();
    })
}