package com.example.connection.util.JsonParse.lex;

/**
 * 保存Token类型信息以及相关静态变量
 *
 */
public class TOK {
	public static final int STR = 0;
	public static final int NUM = 1;
	public static final int DESC = 2;
	public static final int SPLIT = 3;
	public static final int ARRS = 4;
	public static final int OBJS = 5;
	public static final int ARRE = 6;
	public static final int OBJE = 7;
	public static final int FALSE = 8;
	public static final int TRUE = 9;
	public static final int NIL = 10;
	public static final int BGN = 11;
	public static final int EOF = 12;
	/**
	 * 并非tok类型，存储tok类型的个数，添加类型时请同步修改
	 */
	public static final int TOK_NUM = 13;
	/**
	 * 将tok类型转换为字符串的转换数组，添加类型时请同步修改
	 */
	public static final String[] CAST_STRS = { "STR", "NUM", "DESC", "SPLIT",
			"ARRS", "OBJS", "ARRE", "OBJE", "FALSE", "TRUE", "NIL", "BGN", "EOF" };
	
	/**
	 * 将tok类型转换为字符串的转换数组，添加类型时请同步修改
	 */
	public static final String[] CAST_LOCAL_STRS = { "字符串", "数字", ":", ",",
			"[", "{", "]", "}", "false", "true", "null", "开始", "结束" };

	/**
	 * 将tok类型转换为String,测试用显示结果的
	 * 
	 * @return
	 */
	public static String castTokType2Str(int type) {
		if (type < 0 || type > TOK_NUM)
			return "undefine";
		else
			return CAST_STRS[type];
	}
	
	/**
	 * 将tok类型转换为String,用于报错信息
	 * 
	 * @return
	 */
	public static String castTokType2LocalStr(int type) {
		if (type < 0 || type > TOK_NUM)
			return "undefine";
		else
			return CAST_LOCAL_STRS[type];
	}
}
