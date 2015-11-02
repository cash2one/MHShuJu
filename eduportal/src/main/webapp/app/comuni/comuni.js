srvMap.add('getMsg', 'comuni/getMsg.json','commonOuter.action?uid=getMsglist');//聊天接收数据

window.comuniP = {
	intervalAjax: null,
	executeDate: null,
	count: 0,//记录发送的请求次数
	userInfo: null,
	userId: null
}
var webStorage = {};
webStorage.webSql = function () {

    var _this = this;

    //数据库
    var _dataBase;

    //打开数据库连接或者创建数据库
    this.openDatabase = function () {

        if (!!_dataBase) {
            return _dataBase;
        }
        _dataBase = openDatabase("localMsg", "1.0", "聊天记录", 1024 * 1024, function () { });

        if (!_dataBase) {
           console.log("数据库创建失败！");
           return false;
        }
        return _dataBase;

    }

    //创建数据表
    this.createTable = function () {

        var dataBase = _this.openDatabase();
        // 创建表
        dataBase.transaction(function (tx) {
            tx.executeSql(
		        "create table if not exists msg (id REAL UNIQUE, userId TEXT, destid TEXT, createdate TEXT, comuniName TEXT, destinfo TEXT, msgid TEXT, msgContent TEXT)",
		        [],
		        function () { console.log('创建msg表成功'); },
		        function (tx, error) {
		            console.log('创建msg表失败:' + error.message);
	        });
        });
    }

    //添加数据
    this.insert = function (userId, destid, createdate, comuniName, destinfo, msgid, msgContent, callback) {
        var dataBase = _this.openDatabase();
        var id = Math.random();
        dataBase.transaction(function (tx) {
            tx.executeSql(
                "insert into msg (id, userId, destid, createdate, comuniName, destinfo, msgid, msgContent) values('"+id+"', '"+userId+"', '"+destid+"', '"+createdate+"', '"+comuniName+"', '"+destinfo+"', '"+msgid+"', '"+msgContent+"')",
                [],
                function () { console.log('添加数据成功'); callback()},
                function (tx, error) {console.log('添加数据失败: ' + error.message);}
            );
        });
    }
    //查询最新的时间
    this.queryExecuteDate = function (userId, callback) {
        var dataBase = _this.openDatabase();
        dataBase.transaction(function (tx) {
            tx.executeSql(
				"select * from msg where userId=? limit 1",
                [userId],
                function (tx, result) {
                	var len = result.rows.length, comprodata = [];
				    if(len > 0){
				        for(var i=0;i<len;i++){
				            var tempdata = {};
				            tempdata["userId"] = result.rows.item(i)["userId"];
				            tempdata["destid"] = result.rows.item(i)["destid"];
				            tempdata["createdate"] = result.rows.item(i)["createdate"];
				            tempdata["comuniName"] = result.rows.item(i)["comuniName"];
				            tempdata["destinfo"] = result.rows.item(i)["destinfo"];
				            tempdata["msgid"] = result.rows.item(i)["msgid"];
				            tempdata["msgContent"] = JSON.parse(result.rows.item(i)["msgContent"]);
				            comprodata.push(tempdata);
				        }
				        callback(comprodata);
				    }else{
				    	callback("");
				    }
                },
                function (tx, error) {console.log('查询失败: ' + error.message);}
            );
        });
    }
    //查询10条历史对话框
    this.queryHistTalk = function (userId, callback) {
        var dataBase = _this.openDatabase();
        dataBase.transaction(function (tx) {
            tx.executeSql(
                "select * from(select * from msg A group by A.destid) B where B.userId=? order by createdate desc limit 10",
                [userId],
                function (tx, result) {
                	var len = result.rows.length, comprodata = [];
				    if(len > 0){
				        for(var i=0;i<len;i++){
				            var tempdata = {};
				            tempdata["userId"] = result.rows.item(i)["userId"];
				            tempdata["destid"] = result.rows.item(i)["destid"];
				            tempdata["createdate"] = result.rows.item(i)["createdate"];
				            tempdata["comuniName"] = result.rows.item(i)["comuniName"];
				            tempdata["destinfo"] = result.rows.item(i)["destinfo"];
				            tempdata["msgid"] = result.rows.item(i)["msgid"];
				            tempdata["msgContent"] = JSON.parse(result.rows.item(i)["msgContent"]);
				            comprodata.push(tempdata);
				        }
				        callback(comprodata);
				    }else{
				    	callback("");
				    }
                },
                function (tx, error) {console.log('查询历史对话框失败: ' + error.message);}
            );
        });
    }
    //根据 userId destid 获取对话框10条具体聊天内容
    this.queryHistTalkDtl = function (userId, destid, start, end, callback) {
        var dataBase = _this.openDatabase();
        dataBase.transaction(function (tx) {
            tx.executeSql(
                "select A.* from msg A where A.userId = ? and A.destid = ? order by A.createdate desc limit ?, ?;",
                [userId, destid, start, end],
                function (tx, result) {
                	var len = result.rows.length, comprodata = [];
				    if(len > 0){
				        for(var i=len-1; i>=0; i--){
				            var tempdata = {};
				            tempdata["userId"] = result.rows.item(i)["userId"];
				            tempdata["destid"] = result.rows.item(i)["destid"];
				            tempdata["createdate"] = result.rows.item(i)["createdate"];
				            tempdata["comuniName"] = result.rows.item(i)["comuniName"];
				            tempdata["destinfo"] = result.rows.item(i)["destinfo"];
				            tempdata["msgid"] = result.rows.item(i)["msgid"];
				            tempdata["msgContent"] = JSON.parse(result.rows.item(i)["msgContent"]);
				            comprodata.push(tempdata);
				        }
				        console.log(JSON.stringify(comprodata))
				        callback(comprodata);
				    }else{
				    	callback("");
				    }
                },
                function (tx, error) {console.log('查询对话框内容失败: ' + error.message);}
            );
        });
    }

    //删除数据表
    this.dropTable = function () {
        var dataBase = _this.openDatabase();
        dataBase.transaction(function (tx) {
            tx.executeSql('drop  table  msg');
        });
    }
    //查询所有
    this.queryAll = function (callback) {
        var dataBase = _this.openDatabase();
        dataBase.transaction(function (tx) {
            tx.executeSql(
        "select * from msg", 
        [],
         function (tx, result) {
         	var len = result.rows.length, comprodata = [];
		    if(len > 0){
		        for(var i=0;i<len;i++){
		            var tempdata = {};
		            tempdata["userId"] = result.rows.item(i)["userId"];
		            tempdata["destid"] = result.rows.item(i)["destid"];
		            tempdata["createdate"] = result.rows.item(i)["createdate"];
		            tempdata["comuniName"] = result.rows.item(i)["comuniName"];
		            tempdata["destinfo"] = result.rows.item(i)["destinfo"];
		            tempdata["msgid"] = result.rows.item(i)["msgid"];
		            tempdata["msgContent"] = JSON.parse(result.rows.item(i)["msgContent"]);
		            comprodata.push(tempdata);
		       		console.log(JSON.stringify(comprodata))
		        }
		        callback(comprodata);
		    }else{
		    	callback("");
		    }
         },
        function (tx, error) {
            console.log('查询失败: ' + error.message);
            callback("");
        });
        });
    }
    //查询条数
    this.queryCount = function (callback) {
        var dataBase = _this.openDatabase();
        dataBase.transaction(function (tx) {
            tx.executeSql(
        "select count(*) from msg", 
        [],
         function (tx, result) {
             if(result.rows && result.rows.length){
				var Sql_searchLocalRes = result.rows.item(0);
				Sql_searchLocalRes = JSON.stringify(Sql_searchLocalRes);
				callback(Sql_searchLocalRes);
             }else{
             	callback("");
             }
         },
        function (tx, error) {
            console.log('查询失败: ' + error.message);
            callback("");
        });
        });
    }
}

