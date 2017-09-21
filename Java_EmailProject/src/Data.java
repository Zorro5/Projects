/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wg8498_dm3544;

public class Data {
    
	boolean flag;
	String message;
        String from;
	String subject;
	String sentdate;
	
	Data(boolean f, String r, String sub, String d,String m)
	{
		flag = f;
		from = r;
		subject = sub;
		sentdate = d;
		message = m;
	}
	public void setFlag(boolean f) {
	flag = f;
	}
	public boolean getFlag() {
	return flag;
	}
        public void setFrom(String r) {
	from = r;
	}
	public String getFrom() {
	return from;
	}
	public void setSubject(String s) {
	subject = s;
	}
	public String getSubject() {
	return subject;
	}
	public void setSentdate(String d){
	sentdate = d;
	}
	
	public String getSentdate() {
	return sentdate;
	}
	 public void setMessage(String m) {
        message = m;
        }
        public String getMessage() {
        return message;
	}

  } 
      
     
    

