/**
 *Thank you for this plugin all credit to :
* hoverIntent r5 // 2007.03.27 // jQuery 1.1.2+
* <http://cherne.net/brian/resources/jquery.hoverIntent.html>
*/
(function($){$.fn.hoverIntent=function(f,g){var cfg={sensitivity:7,interval:100,timeout:0};cfg=$.extend(cfg,g?{over:f,out:g}:f);var cX,cY,pX,pY;var track=function(ev){cX=ev.pageX;cY=ev.pageY;};var compare=function(ev,ob){ob.hoverIntent_t=clearTimeout(ob.hoverIntent_t);if((Math.abs(pX-cX)+Math.abs(pY-cY))<cfg.sensitivity){$(ob).unbind("mousemove",track);ob.hoverIntent_s=1;return cfg.over.apply(ob,[ev]);}else{pX=cX;pY=cY;ob.hoverIntent_t=setTimeout(function(){compare(ev,ob);},cfg.interval);}};var delay=function(ev,ob){ob.hoverIntent_t=clearTimeout(ob.hoverIntent_t);ob.hoverIntent_s=0;return cfg.out.apply(ob,[ev]);};var handleHover=function(e){var p=(e.type=="mouseover"?e.fromElement:e.toElement)||e.relatedTarget;while(p&&p!=this){try{p=p.parentNode;}catch(e){p=this;}}if(p==this){return false;}var ev=jQuery.extend({},e);var ob=this;if(ob.hoverIntent_t){ob.hoverIntent_t=clearTimeout(ob.hoverIntent_t);}if(e.type=="mouseover"){pX=ev.pageX;pY=ev.pageY;$(ob).bind("mousemove",track);if(ob.hoverIntent_s!=1){ob.hoverIntent_t=setTimeout(function(){compare(ev,ob);},cfg.interval);}}else{$(ob).unbind("mousemove",track);if(ob.hoverIntent_s==1){ob.hoverIntent_t=setTimeout(function(){delay(ev,ob);},cfg.timeout);}}};return this.mouseover(handleHover).mouseout(handleHover);};})(jQuery);
/*Validator stuff */
$.tools.validator.localize("fr",{
    '*':'Corriger cette valeur',
    'name':'Utilisateur non correct',
    ':email':'email non valide',
    ':url':'Adresse non valide',
    '[required]':'Champ obligatoire',
    '[data-equals]':'N\'est pas égal à $1'

});
$.tools.validator.fn("[data-equals]",function(input){
    var name=input.attr("data-equals"),
    field=this.getInputs().filter("[name="+name+"]");
    return input.val() == field.val() ? true : [name];
});

$.tools.validator.fn("[name=username]",
{
    "en":"Username already taken",
    "fr":"Utilisateur existant"
},
function(input, value){
    var result =false;
    $.ajax({
        url:"/verification/verifyuser",
        type:"POST",
        dataType:"json",
        async:false,
        data:({
            fields:"{\"username\""+":\""+value+"\"}"
        }),
        success:function(data){
            result =(data==null)? true:data.isUsernameCorrect;
        },
        error:function(error){
            result=true;
        }
    });
    return result;
});
/* Load an overlay for all the object with class .overlay_lauch*/
$(document).ready(function(){
   $(".overlay_launch").click(function(){
       load_overlay(this);
   });
});
/* Slow slowly evolving background
* List of parameters
*
* */
(function ($) {
   $.fn.backgroundevolve = function(in_options){
       var defaults={
           duration:5000,
           start_color:'#ffffff',
           end_color:'#000000'
       };

        var options = $.extend(defaults, in_options);
        return this.each(function(){
            function changecolor(pointer,startC,endC){
                $(pointer).animate({
                    backgroundColor:endC
                }, options.duration, 'linear', function(){
                    changecolor(pointer,endC,startC);
                    });
            }
            changecolor(this,options.start_color,options.end_color);
           

        });
   };
})(jQuery);
/* Get Help */
$(document).ready(function(){
   $("table  tr").hover(function(){
       $(this).find("span.get_help").fadeIn('fast');
   }, function(){
       $(this).find("span.get_help").fadeOut('fast');
   });
});

/*Display tooltip*/
$(document).ready(function(){
    $(".get_help").tooltip({
        position:'top center',
        effect:'fade',
        relative:true
    });
});
/* auto complete for search with jquery ui plugin */
$(document).ready(function(){
    $("#searchfield > .text_box").autocomplete({
        minLength:2,
        appendTo:"#logo_menu",
        source:function(request,response){
            if($("#searchfield > .text_box").val()!="Rechercher..."){
            $.ajax({
                url:"/verification/autocomplete/search",
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
            }
        },
        open:function(){
            $(this).removeClass("ui-corner-all").addClass("ui-corner-top");
        },
        close:function(){
            $(this).removeClass("ui-corner-top").addClass("ui-corner-all");
        }
    });
});