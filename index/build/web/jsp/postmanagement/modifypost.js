/*Validator for modification */
$(document).ready(function(){
    $("#modification_form").validator({
        'lang':'fr',
        offset:[0,5]
    });
});
/* auto expand*/
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