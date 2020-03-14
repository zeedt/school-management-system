function triggerUpload() {
    $("#uploadForm").click();
}

function readURL(input) {
    if (input.files && input.files[0]) {

        var reader = new FileReader();

        reader.onload = function(e) {
            $('.image-upload-wrap').hide();

            $('.file-upload-image').attr('src', e.target.result);
            $('.file-upload-content').show();

            $('.image-title').html(input.files[0].name);
        };

        reader.readAsDataURL(input.files[0]);

    } else {
        removeUpload();
    }
}

function removeUpload() {
    $('.file-upload-input').replaceWith($('.file-upload-input').clone());
    $('.file-upload-content').hide();
    $('.image-upload-wrap').show();
}
$('.image-upload-wrap').bind('dragover', function () {
    $('.image-upload-wrap').addClass('image-dropping');
});
$('.image-upload-wrap').bind('dragleave', function () {
    $('.image-upload-wrap').removeClass('image-dropping');
});



function uploadRegistarationData(form) {

    console.log("About to upload");
    var formData = new FormData(form);
    formData['parentId'] = 1;
    formData[csrfParameter] = csrfToken;
    $.ajax({
        type : "POST",
        url : "/upload-bulk-registration-details",
        data : formData,
        async:false,
        contentType : false,
        processData : false,
        cache : false,
        success : function (result) {
            bootbox.alert(result.message);
            console.log("Result is " + JSON.stringify(result));
            if (result.failureCount > 0) {
                var html = "<table class='table table-striped'> <thead><tr><td>Row number</td><td>Failure Reasons</td></tr></thead>";
                for (var i=0;i<result.failureCount;i++) {
                    html = html + "<tr><td>"+result.failureRows[i] +"</td>"+"<td>"+result.failureReasons[i]+"</td></tr>";
                }
                html = html + "</table>";
                $("#errorMessageContent").html(html);
            }
        },
        error : function (error) {
            bootbox.alert("Error occurred while processing file");
            console.log("Error is " + JSON.stringify(error));
        }
    })

}

function showErrorMessage() {
    $("#myModal").modal();
}