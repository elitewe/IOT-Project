var T;
var TKL;
var Code;
var MessageId;
var Token;
var Options = {}; //Map类型
var Payload;
var Data = {};

function coapParse(s) {
    var temp = s.substring(2, 4);
    parseT(temp);
    temp = s.substring(4, 8);
    parseTKL(temp);
    temp = s.substring(8, 16);
    parseCode(temp);
    temp = s.substring(16, 32);
    parseMessageId(temp);
    temp = s.substring(32, 32 + 8 * TKL);
    parseToken(temp);
    var index = getSplitIndex(s);
    if (index == -1) { //没有分隔符
        temp = s.substring(32 + 8 * TKL);
        parseOptions(temp);
        Payload = "";
    } else {
        temp = s.substring(32 + 8 * TKL, index);
        parseOptions(temp);
        temp = s.substring(index + 8);
        parsePayload(temp);
    }
    initData();
}

function parseT(s) {
    switch (s) {
        case "00":
            T = "CON"; break;
        case "01":
            T = "NON"; break;
        case "10":
            T = "ACK"; break;
        case "11":
            T = "Reset"; break;
        default:
            T = "Error";
    }
}

function parseTKL(s) {
    TKL = parseBinary(s);
}

function parseCode(s) {
    var num1 = parseBinary(s.substring(0, 3));
    var num2 = parseBinary(s.substring(3, 8));
    var sum = num1 * 100 + num2;
    switch (sum) {
        case 1: Code = "GET"; break;
        case 2: Code = "POST"; break;
        case 3: Code = "PUT"; break;
        case 4: Code = "DELETE"; break;
        case 201: Code = "Created"; break;
        case 202: Code = "Deleted"; break;
        case 203: Code = "Valid"; break;
        case 204: Code = "Changed"; break;
        case 205: Code = "Content"; break;
        case 400: Code = "Bad Request"; break;
        case 401: Code = "Unauthorized"; break;
        case 402: Code = "Bad Option"; break;
        case 403: Code = "Forbidden"; break;
        case 404: Code = "Not Found"; break;
        case 405: Code = "Method Not Allowed"; break;
        case 406: Code = "Not Acceptable"; break;
        case 412: Code = "Precondition Failed"; break;
        case 415: Code = "Unsuppor Conten-Type"; break;
        case 500: Code = "Internal Server Error"; break;
        case 501: Code = "Not Implemented"; break;
        case 502: Code = "Bad Gateway"; break;
        case 503: Code = "Service Unavailable"; break;
        case 504: Code = "Gateway Timeout"; break;
        case 505: Code = "Proxying Not Supported"; break;
    }
}

function parseMessageId(s) {
    MessageId = s;
}

function parseToken(s) {
    Token = s;
}

function parseOptions(s) {
    var delta = 0;
    var length = 0;
    var count = 0;
    var key = "";
    var value = "";
    while (count < s.length) {
        delta += parseBinary(s.substring(count, count + 4));
        length = parseBinary(s.substring(count + 4, count + 8));
        key = getKey(delta);
        var temp = s.substring(count + 8, count + 8 + length * 8);//ascii字符串
        if ("If-Match, E-Tag".indexOf(key) == -1) { //Opaque类型无法解释为ascii
            value = parseAscii(temp);
        } else {
            value = temp;
        }
        if (key == "Uri-Path") { //多个url需要拼接
            value = (Options.has("Uri-Path") ? Options.get("Uri-Path") : "") + "/" + value;
        } else if (key == "Uri-Query") { //多个Query需要拼接
            value = (Options.has("Uri-Query") ? Options.get("Uri-Query") + "&" : "") + value;
        }
        Options[key] = value;
        count = count + 8 + length * 8; //下一个key-value的开始
    }
}

function parsePayload(s) {
    Payload = parseAscii(s);
}

function getSplitIndex(s) {
    return s.indexOf('11111111');
}

function parseBinary(s) {
    var sum = 0;
    for (var i = 0; i < s.length; i++) {
        sum = sum * 2 + parseInt(s.charAt(i));
    }
    return sum;
}

function parseAscii(s) {
    var result = "";
    var t = parseInt(s.length / 8);
    for (var i = 0; i < t; i++) {
        var temp = s.substring(i * 8, i * 8 + 8);
        var c = String.fromCharCode(parseBinary(temp));
        result += c;
    }
    return result;
}

function getKey(v) {
    var result = "";
    switch (v) {
        case 1: result = "If-Match"; break;
        case 2: result = "Uri-Host"; break;
        case 4: result = "E-Tag"; break;
        case 5: result = "If-None-Match"; break;
        case 7: result = "Uri-Port"; break;
        case 8: result = "Location-Path"; break;
        case 11: result = "Uri-Path"; break;
        case 12: result = "Content-Format"; break;
        case 14: result = "Max-Age"; break;
        case 15: result = "Uri-Query"; break;
        case 16: result = "Accept"; break;
        case 17: result = "Location-Query"; break;
    }
    return result;
}

function getUrl() {
    return Options.get('Uri-Path') + (Options.has('Uri-Query') ? '&' + Options.get('Uri-Query') : '');
}

function initData() {
    Data = {
        'T' : T,
        'TKL' : TKL,
        'Code' : Code,
        'MessageId' : MessageId,
        'Token' : Token,
        'Options' : Options,
        'Payload' : Payload
    };
}