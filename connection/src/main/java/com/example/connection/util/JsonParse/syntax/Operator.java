package com.example.connection.util.JsonParse.syntax;


import com.example.connection.util.JsonParse.lex.JsonLex;
import com.example.connection.util.JsonParse.lex.TOK;
import com.example.connection.util.JsonParse.lex.Token;

import java.util.*;

/**
 * 该类负责实际操作
 */
public class Operator {
	private JsonLex lex = null;
	private Stack<Integer> statusStack = new Stack<Integer>();
	private Object curValue = null;
	private Stack<Object> keyStack = new Stack<Object>();
	private Stack<Object> objStack = new Stack<Object>();
	private Object curObj = null;

	public Object getCurObj() {
		return curObj;
	}

	public Operator(JsonLex lex) {
		this.lex = lex;
	}

	public Object getCurValue(){
		return curValue;
	}
	
	public Integer objs(Integer from, Integer to, Token input) {
		if (from != STATE.BGN) {
			statusStack.push(from);
		}
		curObj = new HashMap<Object, Object>();
		objStack.push(curObj);
		return to;
	}
	
	public Integer arrs(Integer from, Integer to, Token input) {
		if (from != STATE.BGN) {
			statusStack.push(from);
		}
		curObj = new ArrayList<Object>();
		objStack.push(curObj);
		return to;
	}

	@SuppressWarnings("unchecked")
	public Integer val(Integer from, Integer to, Token input) {
		switch (input.getType()) {
			case TOK.ARRE:
			case TOK.OBJE:
				curObj = objStack.pop();
				curValue = curObj;
				break;
			case TOK.TRUE:
			case TOK.FALSE:
			case TOK.NIL:
			case TOK.NUM:
			case TOK.STR:
				curValue = getRealValue(input);
				break;
		}
		if (statusStack.isEmpty()) {
			return STATE.EOF;
		}else{
			Integer s = statusStack.pop();
			if (s == STATE.ARRBV) {
				curObj = objStack.peek();
				((List<Object>) curObj).add(curValue);
				s = STATE.ARRAV;
			} else if (s == STATE.OBJBV) {
				curObj = objStack.peek();
				((Map<Object, Object>) curObj).put(keyStack.pop(), curValue);
				s = STATE.OBJAV;
			}
			return s;
		}
	}

	private Object getRealValue(Token input) {
		Object value = null;
		try {
			value = input.getRealValue();
		} catch (RuntimeException e) {
			lex.generateUnexpectedException("字符串转换错误", e);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public Integer arrav(Integer from, Integer to, Token input) {
		curValue = getRealValue(input);
		((List<Object>) curObj).add(curValue);
		return to;
	}

	public Integer objak(Integer from, Integer to, Token input) {
		keyStack.push(getRealValue(input));
		return to;
	}

	@SuppressWarnings("unchecked")
	public Integer objav(Integer from, Integer to, Token input) {
		curValue = getRealValue(input);
		((Map<Object, Object>) curObj).put(keyStack.pop(), curValue);
		return to;
	}
}
