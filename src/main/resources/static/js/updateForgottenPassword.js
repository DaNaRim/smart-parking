$(document).ready(function () {

    const home = "http://localhost:8080/";

    $("#update-password-form").submit(function (e) {
        e.preventDefault();
        $.ajax({
            type: "PUT",
            contentType: "application/json",
            url: home + "updateForgottenPassword",
            data: JSON.stringify($("#update-password-form").serializeJSON()),
            dataType: 'json',
            success: function (tx_response) { //FIXME
                // alert('Success');
                // $('.result').html(tx_response.data)
                // window.location.href = tx_response.data
            },
            error: function (xhr) {
                alert(xhr.responseText);
            }
        });
    });
});