package com.logansrings.booklibrary.bean;

import com.logansrings.booklibrary.app.ApplicationUtilities;
import com.logansrings.booklibrary.domain.User;
import com.logansrings.booklibrary.notification.Notification;
import com.logansrings.booklibrary.notification.Severity;
import com.logansrings.booklibrary.notification.Type;

/**
 * Backs the login.jsp page
 * @author mark
 */
public class UserBean {
     private String userName = "";
     private String password = "";
     private User loggedInUser;

     public String createAccount() {
           if (incomplete()) {
                 ApplicationUtilities.createFacesError("loginForm",
                             "User Name and Password are required", "");
                 return "failure";
           }

           try {
                 User user = User.create(userName, password);
                 if (validUser(user)) {
                       loggedInUser = user;
                       return "success";
                 } else {
                       ApplicationUtilities.createFacesError("loginForm",
                                   "Unable to create account:",
user.getContext());
                       return "failure";
                 }

           } catch (Exception e) {
                 Notification.newNotification(this, "UserBean.createAccount()",
                             "failed", "", Type.TECHNICAL, Severity.ERROR, e);
                 ApplicationUtilities.createFacesError("loginForm",
                             "Technical error prevented user creation", "");
                 return "failure";
           }
     }

     public String login() {
           if (incomplete()) {
                 ApplicationUtilities.createFacesError("loginForm",
                             "User Name and Password are required", "");
                 return "failure";
           }
           try {
                 User user = User.find(userName, password);
                 if (validUser(user)) {
                       loggedInUser = user;
                       return "success";
                 } else {
                       ApplicationUtilities.createFacesError("loginForm",
                                   "Unable to login:", user.getContext());
                       return "failure";
                 }
           } catch (Exception e) {
                 Notification.newNotification(this, "UserBean.login()",
                             "failed", "", Type.TECHNICAL, Severity.ERROR,
e);
                 ApplicationUtilities.createFacesError("loginForm",
                             "Technical error prevented user login", "");
                 return "failure";
           }
     }

     boolean validUser(User user) {
           return user.isValid();
     }

     public void logout() {
           clear();
     }

     private void clear() {
           userName = "";
           password = "";
           loggedInUser = null;
     }

     private boolean incomplete() {
           return ApplicationUtilities.isEmpty(userName, password);
     }

     public String getUserName() { return userName; }
     public void setUserName(String newValue) { userName = newValue; }

     public String getPassword() { return password; }
     public void setPassword(String newValue) { password = newValue; }

     public User getUser() {
           return loggedInUser;
     }

     public boolean isLoggedIn() {
           return loggedInUser != null;
     }

     public boolean isNotLoggedIn() {
           return loggedInUser == null;
     }


}				