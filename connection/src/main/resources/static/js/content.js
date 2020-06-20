$(function () {
    var str = window.location.search;
    var params = str.split('=');
    var topic = params[1];

    $('#return').click(function () {
        $(location).attr('href', 'application');
    });

    getRequest(
        '/search/contents/all/' + topic,
        function (res) {
            var list = res.detail;
            var arr = [];
            for (var i = 0; i < list.length; i++) {
                var content = list[i].content;
                var addTime = dateFormat("YYYY-mm-dd HH:MM:SS", new Date(list[i].addTime));
                // $('#content').append(
                //     '<p id="' + i +'">' + content + ' ' + addTime + '</p>'
                // );
                var obj = {};
                obj.content = content;
                obj.addTime = addTime;
                arr.push(obj);
            }
            $('#table').bootstrapTable('load', arr);

            setInterval(function () {
                getRequest(
                    '/search/contents/all/' + topic,
                    function (res) {
                        var list = res.detail;
                        for (var i = 0; i < list.length; i++) {
                            var content = list[i].content;
                            var addTime = dateFormat("YYYY-mm-dd HH:MM:SS", new Date(list[i].addTime));
                            // if ($('#' + i).length > 0) { //判断元素存在
                            //     $('#' + i).html(content + ' ' + addTime);
                            // } else {
                            //     $('#content').append(
                            //         '<p id="' + i + '">' + content + ' ' + addTime + '</p>'
                            //     );
                            // }
                            if (i + 1 <= $('#table').bootstrapTable('getOptions').totalRows) {
                                continue;
                            }
                            var obj = {};
                            obj.content = content;
                            obj.addTime = addTime;
                            $('#table').bootstrapTable('append', obj);
                        }
                    },
                    function (err) {
                    }
                )
            }, 1000);
        },
        function (err) {
        }
    );


});