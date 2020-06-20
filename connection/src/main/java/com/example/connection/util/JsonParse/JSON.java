package com.example.connection.util.JsonParse;


import com.example.connection.util.JsonParse.lex.JsonLex;
import com.example.connection.util.JsonParse.lex.Token;
import com.example.connection.util.JsonParse.syntax.StateMachine;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON解析器的总入口,该JSON解析器为一个多次遍历解析器（结构清晰，但效率上多有折损），实现较为简单，以弄清原理为目标
 */
public class JSON {
	/**
	 * 解析json字符串返回List/Map的嵌套结构或值（true、false、number、null）
	 * @param str 要解析的json字符串
	 * @return 解析的结果对象
	 */
	public static Object parse(String str) {
		if (str == null || "".equals(str.trim()))
			return null;
		else
			return (new StateMachine(str)).parse();
	}

	public static void main(String[] args) {
		String str = "{a:abc}";
//		str = "{\"A\":\"120\"}"; //过滤掉了冒号
		str = "{\n" +
				"\tHOST:\"127.0.0.1:8090\",\n" +
				"\tAccept:text/pain,\n" +
				"\tAccept-Charset:utf-8,\n" +
				"\tAccept-Language:zh-CN,\n" +
				"\tContent-Type:application/json,\n" +
				"\tCookie:MessageID=jJib4fC7mYYhGFWK,\n" +
				"\tUser-Agent:Mozilla/5.0 Chrome/73.0.3683.103 Safari/537.36\n" +
				"}\n";

//		str = "{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\",\"key4\":[{\"a1\":\"1\",\"a2\":\"2\",\"a3\":\"3\",\"subChildA\":[{\"suba1\":\"3040\",\"suba2\":\"brebb\",\"suba3\":\"fbre\"},{\"suba1\":\"erbrrt\",\"suba2\":\"be4\",\"suba3\":\"5yh5\"},{\"suba1\":\"g445h\",\"suba2\":\"43th\",\"suba3\":\"r5yj4\"}],\"subChildB\":{\"suY1\":\"30L40\",\"suY2\":\"bre00bb\",\"suY3\":\"fbFGFre\",\"subChildA\":[{\"suba1\":\"3040\",\"suba2\":\"brebb\",\"suba3\":\"fbre\"},{\"suba1\":\"erbrrt\",\"suba2\":\"be4\",\"suba3\":\"5yh5\"},{\"suba1\":\"g445h\",\"suba2\":\"43th\",\"suba3\":\"r5yj4\"}]}},{\"a1\":\"s\",\"a2\":\"D\",\"a3\":\"F\"},{\"a1\":\"Q\",\"a2\":\"R\",\"a3\":\"T\"}],\"key5\":[{\"b1\":\"11\",\"b2\":\"21\",\"b3\":\"31\"},{\"b1\":\"3er\",\"b2\":\"3gt\",\"b3\":\"y7u\"},{\"b1\":\"H\",\"b2\":\"Y\",\"b3\":\"R\"}],\"key6\":\"uuid\",\"key7\":{\"vx1\":\"HwH\",\"vx2\":\"YrY\",\"vx3\":\"ReR\"}}";
//		str = "{\n\ta:[1,-23333,-0.3,0.17,5.2,\"\\u82B1\\u6979~\"],\n\tb:[\"a\tbc\",\"12  3\",\"4,5\\\"6\",{\n\t\t\t\t\tx:1,\n\t\t\t\t\ty:\"cc\\ncc\"\n\t\t\t\t},4.56],\n\t\"text\":\"I'm OK~\",\n\t\"1-2\":234,\n\tmybool:false,\n\tmynull:null,\n\tmyreal:true\n}\n";


		System.out.println(str);
		JsonLex jl =new JsonLex(str);
		Token tk = null;
		while ((tk = jl.next()) != Token.EOF) {
			System.out.println(tk);
		}
		Object o = JSON.parse(str);

		HashMap<String, String> hashMap = (HashMap<String, String>)o;

		System.out.println();
		String out = com.alibaba.fastjson.JSON.toJSONString(o);
		System.out.println(out);
		for (Map.Entry<String, String> entry : hashMap.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		System.out.println(hashMap.toString());
	}
}
