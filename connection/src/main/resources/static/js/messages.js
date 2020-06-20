$(function () {
    var str = window.location.search;
    var params = str.split('=');
    var clientId = params[1];

    $('#title').html('设备id：' + clientId);

    $('#return').click(function () {
        $(location).attr('href', 'collection');
    });

    getRequest(
        '/http/get/messages/' + clientId,
        function (res) {
            var arr = [];
            var list = res.detail;
            for (var i = 0; i < list.length; i++) {
                var payload = list[i].payload;
                var addTime = dateFormat("YYYY-mm-dd HH:MM:SS", new Date(list[i].addTime));
                // var html = '<tr>'
                //     + '<td>' + (i + 1) + '</td>'
                //     + '<td>' + payload + '</td>'
                //     + '<td>' + addTime + '</td>'
                // $('table tbody').append(html);
                var obj = {};
                obj.id = i + 1;
                obj.payload = payload;
                obj.addTime = addTime;
                arr.push(obj);
            }
            $('#table').bootstrapTable('load', arr);
        },
        function (err) {
            
        }
    );
});