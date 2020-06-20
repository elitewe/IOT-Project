package com.example.connection.util.JsonParse.lex;

/**
 * 保存token
 */
public class Token {
	public static final Token DESC = new Token(TOK.DESC); // 冒号
	public static final Token SPLIT = new Token(TOK.SPLIT); //分隔符(逗号)
	public static final Token ARRS = new Token(TOK.ARRS); //左中括号
	public static final Token OBJS = new Token(TOK.OBJS); //左大括号
	public static final Token ARRE = new Token(TOK.ARRE); //右中括号
	public static final Token OBJE = new Token(TOK.OBJE); //由大括号
	public static final Token FALSE = new Token(TOK.FALSE); //false
	public static final Token TRUE = new Token(TOK.TRUE); //true
	public static final Token NIL = new Token(TOK.NIL); //空
	public static final Token BGN = new Token(TOK.BGN); //开始
	public static final Token EOF = new Token(TOK.EOF); //结束

	// 从TOK类中定义的类型
	private Integer type;
	// 该tok的值
	private String value;

	public Token(int type) {
		this.type = type;
		this.value = null;
	}

	public Token(int type, String value) {
		this.type = type;
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static String unescape(String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '\\') {
				c = str.charAt(++i);
				switch (c) {
					case '"':
						sb.append('"');
						break;
					case '\\':
						sb.append('\\');
						break;
					case '/':
						sb.append('/');
						break;
					case 'b':
						sb.append('\b');
						break;
					case 'f':
						sb.append('\f');
						break;
					case 'n':
						sb.append('\n');
						break;
					case 'r':
						sb.append('\r');
						break;
					case 't':
						sb.append('\t');
						break;
					case 'u':
						String hex = str.substring(i+1, i+5);
						sb.append((char)Integer.parseInt(hex, 16));
						i+=4;
						break;
					default:
						throw new RuntimeException("“\\”后面期待“\"\\/bfnrtu”中的字符，结果得到“"+c+"”");
				}
			}else{
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	public Object getRealValue(){
		Object curValue = null;
		switch(this.getType()){
		case TOK.TRUE:
			curValue = true;
			break;
		case TOK.FALSE:
			curValue = false;
			break;
		case TOK.NIL:
			curValue = null;
			break;
		case TOK.NUM:
			if(value.indexOf('.')>=0){
				curValue = Double.parseDouble(value);
			}else{
				curValue = Integer.parseInt(value);
			}
			break;
		case TOK.STR:
			curValue = unescape(value);
			break;
		}
		return curValue;
	}

	public String toString() {
		if (this.type > 1) {
			return "[" + TOK.castTokType2Str(this.type) + "]";
		} else {
			return "[" + TOK.castTokType2Str(this.type) + ":" + this.value
					+ "]";
		}
	}
	
	public String toLocalString() {
		if (this.type > 1) {
			return "“" + TOK.castTokType2LocalStr(this.type) + "”";
		} else {
			return "“" + TOK.castTokType2LocalStr(this.type) + ":" + this.value
					+ "”";
		}
	}
}
