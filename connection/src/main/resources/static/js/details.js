var roles = ""; //设备角色
var protocol = ""; //设备协议
var publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCO3H+z8+MgYJH7S8S0WsLoXyW23fcTBWPrZCdBicH5pR0lBxPCZ/Ibl4Y3lRGLT0Agrth1LEz7/GiBKNQjPtB68CIHxIAJr3KM5ReIsq2zkeuIKA3yvUx2vTOzvg9miFLgNjp+uX2MM5sF2kBonJeO/LhIp2H8in41JLCC9RFEBQIDAQAB";
var list = ['topic1', 'topic2']; //topicWithContent

$(function () {
    toastr.options.timeOut = 5000;

    var str = window.location.search;
    var params = str.split('=');
    var clientId = params[1];

    getRequest(
        '/search/device/' + clientId,
        function (res) {
            var device = res.detail;
            if (device.protocol == 'MQTT') {
                $('#form2').removeAttr('hidden');
                getTopics(clientId);
            } else if (device.protocol == 'HTTP') {
                $('#form3').removeAttr('hidden');
            } else if (device.protocol == 'COAP') {
                $('#form3').removeAttr('hidden');
            }

            $('#role').val(device.roleName);
            $('#protocol').val(device.protocol);
            $('#add_time').val(device.addTime);
            roles = device.roleName; //给role赋值
            protocol = device.protocol;
        },
        function (err) {
        }
    );

    //定期获取公钥
    setInterval(function () {
        getRequest(
            '/search/publicKey',
            function (res) {
                publicKey = res.detail;
            },
            function (err) {
            }
        );
    }, 2000);

    $('#subscribe').click(function () {
        var topic = $('#input_topic').val(); //获取输入的主题
        
        for (var i = 0; i < list.length; i++) {
            if (list[i] == topic) {
                toastr.info('请勿重复获取主题!');
                return;
            }
        }
        
        getRequest(
            '/mqtt/subscribe?topic=' + topic + '&client=' + clientId,
            function (res) {
                addTopic(topic);
            },
            function (err) {
            }
        )
    });

    $('#upload').click(function () { //模拟发送http/coap报文
        var data1 = {};
        var url = '/httpUpload?temperature=' + $('#input_param').val();

        if ($('#input_param').val() != '') {
            data1.temperature = $('#input_param').val();
        }
        if ($('#input_param2').val() != '') {
            data1.warm = $('#input_param2').val();
        }

        var request = "";
        if (protocol == 'HTTP') {
            request = postHttpRequest(url, data1);
            // request = getHttpRequest(url);
        } else {
            request = coapRequest('/httpUpload?A=1&B=2', data1);
        }

        var encRole = encRSA(roles); //RSA加密

        postRequest(
            '/http/upload/' + clientId + '?roles=' + encRole,
            request,
            function (res) {
                if (res.success) {
                    var response = res.detail;
                    coapParse(response);
                    var d = Data;
                    toastr.success(d.Payload); //打印payload的信息
                } else {
                    var msg = res.msg;
                    toastr.warning(msg);
                }

            },
            function (e) {

            }
        )
    });

    $('.get').click(function (e) {
        var method = e.target.id.split('-')[1];
        var request = '';
        if (method == 'type') {
            if (protocol == 'COAP') {
                request = coapRequest('/getDeviceTypeBy', {'id' : 12345}); //获取
            } else {
                request = getHttpRequest('/getDeviceTypeBy');
            }

        } else if (method == 'weather') {
            if (protocol == 'COAP') {
                request = coapRequest('/getAllWeathers')
            } else {
                request = getHttpRequest('/getAllWeathers');
            }
        } else if (method == 'core') {
            if (protocol == 'COAP') {
                request = coapRequest('/getAllCores')
            } else {
                request = getHttpRequest('/getAllCores');
            }
        }

        var encRole = encRSA(roles);

       postRequest(
           '/http/upload/' + clientId + '?roles=' + encRole,
           request,
           function (res) {
               var response = res.detail;

               if(res.success) {
                   if (method == 'weather') {
                       var list = [];
                       if (protocol == 'COAP') {
                           coapParse(response);
                           var d = Data;
                           list = d.Payload.substring(1, d.Payload.length - 1).split(", ");
                       } else {
                           list = res.detail;
                       }
                       var info = "";
                       for (var i = 0; i < list.length; i++) {
                           var id;
                           var temperature;
                           var time;
                           var climate;
                           if (protocol == 'HTTP') {
                               id = list[i].id;
                               temperature = list[i].temperature;
                               time = dateFormat("YYYY-mm-dd", new Date(list[i].dayTime));
                               climate = list[i].climate;
                           } else {
                               var arr = list[i].split(":");
                               id = arr[0].split("=")[1];
                               temperature = arr[1].split("=")[1];
                               time = dateFormat("YYYY-mm-dd", new Date(arr[2].split("=")[1]));
                               climate = arr[5].split("=")[1];
                           }
                           info += "日期:" + time + " 温度:" + temperature + " 天气:" + climate + "<br>";
                       }
                       toastr.info(info);
                   } else if (method == 'type') {
                       if (protocol == 'HTTP') {
                           toastr.info('设备类型：' + response);
                       } else {
                           coapParse(response);
                           var d = Data;
                           toastr.info('设备类型：' + d.Payload);
                       }
                   } else if (method == 'core') {
                       if (protocol == 'HTTP') {
                           var list = res.detail;
                           var info = "";
                           for (var i = 0; i < list.length; i++) {
                               var warm = list[i].warm;
                               var zwx = list[i].zwx;
                               var day = dateFormat("YYYY-mm-dd", new Date(list[i].date));
                               info += "日期:" + day + " 湿度:" + warm + " 紫外线指数:" + zwx + "<br>";
                           }
                           toastr.info(info);
                       } else {
                           coapParse(response);
                           var d = Data;
                       }
                   }
               } else {
                   toastr.warning(res.msg); //没有权限
               }

           },
           function (err) {
           }
       )
    });

    $('#form2').on('click', '.btn', function (e) {
        var topic = e.target.id;
        $(location).attr('href', 'content?topic=' + topic);
    });
});

