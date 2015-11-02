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
		        "create table if not exists msg (id REAL UNIQUE, msgId TEXT, json TEXT,stdate TEXT,eddate TEXT)",
		        [],
		        function () { console.log('创建msg表成功'); },
		        function (tx, error) {
		            console.log('创建msg表失败:' + error.message);
	        });
        });
    }

    //添加数据
    this.insert = function (msgId, json, stdate, eddate, callback) {
        var dataBase = _this.openDatabase();
        var id = Math.random();
        dataBase.transaction(function (tx) {
            tx.executeSql(
                "insert into msg (id, msgId, json, stdate, eddate) values(?, ?, ?, ?, ?)",
                [id, msgId, JSON.stringify(json), stdate, eddate],
                function () { console.log('添加数据成功'); callback()},
                function (tx, error) {console.log('添加数据失败: ' + error.message);}
            );
        });
    }

    // 查询
    this.query = function (dataId, start, end, index, callback) {
        var dataBase = _this.openDatabase();
        var index = parseInt(index, 10);
        dataBase.transaction(function (tx) {
            tx.executeSql(
        "select * from msg where msgId = ? and stdate <= ? and eddate >= ?", 
        [dataId, start, end],
         function (tx, result) {
             if(result.rows && result.rows.length){
				var Sql_searchLocalRes = result.rows.item(index);
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
    this.queryAll = function (dataId, callback) {
        var dataBase = _this.openDatabase();
        dataBase.transaction(function (tx) {
            tx.executeSql(
        "select * from msg where msgId = ?", 
        [dataId],
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

    //更新数据
    this.update = function (id, data, startTime, lastTime, stdate, edate, callback) {

        var dataBase = _this.openDatabase();
        dataBase.transaction(function (tx) {
            tx.executeSql(
        "update msg set json = ?,stdate = ?, eddate = ?  where stdate <= ? and eddate >= ? and msgid = ?",
        [data, stdate, edate, startTime, lastTime, id],
         function (tx, result) {
//           _this.query();
			console.log("更新数据成功！");
			callback();
         },
        function (tx, error) {
            console.log('更新失败: ' + error.message);
        });
        });
    }

    //删除数据
    this.del = function (id) {
        var dataBase = _this.openDatabase();
        dataBase.transaction(function (tx) {
            tx.executeSql(
        "delete from  msg where id= ?",
        [id],
         function (tx, result) {

         },
        function (tx, error) {
            console.log('删除失败: ' + error.message);
        });
        });
    }

    //删除数据表
    this.dropTable = function () {
        var dataBase = _this.openDatabase();
        dataBase.transaction(function (tx) {
            tx.executeSql('drop  table  msg');
        });
    }


}