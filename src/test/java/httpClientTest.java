import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zyj on 16/10/22.
 */
public class httpClientTest {
    @Test
    public void httpGetTest() throws IOException{
        String cityId = "310100";
//        String cityId = "310100";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = "http://api.sit.ffan.com/ffan/v1/flashpay/zone?cityId="+cityId;
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);

//        System.out.println("响应码:" + response.getStatusLine().getStatusCode());
//        获取服务器响应内容
        String returnStr = EntityUtils.toString(response.getEntity());
        System.out.println("响应内容:" + returnStr);

//        转换成json对象
        JSONObject jsonObject = JSONObject.fromObject(returnStr);
        String code = jsonObject.getString("status");
        String message = jsonObject.getString("message");
        String strData = jsonObject.getString("data");
        System.out.println("code:"+code);
        System.out.println("message:"+message);
        System.out.println("data:"+strData);

        JSONObject jsonObjectData = JSONObject.fromObject(strData);
//        获取类目
        String strCategory = jsonObjectData.getString("cates");
        System.out.println("cates:"+strCategory);
//        获取门店
        String strStores = jsonObjectData.getString("stores");
        System.out.println("stores:"+strStores);
//        获取排序方式
        String strSorts = jsonObjectData.getString("sorts");
        System.out.println(strSorts);

//        类目校验
        assertFalse(strCategory.equals("[]"));
        assertTrue(strCategory.indexOf("全部")>0);
//        门店校验
        assertFalse(strStores.equals("[]"));
        assertTrue(strStores.indexOf("zyj测试门店")>0);
//        排序校验
        assertFalse(strSorts.equals("[]"));
        assertTrue(strSorts.indexOf("距离最近")>0);

//        释放连接
        httpGet.releaseConnection();
    }


    @Test
    public void httpPostTest() throws IOException{
        String postUrl = "http://api.sit.ffan.com/push/accounts";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("platform", "1"));
        params.add(new BasicNameValuePair("user_list", "[\"15000000000990856\"]"));
        params.add(new BasicNameValuePair("content", "ffapp_test"));
        params.add(new BasicNameValuePair("app_id", "1"));

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(postUrl);
        httpPost.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse response = httpclient.execute(httpPost);
        System.out.println(response.toString());

        HttpEntity entity = response.getEntity();
        String jsonStr = EntityUtils.toString(entity, "utf-8");
        System.out.println(jsonStr);

        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        String ret = jsonObject.getString("ret");
//        校验返回码,0-发送成功
        assertEquals(ret,"0");



        httpPost.releaseConnection();

    }

}
