{
   "errorInfo":{
        "retCode":"${retCode}",
        "retMsg":"${retMsg}"
   },
   "retInfo":[
       <#list userList as user>
   		{
            "phone":"${user.phone}",
            "type":${user.type},
            "userName":"${user.userName}",
            "school":"${user.school}",
            "grade":"${user.grade}",
            "userId":"${user.userId}",
            "userSex":"${user.userSex}"
        }<#if user_has_next>,</#if>
       </#list>
   ]
}