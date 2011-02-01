/**
 *main function. Try to delay.
 */
function retrieveLastEntries(){
    sleepAskLastPosts(120000);
}

function askLastPosts(){
    var page_number=$("span.hidden > span.page_number").html();
    var category=$("span.hidden").find("span.category").html();
    var sub_category=$("span.hidden").find("span.sub_category").html();
    var options={
        type: 'POST',
        url:'/request/lastEntries',
        data:{
            'category':category,
            'sub_category':sub_category,
            'page_number':page_number},
        dataType:'xml',
        error: function(xhr,textStatus, errorThrown){
            failLoadingPosts('retrieveLastEntries');
        },
        success:function(data,textStatus){
            analyzeXML(data,textStatus);
            retrieveLastEntries();
        }
    };
    $.ajax(options);
    
}

function sleepAskLastPosts(delay){
    setTimeout("askLastPosts()", delay);
}