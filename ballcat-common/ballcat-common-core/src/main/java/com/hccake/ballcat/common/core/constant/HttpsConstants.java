package com.hccake.ballcat.common.core.constant;

import com.hccake.ballcat.common.core.https.CompatibleSSLFactory;
import com.hccake.ballcat.common.core.https.SSLSocketFactoryInitException;
import lombok.experimental.UtilityClass;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author lingting
 */
@UtilityClass
@SuppressWarnings("java:S4830")
public class HttpsConstants {

	public static final String SSL = "SSL";

	public static final String SSL_V2 = "SSLv2";

	public static final String SSL_V3 = "SSLv3";

	public static final String TLS = "TLS";

	public static final String TLS_V1 = "TLSv1";

	public static final String TLS_V11 = "TLSv1.1";

	public static final String TLS_V12 = "TLSv1.2";

	public static final String DALVIK = "dalvik";

	public static final String VM_NAME = "java.vm.name";

	/**
	 * Android低版本不重置的话某些SSL访问就会失败
	 */
	private static final String[] ANDROID_PROTOCOLS = { SSL_V3, TLS_V1, TLS_V11, TLS_V12 };

	/**
	 * 默认信任全部的域名校验器
	 */
	@SuppressWarnings("java:S5527")
	public static final HostnameVerifier HOSTNAME_VERIFIER = (s, sslSession) -> true;

	public static final KeyManager[] KEY_MANAGERS = null;

	public static final X509TrustManager TRUST_MANAGER;

	public static final TrustManager[] TRUST_MANAGERS;

	static {
		TRUST_MANAGER = new X509TrustManager() {

			@Override
			public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
				//
			}

			@Override
			public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
				//
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		};
		TRUST_MANAGERS = new TrustManager[] { TRUST_MANAGER };
	}

	/**
	 * 默认的SSLSocketFactory，区分安卓
	 */
	public static final SSLSocketFactory SSF;

	static {
		try {
			if (DALVIK.equalsIgnoreCase(System.getProperty(VM_NAME))) {
				// 兼容android低版本SSL连接
				SSF = new CompatibleSSLFactory(ANDROID_PROTOCOLS);
			}
			else {
				SSF = new CompatibleSSLFactory();
			}
		}
		catch (KeyManagementException | NoSuchAlgorithmException e) {
			throw new SSLSocketFactoryInitException("ssf 创建失败!", e);
		}
	}

}
