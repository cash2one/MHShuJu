/**
 * 当前库实现的基本功能： 0).Util: 基础库； 1).Util.ajax: 异步数据请求封装； 2).Util.date: 日期时间操作；
 * 3).Util.browser: 页面导航工具； 4).Util.vilidate: 常用正则表达式；
 */
(function() {
	var dm=document.documentMode;
	var ie=!-[1,];
	var ie6=ie&&!window.XMLHttpRequest;
	var ie7=ie&&dm==7;
	var ie8=ie&&dm==8;
	if(ie6){
		alert("您的IE浏览器版本过低，请升级到7以上版本！");
		return;
	}

	if (!window.Util) {
		window.Util = {};
	}
})();
Util = {
	/**
	 * 取消事件冒泡
	 * @param {Object}
	 *            e 事件对象
	 */
	stopBubble : function(e) {
		if (e && e.stopPropagation) {
			e.stopPropagation();
		} else {
			// ie
			window.event.cancelBubble = true;
		}
	},
	/**
	 * base64Md5加密
	 * @param {string}
	 * 		str
	 */
	base64Md5 : function(str){
		return CryptoJS.MD5(str);
	},
	/**
	 * 入参转码
	 * @param {string}
	 * 		json格式
	 */
	transCoding : function(json){
		var temp=encodeURIComponent(json);
		temp=CryptoJS.enc.Utf8.parse(temp);
		temp=CryptoJS.enc.Base64.stringify(temp);
		return temp;
	},
	/**
	 * 入参转码
	 * @param {string}
	 * 		json格式
	 */
	transDecoding : function(objStr){
		var words = CryptoJS.enc.Base64.parse(objStr);
		words = words.toString(CryptoJS.enc.Utf8);
		words = decodeURIComponent(words)
		return words;
	}
};
/**
 * 日期时间处理工具
 * 
 * @namespace Util
 * @class date
 */
Util.date = {
	/**
	 * 格式化日期时间字符串
	 * 
	 * @method dateTime2str
	 * @param {Date}
	 *            dt 日期对象
	 * @param {String}
	 *            fmt 格式化字符串，如：'yyyy-MM-dd hh:mm:ss'
	 * @return {String} 格式化后的日期时间字符串
	 */
	dateTime2str : function(dt, fmt) {
		var z = {
			M : dt.getMonth() + 1,
			d : dt.getDate(),
			h : dt.getHours(),
			m : dt.getMinutes(),
			s : dt.getSeconds()
		};
		fmt = fmt.replace(/(M+|d+|h+|m+|s+)/g, function(v) {
			return ((v.length > 1 ? "0" : "") + eval('z.' + v.slice(-1)))
					.slice(-2);
		});
		return fmt.replace(/(y+)/g, function(v) {
			return dt.getFullYear().toString().slice(-v.length);
		});
	},
	/**
	 * 根据日期时间格式获取获取当前日期时间
	 * 
	 * @method dateTimeWrapper
	 * @param {String}
	 *            fmt 日期时间格式，如："yyyy-MM-dd hh:mm:ss";
	 * @return {String} 格式化后的日期时间字符串
	 */
	dateTimeWrapper : function(fmt) {
		if (arguments[0])
			fmt = arguments[0];
		return this.dateTime2str(new Date(), fmt);
	},
	/**
	 * 获取当前日期时间
	 * 
	 * @method getDatetime
	 * @param {String}
	 *            fmt [optional,default='yyyy-MM-dd hh:mm:ss'] 日期时间格式。
	 * @return {String} 格式化后的日期时间字符串
	 */
	getDatetime : function(fmt) {
		return this.dateTimeWrapper(fmt || 'yyyy-MM-dd hh:mm:ss');
	},
	/**
	 * 获取当前日期时间+毫秒
	 * 
	 * @method getDatetimes
	 * @param {String}
	 *            fmt [optional,default='yyyy-MM-dd hh:mm:ss'] 日期时间格式。
	 * @return {String} 格式化后的日期时间字符串
	 */
	getDatetimes : function(fmt) {
		var dt = new Date();
		return this.dateTime2str(dt, fmt || 'yyyy-MM-dd hh:mm:ss') + '.'
				+ dt.getMilliseconds();
	},
	/**
	 * 获取当前日期（年-月-日）
	 * 
	 * @method getDate
	 * @param {String}
	 *            fmt [optional,default='yyyy-MM-dd'] 日期格式。
	 * @return {String} 格式化后的日期字符串
	 */
	getDate : function(fmt) {
		return this.dateTimeWrapper(fmt || 'yyyy-MM-dd');
	},
	/**
	 * 获取当前时间（时:分:秒）
	 * 
	 * @method getTime
	 * @param {String}
	 *            fmt [optional,default='hh:mm:ss'] 日期格式。
	 * @return {String} 格式化后的时间字符串
	 */
	getTime : function(fmt) {
		return this.dateTimeWrapper(fmt || 'hh:mm:ss');
	}
};
/**
 * 通过 HTTP 请求加载远程数据，底层依赖jQuery的AJAX实现。当前接口实现了对jQuery AJAX接口的进一步封装。
 */
