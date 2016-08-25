package kr.co.d2net.netbro.common.xml.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang.StringUtils;

public class DateAdapter extends XmlAdapter<String, Date> {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String marshal(Date v) throws Exception {
    	if(v == null) return "";
    	else return dateFormat.format(v);
    }

    @Override
    public Date unmarshal(String v) throws Exception {
    	if(StringUtils.isNotBlank(v))
    		return dateFormat.parse(v);
    	else return null;
    }
    
}
