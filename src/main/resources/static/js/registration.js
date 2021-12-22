$(document).ready(function () {

    const home = "http://localhost:8080/";

    $("#registration-form").submit(function (e) {
        e.preventDefault();
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: home + "registerUser",
            data: JSON.stringify($("#registration-form").serializeJSON()),
            dataType: 'json',
            success: function () {
                alert('Success');
            },
            error: function (xhr) {
                alert(xhr.responseText);
            }
        });
    });
});