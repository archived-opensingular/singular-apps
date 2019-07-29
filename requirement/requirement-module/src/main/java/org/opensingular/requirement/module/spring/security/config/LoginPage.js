(function () {
    App.init(); // init metronic core components
    Layout.init(); // init current layout
    $("input").keypress(function (event) {
        if (event.keyCode == 13) {
            event.preventDefault();
            $("#btnsubss").click();
        }
    });
});