$(function(){
	
})

function initComuni(){
	//清除定时器
	if(comuniP.intervalAjax)
		clearInterval(comuniP.intervalAjax);

	//获取最后一次的请求时间，并发送ajax
	getLastExecuteDate(function(executeDate){
		console.log(executeDate)
		//重新设定最后请求时间
		comuniP.executeDate = executeDate;
		var param = {
			executeDate: executeDate
		}
		//发送
		getAjaxMsg(param);
	});
}

//获取最后一次的请求时间
function getLastExecuteDate(callback){
	var executeDate = comuniP.executeDate;
	if(executeDate){
		callback(executeDate);
		return false;
	}
	
	//数据库查询是否存在历史记录
	zwebSql.queryExecuteDate(comuniP.userId, function(Sql_searchLocalRes){
		if(Sql_searchLocalRes.length){
			executeDate = Sql_searchLocalRes[0].createdate;
		}
		
		//超过三天截
		if(executeDate){
			var execStr = parseFloat((new Date(executeDate)).getTime(), 10);
			execStr = execStr + 1000;//增加1秒，防止重复数据
			var curStr = parseFloat((new Date()).getTime(), 10);
			var TDays = parseFloat((3 * 24 * 60 * 60 * 1000), 10);
			if(curStr - execStr > TDays ){
				executeDate = dateToStr(new Date(curStr - TDays));
			}
			
			callback(executeDate);
			return false;
		}else{
			//取当前时间
			executeDate = dateToStr((new Date()));
			callback(executeDate);
			return false;
		}
	});
}

