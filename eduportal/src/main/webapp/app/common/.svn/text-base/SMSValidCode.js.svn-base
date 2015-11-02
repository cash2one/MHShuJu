/**
 * 短信发送
 * @Author fanyu@asiainfo.com
 */
;(function($) {
 if (!window.Util) window.Util = {};
 if(!Util.sms)  Util.sms ={};
    /*
     * 发送短信
     *@param current  当前的值
     *@param opts {param : //参数,obj: //容器 maxtime:最大的重新发送时间}
     */
    srvMap.addLogin("sendSmsResult","getValidateCode.json","sendRandNum.action?uid=sdRn");
    srvMap.add("isPassCheckSms","base/smsResult.json","");
    var current = 0;
    $.fn.sendSmsCode = function(opts) {
        opts = jQuery.extend({
            obj : '',
            param : {},
            maxtime : 60,
            timer : null,
            url : window.dataArray.sendSmsResult,
            succ : '重新获取验证码',
            fail : '重新获取验证码',
            wait : '发送中...',
            succf : '{0}秒后可再次获取',
            callback:null
        }, opts || {});
        if (current == 0) {
            Util.sms.bindSendSms(this,opts);
        }
    };
    
    Util.sms.resetSendSms = function(that,opts){
            $(that).unbind("click");
            current = opts.maxtime;
            current = current - 1;
            opts.timer = setInterval(function() {
                if (current > 0) {
                    var msg = Util.sms.formatStr(opts.succf, current);
                    $(opts.obj).html(msg);
					that.siblings('.ui-sms-remind').addClass('ui-remind-scs').removeClass('ui-remind-error').text('发送成功，请查收！').show();
                    $(opts.obj).addClass('ui-getSMS-dis');
                    current = (current) - 1;
                } else {
                    clearInterval(opts.timer);
                    $(opts.obj).html(opts.succ);
                    $(opts.obj).removeClass('ui-getSMS-dis');
                    Util.sms.bindSendSms(that,opts);
                }
            }, 1000);
    };
    
    Util.sms.bindSendSms = function(that,opts){
        $(that).bind('click', function() {
            $(opts.obj).html(opts.wait);
            $(opts.obj).addClass('ui-getSMS-dis');
            Util.ajax.postJson(opts.url, opts.param, function(json, status) {
                if (status) {
                    if (json.rtnCode != 1) {
                        $(opts.obj).html(opts.fail);
                        $(opts.obj).removeClass('ui-getSMS-dis');
						that.siblings('.ui-sms-remind').addClass('ui-remind-error').removeClass('ui-remind-scs').text(json.rtnMsg||'发送失败，请重试！').show();
                    } else {
                        if(opts.callback) opts.callback(json.bean);
                        clearInterval(opts.timer);
                        Util.sms.resetSendSms(that,opts);
                    }
                } else {
                    $(opts.obj).html(opts.fail);
                    $(opts.obj).removeClass('ui-getSMS-dis');
                }
            })
        })
    };
    
    /**
     * 短信校验
     * @param {Object} tempPwd   验证码
     */
   Util.sms.check = function (obj,menuCode,tempPwd,serialID,callback) {
       var param ={};
        var flag = true;
        if($.trim(tempPwd)=='' || tempPwd == undefined){
            art.dialog.tips("请输入短信验证码");
            flag = false;
        }else{
            param['menuCode'] = menuCode;
            param['tempPwd'] = tempPwd;
            param['serialID'] = serialID;
            // ajax检验短信验证码
            Util.ajax.postJsonSync(srvMap.get("isPassCheckSms"),param,function(json, status){
                if(status){
                    if(callback){
                        callback(true,json);
                    }else{
                     $(obj).siblings('.ui-tiptext').html(Util.sms.formatStr(Util.validator.tiptext.success,'短信校验成功'));   
                    }
                } else {
                    if(callback){
                        callback(false,json);
                    }else{
                      $(obj).val('');
                        $(obj).siblings('.ui-tiptext').html(Util.sms.formatStr(Util.validator.tiptext.warning,json.retMessage));
                    }
                    flag = false;
                }
            });
        }
        return flag;
   	};
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
})(jQuery);
