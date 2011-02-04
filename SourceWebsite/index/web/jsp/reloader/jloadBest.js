function retrieveBestInPeriod(){
    sleepAskBestInPeriod(120000);
}


function askBestInPeriod(){
    var page_number=$("span.hidden > span.page_number").html();
    var category=$("span.hidden > span.category").html();
    var sub_category=$("span.hidden > span.sub_category").html();
    var time_filter=$("span.hidden > span.time_filter").html();
    var options={
        type: 'POST',
        url:'/request/bestin'+time_filter,
        dataType:'xml',
        data:{
            'category':category,
            'sub_category':sub_category,
            'page_number':page_number,
            'end':10
        },
        error: function(xhr,textStatus, errorThrown){
            failLoadingPosts('retrieveBestInPeriod');
        },
        success:function(data,textStatus){
            analyzeXML(data,textStatus);
            retrieveBestInPeriod();
        }
    };
    $.ajax(options);
}





function sleepAskBestInPeriod(delay){
    setTimeout("askBestInPeriod()", delay);
}