//请求信息
function getAjaxMsg(param){
//	var newParam = $.extend({},{
//		first: false,
//		time: 30000,
//		executeDate: '2015-03-09 10:12:08'
//	},param);
	
//	comuniP.executeDate = '2015-05-15 13:38:50';
	comuniP.intervalAjax = setInterval(function(){
		sendInterAjax();
	}, 60000)
}

function sendInterAjax(){
	console.log("=================定时发送========================"+comuniP.executeDate)
	Util.ajax.postJson(srvMap.get("getMsg"), "&executeDate="+comuniP.executeDate, function(json, state){
    	if(state){
    		if(json && json.retInfo.length){
    			//处理数据
    			console.log(JSON.stringify(json))
    			dealComuniJson(json);
    			comuniP.count = 0;
    			
    			clearInterval(comuniP.intervalAjax);
				comuniP.intervalAjax = setInterval(function(){
					sendInterAjax();
				}, 60000)
    		}else{
    			comuniP.count++;
    		}
    	}else{
			comuniP.count++;
		}
    	
    	//超过10次无数据返回，放大循环时间为3分钟
		if(comuniP.count > 10){
			Util.alert(json.rtnMsg);
			clearInterval(comuniP.intervalAjax);
			comuniP.intervalAjax = setInterval(function(){
				sendInterAjax();
			}, 300000)
		}
    })
}

