package com.alibaba.ml.timeout.manager;
/**功能说明：超时控制接口
 * @author:linghushaoxia
 * @time:2017年10月19日下午10:24:41
 * @version:1.0
 * 
 */
public interface ITimeOutManager {
	/**
	 * 
	 * 功能说明:根据超时控制的配置,获取具体的
	 * @param className
	 * 配置超时的类全名
	 * @param code
	 * 类的字段名
	 * @return long
	 * @time:2017年10月21日下午6:42:32
	 * @author:linghushaoxia
	 * @exception:
	 *
	 */
	public int getTimeOut(String className,String code);
}
