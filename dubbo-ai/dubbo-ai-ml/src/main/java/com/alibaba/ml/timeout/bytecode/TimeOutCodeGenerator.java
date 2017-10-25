package com.alibaba.ml.timeout.bytecode;

import java.util.Set;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.alibaba.ml.timeout.cache.TimeOutConfigCache;
import com.alibaba.ml.timeout.manager.ITimeOutManager;

/**功能说明：
 * @author:linghushaoxia
 * @time:2017年10月23日下午11:01:23
 * @version:1.0
 * 为中国羸弱的技术撑起一片自立自强的天空
 */
public class TimeOutCodeGenerator implements ApplicationListener<ContextRefreshedEvent>{

	public void onApplicationEvent(ContextRefreshedEvent event) {
		//启动完成，生成
		if (event.getApplicationContext().getParent()==null) {
			//生产实ITimeOutManager接口的方法代码
			String methodCode = geneMethodCode();
			//实例化
			String newClassName="com.alibaba.ml.timeout.manager.ITimeOutManager$Dynamic1";
			String interfaceName="com.alibaba.ml.timeout.manager.ITimeOutManager";
			try {
				ITimeOutManager timeOutManager = (ITimeOutManager) generateClass(methodCode, newClassName, interfaceName).newInstance();
				TimeOutConfigCache.getInstance().init(timeOutManager);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
		}
		
	}
	/**
	 * 
	 * 功能说明:根据代码生成类实例
	 * @param code
	 * @return Class<?>
	 * @time:2017年10月19日下午11:32:36
	 * @author:linghushaoxia
	 * @throws ClassNotFoundException 
	 * @exception:
	 *
	 */
	public static Class<?> generateClass(String methodCode,String newClassName,String interfaceName) throws NotFoundException,CannotCompileException, ClassNotFoundException{
		CtClass ctClass = null;

		ClassPool classPool = ClassPool.getDefault();
		ctClass = classPool.makeClass(newClassName);
		
		ctClass.addInterface(classPool.getCtClass(interfaceName));

		ctClass.addMethod(CtMethod.make(methodCode, ctClass));
		return ctClass.toClass(Thread.currentThread().getClass().getClassLoader(), Class.forName(interfaceName).getProtectionDomain());
	}
	private String geneMethodCode(){
	    	TimeOutConfigCache  timeOutConfigCache=TimeOutConfigCache.getInstance();
		Set<String> classNameSet=timeOutConfigCache.getClassNameSet();
		StringBuilder builder = new StringBuilder();
		builder.append(geneMethodHeader());
		for (String className : classNameSet) {
			builder.append(geneMethodContent(className, timeOutConfigCache.getValues(className)));
		}
		builder.append(geneMethodTail());
		return builder.toString();
	}
	private String geneMethodHeader(){
		StringBuilder builder = new StringBuilder();
		builder.append("	public int getTimeOut(String className,String code) {\n");
		builder.append(" 		int timeOut=1000;");
		return builder.toString();
	}
	private String geneMethodContent(String className,Set<String> fieldSet){
		StringBuilder builder = new StringBuilder();
		builder.append("\n 		if(\"").append(className).append("\".equals(className)){");
		for (String field : fieldSet) {
			builder.append("\n 			if(\"").append(field).append("\".equals(code)){\n");
			builder.append("\n 				return Integer.valueOf("+className+"."+field+"+\"\").intValue();");
			builder.append("\n			}");
		}
		builder.append("\n	}");
		return builder.toString();
	}
	private String geneMethodTail(){
		return " 	\n 		return timeOut;\n	}";
	}
}

/**
* 现实就是实现理想的过程
*/