package com.alibaba.ml.timeout.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alibaba.ml.timeout.manager.ITimeOutManager;

/**功能说明：
 * @author:linghushaoxia
 * @time:2017年10月21日下午6:53:21
 * @version:1.0
 * 为中国羸弱的技术撑起一片自立自强的天空
 */
public class TimeOutConfigCache {
    	private int DEFAULT_TIMEOUT=1000;
	private ITimeOutManager timeOutManager;
	/**
	 * 配置分隔符
	 */
	private final static String spilt="@";
	/**
	 * 配置缓存
	 */
	private  Map<String,Set<String>> timeCodeMap = new HashMap<String, Set<String>>();
	/**
	 * 
	 * 功能说明:
	 * @param timeOutManager
	 * @time:2017年10月23日下午9:30:41
	 * @author:linghushaoxia
	 * @exception:
	 *
	 */
	public  void init(ITimeOutManager timeOutManager){
		this.timeOutManager=timeOutManager;
	}
	public Integer getTimeOutByConfig(String timeOutConfig){
		if (timeOutConfig!=null&&!timeOutConfig.equals("")) {
			String[] configs = timeOutConfig.split(TimeOutConfigCache.spilt);
			return this.timeOutManager.getTimeOut(configs[0],configs[1]);

		}
		return DEFAULT_TIMEOUT;
	}
	/**
	 * 
	 * 功能说明:
	 * @param className
	 * @param code
	 * @return boolean
	 * @time:2017年10月21日下午7:00:10
	 * @author:linghushaoxia
	 * @exception:
	 *
	 */
	public  boolean put(String className,String code){
		if (!timeCodeMap.containsKey(className)) {
			timeCodeMap.put(className,new HashSet<String>());
		}
		return timeCodeMap.get(className).add(code);
	}
	/**
	 * 
	 * 功能说明:存入配置值
	 * @param config
	 * 某个字段的全类名表示
	 * package.className@code
	 * @return boolean
	 * @time:2017年10月22日下午2:38:49
	 * @author:linghushaoxia
	 * @exception:
	 *
	 */
	public  boolean put(String config){
		String[] configs = config.split(TimeOutConfigCache.spilt);
		if (configs.length!=2) {
			return false;
		}
		return put(configs[0], configs[1]);
	}
	
	/**
	 * 
	 * 功能说明:
	 * @return Set<String>
	 * @time:2017年10月21日下午7:05:16
	 * @author:linghushaoxia
	 * @exception:
	 *
	 */
	public  Set<String> getClassNameSet(){
		return timeCodeMap.keySet();
	}
	/**
	 * 
	 * 功能说明:
	 * @return Collection<Set<String>>
	 * @time:2017年10月21日下午7:05:21
	 * @author:linghushaoxia
	 * @exception:
	 *
	 */
	public  Collection<Set<String>> getValues(){
		return timeCodeMap.values();
	}
	public  Set<String> getValues(String key){
		return timeCodeMap.get(key);
	}
	/**
	 * 
	 * 功能说明:
	 * @return Map<String,Set<String>>
	 * @time:2017年10月21日下午7:06:18
	 * @author:linghushaoxia
	 * @exception:
	 *
	 */
	public static TimeOutConfigCache getInstance(){
		return timeOutCacheHolder.instance;
	}
	private static class timeOutCacheHolder{
		private static TimeOutConfigCache instance = new TimeOutConfigCache();
	}
	
	
	
}

/**
* 现实就是实现理想的过程
*/