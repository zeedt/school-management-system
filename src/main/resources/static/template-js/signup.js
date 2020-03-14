function signup(form) {
    $("#matchErrorMsg").html("");
    var formData = new FormData(form);
    var firstName = $("#firstName").val();
    var lastName = $("#lastName").val();
    var email = $("#email").val();
    var sClass = $("#sClass").val();
    var password = $("#password").val();
    var cpassword = $("#cpassword").val();
    var regNo = $("#regNo").val();

    if (password != cpassword) {
        $("#matchErrorMsg").html("Passwords do not match");
        return;
    }

    // var formData = {
    //     "firstName" : firstName,
    //     "lastName" : lastName,
    //     "aClass" : sClass,
    //     "password" : password,
    //     "email" : email,
    //     "regNo" : regNo
    // };
    $.ajax({
        type : "POST",
        url : "/signup",
        data : formData,
        async:false,
        contentType : false,
        processData : false,
        cache : false,
        success : function (result) {
            console.log("Result is " + JSON.stringify(result));
            if (!result.isSuccessful) {
                bootbox.alert(result.message);
            }
            $("#matchErrorMsg").html(result.message);
        },
        error : function (error) {
            bootbox.alert("Error occurred while processing file");
            console.log("Error => ", JSON.stringify(error));
        }
        });

}

function triggerSignup () {
    $("#signupid").click();
}