package util;

import com.logansrings.booklibrary.util.Encrypting;

public class EncryptingMock extends Encrypting {

	String encryptReturnValue;
	boolean encryptCalled;

	@Override
	public synchronized String encrypt(String aString) {
		encryptCalled = true;
		return encryptReturnValue;
	}
}
