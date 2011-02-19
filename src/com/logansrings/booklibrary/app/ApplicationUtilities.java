package com.logansrings.booklibrary.app;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Miscellaneous utilities used throughout the application.
 * @author mark
 *
 */
public class ApplicationUtilities {

     public static Object createObjectForClassName(String className) {
           try {
                 return Class.forName(className).newInstance();
           } catch (Exception e) {
                 System.out.println(
                		 "ApplicationUtilities().createObjectForClassName() " +
                             "failed for " + className + " " +
                             e.getMessage());
                 return null;
           }
     }

     /**
      * @param strings
      * @return true if any strings are null or zero length
      */
     public static boolean isEmpty(String ... strings) {
           boolean isEmpty = false;
           for (String value : strings) {
                 if (value == null || value.length() == 0) {
                       isEmpty = true;
                 }
           }
           return isEmpty;
     }

     /**
     * @param list
     * @return true if list is null or empty
     */
    public static boolean isEmpty(List<?> list) {
    	 return (list == null ? true : list.isEmpty());
     }

     /**
      * @param strings
      * @return true if all strings are not null and > zero length
      */
     public static boolean isNotEmpty(String ... strings) {
           return ! isEmpty(strings);
     }

     public static String arrayToCommaSeparatedString(String[] stringArray) {
         StringBuilder result = new StringBuilder();
         int count = 0;

         for (String value : stringArray) {
           if (count > 0) {
                 result.append(",");
           }
           result.append(value);
           count++;
         }
         return result.toString();
     }

     public static String listToString(List list) {
           StringBuilder result = new StringBuilder();
         int count = 0;

           for (Object obj : list) {
           if (count > 0) {
                 result.append("|");
           }
                 result.append(obj.toString());
                 count++;
           }
           return result.toString();
     }

     public static String arrayToFormattedCommaSeparatedString(Object[] anArray) {
         StringBuilder result = new StringBuilder();
         int count = 0;

         for (Object value : anArray) {
           if (count > 0) {
                 result.append(",");
           }
           if (value instanceof String) {
                 result.append("'" + value + "'");
           } else if (value instanceof Long) {
                 result.append(value);
           }
           count++;
         }
         return result.toString();
     }

     public static void createFacesError(String formId, String summary, String detail) {
           getFacesContext().addMessage(
                       formId, createFacesMessageError(summary, detail));
     }

     private static FacesMessage createFacesMessageError(String summary, String detail) {
           FacesMessage message = new FacesMessage();
           message.setDetail(detail);
           message.setSummary(summary);
           message.setSeverity(FacesMessage.SEVERITY_ERROR);
           return message;
     }

     private static FacesContext getFacesContext() {
           return FacesContext.getCurrentInstance();
     }


}