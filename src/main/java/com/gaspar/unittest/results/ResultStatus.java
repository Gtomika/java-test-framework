package com.gaspar.unittest.results;

/** Egy tesztosztaly vagy teszmetodus futtatasanak lehetseges 3 eredmenye. */
public enum ResultStatus {
	SUCCESS, FAIL, INTERRUPTED;
	
	static String asString(ResultStatus status) {
		String s = null;
		switch (status) {
		case SUCCESS:
			s = "SUCCESS";
			break;
		case FAIL:
			s = "FAIL";
			break;
		case INTERRUPTED:
			s = "INTERRUPTED";
			break;
		}
		return s;
	}
}
