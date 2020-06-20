var list = []; //存放设备对象的类型、协议、名称

$(function () {

    getRequest(
      '/search/device/all',
        function (res) {
            var list = res.detail;
            var arr = [];
            for (var i = 0; i < list.length; i++) {
                // var id = list[i].id;
                // var name = list[i].name;
                // var devType = list[i].typeName;
                // var protocol = list[i].protocol;
                // var html = '<tr>'
                //     + '<td>' + id + '</td>' //这里应该修改为顺序排列的id
                //     + '<td>' + devType + '</td>'
                //     + '<td>' + protocol + '</td>'
                //     + '<td>' + name + '</td>'
                //     + '<td><button class="btn btn-success"' + 'id=more' + id + '>更多</button></td>'//跳转的页面相同，但根据不同隐藏id填充不同信息
                //     + '</tr>';
                // $('table tbody').append(html);
                var obj = {};
                obj.id = list[i].id;
                obj.typeName = list[i].typeName;
                obj.protocol = list[i].protocol;
                obj.name = list[i].name;
                obj.button = '<button class="btn btn-success"' + 'id=more' + obj.id + '>更多</button>';
                arr.push(obj);
            }
            $('#table').bootstrapTable('load', arr);
        },
        function (err) {
            
        }
    );

    $('#add').click(function () {
        var formData = {
            'name' : $('#dev-name').val(),
            'devType' : $('#dev-type').val(),
            'roleType' : $('#dev-role').val(),
            'protocol' : $('#select-protocol').val()
        };
        postRequest(
            '/search/add/device',
            formData,
            function (res) {
                if (res.success) {
                    var device = res.detail;
                    var id = device.id; //插入device表的id
                    var devType = device.typeName;
                    var roleType = device.roleName;
                    var name = device.name; //设备名
                    var protocol = device.protocol;

                    var arr = [];
                    var obj = {};
                    obj.id = id;
                    obj.typeName = devType;
                    obj.protocol = protocol;
                    obj.name = name;
                    obj.button = '<button class="btn btn-success"' + 'id=more' + id + '>更多</button>';
                    $('#table').bootstrapTable('load', obj);
                    // var html = '<tr>'
                    //     + '<td>' + id + '</td>' //这里应该修改为顺序排列的id
                    //     + '<td>' + devType + '</td>'
                    //     + '<td>' + protocol + '</td>'
                    //     + '<td>' + name + '</td>'
                    //     + '<td><button class="btn btn-success"' + 'id=more' + id + '>更多</button></td>'//跳转的页面相同，但根据不同隐藏id填充不同信息
                    //     + '</tr>';
                    // $('table tbody').append(html);
                    window.location.reload();
                }
            },
            function (error) {
                alert(error);
            }
        );
    });

    //根据数据库内容获取input框数据
    getRequest(
        '/search/roles/all',
        function (res) {
            var list = res.detail;
            for(var i = 0; i < list.length; i++) {
                var role = list[i].roleName;
                $('#rolelist').append(
                    '<option>' + role + '</option>'
                );
            }

        },
        function (err) {

        }
    );

    getRequest(
      '/search/devTypes/all',
      function (res) {
          var list = res.detail;
          for(var i = 0; i < list.length; i++) {
              var type = list[i].typeName;
              $('#typelist').append(
                  '<option>' + type + '</option>'
              );
          }
        },
        function (err) {

        }
    );

    $('table').on("click", "button", function (e) {
        console.log('更多');
        var buttonId = e.target.id;
        var clientId = buttonId.substring(4);
        $(location).attr('href', 'details?id=' + clientId); //跳转包含指定信息
    });
});
