package dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author zhanggeng
 * @date 2021/8/13 15:50
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Hello implements Serializable {
    private String message;
    private String description;
}
