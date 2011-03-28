var vn_server_widget="http://widget.voxnucleus.fr";
var vn_server="http://www.voxnucleus.fr"
var vn_server_api=vn_server_widget+"/getinformation";
var vn_share_url=vn_server+"/postmanagement/sharer"
var vn_vote_url=vn_server+"/request/popup/votepopup?postId=";
var vn_post_base=vn_server+"/post/";
var vn_small_logo_widget=vn_server+"/image/website/widget/vn_small_widget.png";
var vn_small_logo_widget_c=vn_server+"/image/website/widget/vn_small_widget_clicked.png";
var button_handler;
var gathered_data;
/* Important pre-function */
/* Script très pratique, merci à son auteur, Robert Nyman
 * Son site : http://www.robertnyman.com */
var getElementsByClassName = function (className, tag, elm){
    if (document.getElementsByClassName) {
        getElementsByClassName = function (className, tag, elm) {
            elm = elm || document;
            var elements = elm.getElementsByClassName(className),
            nodeName = (tag)? new RegExp("\\b" + tag + "\\b", "i") : null,
            returnElements = [],
            current;
            for(var i=0, il=elements.length; i<il; i+=1){
                current = elements[i];
                if(!nodeName || nodeName.test(current.nodeName)) {
                    returnElements.push(current);
                }
            }
            return returnElements;
        };
    }
    else if (document.evaluate) {
        getElementsByClassName = function (className, tag, elm) {
            tag = tag || "*";
            elm = elm || document;
            var classes = className.split(" "),
            classesToCheck = "",
            xhtmlNamespace = "http://www.w3.org/1999/xhtml",
            namespaceResolver = (document.documentElement.namespaceURI === xhtmlNamespace)? xhtmlNamespace : null,
            returnElements = [],
            elements,
            node;
            for(var j=0, jl=classes.length; j<jl; j+=1){
                classesToCheck += "[contains(concat(' ', @class, ' '), ' " + classes[j] + " ')]";
            }
            try	{
                elements = document.evaluate(".//" + tag + classesToCheck, elm, namespaceResolver, 0, null);
            }
            catch (e) {
                elements = document.evaluate(".//" + tag + classesToCheck, elm, null, 0, null);
            }
            while ((node = elements.iterateNext())) {
                returnElements.push(node);
            }
            return returnElements;
        };
    }
    else {
        getElementsByClassName = function (className, tag, elm) {
            tag = tag || "*";
            elm = elm || document;
            var classes = className.split(" "),
            classesToCheck = [],
            elements = (tag === "*" && elm.all)? elm.all : elm.getElementsByTagName(tag),
            current,
            returnElements = [],
            match;
            for(var k=0, kl=classes.length; k<kl; k+=1){
                classesToCheck.push(new RegExp("(^|\\s)" + classes[k] + "(\\s|$)"));
            }
            for(var l=0, ll=elements.length; l<ll; l+=1){
                current = elements[l];
                match = false;
                for(var m=0, ml=classesToCheck.length; m<ml; m+=1){
                    match = classesToCheck[m].test(current.className);
                    if (!match) {
                        break;
                    }
                }
                if (match) {
                    returnElements.push(current);
                }
            }
            return returnElements;
        };
    }
    return getElementsByClassName(className, tag, elm);
};

/*Actual retrieve script */
onload=function(){
    callvoxnucleus(vn_server_api,"","","postId");
}
function callvoxnucleus(url,callback,name,query)
{
    /* Only launch script in the case more than one element of class
     * vn_button */
    if(getElementsByClassName("vn_button").length>0){
        button_handler=getElementsByClassName("vn_button")[0]
        url=addGetParam("buttontype","icone",url);
        gathered_data=gather_data(button_handler);
        var wherefrom_url=gathered_data.url;
        url=addGetParam("wherefrom",wherefrom_url,url);
        var script=document.createElement("script");
        script.async=true;
        script.setAttribute("src", url);
        script.setAttribute("type", "text/javascript");
        document.body.appendChild(script);
        
    }
}
/* first function called */
function displayButtons(data){
    var button;
    if(data.isInDatabase)
    {
        button=createButton(data);
    }
    else
    {
        button=createButton(data);
    }
    button_handler.appendChild(button);
    clean_data(button_handler);
}
/*Not finished */
function createButton(data,button_type){
    var vn_div=document.createElement("div");
    vn_div.style.width=10;
    vn_div.style.height=30;
    vn_div.appendChild(insert_small_image(data.isInDatabase,create_link_from_data(data)));
    return vn_div;
}
/* If is in database open simple popup, instead just open a normal tab */
function insert_small_image(isInDatabase,out_url){
    var img= document.createElement('img');
    img.src=vn_small_logo_widget;
    img.style.cursor='pointer';
    img.onclick=function(){
        if(isInDatabase)
            window.open(out_url,"test","height=300,width=810,scrollbars=yes");
        else
            window.open(out_url);
    }
    return img;
}
/* Creates the link from the gathered data*/
function create_link_from_data(data){
    if(data.isInDatabase){
        return vn_vote_url+data.postId;
    }else{
        var gathered_data=gather_data(button_handler);

        var share_url=vn_share_url;
        share_url=addGetParam("link",encodeURI(gathered_data.url),share_url);
        if(gathered_data.title)
            share_url=addGetParam("title",encodeURI(gathered_data.title),share_url);
        if(gathered_data.description)
            share_url=addGetParam("description",encodeURI(gathered_data.description),share_url);
        if(gathered_data.tags)
            share_url=addGetParam("tags",encodeURI(gathered_data.tags),share_url);
        return share_url;
    }
}
/*Retrieve data that is stored  */
function gather_data(vn_button_handler){
    var info =new Object();
    /* get Description*/
    var descriptionarray =getElementsByClassName("vn_description",vn_button_handler);
    if(descriptionarray.length>0)
        info.description=descriptionarray[0].firstChild.data;
    else
        info.description="";
    /* get Title */
    var titlearray=getElementsByClassName("vn_title",vn_button_handler);
    if(titlearray.length>0)
        info.title=titlearray[0].firstChild.data;
    else
        info.title=document.title;
    /* Get Tags */
    var tagsarray=getElementsByClassName("vn_tags",vn_button_handler);
    if(tagsarray.length>0)
        info.tags=tagsarray[0].firstChild.data;
    else
        info.tags="";
    /* Get Tags */
    var urlarray=getElementsByClassName("vn_link",vn_button_handler);
    if(urlarray.length>0)
        info.url=encodeURIComponent(urlarray[0].firstChild.data);
    else
        info.url=encodeURIComponent(document.location.href);
    return info;

}
/* Called after data has been submitted and analyzed
 * cleans page of remaining artefacts */
function clean_data(button_handler){
    var data_list=getElementsByClassName("vn_data",button_handler);
    for(var i=0; i < data_list.length;i++){
        button_handler.removeChild(toDel=data_list[i]);
    }
}
/*
 * Add parameter to get requests
 */
function addGetParam(param,value,url){
    if(url.indexOf("?")>-1){
        url+="&"+param+"="+value;
    }
    else{
        url+="?"+param+"="+value;
    }
    return url;
}