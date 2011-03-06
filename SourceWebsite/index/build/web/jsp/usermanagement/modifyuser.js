$(document).ready(function(){
        $("textarea").autoResize({
        onResize:function(){
            $(this).css({opacity:0.8});
        },
        animateCallback:function(){
            $(this).css({opacity:1});
        },
        animate:false
    }).trigger("change");

});