Util.ajax = {
	/**
	 * 请求状态码
	 * 
	 * @type {Object}
	 */
	reqCode : {
		/**
		 * 成功返回码 1
		 * 
		 * @type {Number} 1
		 * @property SUCC
		 */
		SUCC : 1
	},
	/**
	 * 请求的数据类型
	 * 
	 * @type {Object}
	 * @class reqDataType
	 */
	dataType : {
		/**
		 * 返回html类型
		 * 
		 * @type {String}
		 * @property HTML
		 */
		HTML : "html",
		/**
		 * 返回json类型
		 * 
		 * @type {Object}
		 * @property JSON
		 */
		JSON : "json",
		/**
		 * 返回text字符串类型
		 * 
		 * @type {String}
		 * @property TEXT
		 */
		TEXT : "text"
	},
	/**
	 * 超时,默认超时30000ms
	 * 
	 * @type {Number} 10000ms
	 * @property TIME_OUT
	 */
	TIME_OUT : 60000,
	/**
	 * 显示请求成功信息
	 * 
	 * @type {Boolean} false
	 * @property SHOW_SUCC_INFO
	 */
	SHOW_SUCC_INFO : false,
	/**
	 * 显示请求失败信息
	 * 
	 * @type {Boolean} false
	 * @property SHOW_ERROR_INFO
	 */
	SHOW_ERROR_INFO : false,
	/**
	 * GetJson是对Util.ajax的封装,为创建 "GET" 请求方式返回 "JSON"(text) 数据类型
	 * @param {String}
	 *            url HTTP(GET)请求地址
	 * @param {Object}
	 *            cmd json对象参数
	 * @param {Function}
	 *            callback [optional,default=undefined] GET请求成功回调函数
	 */
	getJson : function(url, cmd, callback) {
		if (arguments.length !== 3)
			callback = cmd, cmd = '';
		dataType = this.dataType.TEXT;
		// var _this = this;
		// setTimeout( function(){_this.ajax(url, 'GET', cmd, dataType,
		// callback)},1000);
		this.ajax(url, 'GET', cmd, dataType, callback);
	},
	/**
	 * PostJsonAsync是对Util.ajax的封装,为创建 "POST" 请求方式返回 "JSON"(text) 数据类型,
	 * 采用同步阻塞的方式调用ajax
	 * @param {String}
	 *            url HTTP(POST)请求地址
	 * @param {Object}
	 *            cmd json对象参数
	 * @param {Function}
	 *            callback [optional,default=undefined] POST请求成功回调函数
	 */
	postJsonSync : function(url, cmd, callback) {
		dataType = this.dataType.TEXT;
		this.ajax(url, 'POST', cmd, dataType, callback, true);
	},
	/**
	 * PostJson是对Util.ajax的封装,为创建 "POST" 请求方式返回 "JSON"(text) 数据类型
	 * @param {String}
	 *            url HTTP(POST)请求地址
	 * @param {Object}
	 *            cmd json对象参数
	 * @param {Function}
	 *            callback [optional,default=undefined] POST请求成功回调函数
	 */
	postJson : function(url, cmd, callback) {
		dataType = this.dataType.TEXT;
		// var _this = this;
		// setTimeout( function(){_this.ajax(url, 'POST', cmd, dataType,
		// callback)},1000);
		this.ajax(url, 'POST', cmd, dataType, callback);
	},
	/**
	 * loadHtml是对Ajax load的封装,为载入远程 HTML 文件代码并插入至 DOM 中
	 * @param {Object}
	 *            obj Dom对象
	 * @param {String}
	 *            url HTML 网页网址
	 * @param {Function}
	 *            callback [optional,default=undefined] 载入成功时回调函数
	 */
	loadHtml : function(obj, url, data, callback) {
		$(obj).load(url, data, function(response, status, xhr) {
			callback = callback ? callback : function() {
			};
			status == "success" ? callback(true) : callback(false);
		});
	},
	/**
	 * loadTemp是对handlebars 的封装,请求模版加载数据
	 * @param {Object}
	 *            obj Dom对象
	 * @param {Object}
	 *            temp 模版
	 * @param {Object}
	 *            data 数据
	 */
	loadTemp : function(obj, temp, data) {
		var template = Handlebars.compile(temp.html());
		$(obj).html(template(data));
	},
	/**
	 * GetHtml是对Util.ajax的封装,为创建 "GET" 请求方式返回 "hmtl" 数据类型
	 * @param {String}
	 *            url HTTP(GET)请求地址
	 * @param {Object}
	 *            cmd json对象参数
	 * @param {Function}
	 *            callback [optional,default=undefined] GET请求成功回调函数
	 */
	getHtml : function(url, cmd, callback) {
		if (arguments.length !== 3)
			callback = cmd, cmd = '';
		dataType = this.dataType.HTML;
		this.ajax(url, 'GET', cmd, dataType, callback);
	},
	/**
	 * GetHtmlSync是对Util.ajax的封装,为创建 "GET" 请求方式返回 "hmtl" 数据类型
	 * 采用同步阻塞的方式调用ajax
	 * @param {String}
	 *            url HTTP(GET)请求地址
	 * @param {Object}
	 *            cmd json对象参数
	 * @param {Function}
	 *            callback [optional,default=undefined] GET请求成功回调函数
	 */
	getHtmlSync : function(url, cmd, callback) {
		if (arguments.length !== 3)
			callback = cmd, cmd = '';
		dataType = this.dataType.HTML;
		this.ajax(url, 'GET', cmd, dataType, callback,true);
	},
	/**
	 * 基于jQuery ajax的封装，可配置化
	 * 
	 * @method ajax
	 * @param {String}
	 *            url HTTP(POST/GET)请求地址
	 * @param {String}
	 *            type POST/GET
	 * @param {Object}
	 *            cmd json参数命令和数据
	 * @param {String}
	 *            dataType 返回的数据类型
	 * @param {Function}
	 *            callback [optional,default=undefined] 请求成功回调函数,返回数据data和isSuc
	 */
	ajax : function(url, type, cmd, dataType, callback, sync) {
		var param = "";
		if (typeof (cmd) == "object"){
			if(cmd.flg ==1){
				param = cmd.datas;
			}else{
				param = JSON.stringify(cmd);
			}
			
		}else if(typeof(cmd)=="string"){
			param = cmd;
		}
		//cmd = this.jsonToUrl(cmd);
		async = sync ? false : true;
		var thiz = Util.ajax;
		var cache = (dataType == "html") ? true : false;
		if(cmd.flg ==1){
		}else{
			param = encodeURI(param)
		}
		$.ajax({
			url : url,
			type : type,
			data : param,
			cache : cache,
			dataType : dataType,
			async : async,
			timeout : thiz.TIME_OUT,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=utf-8");
			},
			success : function(data) {
				if (!data) {
					return;
				}
				if (dataType == "html") {
					callback(data, true);
					return;
				}
				try {
					data = eval('(' + data + ')');
					if (data.rtnCode=='PAGEFRAME-9527') {
						alert("登陆凭证过期，请重新登陆");
						window.location.reload();
						return;
					}
				} catch (e) {
					alert("JSON Format Error:" + e.toString());
				}
				var isSuc = thiz.printReqInfo(data);
				if (callback && data) {
					callback(data || {}, isSuc);
				}
			},
			error : function() {
			    var retErr ={};
			    retErr['rtnCode']="SCRM-404";
			    retErr['rtnMsg']="网络异常或超时，请稍候再试！"; 
				callback(retErr, false);
			}
		});
	},
	/**
	 * 打开请求返回代码和信息
	 * 
	 * @method printRegInfo
	 * @param {Object}
	 *            data 请求返回JSON数据
	 * @return {Boolean} true-成功; false-失败
	 */
	printReqInfo : function(data) {
		if (!data)
			return false;
		var code = data.rtnCode, msg = data.rtnMsg, succ = this.reqCode.SUCC;
		if (code == succ) {
			if (this.SHOW_SUCC_INFO) {
				// Util.msg.infoCorrect([ msg, ' [', code, ']' ].join(''));
				Util.msg.infoCorrect(msg);
			}
		} else {
			// Util.msg.infoAlert([ msg, ' [', code, ']' ].join(''));
			if (this.SHOW_ERROR_INFO) {
				art.dialog.tips(msg);
			}
		}
		return !!(code == succ);
	},
	/**
	 * JSON对象转换URL参数
	 * 
	 * @method printRegInfo
	 * @param {Object}
	 *            json 需要转换的json数据
	 * @return {String} url参数字符串
	 */
	jsonToUrl : function(json) {
		var temp = [];
		for ( var key in json) {
			if (json.hasOwnProperty(key)) {
				var _key = json[key] + "";
				_key = _key.replace(/\+/g, "%2B");
				_key = _key.replace(/\&/g, "%26");
				temp.push(key + '=' + _key);
			}
		}
		return temp.join("&");
	},
	msg : {
		"suc" : function(obj, text) {
			var _text = text || "数据提交成功！";
			$(obj).html(
					'<div class="msg-hint">' + '<h3 title=' + _text
							+ '><i class="hint-icon hint-suc-s"></i>' + _text
							+ '</h3>' + '</div>').show();
		},
		"war" : function(obj, text) {
			var _text = text || "数据异常，请稍后尝试!";
			$(obj).html(
					'<div class="msg-hint">' + '<h3 title=' + _text
							+ '><i class="hint-icon hint-war-s"></i>' + _text
							+ '</h3>' + '</div>').show();
		},
		"err" : function(obj, text) {
			var _text = text || "数据提交失败!";
			$(obj).html(
					'<div class="msg-hint">' + '<h3 title=' + _text
							+ '><i class="hint-icon hint-err-s"></i>' + _text
							+ '</h3>' + '</div>').show();
		},
		"load" : function(obj, text) {
			var _text = text || "正在加载中，请稍候...";
			$(obj).html(
					'<div class="msg-hint">' + '<h3 title=' + _text
							+ '><i class="hint-loader"></i>' + _text + '</h3>'
							+ '</div>').show();
		},
		"inf" : function(obj, text) {
			var _text = text || "数据提交中，请稍等...";
			$(obj).html(
					'<div class="msg-hint">' + '<h3 title=' + _text
							+ '><i class="hint-icon hint-inf-s"></i>' + _text
							+ '</h3>' + '</div>').show();
		},
		"errorInfo" : function(obj, text) {
			var _text = text || "数据提交失败!";
			$(obj)
					.html(
							'<div class="ui-tiptext-container ui-tiptext-container-message"><p class="ui-tiptext ui-tiptext-message">'
									+ '<i class="ui-tiptext-icon icon-message" title="阻止"></i>'
									+ _text + '</p>' + '</div>').show();
		}
	}
};

