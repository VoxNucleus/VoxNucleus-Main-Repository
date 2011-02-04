$(document).ready(function(){
    var input_remote_file='<input class="postinfo" id="remote_file" name="remote_file" value="" type="textarea">';
    var input_local_file='<input class="postinfo" id="image" name="file" value="" type="file">';

    var message_remote_file="Cliquer pour sélectionner un fichier depuis une adresse";
    var message_local_file="Cliquer pour sélectionner un fichier depuis votre disque dur";

    $("span.remote_uploader").click(function(){
        $("input.file_uploader").remove();
        if($(this).hasClass("remote")){
            $(this).html(message_local_file);
            $(this).removeClass("remote");
            $("tr#input_upload > td[class!=\"left_col\"]").html(input_remote_file);
        }else{
            $(this).html(message_remote_file);
            $("tr#input_upload > td[class!=\"left_col\"]").html(input_local_file);
            $(this).addClass("remote");
        }
    });
});