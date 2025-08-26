package uz.news.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class ApiResponse<T> {
    @JsonIgnore
    private int code;
    private String message;
    private T object;


    public ApiResponse(int code, T object) {
        this.code = code;
        this.object = object;
    }

    public ApiResponse(int code) {
        this.code = code;
    }

    public ApiResponse(String message, int code) {
        this.code = code;
        this.message = message;
    }


    public ApiResponse(int code, String message, T object) {
        this.code = code;
        this.message = message;
        this.object = object;
    }

    public ApiResponse(int code,String message){
        this.code = code;
        this.message=message;
    }


    public static <T> ResponseEntity<ApiResponse<T>> controller(ApiResponse<T> apiResponse) {
        return ResponseEntity.status(apiResponse.code).body(apiResponse);
    }

}