function addTopic(topic) {
    // var html = '<p>' + topic + '</p>' + '<textarea id="' + topic + '"' + '>'  + contents + '</textarea>' + '<br>';
    var html = '<buttton class="btn btn-success" type="button" id="' + topic + '"' + 'style="height:50px; width: 100px; line-height: 37px; margin-right: 10px" text-align: center' + ' + >' + topic + '</buttton>'; //每次添加一个主题按钮
    $('#form2 legend').after(html);
}

function getTopics(clientId) {
    getRequest(
      '/search/topics/all/' + clientId,
      function (res) {
          list = res.detail;
          for (var i = 0; i < list.length; i++) {
              var topic = list[i];
              addTopic(topic);
          }
      },
      function (err) {

      }
    );
}

function getHttpRequest(url) {
    return initHttpRequest('GET', url);
}

function postHttpRequest(url, data) {
    var pre =  initHttpRequest('POST', url);
    var body = '';
    jQuery.each(data, function (k, v) {
       body += k + '=' + v + '&';
    });

    body = body.substring(0, body.length - 1); //去掉末尾的&
    console.log(JSON.stringify(data));
    return pre + '\r\n\r\n' + JSON.stringify(data); //将对象转换为字符串
}
//常规请求头
function initHttpRequest(type, url) {
    var request = type + ' ' + url + ' HTTP/1.1' + '\r\n'
        + 'HOST:' + '127.0.0.1:8090' + '\r\n'
        + 'Accept:' + 'application/json' + '\r\n'
        + 'Accept-Charset:' + 'utf-8' + '\r\n'
        + 'Accept-Language:' + 'zh-CN' + '\r\n'
        + 'Content-Type:' + 'application/json' + '\r\n'
        + 'Cookie:' + 'MessageID=' + getRandomStr(16) + '\r\n'
        + 'User-Agent:' + 'Mozilla/5.0 Chrome/73.0.3683.103 Safari/537.36';
    return request;
}

function coapRequest(url, data) {
    var Ver = "01";
    var T = "00"; //CON
    var TKL = "0000"; //没有Token
    var Code = "00000011"; //PUT
    var MessageId = generateRandomBinary(16);
    var Token = (T == "00") ? "" : generateRandomBinary(parseInt(T, 2) * 8);
    var Options = "";
    var Delta = "";
    var Length = "";
    var Value = "";
    var list = url.split("/"); //url以斜杠开头，split[0]为空串
    var flag = "0101"; //判断是否url后面有问号
    for (var i = 1; i < list.length; i++) { //url-path：11
        if (i == 1) {
            Delta = "1011";
        } else {
            Delta = "0000";
        }
        if (i == list.length - 1 && list[i].indexOf("?") != -1) {
            flag = "0001";
            var arr = list[i].split("?");
            Options += Delta + transferIntToBinary(arr[0].length, 4) + transferStringToAscii(arr[0]); //最后一个url-path
            //下面开始解析param
            var params = parseParam(arr[1]); //字符串数组
            for (var i = 0 ; i < params.length; i++) {
                if (i == 0) {
                    Delta = "0100";
                } else {
                    Delta = "0000";
                }
                Length = transferIntToBinary(params[i].length, 4);
                Value = transferStringToAscii(params[i]);
                Options += Delta + Length + Value;
            }
            break; //最后一次迭代后直接跳出循环
        }
        Length = transferIntToBinary(list[i].length, 4);
        Value = transferStringToAscii(list[i]);
        Options += Delta + Length + Value;
    }
    Options += flag + "0010" + "0011010100110000"; // content-format: json

    var coapRequest = Ver + T + TKL + Code + MessageId + Token + Options;
    if (data == null || data.length == 0) { //如果没有payload可以不加分隔符
        return coapRequest;
    }
    var Sign = "11111111"; //分隔符
    var Payload = transferStringToAscii(JSON.stringify(data));
    coapRequest = coapRequest + Sign + Payload;

    return coapRequest;
}


function generateRandomBinary(len) {
    var s = "";
    for (var i = 0; i < len; i++) {
        s += parseInt(Math.random() * 2);
    }
    return s;
}

function transferIntToBinary(v, size) { //v:十进制数, size:希望生成的长度
    var s = "";
    while (parseInt(v / 2) != 0) {
        s = v % 2 + s;
        v = parseInt(v / 2);
    }
    s = v + s;
    while (s.length < size) {
        s = '0' + s;
    }
    return s;
}

function transferStringToAscii(s) {
    var result = "";
    for (var i = 0; i < s.length; i++) {
        result += transferIntToBinary(s.charCodeAt(i), 8);
    }
    return result;
}

function parseParam(params) { //A=1&B=2&C=3
    var A = new Array();
    if (params.indexOf("&") == -1) {
        A[0] = params;
    } else {
        A = params.split("&");
    }
    return A;
}

function getRandomStr(size) { //用于生成http的messageId
    var s = "";
    var arr = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"; //length = 62
    for (var i = 0; i < size; i++) {
        s += arr.charAt(parseInt(Math.random() * 63));
    }
    return s;
}

function encRSA(roles) {
    var encrypt = new JSEncrypt();
    encrypt.setPublicKey(publicKey);
    var encRole =encrypt.encrypt(roles); //公钥加密
    var encencRole = encodeURIComponent(encRole); //防止url中的加号无法获取

    return encencRole;
}
