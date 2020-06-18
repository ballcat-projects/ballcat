//package com.hccake.ballcat.codegen.controller;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
///**
// * @author Hccake
// * @version 1.0
// * @date 2020/6/18 13:46
// */
//@Slf4j
//@Controller
//@RequestMapping("/api")
//public class BackendController {
//
//	// Forwards all routes to FrontEnd except: '/', '/index.html', '/api', '/api/**'
//	// Required because of 'mode: history' usage in frontend routing, see README for further details
//	@RequestMapping(value = "{_:^(?!index\\.html|api).*$}")
//	public String redirectApi() {
//		log.debug("URL entered directly into the Browser, so we need to redirect...");
//		return "forward:/";
//	}
//
//}
