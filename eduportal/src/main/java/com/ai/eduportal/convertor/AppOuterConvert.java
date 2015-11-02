package com.ai.eduportal.convertor;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ai.frame.web.convertor.CommonConvert;
import com.ai.frame.web.xml.bean.Parameter;

import common.ai.tools.DateUtil;
import common.ai.tools.JsonUtil;
import common.ai.tools.StringUtil;

public class AppOuterConvert extends CommonConvert{
	protected String getSingleObjDataVal(Map<String,Object> values,String key){
    	if(values.containsKey(key)){
            return StringUtil.obj2TrimStr(values.get(key));
        }else{
            return NOTRTNKEY;
        }
    }
	protected Map<String,String> convertObjMap(Map<String,Object> data,List<Parameter> parameters){
    	Map<String,String> rtnMap = new HashMap<String,String>();
		if (parameters != null && parameters.size() > 0) {
			for(Parameter param:parameters){
				String key = param.getKey();
                String toKey = param.getToKey();
                String value = getSingleObjDataVal(data,key);
                if(!NOTRTNKEY.equals(value)){
                    rtnMap.put(toKey, value);
                }
			}
		}else{
			if(data!=null){
				Iterator<Map.Entry<String, Object>> datait = data.entrySet().iterator();
				while(datait.hasNext()){
					Map.Entry<String, Object> ent = datait.next();
					rtnMap.put(ent.getKey(), StringUtil.obj2TrimStr(ent.getValue()));
				}
			}
		}
		return rtnMap;
    }
	
