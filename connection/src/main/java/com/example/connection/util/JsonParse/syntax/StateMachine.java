package com.example.connection.util.JsonParse.syntax;

import com.example.connection.util.JsonParse.lex.JsonLex;
import com.example.connection.util.JsonParse.lex.Token;
import com.example.connection.util.JsonParse.lex.UnexpectedException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 语法状态机，负责状态转换，以及相关操作的调用
 */
public class StateMachine {
	private JsonLex lex = null;
	private Operator opt = null;
	private Integer status = null;
	public StateMachine(String str){
		if (str == null)
			throw new NullPointerException("语法解析构造函数不能传递null");
		lex = new JsonLex(str);
		opt = new Operator(lex);
	}
	
	public Object parse(){
		Token tk = null;
		status = STATE.BGN;
		Integer oldStatus = status;
		while((tk=lex.next())!=Token.EOF){
			if(tk == null){
				throw lex.generateUnexpectedException("发现不能识别的token：“" + lex.getCurChar() + "”");
			}
			if(status == STATE.VAL || status == STATE.EOF || status == STATE.ERR){
				throw lex.generateUnexpectedException("当前状态【"+STATE.castLocalStr(oldStatus)+"】,期待【结束】;却返回"+tk.toLocalString());
			}
			oldStatus = status;
			status = STATE.STM[oldStatus][tk.getType()];
			if(status == STATE.ERR){
				throw lex.generateUnexpectedException("当前状态【"+STATE.castLocalStr(oldStatus)+"】,期待【"+(STATE.ETS[oldStatus]==null?"结束":STATE.ETS[oldStatus])+"】;却返回"+tk.toLocalString());
			}
			try {
				Method m = STATE.TKOL[tk.getType()];
				if(m!=null){//输入Token操作
					status = (Integer)m.invoke(opt, oldStatus, status, tk);
				}
				m = STATE.STOL[status];
				if(m!=null){//目标状态操作
					status = (Integer)m.invoke(opt, oldStatus, status, tk);
				}
			} catch (IllegalArgumentException e) {
				throw lex.generateUnexpectedException("【反射调用】传入非法参数",e);
			} catch (IllegalAccessException e) {
				throw lex.generateUnexpectedException("【反射调用】私有方法无法调用",e);
			} catch (InvocationTargetException e) {
				if(e.getTargetException() instanceof UnexpectedException){
					throw (UnexpectedException)e.getTargetException();
				}else{
					throw lex.generateUnexpectedException("运行时异常",e);
				}
			}
		}
		return opt.getCurValue();
	}
}
