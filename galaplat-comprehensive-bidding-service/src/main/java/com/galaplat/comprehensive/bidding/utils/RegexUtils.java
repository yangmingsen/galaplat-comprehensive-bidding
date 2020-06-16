package com.galaplat.comprehensive.bidding.utils;

import org.apache.commons.lang3.StringUtils;

public class RegexUtils {
	/** 
	 * 转义正则特殊字符 （$()*+.[]?\^{},|） 
	 *  
	 * @param keyword 
	 * @return 
	 */  
	public static String escapeExprSpecialWord(String keyword) {  
	    if (StringUtils.isNotBlank(keyword)) {  
	        String[] fbsArr = { "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}","|"};  
	        
	        if (keyword.contains("\\")) {  
	        	keyword = "\\\\\\\\";  
	        }
	        
	        for (String key : fbsArr) {  
	            if (keyword.contains(key)) {  
	                keyword = keyword.replace(key, "\\\\" + key);  
	            }  
	            
	        }  
	    } 
	    keyword = keyword.replaceAll("'", "''");
	    return keyword = keyword.replaceAll("\r|\n", "");
	}  
	
	public static String toRegexStr(String keyword) {
		keyword = escapeExprSpecialWord(keyword);
		if (StringUtils.isNotBlank(keyword)) {
			String temp = keyword.replace("，", "|");
			temp = temp.replace(",", "|");
			if (!temp.endsWith("\\|") && !temp.equals("\\\\|") && temp.endsWith("|") ) {
				temp = temp.substring(0, temp.length() - 1);
			}
			
			if (temp.startsWith("|") ) {
				temp = temp.substring(1, temp.length());
			}
			
			if(StringUtils.isBlank(temp)) {
				return null;
			}
			return temp;

		}
		
		return null;
	}
	
	public static void main(String[] ages) {
		String sss="&#x6b64;&#x8bb0;&#x5f55;&#x5df2;&#x7ecf;&#x5b58;&#x5728;";
		String[] strs = sss.split("\\\\u");
        String returnStr = "";
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
        for (int i = 1; i < strs.length; i++) {
            returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
        }
        System.out.println(returnStr); 
	}
	
}