	private List<Map<String,String>> getAllDateMap(String date,List<Map<String,Object>> worklist,List<Parameter> parameters){
		List<Map<String,String>> rtnMp = new ArrayList<Map<String,String>>();
		if(worklist!=null){
			for(Map<String,Object> map:worklist){
				if(StringUtil.obj2TrimStr(map.get("createdate")).equals(date)){
					Map<String,String> outMap = convertObjMap(map,parameters);
					rtnMp.add(outMap);
				}
			}
		}
		return rtnMp;
	}
	protected Map<String,String> list2map(List<Parameter> parameters){
		Map<String,String> map = new HashMap<String,String>();
		if(parameters!=null){
			for(Parameter p:parameters){
				if(!StringUtil.isEmpty(p.getToKey())){
					map.put(p.getKey(), p.getToKey());
				}else{
					map.put(p.getKey(), p.getKey());
				}
			}
		}
		return map;
	}
	public String workConvert(List<Map<String,Object>> worklist,List<Parameter> parameters){
		List<Map<String,Object>> rtnlist = new ArrayList<Map<String,Object>>();
		Map<String,String> mptokeys = list2map(parameters);
		if(worklist!=null){
			Map<String,List<Map<String,String>>> dtmap = new HashMap<String,List<Map<String,String>>>();
			for(Map<String,Object> map:worklist){
				String date = StringUtil.obj2Str(map.get("createdate"));
				if(dtmap.containsKey(date)){
					continue;
				}
				List<Map<String,String>> datas = getAllDateMap(date,worklist,parameters);
				dtmap.put(date, datas);
			}
			
			//转换
			Iterator<Map.Entry<String, List<Map<String,String>>>> datait = dtmap.entrySet().iterator();
			while(datait.hasNext()){
				Map.Entry<String, List<Map<String,String>>> ent = datait.next();
				Map<String,Object> mp = new HashMap<String,Object>();
				String dateKey  = mptokeys.get("date");
				String datasKey = mptokeys.get("datas");
				if(!StringUtil.isEmpty(dateKey) && !StringUtil.isEmpty(datasKey)){
					mp.put(mptokeys.get("date"), ent.getKey());
					mp.put(mptokeys.get("datas"), ent.getValue());
					rtnlist.add(mp);
				}
			}
		}
		Collections.sort(rtnlist, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> m1, Map<String, Object> m2) {
				    Date createM=DateUtil.string2Date(StringUtil.trim(String.valueOf(m1.get("date")), "2015-01-01 00:00:00"), "yyyy-MM-dd HH:mm:ss");
				    Date createM2=DateUtil.string2Date(StringUtil.trim(String.valueOf(m2.get("date")), "2015-01-01 00:00:00"), "yyyy-MM-dd HH:mm:ss");
				    return createM2.compareTo(createM);
			}
		});
		return convertListMap2Json(rtnlist,mptokeys);
	}
	protected List<Map<String,Object>> convertListMapO(List<Map<String,Object>> datas,List<Parameter> parameters){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(datas!=null){
			for(Map<String,Object> map:datas){
				Map<String,Object> rtnMap = convertMapO(map,parameters);
				if(rtnMap.size()>0)list.add(rtnMap);
			}
		}
		return list;
    }
	
	protected Map<String,Object> convertMapO(Map<String,Object> data,List<Parameter> parameters){
    	Map<String,Object> rtnMap = new HashMap<String,Object>();
		if (parameters != null && parameters.size() > 0) {
			for(Parameter param:parameters){
				String key = param.getKey();
                String toKey = param.getToKey();
                Object value = getSingleDataValO(data,key);
                if(!NOTRTNKEY.equals(value)){
                    rtnMap.put(toKey, value);
                }
			}
		}else{
			rtnMap.putAll(data);
		}
		return rtnMap;
    }
	protected Object getSingleDataValO(Map<String,Object> values,String key){
    	if(values.containsKey(key)){
            return values.get(key);
        }else{
            return NOTRTNKEY;
        }
    }
	private String convertListMap2Json(List<Map<String,Object>> rtnlist,Map<String,String> mptokeys){
		String retCode  = mptokeys.get(RTNCODEKEY);
		if(StringUtil.isEmpty(retCode)){
			retCode = RTNCODEKEY;
		}
		
		String retMsg   = mptokeys.get(RTNMSGKEY);
		if(StringUtil.isEmpty(retMsg)){
			retMsg = RTNMSGKEY;
		}
		String retInfo  = mptokeys.get("retInfo");
		Map<String,Object> rtn = new HashMap<String,Object>();
		rtn.put(retCode, "1");
		rtn.put(retMsg, "调用成功");
		rtn.put(retInfo, rtnlist);
		
		return JsonUtil.convertObject2Json(rtn);
	}
	
	public String convertList2StudentScoreList(List<Map<String,Object>> studentScoreList,List<Parameter> parameters){
		Map<String,String> mptokeys = list2map(parameters);
		List<String> termIDlist = new ArrayList<String>();   //把不同的 termID 学期ID  都放在同一个List里面
		for(Map<String,Object> map : studentScoreList){
			String termID = map.get("TERMID").toString();
			if(!termIDlist.contains(termID)){
			      termIDlist.add(termID);
			}
		}
		List<Map<String,Object>> rtn = new ArrayList<Map<String,Object>>();
		for(String termID : termIDlist){
			Map<String,Object> map = new HashMap<String,Object>();
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(Map<String,Object> m : studentScoreList){
				if(m.get("TERMID").equals(termID)){
					list.add(m);
				}
			}
			map.put("datas",list);
			rtn.add(map);
		}
		return convertListMap2Json(convertListMapO(rtn,parameters),mptokeys);
	}
	public String convertNoticeCounts(List<Map<String,Object>> NoticeCounts,List<Parameter> parameters){
		Map<String,String> mptokeys = list2map(parameters);
		return convertListMap2Json(NoticeCounts,mptokeys);
	}
	public String convertList2CountList(List<Map<String,Object>> NoticeCountList,List<Parameter> parameters){
		Map<String,String> mptokeys = list2map(parameters);
		return convertListMap2Json(convertListMapO(NoticeCountList,parameters),mptokeys);
	}
	public String convertList2TeaExamList(List<Map<String,Object>> publist,List<Parameter> parameters){
		Map<String,String> mptokeys = list2map(parameters);
		return convertListMap2Json(convertListMapO(publist,parameters),mptokeys);
	}
	
	public String convertList2ListPoit(List<Map<String,Object>> poitList,List<Parameter> parameters){
		Map<String,String> mptokeys = list2map(parameters);
		return convertListMap2Json(poitList,mptokeys);
	}
	public boolean mathMaxMin(String num,String max,String min){
		if(Integer.parseInt(num) <= Integer.parseInt(max) && Integer.parseInt(num) >= Integer.parseInt(min)){
			return true;
		}else{
			return false;
		}
	}
	
	//合并消息
	public String mergeMsgList(List<Map<String,Object>> msgList,Map<String,String> mptokeys) {
	    //按时间升序排列
		Collections.sort(msgList, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> m1, Map<String, Object> m2) {
				    int destCmp=StringUtil.trim(String.valueOf(m1.get("destid"))).compareTo(StringUtil.trim(String.valueOf(m2.get("destid"))));
				    if(destCmp==0){
				    	Date createM=DateUtil.string2Date(StringUtil.trim(String.valueOf(m1.get("createdate")), "2015-01-01 00:00:00"), "yyyy-MM-dd HH:mm:ss");
				    	Date createM2=DateUtil.string2Date(StringUtil.trim(String.valueOf(m2.get("createdate")), "2015-01-01 00:00:00"), "yyyy-MM-dd HH:mm:ss");
				        return createM.compareTo(createM2);
				    }
				    return destCmp;
			}
		});
		List<Map<String,Object>> rtnList=new ArrayList<Map<String,Object>>();
		Map<String,Object> firstMap=new HashMap<String, Object>();
		List<Map<String,Object>> allMsgList=new ArrayList<Map<String,Object>>();
		String destId=String.valueOf(msgList.get(0).get("destid"));
		String lastCreateDateStr=String.valueOf(msgList.get(0).get("createdate"));
		firstMap.put("destid", destId);
		firstMap.put("destinfo", msgList.get(0).get("destinfo"));
		firstMap.put("startdate", msgList.get(0).get("createdate"));
		allMsgList.add(getMsgContent(msgList.get(0)));
		
		for (int i =1,len=msgList.size(); i < len; i++) {
			String currDestId = String.valueOf(msgList.get(i).get("destid"));
			if (currDestId.equals(destId)) {
				allMsgList.add(getMsgContent(msgList.get(i)));
				lastCreateDateStr=String.valueOf(msgList.get(i).get("createdate"));
			} else {
				firstMap.put("enddate",lastCreateDateStr);
				firstMap.put("msg", allMsgList);
				rtnList.add(firstMap);
				
				firstMap=new HashMap<String, Object>();
				allMsgList=new ArrayList<Map<String,Object>>();
				destId=String.valueOf(msgList.get(i).get("destid"));
				lastCreateDateStr=String.valueOf(msgList.get(i).get("createdate"));
				firstMap.put("destid", destId);
				firstMap.put("destinfo", msgList.get(i).get("destinfo"));
				firstMap.put("startdate", msgList.get(i).get("createdate"));
				allMsgList.add(getMsgContent(msgList.get(i)));
			}
		}
		firstMap.put("enddate",lastCreateDateStr);
		firstMap.put("msg", allMsgList);
		rtnList.add(firstMap);
		
		 //按时间降序排列
		Collections.sort(rtnList, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> m1, Map<String, Object> m2) {
				    Date createM=DateUtil.string2Date(StringUtil.trim(String.valueOf(m1.get("enddate")), "2015-01-01 00:00:00"), "yyyy-MM-dd HH:mm:ss");
				    Date createM2=DateUtil.string2Date(StringUtil.trim(String.valueOf(m2.get("enddate")), "2015-01-01 00:00:00"), "yyyy-MM-dd HH:mm:ss");
				    return createM2.compareTo(createM);
			}
		});
		
		String retCode  =mptokeys.get(RTNCODEKEY);
		if(StringUtil.isEmpty(retCode)){
			retCode = RTNCODEKEY;
		}
		String retMsg   =mptokeys.get(RTNMSGKEY);
		if(StringUtil.isEmpty(retMsg)){
			retMsg = RTNMSGKEY;
		}
		String retInfo  =mptokeys.get("retInfo");
		if(StringUtil.isEmpty(retInfo)){
			retInfo="retInfo";
		}
		Map<String,Object> rtn = new HashMap<String,Object>();
		rtn.put(retCode, "1");
		rtn.put(retMsg, "调用成功");
		rtn.put(retInfo, rtnList);
		System.out.println("-------="+JsonUtil.convertObject2Json(rtn));
		return JsonUtil.convertObject2Json(rtn);
	}
	
	public Map<String,Object> getMsgContent(Map<String,Object> msgMap){
		Map<String,Object> rtnMap=new HashMap<String, Object>();
		rtnMap.put("createdate",msgMap.get("createdate"));
		rtnMap.put("content", msgMap.get("content"));
		rtnMap.put("msgid", msgMap.get("msgid"));
		rtnMap.put("destusertype", msgMap.get("destusertype"));
		rtnMap.put("msgtype", msgMap.get("msgtype"));
		rtnMap.put("filetype", msgMap.get("filetype"));
		try {
			rtnMap.put("filename", URLDecoder .decode(StringUtil.obj2TrimStr((msgMap.get("filename"))),"UTF-8"));
		} catch (Exception e) {
			rtnMap.put("filename",msgMap.get("filename"));
		}
		rtnMap.put("filepath",msgMap.get("filepath"));
		rtnMap.put("filetimelong", msgMap.get("filetimelong"));
		rtnMap.put("msgsendtype",msgMap.get("msgflag") );
		
		return rtnMap;
	}
	
	public String convertGetMsgList(List<Map<String,Object>> noticeCountList,List<Parameter> parameters){
		Map<String,String> mptokeys = list2map(parameters);
		if (noticeCountList == null || noticeCountList.size() == 0) {
			String retCode = mptokeys.get(RTNCODEKEY);
			if (StringUtil.isEmpty(retCode)) {
				retCode = RTNCODEKEY;
			}
			String retMsg = mptokeys.get(RTNMSGKEY);
			if (StringUtil.isEmpty(retMsg)) {
				retMsg = RTNMSGKEY;
			}
			String retInfo = mptokeys.get("retInfo");
			if (StringUtil.isEmpty(retInfo)) {
				retInfo = "retInfo";
			}
			Map<String, Object> rtn = new HashMap<String, Object>();
			rtn.put(retCode, "1");
			rtn.put(retMsg, "调用成功");
			rtn.put(retInfo, new HashMap<String, Object>());
			return JsonUtil.convertObject2Json(rtn);
		}
		return mergeMsgList(convertListMapO(noticeCountList,parameters),mptokeys);
	}
	
	public String convertMulList(List<Map<String,Object>> mulList,List<Parameter> parameters){
		Map<String,String> mptokeys = list2map(parameters);
		return convertFistListMap2Json(mulList,mptokeys);
	}
	
	private String convertFistListMap2Json(List<Map<String,Object>> rtnlist,Map<String,String> mptokeys){
		String retCode  = mptokeys.get(RTNCODEKEY);
		if(StringUtil.isEmpty(retCode)){
			retCode = RTNCODEKEY;
		}
		
		String retMsg   = mptokeys.get(RTNMSGKEY);
		if(StringUtil.isEmpty(retMsg)){
			retMsg = RTNMSGKEY;
		}
		Map<String,Object> rtn = new HashMap<String,Object>();
		rtn.put(retCode, "1");
		rtn.put(retMsg, "调用成功");
		rtn.putAll(rtnlist.get(0));
		return JsonUtil.convertObject2Json(rtn);
	}
	
	public String convertList2TeaExamListAvg(List<Map<String,Object>> publist,List<Parameter> parameters){
		Map<String,String> mptokeys = list2map(parameters);
		for(int i=0;i<=publist.size();i++){
			for(int j=1;j<=publist.size()-1;j++){
				Map<String, Object> map = new HashMap<String,Object>();
				if(null != publist.get(j-1).get("SCORE") || "".equals(publist.get(j-1).get("SCORE"))){
					if(Integer.valueOf(publist.get(j-1).get("SCORE").toString())
							.compareTo(Integer.valueOf(publist.get(j).get("SCORE").toString()))<0){
						map = publist.get(j-1);
						publist.set(j-1, publist.get(j));
						publist.set(j, map);
					}
				}
			}
		}
//		Collections.sort(publist, new Comparator<Map<String, Object>>() {
//			public int compare(Map<String, Object> m1, Map<String, Object> m2) {
//				String string = m1.get("SCORE").toString();
//				int score11 = Integer.parseInt(StringUtil.trim(m1.get("SCORE").toString()), 0);
//				int score22 = Integer.parseInt(StringUtil.trim(m2.get("SCORE").toString()),0);
//				Integer score1 = Integer.parseInt(StringUtil.trim(m1.get("SCORE").toString()), 0);
//				Integer score2 = Integer.parseInt(StringUtil.trim(m2.get("SCORE").toString()),0);
//				return score1.compareTo(score2);
//			}
//		});
		//计算平均分
		int count = 0;
		int totalScore = 0;
		for(int k=0;k<publist.size();k++){
			if(null != publist.get(k).get("SCORE") || "".equals(publist.get(k).get("SCORE"))){
				count++;
				totalScore += Integer.valueOf(publist.get(k).get("SCORE").toString());
				publist.get(k).put("RANK", k+1);
				if(k!=0 && publist.get(k).get("SCORE").equals(publist.get(k-1).get("SCORE"))){
					publist.get(k).put("RANK",publist.get(k-1).get("RANK"));
				}
			}
		}
//		for(Map<String,Object> map : publist){
//			if(null != map.get("SCORE") || "".equals(map.get("SCORE"))){
//				count++;
//				totalScore += Integer.valueOf(map.get("SCORE").toString());
//			}
//		}
		int avg = totalScore/count;
		for(Map<String,Object> map : publist){
			map.put("AVGSCORE", avg);
		}
		return convertListMap2Json(convertListMapO(publist,parameters),mptokeys);
	}
}
