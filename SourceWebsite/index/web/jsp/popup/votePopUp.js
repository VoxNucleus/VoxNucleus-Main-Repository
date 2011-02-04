$(document).ready(function(){
    window.resizeTo('810', '500');

    $("#cancel").click(function(){
        self.close();
    });
});

function doVote(idPost){
    var username=$.cookies.get("username");
    var options={
        type: 'POST',
        url:'/request/vote',
        datatype:'xml',
        data:{
            'username':username,
            'postId':idPost
        },
        success:function(data, textStatus, XMLHttpRequest){
            analyseResultPost(data);
        },
        error:function(xhr,textStatus, errorThrown){
            errorResult(textStatus);
        }
    };
    $.ajax(options);
}
function errorResult(textStatus){
    changeMessage("Une erreur s'est produite !");
}
//The request will be in XML
function analyseResultPost(data){
    var response_status =$(data).find("status").text();
    if(response_status=="ok"){
        changeMessage("Vote fait !","green");
    }
    if(response_status=="fail"){
        changeMessage("Une erreur s'est produite !");
    }
    if(response_status=="already"){
        changeMessage("Déjà voté !");
    }
    if(response_status=="unknown"){
        changeMessage("Une erreur inconnu s'est produite !");
    }
    if(response_status=="error"){
        changeMessage("Une erreur s'est produite.");
    }
}
function changeMessage(message,color){
    $("#vote_zone").css("height",$("#vote_zone").height() );
    $("#vote_zone").children().fadeOut('slow', function(color){
        $("#vote_zone").css("font-size", "170%");
        $("#vote_zone").css("font-weight", "bolder");
        $("#vote_zone").css("text-align", "center");
        $("#vote_zone").html(message).fadeIn("slow");
        
    });
}
/* Slide down subscribe zone */

$(document).ready(function(){
    $("#subscribe_click").click(function(){
        $("#subscribe").slideDown('slow',function(){
            $("#subscribe_click").css("cursor", "auto");
        });
    });
});

$(document).ready(function(){
    $("#quick_subscribe_form").validator({
        lang:'fr',
        offset:[0,5]
    });
});
$(document).ready(function(){
    $("#login_form").validator({
        lang:'fr',
        offset:[0,5]
    });
});