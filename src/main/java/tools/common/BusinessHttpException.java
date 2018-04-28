package tools.common;


 /**
  * 业务异常
 * Created by Joey on 2017/3/8 0006.
 */
public class BusinessHttpException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int code;

    private String message;

    public BusinessHttpException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
