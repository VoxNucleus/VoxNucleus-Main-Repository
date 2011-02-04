function doVote(idPost,context){
    var username=$.cookies.get("username");
    var options={
        type: 'POST',
        url:'/request/vote',
        datatype:'xml',
        data:{
            username:username,
            vote:1,
            postId:idPost
        },
        success:function(data, textStatus, XMLHttpRequest){
            analyseResultPost(data,idPost,context);
        },
        error:function(xhr,textStatus, errorThrown){
            errorResult(textStatus, errorThrown,context);
        }
    };
    $.ajax(options);
}

//The request will be in XML

function analyseResultPost(data,idPost,context){
    var response_status =$(data).find("status").text();
    if(response_status=="ok"){
        addOneToVote(context);
        animatepopup(idPost,1,context);
    }
    if(response_status=="fail"){
        animatepopup(idPost,2,context);
    }
    if(response_status=="already"){
        animatepopup(idPost,3,context);
    }
    if(response_status=="unknown"){
        animatepopup(idPost,4,context);
    }
    if(response_status=="error"){
        animatepopup(idPost,5,context);
    }
}
/* Animation of the popup */
function animatepopup(idPost,code,context){
    
    if(code==1){
        $(context).parents(".rank").append("<div class='success popup'>Vote fait</div>");
    }
    if(code==3){
        $(context).parents(".rank").append("<div class='already popup'>Déjà voté</div>");
    }
    if(code==4){
        load_overlay(context);
    }
    if(code==2 || code==5){
        $(context).parents(".rank").append("<div class='error popup'>Erreur</div>");
    }
    $(".popup").animate({
        opacity: "show",
        bottom: "0"
    }, "slow");
    setTimeout("animatepopupout()",5000);
}
/* Animate the created popup */
function animatepopupout(){
    $(".popup").animate({
        opacity: "hide",
        top: "-10"
    }, "fast");
    setTimeout("destroypopup()",1000);
}


/* Remove the popup */

function destroypopup(){
    $(".popup").remove();
}

/* Add one to the total vote */
function addOneToVote(context){
    var scoreContext=$(context).parent().parent().find("div#nbVotes");
    $(scoreContext).attr("nbvotes",parseInt($(scoreContext).attr("nbvotes"))+1);
}


function errorResult(data, errorThrown){
    //Todo
}

$(document).ready(function(){
    attachEffectHoverVote();
});

/* */
function attachEffectHoverVote(){
    $("div#vote_zone").hover(function(){
        var nbVote=$(this).parent().find("div#nbVotes > div.score").html();
        $(this).parent().find("div#nbVotes").attr("nbvotes", nbVote);
        $(this).parent().find("div#nbVotes > div.score").fadeOut('100', function(){
            $(this).html("+1");
            $(this).fadeIn('fast');
        });
        
    }, function(){
        var nbVote=$(this).parent().find("div#nbVotes").attr("nbvotes");
        $(this).parent().find("div#nbVotes > div.score").remove();
        $(this).parent().find("div#nbVotes").prepend("<div class=\"score\">"+nbVote+"</div>").find('div.score').hide();
        $(this).parent().find("div#nbVotes > div.score").fadeIn('slow');
    });
}

/* Comment vote */
(function ($) {
    $.fn.vote_comment = function(){
        return this.each(function(){
            $(this).click(function(){
                do_vote_comment(this);
            });

        });
    };
})(jQuery);

function do_vote_comment(context){
    var score_update;
    ($(context).hasClass("plus_vote"))?score_update=1:score_update=-1;
    var c_id=$(context).parent().parent().attr("id").substring(2);
    var p_id=$(document).find(".post_content").attr("id");
        var options_vote_comment={
        type: 'POST',
        url:'/request/vote/comment',
        datatype:'json',
        data:{
            comment_id:c_id,
            post_id:p_id,
            add_to_score:score_update
        },
        success:function(data, textStatus, XMLHttpRequest){
            analyse_result_vote_comment(data);
        },
        error:function(xhr,textStatus, errorThrown){
            ;
        }
    };
    $.ajax(options_vote_comment);
}

$(document).ready(function(){
    $(".comment_vote").vote_comment();
});

function analyse_result_vote_comment(data){
    (data.is_vote_success==true)?display_success_vote_comment(data):display_error_vote_comment(data);
    
}

function display_success_vote_comment(data){
    var success_message="Vote réussi !";
    alert(success_message);
}
function display_error_vote_comment(data){
    var error_message="Erreur - ";
    error_message= error_message+data.additional_comment;
    alert(error_message);
}