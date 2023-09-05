
    function editBtn() {
    $('#content').attr('contenteditable', 'true');
    $('#content').focus();
}

    $('#content').keypress(function (event) {
    let keycode = (event.keyCode ? event.keyCode : event.which);
    if (keycode == '13') {
    event.preventDefault(); // 엔터 키 기본 동작 방지
    saveContent();
}
});

    function saveContent() {
    let content = $('#content').text();
    $.ajax({
    type: 'POST',
    url: '/updateStatus',
    data: JSON.stringify({ content: content }),
    contentType: 'application/json',
    success: function (data) {
    location.reload();
},
    error: function (xhr, status, error) {
    console.log(error);
}
});
}

    function titleEditBtn() {
    $('#miniTitle').attr('contenteditable', 'true');
    $('#miniTitle').focus();
}

    $('#miniTitle').keypress(function(event) {
    let keycode = (event.keyCode ? event.keyCode : event.which);
    if (keycode == '13') {
    event.preventDefault();
    saveTitle();
}
});

    function saveTitle() {
    let title = $('#miniTitle').text();
    $.ajax({
    type: 'POST',
    url: '/updatePageTitle',
    data: JSON.stringify({ pageTitle: title }),
    contentType: 'application/json',
    success: function(data) {
    location.reload();
},
    error: function(xhr, status, error) {
    console.log(error);
}
});
}