function dealComuniJson(json){
//	var json = {"rtnMsg": "调用成功", "retInfo": [{"destid": "804300,", "startdate": "2015-04-22 09:16:21", "destinfo": "王晓飞", "enddate": "2015-05-22 15:41:38", "msg": [{"content": "您好！昨天下午没有参加会议的老师，请问一下昨日下午的会议内容，并按会议上的布置进行整理。投影机电子白板，打扫干净。下午教体局到我校检查（另外课间时间让学生文明玩耍，不要追逐打闹，注意安全，学校教师要严查）--来自：王晓飞老师", "filepath": "http://mfile.xxt.cn/", "msgid": 539650982, "msgtype": "cc", "createdate": "2015-04-22 09:16:21", "filename": "", "filetype": 0, "destusertype": 0, "filetimelong": null, "msgsendtype": 2 }, {"content": "您好！全体在编教师第二节下课到会议室开会，所有人必须参加，涉及个人利益，请迅速到会。--来自：王晓飞老师", "filepath": "http://mfile.xxt.cn/", "msgid": 539658478, "msgtype": "cc", "createdate": "2015-04-22 09:36:12", "filename": "", "filetype": 0, "destusertype": 0, "filetimelong": null, "msgsendtype": 2 } ] }, {"destid": "689746,", "startdate": "2015-04-20 07:46:27", "destinfo": "刘淑娜", "enddate": "2015-05-21 19:28:21", "msg": [{"content": "今天上午改卷： 二语：轶利 冯丹丹   二数：巧利                一数：乔飞飞  刘缓缓， 没有改卷任务的正常上课。【办公室】", "filepath": "http://mfile.xxt.cn/", "msgid": 538731824, "msgtype": "cc", "createdate": "2015-04-20 07:46:27", "filename": "", "filetype": 0, "destusertype": 0, "filetimelong": null, "msgsendtype": 2 }, {"content": "中心校检查。注意卫生，办公室卫生【办公室】", "filepath": "http://mfile.xxt.cn/", "msgid": 544956266, "msgtype": "cc", "createdate": "2015-05-04 10:44:31", "filename": "", "filetype": 0, "destusertype": 0, "filetimelong": null, "msgsendtype": 2 }, {"content": "上课的老师注意教室卫生，中心校检查【办公室】", "filepath": "http://mfile.xxt.cn/", "msgid": 544956908, "msgtype": "cc", "createdate": "2015-05-04 10:45:12", "filename": "", "filetype": 0, "destusertype": 0, "filetimelong": null, "msgsendtype": 2 } ] }, {"destid": "3765427,", "startdate": "2015-04-20 13:20:59", "destinfo": "周淑娟", "enddate": "2015-05-20 10:57:37", "msg": [{"content": "下午参加评卷的老师，抓紧时间到相应评卷室，不参加评卷的老师上好课，注意班级卫生、楼道卫生、卫生区卫生。【周淑娟】", "filepath": "http://mfile.xxt.cn/", "msgid": 538857356, "msgtype": "cc", "createdate": "2015-04-20 13:20:59", "filename": "", "filetype": 0, "destusertype": 0, "filetimelong": null, "msgsendtype": 2 } ] }, {"destid": "1388504,", "startdate": "2015-04-24 15:50:00", "destinfo": "郭灿阳,孟朝阳,马可豪...", "enddate": "2015-05-18 11:25:15", "msg": [{"content": "家长你好！周末作业写单元五词语盘点和日积月累各写两遍。希望家长督促孩子的学习。让孩子看看四大名著。祝你周末愉快！【崔苗芳老师】", "filepath": "http://mfile.xxt.cn/", "msgid": 540669820, "msgtype": "gg", "createdate": "2015-04-24 15:50:00", "filename": "", "filetype": 0, "destusertype": "2", "filetimelong": null, "msgsendtype": 1 }, {"content": "家长您好！周末在给孩子改善生活的同时，请抽出时间多陪陪孩子，和他们谈谈心，引导他们如何正确处理学习生活中遇到的各种问题，教会他们自己合理安排利用好时间，督促孩子认真完成各科作业，鼓励他们闲暇时多参加一些有意义的活动，而不是长时间看电视或玩游戏。祝周末愉快！【崔苗芳老师】", "filepath": "http://mfile.xxt.cn/", "msgid": 540670116, "msgtype": "gg", "createdate": "2015-04-25 08:00:00", "filename": "", "filetype": 0, "destusertype": "2", "filetimelong": null, "msgsendtype": 1 }, {"content": "家长您好！因为明天是五一国家法定节假日，请于今天下午4：20来校接孩子。周一早上7：30前到校。谢谢您的配合。【崔苗芳老师】", "filepath": "http://mfile.xxt.cn/", "msgid": 543470318, "msgtype": "gg", "createdate": "2015-04-30 10:20:32", "filename": "", "filetype": 0, "destusertype": "2", "filetimelong": null, "msgsendtype": 1 }, {"content": "家长您好！假期语文作业1、生字表二的生字注拼音、组一个词各写一遍。2、写出考后反思。希望家长督促孩子的学习。【崔苗芳老师】", "filepath": "http://mfile.xxt.cn/", "msgid": 543547342, "msgtype": "gg", "createdate": "2015-04-30 16:40:00", "filename": "", "filetype": 0, "destusertype": "2", "filetimelong": null, "msgsendtype": 1 } ] }, {"destid": "1202222,", "startdate": "2015-05-18 10:24:10", "destinfo": "刘如笑", "enddate": "2015-05-18 10:24:10", "msg": [{"content": "请上周填写课堂教学观察量表的同事抓紧时间把表填好交过来，立等上交！【刘如笑老师】", "filepath": "http://mfile.xxt.cn/", "msgid": 550538568, "msgtype": "cc", "createdate": "2015-05-18 10:24:10", "filename": "", "filetype": 0, "destusertype": 0, "filetimelong": null, "msgsendtype": 2 } ] }, {"destid": "3097317,", "startdate": "2015-04-24 07:09:26", "destinfo": "刘婷婷", "enddate": "2015-05-12 09:39:05", "msg": [{"content": "各级老师，今天在教室一定要强调安全问题，文明玩耍，尤其不要去河边玩耍，昨天发现有学生去捉鱼逮螃蟹。【刘婷婷老师】", "filepath": "http://mfile.xxt.cn/", "msgid": 540477128, "msgtype": "cc", "createdate": "2015-04-24 07:09:26", "filename": "", "filetype": 0, "destusertype": 0, "filetimelong": null, "msgsendtype": 2 }, {"content": "各位同事打扰了，如您见到一个绿色U盘，请与胡巧丽老师联系。谢谢。【刘婷婷老师】", "filepath": "http://mfile.xxt.cn/", "msgid": 547193024, "msgtype": "cc", "createdate": "2015-05-08 12:25:59", "filename": "", "filetype": 0, "destusertype": 0, "filetimelong": null, "msgsendtype": 2 }, {"content": "各位老师好，课间操请及时到位，各班主任要求学生注意卫生，保洁员做好保洁工作！谢谢！【刘婷婷老师】", "filepath": "http://mfile.xxt.cn/", "msgid": 548203938, "msgtype": "cc", "createdate": "2015-05-12 09:25:03", "filename": "", "filetype": 0, "destusertype": 0, "filetimelong": null, "msgsendtype": 2 }, {"content": "各位老师好，课间操请及时到位，各班主任要求学生注意卫生，保洁员做好保洁工作！谢谢！【刘婷婷老师】【刘婷婷老师】", "filepath": "http://mfile.xxt.cn/", "msgid": 548205612, "msgtype": "cc", "createdate": "2015-05-12 09:28:56", "filename": "", "filetype": 0, "destusertype": 0, "filetimelong": null, "msgsendtype": 2 }, {"content": "各位老师好，课间操请及时到位，各班主任要求学生注意卫生，保洁员做好保洁工作！谢谢！【刘婷婷老师】【刘婷婷老师】", "filepath": "http://mfile.xxt.cn/", "msgid": 548210334, "msgtype": "cc", "createdate": "2015-05-12 09:35:39", "filename": "", "filetype": 0, "destusertype": 0, "filetimelong": null, "msgsendtype": 2 } ] }, {"destid": "5340259,", "startdate": "2015-04-30 14:05:01", "destinfo": "孙银焕", "enddate": "2015-04-30 14:05:01", "msg": [{"content": "老师们好！今天下午两节课后学生离校，假期三天，班主任老师在教室给孩子们讲好安全。【办公室】", "filepath": "http://mfile.xxt.cn/", "msgid": 543776424, "msgtype": "cc", "createdate": "2015-04-30 14:05:01", "filename": "", "filetype": 0, "destusertype": 0, "filetimelong": null, "msgsendtype": 2 } ] }, {"destid": "776389,", "startdate": "2015-04-29 15:26:03", "destinfo": "范志乐", "enddate": "2015-04-29 16:30:31", "msg": [{"content": "下午好！请现在到后勤处领取水笔芯。如缺红笔芯也一并领取！【范志乐老师】", "filepath": "http://mfile.xxt.cn/", "msgid": 542817700, "msgtype": "cc", "createdate": "2015-04-29 15:26:03", "filename": "", "filetype": 0, "destusertype": 0, "filetimelong": null, "msgsendtype": 2 }, {"content": "下午好！各办公室的饮水机、窗户玻璃和窗纱有问题的请于明天上午八点半报于后勤处；另外各班主任查看教室玻璃和窗框，有问题的，写清楚班级、问题是什么（尽量具体），可由班长明天上午八点半交于后勤处，请务必按时上交！【范志乐老师】", "filepath": "http://mfile.xxt.cn/", "msgid": 542903188, "msgtype": "cc", "createdate": "2015-04-29 16:30:31", "filename": "", "filetype": 0, "destusertype": 0, "filetimelong": null, "msgsendtype": 2 } ] } ], "rtnCode": "1"};
	var sqlTime = 0;
	var retInfo = json.retInfo;
	var destidArray = [];//装载新的数据
	var userId = JSON.parse(Util.lStorage.getParam('userInfo')).userId;
	
	for(var key in retInfo){
		var msgArr = retInfo[key].msg;
		for(var i=0, length=msgArr.length; i<length; i++){
			var arr = {};
			arr.userId = userId;
			arr.destid = retInfo[key].destid;
			arr.destinfo = retInfo[key].destinfo;
			arr.createdate = msgArr[i].createdate;
			arr.msg = msgArr[i];
			destidArray.push(arr);
			
			//将新数据插入数据库
			zwebSql.insert(arr.userId, arr.destid, arr.createdate, arr.destinfo, arr.destinfo, arr.msg.msgid, JSON.stringify(arr.msg), function(){
				console.log("插入成功！");
				sqlTime++;
				
				if(sqlTime == destidArray.length){
	
					//更新executeDate
					var cDate = retInfo[0].enddate;
					cDate = new Date(cDate).getTime() + 1000;//增加1秒，防止返回重复数据
					comuniP.executeDate = dateToStr(new Date(cDate));
					console.log(comuniP.executeDate)
					
					//判断沟通首页是否已经预加载
					var comuniIndex = plus.webview.getWebviewById('comuni/comuniIndex.html');
					if(comuniIndex){
						//如果已加载，通知更新数据
						if(sqlTime == destidArray.length){
							mui.fire(comuniIndex, "pageflowrefresh" );
						}
					}
					
					//判断当前页面是否为聊天详情页
					var currentUrl = plus.webview.currentWebview();
					if(currentUrl.getURL().indexOf('comuniDtlContent') != '-1'){
						//判断新消息内是否包含有当前对话框
						var currentDestid = Util.lStorage.getParam('currentDestid');//注意逗号
						for(var k=0, klength=retInfo.length; k<klength; k++){
							if(currentDestid == retInfo[k].destid){
								//追加数据，去除msgsendtype=1的数据
								var cMsg = retInfo[k].msg,
									newCMsg = [];
								for(var n=0, nLength=cMsg.length; n<nLength; n++){
									if(cMsg[n].msgsendtype == '2'){
										newCMsg.push(cMsg[n]);
									}
								}
								var T_msg = $("#T_msg").val();
								var template = Handlebars.compile(T_msg);
								var html = template(newCMsg); 
								$("#J_worklist").append(html);
							}
						}
					}
				}
			});
		}
	}
}

