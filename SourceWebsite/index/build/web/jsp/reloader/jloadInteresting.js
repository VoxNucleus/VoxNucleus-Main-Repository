function retrieveInteresting(){
    sleepAskInterestingPosts(120000);
}


function askInterestingPosts(){
    var page_number=$("span.hidden > span.page_number").html();
    var category=$("span.hidden >span.category").html();
    var sub_category=$("span.hidden").find("span.sub_category").html();
    var options={
        type: 'POST',
        url:'/request/interestingentries',
        data:{
            'category':category,
            'sub_category':sub_category,
            'page_number':page_number},
        dataType:'xml',
        error: function(xhr,textStatus, errorThrown){
            failLoadingPosts('retrieveInteresting');
        },
        success:function(data,textStatus){
            analyzeXML(data,textStatus);
            retrieveInteresting()
        }
    };
    $.ajax(options);
    
}

function sleepAskInterestingPosts(delay){
    setTimeout("askInterestingPosts()", delay);
}