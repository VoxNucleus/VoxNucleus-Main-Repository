/* */
$(document).ready(function(){
    $("#manual_integration_javascript .postinfo").bind("change keypress", function(){
        updateEncodedURL();
    });
});
function updateEncodedURL(){
    var url="http://www.voxnucleus.fr/postmanagement/sharer?"
    var first= true;
    $(".postinfo").each(function(){
        if($(this).val().length>0){
            if(first==false){
                url+="&";
            }
            url+=$(this).attr("name")+"=";
            url+=encodeURIComponent($(this).val());
            first=false;
        }
        $("#result_share").val(url);
    });
}
/* */
$(document).ready(function(){
    $("#automatic_integration_javascript .postinfo").bind("blur change keypress", function(){
        replaceIntegrationText();
    });
});
function replaceIntegrationText(){
    $("#integration_text").text(constructCodeButton());
}
function constructCodeButton(){
    var button_code="<a class='vn_button "+
    $("div#automatic_integration_javascript select").attr("value")
    +"'>";
    $("div#automatic_integration_javascript input").each(function(){
        if($(this).val().length>0){
            button_code = button_code+buildInvisSpan("vn_"+$(this).attr('name'),$(this).val());
        }
    });
    button_code=button_code+"</a>";
    return button_code;
}
function buildInvisSpan(span_class, span_data){
    return "<span style=\"display:none;\" class=\"vn_data "+span_class+"\">"
    +span_data+"</span>";
}

$(document).ready(function(){
    $(".tocopy").hover(function(){
        if( window.clipboardData){
            var context = this;
            $(this).append("<div class=\"abs_copy\"> </div>").click(function(){
                window.clipboardData.setData((context).text());
            });
        }
    }, function(){
        $(".abs_copy").remove();
    });
});