Util.browser = {
	/**
	 * 获取URL地址栏参数值
	 * name 参数名
	 * url [optional,default=当前URL]URL地址
	 * @return {String} 参数值
	 */
	getParameter : function(name, url) {
		var paramStr = url || window.location.search;
		if (paramStr.length == 0) {
			return null;
		}
		if (paramStr.charAt(0) != "?") {
			return null;
		}
		paramStr = unescape(paramStr).substring(1);
		if (paramStr.length == 0) {
			return null;
		}
		var params = paramStr.split('&');
		for ( var i = 0; i < params.length; i++) {
			var parts = params[i].split('=', 2);
			if (parts[0] == name) {
				if (parts.length < 2 || typeof (parts[1]) === "undefined"
						|| parts[1] == "undefined" || parts[1] == "null")
					return '';
				return parts[1];
			}
		}
		return null;
	}
};
/**
 * 常用正则表达式
 */
Util.validate = {
	regexp : {
		intege : "^-?[1-9]\\d*$", // 整数
		intege1 : "^[1-9]\\d*$", // 正整数
		intege2 : "^-[1-9]\\d*$", // 负整数
		num : "^([+-]?)\\d*\\.?\\d+$", // 数字
		num1 : "^[1-9]\\d*|0$", // 正数（正整数 + 0）
		num2 : "^-[1-9]\\d*|0$", // 负数（负整数 + 0）
		decmal : "^([+-]?)\\d*\\.\\d+$", // 浮点数
		decmal1 : /^[0-9]*.?\d*$/, // 正浮点数
		decmal2 : "^-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)$", // 负浮点数
		decmal3 : "^-?([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0)$", // 浮点数
		decmal4 : "^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0$", // 非负浮点数（正浮点数 +
																// 0）
		decmal5 : "^(-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*))|0?.0+|0$", // 非正浮点数（负浮点数
																	// + 0）
		email : /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i, // 邮件
		color : "^[a-fA-F0-9]{6}$", // 颜色
		url : "^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$", // url
		chinese : "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$", // 仅中文
		ascii : "^[\\x00-\\xFF]+$", // 仅ACSII字符
		zipcode : "^\\d{6}$", // 邮编
		ip4 : "^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$", // ip地址
		picture : "(.*)\\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga)$", // 图片
		rar : "(.*)\\.(rar|zip|7zip|tgz)$", // 压缩文件
		date : "^\\d{4}(\\-|\\/|\.)\\d{1,2}\\1\\d{1,2}$", // 日期
		qq : "^[1-9]*[1-9][0-9]*$", // QQ号码
		mobile :"/^1[3|4|7|5|8][0-9]\d{4,8}$/",
		tel : "^(([0\\+]\\d{2,3}(-)?)?(0\\d{2,3})(-)?)?(\\d{7,8})(-(\\d{3,}))?$", // 电话号码的函数(包括验证国内区号,国际区号,分机号)
		name : "^[\\u4E00-\\u9FA5\\uF900-\\uFA2Da-zA-Z]([\\s.]?[\\u4E00-\\u9FA5\\uF900-\\uFA2Da-zA-Z]+){1,}$", // 真实姓名由汉字、英文字母、空格和点组成，不能以空格开头至少两个字
		addressname : "^[\\u4E00-\\u9FA5\\uF900-\\uFA2Da-zA-Z]{1,}$", // 收货人
		username : "^[0-9a-zA-Z_\u0391-\uFFE5]{3,15}$", // 用来用户注册。匹配由数字、26个英文字母中文或者下划线组成的字符串
														// 3-15个字符串之间
		letter : "^[A-Za-z]+$", // 字母
		letter_u : "^[A-Z]+$", // 大写字母
		letter_l : "^[a-z]+$", // 小写字母
		idcard : "^[1-9]([0-9]{14}|[0-9]{16}[0-9xX])$", // 身份证
		passwrd : "^[\\w-@#$%^&*]{6,20}$", // 密码保证6-20位的英文字母/数字/下划线/减号和@#$%^&*这些符号
		scripts : "[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）&mdash;—|{}【】‘；：”“'。，、？]", // 特殊字符
		notempty : function(value) {
			return value.length > 0;
		}
	},
	/**
	 * 格式校验方法
	 * 
	 * @method Check
	 * @param {String}
	 *            type 验证类型
	 * @param {String}
	 *            value 验证值
	 */
	Check : function(type, value) {
		var _reg = this.regexp[type];
		if (_reg == undefined) {
			alert("Type " + type + " is not in the data");
			return false;
		}
		var reg;
		if (typeof _reg == "string") {
			reg = new RegExp(_reg);
		} else if ((typeof _reg) == "function") {
			return _reg(value);
		} else {
			reg = _reg[type];
		}
		return reg.test(value);
	}
};
Util.sms = {};
Util.sms.formatStr = function(value) {
    if (value) {	
        if (arguments.length > 1) {
            for (var i = 1; i < arguments.length; i++) {
                value = value.replace(new RegExp("\\{" + (i - 1) + "\\}", 'g'), arguments[i]);
            }
        }
    }
    return value;
};

