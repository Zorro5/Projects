/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wg8498_dm3544;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.RequestScoped;
import javax.ejb.EJB;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.naming.*;
import java.util.ArrayList;
import java.util.Iterator;




@ManagedBean(name="LoginManagedBean")
@SessionScoped
public class LoginManagedBean {
    @EJB MailSBean email;
   Data rows;
   ArrayList<String> users = new ArrayList<String>();

 //*private  MailSBean m;
public String login;
String password;
String checkmail;
Boolean valid=false;
String sender,subj,body;
Data data;
int loginflag;
 ArrayList<Data> messages;


   public LoginManagedBean() {
    }
    public void setLogin(String n)
    {
        login=n;
    }
    public String getLogin()
    {
        return login;
    }
    public void setPassword(String p)
    {
        password=p;
    }
    public String getPassword()
    {
        return password;
    }
     public void setBody(String p)
    {
        body=p;
    }
    public String getBody()
    {
        return body;
    }

    public void setValidation(Boolean b)
    {
        valid=b;
    }
    public Boolean  getValidation()
    {
        return valid;
    }
     public void setSender(String s)
    {
        sender=s;
    }
    public String  getSender()
    {
        return sender;
    }

     public void setSubject(String s)
    {
        subj=s;
    }
    public String  getSubject()
    {
        return subj;
    }
    public void setData(Data s)
    {
        data=s;
    }
    public Data  getData()
    {
        return data;
    }



    public String calllogin()
    {
        //(login != null) && (password != null) && 
		
		users.add("csuhduke");
		users.add("csuhduke1");
		users.add("csuhduke2");
		users.add("csuhduke3");
		users.add("csuhduke4");
		users.add("csuhduke5");
		users.add("csuhduke6");
		users.add("csuhduke7");
		users.add("csuhduke8");
		users.add("csuhduke9");
		if( users.contains(login) && password.equals("cs6522"))
    {


        messages = email.connectm(login, password);
      valid=true;
         return "Welcome back "+login;

    } else {
        valid=false;
        return "Wrong login or password: "+login+"/"+password;

    }

    }
    public String sendMsg()
    {
    email.sendMessage(sender, login, subj, body);
    return "loginf.xhtml";
    }

    public ArrayList<Data> beanMessage(){
      //Message[] msg;
   return  messages;

    }
    public void disp(Data row){
        rows=row;

    }

    public String display(){
        String dispmsg;
        dispmsg=email.display(rows);
        return dispmsg;

    }

    public void check() throws MessagingException {
         Iterator<Data> itr;
        // itr=messages.iterator();
         int num= messages.size();
         if(num==0){return;}
         for(int i=num-1;i>=0;i--)
         {
             Data data2=messages.get(i);
             if(data2.flag){
                 email.remov(i);
                 //messages.remove(data2);


             }
         }
       // email.remove();
     itr=messages.iterator();
      while(itr.hasNext())
      {
          Data data1 =(Data)itr.next();
          if(data1.flag){
            int i =messages.indexOf(data1);
            if(i!=-1){
                messages.remove(data1);

            }
          }
      }
    }

    public void disconnect() throws MessagingException{
        email.disconnect();
    }







}