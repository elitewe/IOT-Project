package com.example.connection.util.JsonParse.lex;

import java.util.Stack;


/**
 * Json词法分析器
 */
public class JsonLex {
	// 当前行号
	private int lineNum = 0;
	// 用于记录每一行的起始位置
	private Stack<Integer> colMarks = new Stack<Integer>();
	// 用于报错的行游标
	private int startLine = 0;
	// 用于报错的列游标
	private int startCol = 0;
	// 当前字符游标
	private int cur = -1;
	// 保存当前要解析的字符串
	private String str = null;
	// 保存当前要解析的字符串的长度
	private int len = 0;

	/**
	 * JsonLex构造函数
	 * 
	 * @param str
	 *            要解析的字符串
	 */
	public JsonLex(String str) {
		if (str == null)
			throw new NullPointerException("词法解析构造函数不能传递null");
		this.str = str;
		this.len = str.length();
		this.startLine = 0;
		this.startCol = 0;
		this.cur = -1;
		this.lineNum = 0;
		this.colMarks.push(0);
	}

	public char getCurChar(){
		if (cur >= len - 1) {
			return 0;
		}else{
			return str.charAt(cur);
		}
	}

	public Token parseSymbol(char c) {
		switch (c) {
		case '[':
			return Token.ARRS;
		case ']':
			return Token.ARRE;
		case '{':
			return Token.OBJS;
		case '}':
			return Token.OBJE;
		case ',':
			return Token.SPLIT;
		case ':':
			return Token.DESC;
		}
		return null;
	}

	public boolean isLetterUnderline(char c) {
		return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_');
	}

	public boolean isNumLetterUnderline(char c) {
		return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
				|| (c >= '0' && c <= '9') || c == '_')
				|| c == '-' || c == '/' || c == '=' || c == '.' || c == ' '; //额外添加，用于解析http报文
	}

	public boolean isNum(char c) {
		return (c >= '0' && c <= '9');
	}

	public boolean isDecimal(char c) {
		return ((c >= '0' && c <= '9') || (c == '.'));
	}

	private void checkEnd() {
		if (cur >= len - 1) {
			throw generateUnexpectedException("未预期的结束，字符串未结束");
		}
	}

	public UnexpectedException generateUnexpectedException(String str) {
		return new UnexpectedException(cur,startLine, startCol, str);
	}

	public UnexpectedException generateUnexpectedException(String str,
			Throwable e) {
		return new UnexpectedException(cur,startLine, startCol, str, e);
	}

	private String getStrValue(char s) {
		int start = cur;
		char c;
		while ((c = nextChar()) != 0) {
			if (c == '\\') {// 跳过斜杠以及后面的字符
				c = nextChar();
			} else if (s == c) {
				return str.substring(start + 1, cur);
			}
		}
		checkEnd();
		return null;
	}

	private String getNumValue() {
		int start = cur;
		char c;
		while ((c = nextChar()) != 0) {
			if (!isDecimal(c)) { //碰到不是点和数字的字符就结束循环
				return str.substring(start, revertChar());
			}
		}
		checkEnd();
		return null;
	}

	private Token getDefToken() {
		int start = cur;
		char c;
		while ((c = nextChar()) != 0) {
			if (!isNumLetterUnderline(c)) { //碰到第一个不是字母、数字、下划线的字符就结束循环
				String value = str.substring(start, revertChar());
				if ("true".equals(value)) {
					return Token.TRUE;
				} else if ("false".equals(value)) {
					return Token.FALSE;
				} else if ("null".equals(value)) {
					return Token.NIL;
				} else {
					return new Token(TOK.STR, value);
				}
			}
		}
		checkEnd();
		return null;
	}

	/**
	 * 获取下一个字节，同时进行 行、列 计数
	 * 
	 * @return 下一个字节，结束时返回0
	 */
	private char nextChar() {
		if (cur >= len-1) {
			return 0;
		}
		++cur;
		char c = str.charAt(cur);
		if (c == '\n') {
			++lineNum;
			colMarks.push(cur);
		}
		return c;
	}
	/**
	 * 撤回一个字节，同时进行 行、列 计数，返回撤回前的字符游标
	 * 
	 * @return 下一个字节，结束时返回0
	 */
	private int revertChar() {
		if (cur <= 0) {
			return 0;
		}
		int rcur = cur--;
		char c = str.charAt(rcur);
		if (c == '\n') {
			--lineNum;
			colMarks.pop();
		}
		return rcur;
	}

	public static boolean isSpace(char c) {
		return (c == ' ' || c == '\t' || c == '\n');
	}

	// str \"(\\\"|[^\"])*\"
	// def [_a-zA-Z][_a-zA-Z0-9]*
	// num -?[0-9]+(\.[0-9]+)?
	// space [ \t\n]+
	/**
	 * 获取下一个Token的主函数
	 */
	public Token next() {
		if (lineNum == 0) {
			lineNum = 1;
			return Token.BGN;
		}
		char c;
		while ((c = nextChar()) != 0) {
			startLine = lineNum;
			startCol = getColNum();
			if (c == '"' || c == '\'') {
				return new Token(TOK.STR, getStrValue(c));
			} else if (isLetterUnderline(c)) {
				return getDefToken();
			} else if (isNum(c) || c=='-') {
				return new Token(TOK.NUM, getNumValue());
			} else if (isSpace(c)) {
				continue;
			} else {
				return parseSymbol(c);
			}
		}
		if (c == 0) {
			return Token.EOF;
		}
		return null;
	}

	public int getLineNum() {
		return lineNum;
	}

	public int getColNum() {
		return cur - colMarks.peek();
	}

	public int getCur() {
		return cur;
	}

	public String getStr() {
		return str;
	}

	public int getLen() {
		return len;
	}
}
