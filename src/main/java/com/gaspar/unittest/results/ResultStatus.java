package com.gaspar.unittest.results;

/** Egy teszmetodus futtatasanak lehetseges 3 eredmenye. */
public enum ResultStatus {
	SUCCESS, FAIL, INTERRUPTED;
	
	static String asString(ResultStatus status) {
		String s = null;
		switch (status) {
		case SUCCESS:
			s = "success";
			break;
		case FAIL:
			s = "fail";
			break;
		case INTERRUPTED:
			s = "unexpected exception";
			break;
		}
		return s;
	}
}
