$(function () {

    getRequest(
        '/http/get/httpMessage/all',
        function (res) {

            var list = res.detail; //消息列表，需要过滤
            var message = new Array(); //过滤后的消息列表
            var count = 0;
            var timeMap = {}; //存储最新时间
            var format = "YYYY-mm-dd HH:MM:SS";
            for (var i = 0; i < list.length; i++) {
                if (message.length == 0) {
                    message[count] = list[i];
                    timeMap[message[count].clientId] = dateFormat(format, new Date(message[count].addTime));
                    count++;
                } else {
                    flag = true; //判断是否能添加
                    for (var j = 0; j < message.length; j++) {
                        if (list[i].clientId == message[j].clientId) {
                            flag =false;
                            var time1 = new Date(list[i].addTime).getTime();
                            var time2 = new Date(message[j].addTime).getTime();
                            if (time2 < time1) {
                                timeMap[message[j].clientId] = dateFormat(format, new Date(list[i].addTime)); //更新最近时间
                            }
                            break;
                        }
                    }
                    if (flag) {
                        message[count] = list[i];
                        timeMap[message[count].clientId] = dateFormat(format, new Date(message[count].addTime));
                        count++;
                    }
                }
            }
            var arr = [];
            for (var i = 0; i < message.length; i++) {
                var clientId = message[i].clientId;
                var payload = message[i].payload;
                var protocol = message[i].protocol;
                var url = message[i].url;
                var time = timeMap[clientId];
                var devType = message[i].devType;
                var devName = message[i].devName;
                var randomNumber = getRandom(8);

                var obj = {};
                obj.randomNumber = randomNumber;
                obj.devType = devType;
                obj.devName = devName;
                obj.time = time;
                obj.action = '<button class="btn btn-success"' + 'id="more' + clientId + '">更多</button>';
                arr.push(obj)
                // var html = '<tr>'
                //     + '<td>' + randomNumber + '</td>'
                //     + '<td>' + devType + '</td>'
                //     + '<td>' + devName + '</td>'
                //     + '<td>' + time + '</td>'
                //     + '<td><button class="btn btn-success"' + 'id="more' + clientId + '">更多</button></td>'
                //     + '</tr>';
                // $('table tbody').append(html);
            }
            $('#table').bootstrapTable('load', arr);
        },
        function (err) {

        }
    )

    $('table').on('click', 'button', function (e) {
        var buttonId = e.target.id;
        var clientId = buttonId.substring(4);
        $(location).attr('href', 'messages?id=' + clientId); //跳转包含指定信息
    });
});

function getRandom(size) {
    var res = '';
    for (var i = 0; i < size; i++) {
        var num = Math.random() * 10; // [
        num = parseInt(num);
        res += num;
    }
    return res;
}