function timeToStr(ts){
	if(isNaN(ts)){
		return "--:--:--";
	}
	var h=parseInt(ts/3600);
	var m=parseInt((ts%3600)/60);
	var s=parseInt(ts%60);
	return (ultZeroize(h)+":"+ultZeroize(m)+":"+ultZeroize(s));
};
// 格式化日期时间字符串，格式为"YYYY-MM-DD HH:MM:SS"
function dateToStr(d){
	return (d.getFullYear()+"-"+ultZeroize(d.getMonth()+1)+"-"+ultZeroize(d.getDate())+" "+ultZeroize(d.getHours())+":"+ultZeroize(d.getMinutes())+":"+ultZeroize(d.getSeconds()));
};
/**
 * zeroize value with length(default is 2).
 * @param {Object} v
 * @param {Number} l
 * @return {String} 
 */
function ultZeroize(v,l){
	var z="";
	l=l||2;
	v=String(v);
	for(var i=0;i<l-v.length;i++){
		z+="0";
	}
	return z+v;
}

function initSql(callback){
	zwebSql = window.zwebSql = new webStorage.webSql();//创建本地数据库
	try {
//  	zwebSql.dropTable();
//  	console.log("==========删除表================")
//  	return false;
		zwebSql.openDatabase();//打开数据库
		zwebSql.createTable();//创建表，存在则不创建
		
		comuniP.userInfo = JSON.parse(Util.lStorage.getParam('userInfo'));
		comuniP.userId = comuniP.userInfo.userId;
		
		if(typeof callback == 'function')
			callback(zwebSql);
	} catch (e) { 
		console.log("本地数据库错误！"+e.message);
	}
}
