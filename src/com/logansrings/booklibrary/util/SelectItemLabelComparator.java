package com.logansrings.booklibrary.util;

import java.util.Comparator;

import javax.faces.model.SelectItem;

public class SelectItemLabelComparator implements Comparator<SelectItem> {  
	   public int compare(SelectItem s1, SelectItem s2) {  
	        return s1.getLabel().compareTo(s2.getLabel());  
	    }  
	} 
