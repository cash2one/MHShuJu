{
   "errorInfo":{
        "retCode":"${retCode}",
        "retMsg":"${retMsg}"
   },
   "retConInfo":[
   		<#list userList as user>
   		{
        "TYPE":${user.type},
        "USERNAME":"${user.userName}",
        "AREA":"${user.area}",
        "SUB_AREA":"${user.sub_area}",
        "SCHOOL":"${user.school}",
        "USERID":"${user.userId}",
        "GRADE":"${user.grade}"
      }<#if user_has_next>,</#if>
      </#list>
 ],
   "retTeaInfo":[
      <#list teaList as tea>
      {
   	    "TYPE":${tea.type},
        "USERNAME":"${tea.userName}",
        "AREA":"${tea.area}",
        "SUB_AREA":"${tea.sub_area}",
        "SCHOOL":"${tea.school}",
        "USERID":"${tea.userId}"
      }<#if tea_has_next>,</#if>
      </#list>
   ]  
}