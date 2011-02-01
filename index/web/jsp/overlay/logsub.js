function load_overlay(context){
    $(context).overlay({
        mask:"#fff",
        onBeforeLoad:function(){
            var wrap=this.getOverlay().find(".contentWrap");
            wrap.load(this.getTrigger().attr("quicklogin"));
        },
        onLoad:function(){
            $("div.error").hide();
            $("#quick_subscribe_form").validator({
                'lang':'fr',
                offset:[0,5]
            });
            $("#quick_login_form").validator({
                lang:'fr',
                position:'top left'

            });
            
        },
        onBeforeClose:function(){
            $("div.error").remove();
        },
        load:true
    });
}
/*
 *
            $("ul.logsub_tabs").tabs("div.logsub_panes > div",{
                effect:'fade',
                fadeInSpeed:400,
                history:true,
                onBeforeClick:function(even,tabIndex){

                },
                onClick:function(even,tabIndex){
                    $("#quick_subscribe_form").validator({
                        'lang':'fr',
                        offset:[0,5]
                    });
                    $("#quick_login_form").validator({
                        'lang':'fr',
                        offset:[0,5]
                    });
                }
            });
 */