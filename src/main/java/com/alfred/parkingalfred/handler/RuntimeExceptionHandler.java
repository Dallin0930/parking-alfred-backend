package com.alfred.parkingalfred.handler;

import com.alfred.parkingalfred.exception.EmployeeNotExistedException;
import com.alfred.parkingalfred.utils.ResultVOUtil;
import com.alfred.parkingalfred.vo.ResultVO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class RuntimeExceptionHandler {

  @ExceptionHandler(value = EmployeeNotExistedException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResultVO handleEmployeeNotExistedException(EmployeeNotExistedException e){
    return ResultVOUtil.error(e.getCode(),e.getMessage());
  }
}
