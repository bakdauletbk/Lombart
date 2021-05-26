package kz.pillikan.lombart.common.utils;


public class EpayConstants {

    public static final int TIMEOUT_CONNECTION = 90000;
    public static final int EPAY_PAY_REQUEST = 1;
    public static final int EPAY_PAY_SUCCESS = 1;
    public static final int EPAY_PAY_FAILURE = 2;

    public static final String LOG_TAG = "EPAY_SDK";
    public static final String EPAY_POST_URL = "https://epay.kkb.kz/jsp/process/logon.jsp";
    public static final String EPAY_TEST_POST_URL = "https://testpay.kkb.kz/jsp/process/logon.jsp";

    public static final String EPAY_FAILURE_BACK_LINK = "https://cert.smartideagroup.kz/v1/pay/fail";
    public static final String EPAY_BACK_LINK = "https://cert.smartideagroup.kz/";
    public static final String EXTRA_POST_LINK_VALUE = "https://cert.smartideagroup.kz/v1/pay/success";

}
