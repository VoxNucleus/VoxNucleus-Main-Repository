function retrievePostByUser(userId,begin){
    sleepAskBestInPeriod(120000);
}

function askPostByUser(){
    var options={
        type: 'POST',
        url:'/request/getpostbyuser',
        dataType:'xml',
        data:{
            'userId':userId,
            'begin':begin,
            'end': parseInt(begin)+5
        },
        error: function(xhr,textStatus, errorThrown){
            alert('An error occured ' + errorThrown);
        },
        success:function(data,textStatus){
            analyzeXML(data,textStatus);
        }
    };
    $.ajax(options);
}

function sleepAskPostByUser(delay){
    setTimeout("askPostByUser()", delay);
}

