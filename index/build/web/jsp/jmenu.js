/* */
$(document).ready(function(){
    /*$("ul.subnav").parent().append("<span class=\"unclicked\" id=\"drop_down_button\"></span>"); //Only shows drop down trigger when js is enabled (Adds empty span tag after ul.subnav*)*/
    $("ul.filter > li.best").addClass("unclicked");
    $("ul.filter > li.best").click(function() {
        var status;
        if($(this).attr("class").search("unclicked")>=0){
            $(this).find("ul.subnav").slideDown('fast',function(){
                status="subhover";
                $(this).parent().removeClass("unclicked").addClass("subhover");
            }).show();
        }else{
            $(this).find("ul.subnav").slideUp('slow',function(){
                status="unclicked"
                $(this).removeClass("subhover").parent().addClass("unclicked");
            });
        }
        
    });
});


/*Second dropdownmenu*/
$(document).ready(function(){

    var selectedContext;
    $("div.categories").hover(function(){
        selectedContext=$(this).find("div.selected");
        $("div.categories > div.category_select").click(function(){
            $("div.categories > div.selected").removeClass("selected");
            var cursor=this;
            $(document).find("ul.sub_categories").hide();
            $(document).find("div.category_select.clicked").removeClass("clicked");
            $(this).addClass("clicked");
            $(this).find("ul.sub_categories").show();
        });
    }, function(){
        $("div.categories > div.clicked").removeClass("clicked");
        $(selectedContext).addClass("selected");
        $("ul.sub_categories").hide();
        if($(selectedContext).children("a").length==0 ){
            $(selectedContext).children("ul.sub_categories").show();
        }

    });
    $("div.categories > div.category_select.selected ul.sub_categories").show();
    
});

/*Category Menu*/


$(document).ready(function(){
    $("div.categories > ul.category_select").click(function(){
    });
});

/* Filter menu */

$(document).ready(function(){
    var selected_filter=$("div#category_filter").find("ul.filter > li.selected");
    $("div#category_filter").hover(
        function(){
            $(selected_filter).removeClass("selected");
            $("ul.filter li").hover(function(){
                $(this).addClass("selected");
            },function(){
                $(this).removeClass("selected");
            });
        }, function(){
            $("ul.filter > li.selected").removeClass("selected");
            $(selected_filter).addClass("selected");
            
        });
});

/*Search Bar */
$(document).ready(function(){
    $("form#searchfield > input.text_box").click(function(){
        if($(this).val()=="Rechercher..."){
            $(this).val("");
        }
    });
    $("form#searchfield").click(function(e){
        e.stopPropagation();
    });
    $(document).click(function(){
        if($("form#searchfield > input.text_box").val()==""){
            $("form#searchfield > input.text_box").val("Rechercher...");
        }
    });
});
