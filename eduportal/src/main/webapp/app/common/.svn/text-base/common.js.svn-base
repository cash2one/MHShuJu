/**
 * lStorage 本地数据
 * 
 * userInfo：当前用户信息
 * examDatasSearch：某次考试数据
 * examDatas：考试历史成绩
 * hmwkDatas：作业
 * noticeDtl：公告
 * comuniDtl：沟通详情
 * forgetPwdPhone：忘记密码手机号码
 * studyDtl：产品详情
 * registerPhone：注册手机号码
 * billResult：产品订单结果
 * appDatas：产品订单信息
 * comuniDes：会话描述
 * autoLogin：自动登录
 * loginInfo：登录信息
 * userInfoHasRole：是否有角色
 * fromRedirect：是否跳转
 * 
 * 退出登录时统一清除
 * **/

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
	},
	/**
	 * base64Md5加密
	 * @param {string}
	 * 		str
	 */
	base64Md5 : function(str){
		return CryptoJS.MD5(str);
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
	postJson : function(url, cmd, callback,flag) {
        if(!flag){Util.loading.showLoading();}
		dataType = this.dataType.TEXT;
		// var _this = this;
		// setTimeout( function(){_this.ajax(url, 'POST', cmd, dataType,
		// callback)},1000);
		this.ajax(url, 'POST', cmd, dataType, callback,'',flag);
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
	ajax : function(url, type, cmd, dataType, callback, sync,flag) {
		var param = "";
		if (typeof (cmd) == "object"){
			param = JSON.stringify(cmd);
		}else if(typeof(cmd)=="string"){
			param = cmd;
		}
		//cmd = this.jsonToUrl(cmd);
		async = sync ? false : true;
		var thiz = Util.ajax;
		var cache = (dataType == "html") ? true : false;
		$.ajax({
			url : url,
			type : type,
			data : encodeURI(param),
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
					//alert("JSON Format Error:" + e.toString());
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
			},
            complete:function(){
                if(!flag){Util.loading.hideLoading();}
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
		paramStr = paramStr.split('?')[1];
		if (paramStr.length == 0) {	return null;}
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


Util.lStorage = {
    /*
    localStorage只支持字符串，如果要放json，请先使用JSON.stringify()转换
    读取使用JSON.parse()读取
    */
    setParam: function(name, value) {
        localStorage.setItem(name, value);
    },
    getParam: function(name) {
        return localStorage.getItem(name);
    },
    removeParam:function(name){
    	localStorage.removeItem(name);
    },
    clearParam:function(){
    	//清除所有的存储，谨慎使用
    	localStorage.clear();
    },
    paramSize:function(){
    	return localStorage.length;
    },
    /*
        离线缓存管理器
    */
    cacheManager:new CacheManager(window.cacheCfg)
}

Util.sStorage = {
    /*
    sessionStorage只支持字符串，如果要放json，请先使用JSON.stringify()转换
    读取使用JSON.parse()读取
    */
    setParam: function(name, value) {
        sessionStorage.setItem(name, value);
    },
    getParam: function(name) {
        return sessionStorage.getItem(name);
    },
    removeParam:function(name){
    	sessionStorage.removeItem(name);
    },
    clearParam:function(){
    	//清除所有的存储，谨慎使用
    	sessionStorage.clear();
    },
    paramSize:function(){
    	return sessionStorage.length;
    }
}


Util.loading = {
	showLoading:function(text){
	    if(!text)text='正在加载数据...';
	    $('#J_loading .nb-ui-loader-verbose').html(text);
	    $('#J_loading').removeClass('fn-hide');
	},
	hideLoading:function(){
	    $('#J_loading').addClass('fn-hide');
	}
}
/*
    离线缓存管理器
*/
function CacheManager(config){
    this.config=config;
}
/*
    从离线缓存中获取数据,当前方法有两个功能：
    1、从后台获取数据，第二个参数是一个回调函数，
    当离线缓存中没有要获取的数据时 或 当请求后台的入参值改变时，
    调用update方法从后台获取新数据，并覆盖旧数据
    2、获取本地插入的数据，第二个参数是一个字符串，
    当离线缓存中没有数据时直接返回空
*/
CacheManager.prototype.get=function(name,callback,param){
    var target=this.config[name];
    var cacheKey=target.key;
    var json=Util.lStorage.getParam(cacheKey);
    //当callback是方法时，表示从后台获取数据，需要使用回调处理数据
    if(typeof callback == 'function'&&target['url']){
        //realTime是在config对象中配置，
        //如果realTime配置true,表示每次都从后台取数据
        if(json&&!target.realTime){
            json=JSON.parse(json);
            if(json.param==param){
                callback('success',json);
                return;
            }
        }
        this.update(name,callback,param);
        return;
    }else{
        //callback不是方法的时候，即时返回数据
        return json;
    }
}
CacheManager.prototype.update=function(name,callback,param){
    var target=this.config[name];
    var cacheKey=target.key;
    //当callback是方法时，表示从后台刷新数据
    if(typeof callback == 'function'&&target.url){
        var _self = this;
        $.PostJson(target.url,param,function(state,json){
            if(state=='success'&&json.returnCode=='0'){
                json.param=param;
                Util.lStorage.setParam(cacheKey,JSON.stringify(json));
            }else{
                _self.del(name);
            }
            callback(state,json);
        },true);
    }else{
        //此时callback是字符串
        Util.lStorage.setParam(cacheKey,callback);
    }
}
CacheManager.prototype.del=function(name){
    var target=this.config[name];
    var cacheKey=target.key;
    Util.lStorage.removeParam(cacheKey);
}
CacheManager.prototype.clearAll=function(){
    for(var attr in this.config){
        var target=this.config[attr];
        var cacheKey=target.key;
        Util.lStorage.removeParam(cacheKey);
    }
}

Util.alert = {};
Util.alert = function(msg){
	try{
		mui.toast(msg);
	}catch(e){
		alert(msg);
	}
    /*var alert_html ='<div class="overlay" id="overlay">'+
					'<section class="modal1 modal-test" style="display:none;">'+
					'<div class="modal-hd">提示</div>'+
					'<div class="modal-bd">'+
					'<p id="J_alertContent"></p>'+
					'</div>'+
					'<div class="modal-ft">'+
					'<span class="btn-modal btn-cancel" style="width:100%;">确认</span>'+
					'</div>'+
					'</section>'+
					'</div>';
    if ($('.content #overlay').length == 0) {
        $('.content').append(alert_html);
    };
	var $overlay = $('#overlay');
	var $whichModal = $('.modal-test');
    $('.btn-cancel').unbind('click');
    $('.btn-cancel').click(function(e){
    	if (callback) { docbk(); };
    	modalHidden($whichModal);
    	e.stopPropagation();
    });

    $overlay.unbind('click');
    $overlay.click(function(e){
    	if(e.target.classList.contains('overlay')){
    		modalHidden($whichModal);
    	}
    });
    $('#J_alertContent').text(msg);
	$overlay.addClass('active');
	$whichModal.show();
	$whichModal.animate({display:"block"},100,function(){
		$(this).addClass('modal-in');
	});*/
}

/*function modalHidden($ele) {
	var $overlay = $('#overlay');
	$ele.removeClass('modal-in');
	$ele.one('transitionend',function(){
		$ele.css({"display": "none"});
		$overlay.removeClass('active');
	});
	setTimeout(function(){
		if($overlay.hasClass('active'))
			$overlay.removeClass('active');
	}, 500);
}*/


var templates = {};
var getTemplate = function(name, header, content, loading) {
	var template = templates[name];
	if (!template) {
		//预加载共用父模板；
		var headerWebview = mui.preload({
			url:header,
			id:name+"-main",
			styles:{
				popGesture:"hide",
			},
			extras:{
				mType: 'main'
			}
		});
		
		//预加载共用子webview
		var subWebview = mui.preload({
			url:!content?"":content,
			id:name+"-sub",
			styles:{
				top: '45px',
				bottom: '0px',
			},
			extras:{
				mType: 'sub'
			}
		});
		
		if(subWebview){
			subWebview.addEventListener('loaded', function() {
				setTimeout(function() {
					subWebview.show();
				}, 50);
			});
			subWebview.hide();
			headerWebview.append(subWebview);
		}

		//iOS平台支持侧滑关闭，父窗体侧滑隐藏后，同时需要隐藏子窗体；
		if (mui.os.ios) { //5+父窗体隐藏，子窗体还可以看到？不符合逻辑吧？
			headerWebview.addEventListener('hide', function() {
				subWebview.hide("none");
			});
		}
		templates[name] = template = {
			name: name,
			header: headerWebview,
			content: subWebview,
			loaded: loading
		};
	}
	return template;
};

var addPageInTemp = function(url, title, showMenu, aniShow){
	var subWebview = null,
		template = null;
	if (subWebview == null) {
		//获取共用父窗体
		template = plus.webview.getWebviewById("default-main");
		subWebview = template.children()[0];
	}
	subWebview.loadURL(url);
	//修改共用父模板的标题
	mui.fire(template, 'updateHeader', {
		title: title,
		showMenu: showMenu
	});
	aniShow = aniShow?aniShow:'slide-in-right';
	template.show(aniShow, 150);
}

//在android4.4.2中的swipe事件，需要preventDefault一下，否则触发不正常
window.addEventListener('dragright', function(e) {
	e.detail.gesture.preventDefault();
});


//监听右滑事件，若侧滑菜单未显示，右滑要显示菜单；
window.addEventListener("swiperight",function (e) {
	//默认滑动角度在-45度到45度之间，都会触发右滑菜单，为避免误操作，可自定义限制滑动角度；
	if(Math.abs(e.detail.angle)<4){
		//openMenu();
	}
})

Handlebars.registerHelper('retNewScoreName', function(id, selfClass, divisor){
	var temp = ["语", "数", "英", "物", "化", "生", "理", "地", "历", "政", "文", "言", "体", "社", "音", "美", "科", "艺", "手", "健", "自", "微", "计", "信", "思"],
		temp2 = ["12", "2", "3", "20", "21", "22", "27", "23", "24", "25", "28", "1", "4", "5", "6", "7", "8", "9", "10", "11", "13", "14", "15", "16", "26"];
		buffer = "", buffer2 = "",
		selfClass = selfClass || "";
		index = 0;
	var id = parseInt(id, 10);
	
	for(var key in temp2){
		if(id == temp2[key]){
			index = key;
			break;
		}
	}
	buffer = temp[index];
	switch (id) {
		case 12:case 8:case 1:case 10:
			buffer2 = "";
			break;
		case 2:case 9:case 4:case 11:
			buffer2 = "math";
			break;
		case 3:case 10:case 5:case 13:
			buffer2 = "eng";
			break;
		case 20:case 23:case 6:case 14:
			buffer2 = "phy";
			break;
		case 21:case 24:case 7:case 15:
			buffer2 = "che";
			break;
		case 22:case 25:case 8:case 16:
			buffer2 = "bio";
			break;
		case 27:case 28:case 9:case 26:
			buffer2 = "all";
			break;
		default:
			buffer = '通';
			buffer2 = 'all';
			break;
	}
	return new Handlebars.SafeString('<div class="mui-media-object mui-pull-left '+selfClass+' courseIcon '+buffer2+'">'+buffer+'</div>');
});

Handlebars.registerHelper('retSubStr', function(content, num, fn){
	if(content && content.length > num){
		content = content.substring(0, num)+"...";
	}
	return content;
})
Handlebars.registerHelper('retJson2Str', function(json, fn){
	return JSON.stringify(json);
})

/**
 * 预加载页面管理
 * **/
Util.configPreloadPage = {
	addPage : function(name){
		var preloadPageArry = this.getPage();
		var len = preloadPageArry.length;
		var flag = 0;
		for(var i=0; i<len; i++){
			if(name == preloadPageArry[i]){
				preloadPageArry.splice(i,1);
				flag = 1;
				break;
			}
		}
		if(!flag){
			if(len == 8){
				var cwebview = plus.webview.getWebviewById(preloadPageArry[0]);
				if(cwebview)
					cwebview.close();

				preloadPageArry = preloadPageArry.slice(1);
			}
		}
		setTimeout(function(){
			preloadPageArry.push(name);
			Util.lStorage.setParam('preloadPageArry', JSON.stringify(preloadPageArry));
		}, 100)
	},
	removePage : function(){
		Util.lStorage.removeParam('preloadPageArry');
	},
	getPage : function(){
		var preloadPageArry = Util.lStorage.getParam("preloadPageArry");
		if(preloadPageArry){
			preloadPageArry = JSON.parse(preloadPageArry);
		}else{
			preloadPageArry = [];
		}
		return preloadPageArry;
	}
}