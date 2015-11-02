var conf = 1; //控制服务
var srvMap = (function(){
  var srcPref = ["../data/","http://192.168.0.62/edu/"];//辛恒
//	var srcPref = ["../data/","http://172.21.139.1/edu/"];//春光
//	var srcPref = ["../data/","http://192.168.0.116/edu/"];//向阳
//	var srcPref = ["../data/","http://10.87.30.144:49000/edu/"];//测试
//	var srcPref = ["../data/","http://www.ha.10086.cn/edu/"];//生产
    var dataArray = [
         {

         },
         {

         }
    ];
    
    return {
        add: function(uid, mockSrc, srvSrc) {
            dataArray[0][uid] = srcPref[conf] + mockSrc;
            dataArray[1][uid] = srcPref[conf] + 'app/action/' + srvSrc;         
        },
        addLogin: function(uid, mockSrc, srvSrc) {
            dataArray[0][uid] = srcPref[conf] + mockSrc;
            dataArray[1][uid] = srcPref[conf] + 'hn/login/' + srvSrc;         
        },
        addHn: function(uid, mockSrc, srvSrc) {
            dataArray[0][uid] = srcPref[conf] + mockSrc;
            dataArray[1][uid] = srcPref[conf] + 'hn/action/' + srvSrc;            
        },
        get: function(uid) {
            return dataArray[conf][uid];
        },
        getAppPath:function(){
        	return srcPref[conf];
        },
        dataArrays:function(){
            return dataArray[conf];
        }
    };
})(jQuery);
window.dataArray = srvMap.dataArrays();

/**
 * tpl 定义
 */
var tplMap = (function(){
    var version = '20131225';
    var tplPref = ["/aiscrm/pc/base/tpl/","page/"];
    var tpl = {
        // 办理路径
        "globalPath":tplPref[conf]+"globalPath.tpl"
    };
    
    // 为tpl添加版本号
    version = '?ver=' + version;
    for(var perTpl in tpl){
        if(tpl.hasOwnProperty(perTpl)){
        	var tplPath = tpl[perTpl];
        	tplPath = (conf==1)?tplPath.replace(".tpl","_tpl"):tplPath;
            tpl[perTpl] = tplPath+version;
        }
    }
    
    return {
        add: function(uid,tplSrc) {
            if(1 == conf){
                tplSrc = tplSrc.replace(".tpl","_tpl");
            }
            tpl[uid] = tplPref[conf] + tplSrc + version;
        },
        get: function(uid) {
            return tpl[uid];
        },
        tpls: function() {
            return tpl;
        }
    };
})(jQuery);
window.tpl = tplMap.tpls();