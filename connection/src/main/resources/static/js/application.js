$(function () {

    getRequest(
        '/search/topicWithClients/all',
        function (res) {
            var arr = [];
            var list = res.detail;
            for (var i = 0; i < list.length; i++) {
                topic = list[i].topic;
                clients = list[i].clients;
                if (clients == null || clients.length == 0) {
                    clients = "无";
                }
                // $('#topic').append(
                //     '<p>' + topic + ':' + clients + '</p>'
                //     + '<button class="hist" id="history-' + topic + '"' + '>查看历史发布信息</button>' + '<br>'
                //     + '<input type="text" id=' + topic + ' placeholder="请输入待发布消息">'
                //     + '<button class="post" id="btn-' + topic + '"' + '>发布</button>'
                // );
                var obj = {};
                obj.topic = topic;
                obj.clients = clients;
                // obj.content = '<input type="text" id=' + topic + ' placeholder="请输入待发布消息">';
                obj.content = '<input type="text" id=' + topic + ' placeholder="请输入待发布消息">';
                obj.buttonPost = '<button class="post btn btn-success" id="btn-' + topic + '"' + '>发布</button>';
                obj.buttonHistory = '<button class="hist btn btn-success" id="history-' + topic + '"' + '>查看历史发布信息</button>';
                arr.push(obj);
            }
            $('#table').bootstrapTable('load', arr);
        },
        function (err) {
        }
    );

    $('#topic').on('click', '.hist', function (e) { // todo 没写完
        var btn_id = e.target.id;
        var topic = btn_id.substring(8);
        // getRequest(
        //   'search/contents/all/' + topic,
        //   function (res) {
        //       var list =res.detail;
        //       console.log(list.length);
        //   },
        //   function (err) {
        //
        //   }
        // );
        $(location).attr('href', 'content?topic=' + topic);
    });


    $('#topic').on('click', '.post', function (e) {
       var btn_id = e.target.id;
       var topic = btn_id.substring(4);
       var content = $('#' + topic).val();
       getRequest(
         '/mqtt/send?topic=' + topic + '&content=' + content,
           function (res) {
               toastr.success('消息发布成功!');
           },
           function (err) {

           }
       );
    });

});

