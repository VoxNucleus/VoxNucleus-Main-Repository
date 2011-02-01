$(document).ready(function(){

    /*Text replace*/
    var input_text="Partager un lien rapidement...";
    $("div#electronbar input").click(function(){
        if($(this).val()==input_text){
            $(this).val("");
        }
    });

    $(document).click(function(){
        if($("div#electronbar input").val()==""){
            $("div#electronbar input").val(input_text);
        }
    });

    $("div#footer > div#electronbar").click(function(e){
        e.stopPropagation();
    });


/* Resize Electronbar*/

    $("div#electronbar input").click(function(){
        if($(this).attr("class")=="minized"){
            $(this).removeClass("minized");
            $(this).animate({width:($(this).width()*2)}, '400', 'linear', function(){
                $(this).addClass('maximized');
                $("div#electronbar button").fadeIn();
            });
            
        }
    });

    $(document).click(function(){
        if($("div#footer input").attr("class")=="maximized"){
            $("div#footer input").removeClass("maximized");
            $("div#electronbar button").fadeOut(function(){
                $("div#footer input").animate({
                    "width":($("div#footer input").width()/2)
                }, '300', 'linear', function(){
                    $("div#footer input").addClass('minized');
                });
            });
        }
    });
});