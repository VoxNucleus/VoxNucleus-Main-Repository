$(document).ready(function(){
    $("form select#category_select").show().change(function(){
        var selection=$(this).val();
        if(selection=="Opinion"){
            $("select.sub_category").fadeOut();
            $("form > table").show();
            $("tr#link").fadeOut();
        }else{
            $("form select.sub_category").hide();
            $("form select#sub_categories_"+selection).show();
        }
    });
    $("form select.sub_category").change(function(){
        $("form > table").fadeIn('fast',function(){
            $("tr#link").fadeIn("fast");
        });
    });
});
$(document).ready(function(){
    $("#newpost_form").validator({
        'lang':'fr',
        offset:[0,5]
    });
});
$(document).ready(function(){
    $("textarea").autoResize({
        onResize:function(){
            $(this).css({
                opacity:0.8
            });
        },
        animateCallback:function(){
            $(this).css({
                opacity:1
            });
        },
        animate:false
    }).trigger("change");

});
/* Title changer */
$(document).ready(function(){
    changeTitle(document,$("#input_title").val());
    $("#input_title").bind("keypress change",function(){
        changeTitle(document,$(this).val());
    });
   
});
function changeTitle(doc, val){
    doc.title= "VoxNucleus : Créer un nouveau noyau - "+val;
}
/* Verify that URL i */
$(document).ready(function(){
    if($("#input_link").val().length>0){
        verifyURL($("#input_link"));
    }
});
function verifyURL(context){
    $.ajax({
        url:"/verification/verifyurl",
        type:"POST",
        data:({
            fields:"{\"url\""+":\""+$(context).val()+"\"}"
        }),
        success:function(data){
            var response =$.parseJSON(data);
            analyzeResponse(response);
        },
        error:function(error){
            removeListPostByURL()
        }
    });
}
$(document).ready(function(){
    $("#input_link").change(function(){
        verifyURL(this);
    });
});

function analyzeResponse(responseObject){
    if(responseObject.res.length>0){
        insertListPostByURL(responseObject);
    }else{
        removeListPostByURL();
    }
}
function insertListPostByURL(responseObject){
    $("div.error").hide();
    var list_post_same_url=
    ' Le lien que vous allez poster a déjà été posté avec d\'autres noyaux. '+
    'Voici la liste des ' + responseObject.res.length +' derniers :';
    list_post_same_url+=' <ul class="list_post_by_url" >';
    for(var index=0; index< responseObject.res.length;index++){
        list_post_same_url+=' <li>';
        list_post_same_url+=' <a href=\"/post/'+ responseObject.res[index].postId +'\"> ';
        list_post_same_url+=responseObject.res[index].title +' </a>';
        list_post_same_url+=' </li>';
    }
    list_post_same_url+=' </ul>';
    list_post_same_url+='Toutefois rien ne vous empêche de continuer.';
    if($("#post_by_url").length>0){
        $("#post_by_url").html(list_post_same_url);
        
    }else{
        var div_post_by_url='<div id="post_by_url" style="display:none">'+
            list_post_same_url+'</div>';
        $("#newpost_form").before(div_post_by_url);
        $("#post_by_url").slideDown('fast');
    }
}

function removeListPostByURL(){
    $("div.error").hide();
    $("#post_by_url").slideUp('fast', function(){
        $(this).remove();
    });
}

/* */
$(document).ready(function(){
    $("#disclaimer").backgroundevolve({duration:1500,end_color:'#fd6f6f'});
});

/* Autocomplete tags */
$(document).ready(function(){
    $("#input_tags").autocomplete({
        minLength:3,
        appendTo:"#newpost_form",
        source:function(request,response){
            $.ajax({
                url:"/verification/autocomplete/tags",
                dataType:"json",
                data:{
                    autocomplete_text:request.term
                },
                success:function(data){
                    response($.map(data.term_list, function(item){
                        return {
                            label:item,
                            value:item
                        }
                    }));
                }
            });
        },create:function(event,ui){
            $(ui).addClass("tags-autocomplete");
        },
        open:function(){
            $(this).removeClass("ui-corner-all").addClass("ui-corner-top");
        },
        close:function(){
            $(this).removeClass("ui-corner-top").addClass("ui-corner-all");
        }
    });
});