package com.staryea.util.web;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 通用访问拦截匹配
 * 创建者 伍朝飞
 * 创建时间	2019-08-29
 */
@Controller
public class IndexController {
	
	/**
	 * 页面跳转
	 * @param url
	 * @return
	 */
	@RequestMapping("{url}.shtml")
	public String page(@PathVariable("url") String url) {
		return  url;
	}
	/**
	 * 页面跳转(二级目录)
	 * @param module
	 * @param url
	 * @return
	 */
	@RequestMapping("{module}/{url}.shtml")
	public String page(@PathVariable("module") String module,@PathVariable("url") String url) {
		return module + "/" + url;
	}
	
}