$(function(){
	srvMap.addLogin('loginUserInfo', 'loginUserInfo.json','isLogin.action?uid=isLogin');//获取登录用户的信息
	srvMap.add('loginout', 'loginUserInfo.json','commonCMS-Logout.action?uid=Logout');//退出
	
	//已登录状态的页面显示
	if ($('#J_search_ipt').length) {
		try{
			//生成登录后已经订购的产品跳转url
			Handlebars.registerHelper('forwardUrl', function(sysType, nodeUid){
			    return srvMap.getBaseUrl() + "edu_forward.jsp?nodeUid="+nodeUid+"&sysType="+sysType;
			});
		}catch(e){}
		
		
		Util.ajax.postJsonSync(srvMap.get('loginUserInfo'),'',function(json,status){
			if (status) {
				window.loginUserInfo = json;
				if ($('.ui-head .ui-hello').length>0) {
					//typeid：2老师，3家长
					var role = '';
					if (json.bean.typeid=='2') {
						role = '老师';
					}else if (json.bean.typeid=='99') {
						role = '游客';
					}else{
						role = '家长/学生';
					}
					$('.ui-head .ui-hello').html('您好，<span style="color:#E85C73;font-weight:700;">'+loginUserInfo.bean.mobile+'</span>，您当前的角色是：<span style="color:#E85C73;font-weight:700;">'+role+'</span>。');
				}
				//购物车按钮中的产品数量
				if ($('.ui-myCart span').length) {
					$('.ui-myCart span').text(json.bean.favoritesize);
				};
			}else{
				window.loginUserInfo = '';
			}
		})
	};


	//头部搜索菜单自动补全
	if ($("#J_search_ipt").length) {
		srvMap.add('autoComplete', 'autoComplete.json','commonCMS-AllApps.action?uid=cmsA');//自动补全
		Util.ajax.postJson(srvMap.get('autoComplete'),'',function(json,status){
			if (status) {
				var html = '{{#if hotkeywords.length}}'+
								'<li style="cursor:text;width:75px;margin-right:0px;">热门关键词：</li>'+
								'{{#each hotkeywords}}'+
									'<li>{{#if_eq byOrdered compare="1"}}<a href="{{#forwardUrl sysType nodeUid}}{{/forwardUrl}}" target="_blank" title="{{appName}}">{{appName}}</a>{{else}}<a href="prodDtl.html?appID={{appID}}&nodeUid={{nodeUid}}" title="{{appName}}" target="_blank">{{appName}}</a>{{/if_eq}}</li>'+
								'{{/each}}'+
							'{{/if}}';
				if ($('#J_hot_kwd').length) {
					var template = Handlebars.compile(html);
					$('#J_hot_kwd').html(template(json));
				}
				/*	jQuery UI Autocomplete 插件使用注意：
					对于JSON格式要求有：label、value属性，其中label属性用于显示在autocomplete弹出菜单，而value属性则是选中后给文本框赋的值。
					格式：[ {label: "test", value: "test"}, {label: "test", value: "test"} , ... ]
				 */
				$("#J_search_ipt").autocomplete({
					minLength: 1,
					autoFocus: true, 
					source: json.rows,
					select: function( event, ui ) {
						if (ui.item.byOrdered == '1') {
							var opurl = srvMap.getBaseUrl() + "edu_forward.jsp?nodeUid="+ui.item.nodeUid+"&sysType="+ui.item.sysType;
							window.open(opurl);
							//window.open(ui.item.apphref);
						}else if(ui.item.appID){
							window.open("prodDtl.html?appID="+ui.item.appID+"&nodeUid="+ui.item.nodeUid);
						}
						return false;
					},
					response: function(event, ui){
						if (!ui.content.length) {
							/***
							var _left = parseInt($("#J_search_ipt").offset().left, 10)+1;
							var _top = parseInt($("#J_search_ipt").offset().top + $("#J_search_ipt").height(), 10)+20;
							$('#ui-id-1').css({
								'display': 'block',
								'width': '465px',
								'position': 'absolute',
								'top': _top+"px",
								'left': _left+"px"}).html('<li><a href="javascript:;">对不起，没有查到数据！</a></li>');
							****/
							ui.content.push({value:'对不起，没有查到数据！'});	
						}
					},
					close: function(event, ui){
						var _this = $(this);
						if (_this.val()) {
							$('#ui-id-1').show();
						}else{
							$('#ui-id-1').hide();
						}
						$("#J_search_ipt").blur(function(){
							$('#ui-id-1').hide();
						})
						$("#J_search_ipt").focus(function(){
							if (_this.val()) {
								$('#ui-id-1').show();
							}
						})
					}
				}).data("ui-autocomplete")._renderItem = function( ul, item ) {
					return $( "<li>" ).append( "<a>"+item.value+"</a>" ).appendTo( ul ); 
				};
			}else{
				$('#J_index_head').html('<div class="ui-bindInfo-error">'+json.rtnMsg||'连接失败，请重试！'+'</div>')
			}
		})
	}
	//角色切换
	if ($('.ui-nav-choiceRole').length||$('.ui-pers-choiceRole').length) {
		srvMap.add('switchRoleUrl', 'switchRole.json','forwordTo.action?redirect=choiceRole.html&forword=choiceRole');
		$('.ui-nav-choiceRole,.ui-pers-choiceRole').click(function(){
			if (loginUserInfo.bean.isMulRole>0) {
				//window.location.href = '../hn/action/forwordTo.action?redirect=choiceRole.html&forword=choiceRole';
				window.location.href = srvMap.get('switchRoleUrl');
			}else{
				art.dialog.tips('您当前只有一个角色，无需切换！');
			}
		})
	};
	//和教育
	if ($('.ui-nav-hej a').length){
		srvMap.add('heEduUrl', 'heEdu.json','gotoHeEdu');
		//$('.ui-nav-hej a').attr('href',srvMap.get('heEduUrl'));
		$('.ui-nav-hej a').click(function(){
			Util.ajax.postJson(srvMap.get('heEduUrl'),'',function(json,status){
				if(json.ret == 0){
					window.location.href = json.msg;
				}else {
					art.dialog.tips(json.msg);
				}
			});
		});
	}
	//我的收藏
	if ($('.ui-nav-collect a').length){
		srvMap.add('collectUrl', 'collect.json','forwordTo.action?redirect=myCollect.html&forword=myCollect');
		$('.ui-nav-collect a').attr('href',srvMap.get('collectUrl'));
	}
	//我的订单
	if ($('.ui-nav-app a').length){
		srvMap.add('myordersUrl', 'myorders.json','forwordTo.action?redirect=myOrder.html&forword=myOrder');
		$('.ui-nav-app a').attr('href',srvMap.get('myordersUrl'));
	}
	//我的购物车
	if ($('.ui-myCart').length){
		srvMap.add('myCarUrl', 'mycar.json','forwordTo.action?redirect=sCart.html&forword=sCart');
		$('.ui-myCart').attr('href',srvMap.get('myCarUrl'));
	}
	//退出按钮
	if ($('.ui-nav-loginout a').length){
		var isLogin = 0;
		try{
			isLogin = loginUserInfo.rtnCode;//是否登录标志位  1：已登录
		}catch(e){}
		if (isLogin == '1') {
			$('.ui-nav-loginout a').attr('href',srvMap.get('loginout'));
		}else{
			$('.ui-nav-loginout a').attr('href','#');
		}
	}
	/*if ($('.ui-nav-loginout a').length) {
		var isLogin =loginUserInfo.rtnCode;//是否登录标志位  1：已登录
		if (isLogin == '1') {
			$('.ui-nav-loginout a').click(function(){
				Util.ajax.postJson(srvMap.get('loginout'),'',function(json,status){
					if (status) {
						art.dialog.tips('退出成功！');
						var timer = 1;
						time = setInterval(function(){
							if (timer>0) {
								timer--;
							}else{
								clearInterval(time);
							}
						},1000)
					}else{
						art.dialog.tips('退出失败，请重试！')
					}
				})
			})
		}
	}*/
})

//对Cookie进行操作
function setCookie(name,value,expires,path,domain,secure){   
	var today = new Date();
	today.setTime(today.getTime());
	if(expires){
	  expires = expires * 1000 * 60 * 60 *24;
	}
	var expires_date = new Date(today.getTime() + (expires));
	value = encodeURIComponent(unescape(value));
	document.cookie = name+'='+value+
	( ( expires ) ? ';expires='+expires_date.toGMTString() : '' ) +
	( ( path ) ? ';path=' + path : '' ) +
	( ( domain ) ? ';domain=' + domain : '' ) +
	( ( secure ) ? ';secure' : '' );
};

function getCookie(name){
	var start = document.cookie.indexOf(name+"=");
	var len = start+name.length+1;
	if ((!start) && (name != document.cookie.substring(0,name.length))) return null;
	if (start == -1) return null;
	var end = document.cookie.indexOf(";",len);
	if (end == -1) end = document.cookie.length;
	return decodeURIComponent(document.cookie.substring(len,end));
};