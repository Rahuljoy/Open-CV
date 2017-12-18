package com.example.WebCrawling;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.URL;

public class WebCrawling {

    public static void main(String[]args) throws Exception{

        try {
            URL my_url = new URL("http://www.imdb.com/chart/top");
            BufferedReader br = new BufferedReader(new InputStreamReader(my_url.openStream()));
            String strTemp = "";
            while(null != (strTemp = br.readLine())){
                System.out.println(strTemp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
