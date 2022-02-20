package com.opentext.explore.util;

public class Hash {

	/**
	 * adapted from String.hashCode()
	 * @param string
	 * @return
	 * @see https://stackoverflow.com/questions/1660501/what-is-a-good-64bit-hash-function-in-java-for-textual-strings
	 */
	public static long hash(String string) {
	  long h = 1125899906842597L; // prime
	  int len = string.length();

	  for (int i = 0; i < len; i++) {
	    h = 31*h + string.charAt(i);
	  }
	  return h;
	}
}