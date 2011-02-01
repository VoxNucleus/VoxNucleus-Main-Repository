$(document).ready(function(){
   bindExplanationDown();
   

});

function bindExplanationUp(){
    $("div#explanation_expand").bind('click',function(){
       $("div#explanation_text").slideUp("fast", function(){
          $(this).removeClass("expanded");
       });
   });
   
}
function bindExplanationDown(){
    $("div#explanation_box > div.explanation_expand").bind('click',function(){
       $("div#explanation_text").slideDown('fast', function(){
           $(this).addClass("expanded");
           $("div#explanation_box > div.explanation_expand").unbind('click');
            bindExplanationUp();
       });
   });
}