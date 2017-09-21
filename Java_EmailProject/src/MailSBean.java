
package wg8498_dm3544;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Stateful;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.Address;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


@Stateful

public class MailSBean {

      @Resource(name="mail/JamesMailSession")
   private Session session;
   private static final String mailer = "JavaMailer";
   private Store store=null;
   private Folder folder = null;
   private Message [] messages=null;
   private ArrayList<Data> mmsg= new ArrayList<>();
   Message row;

    public ArrayList<Data> connectm(String login ,String passwd)
    {



          try {
              this.store=this.session.getStore("pop3");
          } catch (NoSuchProviderException ex) {
              Logger.getLogger(MailSBean.class.getName()).log(Level.SEVERE, null, ex);
          }
          try {
              this.store.connect("yyuj.sci.csueastbay.edu",login,passwd);
          } catch (MessagingException ex) {
              Logger.getLogger(MailSBean.class.getName()).log(Level.SEVERE, null, ex);
          }
          try {
              this.folder=this.store.getFolder("INBOX");
          } catch (MessagingException ex) {
              Logger.getLogger(MailSBean.class.getName()).log(Level.SEVERE, null, ex);
          }
          try {
              this.folder.open(Folder.READ_WRITE);
          } catch (MessagingException ex) {
              Logger.getLogger(MailSBean.class.getName()).log(Level.SEVERE, null, ex);
          }
          try {
              this.messages=folder.getMessages();
          } catch (MessagingException ex) {
              Logger.getLogger(MailSBean.class.getName()).log(Level.SEVERE, null, ex);
          }
		  mmsg.clear();
          for(int i=0; i<messages.length; i++)
           {

                        Address address[] = null;
              try {
                  address = messages[i].getFrom();
              } catch (MessagingException ex) {
                  Logger.getLogger(MailSBean.class.getName()).log(Level.SEVERE, null, ex);
              }
                        String sender = address[0].toString();
                        String timestamp = null;
              try {
                  timestamp = messages[i].getSentDate().toString();
              } catch (MessagingException ex) {
                  Logger.getLogger(MailSBean.class.getName()).log(Level.SEVERE, null, ex);
              }
                        String subject = null;
              try {
                  subject = messages[i].getSubject();
              } catch (MessagingException ex) {
                  Logger.getLogger(MailSBean.class.getName()).log(Level.SEVERE, null, ex);
              }
                        String body = null;
              try {
                  body = messages[i].getContent().toString();
              } catch (IOException | MessagingException ex) {
                  Logger.getLogger(MailSBean.class.getName()).log(Level.SEVERE, null, ex);
              }


          Data l=new Data(false,sender,subject,timestamp,body);

          mmsg.add(i,l);
      }
       // return mmsg;





    return mmsg;
    }


    public void sendMessage(String recipient, String sender, String subject,
           String data) {
     try {
       Message msg = new MimeMessage(session);
       msg.setFrom(new InternetAddress(sender,false));
       msg.setRecipients(Message.RecipientType.TO,
             InternetAddress.parse(recipient, false));
      Address add= new InternetAddress(recipient,false);
    //  Address[] addlist=null;
    //  addlist[0]=add;
       msg.setSubject(subject);
       Date timeStamp = new Date();
       msg.setText(data);
       msg.setHeader("X-Mailer", mailer);
       msg.setSentDate(timeStamp);
       Transport.send(msg);

     } catch (Exception e) { /**throw new EJBException(e.getMessage());*/
     }
   }


   /* public ArrayList<Data> getMsgs()  {
          try {
              folder=store.getFolder("INBOX");
          } catch (MessagingException ex) {
              Logger.getLogger(MailSBean.class.getName()).log(Level.SEVERE, null, ex);
          }
          try {
              folder.open(Folder.READ_WRITE);
          } catch (MessagingException ex) {
              Logger.getLogger(MailSBean.class.getName()).log(Level.SEVERE, null, ex);
          }
          try {
              messages=folder.getMessages();
          } catch (MessagingException ex) {
              Logger.getLogger(MailSBean.class.getName()).log(Level.SEVERE, null, ex);
          }
          for(int i=0; i<messages.length; i++)
           {

                        Address address[] = null;
              try {
                  address = messages[i].getFrom();
              } catch (MessagingException ex) {
                  Logger.getLogger(MailSBean.class.getName()).log(Level.SEVERE, null, ex);
              }
                        String sender = address[0].toString();
                        String timestamp = null;
              try {
                  timestamp = messages[i].getSentDate().toString();
              } catch (MessagingException ex) {
                  Logger.getLogger(MailSBean.class.getName()).log(Level.SEVERE, null, ex);
              }
                        String subject = null;
              try {
                  subject = messages[i].getSubject();
              } catch (MessagingException ex) {
                  Logger.getLogger(MailSBean.class.getName()).log(Level.SEVERE, null, ex);
              }
                        String body = null;
              try {
                  body = messages[i].getContent().toString();
              } catch (IOException | MessagingException ex) {
                  Logger.getLogger(MailSBean.class.getName()).log(Level.SEVERE, null, ex);
              }


          Data l=new Data(false,sender,subject,timestamp,body);

          mmsg.add(i,l);
      }
        return mmsg;

	}*/

    public String display(Data message) {
     StringBuilder sb = new StringBuilder();
     try {
    // message.writeTo(buffer);
     sb.append("Sender:  ");
     sb.append(message.from);
     sb.append("   ");
     sb.append("Subject:  ");
     sb.append(message.subject);
     sb.append("  ");
     sb.append("Content:  ");
     sb.append(message.message);
     sb.append("     ");


     // m=message.getContent().toString();
     }catch(Exception e) {System.out.println(e.toString());
     }
     return sb.toString();
   }


    public void remov(int i) throws MessagingException  {
  try {

            if (i < mmsg.size()) {
               messages[i].setFlag(Flags.Flag.DELETED, true);
                //messageslist.get(i).setExpunged(true);
               // msglist.get(i).setFlag(Flags.Flag.DELETED,true);
               mmsg.remove(i);
               folder.close(true);
            }
        } catch (Exception e) {
            //System.out.println(e.toString());
        }


            }


    public void disconnect() throws MessagingException
    {
          try {
             if( folder.isOpen()){
              folder.close(true);}
           if( store.isConnected()){
               store.close();}}
           catch (MessagingException ex) {
              Logger.getLogger(MailSBean.class.getName()).log(Level.SEVERE, null, ex);
          }

    }
}
