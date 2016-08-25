package kr.co.d2net.netbro.common.xml.converter;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang.StringUtils;

public class UrlEncodeAdapter extends XmlAdapter<String, String> {
	
    @Override
    public String marshal(String v) throws Exception {
    	if(StringUtils.isNotBlank(v))
    		return URLEncoder.encode(v, "utf-8");
    	else return "";
    }

    @Override
    public String unmarshal(String v) throws Exception {
    	if(StringUtils.isNotBlank(v))
    		return URLDecoder.decode(v, "utf-8");
    	else return "";
    }
    
}
