package com.WhiteCloud.SpringBootTest.Utils.GlobalException;

import com.WhiteCloud.SpringBootTest.Model.JsonResult;
import com.WhiteCloud.SpringBootTest.Model.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RestController
public class GlobalException {
    // 打印log
    private static final Logger logger = LoggerFactory.getLogger(GlobalException.class);

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public JsonResult handleHttpMessageNotReadableException(
            MissingServletRequestParameterException ex) {
        logger.error("缺少请求参数，{}", ex.getMessage());
        return new JsonResult("400", "缺少必要的请求参数");
    }

    @ExceptionHandler(MyException.class)
    public JsonResult MyException(MyException ex){
        String code = ex.getCode();
        String msg = ex.getMsg();
        logger.error(ex.getMsg());
        JsonResult jsonResult = new JsonResult(code,msg);
        return jsonResult;
    }
}
