/*
 * Copyright ENCO C&S.,LTD.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of ENCO C&S.,LTD. ("Confidential Information").
 */
/**
 * @file TestServlet.java
 * @brief
 * 간략 설명을 작성하시오.
 * @section Major History
 * <pre>
 *   - enco | 2022. 6. 3. | First Created
 * </pre>
 */
/**
 * @namespace com.encocns.servlet
 * @brief 패키지명에 대해 간략 설명을 작성하시오.
 */
package com.encocns.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;

/**
 * @brief 간략 설명을 작성하시오.
 * @details
 *
 *          <pre>
 * 상세 설명을 작성하시오.
 *          </pre>
 *
 * @author : enco
 * @date : 2022. 6. 3.
 * @version : 1.0.0
 */
@WebServlet("/test")
public class TestServlet extends HttpServlet {

    private static final long serialVersionUID = 5516559069471483540L;

//    public void init(ServletConfig config) throws ServletException {
//        System.out.println("init() 실행됨!");
//    }
//
//    public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
//        System.out.println("service() 실행됨!");
//    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setCharacterEncoding("UTF-8");
        res.setContentType("text/html; charset=utf-8");
        res.setCharacterEncoding("UTF-8");

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            List < NameValuePair > form = new ArrayList < > ();
            form.add(new BasicNameValuePair("client_id", "login-app"));
            form.add(new BasicNameValuePair("grant_type", "password"));
            form.add(new BasicNameValuePair("username", "user1"));
            form.add(new BasicNameValuePair("password", "1234"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);

            HttpPost httpPost = new HttpPost("http://sso.shinhan.com:8180/auth/realms/TestRealm/protocol/openid-connect/token");
            httpPost.setEntity(entity);
            System.out.println("Executing request " + httpPost.getRequestLine());

            // Create a custom response handler
            ResponseHandler <String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity responseEntity = response.getEntity();
                    return responseEntity != null ? EntityUtils.toString(responseEntity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            String responseBody = httpclient.execute(httpPost, responseHandler);
            System.out.println("----------------------------------------");
            JsonObject resJson = JsonUtil.toJsonObject(responseBody);
//            JsonElement je = resJson.get(responseBody);
            System.out.println(resJson.get("access_token").getAsString());
            res.addHeader("res_access_token", resJson.get("access_token").getAsString());
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /*
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {

            HttpGet request = new HttpGet("https://www.naver.com");

            // add request headers
            request.addHeader("custom-key", "test");
//            request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

            CloseableHttpResponse response = httpClient.execute(request);

            try {

                // Get HttpResponse Status
                System.out.println(response.getProtocolVersion());              // HTTP/1.1
                System.out.println(response.getStatusLine().getStatusCode());   // 200
                System.out.println(response.getStatusLine().getReasonPhrase()); // OK
                System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    String result = EntityUtils.toString(entity);
                    System.out.println(result);
                }

            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
        */

        req.getRequestDispatcher("/sample/test.jsp").forward(req, res);
    }
}