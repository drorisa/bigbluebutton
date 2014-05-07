package org.bigbluebutton.core.apps.chat.redis

import scala.collection.mutable.HashMap
import org.bigbluebutton.conference.service.chat.ChatKeyUtil
import org.bigbluebutton.core.api._
import com.google.gson.Gson
import scala.collection.mutable.HashMap
import collection.JavaConverters._
import scala.collection.JavaConversions._
import java.util.ArrayList
import org.bigbluebutton.conference.service.messaging.MessagingConstants
import org.bigbluebutton.core.messaging.Util

object ChatMessageToJsonConverter {

  val UNKNOWN = "unknown"
    
  def transformChatMessage(msg: Map[String, String]):HashMap[String, String] = {
    val res = new HashMap[String, String]
    res += "chat_type"       -> msg.get(ChatKeyUtil.CHAT_TYPE).getOrElse(UNKNOWN)
		res += "from_userid"     -> msg.get(ChatKeyUtil.FROM_USERID).getOrElse(UNKNOWN)
		res += "from_username"   -> msg.get(ChatKeyUtil.FROM_USERNAME).getOrElse(UNKNOWN)
		res += "from_color"      -> msg.get(ChatKeyUtil.FROM_COLOR).getOrElse(UNKNOWN)
		res += "from_time"       -> msg.get(ChatKeyUtil.FROM_TIME).getOrElse(UNKNOWN)   
		res += "from_tz_offset"  -> msg.get(ChatKeyUtil.FROM_TZ_OFFSET).getOrElse(UNKNOWN)
		res += "from_lang"       -> msg.get(ChatKeyUtil.FROM_LANG).getOrElse(UNKNOWN)
		res += "to_userid"       -> msg.get(ChatKeyUtil.TO_USERID).getOrElse(UNKNOWN)
		res += "to_username"     -> msg.get(ChatKeyUtil.TO_USERNAME).getOrElse(UNKNOWN)
		res += "message"         -> msg.get(ChatKeyUtil.MESSAGE).getOrElse(UNKNOWN)
  
    res
  }
  
  def getChatHistoryReplyToJson(msg: GetChatHistoryReply):String = {	
    val payload = new java.util.HashMap[String, Any]()
    payload.put(Constants.MEETING_ID, msg.meetingID)
    payload.put(Constants.REQUESTER_ID, msg.requesterID)
  	
  	val collection = new ArrayList[java.util.Map[String, String]]();
  	  
  	msg.history.foreach(p => {
  	    collection.add(mapAsJavaMap(ChatMessageToJsonConverter.transformChatMessage(p)))
  	})
  	
  	payload.put(Constants.CHAT_HISTORY, collection)
    
    val header = Util.buildHeader(MessageNames.GET_CHAT_HISTORY_REPLY, msg.version, Some(msg.replyTo))
    Util.buildJson(header, payload)
  }  
  
  def sendPublicMessageEventToJson(msg: SendPublicMessageEvent):String = { 
    val payload = new java.util.HashMap[String, Any]()
    payload.put(Constants.MEETING_ID, msg.meetingID)
    payload.put(Constants.MESSAGE, mapAsJavaMap(ChatMessageToJsonConverter.transformChatMessage(msg.message)))    
	  
    val header = Util.buildHeader(MessageNames.SEND_PUBLIC_CHAT_MESSAGE, msg.version, None)       
    Util.buildJson(header, payload)
  }  
  
  def sendPrivateMessageEventToJson(msg: SendPrivateMessageEvent):String = {
    val payload = new java.util.HashMap[String, Any]()
    payload.put(Constants.MEETING_ID, msg.meetingID)
    payload.put(Constants.MESSAGE, mapAsJavaMap(ChatMessageToJsonConverter.transformChatMessage(msg.message)))    
	  
    val header = Util.buildHeader(MessageNames.SEND_PRIVATE_CHAT_MESSAGE, msg.version, None)	  
    Util.buildJson(header, payload) 
  }	  
}